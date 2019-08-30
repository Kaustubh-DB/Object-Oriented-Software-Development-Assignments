package application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.animation.Timeline;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.animation.KeyFrame;
import javafx.util.Duration;

public class Main extends Application {

	private enum UserAction {
		NONE, LEFT, RIGHT;
	}

	private static final int APP_W = 800;
	private static final int APP_H = 600;

	private static final int BALL_RADIUS = 10;
	private static final int BAT_W = 180;
	private static final int BAT_H = 20;

	private static final int BRICK_W = 50;
	private static final int BRICK_H = 10;

	private Circle ball = new Circle(BALL_RADIUS);
	private Rectangle bat = new Rectangle(BAT_W, BAT_H);
	private Rectangle brick = new Rectangle(BRICK_W, BRICK_H);

	private boolean ballUp = true, ballLeft = false;
	private UserAction action = UserAction.NONE;
	private Timeline timeline = new Timeline();
	private Timeline timeline2 = new Timeline();

	private boolean running = true;

	Label timerLabel = new Label();

	private Parent createContent() {
		Pane root = new Pane();
		root.setPrefSize(APP_W, APP_H);

		bat.setTranslateX(APP_W / 2);
		bat.setTranslateY(APP_H - 250);
		bat.setFill(Color.BLUE);

		brick.setTranslateX(APP_W - 300);
		brick.setTranslateY(APP_H - 100);
		brick.setFill(Color.DARKRED);

		DateTimeFormatter SHORT_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

		timerLabel.setText(LocalTime.now().format(SHORT_TIME_FORMATTER));
		timerLabel.setTranslateX(APP_W - 70);
		timerLabel.setTranslateY(APP_H - 600);
/*		long endTime = 300;
		Label timeLabel = new Label();*/

		root.getChildren().addAll(ball, bat, brick, timerLabel);

		KeyFrame frame2 = new KeyFrame(Duration.seconds(1), e -> {

			timerLabel.setText(LocalTime.now().format(SHORT_TIME_FORMATTER));

//			timerLabel.setText(counter.toString());
		});

		/*
		 * DateFormat timeFormat = new SimpleDateFormat( "HH:mm:ss" ); final Timeline
		 * timeline3 = new Timeline( new KeyFrame( Duration.millis( 1 ), event -> {
		 * final long diff = endTime - System.currentTimeMillis(); if ( diff < 0 ) { //
		 * timeLabel.setText( "00:00:00" ); timeLabel.setText( timeFormat.format( 0 ) );
		 * } else { timeLabel.setText( timeFormat.format( diff ) ); } } ) );
		 * timeline3.setCycleCount( Animation.INDEFINITE ); timeline3.play();
		 */

		KeyFrame frame = new KeyFrame(Duration.seconds(0.016), event -> {
			if (!running)
				return;

			switch (action) {
			case LEFT:
				if (bat.getTranslateX() - 5 >= 0) {
					bat.setTranslateX(bat.getTranslateX() - 5);
					break;
				}
			case RIGHT:
				if (bat.getTranslateX() + BAT_W + 5 <= APP_W) {
					bat.setTranslateX(bat.getTranslateX() + 5);
					break;
				}
			case NONE:
				break;
			}

			ball.setTranslateX(ball.getTranslateX() + (ballLeft ? -5 : 5));
			ball.setTranslateY(ball.getTranslateY() + (ballUp ? -5 : 5));

			if (ball.getTranslateX() - BALL_RADIUS == 0)
				ballLeft = false;
			else if (ball.getTranslateX() + BALL_RADIUS == APP_W)
				ballLeft = true;

			if (ball.getTranslateY() - BALL_RADIUS == 0)
				ballUp = false;
			/*
			 * else if(ball.getTranslateY() + BALL_RADIUS== APP_H - BAT_H &&
			 * ball.getTranslateX() + BALL_RADIUS >= bat.getTranslateX() + BAT_W) ballUp=
			 * true;
			 */

			/*
			 * if(ball.getTranslateX() + BALL_RADIUS == brick.getTranslateX() ||
			 * ball.getTranslateX() - BALL_RADIUS == brick.getTranslateX() + BRICK_W &&
			 * ball.getTranslateY() - BALL_RADIUS == brick.getTranslateY() + BRICK_H ||
			 * group logic ball.getTranslateY() + BALL_RADIUS == brick.getTranslateY()) {
			 * try { //root.getChildren().remove(brick);
			 * 
			 * Thread.sleep(0); } catch (InterruptedException e) { // TODO Auto-generated
			 * catch block e.printStackTrace(); } }
			 */

			if (ball.getTranslateY() + BALL_RADIUS == APP_H - 100
					&& ball.getTranslateX() + BALL_RADIUS <= brick.getTranslateX() + BRICK_W
					&& ball.getTranslateX() - BALL_RADIUS >= brick.getTranslateX()) {
				root.getChildren().remove(brick);
				stopGame();
			}

			if (ball.getTranslateY() - BALL_RADIUS == APP_H - 90
					&& ball.getTranslateX() + BALL_RADIUS <= brick.getTranslateX() + BRICK_W
					&& ball.getTranslateX() - BALL_RADIUS >= brick.getTranslateX()) {
				root.getChildren().remove(brick);
				stopGame();
			}

			if (ball.getTranslateY() + BALL_RADIUS == APP_H - 250
					&& ball.getTranslateX() + BALL_RADIUS <= bat.getTranslateX() + BAT_W
					&& ball.getTranslateX() - BALL_RADIUS >= bat.getTranslateX()) {
				ballUp = true;
			}

			if (ball.getTranslateY() + BALL_RADIUS == APP_H)
				// restartGame();
				ballUp = true;

			if (ball.getTranslateY() - BALL_RADIUS == APP_H - 240
					&& ball.getTranslateX() + BALL_RADIUS <= bat.getTranslateX() + BAT_W
					&& ball.getTranslateX() - BALL_RADIUS >= bat.getTranslateX())
				// restartGame();
				ballUp = false;

		});

		timeline.getKeyFrames().add(frame);
		timeline2.getKeyFrames().add(frame2);

		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline2.setCycleCount(Timeline.INDEFINITE);
		return root;

	}

	private void restartGame() {
		stopGame();
		startGame();
	}

	private void stopGame() {
		// TODO Auto-generated method stub
		
		Stage dialogStage = new Stage();
		dialogStage.initModality(Modality.WINDOW_MODAL);
		VBox vbox = new VBox(new Text("GAME OVER"));
		vbox.setAlignment(Pos.CENTER);
		//vbox.setPadding(new Insets(50));
		dialogStage.setScene(new Scene(vbox));
		dialogStage.show();
		
		running = false;
		timeline.stop();
		timeline2.stop();
	}

	private void startGame() {
		// TODO Auto-generated method stub
		ballUp = true;
		ball.setTranslateX(APP_W / 2);
		ball.setTranslateY(APP_H / 2);

		timeline.play();
		timeline2.play();
		running = true;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Scene scene = new Scene(createContent());
		scene.setOnKeyPressed(event -> {
			switch (event.getCode()) {
			case A:
				action = UserAction.LEFT;
				break;
			case D:
				action = UserAction.RIGHT;
				break;
			default:
				break;
			}
		});

		primaryStage.setTitle("GAME OF BRICKS");
		primaryStage.setScene(scene);
		primaryStage.show();
		startGame();
	}

	public static void main(String[] args) {
		launch(args);
	}
}