package org.e11eman.crackutilities.keybinds;

import org.e11eman.crackutilities.utilities.CClient;
import org.e11eman.crackutilities.utilities.MessagePresets;
import org.e11eman.crackutilities.utilities.toolclasses.Keybind;
import org.e11eman.crackutilities.wrappers.Player;

public class SpinKeybind extends Keybind {
    public boolean enabled = false;

    public SpinKeybind() {
        super("Spin");
    }

    @Override
    public void execute() {
        if(enabled) {
            CClient.events.unregisterInstance("tick", "spin");
            enabled = false;

            Player.alertClient(MessagePresets.falseTextPreset("Spin disabled"));
        } else {
            CClient.events.register("tick", "spin", (Event) -> {
                if(Player.inWorld) {
                    double increment = CClient.configSystem.getCategory(
                            CClient.configSystem.getCategory(
                                    CClient.configSystem.getConfig(), "keyBindings"
                            ), "spin"
                    ).get("increment").getAsDouble();

                    Player.setYaw((float) (Player.getYaw() + increment));
                }
            });

            enabled = true;

            Player.alertClient(MessagePresets.trueTextPreset("Spin enabled"));
        }
    }
}
