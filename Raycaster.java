package com.myproject;

import java.awt.event.KeyEvent;

public class Raycaster {
	public static int[][] mapGrid;
	public static int gridSize;
	
	private static Player player;
	private static Ray[] rays;
	private RayCalculator rc;
	private BirdsEyeFrame bf;
	private GameFrame gf;
	
	public Raycaster(int[][] mapGrid, int gridSize, Player player) {
		if (mapGrid == null || player == null)
			throw new NullPointerException();
		if (mapGrid.length < 3 || mapGrid[0].length < 3)
			throw new IllegalArgumentException("mapGrid must be at least a 3x3 array");
		for (int i = 0; i < mapGrid.length; i++)
			if (mapGrid[i][0] == 0 || mapGrid[i][mapGrid[0].length - 1] == 0)
				throw new IllegalArgumentException("mapGrid must be enclosed");
		for (int i = 0; i < mapGrid[0].length; i++)
			if (mapGrid[0][i] == 0 || mapGrid[mapGrid.length - 1][i] == 0)
				throw new IllegalArgumentException("mapGrid must be enclosed");
		if (gridSize < 8)
			throw new IllegalArgumentException("gridSize must be at least 8");
		
		Raycaster.mapGrid = mapGrid;
		Raycaster.gridSize = gridSize;
		Raycaster.player = player;
		Raycaster.rays = new Ray[361];
		this.rc = new RayCalculator();
		this.bf = new BirdsEyeFrame();
		this.gf = new GameFrame();
	}
	
	public void start() {
		if (Raycaster.mapGrid[0].length * Raycaster.gridSize < Raycaster.player.x || Raycaster.player.x < 0)
			throw new IllegalArgumentException("Player position is outside the map");
		if (Raycaster.mapGrid.length * Raycaster.gridSize < Raycaster.player.y || Raycaster.player.y < 0)
			throw new IllegalArgumentException("Player position is outside the map");
		if (Raycaster.mapGrid[(int) (Raycaster.player.y / Raycaster.gridSize)][(int) (Raycaster.player.x / Raycaster.gridSize)] == 1)
			throw new IllegalArgumentException("Player position is within a wall");
		
		//creates the ray objects
		double degree = -45;
		for (int i = 0; i < 361; i++) {
			Raycaster.rays[i] = new Ray(degree);
			degree += 0.25;
		}
		this.rc.setRayLengths(player, rays);
		
		//starts the windows
		this.bf.start(player, rays);
		this.gf.start();
		this.run();
	}
	
	public static void rotatePlayer(double mouseVelocityX) {
		player.rotation -= mouseVelocityX / 100;
		if (player.rotation >= 360)
			player.rotation -= 360;
		else if (player.rotation < 0)
			player.rotation += 360;
	}
	
	public static Ray[] getRays() {
		return rays;
	}
	
	//checks for collision and moves the player
	private static void movePlayer() {
		if (!isXCollision())
			player.x += player.velocityX;
		if (!isYCollision())
			player.y += player.velocityY;
	}
	
	//checks for collision in the x direction
	private static boolean isXCollision() {
		if (mapGrid[(int) (player.y / gridSize)][(int) ((player.x + player.velocityX) / gridSize)] == 1)
			return true;
		return false;
	}
	
	//checks for collision in the y direction
	private static boolean isYCollision() {
		if (mapGrid[(int) ((player.y + player.velocityY) / gridSize)][(int) (player.x / gridSize)] == 1)
			return true;
		return false;
	}
	
	//receives a KeyEvent and sets the player's velocity according the direction they're going
	public static void setPlayerVel(KeyEvent e) {
		int speed = 3;
		if (e.getKeyCode() == KeyEvent.VK_A) {
			player.velocityX = -Math.sin(Math.toRadians(player.rotation + 90));
			player.velocityY = -Math.cos(Math.toRadians(player.rotation + 90));
		}
		else if (e.getKeyCode() == KeyEvent.VK_D) {
			player.velocityX = Math.sin(Math.toRadians(player.rotation + 90));
			player.velocityY = Math.cos(Math.toRadians(player.rotation + 90));
		}
		else if (e.getKeyCode() == KeyEvent.VK_W) {
			player.velocityX = Math.sin(Math.toRadians(player.rotation));
			player.velocityY = Math.cos(Math.toRadians(player.rotation));
		}
		else if (e.getKeyCode() == KeyEvent.VK_S) {
			player.velocityX = -Math.sin(Math.toRadians(player.rotation));
			player.velocityY = -Math.cos(Math.toRadians(player.rotation));
		}
		player.velocityX = player.velocityX * speed;
		player.velocityY = player.velocityY * speed;
	}
	
	public static void setPlayerVelX(double velX) {
		player.velocityX = velX;
	}
	
	public static void setPlayerVelY(double velY) {
		player.velocityY = velY;
	}
	
	//main game loop
	private void run() {
		long lastTime = System.nanoTime();
		final double AMOUNT_OF_TICKS = 30.0;
		double ns = 1000000000 / AMOUNT_OF_TICKS;
		double delta = 0;
		double timer = System.currentTimeMillis();
		while (true) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				Raycaster.movePlayer();
				rc.setRayLengths(player, rays);
				bf.update(player, rays);
				gf.update();
				delta--;
			}
			
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
			}
		}
	}
}
