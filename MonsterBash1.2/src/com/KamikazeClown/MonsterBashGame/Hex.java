package com.KamikazeClown.MonsterBashGame;

import com.sun.javafx.scene.paint.GradientUtils.Point;


import javafx.geometry.Point2D;
import javafx.scene.control.Label;

public class Hex extends Sprite {

	public static final int HEX_PIXEL_WIDTH = 100;
	public static final int HEX_PIXEL_HEIGHT = 86;
	public static final int HEX_SIDE = HEX_PIXEL_WIDTH / 2;
	public static final int UIOffsetX = 66;
	public static final int UIOffsetY = 66;

	public Hex(String imagePath, double x, double y, double width, double height, int hp) {
		super(imagePath, x, y, width, height);
		this.hp = hp;
		this.hpLabel = new Label(Integer.toString(hp));
		hpLabel.setLayoutX(getX() + UIOffsetX);
		hpLabel.setLayoutY(getY() + UIOffsetY);
	}

	private int hp;
	public Label hpLabel;

	public int getHp() {
		return hp;
	}

	public void decrementHp() {
		this.hp--;
		hpLabel.setText(Integer.toString(hp));
	}

	public Point2D getCenter() {
		double centerX = getX() + HEX_PIXEL_WIDTH/2;
		double centerY = getY() + HEX_PIXEL_HEIGHT/2;

		return new Point2D(centerX, centerY);
	}
	
	
	
}
