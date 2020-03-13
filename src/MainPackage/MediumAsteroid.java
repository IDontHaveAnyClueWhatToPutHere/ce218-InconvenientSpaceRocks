package MainPackage;

import utilities.Vector2D;

import java.awt.*;

public class MediumAsteroid extends GenericLargerAsteroid {



    public MediumAsteroid(){
        super();
    }

    public MediumAsteroid(Vector2D p){
        super(p);
        //random asteroid at a known position
    }

    @Override
    protected void setSpecifics(){
        super.setSpecifics();
        RADIUS = 25;
        //timeToLive = (int)(Math.random() * 512) + 512;
        pointValue = 4; //worth 4 small asteroids if destroyed by the player
            //2 small asteroids if shot = 6 potential points if shot, but only 5 if allowed to decay
        asteroidScale = 0.75;
        //objectPolygon = PolygonUtilities.scaledPolygonConstructor(hitboxX,hitboxY,1.5);
    }


    @Override
    public void updateColour(){
        if (redScale < 0){
            redScale = 0;
        }
        this.objectColour = new Color(255-redScale,redScale,0,128);
    }

    @Override
    public String toString(){
        return (this.getClass() + " x: " + String.format("%.2f",position.x) + ", y: " + String.format("%.2f",position.y) + ", vx: " + velocity.x + ", vy: " + velocity.y);
    }
}