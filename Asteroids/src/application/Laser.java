package application;

import javafx.scene.image.*;

public class Laser {
	
	//Declare global variables of this class
	private double xPos, yPos, height, width;
	private Image imgLaser;
	private ImageView iView;
	private boolean fired;
	private Ship ship = new Ship();
	
	/**
	 * 	A laser used for shooting down asteroids
	 */
	public Laser() {
		fired = false;
		xPos = ship.getX();
		yPos = ship.getY();
		
		imgLaser = new Image("file:images\\laserbeam_red.png");
		
		height = 0;
		width = 0;
		
		iView = new ImageView(imgLaser);
	}
	
	/**
	 * Sets the location of the asteroid
	 * @param x - Sets the x-position of the asteroid
	 * @param y - Sets the y-position of the asteroid
	 */
	public void setLocation(double x, double y) {
		xPos = x;
		yPos = y;
		
		iView.setX(xPos);
		iView.setY(yPos);
	}
	
	/**
	 * Moves the asteroid at a certain speed
	 * @param x - Set the speed of the asteroid
	 */
	public void move(double x) {
		xPos += x;
		iView.setX(xPos);
	}
	
	/**
	 * A image view object for the laser
	 * @return It will return the image view object for the laser
	 */
	public ImageView getNode() {
		
		return iView;
	}
	
	/**
	 * Gets the image of the laser
	 * @return Returns the current image of the laser
	 */
	public Image getImage() {
		
		return imgLaser;
	}
	
	/**
	 * Gets the width of the laser
	 * @return Returns the width of the laser image
	 */
	public double getWidth() {
		
		return width;
	}
	
	/**
	 * Gets the x-position of the laser
	 * @return Returns the x-position of the laser image
	 */
	public double getX() {
		
		return xPos;
	}
	
	/**
	 * Gets the y-position of the laser
	 * @return Returns the y-position of the laser image
	 */
	public double getY() {
		return yPos;
	}
}
