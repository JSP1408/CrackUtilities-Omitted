package org.e11eman.crackutilities.commands;

public class ConnectionCommand extends Command {

    public ConnectionCommand() {
        super("connection", "Manage your connection to this server", "\n connection <reconnect> \n connection <disconnect> \n connection <swapsrv> <host:port>");
    }

    @Override
    public void execute(ArrayList<String> arguments) {
        switch (arguments.get(0)) {
            case "reconnect" -> Player.reconnectPlayer();

            case "disconnect" -> Player.disconnectPlayer();

            case "swapsrv" -> {
                Player.disconnectPlayer();
                Player.connectPlayer(arguments.get(1), new ServerInfo("newServer", arguments.get(1), false));
            }
        }
    }
}
