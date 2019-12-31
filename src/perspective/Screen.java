package perspective;

import java.util.*;

public class Screen extends BitMap {

	public Random r = new Random();
	public BitMap test;
	public BitMap3D perspectiveVision;

	public Screen(int width, int height) {
		super(width, height);

		test = new BitMap(50, 50);
		for (int i=0; i < test.pixels.length; i++) {
			test.pixels[i] = r.nextInt();
		}
		perspectiveVision = new BitMap3D(width, height);
	}

	int t;

	public void render() {
		t++;
		int ox = (int) (Math.sin(t / 1000.0) * width/2);
		int oy = (int) (Math.cos(t / 1000.0) * height/2);

		//clear();
		perspectiveVision.render();
		perspectiveVision.renderFog();
		render(perspectiveVision, 0, 0);
	}

	public void update() {

	}
}