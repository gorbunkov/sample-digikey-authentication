package com.company.demo.service;

public interface DigiKeyService {
    String NAME = "demo_DigiKeyService";

    String getAuthorizationCodeEndpointUrl();

    /**
     * Gets the access and refresh tokens from the DigiKey server using the authorization code and saves token values
     * to the {@link com.company.demo.config.DigiKeyConfig} values
     *
     * @param code authorization code
     * @return an operation status. May be "Success" or a string with an error description
     */
    String obtainAccessTokenByCode(String code);

    /**
     * Gets the access and refresh tokens from the DigiKey server using the refresh token that is stored in the property
     * of the {@link com.company.demo.config.DigiKeyConfig}. After tokens are retrieved, they replace current token
     * values of the {@link com.company.demo.config.DigiKeyConfig}
     *
     * This method can be used by scheduled task if you need to refresh token periodically.
     *
     * @return an operation status. May be "Success" or a string with an error description
     */
    String obtainAccessTokenByRefreshToken();
}