package com.example.reconstructv2.Repositories.RemoteRepository;


import androidx.annotation.Nullable;

import com.example.reconstructv2.Models.ApiResponses.BaseAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.CheckAvailableAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.DesiredItemsAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.ImageIDAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.ListingIDAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.ListingListAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.SingleListingAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.UserAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.UserTokenAPIResponse;
import com.example.reconstructv2.Models.ListingFull;
import com.example.reconstructv2.Models.ListingItem;
import com.example.reconstructv2.Models.ListingToCreate;
import com.example.reconstructv2.Models.User;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import io.reactivex.Observable;


// set urls available on the server with data required for each destination
public interface APIService {

    // Test Connections

    @GET("/")
    Call<BaseAPIResponse> testConnection();

    @GET("/auth")
    Call<BaseAPIResponse> testConnectionAuthenticated(@Header("authorization") String AuthHeaderToken);

    // Availability Checks

    @GET("/checkUsernameAvailable")
    Call<CheckAvailableAPIResponse> checkUsernameUnique(@Query("username") String username);

    @GET("/checkEmailAvailable")
    Call<CheckAvailableAPIResponse> checkEmailUnique(@Query("email") String email);

    // Listingss

    @POST("/auth/createListing")
    Call<ListingIDAPIResponse> createListing(@Header("authorization") String AuthHeaderToken,
                                             @Body ListingFull listing);

    @POST("/auth/addListingtoWatchList")
    Call<BaseAPIResponse> addListingtoWatchList(@Header("authorization") String AuthHeaderToken,
                                                @Query("listingID") String listingID);

    @POST("/auth/removeListingfromWatchList")
    Call<BaseAPIResponse> removeListingfromWatchList(@Header("authorization") String AuthHeaderToken,
                                                     @Query("listingID") String listingID);

    @GET("/auth/getWatchlist")
    Call<ListingListAPIResponse> getWatchlist(@Header("authorization") String AuthHeaderToken,
                                              @Query("pageNum") Integer pageNum,
                                              @Query("listingsPerPage") Integer listingsPerPage);

    @GET("/auth/getRecentListings")
    Call<ListingListAPIResponse> getRecentListings(@Header("authorization") String AuthHeaderToken,
                                                   @Query("pageNum") Integer pageNum,
                                                   @Query("listingsPerPage") Integer listingsPerPage);

    @GET("/auth/getListing")
    Call<SingleListingAPIResponse> getListingAuthenticated(@Header("authorization") String AuthHeaderToken,
                                                           @Query("listingID") String listingID);

    @POST("/auth/reserveItems")
    Call<BaseAPIResponse> reserveItemsRequest(@Header("authorization") String AuthHeaderToken,
                                              @Query("listingItemIDList") List<JsonObject> listingItemIDs);

    @GET("/getListing")
    Call<SingleListingAPIResponse> getListingNoAuth(@Query("listingID") String listingID);

    @GET("/getFrontPageListings")
    Call<ListingListAPIResponse> getFrontPageListings(
            @Query("locationFilterLimit") double locationFilterLimit,
            @Query("userLat") double userLat,
            @Query("userLon") double userLon,
            @Query("postDateSortType") String postDateSortType,
            @Query("endDateSortType") String endDateSortType,
            @Query("distanceSortType") String distanceSortType,
            @Query("pageNum") Integer pageNum,
            @Query("listingsPerPage") Integer listingsPerPage);

    @GET("/getUserListings")
    Call<ListingListAPIResponse> getUserListings(@Query("userID") String userID,
                                                 @Query("pageNum") Integer pageNum,
                                                 @Query("listingsPerPage") Integer listingsPerPage);

    @GET("/getFilteredListings")
    Call<ListingListAPIResponse> getFilteredListings(@Query("searchString") String searchString,
                                                     @Query("locationFilterLimit") double locationFilterLimit,
                                                     @Query("userLat") double userLat,
                                                     @Query("userLon") double userLon,
                                                     @Query("postDateSortType") String postDateSortType,
                                                     @Query("endDateSortType") String endDateSortType,
                                                     @Query("distanceSortType") String distanceSortType,
                                                     @Query("pageNum") Integer pageNum,
                                                     @Query("listingsPerPage") Integer listingsPerPage);

    @GET("/getDesiredItems")
    Call<DesiredItemsAPIResponse> getDesiredItems(@Query("pageNum") Integer pageNum,
                                                  @Query("listingsPerPage") Integer listingsPerPage);

    // User Management

    @POST("/createAccount")
    Call<UserTokenAPIResponse> createAccount(@Body User user);

    @GET("/auth/login")
    Call<UserTokenAPIResponse> login(@Header("authorization") String AuthHeaderUP);

    @GET("/auth/logout")
    Call<BaseAPIResponse> logout(@Header("authorization") String AuthHeaderUP);

    @GET("/getUserProfile")
    Call<UserAPIResponse> getUserProfile(@Query("userID") String userID);

    @GET("/auth/changeWantedTags")
    Call<BaseAPIResponse> changeWantedTags(@Header("authorization") String AuthHeaderToken,
                                           @Query("new_tags") List<String> new_tags);

    @GET("/auth/getWatchedListings")
    Call<ListingListAPIResponse> getWatchedListings(@Header("authorization") String AuthHeaderToken,
                                                    @Query("pageNum") Integer pageNum,
                                                    @Query("listingsPerPage") Integer listingsPerPage);


    @POST("/auth/updateUserData")
    Call<BaseAPIResponse> saveUser(@Header("authorization") String AuthHeaderToken,
                                   @Body User user);

    @POST("/auth/sendChangePasswordRequest")
    Call<BaseAPIResponse> updatePassword(@Header("authorization") String AuthHeaderToken,
                                         @Query("newPassword") String newPassword);


    @Multipart
    @POST("/auth/saveImage")
    Call<ImageIDAPIResponse> saveImageOnServer(@Header("authorization") String AuthHeaderToken,
                                               @Part MultipartBody.Part image);

    @DELETE("/auth/deleteImage")
    Call<BaseAPIResponse> deleteImageFromServer(@Header("authorization") String AuthHeaderToken,
                                                @Query("imageID") List<String> imageID);

    @DELETE("/auth/deleteListing")
    Call<BaseAPIResponse> deleteListing(@Header("authorization") String AuthHeaderToken,
                                        @Query("listingID") String listingID);
}