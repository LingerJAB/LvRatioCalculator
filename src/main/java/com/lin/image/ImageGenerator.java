package com.lin.image;

import com.freewayso.image.combiner.ImageCombiner;
import com.google.gson.JsonParser;
import com.lin.calc.*;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.lin.image.ImageDrawer.printAvailableFonts;
import static com.lin.image.ImageDrawer.read;

public class ImageGenerator {
    public static Token token = new Token(939088,
            "blY3jomb2Yj3ltwK4yv799CbRuT5bkpa-f8GJEh5mBWkhJcdx3SWYaovbZKEAPW4_7dd3WG3TCKtSM_YxKoG1wVfRS_We5JszqTXEAClrF1l0rWjee82k65hR31N97Qq-auKb0QcKCKzY_4gOBNVHTcLZxT80_SEOXUgX9JLqUn4tWZjDKmmy427ukpHJv8qg7FecxDLHZgc1l3xL-o7dWmchVYrqSUTe1rJQWvzKHIUni3MDNYO1aBPDI0FyzzKCB7sZYbzp1MarGvgRRHDs-T7Nx3KZOzpIJVewWBOQgwmpaoEo8zBDIMMM_A2riCt8t_oISnrkm6fSruALws3HcYonUJla_vMIS3OouciIvT43T65Q7MOs8tthYess9Si-s2WrZbbfTZoAlBxQJpkP8iPhTpfIxrP-5St2AggfF-fBP9wCKRjaFZXxbxMSjDuRGvxtM85KXAFhGZZ72t2lVKYpcINWE_-zFUv2IH1PN2KdzE8wADpVbVDjVVZwmEm4Qvq8Dt4edOT7IinTMbF6l1AY-dg-u6sswM0abfZXdiEbo78C1fzkVusrDRFGwaMdlWCzPkasncWQ1KWXhGLyJsJFijv7hMH1YElXS99WiI8LgsM8E0KprSzAQSVrZy0A4D8PTP9Yn7Y5Ae59IJDJdMs9juw6I7K-0H5DqNZjk6yjC5OfU8pyMiHWWEX6NzYdH44oKE-UL4i42Yx9GNoG7t2fJirAo6o-LvFH1Z1Nhsvq6fA8603KfVVhUZNhvyMfTV1eOWnN9a_i3L8Iz9UD0keTj_HXRR1euIJpuX-La6rWIgxL_5l12BHG2n0QVE8txm-HaEnp7uuphoKlWjVztJPQdWWPlos1U7FIomMr9fyUfUI821UhXKld4T3oXZAlI2hmljm3Y8VsLdJuNvGbQ",
            "",
            9999);
    public static UserInfo info = UserInfo.get(token);
    public static String path = "C:\\Users\\Lin\\IdeaProjects\\LvRatioCalculator\\src\\Materials\\";
    public static String officialImgPath = "C:\\Users\\Lin\\IdeaProjects\\LvRatioCalculator\\src\\all\\officialImg\\";
    public static File defaultImg = new File(officialImgPath + "default.png");

    public static void main(String... args) throws Exception {

        System.out.println(new File(path + "Card1.png").exists());

        BufferedImage card1 = ImageIO.read(new File(path + "Card1.png"));
        BufferedImage card2 = ImageIO.read(new File(path + "Card2.png"));
        BufferedImage card3 = ImageIO.read(new File(path + "Card3.png"));
        BufferedImage lvSSS = ImageIO.read(new File(path + "SSS.png"));
        BufferedImage lvSS = ImageIO.read(new File(path + "SS.png"));
        BufferedImage lvS = ImageIO.read(new File(path + "S.png"));
        BufferedImage lvA = ImageIO.read(new File(path + "A.png"));
        BufferedImage lvB = ImageIO.read(new File(path + "B.png"));
        BufferedImage lvC = ImageIO.read(new File(path + "C.png"));
        BufferedImage lvD = ImageIO.read(new File(path + "D.png"));
//        BufferedImage cover = ImageIO.read(new File("C:\\Users\\Lin\\IdeaProjects\\LvRatioCalculator\\src\\all\\officialImg\\484.jpg"));

        BufferedImage avatar = ImageIO.read(new URL(info.getHeadimgURL()));
        BufferedImage box = ImageIO.read(new URL(info.getHeadimgBoxPath()));
        BufferedImage title = ImageIO.read(new URL(info.getTitleUrl()));

        BufferedImage backgroundImg = ImageIO.read(new File(path + "Main.png"));
        ImageDrawer drawer = new ImageDrawer(backgroundImg);
//        BufferedImage resultImg = ImageIO.read(new File(path + "result.png"));

        // 个人信息 头像/头衔
        drawer.drawImage(avatar, 34, 180, 174, 174)
                .drawImage(box, -24, 122, 290, 290)
                .drawImage(title, 28, 373, 186, 79);

        // 个人信息 文字
        // TODO 段位
        String text = """
                是铃酱呐~

                段位：%d:%s...
                战队：%s
                战力：%d""".formatted(info.getMusicScore(), info.getTeamName(),info.getLvRatio());
        Font font = new Font("得意黑", Font.PLAIN, 45);
        drawer.color(Color.BLACK)
                .font(font)
                .drawText(text, 245, 189, 0);

        // B15

//        ArrayList<BufferedImage> rankCovers = getRankCovers(token);
        ArrayList<RankMusicInfo> allRankList = LvRatioCalculator.getAllRankList(token.getBearerToken(), true);
        List<RankMusicInfo> rank15List = LvRatioCalculator.getSubRank15List(allRankList);
        int index=0;
        int x0=15,y0=620;
        int dx = 395,dy = 180; //x y延伸长度
        for(int row = 0; row<5; row++) { //列
            for(int col = 0; col<3; col++,index++) { //行
                RankMusicInfo musicInfo = rank15List.get(index);
                BufferedImage cover = getCover(musicInfo.getId());
                SingleRank bestInfo = musicInfo.getBestInfo();
                BufferedImage card= switch(bestInfo.getDifficulty()) {
                    case 0:yield card1;
                    case 1:yield card2;
                    case 2:yield card3;
                    default:
                        throw new IllegalStateException("Unexpected value: " + bestInfo.getDifficulty());
                };
                Effect effect=new Effect(35,35);
                drawer.drawImage(cover, x0+col*dx, y0+row*dy,130,158,effect)
                        .drawImage(card,x0+col*dx-5, y0+row*dy-3);

            }
        }

        drawer.dispose();
        drawer.save("PNG", new File(path + "result.png"));

    }


    @Test
    public void test() throws Exception {
        long currentTimeMillis = System.currentTimeMillis();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().get().url("https://api.lolicon.app/setu/v2?r18=1").build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();
        System.out.println(json);

        String url = JsonParser.parseString(json).getAsJsonObject()
                .get("data").getAsJsonArray().get(0).getAsJsonObject()
                .get("urls").getAsJsonObject()
                .get("original").getAsString();

        File out = new File("C://Users\\Lin\\IdeaProjects\\LvRatioCalculator\\src\\result.png");


        byte[] allBytes = new URL(url).openStream().readAllBytes();
        new FileOutputStream(out).write(allBytes);
        System.out.println(System.currentTimeMillis() - currentTimeMillis + "ms");
    }

    public static ArrayList<BufferedImage> getRankCovers(Token token) {
        ArrayList<RankMusicInfo> allRankList = LvRatioCalculator.getAllRankList(token.getBearerToken(), true);
        List<RankMusicInfo> rank15List = LvRatioCalculator.getSubRank15List(allRankList);
        ArrayList<BufferedImage> covers = new ArrayList<>();
        for(RankMusicInfo musicInfo : rank15List) {
            BufferedImage file = getCover(musicInfo.getId());
            covers.add((file));
        }
        return covers;
    }

    public static BufferedImage getCover(int id) {
        File file = new File(officialImgPath + id + ".jpg");
        try {
            if(file.exists()) {
                return ImageIO.read(file);
            } else {
                File defaultImg = new File(officialImgPath + "default.png");
                if(!defaultImg.exists()) {
                    throw new RuntimeException(defaultImg.getAbsolutePath() + "\n默认文件不存在！");
                }
                return ImageIO.read(defaultImg);
            }
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}