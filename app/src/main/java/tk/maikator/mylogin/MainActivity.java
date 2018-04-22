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
import tk.maikator.mylogin.model.User;
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

        Call<User> call = userClient.login("admin","yantai2018");

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    // if you want to get <User> object, you just call `response.body()` to get it.
                    token =response.body().getToken();
                    if(token != null) {
                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "token is empty!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(MainActivity.this, "login not correct :(", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error login-- :(", Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void getPostings(){
        // 注意，因为我们使用的是`JWT-token`认证，标头为：`Bearer`
        // 后台服务器的`settings.py`文件里进行了设置。
        // 这里，必须加上`Bearer`，外加`空格`+token,后台方可认可。
        String authHeader = "Bearer "+ token;
        Call<ResponseBody> call = userClient.getPostings(authHeader);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String posts = response.body().string();
                        Toast.makeText(MainActivity.this,posts, Toast.LENGTH_SHORT).show();
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
