package jp.stage.stagelovemaker.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by congn on 8/7/2017.
 */

public interface InstagramEndpointInterface {
    @GET("/v1/users/self/")
    Call<InstargramResponeModel> getUser(@Query("access_token") String access_token);
}
