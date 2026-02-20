package com.myproject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;

@SuppressWarnings("serial")
public class BirdsEyeFrame extends JFrame{
	private final int frameWidth;
	private final int frameHeight;
	private BirdsEyeLayeredPane layeredPane;
	private Player player;
	private Ray[] rays;
	
	public BirdsEyeFrame() {
		this.frameWidth = Raycaster.gridSize * Raycaster.mapGrid[0].length;
		this.frameHeight = Raycaster.gridSize * Raycaster.mapGrid.length;
		this.layeredPane = new BirdsEyeLayeredPane();
		this.setFocusable(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(0, 0);
		this.setTitle("BirdsEye");
		this.setResizable(false);
		this.setBackground(Color.white);
		this.add(layeredPane);
		this.pack();
	}
	
	public void start(Player player, Ray[] rays) {
		this.update(player, rays);
		this.setVisible(true);
	}
	
	public void update(Player player, Ray[] rays) {
		this.player = player;
		this.rays = rays;
		this.layeredPane.repaint();
	}
	
	public class BirdsEyeLayeredPane extends JLayeredPane {
		public BirdsEyeLayeredPane() {
			this.setPreferredSize(new Dimension(frameWidth, frameHeight));
		}
		
		public void paint(Graphics g) {
			Graphics2D g2D = (Graphics2D) g;
			this.paintMap(g2D);
			this.paintRays(g2D);
			g2D.setPaint(Color.red);
			g2D.fillOval((int) Math.round(player.x) - 5, (int) Math.round(player.y) - 5, 10, 10);
		}
		
		private void paintMap(Graphics2D g) {
			g.setPaint(Color.black);
			g.fillRect(0, 0, frameWidth, frameHeight);
			for (int i = 0; i < Raycaster.mapGrid.length; i++) {
				for (int j = 0; j < Raycaster.mapGrid[0].length; j++) {
					if (Raycaster.mapGrid[i][j] == 0)
						g.setPaint(Color.white);
					else
						g.setPaint(Color.blue);
					g.fillRect(Raycaster.gridSize * j + 2, i * Raycaster.gridSize + 2, Raycaster.gridSize - 2, Raycaster.gridSize - 2);
				}
			}
		}
		
		private void paintRays(Graphics2D g) {
			g.setPaint(Color.red);
			double angle;
			for (Ray ray : rays) {
				angle = Math.toRadians(player.rotation + ray.degree);
				if (angle >= 2 * Math.PI)
					angle -= 2 * Math.PI;
				else if (angle < 0)
					angle += 2 * Math.PI;
				g.drawLine((int) Math.round(player.x), (int) Math.round(player.y), 
						(int) (player.x + ray.length * Math.sin(angle)), (int) (player.y + ray.length * Math.cos(angle)));
			}
		}
	}
}
