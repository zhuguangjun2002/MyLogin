package tk.maikator.mylogin.background;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import tk.maikator.mylogin.ServiceGenerator;
import tk.maikator.mylogin.model.User;
import tk.maikator.mylogin.service.UserClient;


public class BackgroundService extends IntentService {

    private static String token;

    public BackgroundService() {
        super("BackgroundService");
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        UserClient userClient = ServiceGenerator.createService(UserClient.class);
        Call<User> call = userClient.login("admin","yantai2018");
        try{
            Response<User> result = call.execute();
            //Log.i("Retrofit","Success!");
            String my_token =result.body().getToken();
            if(my_token != null) {
                //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                token = my_token;
                Log.i("Retrofit",my_token);
            }
            else{
                Log.i("Retrofit","token is empty!");

            }

        }catch (IOException e){
            Log.i("Retrofit","Failure!");
        }

    }



}
