package tk.maikator.mylogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.blankj.ALog;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tk.maikator.mylogin.background.BackgroundService;
import tk.maikator.mylogin.model.BlogPost;
import tk.maikator.mylogin.model.User;
import tk.maikator.mylogin.service.UserClient;

public class MainActivity extends AppCompatActivity {


    private static String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add a Log
        initALog();

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
        UserClient userClient = ServiceGenerator.createService(UserClient.class);
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



        /*
        Intent intent = new Intent(MainActivity.this, BackgroundService.class);
        startService(intent);
        */
    }


    private void getPostings(){
        // 注意，因为我们使用的是`JWT-token`认证，标头为：`Bearer`
        // 后台服务器的`settings.py`文件里进行了设置。
        // 这里，必须加上`Bearer`，外加`空格`+token,后台方可认可。
        String authHeader = "Bearer "+ token;
        UserClient userClient = ServiceGenerator.createService(UserClient.class);
        Call<List<BlogPost>> call = userClient.getPostings(authHeader);

        call.enqueue(new Callback<List<BlogPost>>() {
            @Override
            public void onResponse(Call<List<BlogPost>> call, Response<List<BlogPost>> response) {
                if (response.isSuccessful()) {
                    List<BlogPost> posts = response.body();
                    // 遍历所有的`BlogPost`对象
                    for(BlogPost post : posts){
                        String title = post.getTitle();
                        if( title != null ){
                            // use ALog to print log
                            ALog.d(title);
                            // 在Android Studio 3.0.1上，当Log比较近的时候，会合并相同`tag`的Log。
                            //Log.d("find", title);
                        }


                    }
                }else {
                    Toast.makeText(MainActivity.this, "getPostings not correct :(", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<BlogPost>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error :(", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void initALog() {
        ALog.Config config = ALog.init(this)
                .setLogSwitch(BuildConfig.DEBUG)// 设置 log 总开关，包括输出到控制台和文件，默认开
                .setConsoleSwitch(BuildConfig.DEBUG)// 设置是否输出到控制台开关，默认开
                .setGlobalTag(null)// 设置 log 全局标签，默认为空
                // 当全局标签不为空时，我们输出的 log 全部为该 tag，
                // 为空时，如果传入的 tag 为空那就显示类名，否则显示 tag
                .setLogHeadSwitch(true)// 设置 log 头信息开关，默认为开
                .setLog2FileSwitch(false)// 打印 log 时是否存到文件的开关，默认关
                .setDir("")// 当自定义路径为空时，写入应用的 /cache/log/ 目录中
                .setFilePrefix("")// 当文件前缀为空时，默认为 "alog"，即写入文件为 "alog-MM-dd.txt"
                .setBorderSwitch(true)// 输出日志是否带边框开关，默认开
                .setSingleTagSwitch(true)// 一条日志仅输出一条，默认开，为美化 AS 3.1 的 Logcat
                .setConsoleFilter(ALog.V)// log 的控制台过滤器，和 logcat 过滤器同理，默认 Verbose
                .setFileFilter(ALog.V)// log 文件过滤器，和 logcat 过滤器同理，默认 Verbose
                .setStackDeep(1)// log 栈深度，默认为 1
                .setStackOffset(0);// 设置栈偏移，比如二次封装的话就需要设置，默认为 0
        ALog.d(config.toString());
    }
}
