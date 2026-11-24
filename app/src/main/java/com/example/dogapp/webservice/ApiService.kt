package com.example.dogapp.webservice

import com.example.dogapp.model.BreedsResponse
import com.example.dogapp.utils.Constants.END_POINT_BREEDS
import retrofit2.http.GET

interface ApiService {
    @GET(END_POINT_BREEDS)
    suspend fun getBreeds(): BreedsResponse
}
