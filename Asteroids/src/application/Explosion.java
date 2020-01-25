package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Explosion {
	
	private double xPos, yPos;
	private Image img;
	private ImageView iView;

	/**
	 * Creates a explosion object
	 */
	public Explosion() {
		xPos = 0;
		yPos = 0;
		
		img = new Image("file:.png");
		iView = new ImageView(img);
	}
	
	/**
	 * Sets the location of the object
	 * @param x - Sets the x-position of the explosion
	 * @param y - Sets the y-position of the explosion
	 */
	public void setLocation(double x, double y) {
		xPos = x;
		yPos = y;
		
		iView.setX(x);
		iView.setY(y);
	}
	/**
	 * Gets the image view object of the explosion
	 * @return Returns the image view object
	 */
	public ImageView getNode() {
		
		return iView;
	}
	
}
