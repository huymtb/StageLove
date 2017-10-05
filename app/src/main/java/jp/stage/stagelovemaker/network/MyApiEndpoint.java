package jp.stage.stagelovemaker.network;

import com.google.gson.JsonObject;

import jp.stage.stagelovemaker.model.DiscoverModel;
import jp.stage.stagelovemaker.model.SettingModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by congn on 8/11/2017.
 */

public interface MyApiEndpoint {
    @POST("api/systems/signIn/")
    Call<ResponseModel> signIn(@Body JsonObject data);

    @POST("api/systems/signUp/")
    Call<ResponseModel> signUp(@Body JsonObject data);

    @GET("api/systems/validate/")
    Call<ResponseModel> validateUserAndEmail(
            @Query("username") String username,
            @Query("email") String email);

    @Multipart
    @POST("api/avatars/")
    Call<ResponseModel> uploadAvatar(@Part MultipartBody.Part avatar,
                                     @Part("index") int index);

    @GET("api/users/{id}")
    Call<ResponseModel> getProfile(@Path("id") int id);

    @PUT("api/users/{user_id}")
    Call<ResponseModel> updateUserInfo(@Path("user_id") int id,
                                       @Body JsonObject data);

    @POST("api/systems/forgotPassword/")
    Call<ResponseModel> forgotPassword(@Body JsonObject data);

    @POST("api/systems/updatePass/")
    Call<ResponseModel> updatePassword(@Body JsonObject data);

    @PUT("api/settings/{user_id}")
    Call<ResponseModel> updateSettings(@Path("user_id") int id, @Body JsonObject data);

    @HTTP(method = "DELETE", path = "api/avatars/{user_id}", hasBody = true)
    Call<ResponseModel> deleteAvatar(@Path("user_id") int id, @Body JsonObject data);

    @GET("api/users/listPeople/{page}")
    Call<ResponseModel> getPeopleList(@Path("page") int page);

    @POST("api/feelings/")
    Call<ResponseModel> setFeeling(@Body JsonObject data);

    @POST("api/contacts/")
    Call<ResponseModel> contacts(@Body JsonObject data);

    @HTTP(method = "DELETE", path = "api/users/{user_id}", hasBody = true)
    Call<ResponseModel> deleteUser(@Path("user_id") int user_id, @Body JsonObject data);

    @GET("api/users/listMatches/{page}")
    Call<ResponseModel> listMatches(@Path("page") int page);

    @GET("api/users/listMatches/{page}")
    Call<ResponseModel> listMatchesSearch(@Path("page") int page,
                                          @Query("first_name") String first_name,
                                          @Query("last_name") String last_name,
                                          @Query("email") String email);

    @POST("api/rooms/")
    Call<ResponseModel> getRoom(@Body JsonObject data);

    @POST("api/messages/")
    Call<ResponseModel> sendMessage(@Body JsonObject data);

//    @Multipart
//    @POST("api/messages/")
//    Call<ResponseModel> sendImage(@Part("user_id") int user_id,
//                                  @Part MultipartBody.Part content,
//                                  //@Part ("content") String content,
//                                  @Part("type") String type,
//                                  @Part("chat_room_id") String chat_room_id);

    @Multipart
    @POST("api/messages/")
    Call<ResponseModel> sendImage(@Part("user_id") int user_id,
                                  @Part MultipartBody.Part content,
                                  //@Part ("content") String content,
                                  @Part("type") RequestBody type,
                                  @Part("chat_room_id") RequestBody chat_room_id);

    @POST("api/reports/")
    Call<ResponseModel> report(@Body JsonObject data);
}
