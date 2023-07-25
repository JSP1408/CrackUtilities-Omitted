package org.e11eman.crackutilities.utilities;
public class ImageUtilities {
    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public static BufferedImage urlToImage(URL imageUrl) {
        BufferedImage image;
        try {
            image = ImageIO.read(imageUrl);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image from URL: " + imageUrl, e);
        }
        return image;
    }
}
