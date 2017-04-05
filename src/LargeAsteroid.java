import java.util.Random;

import javafx.scene.image.ImageView;

public class LargeAsteroid extends ImageView implements AsteroidInterface{
	//Bounced is pre set to false.
	boolean bounced = false;
	double x;
	double y;
	
	public LargeAsteroid(){
		super(LargeAsteroid.class.getResource("Resources/asteroid-png-0.png").toExternalForm());
		Random random = new Random();
		//creates a random position 
	int pos = random.nextInt(600) + 900;
	// makes it negative so it appears off screen
	pos *= -1;
	// random boolean if true comes from the right as it multiplies it again by -1 making it positive.
	if (random.nextBoolean()) {
		this.setPosX((pos * -1));
		this.setPosY(pos);
	} 
	else {
	    //comes from the left as x is negative.
		this.setPosX(pos);
		this.setPosY(pos);
	}	
	}
	
    //Checks whether the asteroids have hit the side of the game window and if so bounce back.
	@Override
	public void move() {
		if (x >= 700) {
			if (x == 700) {
				bounced = true;
			}
			if (bounced) {
				setPosX(x + 0.5);
			} else {
				setPosX(x - 0.5);
			}
		} else {
			if (x == 699.5) {
				bounced = true;
			}
			if (bounced) {
				setPosX(x - 0.5);
			} else {
				setPosX(x + 0.5);
			}

		}
		setPosY(y + 0.5);		
	}
	
	//Returns x
	@Override
	public double getPosX() {
		return x;
	}
	//Returns y
	@Override
	public double getPosY() {
		return y;
	}
	//Sets x
	@Override
	public void setPosX(double pos) {
		 x=pos;			
	}
	//Sets y
	@Override
	public void setPosY(double pos) {
		 y=pos;			
	}
}

