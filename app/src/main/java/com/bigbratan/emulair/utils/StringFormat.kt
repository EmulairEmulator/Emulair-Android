package com.bigbratan.emulair.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatTitlePlaceholder(title: String): String {
    val romanNumeralRegex = "^(M{0,3})(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$".toRegex()

    val sanitizedName = title.replace(Regex("\\(.*\\)"), "").trim()

    if (!sanitizedName.contains(" ")) {
        return when {
            sanitizedName.length > 10 -> {
                val consonants = sanitizedName.filter { it.lowercaseChar() !in "aeiou" }
                if (consonants.length > 10) {
                    "${sanitizedName.take(10)}."
                } else {
                    consonants
                }
            }

            else -> sanitizedName
        }
    }

    return sanitizedName.split(Regex("\\s|(?=\\p{Punct})")).asSequence()
        .map { word ->
            if (romanNumeralRegex.matches(word.uppercase(Locale.ROOT))) {
                word.uppercase(Locale.ROOT)
            } else {
                word.firstOrNull()?.uppercaseChar().toString()
            }
        }
        .filter {
            it.firstOrNull() != null && (it.first().isDigit() or it.first()
                .isUpperCase() or (it.first() == '&') or (it.first() == ':') or (it.first() == '.') or (it.first() == '-'))
        }
        .joinToString("")
        .ifBlank { title.first().toString() }
        .replaceFirstChar(Char::titlecase)
}

fun formatDate(dateInMilliseconds: Long): String {
    val date = Date(dateInMilliseconds)
    val format = SimpleDateFormat("yyyy", Locale.getDefault())

    return format.format(date)
}

fun formatCompany(input: String): String {
    val words = input.split(" ")

    return if (words.size > 2) {
        words.joinToString("") { it.first().toString() }
    } else {
        input
    }
}