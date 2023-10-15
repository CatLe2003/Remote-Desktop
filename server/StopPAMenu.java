package server;



import java.awt.EventQueue;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.TextField;
import java.awt.Button;
import java.awt.SystemColor;
import java.awt.Color;
import javax.swing.JSeparator;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StopPAMenu extends JFrame {

	private JPanel contentPane;
	public Consumer<String> killProcess;
	/**
	 * Launch the application.
	//  */
	public void display() {
		this.setVisible(true);
	}
	/**
	 * Create the frame.
	 */
	public StopPAMenu() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				setTitle("Kill");
				setVisible(false);
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				setResizable(false);
				setBounds(100, 100, 292, 117);
				contentPane = new JPanel();
				contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
				setContentPane(contentPane);
				contentPane.setLayout(null);
				
				JLabel lbKill = new JLabel("Kill");
				lbKill.setBounds(126, 10, 29, 28);
				lbKill.setFont(new Font("Calibri", Font.BOLD, 22));
				lbKill.setToolTipText("");
				contentPane.add(lbKill);
				
				TextField txtID = new TextField();
				txtID.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						txtID.setText("");
					}
				});
				txtID.setFont(new Font("Calibri", Font.PLAIN, 14));
				txtID.setText("Enter ID");
				txtID.setBounds(10, 49, 188, 21);
				contentPane.add(txtID);
				
				Button btnKill = new Button("Kill");
				btnKill.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (killProcess != null) {
							new Thread(new Runnable() {
								public void run() {
									int result = JOptionPane.showConfirmDialog(null,
										  "Are you sure you want to kill this application?",
										  "Confirm",
										  JOptionPane.YES_NO_OPTION,
										  JOptionPane.QUESTION_MESSAGE);
									if(result == JOptionPane.YES_OPTION)
										killProcess.accept(txtID.getText());
								}
							}).start();
							dispose();
						}
					}
				});
				btnKill.setForeground(Color.BLACK);
				btnKill.setFont(new Font("Calibri", Font.PLAIN, 14));
				btnKill.setBackground(SystemColor.activeCaption);
				btnKill.setBounds(204, 49, 64, 21);
				contentPane.add(btnKill);
				
				JSeparator separator = new JSeparator();
				separator.setBounds(10, 38, 258, 9);
				contentPane.add(separator);
			}
		});
	}
}
