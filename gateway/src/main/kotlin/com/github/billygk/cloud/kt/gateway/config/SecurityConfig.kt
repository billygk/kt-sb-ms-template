package com.github.billygk.cloud.kt.gateway.config

import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.http.server.ServerHttpResponse
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity.*
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import org.springframework.web.server.WebSession
import reactor.core.publisher.Mono
import java.net.URI


@EnableWebFluxSecurity
class SecurityConfig {

    private val log = org.slf4j.LoggerFactory.getLogger(SecurityConfig::class.java)

    @Bean
    fun springSecurityFilterChain(
        http: ServerHttpSecurity,
        clientRegistrationRepository: ReactiveClientRegistrationRepository?
    ): SecurityWebFilterChain {
        return http
            .authorizeExchange { exchange: AuthorizeExchangeSpec ->
                exchange //                        .pathMatchers("/", "/*.css", "/*.js", "/favicon.ico").permitAll()
                    .pathMatchers("/actuator/*").permitAll()
                    .pathMatchers("/service-a").permitAll()
                    .pathMatchers("/service-b").permitAll()
                    .pathMatchers("/service-c").permitAll()
                    .pathMatchers("/logout")
                    .permitAll() //                        .pathMatchers(HttpMethod.GET, "/books/**").permitAll()
                    .anyExchange().authenticated()
            } //                .exceptionHandling(exceptionHandling -> exceptionHandling
            //                        .authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED)))

            .oauth2Login(Customizer.withDefaults())

            .logout { logout: LogoutSpec ->
                logout.logoutSuccessHandler(
                    oidcLogoutSuccessHandler(
                        clientRegistrationRepository!!
                    )
                )
            }
            
            .csrf { csrf: CsrfSpec ->
                csrf.csrfTokenRepository(
                    CookieServerCsrfTokenRepository.withHttpOnlyFalse()
                )
            }
            .build()
    }

    @Bean
    fun authorizedClientRepository(): ServerOAuth2AuthorizedClientRepository {
        return WebSessionServerOAuth2AuthorizedClientRepository()
    }

    fun oidcLogoutSuccessHandler(clientRegistrationRepository: ReactiveClientRegistrationRepository): ServerLogoutSuccessHandler {
        val oidcLogoutSuccessHandler = OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository)
        oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}")
        return oidcLogoutSuccessHandler
    }

    @Bean
    fun csrfWebFilter(): WebFilter {
        // Required because of https://github.com/spring-projects/spring-security/issues/5766
        return WebFilter { exchange: ServerWebExchange, chain: WebFilterChain ->
            exchange.response.beforeCommit {
                Mono.defer {
                    val csrfToken: Mono<CsrfToken>? =
                        exchange.getAttribute<Mono<CsrfToken>>(
                            CsrfToken::class.java.getName()
                        )
                    csrfToken?.then() ?: Mono.empty()
                }
            }
            chain.filter(exchange)
        }
    }

}