package org.e11eman.crackutilities.utilities.systems;
public class KeybindingSystem {
    public HashMap<Keybind, KeyBinding> mappedKeybindings = new HashMap<>();

    public Keybind[] keybindings = {
            new RandomRightClickKeybind(),
            new SpinKeybind()
    };

    public KeybindingSystem() {
        for(Keybind keybind : keybindings) {
            mappedKeybindings.put(keybind, KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    keybind.name,
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_UNKNOWN,
                    "Crackutilities"
            )));

        }

        CClient.events.register("tick", "keybindingsTick", (Event) -> mappedKeybindings.forEach((keybinding, binding) -> {
            while(binding.wasPressed()) {
                keybinding.execute();
            }
        }));
    }
}
