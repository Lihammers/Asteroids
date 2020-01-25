package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.*;

public class Asteroid {

	private double xPos, yPos;
	private double width, height, speed;
	private Image[] imgAsteroid = new Image[4];
	private ImageView iViewAsteroid;
	private Random rand;
	private boolean isDestroyed;
	private int value, health, seconds;
	private Timeline timer;
	private boolean isDead; 
	private Main main = new Main();

	/**
	 * Creates a asteroid that can kill the player or be desroyed by a laser
	 */
	public Asteroid(){	
		//Declare variables
		xPos = 0;
		yPos = 0;
		speed = 0;

		isDead = false;
		
		for (int i = 0; i < imgAsteroid.length;i++){
			if(i < 3) {
			imgAsteroid[i] = new Image("file:images\\asteroid" + i + ".png");
			}
			else
				imgAsteroid[i] = new Image("file:images\\explosion.png");
		}

		rand = new Random();
		int rndInt = rand.nextInt(3);

		width = imgAsteroid[rndInt].getWidth();
		height = imgAsteroid[rndInt].getHeight();

		iViewAsteroid = new ImageView(imgAsteroid[rndInt]);	

		isDestroyed = false;

		//Set the health
		if(rndInt == 0){
			value = 1;
			health = 0;
		}
		else if(rndInt == 1){
			value = 2;
			health = 1;
		}
		else{
			value = 3;
			health = 2;
		}

	} 

	/**
	 * Sets the location of the asteroid
	 * @param x - x-position of the asteroid
	 * @param y - y-position of the asteroid
	 */
	public void setLocation(double x, double y) {
		xPos = x;
		yPos = y;

		iViewAsteroid.setX(xPos);
		iViewAsteroid.setY(yPos);
	}

	/**
	 * Makes the asteroid move
	 */
	public void move() {
		xPos -= speed;
		iViewAsteroid.setX(xPos);
	}
	
	/**
	 * Sets the speed of the asteroid
	 * @param speed - The speed in which the asteroid moves
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	/**
	 * Gets the y-position of the asteroid
	 * @return Returns the y-position
	 */
	public double getY(){

		return yPos;
	}

	/**
	 * Gets the x-position of the asteroid
	 * @return Returns the x-position
	 */
	public double getX(){

		return xPos;
	}

	/**
	 * Gets the height of the asteroid
	 * @return Returns the height of the asteroid image
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Gets the width of the asteroid
	 * @return Returns the width of the asteroid image
	 */
	public double getWidth() {
		return width;
	}
	
	/**
	 * Gets the image view object of the asteroid
	 * @return Returns the image view object
	 */
	public ImageView getNode(){

		return iViewAsteroid;
	}
	
	/**
	 * Checks the value of the asteroid to determine the size and health
	 * @return Returns the integer value
	 */
	public int getValue(){
		return value;
	}
	
	/**
	 * Checks the health of the asteroid depending on the value
	 * @return Returns the integer health
	 */
	public int getHealth(){
		return health;
	}
	
	/**
	 * Sets the health of the asteroid
	 * @param die - When a asteroid dies
	 */
	public void setHealth(int die){
		health = die;
	}
	
	/**
	 * Gets the image of a asteroid
	 * @return Returns the current image of the asteroid
	 */
	public Image getImage() {
		return iViewAsteroid.getImage();
	}
	
}