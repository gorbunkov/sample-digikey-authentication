package com.company.demo.service.digikey;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface DigiKeyRestService {

    @FormUrlEncoded
    @POST("/as/token.oauth2")
    Call<JsonObject> getAccessTokenByCode(@Field("code") String code,
                                          @Field("client_id") String clientId,
                                          @Field("client_secret") String clientSecret,
                                          @Field("grant_type") String grantType);

    @FormUrlEncoded
    @POST("/as/token.oauth2")
    Call<JsonObject> getAccessTokenByRefreshToken(@Field("refresh_token") String refreshToken,
                                          @Field("client_id") String clientId,
                                          @Field("client_secret") String clientSecret,
                                          @Field("grant_type") String grantType);
}
