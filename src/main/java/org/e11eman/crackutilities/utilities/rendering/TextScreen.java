package org.e11eman.crackutilities.utilities.rendering;
public class TextScreen extends Screen {
    public double x;
    public double y;
    public double z;
    public String tag = "omittedtag";
    public ArrayList<String> tags = new ArrayList<>();
    public float separation = 0.125f;

    public TextScreen(int width, int height, double x, double y, double z, String character) {
        super(width, height, character);

        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void draw() {
        ArrayList<JsonArray> names = new ArrayList<>();


        for(int y =0; y < height; y++) {
            JsonArray name = new JsonArray();

            for(int x = 0; x < width; x++) {
                JsonObject pixel = new JsonObject();

                pixel.add("text", new JsonPrimitive(this.character));
                pixel.add("color", new JsonPrimitive(screen[x][y]));

                name.add(pixel);
            }

            names.add(name);
        }

        for(int i = 0; i < names.size(); i++) {
            CClient.commandCoreSystem.run("data merge entity @e[tag=" + tags.get(i) + ",limit=1] {text:'" + names.get(i).getAsJsonArray() + "'}");
        }
    }

    public void update() {
        double startY = y;

        for(String i : tags) {
            CClient.commandCoreSystem.run("kill @e[tag=" + i + "]");
        }

        tags.clear();

        for(int i = 0; i < this.height; i++) {
            String randomizedTag = tag +  SecureRandomStuff.getRandomString(6) + Player.getPlayer().getGameProfile().getName();

            tags.add(randomizedTag);
            startY -= separation;

            CClient.commandCoreSystem.run(String.format("summon text_display %s %s %s %s", x + 18, startY, z + 18,
                    "{Tags:[\"" + randomizedTag + "\"],text:'{\"text\":\"placeholder\"}',see_through: 1b,line_width: 64000, background: false, text_opacity:-1, transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[0.5f,0.5f,0.5f]}}"));

            CClient.commandCoreSystem.run(
                    //Removed for preservation of exploit ;)
                    System.exit(0);
            );
        }
    }
}