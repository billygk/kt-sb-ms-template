package com.github.com.billygk.cloud.kt.service_b.service

data class QuotableQuote(
    /* Sample response from https://api.quotable.io/random
        "_id": "5f9d9d9d9d9d9d9d9d9d9d9d",
        "content": "The only way to do great work is to love what you do.",
        "author": "Steve Jobs",
        "tags": [
            "inspirational",
            "success"
        ],
        "authorSlug": "steve-jobs",
        "length": 56,
        "dateAdded": "2020-09-09",
        "dateModified": "2023-04-14"
     */
    var content: String,
    var author: String,
    var tags: List<String>,
    var authorSlug: String,
    var length: Int,
    var dateAdded: String,
    var dateModified: String
)

// convert to generic Quote
fun QuotableQuote.toGenericQuote(): GenericQuote {
    return GenericQuote(
        quoteText = this.content,
        quoteAuthor = this.author
    )
}
