package com.KamikazeClown.MonsterBashGame;

import java.awt.Image;
import java.awt.Point;
import java.lang.reflect.GenericArrayType;
import java.security.InvalidParameterException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;

import javax.management.RuntimeErrorException;

import com.sun.prism.paint.Color;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class MonsterApp2 extends BaseGame {
	private Sprite blackBackgrond;
	private Sprite sidePanel;
	private static final int COLUMNS = 3;
	private static final int ROWS = 8;
	private static final int MONSTER_PIXEL_WIDTH = 75;
	private static final int MONSTER_PIXEL_HEIGHT = 57;
	// Indent from the side of the screen for the board
	private static final int INDENT_Y = 50;

	// indent from the top of the screen for the board
	private static final int INDENT_X = 50;

	private Monster monster1;
	private Sprite monster1Drawing;
	private Monster monster2;
	private Sprite monster2Drawing;
	private static Monster currentMonster;
	private Monster altMonster;

	private Sprite background;

	public Hex[][] hexes;

	private Button move;
	private Button bash;
	private Button deleteMe;
	private Button punch;

	private boolean moveMode;
	private boolean bashMode;

	public boolean isPlayer1Move;
	private TextField playerIndicator;
	private TextField moveIndicator;

	private String currentPlayer;

	ArrayList<Sprite> highlights;
	double horizontalDistanceBetweenAjecentHex;
	double verticalDistanceBetweenAjecentHex;
	double verticalDistanceBetweenAjecentHexOnDifrentColumns;

	private boolean isGameWon;

	/*
	 * "No such thing as too many global variables." - Ben "Yahtzee" Crowshaw
	 */

	@Override
	protected void setupGame() {
		background = new Sprite("Lava.jpg", 0, 0, 1024, 768);
		initBoard();
		initMonsters();
		initUI();
		isPlayer1Move = true;
		initHeader();
		highlights = new ArrayList<>();
		horizontalDistanceBetweenAjecentHex = hexes[0][0].getCenter().distance(hexes[1][0].getCenter());
		verticalDistanceBetweenAjecentHex = hexes[0][0].getCenter().distance(hexes[2][0].getCenter());
		verticalDistanceBetweenAjecentHexOnDifrentColumns = hexes[1][0].getY() - hexes[0][0].getY();
	}

	private void initHeader() {

		if (isPlayer1Move) {
			currentPlayer = "Player1";
		} else {
			currentPlayer = "Player2";
		}

		playerIndicator = new TextField(currentPlayer);
		playerIndicator.setEditable(false);
		playerIndicator.setLayoutX(canvas.getWidth() - 450);

		playerIndicator.setLayoutY(0);
		playerIndicator.setPrefSize(400, 50);
		playerIndicator.setAlignment(Pos.CENTER);
		playerIndicator.setStyle("" + "-fx-font-size: 40px;" + "-fx-font-style: italic;" + "-fx-font-weight: bold;"
				+ "-fx-font-family: fantasy;" + "-fx-text-fill: black;" + "-fx-background-color: white");

		root.getChildren().add(playerIndicator);

	}

	private void initMonsters() {
		monster1 = new Monster("Monster.png", 0, 0, MONSTER_PIXEL_WIDTH, MONSTER_PIXEL_HEIGHT);
		monster1.currentHex = hexes[0][0];
		monster1.resetTurnPoints();
		monster1.resetBashCounter();

		monster1.x = monster1.currentHex.getX() + Hex.HEX_PIXEL_WIDTH / 2 - MONSTER_PIXEL_WIDTH / 2;
		monster1.y = monster1.currentHex.getY() + Hex.HEX_PIXEL_HEIGHT / 2 - MONSTER_PIXEL_HEIGHT / 2;

		currentMonster = monster1;

		monster2 = new Monster("Monster2.png", 0, 0, MONSTER_PIXEL_WIDTH, MONSTER_PIXEL_HEIGHT);
		monster2.currentHex = hexes[6][2];
		monster2.resetTurnPoints();
		monster2.resetBashCounter();

		monster2.x = monster2.currentHex.getX() + Hex.HEX_PIXEL_WIDTH / 2 - MONSTER_PIXEL_WIDTH / 2;
		monster2.y = monster2.currentHex.getY() + Hex.HEX_PIXEL_HEIGHT / 2 - MONSTER_PIXEL_HEIGHT / 2;

	}

	private void initUI() {
		blackBackgrond = new Sprite("Black.jpg", 0, canvas.getHeight() - 170, canvas.getWidth(), 170);
		blackBackgrond.render(canvas.getGraphicsContext2D());
		sidePanel = new Sprite("Black.jpg", canvas.getWidth() - 500, 0, 500, canvas.getHeight());
		sidePanel.render(canvas.getGraphicsContext2D());

		moveIndicator = new TextField();

		moveIndicator.setLayoutX(650);
		moveIndicator.setLayoutY(570);
		moveIndicator.setEditable(false);

		moveIndicator.setStyle("" + "-fx-font-size: 40px;" + "-fx-font-style: italic;" + "-fx-font-weight: bold;"
				+ "-fx-font-family: fantasy;" + "-fx-text-fill: black;" + "-fx-background-color: white");

		monster1Drawing = new Sprite("Blue.jpg", canvas.getWidth() - 450, 50, 400, canvas.getHeight() - 220);
		monster2Drawing = new Sprite("Red.jpg", canvas.getWidth() - 450, 50, 400, canvas.getHeight() - 220);

		// DELETE THIS
		deleteMe = new Button("Test");
		deleteMe.setOnAction(e -> {
			highlightHex(hexes[2][0], "blue");
			highlightHex(hexes[1][0], "red");

		});

		move = new Button("MOVE");
		move.setLayoutX(50);
		move.setLayoutY(570);
		move.setPrefSize(150, 90);
		move.setStyle("-fx-font: 32 cursive; -fx-base: #0000FF;");

		move.setOnAction(e -> {
			bashMode = false;
			moveMode = true;
		});

		move.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (!bashMode) {
					for (int i = 0; i < hexes.length; i++) {
						for (int j = 0; j < hexes[0].length; j++) {
							if (hexes[i][j] != null && isAjecent(currentMonster.currentHex, hexes[i][j])
									&& hexes[i][j] != monster1.currentHex && hexes[i][j] != monster2.currentHex) {
								highlightHex(hexes[i][j], "blue");
							}
						}
					}
				}
			}

		});

		move.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (!moveMode && !bashMode) {
					clearHighlights();
				}

			}

		});

		bash = new Button("BASH");
		bash.setLayoutX(250);
		bash.setLayoutY(570);
		bash.setPrefSize(150, 90);
		bash.setStyle("-fx-font: 32 cursive; -fx-base: #FF0000;");

		bash.setOnAction(e -> {
			moveMode = false;
			bashMode = true;
		});

		bash.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (!moveMode) {
					for (int i = 0; i < hexes.length; i++) {
						for (int j = 0; j < hexes[0].length; j++) {
							if (hexes[i][j] != null && isAjecent(currentMonster.currentHex, hexes[i][j])
									&& hexes[i][j] != monster1.currentHex && hexes[i][j] != monster2.currentHex) {
								highlightHex(hexes[i][j], "red");
							}
						}
					}
				}
			}

		});

		bash.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (!bashMode & !moveMode) {
					clearHighlights();
				}

			}

		});
		punch = new Button("PUNCH");
		punch.setLayoutX(450);
		punch.setLayoutY(570);
		punch.setPrefSize(150, 90);
		punch.setStyle("-fx-font: 28 cursive; -fx-base: #FFA500;");

		punch.setOnAction(e -> {
			if (currentMonster.getTurnPoints() >= 2) {
				punch();
			}

		});

		root.getChildren().addAll(move, bash, punch, moveIndicator);

	}

	private void punch() {
		// Oh, malele, taz mandja s grozde cqlo selo she hrani
		if (currentMonster == monster1) {
			altMonster = monster2;
		} else {
			altMonster = monster1;
		}

		// Horrible use of variables
		// At some point the distance between 2 hexes was set as a variable
		// Now I have 2 of those variables:
		// 1 is for distance between the center
		// and the other is for distance between the draw point
		double horizontalDistanceBetweenAjecentHexAlt = hexes[1][0].getX() - hexes[0][0].getX();
		double hex1X = 0, hex1Y = 0, hex2X = 0, hex2Y = 0, hex3X = 0, hex3Y = 0;

		// basicly the same jumbled mess of code that is
		// the formula for finding what hexes lie in a line
		// the same one can be seen in the bash method
		// except this has the final part cut out, since you only need 2

		if (altMonster.currentHex.getX() > currentMonster.currentHex.getX()) {
			hex1X = altMonster.currentHex.getX();
			hex2X = hex1X + horizontalDistanceBetweenAjecentHexAlt;
		} else if (altMonster.currentHex.getX() < currentMonster.currentHex.getX()) {
			hex1X = altMonster.currentHex.getX();
			hex2X = hex1X - horizontalDistanceBetweenAjecentHexAlt;
		} else if (altMonster.currentHex.getX() == currentMonster.currentHex.getX()) {
			hex1X = altMonster.currentHex.getX();
			hex2X = altMonster.currentHex.getX();
		}

		if (altMonster.currentHex.getX() == currentMonster.currentHex.getX()) {
			if (altMonster.currentHex.getY() > currentMonster.currentHex.getY()) {
				hex1Y = altMonster.currentHex.getY();
				hex2Y = hex1Y + verticalDistanceBetweenAjecentHex;
			} else if (altMonster.currentHex.getY() < currentMonster.currentHex.getY()) {
				hex1Y = altMonster.currentHex.getY();
				hex2Y = hex1Y - verticalDistanceBetweenAjecentHex;
			}
		} else if (altMonster.currentHex.getX() != currentMonster.currentHex.getX()) {
			if (altMonster.currentHex.getY() > currentMonster.currentHex.getY()) {
				hex1Y = altMonster.currentHex.getY();
				hex2Y = hex1Y + verticalDistanceBetweenAjecentHexOnDifrentColumns;
			} else if (altMonster.currentHex.getY() < currentMonster.currentHex.getY()) {
				hex1Y = altMonster.currentHex.getY();
				hex2Y = hex1Y - verticalDistanceBetweenAjecentHexOnDifrentColumns;
			}
		}

		// finding the corresponding hex involves:
		// writting down the coordinates from the formula above
		// then going through the entire field and seeing if they match
		for (int i = 0; i < hexes.length; i++) {
			for (int j = 0; j < hexes[0].length; j++) {
				if (hexes[i][j] != null) {
					if (hexes[i][j].getX() == hex2X && hexes[i][j].getY() == hex2Y) {
						hexes[i][j].decrementHp();
						if (hexes[i][j].getHp() <= 0) {
							setDoUpdate(false);
						}
						altMonster.move(hexes[i][j]);
						currentMonster.decremetnTurnPoints();
						currentMonster.decremetnTurnPoints();
						return;
					}
				}
			}
			if (i == hexes.length - 1) {
				// hasn't found a corresponding hex => doesn't exist => it's
				// out of the bounds
				setDoUpdate(false);
			}
		}
	}

	@Override
	protected void updateGame(double deltaTime) {

		moveIndicator.setText("Moves left: " + currentMonster.getTurnPoints());

		if (currentMonster.getBashCounter() > 0) {
			bash.setVisible(false);
		} else {
			bash.setVisible(true);
		}

		for (int i = 0; i < hexes.length; i++) {
			for (int j = 0; j < hexes[0].length; j++) {
				if (hexes[i][j] != null) {
					if (hexes[i][j].getHp() == 0) {
						root.getChildren().remove(hexes[i][j].hpLabel);
						hexes[i][j] = null;
					}
				}
			}
		}
		if (monster2.currentHex.getHp() == 0) {
			setDoUpdate(false);
		}
		if (monster1.currentHex.getHp() == 0) {
			setDoUpdate(false);
		}

		if (currentMonster.getTurnPoints() == 0) {
			nextTurn();
		}

		if (isPlayer1Move) {
			currentPlayer = "Player1";
		} else {
			currentPlayer = "Player2";
		}
		playerIndicator.setText(currentPlayer);

		if (isAjecent(monster1.currentHex, monster2.currentHex) && currentMonster.getTurnPoints() >= 2) {
			punch.setVisible(true);
		} else {
			punch.setVisible(false);
		}

	}

	@Override
	protected void drawGame() {
		if (!isGameWon) {

			GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
			graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

			background.render(graphicsContext);
			drawBoard();
			sidePanel.render(graphicsContext);

			if (isPlayer1Move) {
				monster2Drawing.render(graphicsContext);
				monster1Drawing.render(graphicsContext);
			} else {
				monster1Drawing.render(graphicsContext);
				monster2Drawing.render(graphicsContext);
			}
			blackBackgrond.render(graphicsContext);
			drawMonsters();

			for (int i = 0; i < highlights.size(); i++) {
				highlights.get(i).render(canvas.getGraphicsContext2D());
			}
		} else {
			if (monster1 == currentMonster) {
				currentPlayer = "Player1";
			} else {
				currentPlayer = "Player2";
			}
			playerIndicator.setText(currentPlayer + " won!");

		}
	}

	private void drawMonsters() {
		monster1.render(canvas.getGraphicsContext2D());
		monster2.render(canvas.getGraphicsContext2D());
	}

	@Override
	protected void onMousePressed(MouseEvent event) {
	}

	@Override
	protected void onMouseReleased(MouseEvent event) {
		// the source of all movement and bashing.
		// a simple boolean is used to find which is called
		Point2D p = new Point2D(event.getX(), event.getY());
		for (int i = 0; i < hexes.length; i++) {
			for (int j = 0; j < hexes[0].length; j++) {
				if (hexes[i][j] != null) {
					if (hexes[i][j].getBoundery().contains(p) && isAjecent(currentMonster.currentHex, hexes[i][j])
							&& hexes[i][j] != monster1.currentHex && hexes[i][j] != monster2.currentHex) {
						if (moveMode) {
							currentMonster.move(hexes[i][j]);
							clearModes();
							clearHighlights();
							currentMonster.decremetnTurnPoints();
						} else if (bashMode) {
							if (currentMonster.getBashCounter() == 0) {
								bash(hexes[i][j]);
								clearModes();
								clearHighlights();
								currentMonster.decremetnTurnPoints();
								currentMonster.incrementBashCounter();
							}
						}

					}
				}
			}

		}
	}

	private void initBoard() {

		double rowIndent = 1.5 * Hex.HEX_SIDE;
		hexes = new Hex[ROWS][COLUMNS];

		Random rn = new Random();

		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				double yCoordinate;
				double xCoordinate;
				if (i == ROWS - 1) {
					// skipping the last row
					continue;
				}
				if (i % 2 == 1) {
					// indenting every second row
					xCoordinate = INDENT_X + j * (Hex.HEX_PIXEL_WIDTH + Hex.HEX_SIDE) + rowIndent;
					if (j == COLUMNS - 1) {
						// skipping the last block of every second row
						continue;
					}
				} else {
					xCoordinate = INDENT_X + j * (Hex.HEX_PIXEL_WIDTH + Hex.HEX_SIDE);
				}
				yCoordinate = INDENT_Y + i * (Hex.HEX_PIXEL_HEIGHT / 2);
				Hex hex = new Hex("Hex.png", xCoordinate, yCoordinate, Hex.HEX_PIXEL_WIDTH, Hex.HEX_PIXEL_HEIGHT,
						rn.nextInt(7) + 3);
				hexes[i][j] = hex;
				root.getChildren().add(hexes[i][j].hpLabel);

			}
		}

	}

	private void drawBoard() {
		for (int i = 0; i < hexes.length; i++) {
			for (int j = 0; j < hexes[0].length; j++) {
				if (hexes[i][j] != null) {
					hexes[i][j].render(canvas.getGraphicsContext2D());
				}
			}
		}

	}

	private boolean isAjecent(Hex center, Hex target) {
		// the way to find if hexes are ajecent is to
		// measure the onscreen distance they have
		// The original idea was to work with an array
		// and do arethimetics based on it's indexes
		// but this seems to be a more reliable
		// if less ellegant solution
		Point2D centerXPoint = center.getCenter();
		Point2D centerYPoint = target.getCenter();

		double distance = centerXPoint.distance(centerYPoint);

		// plus 2 jsut in case.

		if (distance <= horizontalDistanceBetweenAjecentHex) {
			return true;
		}
		if (distance == 0) {
			return false;
			// not ajecent, the same
			// prevents making an accidental emty move

		}

		return false;

	}

	private void nextTurn() {
		currentMonster.resetTurnPoints();
		currentMonster.resetBashCounter();
		if (isPlayer1Move) {
			currentMonster = monster2;
		} else {
			currentMonster = monster1;
		}

		isPlayer1Move = !isPlayer1Move;

	}

	private void highlightHex(Hex hex, String color) {
		try {
			if (hex != null) {
				if (color.equals("blue")) {
					highlights.add(new Sprite("HexHighlight.png", hex.getX(), hex.getY(), hex.HEX_PIXEL_WIDTH,
							hex.HEX_PIXEL_HEIGHT));
				} else if (color.equals("red")) {
					highlights.add(new Sprite("HexHighlightRed.png", hex.getX(), hex.getY(), hex.HEX_PIXEL_WIDTH,
							hex.HEX_PIXEL_HEIGHT));

				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}

	}

	private void clearHighlights() {
		highlights.clear();
	}

	public void clearModes() {
		moveMode = false;
		bashMode = false;
	}

	public void bash(Hex target) {
		/*
		 * I know it looks bad but all this formula does is check the pattern
		 * inputed by the user if the monster is standing on a hex with
		 * coordinates X, Y and then clickes on something that has coordinates
		 * X+1, Y -1 (or something) we can figure out where the next hex along
		 * that line will be with coordinates
		 */

		// this variable needs to be reworked and joined with the ohter one
		double horizontalDistanceBetweenAjecentHexAlt = hexes[1][0].getX() - hexes[0][0].getX();
		double hex1X = 0, hex1Y = 0, hex2X = 0, hex2Y = 0, hex3X = 0, hex3Y = 0;

		if (target.getX() > currentMonster.currentHex.getX()) {
			hex1X = target.getX();
			hex2X = hex1X + horizontalDistanceBetweenAjecentHexAlt;
			hex3X = hex2X + horizontalDistanceBetweenAjecentHexAlt;
		} else if (target.getX() < currentMonster.currentHex.getX()) {
			hex1X = target.getX();
			hex2X = hex1X - horizontalDistanceBetweenAjecentHexAlt;
			hex3X = hex2X - horizontalDistanceBetweenAjecentHexAlt;
		} else if (target.getX() == currentMonster.currentHex.getX()) {
			hex1X = target.getX();
			hex2X = target.getX();
			hex3X = target.getX();
		}

		if (target.getX() == currentMonster.currentHex.getX()) {
			if (target.getY() > currentMonster.currentHex.getY()) {
				hex1Y = target.getY();
				hex2Y = hex1Y + verticalDistanceBetweenAjecentHex;
				hex3Y = hex2Y + verticalDistanceBetweenAjecentHex;
			} else if (target.getY() < currentMonster.currentHex.getY()) {
				hex1Y = target.getY();
				hex2Y = hex1Y - verticalDistanceBetweenAjecentHex;
				hex3Y = hex2Y - verticalDistanceBetweenAjecentHex;
			}
		} else if (target.getX() != currentMonster.currentHex.getX()) {
			if (target.getY() > currentMonster.currentHex.getY()) {
				hex1Y = target.getY();
				hex2Y = hex1Y + verticalDistanceBetweenAjecentHexOnDifrentColumns;
				hex3Y = hex2Y + verticalDistanceBetweenAjecentHexOnDifrentColumns;
			} else if (target.getY() < currentMonster.currentHex.getY()) {
				hex1Y = target.getY();
				hex2Y = hex1Y - verticalDistanceBetweenAjecentHexOnDifrentColumns;
				hex3Y = hex2Y - verticalDistanceBetweenAjecentHexOnDifrentColumns;
			}
		}

		for (int i = 0; i < hexes.length; i++) {
			for (int j = 0; j < hexes[0].length; j++) {
				if (hexes[i][j] != null) {
					if ((hexes[i][j].getX() == hex1X && hexes[i][j].getY() == hex1Y)
							|| (hexes[i][j].getX() == hex2X && hexes[i][j].getY() == hex2Y)
							|| (hexes[i][j].getX() == hex3X && hexes[i][j].getY() == hex3Y)) {
						hexes[i][j].decrementHp();
					}
				}
			}
		}

	}
}