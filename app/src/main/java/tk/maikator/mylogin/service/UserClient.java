package tk.maikator.mylogin.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import tk.maikator.mylogin.model.User;

public interface UserClient {

    @FormUrlEncoded
    @POST("/api/auth/login/")
    Call<User> login(@Field("username") String username, @Field("password") String password);



    @GET("/api/postings")
    Call<ResponseBody> getPostings(@Header("Authorization") String authHeader);
}
