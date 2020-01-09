package perspective; 

import static java.awt.event.KeyEvent.*;

public class Game {

	public Player player;
	public int time;

	public Game() {
		player = new Player();
	}

	public void update(boolean[] keys) {
		time++;
		boolean up = keys[VK_W];
		boolean down = keys[VK_S];
		boolean left = keys[VK_A];
		boolean right = keys[VK_D];
		boolean turnLeft = keys[VK_Q];
		boolean turnRight = keys[VK_E];

		player.update(up, down, left, right, turnLeft, turnRight);
	}
}