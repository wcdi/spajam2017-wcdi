package tech.wcdi.spajam.myapplication.transfer;

import android.util.Log;

import net.arnx.jsonic.JSON;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by acq on 6/4/17.
 */

public class AsyncSender {

    public final MediaType jsontype = MediaType.parse("application/json; charset=utf-8");
    public String urlPath = "/index.html";
    // 入力データ群
    // 全データ
    public Data allData;
    public OkHttpClient client = new OkHttpClient();
    public String url;
    // 受信したデータ
    Data reply;
    private String URL = "http://server.wcdi.tech/";
    private final String urlHosts = URL;

    public void setPath(String path) {
        this.url = this.urlHosts + path;
    }

    public Data getReply() {
        return reply;
    }

    public boolean isSuccess() {
        return reply.isStatus();
    }


    private String Post(String json) throws IOException {
        Log.d("TRANS/POST/START", "Post function start.");
        RequestBody body = RequestBody.create(jsontype, json);
        Request request = new Request.Builder().url(url).addHeader("Content-Language", "ja")

                .post(body).build();
        Response response = client.newCall(request).execute();
        if (response.code() != 200) {
            Log.e("TRANS/POST/ERROR", "Responce code is " + response.code() + "!");
            return "{\"status\":false}";
        }
        Log.d("TRANS/POST/SUCCESS",
                "POST Successful! " + response.code() + "! " + response.body().contentLength());
        return response.body().string();
    }

    protected Data doInBackground(Data... params) {
        try {
            Log.d("TRANSFER/START", "Download start...");
            Log.d("TRANSFER/START/URL", url);
            Log.d("TRANSFER/START/DATA", JSON.encode(allData));

            reply = JSON.decode(Post("q=" + JSON.encode(allData)), Data.class);
        } catch (IOException e) {
            Log.e("TRANSFER/ERROR/EXCEPT", "IOException Error");
            e.printStackTrace();
        }

        Log.d("TRANSFER/FINISH", "TRANSFER FINISHED.");
        return null;
    }
}
