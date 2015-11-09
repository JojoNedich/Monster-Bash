package com.KamikazeClown.MonsterBashGame;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;

public class Sprite {
	public double x;
	public double y;

	private double width;
	private double height;

	private Image image;

	public Sprite(String imagePath, double x, double y, double width, double height) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.image = new Image(imagePath);

	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public Circle getBoundery() {
		return new Circle(x+ (height/2), y + (height/2), height/2 );
	}

	public void render(GraphicsContext graphicsContext) {
		graphicsContext.drawImage(image, x, y, width, height);
	}
}
