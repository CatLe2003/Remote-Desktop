package server;

import java.awt.Button;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Label;
import java.awt.SystemColor;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;



public class UIManager extends JFrame {
    

	private static final long serialVersionUID = 1L;
	private Map<String, Consumer<String>> Events;

	private KeyStrokeMenu keyStrokeMenu;
	private ProcessMenu processMenu;
	private ScreenshotMenu screenshotMenu;

	// private TextField txtPort;
	private JTextPane txpMessageBoard;


	public void inform(String msg) {
		JOptionPane.showMessageDialog(null, msg);
	}

	public void addMessage(String txt) {
		StringBuilder builder = new StringBuilder();
		builder.append(txpMessageBoard.getText());
		builder.append(txt);
		txpMessageBoard.setText(builder.toString());
	}

	public void addKeyLogText(String txt) { keyStrokeMenu.addKeyText(txt); }
	public void addScreenShotImage(ImageIcon icon) { screenshotMenu.addImage(icon); }

	public void displayProcess(Object[][] tasks) { processMenu.displayTable(tasks); }

	public boolean addEvent(String button, Consumer<String> event) {
		if (Events.containsKey(button))
			return false;
		Events.put(button, event);
		return true;
	}

	
	public UIManager() {
		Events = new HashMap<String, Consumer<String>>();
		keyStrokeMenu = new KeyStrokeMenu();
		screenshotMenu = new ScreenshotMenu();
		processMenu = new ProcessMenu();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				setResizable(false);
				setBounds(100, 100, 602, 376);
				JPanel contentPane = new JPanel();
				contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

				setContentPane(contentPane);
				contentPane.setLayout(null);
				
				JLabel lblNewLabel = new JLabel("SERVER");
				lblNewLabel.setBounds(277, 10, 72, 28);
				lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
				lblNewLabel.setFont(new Font("Calibri", Font.BOLD, 22));
				contentPane.add(lblNewLabel);
				
				// Label Port = new Label("Port:");
				// Port.setFont(new Font("Calibri", Font.PLAIN, 14));
				// Port.setBounds(10, 52, 54, 28);
				// contentPane.add(Port);
				
				txpMessageBoard = new JTextPane();
				txpMessageBoard.setFont(new Font("Calibri", Font.PLAIN, 13));
				txpMessageBoard.setBounds(10, 99, 568, 194);
				contentPane.add(txpMessageBoard);
				
				Label Message = new Label("Message:");
				Message.setFont(new Font("Calibri", Font.PLAIN, 14));
				Message.setBounds(10, 299, 80, 28);
				contentPane.add(Message);
				
				TextField txtMessage = new TextField();
				txtMessage.setBounds(96, 299, 394, 28);
				contentPane.add(txtMessage);
				
				Button btnSend = new Button("Send");
				btnSend.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						if (Events.containsKey("Send")) {
							new Thread(new Runnable() {
								@Override public void run() {
									Events.get("Send").accept(txtMessage.getText());
									txtMessage.setText("");
								}
							}).start();
						}
					}
				});
				btnSend.setBackground(SystemColor.activeCaption);
				btnSend.setForeground(SystemColor.desktop);
				btnSend.setFont(new Font("Calibri", Font.PLAIN, 14));
				btnSend.setBounds(496, 299, 82, 28);
				contentPane.add(btnSend);
				
				Button btnConnect = new Button("Connect");
				btnConnect.addActionListener(null);
				btnConnect.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						
						if (Events.containsKey("Connect")) {
							new Thread(new Runnable() {
								@Override public void run() {
									Events.get("Connect").accept(null);
								}
							}).start();
						}
					}
				});
				btnConnect.setForeground(Color.BLACK);
				btnConnect.setFont(new Font("Calibri", Font.PLAIN, 14));
				btnConnect.setBackground(SystemColor.activeCaption);
				btnConnect.setBounds(496, 56, 78, 24);
				contentPane.add(btnConnect);
				
				Button btnAppRunning = new Button("App Running");
				btnAppRunning.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						processMenu.setTypeMenu("App Running");
						processMenu.setShowAction(Events.get("Show Apps"));
						processMenu.setStopAction(Events.get("Kill Process"));
						processMenu.setStartAction(Events.get("Start Process"));
						processMenu.display();
					}
				});
				btnAppRunning.setForeground(Color.BLACK);
				btnAppRunning.setFont(new Font("Calibri", Font.PLAIN, 14));
				btnAppRunning.setBackground(SystemColor.activeCaption);
				btnAppRunning.setBounds(112, 56, 92, 24);
				contentPane.add(btnAppRunning);


				Button btnCapture = new Button("Capture");
				btnCapture.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						screenshotMenu.setDisplayImageAction(Events.get("Capture Screen Shot"));
						screenshotMenu.display();
					}
				});
				btnCapture.setForeground(Color.BLACK);
				btnCapture.setFont(new Font("Calibri", Font.PLAIN, 14));
				btnCapture.setBackground(SystemColor.activeCaption);
				btnCapture.setBounds(21, 56, 78, 24);
				contentPane.add(btnCapture);

				Button btnProcess = new Button("Process");
				btnProcess.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						processMenu.setTypeMenu("Process");
						processMenu.setStopAction(Events.get("Kill Process"));
						processMenu.setStartAction(Events.get("Start Process"));
						processMenu.setShowAction(Events.get("Show Process"));
						processMenu.display();
					}
				});
				btnProcess.setForeground(Color.BLACK);
				btnProcess.setFont(new Font("Calibri", Font.PLAIN, 14));
				btnProcess.setBackground(SystemColor.activeCaption);
				btnProcess.setBounds(216, 56, 78, 24);
				contentPane.add(btnProcess);
				
				Button btnShutDown = new Button("Shut Down");
				btnShutDown.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (Events.containsKey("Shut Down")) {
							new Thread(new Runnable() {
								@Override public void run() {
									Events.get("Shut Down").accept(null);
								}
							}).start();
						}
					}
				});
				btnShutDown.setForeground(Color.BLACK);
				btnShutDown.setFont(new Font("Calibri", Font.PLAIN, 14));
				btnShutDown.setBackground(SystemColor.activeCaption);
				btnShutDown.setBounds(304, 56, 78, 24);
				contentPane.add(btnShutDown);

				Button btnKeyLog = new Button("Key Log");
				btnKeyLog.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						keyStrokeMenu.setHookAction(Events.get("Hook Key Log"));
						keyStrokeMenu.setUnhookAction(Events.get("Unhook Key Log"));
						keyStrokeMenu.setShowAction(Events.get("Show Key Log"));
						keyStrokeMenu.display();
					}
				});
				btnKeyLog.setForeground(Color.BLACK);
				btnKeyLog.setFont(new Font("Calibri", Font.PLAIN, 14));
				btnKeyLog.setBackground(SystemColor.activeCaption);
				btnKeyLog.setBounds(392, 56, 78, 24);
				contentPane.add(btnKeyLog);

				addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						if (Events.containsKey("Exit")) {
							new Thread(new Runnable() {
								public void run() {
									Events.get("Exit").accept(null);
								}
							}).start();
						}
					}
				});
				setVisible(true);
			}
		});
	}
}
