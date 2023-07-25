package org.e11eman.crackutilities.utilities.rendering;
public class ItemScreen extends Screen{
    public ItemScreen(int width, int height, String character) {
        super(width, height, character);
    }

    @Override
    public void draw() {
        JsonArray rawLore = new JsonArray();
        ItemStack item = Player.getPlayer().getInventory().getMainHandStack();

        NbtCompound nbt = item.getOrCreateNbt();
        NbtCompound nbtDisplay = nbt.getCompound(ItemStack.DISPLAY_KEY);

        NbtList nbtLore = new NbtList();

        JsonObject disableItalics = new JsonObject();

        disableItalics.add("text", new JsonPrimitive(""));
        disableItalics.add("italic", new JsonPrimitive(false));

        for(int y =0; y < height; y++) {
            rawLore.add(disableItalics);

            for(int x = 0; x < width; x++) {
                JsonObject pixel = new JsonObject();

                pixel.add("text", new JsonPrimitive(this.character));
                pixel.add("color", new JsonPrimitive(screen[x][y]));

                rawLore.add(pixel);
            }

            nbtLore.add(NbtString.of(
                    rawLore.toString()
            ));

            rawLore = new JsonArray();
        }

        nbtDisplay.put(ItemStack.LORE_KEY, nbtLore);

        JsonObject name = new JsonObject();

        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        int day = date.getDayOfMonth();
        int month = date.getMonthValue();
        int year = date.getYear();
        int hour = time.getHour();
        int minute = time.getMinute();
        int second = time.getSecond();

        name.add("text", new JsonPrimitive(String.format("Created %s/%s/%s at %s:%s:%s",
                day,
                month,
                year,
                hour,
                minute,
                second
                ))
        );

        name.add("color", new JsonPrimitive("#808080"));

        nbtDisplay.put(ItemStack.NAME_KEY, NbtString.of(name.toString()));
        nbt.put(ItemStack.DISPLAY_KEY, nbtDisplay);

        item.setNbt(nbt);

        Player.setMainHand(item);
    }
}
