package jp.stage.stagelovemaker.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;

import jp.stage.stagelovemaker.MyApplication;
import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.base.UserPreferences;
import jp.stage.stagelovemaker.model.DiscoverModel;
import jp.stage.stagelovemaker.model.SettingModel;
import jp.stage.stagelovemaker.model.SignUpModel;
import jp.stage.stagelovemaker.model.UserInfoModel;
import jp.stage.stagelovemaker.utils.Utils;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by congn on 8/11/2017.
 */

public class NetworkManager {
    private static String BASE_API = "http://153.126.150.57/love-maker/";
    private static Retrofit retrofit = null;
    MyApiEndpoint apiService;
    private Context mContext;
    private IHttpResponse iHttpResponse;
    private ProgressDialog progressDialog;

    public NetworkManager(Context context, IHttpResponse iHttpResponse) {
        try {
            this.iHttpResponse = iHttpResponse;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "IHttpResponse");
        }
        this.mContext = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        apiService = getClient((Activity) context).create(MyApiEndpoint.class);
    }

    public static Retrofit getClient(final Activity activity) {
        if (retrofit == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    MyApplication app = Utils.getApplication(activity);
                    String accessToken = "";
                    if (app != null) {
                        accessToken = UserPreferences.getPrefUserAccessToken();
                        if (TextUtils.isEmpty(accessToken)) {
                            accessToken = "";
                        }
                    }
                    String formatedToken = "Bearer " + accessToken;
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .addHeader("Authorization", formatedToken)
                            .addHeader("Content-type", "application/x-www-form-urlencoded");

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });

            OkHttpClient client = httpClient.build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    public void requestApi(final Call<ResponseModel> call, final int idRequest) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (call == null) {
                    return;
                }
                progressDialog.show();
                progressDialog.setContentView(R.layout.dialog_progress_bar);
                call.enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (mContext instanceof Activity) {
                            if (!((Activity) mContext).isFinishing()) {
                                if ((progressDialog != null) && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            }
                            ResponseModel responseModel = response.body();
                            if (responseModel != null) {
                                if (responseModel.getStatus() == 200) {
                                    iHttpResponse.onHttpComplete(responseModel.getResult().toString(), idRequest);
                                } else {
                                    iHttpResponse.onHttpError(responseModel.getResult().toString(), idRequest, responseModel.getStatus());
                                }
                            } else {
                                Toast.makeText(mContext, mContext.getString(R.string.unknown_error_network), Toast.LENGTH_LONG).show();
                                iHttpResponse.onHttpError("", idRequest, 0);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        if (mContext instanceof Activity) {
                            if (!((Activity) mContext).isFinishing())
                                if ((progressDialog != null) && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                        }
                        Toast.makeText(mContext, mContext.getString(R.string.unknown_error_network), Toast.LENGTH_LONG).show();
                        iHttpResponse.onHttpError("", idRequest, 0);
                    }
                });
            }
        }, 150);
    }

    public void requestApiNoProgress(final Call<ResponseModel> call, final int idRequest) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (call == null) {
                    return;
                }
                call.enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        ResponseModel responseModel = response.body();
                        if (responseModel != null) {
                            if (responseModel.getStatus() == 200) {
                                iHttpResponse.onHttpComplete(responseModel.getResult().toString(), idRequest);
                            } else {
                                iHttpResponse.onHttpError(responseModel.getResult().toString(), idRequest, responseModel.getStatus());
                            }
                        } else {
                            iHttpResponse.onHttpError(mContext.getString(R.string.unknown_error_network), idRequest, 0);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        Toast.makeText(mContext, mContext.getString(R.string.unknown_error_network), Toast.LENGTH_LONG).show();
                        iHttpResponse.onHttpError("", idRequest, 0);
                    }
                });
            }
        }, 150);
    }

    public Call<ResponseModel> signUp(SignUpModel signUpModel) {
        JsonObject data = new JsonObject();
        data.addProperty("email", signUpModel.getEmail());
        data.addProperty("username", signUpModel.getUsername());
        data.addProperty("password", signUpModel.getPassword());
        data.addProperty("first_name", signUpModel.getFirst_name());
        data.addProperty("last_name", signUpModel.getLast_name());
        data.addProperty("birthday", signUpModel.getBirthday());
        data.addProperty("gender", signUpModel.getGender());

        return apiService.signUp(data);
    }

    public Call<ResponseModel> signIn(String username, String password) {
        JsonObject data = new JsonObject();
        data.addProperty("username", username);
        data.addProperty("password", password);
        return apiService.signIn(data);
    }

    public Call<ResponseModel> validateUsernameAndEmail(String username, String email) {
        //String usernameAndEmail = String.format("username=%s&email=%s", username, email);
        return apiService.validateUserAndEmail(username, email);
    }

    public Call<ResponseModel> uploadAvatar(int index, Bitmap bitmap) {
        File newfile = Utils.compressFile(mContext, Utils.savebitmap(mContext, bitmap));
        newfile.getPath();
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/png"), newfile);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("avatar", newfile.getName(), requestFile);
        return apiService.uploadAvatar(body, index);
    }

    public Call<ResponseModel> getProfile(int id) {
        return apiService.getProfile(id);
    }

    public Call<ResponseModel> updateLocation(int id, double latitude, double longitude) {
        JsonObject data = new JsonObject();
        data.addProperty("latitude", latitude);
        data.addProperty("longitude", longitude);
        return apiService.updateUserInfo(id, data);
    }

    public Call<ResponseModel> forgotPassword(String email) {
        JsonObject data = new JsonObject();
        data.addProperty("email", email);
        return apiService.forgotPassword(data);
    }

    public Call<ResponseModel> updatePassword(String email, String code_number, String password) {
        JsonObject data = new JsonObject();
        data.addProperty("email", email);
        data.addProperty("code_number", code_number);
        data.addProperty("password", password);
        return apiService.updatePassword(data);
    }

    public Call<ResponseModel> updateSettings(int id, SettingModel settingModel, DiscoverModel discoverModel) {
        JsonObject data = new JsonObject();
        data.addProperty("notify_new_matches", settingModel.getNotifyNewMatches());
        data.addProperty("notify_messages", settingModel.getNotifyMessages());
        data.addProperty("notify_message_likes", settingModel.getNotifyMessageLikes());
        data.addProperty("notify_super_likes", settingModel.getNotifySuperLikes());
        data.addProperty("distance_unit", settingModel.getDistanceUnit());
        data.addProperty("show_me_on_stage_maker", settingModel.getShowMeOnStageMaker());
        data.addProperty("from_age", discoverModel.getFromAge());
        data.addProperty("to_age", discoverModel.getToAge());
        data.addProperty("filter_distance", discoverModel.getFilterDistance());
        data.addProperty("filter_gender", discoverModel.getFilterGender());
        return apiService.updateSettings(id, data);
    }

    public Call<ResponseModel> deleteAvatar(int id, int index) {
        JsonObject data = new JsonObject();
        data.addProperty("index", index);
        return apiService.deleteAvatar(id, data);
    }

    public Call<ResponseModel> updateUserProfile(UserInfoModel userInfoModel) {
        JsonObject data = new JsonObject();
        data.addProperty("first_name", userInfoModel.getFirstName());
        data.addProperty("last_name", userInfoModel.getLastName());
        data.addProperty("birthday", userInfoModel.getMeta().getBirthday());
        data.addProperty("gender", Utils.getIndexGender(userInfoModel.getGender()));
        if (!TextUtils.isEmpty(userInfoModel.getMeta().getAboutMe())) {
            data.addProperty("about_me", userInfoModel.getMeta().getAboutMe());
        }
        if (!TextUtils.isEmpty(userInfoModel.getMeta().getCurrentWork())) {
            data.addProperty("current_work", userInfoModel.getMeta().getCurrentWork());
        }
        if (!TextUtils.isEmpty(userInfoModel.getMeta().getSchool())) {
            data.addProperty("school", userInfoModel.getMeta().getSchool());
        }

        return apiService.updateUserInfo(userInfoModel.getId(), data);
    }

    public Call<ResponseModel> getPeopleList(int page) {
        return apiService.getPeopleList(page);
    }

    public Call<ResponseModel> setFeeling(int user_id, int user_friend, int type) {
        JsonObject data = new JsonObject();
        data.addProperty("user_id", user_id);
        data.addProperty("user_friend", user_friend);
        data.addProperty("type", type);
        return apiService.setFeeling(data);
    }

    public Call<ResponseModel> contacts(String content) {
        JsonObject data = new JsonObject();
        data.addProperty("content", content);
        return apiService.contacts(data);
    }

    public Call<ResponseModel> deleteUser(int user_id, String username, String password) {
        JsonObject data = new JsonObject();
        data.addProperty("username", username);
        data.addProperty("password", password);
        return apiService.deleteUser(user_id, data);
    }

    public Call<ResponseModel> listMatches(int page, String query) {
        if (!TextUtils.isEmpty(query)) {
            return apiService.listMatchesSearch(page, query, query, query);
        } else {
            return apiService.listMatches(page);
        }
    }

    public Call<ResponseModel> getRoom(int receiver_id) {
        JsonObject data = new JsonObject();
        data.addProperty("receiver_id", receiver_id);
        return apiService.getRoom(data);
    }

    public Call<ResponseModel> sendMessage(int userId, String content, String chatRoomId) {
        JsonObject data = new JsonObject();
        data.addProperty("user_id", userId);
        data.addProperty("content", content);
        data.addProperty("type", "text");
        data.addProperty("chat_room_id", chatRoomId);
        return apiService.sendMessage(data);
    }

//    public Call<ResponseModel> sendPicture(int userId, Bitmap bitmap, String chatRoomId) {
//        File newfile = Utils.compressFile(mContext, Utils.savebitmap(mContext, bitmap));
//        newfile.getPath();
//        RequestBody requestFile =
//                RequestBody.create(MediaType.parse("image/**"), newfile);
//        MultipartBody.Part body =
//                MultipartBody.Part.createFormData("content", newfile.getName(), requestFile);
//        return apiService.sendImage(userId, body, "image", chatRoomId);
//    }

    public Call<ResponseModel> sendPicture(int userId, Bitmap bitmap, String chatRoomId) {
        File newfile = Utils.compressFile(mContext, Utils.savebitmap(mContext, bitmap));
        newfile.getPath();
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/**"), newfile);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("content", newfile.getName(), requestFile);
        return apiService.sendImage(userId,
                body,
                Utils.toRequestBody("image"),
                Utils.toRequestBody(chatRoomId));
    }

    public Call<ResponseModel> report(int receiverId, String reason) {
        JsonObject data = new JsonObject();
        data.addProperty("receiver_id", receiverId);
        data.addProperty("reason", reason);
        return apiService.report(data);
    }
}
