package com.example.springbootkotlinwebflux.util

data class CursorResult<T, Cursor>(
    val items: List<T>,
    val cursor: Cursor?,
    val isLast: Boolean,
)
