package MiniMon;

import javax.swing.JFrame;

public class Battle extends JFrame {
	public Battle() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new BattlePanel());
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setTitle("Battle");
	}

	public static void main(String[] args) {
		new Battle();
	}
}
