package perspective;

import perspective.tools.*; 

import java.awt.*;
import java.awt.image.*;
import javax.swing.JFrame;

public class Engine extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	private static boolean isRunning = false;

	public static final int WIDTH = 640;
	public static final int HEIGHT = WIDTH * 3 / 4;
	public static final int SCALE = 1;
	public static final double FRAME_LIMIT = 60.0;
	public static final String TITLE = "java-perspectives";
	public static JFrame j;

	public final BufferedImage image;
	public final int[] pixels;

	private Game game;
	private Screen screen;
	private InputHandler inputHandler;

	public Engine() {
		Dimension dimension = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
		setMinimumSize(dimension);
		setMaximumSize(dimension);
		setPreferredSize(dimension);

		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		inputHandler = new InputHandler();

		addKeyListener(inputHandler);
		addMouseListener(inputHandler);
		addFocusListener(inputHandler);
		addMouseMotionListener(inputHandler);
		addMouseWheelListener(inputHandler);
	}

	public static void main(String[] args) {
		j = new JFrame();
		j.setResizable(false);
		Engine engine = new Engine();
		j.add(engine);
		j.pack();
		j.setTitle(TITLE);
		j.setLocationRelativeTo(null);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		j.setAlwaysOnTop(true);
		j.setVisible(true);

		engine.start();
		
	}

	public void run() {
		final double nsPerUpdate = 1000000000.0 / FRAME_LIMIT;

		long lastTime = System.nanoTime();
		long frameCounter = System.currentTimeMillis();
		double unprocessedTime = 0;
		int frames = 0;
		int updates = 0;
		while (isRunning) {
			long currentTime = System.nanoTime();
			long passedTime = currentTime - lastTime;
			lastTime = currentTime;
			unprocessedTime += passedTime;
			if (unprocessedTime >= nsPerUpdate) 
			{
				unprocessedTime = 0;
				updates++;
				update();
			}
			render();
			frames++;
			if (System.currentTimeMillis() - frameCounter >= 1000) {
				System.out.println("Frames: " + frames + "\n" + "Updates: " + updates);
				frames = 0;
				updates = 0;
				frameCounter += 1000;
			}
		}
		dispose();
	}

	public void start() {
		if (isRunning) return;

		isRunning = true;
		init();
		new Thread(this).start();
	}

	public void init() {
		game = new Game();
		screen = new Screen(WIDTH, HEIGHT);
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		for (int i=0; i < pixels.length; i++) 
			pixels[i] = 0;

		screen.render(game);
		for (int i=0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}

		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g.dispose();
		bs.show();
	}

	public void update() {
		game.update(inputHandler.keys);
		screen.update();
	}

	public void stop() {
		if (!isRunning) return;

		isRunning = false;
	}

	public void dispose() {
		System.exit(0);
	}
}