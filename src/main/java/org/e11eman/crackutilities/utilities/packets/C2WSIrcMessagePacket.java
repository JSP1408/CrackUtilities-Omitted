package org.e11eman.crackutilities.utilities.packets;
public class C2WSIrcMessagePacket {
    private final JsonObject packet = new JsonObject();

    public C2WSIrcMessagePacket(String username, String origin, boolean genAvatar, String payload) {
        JsonObject senderData = new JsonObject();

        senderData.add("origin", new JsonPrimitive(origin));

        if(genAvatar) {
            senderData.add("avatar", new JsonPrimitive("URL OMITTED" + username + ".png"));
        } else {
            senderData.add("avatar", new JsonPrimitive("URL OMITTED"+ ".png"));
        }

        senderData.add("username", new JsonPrimitive(username));

        packet.add("type", new JsonPrimitive("minecraft"));
        packet.add("payload", new JsonPrimitive(payload));
        packet.add("sender", senderData);
    }

    public JsonObject getPacket() { return packet; }
}
