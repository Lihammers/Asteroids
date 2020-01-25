package application;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import javax.swing.JOptionPane;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Main extends Application {

	//Imports
	private Timeline backgroundTimer;
	private Label lblScore;
	private int score, lives, randX, randY, seconds, option;
	private Image imgBackground;
	private ImageView ivLives;
	private Image[] imgLives;
	private ImageView[] ivBackground;
	private Ship ship = new Ship();
	private Laser laser = new Laser();
	private Asteroid[] asteroid = new Asteroid[10];
	private Random rand = new Random();
	private Timeline laserTimer, timer[] = new Timeline[10];
	private boolean isFired = false;
	private Explosion[] exp = new Explosion[10];
	private ArrayList<Laser> newLaser = new ArrayList<>();

	public void start(Stage primaryStage) {

		try {

			//Method for moving the background
			initBackground();

			//Label for the title
			Label lblTitle = new Label("ASTEROIDS");
			lblTitle.setPrefSize(400, 50);
			lblTitle.setFont(Font.loadFont(new FileInputStream(new File("neuropol.ttf")), 48));
			lblTitle.setAlignment(Pos.CENTER);
			lblTitle.setLayoutX(imgBackground.getWidth() / 2 - lblTitle.getPrefWidth() / 2);
			lblTitle.setLayoutY(20);
			lblTitle.setTextFill(Color.LIGHTGRAY);

			//Label for the score
			lblScore = new Label();
			lblScore.setText(Integer.toString(score));
			lblScore.setPrefSize(200, 50);
			lblScore.setFont(Font.loadFont(new FileInputStream(new File("neuropol.ttf")), 48));
			lblScore.setAlignment(Pos.CENTER);
			lblScore.setTextFill(Color.LIGHTGRAY);
			lblScore.setLayoutX(0);
			lblScore.setLayoutY(20);

			//ImageView object for displaying lives
			ivLives = new ImageView();
			ivLives.setImage(imgLives[lives]);
			ivLives.setLayoutX(imgBackground.getWidth() - imgLives[lives].getWidth() - 50);
			ivLives.setLayoutY(40);

			//Pane root to add all the controls in
			Pane root = new Pane();
			root.getChildren().addAll(ivBackground);
			root.getChildren().addAll(lblTitle, lblScore, ivLives);

			//Set the size of the scene
			Scene scene = new Scene(root, imgBackground.getWidth(), imgBackground.getHeight());

			//Mouse event for when the mouse moves
			scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent e) {
					//Set the location of the ship to wherever the cursor moves
					ship.setLocation(e.getX() - ship.getWidth()/2, e.getY() - ship.getHeight()/2);
				}
			});
			
			laser.getNode().setImage(new Image("file:.png"));
			
			//Key event for when the space bar is pressed			
			scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent e) {
					//Check if the space bar is pressed and the bullet is not fired
					if (e.getCode() == KeyCode.SPACE && !isFired) {
						//Create a new laser object and play the bullet timer to make the bullet move
						newLaser.add(laser);
						laser.getNode().setImage(new Image("file:images\\laserbeam_red.png"));
						laser.setLocation(ship.getX(), ship.getY());
						laserTimer.play();
						isFired = true;
					}
				}
			});

			//KeyFrame for the laser to move
			KeyFrame kfLaser = new KeyFrame(Duration.millis(25),
					new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					//Move the laser and set the speed inside the parameter
					laser.move(30);

					//Check if the laser goes beyond the screen and remove the laser object
					if (laser.getX() > scene.getWidth() + laser.getWidth()) {
						laserTimer.stop();
						newLaser.remove(laser);
						isFired = false;
					}
				}
			});

			//Create a time line for the key frame of the laser
			laserTimer = new Timeline(kfLaser);
			laserTimer.setCycleCount(Timeline.INDEFINITE);

			//For loop to initialize the asteroid objects
			for (int i = 0;i < asteroid.length;i++) {
				asteroid[i] = new Asteroid();
				exp[i] = new Explosion();
				
				//Call in the random method
				random(i);

				//Set the speed and the location of the asteroid and add it to the pane
				asteroid[i].setLocation(randX,randY);
				asteroid[i].setSpeed(10);
				root.getChildren().addAll(asteroid[i].getNode(), exp[i].getNode());
			}

			//Method for checking if a asteroid is overlapping another asteroid
			asteroidCollision();

			//Key frame for the asteroid
			KeyFrame kfAsteroid = new KeyFrame(Duration.millis(50),
					new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					for (int i = 0;i < asteroid.length;i++) {
						//Make the asteroid move
						asteroid[i].move();

						//Check if the asteroid passes the screen
						if (asteroid[i].getX() < 0 - asteroid[i].getWidth()) {
							//Get a random number from the random method
							//Set it to random location outside of the screen
							//Then check if its colliding with another asteroid
							random(i);
							asteroid[i].setLocation(randX, randY);
							asteroidCollision();
						}
					}
				}
			});

			//Key frame for when the ship collides with a asteroid
			KeyFrame kfShipCollision = new KeyFrame(Duration.millis(50),
					new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					
					//For-loop to check which asteroid the ship collides with
					for (int i = 0;i < asteroid.length;i++) {
						//Check if the ship collides with a asteroid
						if (ship.getNode().getBoundsInParent().intersects(asteroid[i].getNode().getBoundsInParent()) 
								&& !asteroid[i].getDeath()) {
							//show destroyed ship
							ship.getNode().setImage(new Image("file:images/exploded_ship.png"));
							
							//Deduct your lives
							lives--;
					
							for (int j = 0;j < asteroid.length;j++) {
								asteroid[j].setSpeed(0);
							}
							//Check if the player has lives larger than or equal to 0
							if (lives >= 0) {
								
								Thread t = new Thread(new Runnable(){
								public void run(){
									//Show a message the player showing how much lives they have
									JOptionPane.showMessageDialog(null, "You lose a life, you have " + (lives + 1) + " left");
									//reset ship to fixed
									ship.getNode().setImage(new Image("file:images/spaceship.png"));
									for (int j = 0;j < asteroid.length;j++) {
										asteroid[j].setSpeed(10);
									}
								}
							});
						t.start();
							
								
								//Deduct the lives on the screen
								ivLives.setImage(imgLives[lives]);
								//Use a for loop to reset all of the asteroids location
								for (int a = 0;a < asteroid.length;a++) {
									random(a);
									//timer[i].stop();
									exp[a].getNode().setImage(new Image("file:.png"));
									asteroid[a].setLocation(randX, randY);
									asteroidCollision();
								}
							}
							//Check if the player has no more lives yet, and tell them the game is over
							else if (lives < 0) {
//								Thread thread = new Thread(new Runnable() {
//									public void run() {
										option = JOptionPane.showConfirmDialog(null, "Game over\nWould you like to play again?",
												"GAMEOVER", JOptionPane.YES_NO_OPTION);
//									}
//								});
								//thread.start();
										
									if (option == JOptionPane.YES_OPTION) {
										score = 0;
										lblScore.setText(Integer.toString(score));
										lives = 2;
										ivLives.setImage(imgLives[lives]);
											
										for (int j = 0;j < asteroid.length;j++) {
											exp[i].getNode().setImage(new Image("file:.png"));
											random(j);
											asteroid[j].setLocation(randX, randY);
											asteroidCollision();
										}
											
										ship.getNode().setImage(new Image("file:images\\spaceship.png"));
									}
									else {
										System.exit(0);
									}
							}
						}
					}
				} 
			});

			//Key frame for the laser collision
			KeyFrame kfLaserCollision = new KeyFrame(Duration.millis(50),
					new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					//Loop to check each asteroid
					for (int i = 0;i < asteroid.length;i++) {
						//Check if the laser collides with the asteroid
						if (laser.getNode().getBoundsInParent().intersects(asteroid[i].getNode().getBoundsInParent())) {
							laserTimer.stop();
							newLaser.remove(laser);
							laser.getNode().setImage(new Image("file:.png"));
							laser.setLocation(ship.getX(), ship.getY());
							isFired = false;
							
							if(asteroid[i].getHealth() > 0){
								int loss = asteroid[i].getHealth() - 1;
								asteroid[i].setHealth(loss);
								
								
							}
							else if (asteroid[i].getHealth() == 0){
								explosion(i);
								//asteroid[i].isDead(true);
								asteroid[i].setHealth(asteroid[i].getValue() - 1);
								

								//Add score up, remove the laser object, and stop the laser timer
								if(asteroid[i].getValue() == 1){
									score += 10;
								}
								else if(asteroid[i].getValue() == 2){
									score += 20;
								}
								else{
								score+=30;
								}
								lblScore.setText("" + score);
								laser.getNode().setImage(new Image("file:.png"));
								newLaser.remove(laser);
								isFired = false;
								laserTimer.stop();
							}
							
							
						}
						
					}
				}
			});

			//Time line for the collision between the laser and asteroid
			Timeline collisionTimer = new Timeline(kfLaserCollision);
			collisionTimer.setCycleCount(Timeline.INDEFINITE);

			//Time line for when the ship collides with asteroid
			Timeline deathTimer = new Timeline(kfShipCollision);
			deathTimer.setCycleCount(Timeline.INDEFINITE);

			//Time line for moving the asteroid
			Timeline asteroidTime = new Timeline(kfAsteroid);
			asteroidTime.setCycleCount(Timeline.INDEFINITE);

			//Add all the objects to the pane
			root.getChildren().addAll(ship.getNode(), laser.getNode());
			primaryStage.setTitle("Asteroids");
			primaryStage.setScene(scene);
			primaryStage.show();

			//Play all timers
			backgroundTimer.play();
			asteroidTime.play();
			collisionTimer.play();
			deathTimer.play();
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	//Method for moving the background
	public void initBackground() {

		imgBackground = new Image("file:images\\space_background.png");

		ivBackground = new ImageView[2];
		double[] xPos = new double[] {0, (int)imgBackground.getWidth()};

		imgLives = new Image[3];

		ivLives = new ImageView();
		lives = 2;

		for (int i = 0; i < ivBackground.length; i++)
		{
			ivBackground[i] = new ImageView(imgBackground);
		}

		for (int i = 0; i < imgLives.length; i++)
		{
			imgLives[i] = new Image("file:images\\lives" + (i + 1) + ".png");
		}

		KeyFrame kfBackground = new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				for (int i = 0; i < ivBackground.length; i++)
				{
					xPos[i] -= 5;
				}

				if (xPos[0] + imgBackground.getWidth() <= 0)
				{
					xPos[0] = xPos[1] + imgBackground.getWidth();
				}

				if (xPos[1] + imgBackground.getWidth() <= 0)
				{
					xPos[1] = xPos[0] + imgBackground.getWidth();
				}

				for (int i = 0; i < ivBackground.length; i++)
				{
					ivBackground[i].setLayoutX(xPos[i]);
					ivBackground[i].setLayoutY(0);
				}
			}
		});

		backgroundTimer = new Timeline(kfBackground);
		backgroundTimer.setCycleCount(Timeline.INDEFINITE);

	}

	//Method for checking the overlapping between asteroids
	public void asteroidCollision(){
		//Double for-loop to linear search a asteroid collision
		for (int i = 0;i < asteroid.length;i++) {
			for (int j = 0;j < asteroid.length;j++) {
				//Check for collision/overlapping
				if (asteroid[i].getNode().getBoundsInParent().intersects(asteroid[j].getNode().getBoundsInParent()) && i != j
						&& !asteroid[i].getDeath()) {
					//Get random numbers, set to a another location, and restart the method until there are no overlaps
					random(i);					
					asteroid[i].setLocation(randX, randY);
					asteroidCollision();
				}
			}
		}
	}

	//Method for getting a random integer for the x and y. X values are outside the screen
	public void random(int i) {
		randX = rand.nextInt((int)imgBackground.getWidth()) + (int)imgBackground.getWidth();
		randY = rand.nextInt((int)imgBackground.getHeight() - (int)asteroid[i].getHeight());
	}

	public void explosion(int i) {
		exp[i].setLocation(asteroid[i].getX(), asteroid[i].getY());
		exp[i].getNode().setImage(new Image("file:images\\explosion.png"));
		
		random(i);
		asteroid[i].setLocation(randX,randY);
		asteroidCollision();
		
		seconds = 1;
		KeyFrame[] kfDead = new KeyFrame[10];
		kfDead[i] = new KeyFrame(Duration.millis(1000), new
				EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				seconds--; 
				if (seconds == 0)
				{
					exp[i].getNode().setImage(new Image("file:.png"));
					timer[i].stop();
				}
			}
		});
		
		timer[i] = new Timeline(kfDead[i]);
		timer[i].setCycleCount(Timeline.INDEFINITE);
		timer[i].play();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
