package application;



import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;

import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import myexception.nameTooLongException;
import myexception.noPotionException;
import java.io.InputStream;
import java.util.Random;
import entity.Character;
import entity.Monster;

public class Main extends Application {

	private Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
	private Double screenWidth = primaryScreenBounds.getWidth();
	private  Double screenHeight = primaryScreenBounds.getHeight() * 0.97;

	private Stage window;
	private Scene mainScene;
	private Scene battleScene;


	
	private BorderPane root = new BorderPane();
	private Canvas characterImageCanvas;
	private Canvas characterActionCanvas;
	private Canvas hoverCanvas;
	private Canvas battleCanvas;
	private Button inspectButton, fightButton, itemButton;
	private Stage fightStage = new Stage(StageStyle.UNDECORATED);

	
	private Character c1;
	private  boolean isBoy;
	private  int stageNumber  = 1;
	private  int healthPotion = 0;
	

	private  Font CHR_FONT = Font.font("Helveltica", screenWidth*0.012);
	private  Font HP_FONT = Font.font("Helveltica", FontWeight.BOLD,screenWidth*0.01);
	private  Font S_HP_FONT = Font.font("Helveltica", FontWeight.BOLD, screenWidth*0.01);
	private  Font ACTION_FONT = Font.font("Helveltica", FontWeight.BOLD, screenWidth*0.02);
	private  Font FIGHT_FONT = Font.font("Helveltica", screenWidth*0.01);
	
	private AudioClip bgm = new AudioClip(getClass().getResource("/audio/bgm.wav").toExternalForm()); 
	private AudioClip attackSound = new AudioClip(getClass().getResource("/audio/attack.wav").toExternalForm()); 
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			
			bgm.setVolume(0.3f);
			attackSound.setVolume(0.2f);
			bgm.setCycleCount(100);
	        bgm.play();

			window = primaryStage;
			window.setMaximized(true);
			window.setResizable(false);
			
			window.sizeToScene();
			window.setX(primaryScreenBounds.getMinX());
			window.setY(primaryScreenBounds.getMinY());

			root = new BorderPane();
			mainScene = new Scene(root, screenWidth, screenHeight);
			primaryStage.setScene(mainScene);
			primaryStage.setTitle("Main Screen");
			
			
			startScene();
			primaryStage.show();
			
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent arg0) {
					fightStage.close();
				}
				
			});
			
			primaryStage.setOnHidden(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent arg0) {
					fightStage.close();
				}
				
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	private void initializeBattleScene(String background) {
		
	
        
		VBox vbox = new VBox();

		battleScene = new Scene(vbox, screenWidth, screenHeight);
		window.setTitle("BattleScene");
		vbox.setOpacity(0);

		HBox hbox = new HBox();

		// characterImageCanvas--------------------------------------------------------------------------//
		characterImageCanvas = new Canvas(battleScene.getWidth() * 0.20, battleScene.getHeight() * 0.30);
		GraphicsContext cigc = characterImageCanvas.getGraphicsContext2D();

		cigc.setFill(Color.BISQUE);
		cigc.fillRect(0, 0, characterImageCanvas.getWidth(), characterImageCanvas.getHeight());

		cigc.setStroke(Color.SADDLEBROWN);
		cigc.setLineWidth(10);
		cigc.strokeRect(0, 0, characterImageCanvas.getWidth(), characterImageCanvas.getHeight());

		// characterImageCanvas--------------------------------------------------------------------------//

		VBox actionBox = new VBox();
		// characterActionCanvas--------------------------------------------------------------------------//
		characterActionCanvas = new Canvas(battleScene.getWidth() * 0.40, battleScene.getHeight() * 0.22);

		GraphicsContext cagc = characterActionCanvas.getGraphicsContext2D();

		cagc.setFill(Color.BISQUE);
		cagc.fillRect(0, 0, characterActionCanvas.getWidth(), characterActionCanvas.getHeight());

		cagc.setStroke(Color.SADDLEBROWN);
		cagc.setLineWidth(10);
		cagc.strokeRect(0, 0, characterActionCanvas.getWidth(), characterActionCanvas.getHeight());

		HBox buttonsBox = new HBox();
		buttonsBox.setPadding(new Insets(2));
		fightButton = new Button("Fight");
		itemButton = new Button("Health Potion");
		inspectButton = new Button("Inspect");

		fightButton.setPrefSize(battleScene.getWidth() * 0.33 * 0.4, battleScene.getHeight() * 0.08);
		itemButton.setPrefSize(battleScene.getWidth() * 0.33 * 0.4, battleScene.getHeight() * 0.08);
		inspectButton.setPrefSize(battleScene.getWidth() * 0.33 * 0.4, battleScene.getHeight() * 0.08);

		fightButton.setFont(CHR_FONT);
		itemButton.setFont(CHR_FONT);
		inspectButton.setFont(CHR_FONT);

		buttonsBox.getChildren().addAll(fightButton, itemButton,inspectButton);

		// characterImageCanvas--------------------------------------------------------------------------//

		hoverCanvas = new Canvas(battleScene.getWidth() * 0.40, battleScene.getHeight() * 0.30);
		GraphicsContext hgc = hoverCanvas.getGraphicsContext2D();

		hgc.setFill(Color.SANDYBROWN);
		hgc.fillRect(0, 0, hoverCanvas.getWidth(), hoverCanvas.getHeight());
		hgc.setStroke(Color.DARKSLATEGRAY);
		hgc.setLineWidth(10);
		hgc.strokeRect(0, 0, hoverCanvas.getWidth(), hoverCanvas.getHeight());

		actionBox.getChildren().add(characterActionCanvas);
		actionBox.getChildren().add(buttonsBox);

		hbox.getChildren().add(characterImageCanvas);
		hbox.getChildren().add(actionBox);

		hbox.getChildren().add(hoverCanvas);

		// background----------------------------------------------------------------------------//

		battleCanvas = new Canvas(battleScene.getWidth(), battleScene.getHeight() * 0.70);
		

		String backgroundPath = "/battleBackground/"+background+".jpg";
		InputStream bgInp = getClass().getResourceAsStream(backgroundPath);
		Image bgImg = new Image(bgInp, battleCanvas.getWidth(), battleCanvas.getHeight(), false, false);
		
		GraphicsContext bgc = battleCanvas.getGraphicsContext2D();
		bgc.drawImage(bgImg, 0, 0);

		// background----------------------------------------------------------------------------//
		
		
		// drawPortrait -------------------------------------------------------------------------------
	

		Image steveImg = c1.getPortrait();
		cigc.drawImage(steveImg, 0, 0, characterImageCanvas.getWidth(), characterImageCanvas.getHeight());
		cigc.setStroke(Color.SADDLEBROWN);
		cigc.setLineWidth(10);
		cigc.strokeRect(0, 0, characterImageCanvas.getWidth(), characterImageCanvas.getHeight());
		cigc.strokeRect(0, 0, characterImageCanvas.getWidth(), characterImageCanvas.getHeight());
		// -------------------------------------------------------------------------------------
		
		//drawName&Stage-----------------------------------------------------------------------------------
		cagc.setFont(CHR_FONT);
		cagc.setFill(Color.BLACK);
		String nameLvText = c1.getName()+ "        Fighting at Stage: "+stageNumber+"";
		cagc.fillText(nameLvText, characterActionCanvas.getWidth() * 0.05,
				characterActionCanvas.getHeight() * 0.3);
		cagc.fillText("HP:", characterActionCanvas.getWidth() * 0.05, characterActionCanvas.getHeight() * 0.65);
		//--------------------------------------------------------------------------------------------------
		
		vbox.getChildren().add(battleCanvas);
		vbox.getChildren().add(hbox);

		FadeTransition fadeOut = new FadeTransition(Duration.millis(1000), root);
		fadeOut.setFromValue(1);
		fadeOut.setToValue(0);
		fadeOut.play();

		fadeOut.setOnFinished(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				FadeTransition fadeIn = new FadeTransition(Duration.millis(700), vbox);
				fadeIn.setFromValue(0);
				fadeIn.setToValue(1);
				fadeIn.play();

				window.setScene(battleScene);
			}
		});
	}
	
	private void updateBattle(Character c1, Monster m1, Monster m2, Monster m3,String background) {
		

				GraphicsContext bcgc = battleCanvas.getGraphicsContext2D();
				
				String backgroundPath = "/battleBackground/"+background+".jpg";
				InputStream bgInp = getClass().getResourceAsStream(backgroundPath);
				Image bgImg = new Image(bgInp, battleCanvas.getWidth(), battleCanvas.getHeight(), false, false);
				
				bcgc.drawImage(bgImg, 0, 0);
				
				bcgc.setFill(Color.WHITE);
				
				

				drawCharacter(c1, 2);

				drawMonster(m1, 1);
				drawMonster(m2, 2);
				drawMonster(m3, 3);

		

				// Character
				// Action-----------------------------------------------------------------------
				GraphicsContext cagc = characterActionCanvas.getGraphicsContext2D();

				cagc.setFont(CHR_FONT);
				cagc.setFill(Color.BLACK);
				
				cagc.setStroke(Color.BLACK);
				cagc.setLineWidth(3);
				double startWidth = characterActionCanvas.getWidth() * 0.15;
				double startHeight = characterActionCanvas.getHeight() * 0.52;
				double healthBarWidth = characterActionCanvas.getWidth() * 0.8;
				double healthBarHeight = characterActionCanvas.getHeight() * 0.15;
				cagc.setFill(Color.LIGHTGRAY);
				cagc.fillRect(startWidth, startHeight, healthBarWidth, healthBarHeight);
				double greenbar = healthBarWidth * c1.getHp() / c1.getMaxHp();
				cagc.setFill(Color.MEDIUMSEAGREEN);
				cagc.fillRect(startWidth, startHeight, greenbar, healthBarHeight);
				cagc.setStroke(Color.BLACK);
				cagc.strokeRect(startWidth, startHeight, healthBarWidth, healthBarHeight);
				cagc.setFill(Color.WHITE);
				cagc.setFont(HP_FONT);
				;
				String hpText = c1.getHp() + " / " + c1.getMaxHp();
				cagc.fillText(hpText, startWidth * 3.3, startHeight * 1.22);

				inspectButton.setOnMouseEntered(new EventHandler<MouseEvent>() {

					public void handle(MouseEvent mE) {
						// TODO Auto-generated method stub
						GraphicsContext hcgc = hoverCanvas.getGraphicsContext2D();
						hcgc.setFont(ACTION_FONT);

						hcgc.setFill(Color.LIGHTYELLOW);
						hcgc.fillText("Inspect", hoverCanvas.getWidth() * 0.07, hoverCanvas.getHeight() * 0.25);

						hcgc.setFont(CHR_FONT);
						String fleeDescription = "You're likely to encounter different enemies\nInspecting them will show their stats.\nUse the information to plan your attacks.\nBe careful! Inspecting cost you a turn.";
						hcgc.fillText(fleeDescription, hoverCanvas.getWidth() * 0.07, hoverCanvas.getHeight() * 0.45);

						inspectButton.setOnMouseExited(new EventHandler<MouseEvent>() {

							public void handle(MouseEvent mE) {
								hcgc.setFill(Color.SANDYBROWN);
								hcgc.fillRect(0, 0, hoverCanvas.getWidth(), hoverCanvas.getHeight());
								hcgc.setStroke(Color.DARKSLATEGRAY);
								hcgc.setLineWidth(10);
								hcgc.strokeRect(0, 0, hoverCanvas.getWidth(), hoverCanvas.getHeight());

							}
						});

					}
				});

				
			
				fightButton.setOnMouseEntered(new EventHandler<MouseEvent>() {

					public void handle(MouseEvent mE) {
						// TODO Auto-generated method stub
						GraphicsContext hcgc = hoverCanvas.getGraphicsContext2D();
						hcgc.setFont(ACTION_FONT);

						hcgc.setFill(Color.LIGHTYELLOW);
						hcgc.fillText("Fight", hoverCanvas.getWidth() * 0.07, hoverCanvas.getHeight() * 0.25);

						hcgc.setFont(CHR_FONT);
						String fleeDescription = "Do something to overcome your opponent.";
						hcgc.fillText(fleeDescription, hoverCanvas.getWidth() * 0.07, hoverCanvas.getHeight() * 0.45);

						fightButton.setOnMouseExited(new EventHandler<MouseEvent>() {

							public void handle(MouseEvent mE) {
								hcgc.setFill(Color.SANDYBROWN);
								hcgc.fillRect(0, 0, hoverCanvas.getWidth(), hoverCanvas.getHeight());
								hcgc.setStroke(Color.DARKSLATEGRAY);
								hcgc.setLineWidth(10);
								hcgc.strokeRect(0, 0, hoverCanvas.getWidth(), hoverCanvas.getHeight());

							}
						});

					}
				});

				itemButton.setOnMouseEntered(new EventHandler<MouseEvent>() {

					public void handle(MouseEvent mE) {
						// TODO Auto-generated method stub
						GraphicsContext hcgc = hoverCanvas.getGraphicsContext2D();
						hcgc.setFont(ACTION_FONT);

						hcgc.setFill(Color.LIGHTYELLOW);
						hcgc.fillText("Health Potion(s)", hoverCanvas.getWidth() * 0.07, hoverCanvas.getHeight() * 0.25);

						hcgc.setFont(CHR_FONT);
						String fleeDescription = "Current you have "+healthPotion+" health potion(s)\nRestore 75% of your HP. Doesn't skip your turn.";
						hcgc.fillText(fleeDescription, hoverCanvas.getWidth() * 0.07, hoverCanvas.getHeight() * 0.45);

						itemButton.setOnMouseExited(new EventHandler<MouseEvent>() {

							public void handle(MouseEvent mE) {
								hcgc.setFill(Color.SANDYBROWN);
								hcgc.fillRect(0, 0, hoverCanvas.getWidth(), hoverCanvas.getHeight());
								hcgc.setStroke(Color.DARKSLATEGRAY);
								hcgc.setLineWidth(10);
								hcgc.strokeRect(0, 0, hoverCanvas.getWidth(), hoverCanvas.getHeight());

							}
						});

					}
				});
				
				itemButton.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						try {
							
							if (healthPotion==0) throw new noPotionException();
						} catch (noPotionException e) {
							// TODO Auto-generated catch block
							Alert errorAlert = 	 new Alert(AlertType.ERROR);
							errorAlert.setHeaderText("Can't use health potion!");
							errorAlert.setContentText(e.getError());
							errorAlert.setTitle("No Potion");
							errorAlert.showAndWait();
							
							return;
						}
						
							int plusHP = (int) (c1.getMaxHp()*0.75);
							c1.increaseHp(plusHP);
							healthPotion--;
							updateBattle(c1,m1,m2,m3,background);
					}
				});

				inspectButton.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						
						Alert errorAlert = 	 new Alert(AlertType.INFORMATION);
						errorAlert.setHeaderText("Monster Information\n"
								+ "Be noted that this is just a base attack \nReal damage varies every attack.");
						errorAlert.setContentText("Monster1(Left):	"+m1.getName()+" 	Base Atk:	"+m1.getAtk()
								+ "\nMonster2(Middle):	"+m1.getName()+ "	Base Atk:	"+m2.getAtk()
								+ "\nMonster3(Right ):	"+m3.getName()+"	Base Atk:	"+m3.getAtk());
						
						errorAlert.showAndWait();
						if (!m1.isDead()) m1.attack(c1);
						if (!m2.isDead()) m2.attack(c1);
						if (!m3.isDead()) m3.attack(c1);
						if (c1.isDead()) gameOverScene();
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						updateBattle(c1,m1,m2,m3,background);
						
					}
				});

				fightButton.setOnAction(new EventHandler<ActionEvent>() {

					public void handle(ActionEvent event) {
						initializeFightStage(c1,m1,m2,m3,background);
						if (m1.isDead() && m2.isDead() && m3.isDead()) {
							window.setScene(mainScene);
							window.setTitle("Main Screen");
						}
					}
				});

			

	}
	private void drawMonster(Monster c, int position) {

		Image img = c.getCharImage();
		
		if (c.isDead()) {
			img = c.getDeadImage();
		}
		double startCWidth = battleCanvas.getWidth();
		double startCHeight = battleCanvas.getWidth() * 0.1;
		double cWidth = battleCanvas.getWidth() * 0.1;
		double cHeight = battleCanvas.getHeight() * 0.5;
		
		GraphicsContext bcgc = battleCanvas.getGraphicsContext2D();

		
		bcgc.drawImage(img, (0.50 + 0.1 * position) * startCWidth, startCHeight, cWidth, cHeight);

		bcgc.setFill(Color.LIGHTGRAY);
		bcgc.fillRect((0.515 + 0.1 * position) * startCWidth, startCHeight * 0.95, cWidth * 0.7, cHeight * 0.08);
		double redBar = cWidth * 0.7 * c.getHp() / c.getMaxHp();
		bcgc.setFill(Color.INDIANRED);
		bcgc.fillRect((0.515 + 0.1 * position) * startCWidth, startCHeight * 0.95, redBar, cHeight * 0.08);

		bcgc.setStroke(Color.BLACK);
		bcgc.setLineWidth(3);
		bcgc.strokeRect((0.515 + 0.1 * position) * startCWidth, startCHeight * 0.95, cWidth * 0.7, cHeight * 0.08);

		bcgc.setFont(S_HP_FONT);
		bcgc.setFill(Color.WHITE);
		bcgc.fillText(c.getHp() + " / " + c.getMaxHp(), (0.515 + 0.1 * position) * startCWidth + cWidth * 0.25,
				startCHeight * 1.06);
		}

	
	private void drawCharacter(Character c, int position) {

			double startCWidth = battleCanvas.getWidth();
			double startCHeight = battleCanvas.getWidth() * 0.1;
			double cWidth = battleCanvas.getWidth() * 0.1;
			double cHeight = battleCanvas.getHeight() * 0.5;

			Image img = c.getCharImage();
			GraphicsContext bcgc = battleCanvas.getGraphicsContext2D();
			bcgc.drawImage(img, (0.1 * position * startCWidth), 0.5*startCHeight, cWidth*1.3, cHeight*1.3);
		}
   
	private void initializeFightStage(Character c1,Monster m1,Monster m2,Monster m3,String background) {
		double w = screenWidth * 0.398;
		double h = screenHeight * 0.287;
		
		StackPane root= new StackPane();
		Scene fightScene = new Scene(root, w,h);
		
		BorderPane bg = new BorderPane();
		Canvas bgCanvas = new Canvas(w,h);
		GraphicsContext gc = bgCanvas.getGraphicsContext2D();
		gc.setFill(Color.INDIANRED);
		gc.fillRect(0, 0, w, h);
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(5);
		gc.strokeRect(0,0,w,h);
		bg.setCenter(bgCanvas);
		
		VBox vbox = new VBox();
		HBox normalAttackBox = new HBox();
		
		Button b1 = new Button("Attack Enemy 1");
		Button b2 = new Button("Attack Enemy 2");
		Button b3 = new Button("Attack Enemy 3");
		b1.setPrefSize(w/3, h/5);b1.setFont(FIGHT_FONT);
		b2.setPrefSize(w/3, h/5);b2.setFont(FIGHT_FONT);
		b3.setPrefSize(w/3, h/5);b3.setFont(FIGHT_FONT);
		normalAttackBox.getChildren().addAll(b1,b2,b3);
		
		Button splashAttack = new Button("Splash Attack");
		splashAttack.setPrefSize(w/3, h/5);splashAttack.setFont(FIGHT_FONT);
		
		Button back = new Button("Back");
		back.setPrefSize(w/3, h/5);back.setFont(FIGHT_FONT);
		
		vbox.setSpacing(h/7);
		vbox.getChildren().add(normalAttackBox);
		vbox.getChildren().add(splashAttack);
		vbox.getChildren().add(back);
		
		
		root.getChildren().add(bg);
		root.getChildren().add(vbox);
		fightStage.setScene(fightScene);
		fightStage.sizeToScene();

		fightStage.setX(screenWidth * 0.2);
		fightStage.setY(screenHeight * 0.729);
		fightStage.setAlwaysOnTop(true);
		fightStage.setResizable(false);
		fightStage.show();
		
		b1.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (m1.isDead() ) return;
				c1.attack(m1);  attackSound.play();
				fightStage.close();
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				updateBattle(c1,m1,m2,m3,background);
			
				if (m1.isDead() && m2.isDead() && m3.isDead()) {
					
					rewardScene();
					window.setTitle("Reward Scene");
				}
				
				if (!m1.isDead()) {m1.attack(c1);updateBattle(c1,m1,m2,m3,background);}	
				if (!m2.isDead()) m2.attack(c1);updateBattle(c1,m1,m2,m3,background);		
				if (!m3.isDead()) m3.attack(c1);updateBattle(c1,m1,m2,m3,background);
				
				if (c1.isDead()) {gameOverScene();
				
				}
 			}
		});
		
		b1.setOnMouseEntered(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent mE) {
				// TODO Auto-generated method stub
				GraphicsContext hcgc = hoverCanvas.getGraphicsContext2D();
				hcgc.setFont(ACTION_FONT);
				
				String str = "/battleBackground/monsterIcon.png";
				InputStream inp  = getClass().getResourceAsStream(str);
				Image img = new Image(inp);
				
				String strM = "/battleBackground/mark.png";
				InputStream inpM  = getClass().getResourceAsStream(strM);
				Image imgM = new Image(inpM);
				 
				
				hcgc.setFill(Color.LIGHTYELLOW);
				hcgc.fillText("Normal Attack", hoverCanvas.getWidth() * 0.07, hoverCanvas.getHeight() * 0.25);

				hcgc.setFont(CHR_FONT);
				String fleeDescription = "Focus attack on a single target";
				hcgc.fillText(fleeDescription, hoverCanvas.getWidth() * 0.07, hoverCanvas.getHeight() * 0.45);
				
				hcgc.drawImage(img,hoverCanvas.getWidth() * 0.07, hoverCanvas.getHeight() * 0.65,70,70);
				hcgc.drawImage(imgM,hoverCanvas.getWidth() * 0.07, hoverCanvas.getHeight() * 0.65,70,70);
				
				hcgc.drawImage(img,hoverCanvas.getWidth() * 0.27, hoverCanvas.getHeight() * 0.65,70,70);
				hcgc.drawImage(img,hoverCanvas.getWidth() * 0.47, hoverCanvas.getHeight() * 0.65,70,70);
				b1.setOnMouseExited(new EventHandler<MouseEvent>() {

					public void handle(MouseEvent mE) {
						hcgc.setFill(Color.SANDYBROWN);
						hcgc.fillRect(0, 0, hoverCanvas.getWidth(), hoverCanvas.getHeight());
						hcgc.setStroke(Color.DARKSLATEGRAY);
						hcgc.setLineWidth(10);
						hcgc.strokeRect(0, 0, hoverCanvas.getWidth(), hoverCanvas.getHeight());

					}
				});

			}
		});
		
		
		b2.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (m2.isDead() ) return;
				c1.attack(m2);  attackSound.play();
				fightStage.close();
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				updateBattle(c1,m1,m2,m3,background);
				
				if (m1.isDead() && m2.isDead() && m3.isDead()) {
					
					rewardScene();
					window.setTitle("Reward Scene");
				}
				
				
				if (!m1.isDead()) m1.attack(c1);updateBattle(c1,m1,m2,m3,background);
			
				if (!m2.isDead()) m2.attack(c1);updateBattle(c1,m1,m2,m3,background);
			
				
				if (!m3.isDead()) m3.attack(c1);updateBattle(c1,m1,m2,m3,background);
				
				if (c1.isDead()) {
					gameOverScene();
				}
			}
		});
		
		b2.setOnMouseEntered(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent mE) {
				// TODO Auto-generated method stub
				GraphicsContext hcgc = hoverCanvas.getGraphicsContext2D();
				hcgc.setFont(ACTION_FONT);
				
				String str = "/battleBackground/monsterIcon.png";
				InputStream inp  = getClass().getResourceAsStream(str);
				Image img = new Image(inp);
				
				String strM = "/battleBackground/mark.png";
				InputStream inpM  = getClass().getResourceAsStream(strM);
				Image imgM = new Image(inpM);
				 
				
				hcgc.setFill(Color.LIGHTYELLOW);
				hcgc.fillText("Normal Attack", hoverCanvas.getWidth() * 0.07, hoverCanvas.getHeight() * 0.25);

				hcgc.setFont(CHR_FONT);
				String fleeDescription = "Focus attack on a single target";
				hcgc.fillText(fleeDescription, hoverCanvas.getWidth() * 0.07, hoverCanvas.getHeight() * 0.45);
				
				hcgc.drawImage(img,hoverCanvas.getWidth() * 0.07, hoverCanvas.getHeight() * 0.65,70,70);
				hcgc.drawImage(img,hoverCanvas.getWidth() * 0.27, hoverCanvas.getHeight() * 0.65,70,70);
				hcgc.drawImage(img,hoverCanvas.getWidth() * 0.47, hoverCanvas.getHeight() * 0.65,70,70);
				hcgc.drawImage(imgM,hoverCanvas.getWidth() * 0.27, hoverCanvas.getHeight() * 0.65,70,70);
				
				
				b2.setOnMouseExited(new EventHandler<MouseEvent>() {

					public void handle(MouseEvent mE) {
						hcgc.setFill(Color.SANDYBROWN);
						hcgc.fillRect(0, 0, hoverCanvas.getWidth(), hoverCanvas.getHeight());
						hcgc.setStroke(Color.DARKSLATEGRAY);
						hcgc.setLineWidth(10);
						hcgc.strokeRect(0, 0, hoverCanvas.getWidth(), hoverCanvas.getHeight());

					}
				});

			}
		});
		
		b3.setOnAction(new EventHandler<ActionEvent>()  {
			public void handle(ActionEvent event)  {
				if (m3.isDead() ) return;		
		
				c1.attack(m3); attackSound.play();
				fightStage.close();
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				updateBattle(c1,m1,m2,m3,background);
				
				
			
				if (m1.isDead() && m2.isDead() && m3.isDead()) {
					
					rewardScene();
					window.setTitle("Reward Scene");
				}
				

				if (!m1.isDead()) m1.attack(c1);updateBattle(c1,m1,m2,m3,background);
		
				if (!m2.isDead()) m2.attack(c1);updateBattle(c1,m1,m2,m3,background);
				
				if (!m3.isDead()) m3.attack(c1);updateBattle(c1,m1,m2,m3,background);
				
				if (c1.isDead()) {
					gameOverScene();
				}
				
			}
		});
		
		b3.setOnMouseEntered(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent mE) {
				// TODO Auto-generated method stub
				GraphicsContext hcgc = hoverCanvas.getGraphicsContext2D();
				hcgc.setFont(ACTION_FONT);
				
				String str = "/battleBackground/monsterIcon.png";
				InputStream inp  = getClass().getResourceAsStream(str);
				Image img = new Image(inp);
				
				String strM = "/battleBackground/mark.png";
				InputStream inpM  = getClass().getResourceAsStream(strM);
				Image imgM = new Image(inpM);
				 
				
				hcgc.setFill(Color.LIGHTYELLOW);
				hcgc.fillText("Normal Attack", hoverCanvas.getWidth() * 0.07, hoverCanvas.getHeight() * 0.25);

				hcgc.setFont(CHR_FONT);
				String fleeDescription = "Focus attack on a single target";
				hcgc.fillText(fleeDescription, hoverCanvas.getWidth() * 0.07, hoverCanvas.getHeight() * 0.45);
				
				hcgc.drawImage(img,hoverCanvas.getWidth() * 0.07, hoverCanvas.getHeight() * 0.65,70,70);
				hcgc.drawImage(img,hoverCanvas.getWidth() * 0.27, hoverCanvas.getHeight() * 0.65,70,70);
				hcgc.drawImage(img,hoverCanvas.getWidth() * 0.47, hoverCanvas.getHeight() * 0.65,70,70);
				hcgc.drawImage(imgM,hoverCanvas.getWidth() * 0.47, hoverCanvas.getHeight() * 0.65,70,70);
				
				b3.setOnMouseExited(new EventHandler<MouseEvent>() {

					public void handle(MouseEvent mE) {
						hcgc.setFill(Color.SANDYBROWN);
						hcgc.fillRect(0, 0, hoverCanvas.getWidth(), hoverCanvas.getHeight());
						hcgc.setStroke(Color.DARKSLATEGRAY);
						hcgc.setLineWidth(10);
						hcgc.strokeRect(0, 0, hoverCanvas.getWidth(), hoverCanvas.getHeight());

					}
				});

			}
		});
		
		
		
		splashAttack.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				
				int dmg = (int) (c1.getAtk()/1.5);
				m1.decreaseHp(dmg);
				m2.decreaseHp(dmg);
				m3.decreaseHp(dmg);  attackSound.play();
				fightStage.close();
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				updateBattle(c1,m1,m2,m3,background);
				
				
				
				if (m1.isDead() && m2.isDead() && m3.isDead()) {
				
					rewardScene();
					window.setTitle("Reward Scene");
				}	
				
				
				if (!m1.isDead()) m1.attack(c1);
				updateBattle(c1,m1,m2,m3,background);
				if (!m2.isDead()) m2.attack(c1);
				updateBattle(c1,m1,m2,m3,background);
				if (!m3.isDead()) m3.attack(c1);
				updateBattle(c1,m1,m2,m3,background);
				
				if (c1.isDead()) {
					gameOverScene();
				}
			
			}
			
		});
		
		splashAttack.setOnMouseEntered(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent mE) {
				// TODO Auto-generated method stub
				GraphicsContext hcgc = hoverCanvas.getGraphicsContext2D();
				hcgc.setFont(ACTION_FONT);
				
				String str = "/battleBackground/monsterIcon.png";
				InputStream inp  = getClass().getResourceAsStream(str);
				Image img = new Image(inp);
				
				String strM = "/battleBackground/mark.png";
				InputStream inpM  = getClass().getResourceAsStream(strM);
				Image imgM = new Image(inpM);
				 
				
				hcgc.setFill(Color.LIGHTYELLOW);
				hcgc.fillText("Splash Attack", hoverCanvas.getWidth() * 0.07, hoverCanvas.getHeight() * 0.25);

				hcgc.setFont(CHR_FONT);
				String fleeDescription = "Hit all enemies with slightly lower damage.";
				hcgc.fillText(fleeDescription, hoverCanvas.getWidth() * 0.07, hoverCanvas.getHeight() * 0.45);
				
				hcgc.drawImage(img,hoverCanvas.getWidth() * 0.07, hoverCanvas.getHeight() * 0.65,70,70);
				hcgc.drawImage(img,hoverCanvas.getWidth() * 0.27, hoverCanvas.getHeight() * 0.65,70,70);
				hcgc.drawImage(img,hoverCanvas.getWidth() * 0.47, hoverCanvas.getHeight() * 0.65,70,70);
				
				hcgc.drawImage(imgM,hoverCanvas.getWidth() * 0.07, hoverCanvas.getHeight() * 0.65,70,70);
				hcgc.drawImage(imgM,hoverCanvas.getWidth() * 0.27, hoverCanvas.getHeight() * 0.65,70,70);
				hcgc.drawImage(imgM,hoverCanvas.getWidth() * 0.47, hoverCanvas.getHeight() * 0.65,70,70);
				
				splashAttack.setOnMouseExited(new EventHandler<MouseEvent>() {

					public void handle(MouseEvent mE) {
						hcgc.setFill(Color.SANDYBROWN);
						hcgc.fillRect(0, 0, hoverCanvas.getWidth(), hoverCanvas.getHeight());
						hcgc.setStroke(Color.DARKSLATEGRAY);
						hcgc.setLineWidth(10);
						hcgc.strokeRect(0, 0, hoverCanvas.getWidth(), hoverCanvas.getHeight());

					}
				});

			}
		});
		
		back.setOnMouseEntered(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent mE) {
				// TODO Auto-generated method stub
				GraphicsContext hcgc = hoverCanvas.getGraphicsContext2D();
				hcgc.setFont(ACTION_FONT);

				hcgc.setFill(Color.LIGHTYELLOW);
				hcgc.fillText("Back", hoverCanvas.getWidth() * 0.07, hoverCanvas.getHeight() * 0.25);

				hcgc.setFont(CHR_FONT);
				String fleeDescription = "Go back to previous menu.";
				hcgc.fillText(fleeDescription, hoverCanvas.getWidth() * 0.07, hoverCanvas.getHeight() * 0.45);

				back.setOnMouseExited(new EventHandler<MouseEvent>() {

					public void handle(MouseEvent mE) {
						hcgc.setFill(Color.SANDYBROWN);
						hcgc.fillRect(0, 0, hoverCanvas.getWidth(), hoverCanvas.getHeight());
						hcgc.setStroke(Color.DARKSLATEGRAY);
						hcgc.setLineWidth(10);
						hcgc.strokeRect(0, 0, hoverCanvas.getWidth(), hoverCanvas.getHeight());

					}
				});

			}
		});

		
		back.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				fightStage.close();
				
			}
		});
	}
	
	
	public void startScene() throws InterruptedException {

		BorderPane root = new BorderPane();
		Scene startScene = new Scene(root, screenWidth, screenHeight);
		Canvas startCanvas = new Canvas(screenWidth,screenHeight);
		GraphicsContext gc = startCanvas.getGraphicsContext2D();
		
		String str = "/sceneImage/startImage.png";
		InputStream inp  = getClass().getResourceAsStream(str);
		Image img = new Image(inp);
		
		gc.drawImage(img, 0, 0,screenWidth,screenHeight);
		root.getChildren().add(startCanvas);
		
		window.setScene(startScene);
		window.setTitle("Small Town");
		healthPotion=0;
		stageNumber =1;

		
		
		startScene.setOnKeyPressed((KeyEvent e) -> {	
				
			try {
				introScene();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
				
		

		});
	
}
	public void introScene() throws InterruptedException {

		BorderPane root = new BorderPane();
		Scene introScene = new Scene(root, screenWidth, screenHeight);
	
		
		MediaPlayer player = new MediaPlayer( new Media(getClass().getResource("/sceneImage/intro.mp4").toExternalForm()));
		MediaView mediaView = new MediaView(player);
		root.getChildren().add(mediaView);
		player.play();
		mediaView.setFitWidth(screenWidth);
		window.setScene(introScene);
		window.setTitle("Intro");
		
		player.setOnEndOfMedia(new Runnable() {
			

			@Override
			public void run() {
				try {
					characterCreationScene();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
			

		
		introScene.setOnKeyPressed((KeyEvent e) -> {	
			
		if (e.getCode()==KeyCode.ENTER) {
				try {
					player.stop();
					characterCreationScene();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		}

		});


}
	public void characterCreationScene() throws InterruptedException {
		
		isBoy = true;
		StackPane root = new StackPane();
		
		Scene characterCreationScene = new Scene(root, screenWidth, screenHeight);
		
		Canvas bgCanvas = new Canvas(screenWidth, screenHeight);
		GraphicsContext bgc = bgCanvas.getGraphicsContext2D();
		bgc.setFill(Color.MOCCASIN);
		bgc.fillRect(0, 0, screenWidth,screenHeight);
		root.getChildren().add(bgCanvas);
		
		String strP ="/characterImage/boyP.png";
		InputStream inpP  = getClass().getResourceAsStream(strP);
		Image imgP = new Image(inpP);
		
		String strC ="/characterImage/boy.png";
		InputStream inpC  = getClass().getResourceAsStream(strC);
		Image imgC = new Image(inpC);
		
		VBox vbox = new VBox();
		
		vbox.setPadding(new Insets(20));
		Canvas pCanvas = new Canvas(screenWidth*0.3,screenHeight*0.7);
		GraphicsContext pgc = pCanvas.getGraphicsContext2D();
		
		pgc.drawImage(imgP,0,0,pCanvas.getWidth()*0.4,pCanvas.getHeight()*0.3);
		pgc.drawImage(imgC,0,pCanvas.getHeight()*0.4,pCanvas.getWidth()*0.5, pCanvas.getHeight()*0.6);
		
		Button boy = new Button("Boy");
		Button girl = new Button("Girl");
		
		HBox hbox = new HBox();
		
		vbox.getChildren().add(pCanvas);
		HBox buttons = new HBox();
		buttons.setSpacing(5);
		buttons.getChildren().add(boy);
		buttons.getChildren().add(girl);
		
		boy.setPrefSize(pCanvas.getWidth()*0.2, pCanvas.getHeight()*0.1);
		girl.setPrefSize(pCanvas.getWidth()*0.2,  pCanvas.getHeight()*0.1);
		boy.setFont(CHR_FONT);
		girl.setFont(CHR_FONT);
		
		Label labelName = new Label ("Character Name :");
		TextField name = new TextField("Enter Name");
		
		labelName.setFont(CHR_FONT);
		name.setFont(CHR_FONT);
		labelName.setPrefSize(pCanvas.getWidth()*0.6, pCanvas.getHeight()*0.1);
		name.setPrefSize(pCanvas.getWidth()*0.6, pCanvas.getHeight()*0.1);
		hbox.setPadding(new Insets(100));
		Canvas b = new Canvas (screenWidth*0.5,screenHeight*0.1);
		hbox.getChildren().add(b);
		
		hbox.getChildren().add(labelName);
		hbox.getChildren().add(name);
		
		HBox hbox2 = new HBox();
		//vbox.getChildren().add(buttons);
		Canvas b2 = new Canvas (screenWidth*0.5,screenHeight*0.1);
		hbox2.getChildren().add(b2);
		
		Canvas nameCanvas = new Canvas(screenWidth*0.45,screenHeight*0.5);
		GraphicsContext ngc = nameCanvas.getGraphicsContext2D();
		ngc.setFill(Color.LIGHTBLUE);
		ngc.fillRect(0,0,nameCanvas.getWidth(),nameCanvas.getHeight());
		ngc.setStroke(Color.BLACK);
		ngc.setLineWidth(5);
		ngc.strokeRect(0,0,nameCanvas.getWidth(),nameCanvas.getHeight());
		
		Button confirmButton = new Button("Confirm");
		confirmButton.setPrefSize(pCanvas.getWidth()*0.8, pCanvas.getHeight()*0.1);
		confirmButton.setFont(CHR_FONT);
		
		ngc.setFill(Color.BLACK);
		ngc.setFont(Font.font("Helveltica", 20));
		ngc.fillText("Your name must not contain more than 10 characters.\n\n"
				
				+ "\nGender of your character is only for cosmetic purpose.\n\n"
				+ "To save this town you must defeat all 7 stages.\n"
				+ "The boss is waiting in the last stage. Be prepared.\n\n"
				
				+ "If you fail, You will have to start all over.\n\n"
				+ "Good luck!", 50, 50);
		
		HBox hc = new HBox();
		Canvas b3 = new Canvas (screenWidth*0.5,screenHeight*0.1);
		hc.getChildren().add(b3);
		hc.getChildren().add(confirmButton);
		
		VBox v2 = new VBox();
		v2.setSpacing(10);
		hbox2.getChildren().add(nameCanvas);
		v2.getChildren().add(hbox);
		v2.getChildren().add(hbox2);
		v2.getChildren().add(buttons);
		v2.getChildren().add(hc);
		root.getChildren().add(vbox);
		root.getChildren().add(v2);
		
		

		
		boy.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				String strP ="/characterImage/boyP.png";
				InputStream inpP  = getClass().getResourceAsStream(strP);
				Image imgP = new Image(inpP);
				
				String strC ="/characterImage/boy.png";
				InputStream inpC  = getClass().getResourceAsStream(strC);
				Image imgC = new Image(inpC);
				pgc.setFill(Color.MOCCASIN);
				pgc.fillRect(0, 0, pCanvas.getWidth(), pCanvas.getHeight());
				pgc.drawImage(imgP,0,0,pCanvas.getWidth()*0.4,pCanvas.getHeight()*0.3);
				pgc.drawImage(imgC,0,pCanvas.getHeight()*0.4,pCanvas.getWidth()*0.5, pCanvas.getHeight()*0.6);
				isBoy = true;
			}
		});
		
		girl.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				String strP ="/characterImage/girlP.png";
				InputStream inpP  = getClass().getResourceAsStream(strP);
				Image imgP = new Image(inpP);
				
				String strC ="/characterImage/girl.png";
				InputStream inpC  = getClass().getResourceAsStream(strC);
				Image imgC = new Image(inpC);
				pgc.setFill(Color.MOCCASIN);
				pgc.fillRect(0, 0, pCanvas.getWidth(), pCanvas.getHeight());
				pgc.drawImage(imgP,0,0,pCanvas.getWidth()*0.4,pCanvas.getHeight()*0.3);
				pgc.drawImage(imgC,0,pCanvas.getHeight()*0.4,pCanvas.getWidth()*0.5, pCanvas.getHeight()*0.6);
				isBoy = false;
			}
		});
		
		confirmButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				try {
					String s = name.getText();
					if (s.length()>10) throw new nameTooLongException();
				} catch (nameTooLongException e) {
					// TODO Auto-generated catch block
					Alert errorAlert = 	 new Alert(AlertType.ERROR);
					errorAlert.setHeaderText("Character name not valid");
					errorAlert.setContentText(e.getError());
					errorAlert.showAndWait();
					return;
				}
				
				//c1 = new Character(name.getText(),100,5,isBoy);
				c1 = new Character(name.getText(),100,15,isBoy);
				
				stage1();
			}
		});
		
		
		window.setScene(characterCreationScene);
		window.setTitle("CharacterCreation");
		
		

}
	
	public void stage1() {
		
		Monster m1 = new Monster("eggMan", 16, 1);
		Monster m2 = new Monster("eggMan", 21, 1);
		Monster m3 = new Monster("eggMan", 14, 1);
		
	
		initializeBattleScene("caveBackground");
		updateBattle(c1, m1, m2, m3,"caveBackground");		
		
	}
	
	public void stage2() {
		Monster m1 = new Monster("stingy", 15, 3);
		Monster m2 = new Monster("eggMan", 20, 3);
		Monster m3 = new Monster("eggMan", 15, 3);
		
	
		initializeBattleScene("caveBackground");
		updateBattle(c1, m1, m2, m3,"caveBackground");	
		
	}
	
	public void stage3() {
		Monster m1 = new Monster("stingy", 12, 4);
		Monster m2 = new Monster("eggMan", 15, 2);
		Monster m3 = new Monster("stingy", 12, 4);
		
	
		initializeBattleScene("darkForest");
		updateBattle(c1, m1, m2, m3,"darkForest");	
		
	}
	
	public void stage4() {
		Monster m1 = new Monster("stingy", 20, 2);
		Monster m2 = new Monster("stingy", 20, 4);
		Monster m3 = new Monster("stingy", 20, 2);
		
	
		initializeBattleScene("darkForest");
		updateBattle(c1, m1, m2, m3,"darkForest");	
		
	}
	
	public void stage5() {
		Monster m1 = new Monster("slime", 25, 3);
		Monster m2 = new Monster("stingy", 30, 3);
		Monster m3 = new Monster("slime", 18, 4);
		
	
		initializeBattleScene("beach");
		updateBattle(c1, m1, m2, m3,"beach");	
		
	}
	
	public void stage6() {
		Monster m1 = new Monster("stingy", 30, 3);
		Monster m2 = new Monster("slime", 30, 3);
		Monster m3 = new Monster("slime", 30, 2);
		
	
		initializeBattleScene("beach");
		updateBattle(c1, m1, m2, m3,"beach");	
		
	}
	
	public void stage7() {
		Random r = new Random();
		float rate = r.nextFloat();
		
		Monster m1 = new Monster("LeftCerberus", 50, 2+(int) (5*rate));
		Monster m2 = new Monster("MiddleCerberus", 50, 4);
		Monster m3 = new Monster("RightCerberus", 50, 2+(int) (5*(1-rate)));
		
	
		initializeBattleScene("skull");
		updateBattle(c1, m1, m2, m3,"skull");	
		
	}
	
	
	
	public void rewardScene() {
		
		
		
		StackPane root = new StackPane();
		Scene rewardScene = new Scene(root, screenWidth, screenHeight);
		VBox v = new VBox();
		v.setSpacing(screenHeight*0.05);
		Canvas bgCanvas = new Canvas(screenWidth, screenHeight);
		GraphicsContext bgc = bgCanvas.getGraphicsContext2D();
		bgc.setFill(Color.MOCCASIN);
		bgc.fillRect(0, 0, screenWidth,screenHeight);
		root.getChildren().add(bgCanvas);
		
		Canvas topCanvas = new Canvas(screenWidth,screenHeight*0.25);
		GraphicsContext tgc = topCanvas.getGraphicsContext2D();
		tgc.setFill(Color.LIGHTYELLOW);
		tgc.fillRect(0, 0, topCanvas.getWidth(), topCanvas.getHeight());
		tgc.setStroke(Color.BROWN);tgc.setLineWidth(10);
		tgc.strokeRect(0, 0, topCanvas.getWidth(), topCanvas.getHeight());
		
		tgc.setFont(ACTION_FONT);
		tgc.setFill(Color.CORNFLOWERBLUE);
		tgc.fillText("You Won Stage "+stageNumber,topCanvas.getWidth()/2.3, topCanvas.getHeight()*0.3);
		tgc.fillText("Choose your reward wisely.",topCanvas.getWidth()/2.6, topCanvas.getHeight()*0.6);
		stageNumber++;
		
		Button b1 = new Button("Full Heal");
		Button b2 = new Button("Increase Attack");
		Button b3 = new Button("Health Potion");
		
		b1.setPrefSize(screenWidth*0.2, screenHeight*0.2);b1.setFont(ACTION_FONT);
		b2.setPrefSize(screenWidth*0.2, screenHeight*0.2);b2.setFont(ACTION_FONT);
		b3.setPrefSize(screenWidth*0.2, screenHeight*0.2);b3.setFont(ACTION_FONT);
		
		HBox buttons = new HBox();
		buttons.setSpacing(screenWidth*0.020);
		
		Canvas b = new Canvas(screenWidth*0.2,screenHeight*0.2);
		
		buttons.getChildren().add(b);
		buttons.getChildren().addAll(b1,b2,b3);
		
		Canvas bottomCanvas = new Canvas(screenWidth,screenHeight*0.4);
		GraphicsContext bottomGc = bottomCanvas.getGraphicsContext2D();
		bottomGc.setStroke(Color.BLACK);
		bottomGc.setLineWidth(10);
		bottomGc.setFill(Color.LIGHTBLUE);
		bottomGc.fillRect(0, 0, bottomCanvas.getWidth(), bottomCanvas.getHeight());
		bottomGc.strokeRect(0, 0, bottomCanvas.getWidth(), bottomCanvas.getHeight());
		v.getChildren().add(topCanvas);
		v.getChildren().add(buttons);
		v.getChildren().add(bottomCanvas);
		root.getChildren().add(v);
		
		b1.setOnMouseEntered(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent mE) {
				// TODO Auto-generated method stub
				bottomGc.setFill(Color.DARKBLUE);
				bottomGc.setFont(ACTION_FONT);
				
				String text = "Full Heal\n\n"
						+ "Your health doesn't restore itself after each stage.\n"
						+ "Choose this option to restore it to full health.\n"
						+ "Also increase your max HP by 20%.\n"
						+ "Current HP: "+c1.getHp()+" / "+c1.getMaxHp();
						
				
				bottomGc.fillText(text, bottomCanvas.getWidth()*0.1, bottomCanvas.getHeight()*0.2);
					

			b1.setOnMouseExited(new EventHandler<MouseEvent>() {

					public void handle(MouseEvent mE) {
						bottomGc.setStroke(Color.BLACK);
						bottomGc.setLineWidth(10);
						bottomGc.setFill(Color.LIGHTBLUE);
						bottomGc.fillRect(0, 0, bottomCanvas.getWidth(), bottomCanvas.getHeight());
						bottomGc.strokeRect(0, 0, bottomCanvas.getWidth(), bottomCanvas.getHeight());

					}
				});

			}
		});
		
		b1.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				int m = c1.getMaxHp();
				c1.setMaxHp((int) (m*1.2));
				c1.setHp(c1.getMaxHp());
				if (stageNumber==2) stage2();
				if (stageNumber==3) stage3();
				if (stageNumber==4) stage4();
				if (stageNumber==5) stage5();
				if (stageNumber==6) stage6();
				if (stageNumber==7) stage7();
				
 			}
		});
		
		b2.setOnMouseEntered(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent mE) {
				// TODO Auto-generated method stub
				bottomGc.setFill(Color.DARKBLUE);
				bottomGc.setFont(ACTION_FONT);
				
				String text = "Increase Attack\n\n"
						+ "Become stronger. Deal more damage.\n"
						+ "Choose this option to increase your attack by 3.\n"
						+ "Current Attack: "+c1.getAtk();
				
				bottomGc.fillText(text, bottomCanvas.getWidth()*0.1, bottomCanvas.getHeight()*0.2);
					

			b2.setOnMouseExited(new EventHandler<MouseEvent>() {

					public void handle(MouseEvent mE) {
						bottomGc.setStroke(Color.BLACK);
						bottomGc.setLineWidth(10);
						bottomGc.setFill(Color.LIGHTBLUE);
						bottomGc.fillRect(0, 0, bottomCanvas.getWidth(), bottomCanvas.getHeight());
						bottomGc.strokeRect(0, 0, bottomCanvas.getWidth(), bottomCanvas.getHeight());

					}
				});

			}
		});
		
		b2.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				int a = c1.getAtk();
				c1.setAtk(a+3);
				if (stageNumber==2) stage2();
				if (stageNumber==3) stage3();
				if (stageNumber==4) stage4();
				if (stageNumber==5) stage5();
				if (stageNumber==6) stage6();
				if (stageNumber==7) stage7();
 			}
		});
		
		b3.setOnMouseEntered(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent mE) {
				// TODO Auto-generated method stub
				bottomGc.setFill(Color.DARKBLUE);
				bottomGc.setFont(ACTION_FONT);
				
				String text = "Health Potion\n\n"
						+ "A health potion can be used to restore 75% of your max HP.\n"
						+ "It can be used during fights without losing a turn.\n"
						+ "Choose this option to get a health potion.\n"
						+ "Current Amount of Health Potion(s): " + healthPotion;
				
				bottomGc.fillText(text, bottomCanvas.getWidth()*0.1, bottomCanvas.getHeight()*0.2);
					

			b3.setOnMouseExited(new EventHandler<MouseEvent>() {

					public void handle(MouseEvent mE) {
						bottomGc.setStroke(Color.BLACK);
						bottomGc.setLineWidth(10);
						bottomGc.setFill(Color.LIGHTBLUE);
						bottomGc.fillRect(0, 0, bottomCanvas.getWidth(), bottomCanvas.getHeight());
						bottomGc.strokeRect(0, 0, bottomCanvas.getWidth(), bottomCanvas.getHeight());

					}
				});

			}
		});
		
		b3.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				healthPotion++;
				if (stageNumber==2) stage2();
				if (stageNumber==3) stage3();
				if (stageNumber==4) stage4();
				if (stageNumber==5) stage5();
				if (stageNumber==6) stage6();
				if (stageNumber==7) stage7();
 			}
		});
		
		window.setScene(rewardScene);
		window.setTitle("Reward Scene");
		if (stageNumber==8) endGameScene();
	}
	
	private void endGameScene() {
		BorderPane root = new BorderPane();
		Scene endGameScene = new Scene(root, screenWidth, screenHeight);
		Canvas startCanvas = new Canvas(screenWidth,screenHeight);
		GraphicsContext gc = startCanvas.getGraphicsContext2D();
		
		String str = "/sceneImage/endGame.png";
		InputStream inp  = getClass().getResourceAsStream(str);
		Image img = new Image(inp);
		
		gc.drawImage(img, 0, 0,screenWidth,screenHeight);
		root.getChildren().add(startCanvas);
		
		window.setScene(endGameScene);
		window.setTitle("Congratulations.");
		
		endGameScene.setOnKeyPressed((KeyEvent e) -> {	
			
			try {
				
				if( e.getCode()==KeyCode.ENTER) startScene();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	

	});
	}
	
	public void gameOverScene() {
		BorderPane root = new BorderPane();
		Scene startScene = new Scene(root, screenWidth, screenHeight);
		Canvas startCanvas = new Canvas(screenWidth,screenHeight);
		GraphicsContext gc = startCanvas.getGraphicsContext2D();
		
		String str = "/sceneImage/deadImage.png";
		InputStream inp  = getClass().getResourceAsStream(str);
		Image img = new Image(inp);
		
		gc.drawImage(img, 0, 0,screenWidth,screenHeight);
		root.getChildren().add(startCanvas);
		
		window.setScene(startScene);
		window.setTitle("Game Over");
		
		
		startScene.setOnKeyPressed((KeyEvent e) -> {	
			
				try {
					if(e.getCode()==KeyCode.ENTER) startScene();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		

		});
	}
}
