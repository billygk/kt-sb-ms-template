package com.github.com.billygk.cloud.kt.service_b.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Service
class QuoteService (@Autowired val webClient: WebClient) {
    private val log = org.slf4j.LoggerFactory.getLogger(QuoteService::class.java)

    private val forismaticQuoteUrl = "http://api.forismatic.com/api/1.0/?method=getQuote&format=json&lang=en"
    private val quotableQuoteUrl = "https://api.quotable.io/random"

    // get fortismatic quote from API: http://api.forismatic.com/api/1.0/?method=getQuote&format=json&lang=en
    fun getForismaticQuote(): GenericQuote? =
        webClient.get()
            .uri(forismaticQuoteUrl)
            .retrieve()
            .bodyToMono<ForismaticQuote>()
            .block()
            ?.toGenericQuote()

    // get quotable quote from API: https://api.quotable.io/random
    fun getQuotableQuote(): GenericQuote? =
        webClient.get()
            .uri(quotableQuoteUrl)
            .retrieve()
            .bodyToMono<QuotableQuote>()
            .block()
            ?.toGenericQuote()

}