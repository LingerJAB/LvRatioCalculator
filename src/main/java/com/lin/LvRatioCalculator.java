package com.lin;

import com.google.gson.*;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class LvRatioCalculator {
    public static void main(String[] args) throws IOException {

        //Input Your DanceCubeBase Bearer Token Here
        String auth = "bearer " + "iDQH1yM8yK_WhbZffU_GU641aYxUJXnFyiZFF8iSe-taI3Fk5EnyBeFMuAn00A8uuRWTy_DIzNaWi1hygQQLU_QiROBC6ztsAvxd8Itz-djHQOrth4EQOnLcwTSEBz2JsF5lSb4_14QwxJrzG9UzGcMhPXZ-A5yLR3da21VzXYbzKgcLi2_ykNWKTnJhqBa6V4iRIZUdFx_bittJWpw8XOzpB4n8lLcYZGcu4YEsGvYLTrqnHn41fpbN-NpPrQaxZfC7I4GYJV9sPdoZMrdnlVQUyHuEY4kaw5FF7QNofrtXhjCuZE2ejdNiGulNRJpIIdc43mzA1j7O-XghsXY4XuuAfSajn79GGwg-7N146clzPt7v4PHIoyDOixlo5JLVEReEk8MjEYowuKICoSeA3z5rvLe45zH8bYtN0ojxSIZ_VyGqRWBR1LMTYlq98Yf_M8RBBVOt98pj0aXL5SoJl8fm2bo0CK-i-RZXu0fKbG8oxf107keXHbEGZ274c16k-IhIfvy1J_5QoomvUglMY0sSY4dgwk1UhBHd8phpiayQIFECdH_WV9CwehWliK2BZwYwl2Xh6IRmmUqsY2XI3zLQnzq52SwBnWx-Nu78hPjAKnNZFlTlE0PfSYa6xcSzRz3aci-mY-m9pLp1t3oJDxTSLR5Pw9UdxcWZ-D9C-CfRim-OzLVBrtmn0dEF07XNYYPTZwxq8DZwfAi-Xa5at7mHfOWDt1RXVCvwhAf8uYKcq12zp4uaZDXmjJefSpx5-524g1IOwpcpswYGZIhdfclfI-_fIgY_t7ciFl9zmYOgvgf60u6LGkm1KeycHztfFFQAJrBU3REIOWDqYHNL5oUYk-CaM2mEKYzb1DoITNBENuqicDb3_whU0IryMEiK";
        ArrayList<RankMusicInfo> allRankList = getAllRankList(auth, true);
        ArrayList<RecentMusicInfo> allRecentList = getAllRecentList(auth, true);
        ArrayList<RankMusicInfo> rank15List = new ArrayList<>(getSubRank15List(allRankList));
        ArrayList<RecentMusicInfo> recent15List = new ArrayList<>(getSubRecent15List(allRecentList));

        //Best 15
        System.out.println("#The Best 15 Music");
        for(int i = 0, rank15ListSize = rank15List.size(); i<rank15ListSize; i++) {
            RankMusicInfo musicInfo = rank15List.get(i);
            System.out.printf("#%d Name:%s, Ratio: %.2f\n", i + 1, musicInfo.getName(), musicInfo.getBestRatio());
        }

        //Recent 15
        System.out.println("\n\n#The Recent 15 Music");
        for(int i = 0, recent15ListSize = recent15List.size(); i<recent15ListSize; i++) {
            RecentMusicInfo musicInfo = recent15List.get(i);
            System.out.printf("#%d Name:%s, Ratio: %.2f\n", i + 1, musicInfo.getName(), musicInfo.getBestRatio());
        }

        //Sum up
        System.out.println("\n# Sum #");
        float best15Avg = average(rank15List);
        float recent15Avg = average(recent15List);
        System.out.printf("Best 15 Ratio: %.2f\n", best15Avg);
        System.out.printf("Recent 15 Ratio: %.2f\n", recent15Avg);
        System.out.println("Your average ratio: " + (best15Avg + recent15Avg) / 2);


    }

    private static <T extends MusicInfo> float average(ArrayList<T> legacyInfos) {
        float sum = 0;
        for(MusicInfo info : legacyInfos) {
            sum = info.getBestRatio() + sum;
        }
        return sum / legacyInfos.size();
    }

    private static ArrayList<RankMusicInfo> getCategoryRankList(String json, boolean officialOnly) {
        List<JsonElement> list = JsonParser.parseString(json).getAsJsonArray().asList();
        ArrayList<RankMusicInfo> infos = new ArrayList<>();
        list.forEach(element -> {
            RankMusicInfo musicInfo = RankMusicInfo.get(element.getAsJsonObject());
            //仅官谱计入
            if(officialOnly) {
                if(musicInfo.isOfficial())
                    infos.add(musicInfo);
            } else {
                infos.add(musicInfo);
            }

        });
        return infos;
    }

    public static ArrayList<RankMusicInfo> getAllRankList(String auth, boolean officialOnly) throws IOException {

        OkHttpClient client = new OkHttpClient();
        String url = "https://dancedemo.shenghuayule.com/Dance/api/User/GetMyRankNew?musicIndex=";
        String json;
        ArrayList<RankMusicInfo> musicInfos = new ArrayList<>();

        for(int i = 1; i<=6; i++) {
            Request request = new Request.Builder().url(url + i).get().addHeader("Authorization", auth).build();
            Response response = client.newCall(request).execute();

            ResponseBody body = response.body();
            json = body.string();
            body.close();
            musicInfos.addAll(getCategoryRankList(json, officialOnly));
        }
        return musicInfos;
    }

    public static List<RankMusicInfo> getSubRank15List(ArrayList<RankMusicInfo> list) {
        ((List<RankMusicInfo>) list).sort((o1, o2) -> o1.getBestRatio()>o2.getBestRatio() ? -1 : (o1.getBestRatio()==o2.getBestRatio() ? 0 : 1));
        return ((List<RankMusicInfo>) list).subList(0, 15);
    }


    public static ArrayList<RecentMusicInfo> getAllRecentList(String auth, boolean officialOnly) throws IOException {

        OkHttpClient client = new OkHttpClient();
        String url = "https://dancedemo.shenghuayule.com/Dance/api/User/GetLastPlay";
        String json;

        Request request = new Request.Builder().url(url).get().addHeader("Authorization", auth).build();
        Response response = client.newCall(request).execute();

        ResponseBody body = response.body();
        json = body.string();
        body.close();

        ArrayList<RecentMusicInfo> musicInfoList = new ArrayList<>();
        for(JsonElement element : JsonParser.parseString(json).getAsJsonArray()) {
            RecentMusicInfo musicInfo = RecentMusicInfo.get(element.getAsJsonObject());

            if(officialOnly & Official.OFFICIAL_ID.contains(musicInfo.id))
                musicInfoList.add(musicInfo);
            else
                musicInfoList.add(musicInfo);
        }
        return musicInfoList;
    }

    public static List<RecentMusicInfo> getSubRecent15List(ArrayList<RecentMusicInfo> list) {
        //        musicInfos.sort((o1, o2) -> o1.getBestRatio()>o2.getBestRatio() ? -1 : (o1.getBestRatio()==o2.getBestRatio() ? 0 : 1));
        return ((List<RecentMusicInfo>) list).subList(0, 15);
    }

}


abstract class MusicInfo {
    int id;
    String name;


    abstract float getBestRatio();

}

/**
 * 最近记录音乐信息
 */
class RecentMusicInfo extends MusicInfo {
    private int level;
    private float acc;

    //TODO

    public static RecentMusicInfo get(JsonObject object) {
        RecentMusicInfo musicInfo = new RecentMusicInfo();
        musicInfo.id = object.get("MusicID").getAsInt();
        musicInfo.name = object.get("MusicName").getAsString();
        musicInfo.level = object.get("MusicLevel").getAsInt();
        musicInfo.acc = object.get("PlayerPercent").getAsFloat() / 100;
        return musicInfo;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public float getAcc() {
        return acc;
    }

    public float getBestRatio() {
//        return Math.round((level + 2) * getAcc());
        return (level + 2) * getAcc();
    }

    @Override
    public String toString() {


        return String.format("""
                Name: %s
                Level: %d
                Percent: %.2f
                #Ratio: %.2f
                """, name, level, getAcc(), getBestRatio());
    }
}

/**
 * 排名记录音乐信息
 */
class RankMusicInfo extends MusicInfo {
    private boolean official;
    private final ArrayList<SingleRank> accList = new ArrayList<>();

    public static RankMusicInfo get(JsonObject object) {
        RankMusicInfo musicInfo = new RankMusicInfo();
        musicInfo.id = object.get("MusicID").getAsInt();
        musicInfo.name = object.get("Name").getAsString();
        musicInfo.official = object.get("OwnerType").getAsInt()==1;
        object.get("ItemRankList").getAsJsonArray().forEach(element -> {
            float acc = element.getAsJsonObject().get("PlayerPercent").getAsFloat() / 100;
            int level = element.getAsJsonObject().get("MusicRank").getAsInt();
            musicInfo.accList.add(new SingleRank(level, acc));
        });
        return musicInfo;
    }

    public String getName() {
        return name;
    }

    public boolean isOfficial() {
        return official;
    }

    public float getBestRatio() {
        accList.sort((o1, o2) -> o1.ratio>o2.ratio ? -1 : (o1.ratio==o2.ratio ? 0 : 1));
//        return Math.round(accList.get(0).ratio);
        return accList.get(0).ratio;
    }

    @Override
    public String toString() {
        return "Name: %s\n#Ratio: %s\n#BestRatio: %.2f\n".formatted(name, accList, getBestRatio());
    }


}

class SingleRank {
    int level;
    float acc;
    float ratio;

    public SingleRank(int level, float acc) {
        this.level = level;
        this.acc = acc;
        this.ratio = (level + 2) * acc;
    }


    @Override
    public String toString() {
        return "{level: " + level + ", acc:" + acc + ", ratio=" + ratio + "}";
    }
}

