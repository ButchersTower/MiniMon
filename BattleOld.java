package MiniMon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BattleOld extends JPanel implements Runnable, MouseListener,
		KeyListener {

	// wait for user input, either 1-4
	// decide who goes first

	// needs a g loop if there is going to be draw animations
	// or atleast a draw loop, but they would do the same thing anyway

	int width = 200;
	int height = 300;

	Random rand;

	// [0][x] = evilGuy
	// [1][x] = wolfMan

	// [x][0] = hp;
	// [x][1] = attack
	// [x][2] = defence
	// [x][3] = speed;
	// [x][4] = maxHealth

	int[][] stats = { { 20, 20, 20, 10, 20 }, { 20, 20, 16, 15, 20 } };

	// / Make an array to hold all the information for the button locs.
	// [x][0] = x
	// [x][1] = y
	// [x][2] = w
	// [x][3] = h
	// [x][4] = graphics ##
	// [x][5] = what it does
	int[][] butts = { { 50, 240, 40, 20, 0, 1 }, { 110, 240, 40, 20, 0, 2 },
			{ 50, 270, 40, 20, 0, 3 }, { 110, 270, 40, 20, 0, 4 } };

	// [x][0] = power
	// [x][1] = hitChance
	int[][] attacks = { { 40, 80 }, { 25, 100 } };
	int[][] enAttacks = { { 40, 80 }, { 25, 100 } };

	Image[] imageAr;

	Thread thread;
	Image image;
	Graphics g;

	public BattleOld() {
		super();

		setPreferredSize(new Dimension(width, height));
		setFocusable(true);
		requestFocus();
	}

	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public void run() {
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		this.setSize(new Dimension(width, height));

		gStart();
	}

	/**
	 * Methods go below here.
	 * 
	 */

	public void gStart() {
		addMouseListener(this);
		addKeyListener(this);
		imageInit();

		rand = new Random();

		drawBattle();
		drwGm();
	}

	boolean playAttackDone;
	boolean enemAttackDone;

	void drawBattle() {
		g.setColor(Color.MAGENTA);
		g.fillRect(0, 0, width, height);
		g.drawImage(imageAr[0], playXX, playYY, null);
		g.drawImage(imageAr[1], 130, 10, null);

		// draws the healths
		g.drawImage(health[drawHealth(stats[0][4], stats[0][0])], 18,
				160 + imageAr[0].getHeight(null), null);
		g.drawImage(health[drawHealth(stats[0][4], stats[1][0])], 142,
				10 + imageAr[1].getHeight(null), null);

		drawButts();
	}

	int playXX = 10;
	int playYY = 160;

	int attackAnimation = -1;

	void constantAttack() {
		if (stats[1][0] > 0 && stats[0][0] > 0) {
			System.out.println("****TURN****");
			if (stats[0][3] > stats[1][3]) {
				playAttack(1);
				enemyAttack();
			} else if (stats[0][3] < stats[1][3]) {
				enemyAttack();
				attackAnimation = 0;

				playAttack(1);

			} else if (stats[0][3] == stats[1][3]) {
				int playRan = rand.nextInt(100);
				int enemRan = rand.nextInt(100);
				if (playRan < enemRan) {
					enemyAttack();
					playAttack(1);
				} else {
					playAttack(1);
					enemyAttack();
				}
			}
			// if enemy is still alive
			if (stats[1][0] <= 0) {
				System.out.println("playerWins");
			}
			// if enemy is still alive
			if (stats[0][0] <= 0) {
				System.out.println("enemyWins");
			}
		} else {
			System.out.println("battle over");
		}

	}

	void attack(int playAttack) {
		if (stats[1][0] > 0 && stats[0][0] > 0) {
			System.out.println("****TURN****");
			if (stats[0][3] > stats[1][3]) {
				playAttack(playAttack);
				enemyAttack();
			} else if (stats[0][3] < stats[1][3]) {
				enemyAttack();
				attackAnimation = 0;
				playAttack(playAttack);
			} else if (stats[0][3] == stats[1][3]) {
				int playRan = rand.nextInt(100);
				int enemRan = rand.nextInt(100);
				if (playRan < enemRan) {
					enemyAttack();
					playAttack(playAttack);
				} else {
					playAttack(playAttack);
					enemyAttack();
				}
			}
			// if enemy is still alive
			if (stats[1][0] <= 0) {
				System.out.println("playerWins");
			}
			// if enemy is still alive
			if (stats[0][0] <= 0) {
				System.out.println("enemyWins");
			}
		} else {
			System.out.println("battle over");
		}
	}

	void playAttack(int playAttack) {
		// if player is still alive
		if (stats[0][0] > 0) {
			if (playAttack == 1) {
				int f = rand.nextInt(100) + 1;
				if (f < attacks[0][1]) {
					int dmg = damageCalc(stats[0][1], stats[1][2],
							attacks[0][0], 10);
					System.out.println("Attack 1 damage: " + dmg);
					stats[1][0] -= dmg;
				} else {
					System.out.println("Attack 1 miss");
				}
			} else if (playAttack == 2) {
				int f = rand.nextInt(100) + 1;
				if (f < attacks[0][1]) {
					int dmg = damageCalc(stats[0][1], stats[1][2],
							attacks[1][0], 10);
					System.out.println("Attack 2 damage: " + dmg);
					stats[1][0] -= dmg;
				} else {
					System.out.println("Attack 2 miss");
				}
			}
		}
	}

	void enemyAttack() {
		// if enemy is still alive
		if (stats[1][0] > 0) {
			int a = rand.nextInt(2) + 1;
			if (a == 1) {
				int f = rand.nextInt(100) + 1;
				if (f < attacks[0][1]) {
					int dmg = damageCalc(stats[0][1], stats[1][2],
							attacks[0][0], 10);
					System.out.println("Enemy  1 damage: " + dmg);
					stats[0][0] -= dmg;
				} else {
					System.out.println("Enemy  1 miss");
				}
			} else if (a == 2) {
				int f = rand.nextInt(100) + 1;
				if (f < attacks[0][1]) {
					int dmg = damageCalc(stats[0][1], stats[1][2],
							attacks[1][0], 10);
					System.out.println("Enemy  2 damage: " + dmg);
					stats[0][0] -= dmg;
				} else {
					System.out.println("Enemy  2 miss");
				}
			}
		}
	}

	int damageCalc(int atk, int def, int pow, int lvl) {
		int ranNum = rand.nextInt(16);
		ranNum += 85;
		int dmg = ((((((2 * lvl / 5) + 2) * pow * atk / def) / 50) + 2) * ranNum) / 100;
		return dmg;
	}

	int drawHealth(int fullHp, int curHp) {
		int de;
		if (curHp > 0) {
			if (curHp >= fullHp) {
				de = 24;
			} else if (curHp >= fullHp * 23 / 24) {
				de = 23;
			} else if (curHp >= fullHp * 22 / 24) {
				de = 22;
			} else if (curHp >= fullHp * 21 / 24) {
				de = 21;
			} else if (curHp >= fullHp * 20 / 24) {
				de = 20;
			} else if (curHp >= fullHp * 19 / 24) {
				de = 19;
			} else if (curHp >= fullHp * 18 / 24) {
				de = 18;
			} else if (curHp >= fullHp * 17 / 24) {
				de = 17;
			} else if (curHp >= fullHp * 16 / 24) {
				de = 16;
			} else if (curHp >= fullHp * 15 / 24) {
				de = 15;
			} else if (curHp >= fullHp * 14 / 24) {
				de = 14;
			} else if (curHp >= fullHp * 13 / 24) {
				de = 13;
			} else if (curHp >= fullHp * 12 / 24) {
				de = 12;
			} else if (curHp >= fullHp * 11 / 24) {
				de = 11;
			} else if (curHp >= fullHp * 10 / 24) {
				de = 10;
			} else if (curHp >= fullHp * 9 / 24) {
				de = 9;
			} else if (curHp >= fullHp * 8 / 24) {
				de = 8;
			} else if (curHp >= fullHp * 7 / 24) {
				de = 7;
			} else if (curHp >= fullHp * 6 / 24) {
				de = 6;
			} else if (curHp >= fullHp * 5 / 24) {
				de = 5;
			} else if (curHp >= fullHp * 4 / 24) {
				de = 4;
			} else if (curHp >= fullHp * 3 / 24) {
				de = 3;
			} else if (curHp >= fullHp * 2 / 24) {
				de = 2;
			} else {
				de = 1;
			}
		} else {
			de = 0;
		}
		return de;
	}

	void drawButts() {
		// Draws buttons
		for (int b = 0; b < butts.length; b++) {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(butts[b][0], butts[b][1], butts[b][2], butts[b][3]);
		}
	}

	public void buttons(MouseEvent migg) {
		for (int a = 0; a < butts.length; a++) {
			if (butts[a][4] == 0) {
				if (migg.getY() > butts[a][1]) {
					if (migg.getY() < butts[a][1] + butts[a][3]) {
						if (migg.getX() > butts[a][0]) {
							if (migg.getX() < butts[a][0] + butts[a][2]) {
								buttonP(butts[a][5]);
							}
						}
					}
				}
			}
		}
	}

	public void buttonP(int a) {
		// System.out.println("click a: " + a);
		if (a == 1) {
			attack(a);
		} else if (a == 2) {
			attack(a);
		}
		g.drawImage(health[drawHealth(stats[0][4], stats[0][0])], 18,
				160 + imageAr[0].getHeight(null), null);
		g.drawImage(health[drawHealth(stats[0][4], stats[1][0])], 142,
				10 + imageAr[1].getHeight(null), null);
		drwGm();
	}

	/**
	 * Methods go above here.
	 * 
	 */

	public void drwGm() {
		Graphics g2 = this.getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
	}

	Image[] health;

	public void imageInit() {

		imageAr = new Image[2];
		ImageIcon ii = new ImageIcon(this.getClass().getResource(
				"res/evilGuy.png"));
		imageAr[0] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/wolfMan.png"));
		imageAr[1] = ii.getImage();

		health = new Image[25];
		ii = new ImageIcon(BattlePanel.class.getResource("res/Health/h0.png"));
		health[0] = ii.getImage();
		ii = new ImageIcon(BattlePanel.class.getResource("res/Health/h1.png"));
		health[1] = ii.getImage();
		ii = new ImageIcon(BattlePanel.class.getResource("res/Health/h2.png"));
		health[2] = ii.getImage();
		ii = new ImageIcon(BattlePanel.class.getResource("res/Health/h3.png"));
		health[3] = ii.getImage();
		ii = new ImageIcon(BattlePanel.class.getResource("res/Health/h4.png"));
		health[4] = ii.getImage();
		ii = new ImageIcon(BattlePanel.class.getResource("res/Health/h5.png"));
		health[5] = ii.getImage();
		ii = new ImageIcon(BattlePanel.class.getResource("res/Health/h6.png"));
		health[6] = ii.getImage();
		ii = new ImageIcon(BattlePanel.class.getResource("res/Health/h7.png"));
		health[7] = ii.getImage();
		ii = new ImageIcon(BattlePanel.class.getResource("res/Health/h8.png"));
		health[8] = ii.getImage();
		ii = new ImageIcon(BattlePanel.class.getResource("res/Health/h9.png"));
		health[9] = ii.getImage();
		ii = new ImageIcon(BattlePanel.class.getResource("res/Health/h10.png"));
		health[10] = ii.getImage();
		ii = new ImageIcon(BattlePanel.class.getResource("res/Health/h11.png"));
		health[11] = ii.getImage();
		ii = new ImageIcon(BattlePanel.class.getResource("res/Health/h12.png"));
		health[12] = ii.getImage();
		ii = new ImageIcon(BattlePanel.class.getResource("res/Health/h13.png"));
		health[13] = ii.getImage();
		ii = new ImageIcon(BattlePanel.class.getResource("res/Health/h14.png"));
		health[14] = ii.getImage();
		ii = new ImageIcon(BattlePanel.class.getResource("res/Health/h15.png"));
		health[15] = ii.getImage();
		ii = new ImageIcon(BattlePanel.class.getResource("res/Health/h16.png"));
		health[16] = ii.getImage();
		ii = new ImageIcon(BattlePanel.class.getResource("res/Health/h17.png"));
		health[17] = ii.getImage();
		ii = new ImageIcon(BattlePanel.class.getResource("res/Health/h18.png"));
		health[18] = ii.getImage();
		ii = new ImageIcon(BattlePanel.class.getResource("res/Health/h19.png"));
		health[19] = ii.getImage();
		ii = new ImageIcon(BattlePanel.class.getResource("res/Health/h20.png"));
		health[20] = ii.getImage();
		ii = new ImageIcon(BattlePanel.class.getResource("res/Health/h21.png"));
		health[21] = ii.getImage();
		ii = new ImageIcon(BattlePanel.class.getResource("res/Health/h22.png"));
		health[22] = ii.getImage();
		ii = new ImageIcon(BattlePanel.class.getResource("res/Health/h23.png"));
		health[23] = ii.getImage();
		ii = new ImageIcon(BattlePanel.class.getResource("res/Health/h24.png"));
		health[24] = ii.getImage();

	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {

		drwGm();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		buttons(me);
	}
}
