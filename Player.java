package MiniMon;

import java.awt.Graphics;

public class Player {
	static Graphics g;

	int xx = 0;
	int yy = 0;

	// impasssable blocks
	int[] impBlocks = { 3 };

	// directing the player is moving in
	int moveingDir = -1;
	// how many ticks the player has moves (takes 4 to move a full block)
	int numMoves = 0;
	// if the player can move
	boolean canMove = true;

	boolean moveNorth = false, moveEast = false, moveSouth = false,
			moveWest = false;

	void setGraphics(Graphics ge) {
		g = ge;
	}

	void drawPlayer() {
		g.drawImage(WalkingPanel.entities[0], xx, yy, null);
	}

	void updatePlayer() {
		// set to true if the player is trying to walk into a wall.
		boolean cantWalk = false;
		if (canMove) {
			// key commands
			if ((moveNorth && !moveSouth && !moveEast && !moveWest)
					|| (moveNorth && !moveSouth && moveEast && moveWest)) {
				// is the block above passable?
				for (int p = 0; p < impBlocks.length; p++) {
					try {
						if (WalkingPanel.map.get((yy / 32) - 1)[xx / 32] == impBlocks[p]) {
							cantWalk = true;
						}
					} catch (Exception ex) {
						cantWalk = true;
					}
				}
				if (!cantWalk) {
					moveingDir = 0;
					canMove = false;
				}
			}
			if ((moveEast && !moveWest && !moveNorth && !moveSouth)
					|| (moveEast && !moveWest && moveNorth && moveSouth)) {
				for (int p = 0; p < impBlocks.length; p++) {
					try {
						if (WalkingPanel.map.get(yy / 32)[(xx / 32) + 1] == impBlocks[p]) {
							// cant walk
							cantWalk = true;
						}
					} catch (Exception ex) {
						cantWalk = true;
					}
				}
				if (!cantWalk) {
					moveingDir = 1;
					canMove = false;
				}
			}
			if ((moveSouth && !moveNorth && !moveEast && !moveWest)
					|| (moveSouth && !moveNorth && moveWest && moveEast)) {
				for (int p = 0; p < impBlocks.length; p++) {
					try {
						if (WalkingPanel.map.get((yy / 32) + 1)[xx / 32] == impBlocks[p]) {
							// cant walk
							cantWalk = true;
						}
					} catch (Exception ex) {
						cantWalk = true;
					}
				}
				if (!cantWalk) {
					moveingDir = 2;
					canMove = false;
				}
			}
			if ((moveWest && !moveEast && !moveSouth && !moveNorth)
					|| (moveWest && !moveEast && moveNorth && moveSouth)) {
				for (int p = 0; p < impBlocks.length; p++) {
					try {
						if (WalkingPanel.map.get(yy / 32)[(xx / 32) - 1] == impBlocks[p]) {
							// cant walk
							cantWalk = true;
						}
					} catch (Exception ex) {
						cantWalk = true;
					}
				}
				if (!cantWalk) {
					moveingDir = 3;
					canMove = false;
				}
			}
		}
		if (moveingDir != -1) {
			if (numMoves != 4) {
				if (moveingDir == 0) {
					yy -= 8;
					numMoves += 1;
				}
				if (moveingDir == 1) {
					xx += 8;
					numMoves += 1;
				}
				if (moveingDir == 2) {
					yy += 8;
					numMoves += 1;
				}
				if (moveingDir == 3) {
					xx -= 8;
					numMoves += 1;
				}
			} else {
				numMoves = 0;
				moveingDir = -1;
				canMove = true;
			}
		}
		moveNorth = false;
		moveEast = false;
		moveSouth = false;
		moveWest = false;
	}

	void move(int dir) {
		if (dir == 0) {
			moveNorth = true;
		}
		if (dir == 1) {
			moveEast = true;
		}
		if (dir == 2) {
			moveSouth = true;
		}
		if (dir == 3) {
			moveWest = true;
		}
	}
}
