package org.e11eman.crackutilities.utilities.systems;
public class ChatQueueSystem {
    private final ArrayList<String> messages = new ArrayList<>();
    private Timer queue = new Timer();

    public ChatQueueSystem() {
        CClient.events.register("openWorld", "chatQueueOpenWorld",  (Event) -> queue.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!messages.isEmpty()) {
                    String message = messages.get(messages.size() - 1);
                    Player.sendChat(message);
                    messages.remove(messages.size() - 1);
                }
            }
        }, 0, (long) CClient.configSystem.getCategory(CClient.configSystem.getConfig(), "chatQueueSystem").get("delay").getAsDouble()));

        CClient.events.register("closeWorld", "chatQueueCloseWorld",  (Event) -> {
           queue.purge();
           queue.cancel();

           messages.clear();

           queue = new Timer();
        });
    }

    public void addMessageToQueue(String message) {
        messages.add(message);
    }
}
