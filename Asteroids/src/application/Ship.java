package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Ship {

	//Declare global variables
	private double xPos, yPos, width, height;
	private Image imgShip;
	private boolean isDead;
	private ImageView iViewShip;
	
	/**
	 * Creates a ship object which the player can move using the mouse
	 */
	public Ship(){
		
		xPos = 0;
		yPos = 0;
		
		imgShip = new Image("file:images\\spaceship.png");
		
		width = imgShip.getWidth();
		height = imgShip.getHeight();
		
		iViewShip = new ImageView(imgShip);
	}
	
	/**
	 * Sets the location of the ship
	 * @param x - Sets the x-position of the ship
	 * @param y - Sets the y-position of the ship
	 */
	public void setLocation(double x,double y) {
		xPos = x;
		yPos = y;
		
		iViewShip.setX(xPos);
		iViewShip.setY(yPos);
	}
	
	/**
	 * Gets the image view of the ship object
	 * @return
	 */
	public ImageView getNode() 
	{
		 return iViewShip;
	}
	
	/**
	 * Gets the x-position of the ship
	 * @return Returns the x-position
	 */
	public double getX() {
		return xPos;
	}
	
	/**
	 * Gets the y-position of the ship
	 * @return Returns the y-position
	 */
	public double getY() {
		return yPos;
	}
	
	/**
	 * Gets the height of the ship
	 * @return Returns the height
	 */
	public double getHeight() {
		return height;
	}
	
	/**
	 * Gets the width of the ship
	 * @return Returns the width
	 */
	public double getWidth() {
		return width;
	}
	
	
}