package com.lin.calc;

import com.google.gson.JsonObject;

/**
 * 最近记录音乐信息
 */
public class RecentMusicInfo extends MusicInfo {
    private int difficulty;
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
    public int getId(){
        return this.id;
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

    public int getDifficulty() {
        return difficulty;
    }
}
