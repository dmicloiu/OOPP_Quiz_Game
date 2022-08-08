package client.utils;

import client.scenes.MultiPlayerCtrl;
import client.scenes.PlayerCtrl;
import client.scenes.QuestionCtrl;
import client.scenes.WaitingRoomCtrl;
import commons.Player;

public class LongPollingThread extends Thread {
    private ServerUtils server;
    private WaitingRoomCtrl waitingroomCtrl;
    private QuestionCtrl questionCtrl;
    private PlayerCtrl playerCtrl;

    /**
     * Constructs and initializes a new LongPollingThread
     * @param server - the corresponding server
     * @param waitingroomCtrl - the corresponding waitingroom controller
     * @param questionCtrl - the corresponding question controller
     */
    public LongPollingThread(ServerUtils server, WaitingRoomCtrl waitingroomCtrl,
                             QuestionCtrl questionCtrl, PlayerCtrl playerCtrl) {
        this.server = server;
        this.waitingroomCtrl = waitingroomCtrl;
        this.questionCtrl = questionCtrl;
        this.playerCtrl = playerCtrl;
    }

    /**
     * Registers this player for updates and sends the data for the received update
     */
    @Override
    public void run() {
        server.registerForUpdates(p -> {
            Player player = p.getPlayer();
            int b = p.getType();
            if (b < 3) waitingroomCtrl.update(b, player);
            if (b >= 3) {
                ((MultiPlayerCtrl) playerCtrl).halveTime(player, b - 3);
            }
        });
    }

    /**
     * Sets the playerCtrl of the thread
     * @param playerCtrl the in-use playerCtrl
     */
    public void setPlayerCtrl(PlayerCtrl playerCtrl) {
        this.playerCtrl = playerCtrl;
    }
}
