package ufrn.ia.star;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

/**
 * Classe que cria a AboutBox
 */
public class AboutBox extends JDialog {

	public AboutBox(Frame parent, boolean modal) {
		super(parent, modal);

		// Está localizado no centro da tela
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

		JLabel title = new JLabel("A* e Busca Gulosa", JLabel.CENTER);
		title.setFont(new Font("Helvetica", Font.PLAIN, 24));
		title.setForeground(new java.awt.Color(0, 102, 0));

		JLabel version = new JLabel("Version: 2016.1", JLabel.CENTER);
		version.setFont(new Font("Helvetica", Font.BOLD, 14));

		JLabel developers = new JLabel("Desenvolvedores:", JLabel.CENTER);
		developers.setFont(new Font("Helvetica", Font.PLAIN, 16));

		JLabel developersFernando = new JLabel("Fernando Fernandes (Engenharia Mecatrônica)", JLabel.CENTER);
		developersFernando.setFont(new Font("Helvetica", Font.PLAIN, 14));

		JLabel developersPedro = new JLabel("Pedro de Castro (Engenharia da Computação)", JLabel.CENTER);
		developersPedro.setFont(new Font("Helvetica", Font.PLAIN, 14));


		add(title);
		add(version);
		add(developers);
		add(developersFernando);
		add(developersPedro);

		title.setBounds(5, 0, 430, 30);
		version.setBounds(5, 30, 430, 20);
		developers.setBounds(5, 55, 430, 20);
		developersFernando.setBounds(5, 80, 430, 20);
		developersPedro.setBounds(5, 155, 430, 20);
	}
} // end class AboutBox