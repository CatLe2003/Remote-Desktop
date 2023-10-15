package server;



import java.awt.Button;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

public class StartPAMenu extends JFrame {

	private JPanel contentPane;
	public Consumer<String> startProcess;
	/**
	 * Launch the application.
	 */
	
	public void display() {
		this.setVisible(true);
	}
	/**
	 * Create the frame.
	 */
	public StartPAMenu() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {

				setTitle("Start");
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				setResizable(false);
				setBounds(100, 100, 292, 117);
				setVisible(false);
				contentPane = new JPanel();
				contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
				setContentPane(contentPane);
				contentPane.setLayout(null);
				
				JLabel lbStart = new JLabel("Start");
				lbStart.setBounds(116, 10, 55, 28);
				lbStart.setFont(new Font("Calibri", Font.BOLD, 22));
				lbStart.setToolTipText("");
				contentPane.add(lbStart);
				
				TextField txtAppName = new TextField();
				txtAppName.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						txtAppName.setText("");
					}
				});
				txtAppName.setFont(new Font("Calibri", Font.PLAIN, 14));
				txtAppName.setText("Enter name");
				txtAppName.setBounds(10, 49, 188, 21);
				contentPane.add(txtAppName);
				
				Button btnStart = new Button("Start");
				btnStart.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (startProcess != null) {
							new Thread(new Runnable() {
								public void run() {
									startProcess.accept(txtAppName.getText());
								}
							}).start();
							dispose();
						}
					}
				});
				btnStart.setForeground(Color.BLACK);
				btnStart.setFont(new Font("Calibri", Font.PLAIN, 14));
				btnStart.setBackground(SystemColor.activeCaption);
				btnStart.setBounds(204, 49, 64, 21);
				contentPane.add(btnStart);
				
				JSeparator separator = new JSeparator();
				separator.setBounds(10, 38, 258, 9);
				contentPane.add(separator);
			}
		});
	}
}
