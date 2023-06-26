package com.lin.calc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Map;

public class Token {
    private final int userId;
    private String accessToken;
    private String refreshToken;
    private boolean available=true;


    private long recTime;

    public int getUserId() {
        return userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getBearerToken() {
        return "bearer " + accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public long getRecTime() {
        return recTime;
    }

    public Token(int userId, String accessToken, String refreshToken, long recTime) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.recTime = recTime;
    }

    public boolean isAvailable() {
        return System.currentTimeMillis() - recTime<604_800_000 & available;
    }


    @Override
    public String toString() {
        return ("""
                {
                    "userId"="%s",
                    "accessToken"="%s",
                    "refreshToken"="%s",
                    "recTime"=%d
                }
                token时长：%.3f天（大于7天需要重新登录）
                """)
                .formatted(userId, accessToken, refreshToken, recTime, (float) (System.currentTimeMillis() - recTime) / 86400_000);
    }
}
