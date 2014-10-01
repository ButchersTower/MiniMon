package pkmn;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class WalkingPanel extends JPanel implements Runnable, MouseListener,
		KeyListener {

	// need a map array
	// need simple walking comands

	int imgH = 32;

	int width = 400;
	int height = 360;

	int w1 = 400;
	int h1 = 360;

	// map arraylist
	static ArrayList<int[]> map;

	// # of horizontal and vertical blocks, used for camera control.
	static int horBlocks;
	static int verBlocks;

	// self explanitory
	Image[] tiles;
	static Image[] entities;

	Thread thread;
	Image image;
	Graphics g;

	// Vars for gLoop Below
	public int tps = 20;
	public int milps = 1000 / tps;
	long lastTick = 0;
	int sleepTime = 0;
	long lastSec = 0;
	int ticks = 0;
	long startTime;
	long runTime;
	private long nextTick = 0;
	private boolean running = false;

	// Vars for gLoop Above

	public WalkingPanel() {
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
		addKeyListener(this);
		addMouseListener(this);

		this.setSize(new Dimension(width, height));

		startTime = System.currentTimeMillis();

		gStart();
	}

	/**
	 * Methods go below here.
	 * 
	 */

	Player play;

	public void gStart() {

		try {
			TextInit.readMap();
		} catch (IOException e) {
		}
		map = new ArrayList<int[]>();
		map = TextInit.getmap1();

		horBlocks = map.get(1).length;
		verBlocks = map.size();

		image = new BufferedImage(horBlocks * 32, verBlocks * 32,
				BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();

		imageInit();

		play = new Player();

		play.setGraphics(g);

		running = true;
		gLoop();
	}

	public void gLoop() {
		while (running) {
			// Do the things you want the gLoop to do below here
			play.updatePlayer();

			drawMap();
			play.drawPlayer();
			// And above here.
			drwGm(g);

			ticks++;
			// Runs once a second and keeps track of ticks;
			// 1000 ms since last output
			if (timer() - lastSec > 1000) {
				if (ticks < tps - 1 || ticks > tps + 1) {
					if (timer() - startTime < 2000) {
						System.out.println("Ticks this second: " + ticks);
						System.out.println("timer(): " + timer());
						System.out.println("nextTick: " + nextTick);
					}
				}

				ticks = 0;
				lastSec = (System.currentTimeMillis() - startTime);
			}

			// Used to protect the game from falling beind.
			if (nextTick < timer()) {
				nextTick = timer() + milps;
			}

			// Limits the ticks per second
			if (timer() - nextTick < 0) {
				sleepTime = (int) (nextTick - timer());
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
				}

				nextTick += milps;
			}
		}
	}

	public void drawMap() {
		for (int z = 0; z < map.size(); z++) {
			for (int x = 0; x < map.get(z).length; x++) {
				for (int hg = 0; hg < tiles.length; hg++) {
					if (map.get(z)[x] == hg + 1) {
						if (hg + 1 == 7 || hg + 1 == 6) {
							g.drawImage(tiles[0], (x * imgH), (z * imgH), null);
						}
						g.drawImage(tiles[hg], (x * imgH), (z * imgH), null);
					}
				}
			}
		}
	}

	/**
	 * Methods go above here.
	 * 
	 */

	public long timer() {
		return System.currentTimeMillis() - startTime;

	}

	public void drwGm(Graphics g) {

		// All this shit just centers the camera around the player
		int i1x, i1y;
		boolean xTop, yTop, xBot, yBot;

		i1x = (int) ((w1 / 2) - (play.xx + 16));
		i1y = (int) ((h1 / 2) - (play.yy + 16));
		// Adjusts the cameras for when the player at the edge of a map.
		if ((play.xx + 16) < (w1 / 2)) {
			i1x = 0;
			xTop = true;
		} else {
			xTop = false;
		}
		if ((play.xx + 16) > (horBlocks * imgH) - (w1 / 2)) {
			i1x = -(((horBlocks) * imgH) - w1);
			xBot = true;
		} else {
			xBot = false;
		}
		if ((play.yy + 16) < (h1 / 2)) {
			i1y = 0;
			yTop = true;
		} else {
			yTop = false;
		}
		if ((play.yy + 16) > (verBlocks * imgH) - (h1 / 2)) {
			i1y = -(((verBlocks) * imgH) - h1);
			yBot = true;
		} else {
			yBot = false;
		}
		Graphics g2 = this.getGraphics();
		g2.drawImage(image, i1x, i1y, null);
		g2.dispose();
	}

	public void imageInit() {

		tiles = new Image[3];
		ImageIcon ii = new ImageIcon(this.getClass().getResource(
				"res/Grass1/Grass.png"));
		tiles[0] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/Grass1/GrassL.png"));
		tiles[1] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("res/Wall2/Wall2.png"));
		tiles[2] = ii.getImage();

		entities = new Image[1];
		ii = new ImageIcon(this.getClass()
				.getResource("res/Entities/MikeS.png"));
		entities[0] = ii.getImage();

	}

	/**
	 * Listeners
	 * 
	 */
	@Override
	public void keyReleased(KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_W) {
			play.move(0);
		}
		if (ke.getKeyCode() == KeyEvent.VK_D) {
			play.move(1);
		}
		if (ke.getKeyCode() == KeyEvent.VK_S) {
			play.move(2);
		}
		if (ke.getKeyCode() == KeyEvent.VK_A) {
			play.move(3);
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent me) {

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
