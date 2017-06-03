package tech.wcdi.spajam.myapplication.transfer;


import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Transfer extends AsyncTask<Data, Data, Data> {
    public static final MediaType jsontype = MediaType.parse("application/json; charset=utf8");
    private OkHttpClient client = new OkHttpClient();

    private String Post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(jsontype, json);
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    @Override
    protected Data doInBackground(Data... params) {

        return null;
    }
}