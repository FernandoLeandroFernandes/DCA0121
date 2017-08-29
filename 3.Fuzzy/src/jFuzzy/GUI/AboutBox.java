package jFuzzy.GUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.GridLayout;

/**
 * Classe que cria a AboutBox
 */
public class AboutBox extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AboutBox(Frame parent, boolean modal) {
		super(parent, modal);

		// Estao localizado no centro da tela
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double screenWidth = screenSize.getWidth();
		double ScreenHeight = screenSize.getHeight();
		int width = 450;
		int height = 280;
		int x = ((int) screenWidth - width) / 2;
		int y = ((int) ScreenHeight - height) / 2;
		setSize(width, height);
		setLocation(x, y);

		setResizable(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
				
						JLabel title = new JLabel("jFuzzyApp", JLabel.CENTER);
						title.setFont(new Font("Helvetica", Font.PLAIN, 24));
						title.setForeground(Color.MAGENTA);
						
								getContentPane().add(title);
								
										title.setBounds(5, 20, 430, 30);
		
				JLabel version = new JLabel("Version: 2016.1", JLabel.CENTER);
				version.setBackground(Color.WHITE);
				version.setFont(new Font("Helvetica", Font.BOLD, 14));
				getContentPane().add(version);
				version.setBounds(5, 50, 430, 20);
				
						JLabel developers = new JLabel("Desenvolvedores:", JLabel.CENTER);
						developers.setFont(new Font("Helvetica", Font.PLAIN, 16));
						getContentPane().add(developers);
						developers.setBounds(5, 60, 430, 20);
				
				JLabel developersFernando = new JLabel("Fernando Fernandes (Engenharia Mecatr\u00F4nica)", SwingConstants.CENTER);
				developersFernando.setFont(new Font("SansSerif", Font.PLAIN, 14));
				getContentPane().add(developersFernando);
				
						JLabel developersPedro = new JLabel("Pedro de Castro (Engenharia da Computação)", JLabel.CENTER);
						developersPedro.setFont(new Font("Helvetica", Font.PLAIN, 14));
						getContentPane().add(developersPedro);
						developersPedro.setBounds(5, 120, 430, 20);
	}
} // end class AboutBox