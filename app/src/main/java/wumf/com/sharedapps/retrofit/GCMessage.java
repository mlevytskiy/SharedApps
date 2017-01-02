package wumf.com.sharedapps.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by max on 27.12.16.
 */

public interface GCMessage {

    @Headers({
            "Content-Type: application/json",
            "Authorization: key=AAAAhJkH6zE:APA91bF8xuQKg-hWC-jynspp9wzRl-27J6Z-ZRPWcK4us8Dt-44njI68Pwmz-ZpNK6G0_MdjHfAnI6E5A5PD1v8no-XruvW0rZCbQNvTYt6QbuSOhtZ4xtMzQLJgGjVqtzNXFDap4iUTpP-opyLHgQgzl2rv_jj-SQ"
    })
    @POST("fcm/send")
    Call<ResponseBody> send(@Body Message message);

}
