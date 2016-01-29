package com.uber.sdk.android.rides;

public class AuthInfo {

    String access_token;
    public String getAccessToken() {
        return access_token;
    }

    String token_type;
    public String getTokenType() {
        return token_type;
    }

    String expires_in;
    public String getExpiresIn() {
        return expires_in;
    }

    String refresh_token;
    public String getRefreshToken() {
        return refresh_token;
    }

    String scope;
    public String getScope() {
        return scope;
    }

}
