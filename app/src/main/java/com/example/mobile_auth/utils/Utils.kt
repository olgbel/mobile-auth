package com.example.mobile_auth.utils

import java.util.regex.Pattern

/**
 * Minimum is 6 chars. Should be at least one capital letter. Allow only english characters and
 * numbers
 */
private val pattern by lazy {
    Pattern.compile("(?=.*[A-Z])(?!.*[^a-zA-Z0-9])(.{6,})\$")
}

fun String.isValid() = pattern.matcher(this).matches()

