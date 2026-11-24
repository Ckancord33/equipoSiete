package com.example.dogapp.utils

object Constants {
    const val NAME_BD: String = "app_data.db"
    const val BASE_URL = "https://dog.ceo/api/"
    const val END_POINT_BREEDS = "breeds/list/all"
    const val DEFAULT_SYMPTOM = "Síntomas"
    val SYMPTOMS_LIST: List<String> = listOf(
        DEFAULT_SYMPTOM,
        "Solo duerme",
        "No come",
        "Fractura extremidad",
        "Tiene pulgas",
        "Tiene garrapatas",
        "Bota demasiado pelo"
    )
}
