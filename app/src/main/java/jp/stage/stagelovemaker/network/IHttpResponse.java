package jp.stage.stagelovemaker.network;

/**
 * Created by congn on 8/7/2017.
 */

public interface IHttpResponse {
    void onHttpComplete(String response, int idRequest);

    void onHttpError(String response, int idRequest, int errorCode);
}
