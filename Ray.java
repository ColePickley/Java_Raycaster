package com.myproject;

public class Ray {
	public final double degree; //the degree from the player's directional vector that the ray is cast from
	public double length;
	public boolean isVerticalCollision;
	
	public Ray(double degree) {
		this.degree = degree;
		this.length = 0;
		this.isVerticalCollision = false;
	}
}
