package com.example.dogapp.webservice

import com.example.dogapp.model.BreedsResponse
import com.example.dogapp.model.DogImageResponse
import com.example.dogapp.utils.Constants.END_POINT_BREEDS
import com.example.dogapp.utils.Constants.END_POINT_BREED_IMAGE
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET(END_POINT_BREEDS)
    suspend fun getBreeds(): BreedsResponse

    @GET(END_POINT_BREED_IMAGE)
    suspend fun getBreedImage(@Path("breed") breed: String): DogImageResponse
}
