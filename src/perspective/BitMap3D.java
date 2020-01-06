package perspective;

import java.util.*;

public class BitMap3D extends BitMap {

	int t;
	int xPix;
	int yPix;
	int color;
	double xd;
	double yd;
	double zd;
	double rSin;
	double rCos;
	double xCam = 0;
	double yCam = 0;
	double zCam = 4;
	double rot = 0;
	private double fov = height;
	private double[] depthBuffer;

	public BitMap3D(int width, int height) {
		super(width, height);
		depthBuffer = new double[width * height];
	}

	public void render(Game game) {
		t++;
		rot = Math.sin(game.time / 40.0) * 0.5;
		rot = game.player.rot;
		xCam = game.player.x;
		yCam = game.player.y;
		zCam = 0;
		rSin = Math.sin(rot);
		rCos = Math.cos(rot);

		for (int y=0; y < height; y++) {
			yd = ((y + 0.5) - (height / 2)) / fov;
			zd = (4 + zCam) / yd;
			if (yd < 0) 
				zd = (8 - zCam) / -yd;
			for (int x=0; x < width; x++) {
				xd = (x - (width / 2)) / fov;
				xd *= zd;

				double xx = xd * rCos - zd * rSin + (xCam + 1) * 8;
				double yy = xd * rSin + zd * rCos + (yCam) * 8;

				xPix = (int) xx * 2;
				yPix = (int) yy * 2;

				if (xx < 0) xPix--;
				if (yy < 0) yPix--;

				depthBuffer[x + y * width] = zd;
				// pixels[x + y * width] = Textures.floor.pixels[(xPix & 8) | (yPix & 8) * Textures.floor.width];
				pixels[x + y * width] = ((yPix & 15) * 16) << 8 | ((xPix & 15) *16);
				// if ((xPix & 15) == 0) {
				// 	pixels[x + y * width] = 0xff00ff;
				// }
			}
		}
		//renderWall();
	}

	public void renderWall() {
		Random r = new Random(100);
		for (int i=0; i < 100000; i++) {
			double x = r.nextDouble() * 2 - 1;
			double y = r.nextDouble() * 2 - 1;
			double z = 4;
		

			int xPixel = (int) (x / z * fov + width / 2.0);
			int yPixel = (int) (y / z * fov + height / 2.0);

			if (xPixel >= 0 && xPixel < width && yPixel >= 0 && yPixel < height) {
				depthBuffer[xPixel + yPixel * width] = z;
				pixels[xPixel + yPixel * width] = 0xff00ff;
			}
		}
	}

	public void renderFog() {
		for (int i=0; i < depthBuffer.length; i++) {
			color = pixels[i];
			// colors //
			int red = (color >> 16) & 0xff;
			int green = (color >> 8) & 0xff;
			int blue = (color) & 0xff;

			double brightness = 255 - depthBuffer[i] * 3;
			red = (int) (red / 255.0 * brightness);
			green = (int) (green / 255.0 * brightness);
			blue = (int) (blue / 255.0 * brightness);

			pixels[i] = red << 16 | green << 16 | blue;
		}
	}
}