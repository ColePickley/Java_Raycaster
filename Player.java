package com.myproject;

public class Player {
	public double x, y;
	public double rotation;
	public double velocityX, velocityY;
	
	public Player(int gridX, int gridY, double rotation, int gridSize) {
		//#centers the player's position within the specified grid space
		this.x = gridX * gridSize + gridSize / 2;
		this.y = gridY * gridSize + gridSize / 2;
		
		this.rotation = rotation + 0.0001;
		this.velocityX = 0;
		this.velocityY = 0;
	}
}
