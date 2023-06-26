package com.lin.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

public class ImageDrawer {
    private final BufferedImage originImage;
    private final Graphics2D graphics;
    private Font font;

    public ImageDrawer(BufferedImage image) {
        originImage = image;
        graphics = originImage.createGraphics();
    }

    public ImageDrawer(File file) {
        try {
            originImage = ImageIO.read(new FileInputStream(file));
            graphics = originImage.createGraphics();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ImageDrawer(String url) {
        try {
            originImage = ImageIO.read(new URL(url));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        graphics = originImage.createGraphics();
    }

    public ImageDrawer color(Color color) {
        graphics.setColor(color);
        return this;
    }

    public ImageDrawer font(String name, int style, int size) {
        this.font = new Font(name, style, size);
        return this;
    }

    public ImageDrawer font(String name, int size) {
        this.font = new Font(name, Font.PLAIN, size);
        return this;
    }

    //Todo font!!!!
    public ImageDrawer font(Font font) {
        this.font = font;
        graphics.setFont(font);
        return this;
    }

    public ImageDrawer font(int style, int size) {
        if(font!=null) {
            this.font = font.deriveFont(style, size);
        }
        return this;
    }

    public ImageDrawer drawText(String text, int x, int y) {
        graphics.setFont(font);
        graphics.drawString(text, x, y);
        return this;
    }

    public ImageDrawer drawText(String text, int x, int y, int spaceHeight) {
        int lineHeight = graphics.getFontMetrics().getHeight();    // 获取文本行高
        for(String line : text.split("\n")) {
            graphics.drawString(line, x, y);
            y += lineHeight + spaceHeight;
        }
        return this;
    }

    public ImageDrawer drawImage(BufferedImage bufferedImage, int x, int y) {
        drawImage(bufferedImage,x,y,null);
        return this;
    }
    public ImageDrawer drawImage(BufferedImage bufferedImage, int x, int y,Effect effect) {
        if(effect!=null) {
            bufferedImage=makeRoundCorner(bufferedImage,effect.arcW,effect.arcH);
        }
        graphics.drawImage(bufferedImage, x, y, null);
        return this;
    }

    public ImageDrawer drawImage(BufferedImage bufferedImage, int x, int y, int width, int height) {
        drawImage(bufferedImage, x, y, width, height, null);
        return this;
    }
    public ImageDrawer drawImage(BufferedImage bufferedImage, int x, int y, int width, int height,Effect effect) {
        if(effect!=null) {
            bufferedImage=makeRoundCorner(bufferedImage,effect.arcW,effect.arcH);
        }
        graphics.drawImage(bufferedImage, x, y, width, height, null);
        return this;
    }

    public ImageDrawer drawImage(String url, int x, int y, int width, int height) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new URL(url));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        graphics.drawImage(image, x, y, width, height, null);
        return this;
    }

    public ImageDrawer dispose() {
        graphics.dispose();
        return this;
    }

    public void save(String formatName, OutputStream outputStream) {
        graphics.dispose();
        try {
            ImageIO.write(originImage, formatName, outputStream);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(String formatName, File file) {
        graphics.dispose();
        try {
            ImageIO.write(originImage, formatName, new FileOutputStream(file));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static BufferedImage read(File file) {
        try {
            return ImageIO.read(file);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage read(InputStream stream) {
        try {
            return ImageIO.read(stream);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static BufferedImage read(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

    }

    private BufferedImage makeRoundCorner(BufferedImage srcImage, int radiusW, int radiusH) {
        int width = srcImage.getWidth();
        int height = srcImage.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.fillRoundRect(0, 0, width, height, radiusW, radiusH);
        g.setComposite(AlphaComposite.SrcIn);
        g.drawImage(srcImage, 0, 0, width, height, null);
        g.dispose();
        return image;
    }


    public static void printAvailableFonts() {
        // 获取系统所有可用字体名称
        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontName = e.getAvailableFontFamilyNames();
        for(String s : fontName) {
            System.out.println(s);
        }
    }

}

class Effect {
    int arcW=-1;
    int arcH=-1;
    int blur=-1;

    public Effect(int arcW, int arcH) {
        this.arcW = arcW;
        this.arcH = arcH;
    }

    public Effect(int arcW, int arcH, int blur) {
        this.arcW = arcW;
        this.arcH = arcH;
        this.blur = blur;
    }
}
