package org.e11eman.crackutilities.commands;

public class ScreenShareCommand extends Command {
    public int fps = 10;
    public int width = 35;
    public int height = 18;
    public ChatScreen chatScreen = new ChatScreen(width, height, null);
    public String type = "chat";
    public boolean enabled = false;
    public Timer loop = new Timer();
    public Robot robot;
    public TextScreen textScreen;
    public boolean compact = false;

    {
        try {
            System.setProperty("java.awt.headless", "false");
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public ScreenShareCommand() {
        super("screenshare", "Draw your screen to minecraft via chat or text displays", "\nscreenshare <set> <chat/screen>\nscreenshare <setDimensions> <width> <height>\nscreenshare <start/stop>\nscreenshare <setFps> <fps>\nscreenshare <fixScreen>\nscreenshare <setSelector> <selector>\nscreenshare <compactMode>");

        CClient.events.register("closeWorld", "ssClose", (Event) -> {
            loop.purge();
            loop.cancel();

            loop = new Timer();

            enabled = false;

            if(Objects.equals(type, "screen")) {
                for(String i : textScreen.tags) {
                    CClient.commandCoreSystem.run("kill @e[tag=" + i + "]");
                }
            }
        });
    }

    @Override
    public void execute(ArrayList<String> arguments) {
        switch (arguments.get(0)) {
            case "set" -> {
                switch (arguments.get(1)) {
                    case "chat" -> {
                        type = "chat";

                        Player.alertClient(MessagePresets.trueTextPreset("Successfully set mode to chat!"));
                    }

                    case "screen" -> {
                        type = "screen";
                        Player.alertClient(MessagePresets.trueTextPreset("Successfully set mode to screen!"));
                    }

                    default -> Player.alertClient(MessagePresets.falseTextPreset("Unknown mode! Modes are chat or screen!"));
                }
            }

            case "fixScreen" -> {
                textScreen.clear();
                textScreen.update();
                textScreen.draw();
            }

            case "setDimensions" -> {
                try {
                    width = Integer.parseInt(arguments.get(1));
                    height = Integer.parseInt(arguments.get(2));

                    chatScreen.width = Integer.parseInt(arguments.get(1));
                    chatScreen.height = Integer.parseInt(arguments.get(2));

                    chatScreen.screen = new String[width][height];

                    chatScreen.clear();
                } catch (NumberFormatException e) {
                    Player.alertClient(MessagePresets.falseTextPreset("Incorrect dimensions set! Must be of type Integer!"));
                    return;
                }

                Player.alertClient(MessagePresets.trueTextPreset("Successfully set dimensions!"));
            }

            case "setFps" -> {
                try {
                    fps = Integer.parseInt(arguments.get(1));
                } catch (NumberFormatException e) {
                    Player.alertClient(MessagePresets.falseTextPreset("Incorrect fps set! Must be of type Integer"));
                    return;
                }

                Player.alertClient(MessagePresets.trueTextPreset("Successfully set fps!"));
            }

            case "start" -> {
                if(enabled) {
                    Player.alertClient(MessagePresets.falseTextPreset("Screenshare already running!"));
                } else {
                    enabled = true;
                    Player.alertClient(MessagePresets.trueTextPreset("Screenshare running!"));
                    String finalType = type;

                    switch (finalType) {
                        case "chat" -> loop.scheduleAtFixedRate(new TimerTask() {
                            @Override
                            public void run() {
                                setScreen(chatScreen);
                            }
                        },0, 1000 / fps);

                        case "screen" -> {
                            Vec3d position = Player.getPosition();

                            if(compact) {
                                textScreen = new TextScreen(width, height, position.getX(), position.getY(), position.getZ(), "▪");
                                textScreen.separation = 0.0375f;
                            } else {
                                textScreen = new TextScreen(width, height, position.getX(), position.getY(), position.getZ(), "█");
                                textScreen.separation = 0.125f;
                            }

                            textScreen.clear();
                            textScreen.update();

                            loop.scheduleAtFixedRate(new TimerTask() {
                                @Override
                                public void run() {
                                    setScreen(textScreen);
                                }
                            },0, 1000 / fps);
                        }
                    }


                }
            }

            case "stop" -> {
                if(enabled) {
                    loop.purge();
                    loop.cancel();

                    loop = new Timer();

                    Player.alertClient(MessagePresets.falseTextPreset("Screenshare finished!"));
                    enabled = false;
                } else {
                    Player.alertClient(MessagePresets.falseTextPreset("No screen instance running!"));
                    return;
                }


                if(Objects.equals(type, "screen")) {
                    for(String i : textScreen.tags) {
                        CClient.commandCoreSystem.run("kill @e[tag=" + i + "]");
                    }
                }
            }

            case "setSelector" -> {
                chatScreen.selector = arguments.get(1);
                Player.alertClient(MessagePresets.trueTextPreset("Selector set!"));
            }

            case "compactMode" -> {
                if(compact) {
                    Player.alertClient(MessagePresets.falseTextPreset("Compact mode disabled!"));
                    compact = false;
                } else {
                    Player.alertClient(MessagePresets.trueTextPreset("Compact mode enabled!"));
                    compact = true;
                }
            }

            default -> Player.alertClient(MessagePresets.falseTextPreset("Unknown action! See help command for help!"));
        }
    }

    public void setScreen(Screen screen) {
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

        BufferedImage capture = robot.createScreenCapture(screenRect);
        BufferedImage resized = ImageUtilities.resize(capture, chatScreen.width, chatScreen.height);

        int[] pixels = resized.getRGB(0, 0, resized.getWidth(), resized.getHeight(), null, 0, resized.getWidth());
        for (int y = 0; y < resized.getHeight(); y++) {
            for (int x = 0; x < resized.getWidth(); x++) {
                int pixel = pixels[y * resized.getWidth() + x];
                int red = (pixel >> 16) & 255;
                int green = (pixel >> 8) & 255;
                int blue = pixel & 255;

                screen.screen[x][y] = String.format("Format removed for the funnies", red, green, blue);
            }
        }

        screen.draw();
    }
}