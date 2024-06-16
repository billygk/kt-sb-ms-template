package com.github.com.billygk.cloud.kt.service_b.controller

import com.github.com.billygk.cloud.kt.service_b.service.GenericQuote
import com.github.com.billygk.cloud.kt.service_b.service.QuoteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
//import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import java.time.Instant

@RestController
@RequestMapping("/service-b")
class ServiceBController (@Autowired val quoteService: QuoteService) {
    private val log = org.slf4j.LoggerFactory.getLogger(ServiceBController::class.java)

    @GetMapping
    fun getHome(@RequestHeader headers: MultiValueMap<String, String>, principal: Principal?): ResponseEntity<Map<String, String>> {
        log.info("/service-b")
        val response = buildMap {
            put("path", "/service-b")
            put("timestamp", Instant.now().toString())
            principal?.let {
                put("Principal", it.name)
            }
        }
        return ResponseEntity.ok(response)
    }

    @GetMapping(path = ["/quote"])
    @Secured("ADMIN","ROLE_ROLL1")
    fun getQuote(): ResponseEntity<GenericQuote> {
        log.info("=> /service-b/quote")
        var response: GenericQuote? = null
        response = quoteService.getForismaticQuote()
        if (response == null) {
            log.info("using fallback QuoteProvider")
            response = quoteService.getQuotableQuote()
        }

        return ResponseEntity.ok(response)
    }

    @GetMapping(path = ["/auth-roll1"])
    @Secured("ADMIN","ROLE_ROLL1")
    fun getAuthUser1( @RequestHeader headers: MultiValueMap<String, String>, principal: Principal?
    ): ResponseEntity<Map<String, String>> {
        log.debug("=> /service-a/auth-roll1")
        val response = mutableMapOf<String, String>(
            "path" to "/service-a/auth-roll1",
            "timestamp" to Instant.now().toString()
        )
        if (principal != null) {
            response["Principal"] = principal.name
        }
        response.putAll(headers.toSingleValueMap())
        return ResponseEntity.ok(response)
    }

}