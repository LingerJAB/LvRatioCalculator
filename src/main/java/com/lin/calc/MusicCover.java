package com.lin.calc;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Objects;

public class MusicCover {
    public static int i=1;
    public static void main(String[] args) throws IOException{
        String json = Files.readString(Path.of("C:\\Users\\Lin\\IdeaProjects\\JavaLearnings\\src\\all\\GoodsMusic.json"));

        OkHttpClient client=new OkHttpClient();
        for(JsonElement element : JsonParser.parseString(json).getAsJsonObject().get("List").getAsJsonArray()) {
            GoodsMusic music = new Gson().fromJson(element.getAsJsonObject(), GoodsMusic.class);
            saveGoodsImg(client, music);
        }

        System.out.println("\n# All right!");

//        System.out.println(new Gson().toJson(musicSet));

    }


    private static HashSet<OfficialMusic> getMusicSet(int index, int page, int size) {
        String url = "https://dancedemo.shenghuayule.com/Dance/api/User/GetMusicRankingNew?musicIndex=%d&pagesize=%d&page=%d".formatted(index, size, page);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();
        String musicJsons = "";
        try {
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();
            musicJsons = body.string();
            body.close();
        } catch(IOException e) {
            e.printStackTrace();
        }

        HashSet<OfficialMusic> musicHashSet = new HashSet<>();
        JsonObject object = JsonParser.parseString(musicJsons).getAsJsonObject();
        int num = 0;
        for(JsonElement element : object.get("List").getAsJsonArray()) {
            Gson gson = new Gson();
            OfficialMusic music = gson.fromJson(element.getAsJsonObject(), OfficialMusic.class);
            System.out.println(++num + ": " + music);
            musicHashSet.add(music);
        }
        return musicHashSet;
    }

    public static void saveOffcialImg(OfficialMusic music) {
        String name = music.getName();
        int id = music.getId();
        String url = music.getCoverUrl();

        try {
            Response response = new OkHttpClient().newCall(new Request.Builder().url(url).build()).execute();

            File file = new File("C:\\Users\\Lin\\IdeaProjects\\JavaLearnings\\src\\officialImg\\" + id + ".jpg");
            if(file.exists()) {
                System.out.println("#" + id + " " + name + " 已存在，未保存");
            } else {
                InputStream inputStream = response.body().byteStream();
                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(inputStream.readAllBytes());
                outputStream.close();
                System.out.println("#" + id + " " + name + " 已保存");
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    public static void saveGoodsImg(OkHttpClient client,GoodsMusic music) {
        if(i++<400) return;
        String name = music.getName();
        int id = music.getId();
        String url = music.getCoverUrl();

        try {
            Response response = client.newCall(new Request.Builder().url(url).build()).execute();

            File file = new File("C:\\Users\\Lin\\IdeaProjects\\JavaLearnings\\src\\goodsImg\\" + id + ".jpg");
            int num=i++;
            if(file.exists()) {
                System.out.println("["+num+"]#" + id + " " + name + " 已存在，未保存");
            } else {
                ResponseBody responseBody = response.body();
                InputStream inputStream = responseBody.byteStream();
                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(inputStream.readAllBytes());
                responseBody.close();
                outputStream.close();
                System.out.println("["+num+"]#" + id + " " + name + " 已保存");
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}

class OfficialMusic {
    @SerializedName("MusicID")
    private final int id;
    @SerializedName("Name")
    private final String name;
    @SerializedName("Cover")
    private final String coverUrl;
//    private final ArrayList<Integer> levels;

    public OfficialMusic(int id, String name, String coverUrl) {
        this.id = id;
        this.name = name;
        this.coverUrl = coverUrl;
//        this.levels = levels;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

//    public ArrayList<Integer> getLevels() {
//        return levels;
//    }

    @Override
    public boolean equals(Object o) {
        if(this==o) return true;
        if(o==null || getClass()!=o.getClass()) return false;

        OfficialMusic music = (OfficialMusic) o;

        if(id!=music.id) return false;
        return Objects.equals(name, music.name);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name!=null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OfficialMusic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                '}';
    }
}

class GoodsMusic {
    @SerializedName("MusicID")
    private final int id;
    @SerializedName("GoodsName")
    private final String name;
    @SerializedName("PicPath")
    private final String coverUrl;

    public GoodsMusic(int id, String name, String coverUrl) {
        this.id = id;
        this.name = name;
        this.coverUrl = coverUrl;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCoverUrl() {
        return coverUrl;
    }


    @Override
    public boolean equals(Object o) {
        if(this==o) return true;
        if(o==null || getClass()!=o.getClass()) return false;

        GoodsMusic music = (GoodsMusic) o;

        if(id!=music.id) return false;
        return Objects.equals(name, music.name);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name!=null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GoodsMusic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                '}';
    }
}