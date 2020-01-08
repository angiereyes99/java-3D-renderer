package perspective;

import perspective.Game;

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
		// rot = game.player.rot;
		// xCam = game.player.x;
		// yCam = game.player.y;
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
			}
		}
		renderWall(0,2,1,2);
	}

	public void renderWall(double x0, double y0, double x1, double y1) {
		double xo0 = x0 - 0.5 - xCam * 2;
		double u0 = -0.5 + zCam / 4;
		double d0 = +0.5 + zCam / 4;
		double zo0 = y0 - yCam * 2;

		double xx0 = xo0 * rCos + zo0 * rSin;
		double zz0 = -xo0 * rSin + zo0 * rCos;

		double xo1 = x1 - 0.5 - xCam * 2;
		double u1 = -0.5 + zCam / 4;
		double d1 = +0.5 + zCam / 4;
		double zo1 = y1 - yCam * 2;

		double xx1 = xo1 * rCos + zo1 * rSin;
		double zz1 = -xo1 * rSin + zo1 * rCos;

		double xPixel0 = xx0 / zz0 * fov + width / 2.0 + 0.5;
		double xPixel1 = xx1 / zz1 * fov + width / 2.0 + 0.5;

		if (xPixel0 > xPixel1) return;

		int xp0 = (int) Math.floor(xPixel0);
		int xp1 = (int) Math.floor(xPixel1);
		if (xp0 < 0) xp0 = 0;
		if (xp1 > width) xp1 = width;

		double yPixel00 = (u0 / zz0 * fov + height / 2.0) + 0.5;
		double yPixel10 = (u1 / zz1 * fov + height / 2.0) + 0.5;
		double yPixel01 = (d0 / zz0 * fov + height / 2.0) + 0.5;
		double yPixel11 = (d1 / zz1 * fov + height / 2.0) + 0.5;

		double iz0 = zz0;
		double iz1 = zz1;

		for (int x = xp0; x < xp1; x++) {
			double p = (x - xPixel0) / (xPixel1 - xPixel0);

			double yPixel0 = yPixel00 + (yPixel10 - yPixel00) * p;
			double yPixel1 = yPixel01 + (yPixel11 - yPixel01) * p;

			double iz = iz0 + (iz1 - iz0) * p;

			if (yPixel10 > yPixel11) return;

			int yp0 = (int) Math.floor(yPixel0);
			int yp1 = (int) Math.floor(yPixel1);
			if (xp0 < 0) xp0 = 0;
			if (xp1 > width) xp1 = width;

			for (int y = yp0; y < yp1; y++) {
				pixels[x + y * width] = 0xff00ff;
				depthBuffer[x + y * width] = iz * 10;
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

			double brightness = 255 - depthBuffer[i] * 5;
			red = (int) (red / 255.0 * brightness);
			green = (int) (green / 255.0 * brightness);
			blue = (int) (blue / 255.0 * brightness);

			pixels[i] = red << 16 | green << 16 | blue;
		}
	}
}