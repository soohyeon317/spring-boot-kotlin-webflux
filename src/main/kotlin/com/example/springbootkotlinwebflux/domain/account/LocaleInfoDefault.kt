package com.example.springbootkotlinwebflux.domain.account

enum class LocaleInfoDefault(val default: String) {

    LANGUAGE_CODE(default = LanguageCode.ENGLISH.code),
    COUNTRY_CODE(default = "UNKNOWN"),
    TIME_ZONE_CODE(default = "UTC")
}
