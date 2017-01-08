package wumf.com.sharedapps.retrofit;

import android.util.Log;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by max on 27.12.16.
 */

public class GCMSender {

    private static final String TAG = "push";

    public void send(String to, String messageStr) {
        Log.i(TAG, "to=" + to + " message=" + messageStr);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fcm.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Message message = new Message();
        Data data = new Data();
        data.setTitle("test");
        data.setBody(messageStr);
        message.setData(data);
        message.setTo(to);
        retrofit.create(GCMessage.class).send(message).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.i(TAG, "response=" + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.i(TAG, "failed");
            }
        });
    }

}
