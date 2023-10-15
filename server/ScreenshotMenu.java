package server;

import java.awt.EventQueue;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.Button;
import java.awt.SystemColor;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.rmi.StubNotFoundException;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;
import java.awt.event.ActionEvent;

public class ScreenshotMenu extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private Consumer<String> capture;
	private Image image;
	private JFileChooser fileChooser;

	static void writeImageToFile(Image image, File file) {
        try {
			BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			Graphics g = bufferedImage.getGraphics();
			g.drawImage(image, 0, 0, null);
			g.dispose();
            ImageIO.write(bufferedImage, "png", file);
		} catch (IOException e) {
			// e.printStackTrace();
		}
    }

	
	public void setDisplayImageAction(Consumer<String> action) {
		capture = action;
	}

	public void display() {
		this.setVisible(true);
	}

	public void addImage(ImageIcon imageIcon) {
		image = imageIcon.getImage();
		textField.repaint();
	}

	/**
	 * Create the frame.
	 */
	public ScreenshotMenu() {
		fileChooser = new JFileChooser(".", null);
		fileChooser.setDialogTitle("Save file");
		fileChooser.setFileFilter(new FileNameExtensionFilter("Png", "png"));
		EventQueue.invokeLater(new Runnable() {
			public void run() {

				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				setResizable(false);
				addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						image = null;
						textField.getGraphics().dispose();
					}
				});
				setBounds(100, 100, 374, 372);
				setVisible(false);
				contentPane = new JPanel();
				contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

				setContentPane(contentPane);
				contentPane.setLayout(null);

				JLabel lblScreenshot = new JLabel("Screenshot");
				lblScreenshot.setBounds(128, 10, 103, 28);
				lblScreenshot.setFont(new Font("Calibri", Font.BOLD, 22));
				lblScreenshot.setBackground(Color.BLACK);
				contentPane.add(lblScreenshot);

				textField = new JTextField() {
					@Override
					public void paintComponent(Graphics g) {
						if (image == null)
							g.clearRect(0, 0, this.getWidth(), this.getHeight());
						else
							g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(),
									0, 0, image.getWidth(null), image.getHeight(null), null);
						;
					}
				};
				textField.setBounds(10, 41, 251, 284);
				contentPane.add(textField);
				textField.setColumns(10);

				Button btnCapture = new Button("Capture");
				btnCapture.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (capture != null) {
							new Thread(new Runnable() {
								public void run() {
									capture.accept(null);
								}
							}).start();
						}
					}
				});
				btnCapture.setForeground(Color.BLACK);
				btnCapture.setFont(new Font("Calibri", Font.PLAIN, 14));
				btnCapture.setBackground(SystemColor.activeCaption);
				btnCapture.setBounds(267, 41, 83, 177);
				contentPane.add(btnCapture);

				Button btnSave = new Button("Save");
				btnSave.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (image != null) {

							new Thread(new Runnable() {
								public void run() {
									int option = fileChooser.showSaveDialog(null);
									if (option == JFileChooser.APPROVE_OPTION) {
										File fileToSave = fileChooser.getSelectedFile();
										try {
											fileToSave.createNewFile();
											String filename = fileToSave.getPath();
											if (!filename.endsWith(".png")) {
												File newFile = new File(filename + ".png");
												fileToSave.renameTo(newFile);
												fileToSave = newFile;
											}
										} catch (IOException e) {
											// e.printStackTrace();
										}
										
										writeImageToFile(image, fileToSave);
									}
								}
							}).start();
						}
					}
				});
				btnSave.setForeground(Color.BLACK);
				btnSave.setFont(new Font("Calibri", Font.PLAIN, 14));
				btnSave.setBackground(SystemColor.activeCaption);
				btnSave.setBounds(267, 224, 83, 101);
				contentPane.add(btnSave);
			}
		});
	}
}
