import javafx.scene.image.Image;

public interface AsteroidInterface {
  //Move method.
  void move();
  //get pos x
  double getPosX();
  //get pos y
  double getPosY();
  //set pos x
  void setPosX(double pos);
  //set pos y
  void setPosY(double pos);
  //get image
  Image getImage();
  
}
