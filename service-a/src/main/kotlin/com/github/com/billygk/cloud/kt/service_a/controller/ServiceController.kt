package com.github.com.billygk.cloud.kt.service_a.controller

import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import java.time.Instant


@RestController
@RequestMapping("/service-a")
class ServiceController {
    // Init logger
    private val log = org.slf4j.LoggerFactory.getLogger(ServiceController::class.java)

    @GetMapping
    fun getHome(@RequestHeader headers: MultiValueMap<String, String>, principal: Principal?): ResponseEntity<Map<String, String>> {
        log.debug("=> /service-a")

        val response = buildMap {
            put("path", "/service-a")
            put("timestamp", Instant.now().toString())
            principal?.let {
                put("Principal", it.name)
            }
        }

        return ResponseEntity.ok(response)
    }

    @GetMapping(path = ["/auth-user1"])
    fun getAuthUser1( @RequestHeader headers: MultiValueMap<String, String>
    ): ResponseEntity<Map<String, String>> {
        log.debug("=> /service-a/auth-user1")
        val response: MutableMap<String, String> = HashMap()
        response["path"] = "/service-a/auth-user1"
        response["timestamp"] = Instant.now().toString()
        response.putAll(headers.toSingleValueMap())
        return ResponseEntity.ok(response)
    }

    @GetMapping(path = ["/auth-user2"])
    fun getAuthUser2(@RequestHeader headers: MultiValueMap<String, String>
    ): ResponseEntity<Map<String, String>> {
        log.debug("=> /service-a/auth-user2")
        val response = mutableMapOf(
            "path" to "/service-a/auth-user2",
            "timestamp" to Instant.now().toString()
        )
        response.putAll(headers.toSingleValueMap())
        return ResponseEntity.ok(response)
    }

}