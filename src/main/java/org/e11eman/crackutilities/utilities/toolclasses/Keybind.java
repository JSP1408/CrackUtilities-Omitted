package org.e11eman.crackutilities.utilities.toolclasses;

public abstract class Keybind {
    abstract public void execute();
    public String name;

    public Keybind(String name) {
        this.name = name;
    }
}
