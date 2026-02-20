package com.myproject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;

@SuppressWarnings("serial")
public class GameFrame extends JFrame implements KeyListener, MouseMotionListener, MouseListener{
	private final int lineWidth = 3;
	private final int frameWidth = 361 * lineWidth; //num rays * ray thickness
	private final int frameHeight = 512;
	private GameLayeredPane layeredPane;
	private double mouseVelocityX;
	private int lastMouseX, nowMouseX;
	private long lastMouseTime, nowMouseTime;
	
	public GameFrame() {
		layeredPane = new GameLayeredPane();
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setFocusable(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(0, 0);
		this.setTitle("Game");
		this.setResizable(false);
		this.setBackground(Color.white);
		this.add(layeredPane);
		this.pack();
	}
	
	public void start() {
		this.update();
		this.setVisible(true);
	}
	
	public void update() {
		layeredPane.repaint();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}


	@Override
	public void mousePressed(MouseEvent e) {
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		if (lastMouseX != -1 && lastMouseTime != -1) {
            nowMouseTime = System.nanoTime();
            long deltaTimeNanos = nowMouseTime - lastMouseTime;
            double deltaTimeSeconds = deltaTimeNanos / 1_000_000_000.0;
            nowMouseX = e.getX();
            int deltaX = lastMouseX - nowMouseX;
            if (deltaTimeSeconds > 0) {
                mouseVelocityX = deltaX / deltaTimeSeconds;
            } else {
                mouseVelocityX = 0;
            }
        }
        lastMouseX = e.getX();
        lastMouseTime = System.nanoTime();
        Raycaster.rotatePlayer(this.mouseVelocityX);
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		
	}


	@Override
	public void keyTyped(KeyEvent e) {
		
	}


	@Override
	public void keyPressed(KeyEvent e) {
		Raycaster.setPlayerVel(e);
	}


	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_S) {
			Raycaster.setPlayerVelX(0);
			Raycaster.setPlayerVelY(0);
		}
	}
	
	public class GameLayeredPane extends JLayeredPane {
		
		public GameLayeredPane() {
			this.setPreferredSize(new Dimension(frameWidth, frameHeight));
		}
		
		public void paint(Graphics g) {
			Graphics2D g2D = (Graphics2D) g;
			int lineLength;
			int i = 0;
			for (Ray ray: Raycaster.getRays()) {
				if (ray.isVerticalCollision)
					g2D.setPaint(Color.decode("#0000FF"));
				else
					g2D.setPaint(Color.decode("#0000D0"));
				lineLength = (int) (64 * frameHeight / (ray.length * Math.cos(Math.toRadians(ray.degree))));
				g2D.fillRect(i * lineWidth, (frameHeight - lineLength) / 2, lineWidth, lineLength);
				i++;
			}
		}
	}
}
