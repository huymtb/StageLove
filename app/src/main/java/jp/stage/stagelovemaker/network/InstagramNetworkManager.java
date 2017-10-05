package jp.stage.stagelovemaker.network;

import android.content.Context;
import android.widget.Toast;

import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by congn on 8/7/2017.
 */

public class InstagramNetworkManager {
    private static Retrofit retrofit = null;
    Context mContext;
    IHttpResponse iHttpResponse;
    InstagramEndpointInterface apiService;


    public InstagramNetworkManager(Context context, IHttpResponse iHttpResponse) {
        try {
            this.iHttpResponse = iHttpResponse;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "IHttpResponse");
        }
        this.mContext = context;
        apiService = getClient().create(InstagramEndpointInterface.class);
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.INSTAGRAM_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public void requestInstagramApi(Call<InstargramResponeModel> call, final int idRequest) {
        call.enqueue(new Callback<InstargramResponeModel>() {
            @Override
            public void onResponse(Call<InstargramResponeModel> call, Response<InstargramResponeModel> response) {
                InstargramResponeModel responseModel = response.body();
                if (responseModel != null) {
                    iHttpResponse.onHttpComplete(responseModel.getData().toString(), idRequest);
                } else {
                    iHttpResponse.onHttpError("", idRequest, 0);
                }
            }

            @Override
            public void onFailure(Call<InstargramResponeModel> call, Throwable t) {
                Toast.makeText(mContext, mContext.getString(R.string.unknown_error_network), Toast.LENGTH_LONG).show();
            }
        });
    }


    public Call<InstargramResponeModel> getUserInfo(String token) {
        return apiService.getUser(token);
    }
}
