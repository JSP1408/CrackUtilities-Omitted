package org.e11eman.crackutilities.mixins;

@Mixin(MinecraftClient.class)
public interface ClientAccessor {
    @Mutable
    @Accessor("session")
    void setSession(Session session);
}
