package Raycaster;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Raycaster {
	public static void main(String[] args) {

		String[] map =
		      { "####################", "#__________________#", "#__________________#", "#__________________#", "#__________________#", "#________##________#", "#________##________#", "#__________________#", "#__________________#", "#__________________#", "####################",

				};
		new g1(map);

	}

}



class g1 {

	String[] map;
	int sx, sy;
	final int width = 500;
	final int height = 500;
	final int md = 30;
	final int ss = 10;
	final int xOffset = 510;
	final int yOffset = 40;

	int px = 5, py = 9;
	int pa = 0;
	int prx = 100;
	int pry = 100;


	int rhx, rhy, rhd;

	public g1(String[] map) {


		sx = map[0].length();
		sy = map.length;
		System.out.println(sx + " " + sy);
		this.map = map;
		initFrame();
		f.requestFocusInWindow();
		try {
			gameLoop();
		} catch(InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void gameLoop() throws InterruptedException {
		Thread.sleep(100);
		while (true) { render(); Thread.sleep(100); }
	}

	final int rA = 17;
	int[] toDraw = new int[rA];
	void render() {

		BufferStrategy buf = c.getBufferStrategy();


		if (buf == null) {
			c.createBufferStrategy(2);
			buf = c.getBufferStrategy();
		}
		Graphics2D g = (Graphics2D) buf.getDrawGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);


		g.setColor(Color.black);
		g.fillRect(0, 0, width, height);

		g.setColor(Color.white);
		g.fillRect(500, 0, 300, height);

		draw2dMap(g);
		int laxS = 0, layS = 0;
		int axp = 0, ayp = 0;

		int add = 6;

		switch (pa) {
			case 0:
				laxS = 0;
				layS = -100;
				axp = -add;
				ayp = 0;
				break;

			case 45:
				laxS = 50;
				layS = -50;
				axp = -add / 2;
				ayp = -add / 2;
				break;

			case 90:
				laxS = 100;
				layS = 0;
				axp = 0;
				ayp = -add;
				break;

			case 135:
				laxS = 50;
				layS = 50;
				axp = add / 2;
				ayp = -add / 2;
				break;

			case 180:
				laxS = 0;
				layS = 100;
				axp = -add;
				ayp = 0;
				break;

			case 225:
				laxS = -50;
				layS = 50;
				axp = -add / 2;
				ayp = -add / 2;
				break;

			case 270:
				laxS = -100;
				layS = 0;
				axp = 0;
				ayp = add;
				break;

			case 315:
				laxS = -50;
				layS = -50;
				axp = add / 2;
				ayp = -add / 2;
				break;

		}




		int lax = laxS, lay = layS;
		ray(px, py, rA / 2, lax, lay, g);
		for (int h = rA / 2 + 1; h < rA; h++) { int angleX = lax += axp; int angleY = lay += ayp; ray(px, py, h, angleX, angleY, g); }
		lax = laxS;
		lay = layS;
		for (int h = rA / 2 - 1; h > -1; h--) {
			int angleX = lax += axp * -1;
			int angleY = lay += ayp * -1;


			ray(px, py, h, angleX, angleY, g);
		}


		draw3d(g);




		drawPlayer(g);
		toDraw = new int[rA];

		g.dispose();
		buf.show();
		buf.dispose();

	}

	void draw3d(Graphics2D g) {
		int rectwidth = width / rA;

		for (int j = 0; j < rA; j++) {
			int d = toDraw[j];
			if (d == -1) { continue; }

			int x1 = rectwidth * j, y1 = d * 10 + 60;

			g.setColor(Color.white);
			g.fillRect(x1, y1, rectwidth, height - y1 - y1);
			g.setColor(Color.orange);
			g.setStroke(new BasicStroke(6));
			g.drawRect(x1, y1, rectwidth, height - y1 - y1);

		}
	}



	void ray(int x, int y, int index, int px, int py, Graphics2D g) {
		int x1 = x * 100, y1 = y * 100;

		int d = 0;
		int mx, my;

		for (int h = 0; h < md; h++) {
			mx = x1 / 100;
			my = y1 / 100;

			if (mx < 0 || my < 0 || mx >= sx || my >= sy) {
				toDraw[index] = -1;
				break;
			}

			char c = map[my].charAt(mx);
			if (c == '#') {
				toDraw[index] = d;

				break;
			}
			d++;
			x1 += px;
			y1 += py;

		}

		g.setColor(Color.MAGENTA);
		g.setStroke(new BasicStroke(2));

		int lx1 = this.px * ss + xOffset + ss / 2 + px;
		int ly1 = this.py * ss + yOffset + ss / 2 + py;

		g.drawLine(this.px * ss + xOffset + ss / 2, this.py * ss + yOffset + ss / 2, lx1, ly1);
	}


	void draw2dMap(Graphics2D g) {
		g.setColor(Color.white);
		g.fillRect(xOffset, yOffset, sx * ss, sy * ss);
		for (int y = 0; y < sy; y++) {
			for (int x = 0; x < sx; x++) {
				char c = map[y].charAt(x);
				if (c == '#') {
					g.setColor(Color.black);
					g.fillRect(x * ss + xOffset, y * ss + yOffset, ss, ss);
				}


			}
		}
	}

	void drawPlayer(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(px * ss + xOffset, py * ss + yOffset, ss, ss);
	}


	JFrame f;
	Canvas c;
	void initFrame() {
		f = new JFrame() {
			private static final long serialVersionUID = 1L;
			@Override
			public void paint(Graphics g) {}
		};
		f.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) { f.requestFocusInWindow(); }

			@Override
			public void focusLost(FocusEvent e) {

			}
		});
		f.setIgnoreRepaint(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		f.setResizable(false);

		c = new Canvas();
		c.setSize(width + 300, height);
		c.setIgnoreRepaint(true);
		c.setVisible(true);



		f.add(c);
		f.pack();
		f.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				char key = e.getKeyChar();
				switch (key) {
					case 'w':
						py--;
						break;
					case 's':
						py++;
						break;
					case 'a':
						px--;
						break;
					case 'd':
						px++;
						break;
					case 'm':
						pa += 45;
						if (pa == 360) pa = 0;
						break;
					case 'n':
						pa -= 45;
						if (pa == -45) pa = 315;
						break;

				}
			}

			@Override
			public void keyReleased(KeyEvent e) {}

		});
	}



}
