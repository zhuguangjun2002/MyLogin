package tk.maikator.mylogin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tk.maikator.mylogin.service.UserClient;

public class MainActivity extends AppCompatActivity {

    UserClient userClient = ServiceGenerator.createService(UserClient.class);
    private static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the login form.

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        findViewById(R.id.btn_postings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPostings();
            }
        });
    }

    private void login()  {

        Call<ResponseBody> call = userClient.login("admin","yantai2018");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Toast.makeText(MainActivity.this, response.body().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(MainActivity.this, "login not correct :(", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error login-- :(", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void getPostings(){
        Call<ResponseBody> call = userClient.getPostings(token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Toast.makeText(MainActivity.this, response.body().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(MainActivity.this, "getPostings not correct :(", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error :(", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
