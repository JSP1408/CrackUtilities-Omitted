package org.e11eman.crackutilities.keybinds;

public class RandomRightClickKeybind extends Keybind {
    public boolean enabled = false;
    public SecureRandom secureRandom = new SecureRandom();

    public RandomRightClickKeybind() {
        super("RRClickL");
    }

    @Override
    public void execute() {
        if(enabled) {
            CClient.events.unregisterInstance("tick", "rrcl");
            enabled = false;

            Player.alertClient(MessagePresets.falseTextPreset("RRCL disabled"));
        } else {
            CClient.events.register("tick", "rrcl", (Event) -> {
                if(Player.inWorld) {
                    Player.setYaw(secureRandom.nextFloat(-180, 180));
                    Player.setPitch(secureRandom.nextFloat(-90, 90));

                    Player.rightClick(Hand.MAIN_HAND);
                }
            });

            enabled = true;

            Player.alertClient(MessagePresets.trueTextPreset("RRCL enabled"));
        }
    }
}
