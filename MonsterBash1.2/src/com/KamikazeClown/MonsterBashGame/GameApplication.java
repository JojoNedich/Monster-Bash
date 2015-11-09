package com.KamikazeClown.MonsterBashGame;

import javafx.application.Application;
import javafx.stage.Stage;

public class GameApplication extends Application {

	@Override
	public void start(Stage primaryStage) {
		BaseGame game = new MonsterApp2();
		game.start(primaryStage);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
