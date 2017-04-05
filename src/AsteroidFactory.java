
public class AsteroidFactory  {
	
	public AsteroidInterface getAsteriods(String size){
		//Switch for asteroid size.
		switch(size){
		case "small":
			//Returns small asteroids
			return new SmallAsteroid();
		case "medium":
			//Returns medium asteroids
			return new MediumAsteroid();
		case "large":
			//Returns large asteroids
			return new LargeAsteroid();
			default: 
				return null;
		}
		
	}

}
