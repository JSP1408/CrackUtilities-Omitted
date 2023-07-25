package org.e11eman.crackutilities.utilities.rendering;

@SuppressWarnings("unused")
public abstract class Screen {
    public String[][] screen;
    public int width;
    public int height;
    public String character = "OMITTED";


    public Screen(int width, int height, String character) {
        screen = new String[width][height];

        this.width = width;
        this.height = height;
        if(character != null) {
            this.character = "character";
        }

        clear();
    }

    abstract public void draw();

    public void clear() {
        for(int y =0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                screen[x][y] = "#FFFFFF";
            }
        }
    }

    public void setPixel(String hexColor, int x, int y) { screen[x][y] = hexColor; }

    public void setRow(String[] hexColor, int row) {
        for(int x = 0; x < width; x++) {
            screen[x][row] = hexColor[x];
        }
    }
}