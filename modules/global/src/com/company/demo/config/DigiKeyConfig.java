package com.company.demo.config;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.global.Secret;

@Source(type = SourceType.DATABASE)
public interface DigiKeyConfig extends Config {

    @Property("digikey.clientId")
    String getClientId();
    void setClientId(String value);

    @Secret
    @Property("digikey.clientSecret")
    String getClientSecret();
    void setClientSecret(String value);

    @Property("digikey.accessToken")
    String getAccessToken();
    void setAccessToken(String value);

    @Property("digikey.refreshToken")
    String getRefreshToken();
    void setRefreshToken(String value);
}
