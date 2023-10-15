package Client;

import java.awt.Button;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Label;
import java.awt.SystemColor;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import java.awt.event.WindowAdapter;

public class UIManager extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private Map<String, Consumer<String>> Events;

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

	public boolean addEvent(String button, Consumer<String> event) {
		if (Events.containsKey(button))
			return false;
		Events.put(button, event);
		return true;
	}

	public UIManager() {
		Events = new HashMap<String, Consumer<String>>();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				setBounds(100, 100, 602, 376);
				setResizable(false);
				contentPane = new JPanel();
				contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

				setContentPane(contentPane);
				contentPane.setLayout(null);

				JLabel lblNewLabel = new JLabel("CLIENT");
				lblNewLabel.setBounds(277, 10, 72, 28);
				lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
				lblNewLabel.setFont(new Font("Calibri", Font.BOLD, 22));
				contentPane.add(lblNewLabel);

				Label ServerHost = new Label("Server host:");
				ServerHost.setFont(new Font("Calibri", Font.PLAIN, 14));
				ServerHost.setBounds(10, 52, 102, 28);
				contentPane.add(ServerHost);

				txpMessageBoard = new JTextPane();
				txpMessageBoard.setFont(new Font("Calibri", Font.PLAIN, 13));
				txpMessageBoard.setBounds(10, 99, 566, 194);
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
								@Override
								public void run() {
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

				TextField txtServerHost = new TextField();
				txtServerHost.setText("127.0.0.1");
				txtServerHost.setFont(new Font("Calibri", Font.PLAIN, 14));
				txtServerHost.setBounds(118, 58, 121, 22);
				contentPane.add(txtServerHost);

				// Label Port = new Label("Port:");
				// Port.setFont(new Font("Calibri", Font.PLAIN, 14));
				// Port.setBounds(319, 52, 54, 28);
				// contentPane.add(Port);

				// txtPort = new TextField();
				// txtPort.setText("1913");
				// txtPort.setFont(new Font("Calibri", Font.PLAIN, 14));
				// txtPort.setBounds(379, 58, 92, 22);
				// contentPane.add(txtPort);

				Button btnConnect = new Button("Connect");
				btnConnect.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						if (Events.containsKey("Connect")) {
							new Thread(new Runnable() {
								@Override
								public void run() {
									Events.get("Connect").accept(txtServerHost.getText());
								}
							}).start();
						}
					}
				});
				btnConnect.setForeground(Color.BLACK);
				btnConnect.setFont(new Font("Calibri", Font.PLAIN, 14));
				btnConnect.setBackground(SystemColor.activeCaption);
				btnConnect.setBounds(496, 50, 78, 24);
				contentPane.add(btnConnect);

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
