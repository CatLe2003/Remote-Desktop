package server;

import java.awt.Button;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

public class ProcessMenu extends JFrame {
	private final String[] headers = {"Image Name", "PID", "Memory Usage"};
	private JPanel contentPane;
	private JScrollPane tableScrollPane;
	private StartPAMenu startPAMenu;
	private StopPAMenu stopPAMenu;
	JLabel lbProcess;
	public Consumer<String> show;

	public void displayTable(Object[][] tasks) {
		if (tableScrollPane != null)
			contentPane.remove(tableScrollPane);

		JTable tableProcess = new JTable(tasks, headers);
		tableScrollPane = new JScrollPane(tableProcess);
		tableScrollPane.setBounds(10, 108, 348, 199);
		tableScrollPane.setVisible(true);
		contentPane.add(tableScrollPane);

	}

	public void display() {
		this.setVisible(true);
	}

	public void setTypeMenu(String type) {
		setTitle(type);
		lbProcess.setText(type);
		if (type.equals("App Running"))
			lbProcess.setBounds(124, 10, 126, 28);
		else if (type.equals("Process"))
			lbProcess.setBounds(148, 10, 70, 28);
	}

	public void setStartAction(Consumer<String> action) {
		startPAMenu.startProcess = action;
	}

	public void setStopAction(Consumer<String> action) {
		stopPAMenu.killProcess = action;
	}

	public void setShowAction(Consumer<String> action) {
		show = action;
	}

	/**
	 * Create the frame.
	 */
	public ProcessMenu() {
		startPAMenu = new StartPAMenu();
		stopPAMenu = new StopPAMenu();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				setTitle("App Running");
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						if (tableScrollPane != null) {
							contentPane.remove(tableScrollPane);
							tableScrollPane = null;
						}
					}
				});
				setBounds(100, 100, 383, 354);
				contentPane = new JPanel();
				contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

				setContentPane(contentPane);
				contentPane.setLayout(null);
				lbProcess = new JLabel("App Running");
				lbProcess.setBounds(124, 10, 126, 28);
				lbProcess.setFont(new Font("Calibri", Font.BOLD, 22));
				lbProcess.setBackground(Color.BLACK);
				contentPane.add(lbProcess);

				// tableProcess = new JTable();
				// tableProcess.setBounds(10, 108, 348, 199);
				// contentPane.add(tableProcess);

				Button btnKill = new Button("Kill");
				btnKill.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						stopPAMenu.display();
					}
				});
				btnKill.setForeground(Color.BLACK);
				btnKill.setFont(new Font("Calibri", Font.PLAIN, 14));
				btnKill.setBackground(SystemColor.activeCaption);
				btnKill.setBounds(10, 39, 83, 51);
				contentPane.add(btnKill);

				Button btnShow = new Button("Show");
				btnShow.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (show != null)
							show.accept(null);
					}
				});
				btnShow.setForeground(Color.BLACK);
				btnShow.setFont(new Font("Calibri", Font.PLAIN, 14));
				btnShow.setBackground(SystemColor.activeCaption);
				btnShow.setBounds(99, 39, 83, 51);
				contentPane.add(btnShow);

				Button btnDelete = new Button("Delete");
				btnDelete.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (tableScrollPane != null) {
							contentPane.remove(tableScrollPane);
							contentPane.repaint();
							tableScrollPane = null;
						}
					}
				});
				btnDelete.setActionCommand("Delete");
				btnDelete.setForeground(Color.BLACK);
				btnDelete.setFont(new Font("Calibri", Font.PLAIN, 14));
				btnDelete.setBackground(SystemColor.activeCaption);
				btnDelete.setBounds(188, 39, 83, 51);
				contentPane.add(btnDelete);

				Button btnStart = new Button("Start");
				btnStart.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						startPAMenu.display();
					}
				});
				btnStart.setForeground(Color.BLACK);
				btnStart.setFont(new Font("Calibri", Font.PLAIN, 14));
				btnStart.setBackground(SystemColor.activeCaption);
				btnStart.setBounds(277, 39, 83, 51);
				contentPane.add(btnStart);
				setVisible(false);
			}
		});
	}
}
