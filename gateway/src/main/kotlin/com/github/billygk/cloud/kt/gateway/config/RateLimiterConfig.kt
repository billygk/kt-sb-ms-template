package com.github.billygk.cloud.kt.gateway.config

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import reactor.core.publisher.Mono
import java.security.Principal


@Configuration
class RateLimiterConfig {

    private var env: Environment? = null

    // logger
    private val log = org.slf4j.LoggerFactory.getLogger(javaClass)

    fun RateLimiterConfig(env: Environment?) {
        this.env = env
    }

    @Bean
    fun keyResolver(): KeyResolver {
        return KeyResolver { exchange ->
            val principalName = exchange.getPrincipal<Principal>()
                .flatMap { p -> Mono.just(p.name) }
                .defaultIfEmpty("ANONYMOUS")

            val ipAddress = exchange.request.remoteAddress?.address
            return@KeyResolver principalName.map {
                log.info("principalName: $it")
                log.info("ipAddress: $ipAddress")
                "$it:$ipAddress"
            }
        }
    }

}