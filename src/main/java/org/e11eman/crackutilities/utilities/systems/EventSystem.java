package org.e11eman.crackutilities.utilities.systems;
@SuppressWarnings("unused")
public class EventSystem {
    private final HashMap<String, HashMap<String, VarargsConsumer<Object>>> events = new HashMap<>();

    public void register(String event, String eventInstanceName, VarargsConsumer<Object> eventInvoke) {
        if (events.containsKey(event)) {
            events.get(event).put(eventInstanceName, eventInvoke);
        } else {
            HashMap<String, VarargsConsumer<Object>> newEvent = new HashMap<>();
            newEvent.put(eventInstanceName, eventInvoke);

            events.put(event, newEvent);
        }
    }

    public void unregister(String event) {
        events.remove(event);
    }

    public void unregisterInstance(String event, String eventInstance) {
        if (events.containsKey(event)) {
            events.get(event).remove(eventInstance);
        }
    }

    public void invoke(String event, Object... values) {
        if (events.containsKey(event)) {
            for (VarargsConsumer<Object> i : events.get(event).values()) {
                try {
                    i.accept(values);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
