package org.e11eman.crackutilities.utilities.systems;
public class CloopSystem {
    public ArrayList<Timer> loops = new ArrayList<>();

    public CloopSystem() {
        CClient.events.register("closeWorld", "closeWorldCloop", (Event) -> {
            for(Timer loop : loops) {
                loop.cancel();
                loop.purge();

                loops.remove(loop);
            }
        });
    }

    public void addCloop(int Delay, String command) {
        Timer cmc = new Timer();
        cmc.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                CClient.commandCoreSystem.run(command);
            }
        }, 0, Delay);

        loops.add(cmc);
    }

    public void removeCloop(int cloopNumber) {
        Timer cloop = loops.get(cloopNumber);
        cloop.purge();
        cloop.cancel();
        loops.remove(cloop);
    }
}
