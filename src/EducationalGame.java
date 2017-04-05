import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/**
 * @author Curtis Thomas
 */
public class EducationalGame extends Application {
	public static void main(String[] args) {
		launch(args);
	}
    //Scene
	Scene scene;
	//Root
	TabPane root;
	//Tab 1 and Tab2
	Tab tab1, tab2;
	//Pane for tab1
	Pane display1 = new Pane();
	//Pane for tab 2
	Pane display2 = new Pane();
	//Canvas
	Canvas canvas = new Canvas(800, 600);
	//Graphics
	GraphicsContext g;
	//Math Invader title
	Label l1;
	//Score board
	Label l2;
	//Help
	Label l3;
	//Pause
	Label l4;
	//Game Aim
	Label l5;
	//Controls
	Label l6;
	//Game Over
	Label l7;
	//Score
	Label l8;
	//Answer Ships
	Label answerShip1;
	Label answerShip2;
	Label answerShip3;
	Label answerShip4;
	Label answerShip5;
	//Answer
	Label ans = new Label();
	//Aim text
	Text t = new Text();
	//Controls text
	Text t2 = new Text();
	//question text
	Text question = new Text();
	// main menu buttons
	Button b1;
	Button b2;
	Button b3;
	Button b4;
	Button details;
	// Pause menu
	Button b5;
	Button b6;
	Button b7;
	Button b8;
	Button submit;
	//Enter name text field
	TextField name = new TextField ();
	//Asteroid image
	Image asteroid;
	//un-mute image
	ImageView unMute;
	//mute image
	Image mute;
	//AnswerShipImage image
	ImageView answerShipImage = new ImageView(new Image(EducationalGame.class.getResource("/Resources/AnswerShip.png").toExternalForm()));
	//Bullet image
	ImageView bullet = new ImageView(new Image(EducationalGame.class.getResource("/Resources/gameBullet.png").toExternalForm()));
	//Background image
	Image background = new Image(EducationalGame.class.getResource("/Resources/starstars.jpg").toExternalForm());
	//menu sound sound
	AudioClip menuSound = new AudioClip(EducationalGame.class.getResource("/Resources/Relaxation music- deep space.mp3").toExternalForm());
	//bullet sound
	AudioClip bulletSound = new AudioClip(EducationalGame.class.getResource("/Resources/science_fiction_laser_006.mp3").toExternalForm());
	//explosion sound
	AudioClip explosion = new AudioClip(EducationalGame.class.getResource("/Resources/explosion_single_large_09.mp3").toExternalForm());
	//game over sound
	AudioClip gameOver = new AudioClip(EducationalGame.class.getResource("/Resources/Game Over Sound Effect.mp3").toExternalForm());
	//Correct answer sound
	AudioClip correctAudio = new AudioClip(EducationalGame.class.getResource("/Resources/correct_sound.mp3").toExternalForm());
 

    //ship shape rectangle for collision
	Rectangle shipShape = new Rectangle();
	//Ship image.
	ImagePattern shipImage = new ImagePattern(new Image(EducationalGame.class.getResource("Resources/fighter-spaceship-icone-8705-128.png").toExternalForm()));
	//pause
	boolean pause = false;
	//mouse x
	double mouseX;
	//mouse y
	double mouseY;
	//answer
	int answer = 0;
	//Score 
	int score = 0;
	//y axis
	int y = -600;

	//Array list of asteroids
	ArrayList<AsteroidInterface> asteroidList = new ArrayList<AsteroidInterface>();
	//Array list of answer ships
	ArrayList<Label> answerShipList = new ArrayList<Label>();
	// Array of Players
	ArrayList<Player> players = new ArrayList<Player>();
	//AsteroidFactory object
	AsteroidFactory factory = new AsteroidFactory();

	//Table for leader board
	 TableView<Player> table = new TableView<Player>();
	 TableColumn<Player, String> nameCol = new TableColumn<Player, String>("Name");
     TableColumn<Player, String> scoreCol = new TableColumn<Player, String>("Score");
     
     //Animation timer
	AnimationTimer timer = new AnimationTimer() {

		@Override
		public void handle(long arg0) {
			boolean answered = false;
			g.drawImage(background, 0, y);
			//If y is equal to 0 set the background image back to the start.
			if (y == 0) {
				y = -600;
			}
			//Plus 1 to y to act like the image is looping.
			y += 1;
			for (AsteroidInterface asteroid : asteroidList) {
				//if the game is not paused the move method is called from the asteroid classes for the asteroid images in asteroid list.
				if (!pause) {
					asteroid.move();;
				}
				//Asteroid image is drawn
				g.drawImage(asteroid.getImage(), asteroid.getPosX(),
						asteroid.getPosY());
			}
			//checks if bullet has been created if so moves it.
			if(bullet !=null){
				bullet.setY(bullet.getY() - 10);
			}
			for (Iterator<AsteroidInterface> iterator = asteroidList.iterator(); iterator.hasNext();) {
				AsteroidInterface asteroids = (AsteroidInterface) iterator.next();
				//Checks to see if the ship shape drawn around the cursor collides with asteroids.
				if (shipShape.getBoundsInParent().intersects(asteroids.getPosX(),asteroids.getPosY(), asteroids.getImage().getWidth(),asteroids.getImage().getHeight())) {
					//If collision is detected the following is carried out.
					//The ship shape around cursor image is removed.
					display1.getChildren().remove(shipShape);
					//Sets the cursor to default.
					scene.setCursor(Cursor.DEFAULT);
					//Explosion sound is played.
					explosion.play();
					//Game over sound is played
					gameOver.play();
					//The animation timer is stopped
				    timer.stop();
				    //Game over text is set to visible.
				    l7.setVisible(true);
				    //l8 text is set to the following, displaying the users score.
				    l8.setText("your score is: " + score);
				    //the score text is then set to visible.
				    l8.setVisible(true);
				    //Main menu button appears
				    b7.setVisible(true);
				    //Enter name input is set as visible
					name.setVisible(true);
					//submit button is set as visible
					submit.setVisible(true);
                    //Remove the question for the display
				    display1.getChildren().remove(question);
				    //Remove the answer ships.
				for (Label ans : answerShipList){
					display1.getChildren().remove(ans);
				 }
				}
				if (bullet !=null){
					//Checks if the bullet collides with the asteroids.
					if (bullet.getBoundsInParent().intersects(asteroids.getPosX(),asteroids.getPosY(), asteroids.getImage().getWidth(),asteroids.getImage().getHeight())) {
						//If collision is detected the following is carried out.
						//Removes the image shot out of the array
						iterator.remove();
						//Plays the explosion sound.
						explosion.play();
						//Removes the asteroids from the display.
						display1.getChildren().remove(asteroids);
						//removes the bullet from the display.
						display1.getChildren().remove(bullet);
						//Sets bullet to null.
						bullet = null;
					}
				}
			}
				if (!answerShipList.isEmpty()) {
					for (Iterator<Label> iterator = answerShipList.iterator(); iterator.hasNext();) {
						Label answerShip = (Label) iterator.next();
						//Checks to see if the ship shape drawn around the cursor collides with the answer ship.
						if (shipShape.getBoundsInParent().intersects(answerShip.getBoundsInParent())) {
							//If collision is detected the following is carried out.
							//The ship shape around cursor image is removed.
							display1.getChildren().remove(shipShape);
							//Sets the cursor to default.
							scene.setCursor(Cursor.DEFAULT);
							//Explosion sound is played.
							explosion.play();
							//Game over sound is played
							gameOver.play();
							//The animation timer is stopped
						    timer.stop();
						    //Game over text is set to visible.
						    l7.setVisible(true);
						    //l8 text is set to the following, displaying the users score.
						    l8.setText("your score is: " + score);
						    //the score text is then set to visible.
						    l8.setVisible(true);
						    //Main menu button appears
						    b7.setVisible(true);
						    //Enter name input is set as visible
							name.setVisible(true);
							//submit button is set as visible
							submit.setVisible(true);
		                    //Remove the question for the display
						    display1.getChildren().remove(question);
						    //Remove the answer ships.
						for (Label ans : answerShipList){
							display1.getChildren().remove(ans);
						 }}
						
						if(bullet !=null){
							//Checks to see if the bullet collides with the answer ship.
							if (bullet.getBoundsInParent().intersects(answerShip.getBoundsInParent())) {
								//If collision is detected the following is carried out.
								//Checks to see if the ship id of the ship shot matches the answer.
							if (answerShip.getId().equals("" + answer)) {
								//If so 5 is added to the score
								score += 5; 
								correctAudio.play();
							}
							else{
								//If the question in answered wrong game over
								//Cursor set to default.
								scene.setCursor(Cursor.DEFAULT);
								//Explosion sound is played.
								explosion.play();
								//Game over sound is played
								gameOver.play();
								//The animation timer is stopped
							    timer.stop();
							    //Game over text is set to visible.
							    l7.setVisible(true);
							    //l8 text is set to the following, displaying the users score.
							    l8.setText("your score is: " + score);
							    //the score text is then set to visible.
							    l8.setVisible(true);
							    //Main menu button appears
							    b7.setVisible(true);
							    //Enter name input is set as visible
								name.setVisible(true);
								//submit button is set as visible
								submit.setVisible(true);
			                    //Remove the question for the display
							    display1.getChildren().remove(question);
							  //The ship shape around cursor image is removed.
								display1.getChildren().remove(shipShape);
							    //Remove the answer ships.
							for (Label ans : answerShipList){
								display1.getChildren().remove(ans);
							 }
							}
							//Remove the answer ships from the display.
								for (Label l : answerShipList){	
									display1.getChildren().remove(l);
							}
							//answered is set to true
							answered = true;
							//set the questions visibility to false.
							question.setVisible(false);
							//Removes the bullet,
							display1.getChildren().remove(bullet);
							//Sets the bullet to null.
							bullet = null;
							//Clears the asteroid array list/
							asteroidList.clear();
							//Calls the create asteroids method.
							createAsteroid();
						}}}
					
					if(answered){
						//If answered is true clear answer ship list.
						answerShipList.clear();
						//Sets the questions visibility to false. 
						question.setVisible(false);
						//Calls generate questionAnswer.
						generateQuestionAnswer();
					}
			}
				//ran when the last asteroid is displayed on the screen
			if ((asteroidList.get(asteroidList.size() - 1).getPosY()) >= 0) {
				//x is set to 110.
				int x =110;
					for(Label answer : answerShipList){
						//for answer ships set the layout to x then display them each +120 on x.
						answer.setLayoutX(x);
						x+=120;
					}
				//set the ships visibility to true.
				answerShip1.setVisible(true);
				answerShip2.setVisible(true);
				answerShip3.setVisible(true);
				answerShip4.setVisible(true);
				answerShip5.setVisible(true);						
                //Set questions visibility to true.
				question.setVisible(true);

			}
		}
	
	};
		
	//Event handle for mouse.
	EventHandler<MouseEvent> movingHandler1 = new EventHandler<MouseEvent>() {

		@Override
	public void handle(MouseEvent event) {
	//the x and y of that to the ship image.
    mouseX = event.getX();
    mouseY = event.getY();
	shipShape.setLayoutX(mouseX);
	shipShape.setLayoutY(mouseY);
	//Sets the height and width of the ship image. 
    shipShape.setHeight(80);
	shipShape.setWidth(77);
	}};

	//Event handler for keys.
	EventHandler<KeyEvent> keyHandler = new EventHandler<KeyEvent>() {

		@Override
		public void handle(KeyEvent key) {
			//pressing p brings up the pause menu.
			if ((key.getCode() == KeyCode.P)) {
				//Checks if pause text is already visible, if not the following is carried out.
				if (!l4.isVisible()) {
					//pause is set to true.
					pause = true;
					//The animation times is stopped, image no longer loops and asteroids stop moving.
					timer.stop();
					//Plays menu sound.
					menuSound.play();
					//Sets the cursor to default.
					scene.setCursor(Cursor.DEFAULT);
					//Sets the ship image's visibility to false.
					shipShape.setVisible(false);
					//The ship shape around cursor image is removed.
					display1.getChildren().remove(shipShape);
					//Adds the mute/un mute icon and sets its x and y.
					display1.getChildren().add(unMute);
					unMute.setLayoutX(20);
					unMute.setLayoutY(20);
					// Pause text is set to visible.
					l4.setVisible(true);
					// buttons, resume, help and main menu are set to visible.
					b5.setVisible(true);
					b6.setVisible(true);
					b7.setVisible(true);
					//If questions is visible it is set to false.
					question.setVisible(false);
					//If the ships are visible they're set to false.
					answerShip1.setVisible(false);
					answerShip2.setVisible(false);
					answerShip3.setVisible(false);
					answerShip4.setVisible(false);
					answerShip5.setVisible(false);
					
				}
			}
            //pressing space shoots
			if ((key.getCode() == KeyCode.SPACE)) {
				if (bullet != null) {
					//Removes the bullet if its not null.
					display1.getChildren().remove(bullet);
				}
				//Creates the bullet image.
				bullet = new ImageView(new Image(EducationalGame.class.getResource("Resources/gameBullet.png").toExternalForm()));
				//Sets its x and y and adds it to the display.
				bullet.setLayoutX(mouseX + 33);
				bullet.setLayoutY(mouseY);
				display1.getChildren().add(bullet);
				//Plays bullet sound.
				bulletSound.play();
		

			}
		}
	};

	EventHandler<ActionEvent> bH = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			// Start game button event
			if (event.getSource() == b1) {
				//Menu sound starts
				menuSound.stop();
				//buttons and labels are removed.
				b1.setVisible(false);
				b2.setVisible(false);
				b3.setVisible(false);
				details.setVisible(false);
				l1.setVisible(false);
				//Mute/unMute icon is removed.
				display1.getChildren().remove(unMute);
				//Event Handlers are added to the game.
				scene.setOnKeyPressed(keyHandler);
				display1.setOnMouseMoved(movingHandler1);
				//Ship shape drawn around the cursor is set to visible.
				shipShape.setVisible(true);
				shipShape.setFill(shipImage);
				//shipShape is added to the display.
			    display1.getChildren().add(shipShape);
				//Sets the cursor to none
				scene.setCursor(Cursor.NONE);
				//Create asteroids is called.
				createAsteroid();
				//Animation timer is started.
				timer.start();
				//Adds the question and calls generateQuestionAnswer.
				display1.getChildren().add(question);
				generateQuestionAnswer();
				}
			// Main menu help button event
			if (event.getSource() == b2) {
				//Buttons and labels set to false
				b1.setVisible(false);
				b2.setVisible(false);
				details.setVisible(false);
				b3.setVisible(false);
				l1.setVisible(false);
				//Buttons and labels set to true
				l3.setVisible(true);
				b4.setVisible(true);
				t.setVisible(true);
				t2.setVisible(true);
				l5.setVisible(true);
				l6.setVisible(true);
			}
			//Details button event
			if (event.getSource() == details) {
				try {
					//Shows the user the ReadMe document.
					Desktop.getDesktop().open(new File("src/Resources/Readme.txt"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// Exit button event
			if (event.getSource() == b3) {
				//Closes the program.
				System.exit(0);
			}
			// Main help back
			if (event.getSource() == b4) {
				//Buttons and labels set to true
				b1.setVisible(true);
				b2.setVisible(true);
				details.setVisible(true);
				b3.setVisible(true);
				l1.setVisible(true);
				//Buttons and labels set to false
				b4.setVisible(false);
				l3.setVisible(false);
				t.setVisible(false);
				t2.setVisible(false);
				l5.setVisible(false);
				l6.setVisible(false);
			}
			// Pause resume button event
			if (event.getSource() == b5) {
				//Pause is set to false
				pause = false;
				//Mute/un mute icon is removed.
				display1.getChildren().remove(unMute);
				//menu sound is stopped.
				menuSound.stop();
				//Event handlers re added to the game.
				//Buttons and labels set to false
				l4.setVisible(false);
				b5.setVisible(false);
				b6.setVisible(false);
				b7.setVisible(false);
				//If answer ship is visible make the question visible.
				if(answerShipList.get(0).isVisible() == true){
					question.setVisible(true);
				}
				//Animation timer is started.
				timer.start();
				//Buttons and labels set to true
				answerShip1.setVisible(true);
				answerShip2.setVisible(true);
				answerShip3.setVisible(true);
				answerShip4.setVisible(true);
				answerShip5.setVisible(true);
				//ship shape drawn around the cursor is re added to the game
			    display1.getChildren().add(shipShape);
				shipShape.setVisible(true);
				shipShape.setFill(shipImage);
				//Sets the cursor to none.
				scene.setCursor(Cursor.NONE);
			}
			// pause help button event
			if (event.getSource() == b6) {
				//Buttons and labels set to false
				l4.setVisible(false);
				b5.setVisible(false);
				b6.setVisible(false);
				b7.setVisible(false);
				//Buttons and labels set to true
				l3.setVisible(true);
				b8.setVisible(true);
				t.setVisible(true);
				t2.setVisible(true);
				l5.setVisible(true);
				l6.setVisible(true);
			}
			// Main menu button event
			if (event.getSource() == b7) {
				//Buttons and labels set to false
				l4.setVisible(false);
				b5.setVisible(false);
				b6.setVisible(false);
				b7.setVisible(false);
				l7.setVisible(false);
				l8.setVisible(false);
				name.setVisible(false);
				submit.setVisible(false);
				//Buttons and labels set to true
				l1.setVisible(true);
				b1.setVisible(true);
				b2.setVisible(true);
				details.setVisible(true);
				b3.setVisible(true);
				// Remove asteroids
				asteroidList.clear();
				//Pause set to false
				pause = false;
				//Score is reset to 0
				score = 0;
				//Removes question from the display.
			    display1.getChildren().remove(question);
			    //Clears answer ships
			    answerShipList.clear();
			}
			// pause back button event
			if (event.getSource() == b8) {
				//Buttons and labels set to true
				l4.setVisible(true);
				b5.setVisible(true);
				b6.setVisible(true);
				b7.setVisible(true);
				//Buttons and labels set to false
				b8.setVisible(false);
				l3.setVisible(false);
				t.setVisible(false);
				t2.setVisible(false);
				l5.setVisible(false);
				l6.setVisible(false);
			}
			//Submit button event
			if (event.getSource() == submit && !name.getText().isEmpty() ) {
			  try {
				  //Save is called saving what's in the name text field into the player class.
				  players.add(new Player(name.getText(),"" + score));
				save();
				} catch (IOException e) {
				e.printStackTrace();
				}
			    //button and text field is set to not being visible.
			    submit.setVisible(false);
			    name.setVisible(false);
			}
		}
	};

	@SuppressWarnings("unchecked")
	public void start(Stage stage) throws Exception {
		stage.setTitle("Software Architectures – CurtisThomas");
		root = new TabPane();
		scene = new Scene(root, 800, 600);
		stage.setScene(scene);
		stage.show();
		
		//Tab1
		tab1 = new Tab();
		tab1.setText("Game");
		tab1.setClosable(false);
		tab1.setStyle("-fx-background-image: url(Resources/TabShip)");
		root.getTabs().add(tab1);
		tab1.setContent(display1);

		//Tab2
		tab2 = new Tab();
		tab2.setText("Scoreboard");
		tab2.setClosable(false);
		tab2.setStyle("-fx-background-image: url(Resources/TabShip)");
		root.getTabs().add(tab2);
		tab2.setContent(display2);

		//2D Graphics
		g = canvas.getGraphicsContext2D();
		root.setStyle("-fx-background-image: url(Resources/starstars.jpg)");
		display1.getChildren().add(canvas);
		
		asteroid = new Image(EducationalGame.class.getResource("Resources/asteroid-icon.png").toExternalForm());
	
		//Ship rectangle
       shipShape.setFill(shipImage);

		// Main menu text
		l1 = new Label("Math Invader");
		l1.setFont(new Font("Ayuthaya", 40));
		l1.setTextFill(Color.web("#FFFFFF"));
		l1.setLayoutX(280);
		l1.setLayoutY(50);
		display1.getChildren().add(l1);

		// New game button
		b1 = getButton("New Game");
		b1.setLayoutX(300);
		b1.setLayoutY(150);

		// Main Help button
		b2 = getButton("Help");
		b2.setLayoutX(300);
		b2.setLayoutY(250);

		// Main Help button
		details = getButton("Details");
		details.setLayoutX(300);
		details.setLayoutY(350);

		// Main Quit button
		b3 = getButton("Quit");
		b3.setLayoutX(300);
		b3.setLayoutY(450);

		// Help label 
		l3 = new Label("Help");
		l3.setFont(new Font("Ayuthaya", 40));
		l3.setTextFill(Color.web("#FFFFFF"));
		l3.setLayoutX(340);
		l3.setLayoutY(50);
		display1.getChildren().add(l3);
		l3.setVisible(false);
		
		//Pause label
		l4 = new Label ("Pause");
		l4.setFont(new Font("Ayuthaya", 40));
		l4.setTextFill(Color.web("#FFFFFF"));
		l4.setLayoutX(335);
		l4.setLayoutY(50);
		display1.getChildren().add(l4);
		l4.setVisible(false);
		
		// Aim label
		l5 = new Label("Aim");
		l5.setFont(new Font("Ayuthaya", 30));
		l5.setTextFill(Color.web("#FFFFFF"));
		l5.setLayoutX(350);
		l5.setLayoutY(125);
		display1.getChildren().add(l5);
		l5.setVisible(false);
		// Aim text
		t.setFont(new Font("Ayuthaya", 20));
		t.setFill(Color.web("#FFFFFF"));
		t.setText("The aim of the game is to defend the ship from incoming asteroids and to shoot\nthe ship with the correct answer"
				+ "on to the maths question provided. The game is\nplayed until either you die from an asteriod or get the answer incorrect.");
		t.setLayoutX(50);
		t.setLayoutY(200);
		display1.getChildren().add(t);
		t.setVisible(false);

		// Controls label
		l6 = new Label("Controls");
		l6.setFont(new Font("Ayuthaya", 30));
		l6.setTextFill(Color.web("#FFFFFF"));
		l6.setLayoutX(325);
		l6.setLayoutY(280);
		display1.getChildren().add(l6);
		l6.setVisible(false);
		
		// Controls text
		t2.setFont(new Font("Ayuthaya", 20));
		t2.setFill(Color.web("#FFFFFF"));
		t2.setText("Shoot: Space \nControl Ship: Mouse/Trackpad\nPause game: P");
		t2.setLayoutX(325);
		t2.setLayoutY(350);
		display1.getChildren().add(t2);
		t2.setVisible(false);

		// Main help Back button
		b4 = getButton("Back");
		b4.setLayoutX(75);
		b4.setLayoutY(475);
		b4.setPrefSize(100, 35);
		b4.setVisible(false);

		// Pause Resume button
		b5 = getButton("Resume");
		b5.setLayoutX(300);
		b5.setLayoutY(150);
		b5.setVisible(false);

		// Pause Help button
		b6 = getButton("Help");
		b6.setLayoutX(300);
		b6.setLayoutY(250);
		b6.setVisible(false);

		// pause Main menu button
		b7 = getButton("Main Menu");
		b7.setLayoutX(300);
		b7.setLayoutY(350);
		b7.setVisible(false);

		// Pause back button
		b8 = getButton("Back");
		b8.setLayoutX(75);
		b8.setLayoutY(475);
		b8.setPrefSize(100, 35);
		b8.setVisible(false);
		
		//End game label
		l7 = new Label("GAME OVER");
	    l7.setFont(new Font("Ayuthaya", 100));
		l7.setTextFill(Color.web("#FFFFFF"));
		l7.setLayoutX(110);
		l7.setLayoutY(100);
		display1.getChildren().add(l7);
		l7.setVisible(false);
		
		//score label
		l8 = new Label("your score is: " + score);
	    l8.setFont(new Font("Ayuthaya", 40));
		l8.setTextFill(Color.web("#FFFFFF"));
		l8.setLayoutX(270);
		l8.setLayoutY(220);
		display1.getChildren().add(l8);
		l8.setVisible(false);
		
		//Enter name text field
		name.setPromptText("Enter name");
		name.getText();
		name.setLayoutX(300);
		name.setLayoutY(300);
		name.setPrefColumnCount(9);
		display1.getChildren().add(name);
		name.setVisible(false);
		
		//Submit button
		submit = getButton("Submit");
		submit.setLayoutX(430);
		submit.setLayoutY(300);
		submit.setPrefHeight(10);
		submit.setPrefWidth(75);
		submit.setVisible(false);

		//Creates the mute/un mute image and sets its x and y.
		unMute = new ImageView(new Image(EducationalGame.class.getResource("Resources/unmute-128.png").toExternalForm()));
		unMute.setId("UnMute");
		display1.getChildren().add(unMute);
		unMute.setLayoutX(20);
		unMute.setLayoutY(20);
		//Mouse clicked event handler for mute/un mute.
		unMute.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				//Checks the id of each of the images and swaps the images round.
				if (unMute.getId().equals("UnMute")) { 
					// stops it
					menuSound.stop();
					unMute.setImage(new Image(EducationalGame.class.getResource("Resources/speaker_mute.png").toExternalForm()));
					unMute.setId("Mute");

				} else { 
					// plays it
					menuSound.play();
					unMute.setImage(new Image(EducationalGame.class.getResource("Resources/unmute-128.png").toExternalForm()));
					unMute.setId("UnMute");
				}
			}
		});
        //Play menu sound
		menuSound.play();
		
		//Tab 2 Code
		
		// Tab two main label
		l2 = new Label("Scoreboard");
	    l2.setFont(new Font("Ayuthaya", 40));
		l2.setTextFill(Color.web("#FFFFFF"));
		l2.setLayoutX(300);
		l2.setLayoutY(50);
		display2.getChildren().add(l2);
		
	    //Leader board table
		loadScores();
	    table.getColumns().addAll(nameCol, scoreCol);
	    nameCol.setCellValueFactory(new PropertyValueFactory<Player, String>("name"));
	    scoreCol.setCellValueFactory(new PropertyValueFactory<Player, String>("score"));
	    table.setLayoutX(280);
	    table.setLayoutY(120);
	    table.setItems(FXCollections.observableArrayList(players));
	    display2.getChildren().add(table);
	    
	}

	public Button getButton(String name) {
        //As all button styles are the same, get button allows the use of one set of code to be used for all buttons
		//This means it does not have to be retyped for every button.
		final Button buttonCreation = new Button(name);
		buttonCreation.setOnAction(bH);
		buttonCreation.setPrefSize(200, 50);
		buttonCreation.setStyle("-fx-base:#585858; -fx-focus-color: transparent;");
		buttonCreation.setEffect(new Reflection());
		display1.getChildren().add(buttonCreation);
		buttonCreation.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				buttonCreation.setEffect(new DropShadow(50, Color.WHITE));
			}
		});
		buttonCreation.addEventHandler(MouseEvent.MOUSE_EXITED ,new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						buttonCreation.setEffect(new Reflection());
					}
				});
		return buttonCreation;
	}

	public void createAsteroid() {
		//create asteroid makes a call to getAsteroids in AsteroidFactory which uses a switch to generate asteroid sizes.
		// smallest image
		for (int x = 1; x < 55; x += 1) {
	    asteroidList.add(factory.getAsteriods("small"));
		} //medium image
		for (int x = 1; x < 45; x += 1) {
		    asteroidList.add(factory.getAsteriods("medium"));
		}// large image
		for (int x = 1; x < 30; x += 1) {
		    asteroidList.add(factory.getAsteriods("large"));
		}
	}

	//each time enerateQuestionAnswer is called it generates a random answer and question.
	public void generateQuestionAnswer() {
		//Creates two random integers between 1 and 10.
		Random rand = new Random();
		int a = rand.nextInt(10) + 1;
		int b = rand.nextInt(10) + 1;
		String operator;
		//Generates a random operator for the question.
		switch (rand.nextInt(4) + 1) {
		case 1:
			operator = "+";
			answer = (a + b);
			break;
		case 2:
			operator = "-";
			answer = (a - b);
			break;
		case 3:
			operator = "/";
			answer = (a / b);
			break;
		case 4:
			operator = "*";
			answer = (a * b);
			break;
		default:
			operator = "+";
			break;
		}
		
		//Sets the question text to random integer a and b and assigns a random operator.
		question.setText(a + " " + operator + " " + b);
		//Questions font, color and x and y axis are set.
		question.setFont(new Font("Ayuthaya", 60));
		question.setFill(Color.web("#FFFFFF"));
		question.setLayoutX(290);
		question.setLayoutY(220);
		//It is then added to the display and visibility is set to false.
		question.setVisible(false);
		
		//First answer ship is added and the text is set to the answer.
		answerShip1 = new Label("" + answer, new ImageView(new Image(EducationalGame.class.getResource("Resources/AnswerShip.png").toExternalForm())));
		answerShip1.setStyle("-fx-font-size: 30; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;");
		answerShip1.setLayoutX(-150);
		answerShip1.setLayoutY(10);
		answerShip1.setContentDisplay(ContentDisplay.CENTER);
		//Ships id is set to the answer.
		answerShip1.setId("" + answer);
		answerShip1.setVisible(false);
		display1.getChildren().add(answerShip1);
		answerShipList.add(answerShip1);

		//Creates 4 more ships which will have incorrect answers on.
		for (int x = 230; x <= 590; x += 120) {
			Label ans = new Label("" + (rand.nextInt(10) + 1), new ImageView(new Image(EducationalGame.class.getResource("Resources/AnswerShip.png").toExternalForm())));
			ans.setStyle("-fx-font-size: 30; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;");
			ans.setLayoutX(-150);
			ans.setLayoutY(10);
			ans.setId(ans.getText());
			ans.setVisible(false);
			ans.setContentDisplay(ContentDisplay.CENTER);
			answerShipList.add(ans);
			display1.getChildren().add(ans);
		}
		answerShip2 = answerShipList.get(1);
		answerShip3 = answerShipList.get(2);
		answerShip4 = answerShipList.get(3);
		answerShip5 = answerShipList.get(4);
		Collections.shuffle(answerShipList);
	}
	
    //Called when submit name is pressed, gets the name from the text field and saves it to leaderboard.txt
	public void save() throws IOException{
		FileWriter writer = new FileWriter("src/Resources/leaderboard.txt", false);
		PrintWriter printer = new PrintWriter(writer);
		int x = 1;
		for (Player player : players){
			if(x==players.size()){
				printer.print(player.getName() + " " + player.getScore());
			}
			else{
				printer.println(player.getName() + " " + player.getScore());
			}
			x++;
		}
		printer.close();
	}
	
		//Loads the scores from leader board.
	public void loadScores() throws FileNotFoundException{
		/**
		 * Allows leaderboard.txt to be used with jar file.
		 */
		  InputStream in = getClass().getResourceAsStream("/Resources/leaderboard.txt");
		  Scanner scanner = new Scanner(in);
		   //Scanner scanner = new Scanner(new File("src/Resources/leaderboard.txt"));
		while(scanner.hasNextLine()){
			players.add(new Player(scanner.next(),scanner.next()));
			}
		scanner.close();
		}
}


