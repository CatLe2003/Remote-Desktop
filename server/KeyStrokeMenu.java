package server;

import java.awt.Button;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextPane;

public class KeyStrokeMenu extends JFrame {

	private JPanel contentPane;
	private Consumer<String> hook;
	private Consumer<String> unhook;
	private Consumer<String> show;
	private StringBuilder buffer;

	/**
	 * Create the frame.
	 */
	public void setHookAction(Consumer<String> action) {
		hook = action;
	}

	public void setShowAction(Consumer<String> action) {
		show = action;
	}

	public void setUnhookAction(Consumer<String> action) {
		unhook = action;
	}

	public void display() {
		this.setVisible(true);
	}

	public void addKeyText(String s) {
		buffer.append(s);
	}

	public KeyStrokeMenu() {
		buffer = new StringBuilder();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				setTitle("Keystroke");
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				setResizable(false);
				setBounds(100, 100, 383, 354);
				setVisible(false);
				// unhook when exit prevent error
				addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						if (unhook != null) {
							new Thread(new Runnable() {
								public void run() {
									unhook.accept(null);
								}
							}).start();
						}
					}
				});
				contentPane = new JPanel();
				contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

				setContentPane(contentPane);
				contentPane.setLayout(null);

				JLabel lbKeystroke = new JLabel("Keystroke");
				lbKeystroke.setBounds(134, 10, 100, 28);
				lbKeystroke.setFont(new Font("Calibri", Font.BOLD, 22));
				lbKeystroke.setBackground(Color.BLACK);
				contentPane.add(lbKeystroke);

				Button btnHook = new Button("Hook");
				btnHook.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (hook != null) {
							new Thread(new Runnable() {
								public void run() {
									hook.accept(null);
								}
							}).start();
						}
					}
				});
				btnHook.setForeground(Color.BLACK);
				btnHook.setFont(new Font("Calibri", Font.PLAIN, 14));
				btnHook.setBackground(SystemColor.activeCaption);
				btnHook.setBounds(10, 39, 83, 51);
				contentPane.add(btnHook);

				JTextPane txpKeyBoard = new JTextPane();
				txpKeyBoard.setBounds(10, 103, 359, 204);
				contentPane.add(txpKeyBoard);

				Button btnShow = new Button("Show");
				btnShow.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (show == null)
							return;
						new Thread(new Runnable() {
							public void run() {
								show.accept(null);
								try {
									Thread.sleep(200);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								txpKeyBoard.setText(buffer.toString());
							}
						}).start();
					}
				});
				btnShow.setForeground(Color.BLACK);
				btnShow.setFont(new Font("Calibri", Font.PLAIN, 14));
				btnShow.setBackground(SystemColor.activeCaption);
				btnShow.setBounds(188, 39, 83, 51);
				contentPane.add(btnShow);

				Button btnDelete = new Button("Delete");
				btnDelete.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						txpKeyBoard.setText("");
						buffer.setLength(0);
					}
				});
				btnDelete.setActionCommand("Delete");
				btnDelete.setForeground(Color.BLACK);
				btnDelete.setFont(new Font("Calibri", Font.PLAIN, 14));
				btnDelete.setBackground(SystemColor.activeCaption);
				btnDelete.setBounds(277, 39, 83, 51);
				contentPane.add(btnDelete);

				Button btnStart = new Button("Unhook");
				btnStart.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (unhook != null) {
							new Thread(new Runnable() {
								public void run() {
									unhook.accept(null);
								}
							}).start();
						}
					}
				});
				btnStart.setForeground(Color.BLACK);
				btnStart.setFont(new Font("Calibri", Font.PLAIN, 14));
				btnStart.setBackground(SystemColor.activeCaption);
				btnStart.setBounds(99, 39, 83, 51);
				contentPane.add(btnStart);
			}
		});
	}
}
