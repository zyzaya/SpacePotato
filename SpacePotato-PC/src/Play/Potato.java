package Play;

import Collision.Square;
import Draw.Draw;
import Draw.Image;
import GameObject.CollisionObject;

public class Potato extends CollisionObject {

    private final int MAX_X, MAX_Y;
    private double xSpeed, ySpeed;
    private Image image;
    
    public Potato(Image image, double xSpeed, double ySpeed, int maxX, int maxY) {
        MAX_X = maxX;
        MAX_Y = maxY;
        
        this.image = image;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }
    
    public void moveHorizontally() {
        Square bounds = getBounds();
        
        if (bounds.getRight() >= MAX_X) xSpeed = -Math.abs(xSpeed);
        else if (bounds.getX() <= 0) xSpeed = Math.abs(xSpeed);
        
        double newX = bounds.getX() + xSpeed;
        bounds.setX(newX);
        setBounds(bounds);
    }
    
    public void reverseHorizontally() {
        xSpeed *= -1;
    }
    
    public void moveVertically() {
        Square bounds = getBounds();
        
        if (bounds.getBottom() >= MAX_Y) ySpeed = -Math.abs(ySpeed);
        else if (bounds.getY() <= 0) ySpeed = Math.abs(ySpeed);
        
        double newY = bounds.getY() + ySpeed;
        bounds.setY(newY);
        setBounds(bounds);
    }
    
    public void reverseVertically() {
        ySpeed *= -1;
    }
    
    @Override
    public void draw(Draw graphics) {
        graphics.drawImage(getBounds(), image);
    }
}
