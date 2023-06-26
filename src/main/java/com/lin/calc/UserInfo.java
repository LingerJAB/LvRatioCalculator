package com.lin.calc;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Map;

public class UserInfo {
    private int userID; //用户ID
    private int musicScore; //积分
    private int lvRatio; //战力
    private int rankNation; //全国排名
    private int comboPercent; //连击率（518为5.18%）
    private int sex; //性别（1男 2女）
    private String userName; //用户名
    private String headimgURL; //头像URL
    private String phone; //手机号
    private String cityName; //城市名
    private String teamName; //战队名
    private String titleUrl; //头衔
    private String headimgBoxPath; //头像框


    public int getUserID() {
        return userID;
    }

    public int getLvRatio() {
        return lvRatio;
    }

    public String getHeadimgURL() {
        return headimgURL;
    }

    public String getUserName() {
        return userName;
    }

    public int getSex() {
        return sex;
    }

    public String getPhone() {
        return phone;
    }

    public String getCityName() {
        return cityName;
    }

    public int getMusicScore() {
        return musicScore;
    }

    public int getRankNation() {
        return rankNation;
    }

    public int getComboPercent() {
        return comboPercent;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getTitleUrl() {
        return titleUrl==null?"":titleUrl;
    }

    public String getHeadimgBoxPath() {
        return headimgBoxPath==null?"":headimgBoxPath;
    }

    public static UserInfo get(Token token) {
        String userInfoJson = "";
        Call call = httpApiCall("https://dancedemo.shenghuayule.com/Dance/api/User/GetInfo?userId=" + token.getUserId(),
                token.getBearerToken());
        try {
            Response response = call.execute();
            try {

                if(response.body()!=null) {
                    userInfoJson = response.body().string();
                }
            } finally {
                response.body().close();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        Gson gson = new GsonBuilder()
                .serializeNulls()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .create();

        return gson.fromJson(userInfoJson, UserInfo.class);
    }

    private static Call httpApiCall(String url, String authorization) {
        OkHttpClient client=new OkHttpClient();
        return client.newCall(new Request.Builder().url(url).addHeader("Authorization", authorization).get().build());
    }

    @Override
    public String toString() {
        return "UserInfo{userID=%d, musicScore=%d, lvRatio=%d, rankNation=%d, comboPercent=%d, sex=%d, userName='%s', headimgURL='%s', phone='%s', cityName='%s', teamName='%s', titleUrl='%s', headimgBoxPath='%s'}".formatted(userID, musicScore, lvRatio, rankNation, comboPercent, sex, userName, headimgURL, phone, cityName, teamName, titleUrl, headimgBoxPath);
    }

}