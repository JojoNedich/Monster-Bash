package com.KamikazeClown.MonsterBashGame;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public abstract class BaseGame extends AnimationTimer {
	protected Scene scene;
	protected Scene scene2;
	protected Group root;
	protected Group root2;
	protected Canvas canvas;
	protected Canvas winCanvas;
	protected Stage primaryStage;
	protected Label winLabel;

	private double lastTime = -1;
	private boolean doUpdate = true;
	private Canvas canvas2;
	private Sprite winScreen;
	private TextField winText;


	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Monster Bash");
		root = new Group();
		root2 = new Group();

		scene = new Scene(root);
		scene2 = new Scene(root2, 1024, 700);
		canvas = new Canvas(1024, 700);
		this.canvas2 = new Canvas(1024, 700);
		winCanvas = new Canvas(1024, 700);
		root.getChildren().add(canvas);

		this.winScreen = new Sprite("LavaHand.jpg", 0, 0, canvas2.getWidth(), canvas2.getHeight());


		addEventListeners();
		setupGame();

		this.start();
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void handle(long now) {
		if (lastTime == -1) {
			this.lastTime = now;
		}
		final double delta = (now - lastTime) / 1000000000;
		lastTime = now;
		if (this.isDoUpdate()) {
			updateGame(delta);
			drawGame();
		} else {
			primaryStage.setScene(scene2);
			winScreen.render(canvas2.getGraphicsContext2D());
			primaryStage.show();

			return;
		}
	}

	protected abstract void setupGame();

	protected abstract void updateGame(double deltaTime);

	protected abstract void drawGame();

	protected abstract void onMousePressed(MouseEvent event);

	protected abstract void onMouseReleased(MouseEvent event);

	private void addEventListeners() {
		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				onMousePressed(event);
			}
		});

		scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				onMouseReleased(event);
			}
		});
		scene.setOnMouseMoved(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

			}

		});

	}

	public boolean isDoUpdate() {
		return doUpdate;
	}

	public void setDoUpdate(boolean doUpdate) {
		this.doUpdate = doUpdate;
	}
}
