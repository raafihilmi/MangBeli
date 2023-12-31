package com.capstone.mangbeli.data.remote.network

import com.capstone.mangbeli.data.remote.response.DetailVendorResponse
import com.capstone.mangbeli.data.remote.response.ErrorResponse
import com.capstone.mangbeli.data.remote.response.ImageUploadResponse
import com.capstone.mangbeli.data.remote.response.LoginResponse
import com.capstone.mangbeli.data.remote.response.ProfileVendorResponse
import com.capstone.mangbeli.data.remote.response.RefreshTokenResponse
import com.capstone.mangbeli.data.remote.response.RegisterResponse
import com.capstone.mangbeli.data.remote.response.UserDetailResponse
import com.capstone.mangbeli.data.remote.response.UserMapsResponse
import com.capstone.mangbeli.data.remote.response.UserResponse
import com.capstone.mangbeli.data.remote.response.VendorsMapsResponse
import com.capstone.mangbeli.data.remote.response.VendorsResponse
import com.capstone.mangbeli.model.FCMToken
import com.capstone.mangbeli.model.LocationUpdate
import com.capstone.mangbeli.model.SendNotif
import com.capstone.mangbeli.model.UserProfile
import com.capstone.mangbeli.model.VendorProfile
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("confPassword") confPassword: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @POST("/fcm")
    suspend fun updateFCMToken(
        @Body fcm: FCMToken
    ): ErrorResponse

    @POST("/notif")
    suspend fun sendNotification(
        @Body sendNotif: SendNotif
    ): ErrorResponse

    @GET("/user/profile")
    suspend fun getUserProfile(): UserResponse

    @GET("/vendor/profile")
    suspend fun getVendorProfile(): ProfileVendorResponse

    @GET("/vendor")
    suspend fun getDetailVendor(@Query("vendorId") vendorId: String): DetailVendorResponse

    @GET("/user")
    suspend fun getDetailtUser(@Query("userId") userId: String): UserDetailResponse

    @GET("/vendors")
    suspend fun getVendors(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20,
        @Query("location") location: Int?,
        @Query("null") isEnable: Int?,
        @Query("search") search: String?,
        @Query("filter") filter: String?

    ): VendorsResponse

    @GET("/vendors/maps")
    suspend fun getMapsVendors(): VendorsMapsResponse

    @GET("/users/maps")
    suspend fun getMapsUsers(): UserMapsResponse

    @GET("/token")
    suspend fun refreshToken(): RefreshTokenResponse

    @PATCH("/location")
    suspend fun updateLocation(
        @Body locationUpdate: LocationUpdate
    ): ErrorResponse

    @DELETE("/location")
    suspend fun deleteLocation(): ErrorResponse

    @PATCH("/user/profile")
    suspend fun updateUserProfile(
        @Body userProfile: UserProfile
    ): ErrorResponse

    @DELETE("/logout")
    suspend fun logout(@Header("Cookie") refreshToken: String): ErrorResponse

    @PATCH("/vendor/profile")
    suspend fun updateVendorProfile(
        @Body vendorProfile: VendorProfile
    ): ErrorResponse

    @Multipart
    @POST("/user/profile/upload")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): ImageUploadResponse
}