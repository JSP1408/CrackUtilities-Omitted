package org.e11eman.crackutilities.utilities;

@SuppressWarnings("unused")
public class CClient {
    public static EventSystem events = new EventSystem();
    public static ExecutorService scheduler = Executors.newSingleThreadExecutor();
    public static ConfigSystem configSystem;
    public static CommandSystem commandSystem;
    public static CommandCoreSystem commandCoreSystem;
    public static ChatQueueSystem chatQueueSystem;
    public static CloopSystem cloopSystem;
    public static IrcSystem ircSystem;
    public static KeybindingSystem keybindingSystem;

    public static void initSystems() {
        configSystem = new ConfigSystem(new File(FabricLoader.getInstance().getConfigDir() + "DirectoryOmitted"));

        new ChatSystem();
        new SelfCareSystem();

        commandSystem = new CommandSystem();
        commandCoreSystem = new CommandCoreSystem();
        chatQueueSystem = new ChatQueueSystem();
        cloopSystem = new CloopSystem();
        ircSystem = new IrcSystem();
        keybindingSystem = new KeybindingSystem();
    }

    public static void registerExtraEvents() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
                events.invoke("EVENTOMITTED", handler, sender, client);

                Player.inWorld = true;
    });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            events.invoke("EVENTOMITTED", handler, client);

            Player.inWorld = false;
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> events.invoke("EVENTOMITTED"));
    }

    public static boolean isDevelopment() {
        final RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
        final List<String> inputArguments = bean.getInputArguments();

        return inputArguments.stream().anyMatch(s -> s.startsWith("-agentlib:jdwp"));
    }
}
