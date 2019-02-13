package com.company.demo.service.digikey;

import com.company.demo.config.DigiKeyConfig;
import com.company.demo.service.DigiKeyService;
import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Inject;
import java.io.IOException;

@Service(DigiKeyService.NAME)
public class DigiKeyServiceBean implements DigiKeyService {

    @Inject
    protected DigiKeyConfig digiKeyConfig;

    @Inject
    protected Logger log;

    @Override
    public String getAuthorizationCodeEndpointUrl() {
        return "https://sso.digikey.com/as/authorization.oauth2?response_type=code" +
                "&client_id=" + digiKeyConfig.getClientId();
    }

    @Override
    public String obtainAccessTokenByCode(String code) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://sso.digikey.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DigiKeyRestService digiKeyRestService = retrofit.create(DigiKeyRestService.class);
        try {
            Response<JsonObject> response = digiKeyRestService.getAccessTokenByCode(code,
                    digiKeyConfig.getClientId(),
                    digiKeyConfig.getClientSecret(),
                    "authorization_code")
                    .execute();
            return processTokenResponse(response);
        } catch (IOException e) {
            throw new RuntimeException("Error", e);
        }
    }

    @Override
    public String obtainAccessTokenByRefreshToken() {
        String refreshToken = digiKeyConfig.getRefreshToken();
        if (Strings.isNullOrEmpty(refreshToken)) {
            return "Refresh token value is not set";
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://sso.digikey.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DigiKeyRestService digiKeyRestService = retrofit.create(DigiKeyRestService.class);
        try {
            Response<JsonObject> response = digiKeyRestService.getAccessTokenByRefreshToken(refreshToken,
                    digiKeyConfig.getClientId(),
                    digiKeyConfig.getClientSecret(),
                    "refresh_token")
                    .execute();
            return processTokenResponse(response);
        } catch (IOException e) {
            throw new RuntimeException("Error", e);
        }
    }

    private String processTokenResponse(Response<JsonObject> response) throws IOException {
        if (response.isSuccessful()) {
            JsonObject element = response.body();
            String accessToken = element.get("access_token").getAsString();
            String refreshToken = element.get("refresh_token").getAsString();
            digiKeyConfig.setAccessToken(accessToken);
            digiKeyConfig.setRefreshToken(refreshToken);
            log.info("Tokens stored. Access token: {}, refresh token: {}", maskToken(accessToken), maskToken(refreshToken));
            return "Success";
        } else {
            JsonObject jsonObject = new JsonParser().parse(response.errorBody().string()).getAsJsonObject();
            return jsonObject.get("error") + ": " + jsonObject.get("error_description");
        }
    }

    private String maskToken(String tokenValue) {
        return (tokenValue.length() > 5) ? "****" + tokenValue.substring(tokenValue.length() - 5) : "****";
    }
}