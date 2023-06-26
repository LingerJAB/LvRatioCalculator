package com.lin.calc;

import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * 排名记录音乐信息
 */
public class RankMusicInfo extends MusicInfo {
    private boolean official;
    private final ArrayList<SingleRank> accList = new ArrayList<>();

    public static RankMusicInfo get(JsonObject object) {
        RankMusicInfo musicInfo = new RankMusicInfo();
        musicInfo.id = object.get("MusicID").getAsInt();
        musicInfo.name = object.get("Name").getAsString();
        musicInfo.official = object.get("OwnerType").getAsInt()==1;
        object.get("ItemRankList").getAsJsonArray().forEach(element -> {
            JsonObject obj = element.getAsJsonObject();
            float acc = obj.get("PlayerPercent").getAsFloat() / 100;
            int level = obj.get("MusicRank").getAsInt();
            int difficulty= obj.get("MusicLevOld").getAsInt();
            musicInfo.accList.add(new SingleRank(difficulty,level, acc));
        });
        return musicInfo;
    }

    public String getName() {
        return name;
    }
    public int getId(){
        return this.id;
    }


    public boolean isOfficial() {
        return official;
    }

    public float getBestRatio() {
        return getBestInfo().ratio;
    }

    public SingleRank getBestInfo(){
        accList.sort((o1, o2) -> o1.ratio>o2.ratio ? -1 : (o1.ratio==o2.ratio ? 0 : 1));
//        return Math.round(accList.get(0).ratio);
        return accList.get(0);

    }

    @Override
    public String toString() {
        return "Name: %s\n#Ratio: %s\n#BestRatio: %.2f\n".formatted(name, accList, getBestRatio());
    }


}
