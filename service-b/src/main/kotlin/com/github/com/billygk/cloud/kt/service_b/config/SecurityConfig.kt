package com.github.com.billygk.cloud.kt.service_b.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import java.util.stream.Collectors


@EnableWebSecurity
@Configuration
class SecurityConfig {
    private val log = org.slf4j.LoggerFactory.getLogger(SecurityConfig::class.java)

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        val publicUrls = listOf(
            "/service-a",
            "/actuator/*",
            "/swagger-ui/*",
            "/swagger-ui.html",
            "/swagger-resources/*",
            "/v3/api-docs/*"
        )
        val localHostV4 = "127.0.0.1"
        val localHostV6 = "0:0:0:0:0:0:0:1"

        http.authorizeHttpRequests { authRegistry ->
            // Allow public urls
            publicUrls.forEach { url ->
                authRegistry.requestMatchers(url).permitAll()
            }
            authRegistry.anyRequest().authenticated()
        }

        http.oauth2ResourceServer { obj: OAuth2ResourceServerConfigurer<HttpSecurity?> -> obj.jwt() }
            .sessionManagement { sessionManagement: SessionManagementConfigurer<HttpSecurity?> ->
                sessionManagement.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            }

        http.csrf { csrfRegistry ->
            csrfRegistry.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers(publicUrls.toString())
                .ignoringRequestMatchers(localHostV4, localHostV6)
        }

        return http.build()
    }

    /*
      Spring boot security prefixes roles with "ROLE_" this Bean performs that conversion,
      so we can use spring boot security transparently.
     */
    @Bean
    fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val jwtConverter = JwtAuthenticationConverter()
        jwtConverter.setJwtGrantedAuthoritiesConverter(RealmRoleConverter())
        return jwtConverter
    }

}

class RealmRoleConverter : Converter<Jwt?, Collection<GrantedAuthority?>?> {
    override fun convert(jwt: Jwt): Collection<GrantedAuthority> {
        val realmAccess = jwt.claims["realm_access"] as Map<String, List<String>>
        return realmAccess["roles"]!!.stream()
            .map { roleName: String -> "ROLE_$roleName" }
            .map { role: String? ->
                SimpleGrantedAuthority(
                    role
                )
            }
            .collect(Collectors.toList())
    }
}
