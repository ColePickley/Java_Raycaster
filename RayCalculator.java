package com.myproject;

public class RayCalculator {
	private int gridX, gridY; //grid location the current end of the ray exists in
	private double x, y; //coordinates of the current end of the ray
	private double rad; //rotation of the ray in radians
	private int orientation; //this is an integer value assigned using rad to help with simplifying the code
	
	/*
	Each ray has two potential lengths which are calculated with the grid and ray deltas.
    The two ray lengths are then compared to determine which one is correct. Finally, that
    information is stored in the final deltas and length variables.
	*/
	private double length1, length2;
	private double gridDeltaX, gridDeltaY;
	private double rayDeltaX, rayDeltaY;
	private double finalLength;
	private double finalDeltaX, finalDeltaY;
	
	//Sets the length of all the Ray objects
	//This is the primary function of the RayCalculator class
	public void setRayLengths(Player player, Ray[] rays) {
		for (Ray ray : rays) {
			double length = 0;
			//resets variables for the new Ray
			this.setCoords(player.x, player.y);
			this.setRad(player.rotation, ray.degree);
			this.setOrientation();
			this.setGridCoords();
			
			//this loop checks one grid space at a time for a wall collision
			boolean collision = false;
			while (!collision) {
				this.cycleVars();
				this.setVerticalCollision(ray);
				this.setGridCoords();
				if (Raycaster.mapGrid[this.gridY][this.gridX] == 1)
					collision = true;
				length += this.finalLength;
			}
			ray.length = length;
		}
	}
	
	//calculates ray properties for the next grid space
	private void cycleVars() {
		this.setGridDeltas();
		this.setRayDeltas();
		this.setLengths();
		this.setFinalDeltas();
		this.moveCoords();
	}
	
	private void setCoords(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	private void moveCoords() {
		this.x += this.finalDeltaX;
		this.y += this.finalDeltaY;
	}
	
	private void setRad(double pr, double deg) {
		this.rad = Math.toRadians(pr + deg);
		if (this.rad < 0)
			rad += Math.PI * 2;
		else if (this.rad >= Math.PI * 2)
			rad -= Math.PI * 2;
	}
	
	private void setOrientation() {
		if (this.rad == 0)
			this.orientation = 0;
		else if (this.rad < Math.PI / 2)
			this.orientation = 1;
		else if (this.rad == Math.PI / 2)
			this.orientation = 2;
		else if (this.rad < Math.PI)
			this.orientation = 3;
		else if (this.rad == Math.PI)
			this.orientation = 4;
		else if (this.rad < 3 * Math.PI / 2)
			this.orientation = 5;
		else if (this.rad == 3 * Math.PI / 2)
			this.orientation = 6;
		else
			this.orientation = 7;
	}
	
	private void setGridDeltas() {
		if (this.orientation == 0 || this.orientation == 4)
			this.gridDeltaX = 0;
		else if (this.orientation < 4)
          this.gridDeltaX = Raycaster.gridSize - (this.x % Raycaster.gridSize);
		else if (this.x % Raycaster.gridSize == 0)
          this.gridDeltaX = -Raycaster.gridSize;
		else
          this.gridDeltaX = -1 * (this.x % Raycaster.gridSize);

		if (this.orientation == 2 || this.orientation == 6)
			this.gridDeltaY = 0;
		else if (this.orientation < 2 || this.orientation > 6)
			this.gridDeltaY = Raycaster.gridSize - (this.y % Raycaster.gridSize);
		else if (this.y % Raycaster.gridSize == 0)
			this.gridDeltaY = -Raycaster.gridSize;
		else
			this.gridDeltaY = -1 * (this.y % Raycaster.gridSize);
	}
	
	private void setRayDeltas() {
		switch (this.orientation) {
			case 0, 4:
				this.rayDeltaX = 0;
				this.rayDeltaY = this.gridDeltaY;
			case 2, 6:
				this.rayDeltaX = this.gridDeltaX;
				this.rayDeltaY = 0;
			case 1, 5:
				this.rayDeltaX = Math.tan(this.rad) * this.gridDeltaY;
				this.rayDeltaY = this.gridDeltaX / Math.tan(this.rad);
			case 3, 7:
				this.rayDeltaX = -this.gridDeltaY / Math.tan(this.rad - Math.PI / 2);
				this.rayDeltaY = -Math.tan(this.rad - Math.PI / 2) * this.gridDeltaX;
		}
	}
	
	private void setLengths() {
		this.length1 = Math.sqrt(this.rayDeltaX * this.rayDeltaX + this.gridDeltaY * this.gridDeltaY);
		this.length2 = Math.sqrt(this.rayDeltaY * this.rayDeltaY + this.gridDeltaX * this.gridDeltaX);
	}
	
	private void setFinalDeltas() {
		if (this.length1 == this.length2) {
			this.finalDeltaX = this.gridDeltaX;
			this.finalDeltaY = this.gridDeltaY;
			this.finalLength = this.length1;
		}
		else if (this.length1 > this.length2) {
			this.finalDeltaX = this.gridDeltaX;
			this.finalDeltaY = this.rayDeltaY;
			this.finalLength = this.length2;
		}
		else {
			this.finalDeltaX = this.rayDeltaX;
			this.finalDeltaY = this.gridDeltaY;
			this.finalLength = this.length1;
		}
	}
	
	private void setGridCoords() {
		if (4 < this.orientation && this.x % Raycaster.gridSize == 0)
			this.gridX = (int) (this.x / Raycaster.gridSize) - 1;
		else
			this.gridX = (int) (this.x / Raycaster.gridSize);

		if (2 < this.orientation && this.orientation < 6 && this.y % Raycaster.gridSize == 0)
			this.gridY = (int) (this.y / Raycaster.gridSize) - 1;
		else
			this.gridY = (int) (this.y / Raycaster.gridSize);
	}
	
	/*
	Tells the ray object if it's collision was on a vertical grid line.
    This information determines the color that the ray will be drawn with in the GameFrame.
	*/
	private void setVerticalCollision(Ray ray) {
		if (this.orientation == 2 || this.orientation == 6)
            ray.isVerticalCollision = true;
        else if (this.orientation == 0 || this.orientation == 4)
            ray.isVerticalCollision = false;
        else if (this.length1 > this.length2)
            ray.isVerticalCollision = true;
        else
            ray.isVerticalCollision = false;
	}
}
