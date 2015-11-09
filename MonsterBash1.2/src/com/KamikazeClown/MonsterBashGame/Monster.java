package com.KamikazeClown.MonsterBashGame;

import javafx.geometry.Point2D;

public class Monster extends Sprite {

	public Monster(String imagePath, double x, double y, double width, double height) {
		super(imagePath, x, y, width, height);
		// TODO Auto-generated constructor stub
	}

	public Hex currentHex;
	private int turnPoints;
	private int bashCounter;

	public void move(Hex hex) {
		double xCoordinate, yCoordinate;

		Point2D hexCenter = hex.getCenter();

		xCoordinate = hexCenter.getX() - getWidth() / 2;
		yCoordinate = hexCenter.getY() - (getHeight() / 2) -2;

		setX(xCoordinate);
		setY(yCoordinate);

		currentHex = hex;

	}

	public void resetTurnPoints() {
		turnPoints = 3;
	}

	public void decremetnTurnPoints() {
		turnPoints--;
	}

	public int getTurnPoints() {
		return turnPoints;
	}

	public int getBashCounter() {
		return bashCounter;
	}

	public void incrementBashCounter() {
		bashCounter++;
	}
	
	public void resetBashCounter(){
		bashCounter = 0;
	}
}
