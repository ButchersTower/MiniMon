package MiniMon;

import javax.swing.JFrame;

public class Walking extends JFrame {
	public Walking() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new WalkingPanel());
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setTitle("Walking");
	}

	public static void main(String[] args) {
		new Walking();
	}
}
