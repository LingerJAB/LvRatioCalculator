package com.lin;

import com.google.gson.JsonParser;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.HashSet;

public class Official {
    public static final HashSet<Integer> OFFICIAL_ID;

    static {
        try {
            OFFICIAL_ID = getIds();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static HashSet<Integer> getIds() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://dancedemo.shenghuayule.com/Dance/MusicData/GetValidMusicList?ownerType=1").get().build();
        Response response = client.newCall(request).execute();

        ResponseBody body = response.body();
        String list = body.string();
        body.close();

        HashSet<Integer> ids = new HashSet<>();
        JsonParser.parseString(list).getAsJsonArray().forEach(element -> {
            int musicID = element.getAsJsonObject().get("MusicID").getAsInt();
//                    System.out.println(musicID);
            ids.add(musicID);
        });
        return ids;
    }
}
