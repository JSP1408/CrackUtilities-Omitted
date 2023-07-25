package org.e11eman.crackutilities.wrappers;
@SuppressWarnings("unused")
public class Player {
    private static final LastSeenMessagesCollector lastSeenMessagesCollector = new LastSeenMessagesCollector(120);
    private static final CommandDispatcher<CommandSource> commandDispatcher = new CommandDispatcher<>();
    public static boolean inWorld = false;


    public static ClientPlayerEntity getPlayer() {
        return MinecraftClient.getInstance().player;
    }

    public static ClientPlayNetworkHandler getNetworkHandler() { return getPlayer().networkHandler; }

    public static ClientPlayerInteractionManager getInteractionManager() { return  MinecraftClient.getInstance().interactionManager; }

    public static Entity getCameraInstance() { return MinecraftClient.getInstance().getCameraEntity(); }

    public static String getUsername() {
        if(getPlayer() != null) return getPlayer().getGameProfile().getName();
        return "unknown";
    }

    public static Block getBlock(BlockPos blockPos) {
        return getPlayer().clientWorld.getBlockState(blockPos).getBlock();
    }

    public static BlockState getBlockState(BlockPos blockPos) {
        return getPlayer().clientWorld.getBlockState(blockPos);
    }

    public static void placeBlock(BlockPos blockPos) {
        getInteractionManager().interactBlock(getPlayer(), Hand.MAIN_HAND, new BlockHitResult(blockPos.toCenterPos(), Direction.DOWN, blockPos, false));
    }

    public static String getUuid() {
        if(getPlayer() != null) return getPlayer().getUuidAsString();
        return "00000000-0000-0000-0000-000000000000";
    }

    public static Vec3d getPosition() {
        if(getPlayer() != null) return getPlayer().getPos();
        return new Vec3d(0,0,0);
    }

    public static void setMainHand(ItemStack item) {
        Player.getInteractionManager().clickCreativeStack(item, 36 + Player.getPlayer().getInventory().selectedSlot);
    }

    public static void setUsername(String username) {
        MinecraftClient client = MinecraftClient.getInstance();

        Session newSession = new Session(username, "", "", Optional.empty(), Optional.empty(), Session.AccountType.MOJANG);

        client.getSessionProperties().clear();
        ((ClientAccessor) client).setSession(newSession);
    }

    public static void sendChat(String message) {
        if(getPlayer() != null) {
            if(message.startsWith("/")) {
                getNetworkHandler().sendChatCommand(message.substring(1));
            } else {
                getNetworkHandler().sendChatMessage(message);
            }

        }

    }

    public static void alertClient(Text chat) {
        if(getPlayer() != null) getPlayer().sendMessage(chat);
    }

    public static void rightClick(Hand hand) {
        if(getInteractionManager() != null && getPlayer() != null) {
            getInteractionManager().interactItem(getPlayer(), hand);
        }
    }

    public static float getYaw() {
        if(getPlayer() != null) return getPlayer().getYaw();
        return 0;
    }

    public static float getPitch() {
        if(getPlayer() != null) return getPlayer().getPitch();
        return 0;
    }

    public static void setYaw(float yaw) {
        if(getPlayer() != null) getPlayer().setYaw(yaw);
    }

    public static void setPitch(float pitch) {
        if(getPlayer() != null) getPlayer().setPitch(pitch);
    }

    public static void setCameraYaw(float yaw) {
        if(getCameraInstance() != null) {
            getCameraInstance().setYaw(yaw);
        }
    }

    public static void setCameraPitch(float pitch) {
        if(getCameraInstance() != null) {
            getCameraInstance().setPitch(pitch);
        }
    }

    public static float getCameraYaw() {
        if(getCameraInstance() != null) {
            getCameraInstance().getYaw();
        }
        return 0;
    }

    public static float getCameraPitch() {
        if(getCameraInstance() != null) {
            getCameraInstance().getPitch();
        }
        return 0;
    }

    public static String getAddress() {
        if(getPlayer() != null) {
            return getNetworkHandler().getConnection().getAddress().toString().split("/")[1];
        }

        return "127.0.0.1";
    }

    public static boolean isOnGround() {
        if(getPlayer() != null) {
            return getPlayer().isOnGround();
        }

        return false;
    }

    public static <P extends PacketListener> void sendPacket(Packet<P> packet) {
        if(getPlayer() != null) {
            getNetworkHandler().sendPacket(packet);
        }
    }

    public static Collection<PlayerListEntry> getPlayerList() {
        if(getPlayer() != null) return getNetworkHandler().getPlayerList();
        return new ArrayList<>();
    }

    public static void connectPlayer(String addr, ServerInfo info) {
        ConnectScreen.connect(new MultiplayerScreen(new TitleScreen()), MinecraftClient.getInstance(), ServerAddress.parse(addr), info, false);
    }

    public static void reconnectPlayer() {
        String lastSocket;
        ServerInfo lastInfo;
        MinecraftClient client = MinecraftClient.getInstance();
        lastSocket = Objects.requireNonNull(client.getCurrentServerEntry()).address;
        lastInfo = client.isInSingleplayer() ? null : client.getCurrentServerEntry();
        disconnectPlayer();
        connectPlayer(lastSocket, lastInfo);
    }

    public static void disconnectPlayer() {
        MinecraftClient client = MinecraftClient.getInstance();

        boolean bl = client.isInSingleplayer();
        boolean bl2 = client.isConnectedToRealms();
        assert client.world != null;
        client.world.disconnect();

        if (bl) {
            client.disconnect(new MessageScreen(Text.literal("Disconnection")));
        } else {
            client.disconnect();
        }

        TitleScreen titleScreen = new TitleScreen();
        if (bl) {
            client.setScreen(titleScreen);
        } else if (bl2) {
            client.setScreen(new RealmsMainScreen(titleScreen));
        } else {
            client.setScreen(new MultiplayerScreen(titleScreen));
        }
    }
}