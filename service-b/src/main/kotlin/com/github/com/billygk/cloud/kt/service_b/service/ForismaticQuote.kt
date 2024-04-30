package com.github.com.billygk.cloud.kt.service_b.service



data class ForismaticQuote(

    /* Sample response from http://api.forismatic.com/api/1.0/?method=getQuote&format=json&lang=en
        "quoteText": "If you want things to be different, perhaps the answer is to become different yourself. ",
        "quoteAuthor": "Norman Peale",
        "senderName": "",
        "senderLink": "",
        "quoteLink": "http://forismatic.com/en/4ce82da60b/"
     */

    var quoteText: String,
    var quoteAuthor: String,
    var senderName: String,
    var senderLink: String,
    var quoteLink: String
)

// convert to generic Quote
fun ForismaticQuote.toGenericQuote(): GenericQuote {
    return GenericQuote(
        quoteText = this.quoteText,
        quoteAuthor = this.quoteAuthor
    )
}

