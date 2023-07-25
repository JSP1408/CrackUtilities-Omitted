package org.e11eman.crackutilities;
public class InitializationPoint implements ModInitializer {
    @Override
    public void onInitialize() {
        CClient.initSystems();
        CClient.registerExtraEvents();

        if(CClient.isDevelopment()) {
            Player.setUsername("CrackDev" + SecureRandomStuff.getRandomString(4));
        }
    }
}
