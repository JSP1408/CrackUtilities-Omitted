package org.e11eman.crackutilities.commands;

public class ItemImageRendererCommand extends Command {
    public ItemImageRendererCommand() {
        super("IImageRenderer", "Lets you render images on the item you are currently holdings tooltip", "\nIImageRenderer <URL> <width> <height> <optionalChar>");
    }

    @Override
    public void execute(ArrayList<String> arguments) {
        int width = Integer.parseInt(arguments.get(1));
        int height = Integer.parseInt(arguments.get(2));


        try {
            BufferedImage downloadedImage = ImageUtilities.urlToImage(new URL(arguments.get(0)));
            BufferedImage resizedImage = ImageUtilities.resize(downloadedImage, width, height);
            ItemScreen image;

            if(arguments.size() == 4) {
                image = new ItemScreen(width,height, arguments.get(3));
            } else {
                image = new ItemScreen(width,height, null);
            }


            for(int y = 0; y < resizedImage.getHeight(); y++) {
                for(int x = 0; x < resizedImage.getWidth(); x++) {
                    int RGBA = resizedImage.getRGB(x, y);
                    int red = (RGBA >> 16) & 255;
                    int green = (RGBA >> 8) & 255;
                    int blue = RGBA & 255;

                    image.screen[x][y] = String.format("#%02x%02x%02x", red, green, blue);
                }
            }

            image.draw();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
