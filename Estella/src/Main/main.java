package Main;

import javax.swing.JFrame;

public class main {

	public static void main(String[] args) {
		
		System.out.println("Hello World");
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("Game");
		
		GamePanel GamePanel = new GamePanel();
		window.add(GamePanel);
		
		window.pack();
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		GamePanel.startGameThread();
		
	}

}
