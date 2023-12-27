package com.bookstore.libraryservice.dto

data class AddBookRequest @JvmOverloads constructor(
        val id: String,
        val isbn: String
)
