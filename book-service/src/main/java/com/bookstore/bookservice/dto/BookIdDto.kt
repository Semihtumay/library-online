package com.bookstore.bookservice.dto

import java.io.Serializable

/**
 * DTO for {@link com.bookstore.bookservice.model.Book}
 */
data class BookIdDto @JvmOverloads constructor(
        val bookId: String? = "",
        val isbn: String
){

    companion object {
        @JvmStatic
        fun convert(id: String, isbn: String): BookIdDto {
            return BookIdDto(id, isbn)
        }
    }
}