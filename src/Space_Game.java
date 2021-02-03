import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.scene.Cursor;
import java.awt.Robot;
import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.awt.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Sam Gershkovich - 000801766
 * @date January 21, 2020 (Last updated: January 24, 2020)
 */
public class Space_Game extends Application {

    //These variables need to be able to be accessed by everything
    double xIndex = 0;
    double yIndex = 0;
    double speed = 10;

    double mouseX = 0;
    double mouseY = 0;

    double cursorX = 0;
    double cursorY = 0;

    double prevX=0;
    double prevY=0;

    int screenX = 0;
    int screenY = 0;

    boolean paused = false;
    boolean gameOver = false;

    int health = 100;
    double score = 0;

    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

    int midX = dim.width/2;
    int midY = dim.height/2 + 20;

    double backX = 0;
    double backY = 0;

    double currAngle = 0;

    String playerName;

    boolean scoreSaved = false;

    MediaPlayer soundTrack;

    MediaPlayer ambiance;

    MediaPlayer sounds;


    boolean startGame = false;

    boolean playAgainOn =  false;

    Stage myStage;

    boolean restart = false;


    TextField input = new TextField();
    ImageView background = new ImageView("images/cockpit.png");
    ImageView starBack = new ImageView("images/background.jpg");
    Label scoreLabel = new Label();

    Text leadText = new Text();

    Button start = new Button();
    Button quit = new Button();
    Button leaderboard = new Button();
    Button info = new Button();
    Button playAgain = new Button();

    public static void pause(int duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException ex) {
        }
    }

    /**
     * Launches the app
     *
     * @param args unused
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Sets up the stage and starts the main thread.
     *
     * @param stage The first stage
     */
    @Override
    public void start(Stage stage) {
        myStage = stage;
        stage.setTitle("Space Flight");
        Canvas canvas = new Canvas(1400, 920);
        Group root = new Group();
        Scene scene = new Scene(root, Color.BLACK);

        root.getChildren().addAll(canvas, input, start, quit, leaderboard, info, scoreLabel, leadText, playAgain);

        stage.setScene(scene);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        //Create and play the soundtrack
        String path = "Soundtrack.mp3";
        Media media = new Media(new File(path).toURI().toString());
        soundTrack = new MediaPlayer(media);
        soundTrack.setStartTime(Duration.seconds(0));
        soundTrack.setStopTime(media.getDuration());
        soundTrack.setCycleCount(MediaPlayer.INDEFINITE);
        soundTrack.setVolume(0.75);
        soundTrack.play();
        path = "ambience.mp3";
        media = new Media(new File(path).toURI().toString());
        ambiance = new MediaPlayer(media);
        ambiance.setStartTime(Duration.seconds(0));
        ambiance.setStopTime(media.getDuration());
        ambiance.setCycleCount(MediaPlayer.INDEFINITE);
        ambiance.play();

        input.relocate(630, 270);
        if(playerName != null){

        }
        else {
            input.setText("Pilot");
        }

        leaderboard.relocate(570, 340);
        info.relocate(570, 440);
        start.relocate(630, 692);
        quit.relocate(630, 792);
        playAgain.relocate(-2000, 0);
        playAgain.setScaleX(0.6);
        playAgain.setScaleY(0.7);

        leadText.setFont(Font.font(24));
        leadText.setFill(Color.rgb(60,100,228));

        input.setScaleX(1.6);
        input.setScaleY(2);

        start.setGraphic(new ImageView("images/launch.png"));
        start.setStyle("-fx-background-color: transparent; ");

        quit.setGraphic(new ImageView("images/quit.png"));
        quit.setStyle("-fx-background-color: transparent; ");

        leaderboard.setGraphic(new ImageView("images/leaderboard.png"));
        leaderboard.setStyle("-fx-background-color: transparent; ");

        info.setGraphic(new ImageView("images/info.png"));
        info.setStyle("-fx-background-color: transparent; ");

        playAgain.setGraphic(new ImageView("images/playAgain.png"));
        playAgain.setStyle("-fx-background-color: transparent; -fx-opacity: 0;");

        start.setOnMouseEntered(e -> start.setGraphic(new ImageView("images/launch-hover.png")));
        start.setOnMouseExited(e -> start.setGraphic(new ImageView("images/launch.png")));
        start.setOnMousePressed(e -> fade(start, 1, 0.6, 50).play());
        start.setOnMouseReleased(e -> fade(start, 0.6, 1, 50).play());

        quit.setOnMouseEntered(e -> quit.setGraphic(new ImageView("images/quit-hover.png")));
        quit.setOnMouseExited(e -> quit.setGraphic(new ImageView("images/quit.png")));
        quit.setOnMousePressed(e -> fade(quit, 1, 0.6, 50).play());
        quit.setOnMouseReleased(e -> fade(quit, 0.6, 1, 50).play());

        info.setOnMouseEntered(e -> info.setGraphic(new ImageView("images/info-hover.png")));
        info.setOnMouseExited(e -> info.setGraphic(new ImageView("images/info.png")));
        info.setOnMousePressed(e -> fade(info, 1, 0.6, 50).play());
        info.setOnMouseReleased(e -> fade(info, 0.6, 1, 50).play());

        leaderboard.setOnMouseEntered(e -> leaderboard.setGraphic(new ImageView("images/leaderboard-hover.png")));
        leaderboard.setOnMouseExited(e -> leaderboard.setGraphic(new ImageView("images/leaderboard.png")));
        leaderboard.setOnMousePressed(e -> fade(leaderboard, 1, 0.6, 50).play());
        leaderboard.setOnMouseReleased(e -> fade(leaderboard, 0.6, 1, 50).play());

       playAgain.setOnMouseEntered(e -> fade(playAgain, 0.5, 0.8, 100).play());
       playAgain.setOnMouseExited(e -> fade(playAgain, 0.8, 0.5, 100).play());
       playAgain.setOnMousePressed(e -> fade(playAgain, 0.8, 0.5, 50).play());
       playAgain.setOnMouseReleased(e -> fade(playAgain, 0.5, 0.8, 50).play());

        gc.drawImage(starBack.getImage(), 0, 0, 1400, 920);
        gc.drawImage(background.getImage(), 0 - canvas.getWidth() / 2, 0 - canvas.getHeight()-220, 2800, 2280);

        leaderboard.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                fade(info, 1, 0, 150).play();
                fade(leaderboard, 1, 0, 150).play();
                fade(input, 1, 0, 150).play();
                try {
                    getScores(canvas);
                } catch (IOException e) {
                }
                fade(leadText, 0, 1, 150).play();
            }
        });

        start.setOnAction(new EventHandler<ActionEvent> (){
            @Override
            public void handle(ActionEvent event) {

                if (input.getText() == null) {

                } else {
                    startGame = true;
                    fade(start, 1, 0, 150).play();
                    fade(quit, 1, 0, 150).play();
                    fade(info, 1, 0, 150).play();
                    fade(leaderboard, 1, 0, 150).play();
                    fade(input, 1, 0, 150).play();
                    fade(leadText, 1, 0, 150).play();
                    playerName = input.getText();

                    //Make the mouse invisible
                    scene.setCursor(Cursor.NONE);

                    gameOver = false;
                    Thread t = new Thread(() -> animate(gc, canvas, scene, canvas.getWidth(), canvas.getHeight()));
                    t.start();
                }
            }
        });

        quit.setOnAction(new EventHandler<ActionEvent> (){
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Done");
                System.exit(0);
            }
        });

        playAgain.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                restart = true;
            }
        });
        stage.show();
    }

    public Transition fade(Node node, double from, double to, double dur){
        FadeTransition fade = new FadeTransition(Duration.millis(dur), node);
        fade.setFromValue(from);
        fade.setToValue(to);
        fade.setCycleCount(1);
        return fade;
    }

    /**
     * Animation thread..
     *
     * @param gc     The drawing surface
     * @param width  the width of the canvas
     * @param height the height of the canvas
     */
    public void animate(GraphicsContext gc, Canvas canvas, Scene scene, double width, double height) {

        System.out.println("Starting");

        start.relocate(-2000,0);
        quit.relocate(-2000,0);
        leaderboard.relocate(-2000,0);
        info.relocate(-2000,0);
        input.relocate(-2000,0);

        playAgainOn = false;


        Media[] soundEffects = new Media[3];
        for(int i = 0; i<3; i++){
            String path = "crackSound" + (i+1) + ".wav";
            soundEffects[i] = new Media(new File(path).toURI().toString());
        }





        //store the mouse position when moved
        scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                screenX = (int)event.getScreenX();
                screenY = (int)event.getScreenY();
                cursorX = (int)event.getSceneX();
                cursorY = (int)event.getSceneY();
                mouseX = width/2;
                mouseY = height/2+43;

                if(!paused && !gameOver) {

                    //Lock the mouse position so that it remains within the bounds of the Crosshair image
                    try {
                        if (screenX < midX - 135 || screenX > midX + 135 || screenY < midY - 122 || screenY > midY + 122) {
                            new Robot().mouseMove((int) prevX, (int) prevY);
                        } else {
                            prevX = screenX;
                            prevY = screenY;
                        }
                    } catch (AWTException e) {
                    }
                }
                if(!gameOver) {
                    xIndex = (mouseX - event.getSceneX()) / 8;
                    yIndex = (mouseY - event.getSceneY()) / 8;
                }
            }
        });

        //Check if certain keys are pressed
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            //if escape pressed, quit the application
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE) {

                    if(!scoreSaved) {
                        saveScore();
                    }
                    System.out.println("Done");
                    System.exit(0);
                }
                if (event.getCode() == KeyCode.ENTER) {
                    if(gameOver) {
                        restart = true;
                    }
                }
            }
        });

        int numAsteroids = 15;

        //Create array of Star objects
        Asteroid[] asteroids = new Asteroid[numAsteroids];
        for (int i = 0; i < numAsteroids; i++) {
            asteroids[i] = new Asteroid(width, height, gc, canvas);
        }

        int numSpecks = 25;

        //Create array of Star objects
        Speck[] specks = new Speck[numSpecks];
        for (int i = 0; i < numSpecks; i++) {
            specks[i] = new Speck(width, height, gc, canvas);
        }

        int numCracks = 0;
        //Create array of cracks objects
        Crack[] cracks = new Crack[5];
        for (int i = 0; i < 5; i++) {
            cracks[i] = new Crack(gc);
        }

        //Initialize the images to be used
        ImageView cockpit = new ImageView("images/cockpit.png");
        Image cursor = new Image("images/Cursor.png");

        Image[] back = new Image[9];
        for(int i = 0; i < 9; i++){
            back[i] = new Image("images/background.jpg");
        }



        HealthBar healthBar = new HealthBar(gc, width, height);

        double initX = -width / 2;
        double initY = -height-220;
        double initW = 2800;
        double initH = 2280;
        double toX = -10;
        double toY = -10;
        double toW = 1420;
        double toH = 1140;
        double currX = initX;
        double currY = initY;
        double currW = initW;
        double currH = initH;

        for(int i = 0; i < 50; i ++)
        {
            gc.clearRect(0, 0, width, height);
            rotateBack(gc, width, height, back, specks, asteroids, numSpecks, 0, xIndex);

            currX = currX + (toX - currX)/10;
            currY = currY + (toY - currY)/10;
            currW = currW + (toW - currW)/10;
            currH = currH + (toH - currH)/10;

            gc.drawImage(cockpit.getImage(), currX, currY, currW, currH);
            pause(16);
        }

        //When game starts set mouse to center
        try {
            new Robot().mouseMove(midX, midY);

        } catch (AWTException e) {
        }

        //Main animation loop
        while (!restart) {
            gc.clearRect(0, 0, width, height);

            healthBar.startUp();



            rotateBack(gc, width, height, back, specks, asteroids, numSpecks, numAsteroids, xIndex);

            for (int i = 0; i < numAsteroids; i++) {
                if (!gameOver) {
                    if (asteroids[i].checkHit()) {
                        int soundNum = (int)(Math.random()*soundEffects.length);
                        sounds = new MediaPlayer(soundEffects[soundNum]);
                        sounds.setStartTime(Duration.seconds(0));
                        sounds.setStopTime(soundEffects[soundNum].getDuration());
                        sounds.setCycleCount(1);
                        sounds.play();
                        numCracks++;
                        if(numCracks < cracks.length) {
                            cracks[numCracks - 1].setSize(asteroids[i].getSize());
                            cracks[numCracks - 1].setX(asteroids[i].getHitChords()[0]);
                            cracks[numCracks - 1].setY(asteroids[i].getHitChords()[1]);
                        }

                        for (int j = 0; j < 16; j++) {
                            gc.setFill(Color.rgb(150, 10, 10, 0.03));
                            gc.fillRect(0, 0, width, height);
                        }
                        health -= 20;
                    }
                }
            }
            for (int i = 0; i < numCracks; i++) {
                cracks[i].draw(xIndex / 2, yIndex / 2);
            }

            gc.drawImage(cockpit.getImage(), -10 + xIndex / 2, -10 + yIndex / 2, width + 20, height + 220);//Draw the cockpit on the whole canvas
            gc.drawImage(cursor, cursorX - cursor.getWidth() / 2, cursorY - cursor.getHeight() / 2);//Draw the cursor at the mouse position

            healthBar.update(health, xIndex, yIndex);

            if (health <= 0) {
                health = 0;
                gameOver = true;
                if (speed >= 0) {
                    speed /= 1.01;
                    if (speed <= 0) {
                        speed = 0;
                    }
                }
                xIndex /= 1.01;
                yIndex /= 1.01;

                gc.setFill(Color.rgb(255, 255, 255, 0.2));
                gc.fillRect(0, 0, width, height);

                Text gameOverText = new Text("Game Over!");
                gameOverText.setFont(Font.font(100));
                gc.setFont(Font.font(100));
                gc.setFill(Color.RED);
                gc.fillText(gameOverText.getText(), width / 2 - gameOverText.getLayoutBounds().getWidth()/2, height * 0.2);

                if (!scoreSaved) {
                    saveScore();
                }
            }
/*
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font(12));

            gc.fillText("x: "+cursorX, cursorX, cursorY);
            gc.fillText("y: "+cursorY, cursorX, cursorY+10);
*/
            gc.setFill(Color.rgb(60,100,228));
            gc.setFont(Font.font(24));
            gc.fillText("Score", width/2-220 + xIndex/2, height/2+280 +yIndex/2);

            if (!gameOver) {
                score += 0.1;
            }

            double numLength = (int)Math.log10((int)score) + 1;

            Text scoreText = new Text("" + (int)score);

            scoreText.setFill(Color.rgb(60,100,228));
            scoreText.setFont(Font.font(200 /(numLength)));

            gc.setFont(Font.font(200 /(numLength)));

            gc.fillText(scoreText.getText(), 390 + xIndex/2 + scoreText.getLayoutBounds().getWidth()/2,820 + scoreText.getLayoutBounds().getHeight()/4 + yIndex/2);

            if(gameOver){
                if(!playAgainOn) {
                    playAgain.relocate(620, 800);
                    fade(playAgain, 0, 0.6, 150).play();
                    playAgainOn = true;
                }
                scene.setCursor(Cursor.DEFAULT);
            }

            //speed += 0.005;
            pause(16);//60 fps

        }
        Platform.runLater(()->{
            restart(scene);
            start(myStage);
        });
    }

    private void rotate(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }

    private void rotateBack(GraphicsContext gc, double width, double height, Image[] back, Speck[] specks, Asteroid[] asteroids, int numSpecks, int numAsteroids, double angle) {
        gc.save(); // saves the current state on stack, including the current transform
        angle*= 1.75;
        if(currAngle < angle)
        {
            currAngle += (angle-currAngle)/15;
        }
        if(currAngle > angle)
        {
            currAngle -= (currAngle-angle)/15;
        }

        rotate(gc, currAngle, width/2, height/2);

        //move the background in sync with the players movement,
        backX += xIndex;
        backY += yIndex;

        //simulate an infinite background
        if (backX >= width) {
            backX = width * -1;
        } else if (backX + width <= 0) {
            backX = width;
        }
        if (backY >= height) {
            backY = height * -1;
        } else if (backY + height <= 0) {
            backY = height;
        }

        //draw the background images
        gc.drawImage(back[0], backX, backY, width, height);
        gc.drawImage(back[1], backX, backY + height, width, height);
        gc.drawImage(back[2], backX, backY - height, width, height);
        gc.drawImage(back[3], backX + width, backY, width, height);
        gc.drawImage(back[4], backX + width, backY + height, width, height);
        gc.drawImage(back[5], backX + width, backY - height, width, height);
        gc.drawImage(back[6], backX - width, backY, width, height);
        gc.drawImage(back[7], backX - width, backY + height, width, height);
        gc.drawImage(back[8], backX - width, backY - height, width, height);

        //Loop through every speck in the array, for each speck, update it(update position and redraw it)
        for (int i = 0; i < numSpecks; i++) {
            specks[i].UpdateStar(xIndex, yIndex, speed);
        }

       /** Asteroid[] temp = sortAsteroids(asteroids);*/

        //Loop through every asteroid in the array, for each asteroid, Update it(update position and redraw it)
        for (int i = 0; i < numAsteroids; i++) {
            asteroids[i].UpdateAsteroid(xIndex, yIndex, speed / 50);
        }

        gc.restore(); // back to original state (before rotation)
    }

    private Asteroid[] sortAsteroids(Asteroid[] asteroids){

        //create an int array to store the sorted values
        Asteroid[] sorted = new Asteroid [asteroids.length];

        //copy each cards value into the array
        for(int i = 0; i < asteroids.length; i++){
            sorted[i] = asteroids[i];
        }
        //sort the array
        for(int i = 1; i < sorted.length; i++){
            for (int j = 0; j < sorted.length; j++){
                if(sorted[j].getSize() > sorted[i].getSize()){
                    Asteroid temp = sorted[i];
                    sorted[i] = sorted[j];
                    sorted[j] = temp;
                }
            }
        }
        return sorted;
    }

    public void restart(Scene scene) {
        gameOver = false;
        speed = 5;
        health = 100;
        score = 0;
        scoreSaved = false;

        fade(start, 0, 1, 150).play();
        fade(quit, 0, 1, 150).play();
        fade(info, 0, 1, 150).play();
        fade(leaderboard, 0, 1, 150).play();
        fade(input, 0, 1, 150).play();
        input.setText(playerName);
        leadText.setText(null);

        //Make the mouse visible
        scene.setCursor(Cursor.DEFAULT);

        soundTrack.stop();
        ambiance.stop();

        restart = false;

    }

    public void saveScore() {
        try {
            FileWriter fr = new FileWriter("scores.txt", true);
            fr.write(playerName + "," + (int)score + "\n");
            fr.close();
            System.out.println("Successfully wrote to the file.");
            scoreSaved = true;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void getScores(Canvas canvas) throws IOException {

        File temp = new File("scores.txt");

        if (temp.length() != 0) {

            List<String> result = Files.readAllLines(Paths.get("scores.txt"));
            Collections.sort(result, new Comparator<String>() {
                public int compare(String o1, String o2) {
                    String sub1 = o1.substring(o1.indexOf(',') + 1);
                    String sub2 = o2.substring(o2.indexOf(',') + 1);
                    int num1 = Integer.parseInt(sub1);
                    int num2 = Integer.parseInt(sub2);
                    return num2 - num1;
                }
            });

            int size = result.size();
            if(size>10)
            {
                size = 10;
            }
            for (int i = 0; i < size; i++) {
                String name = result.get(i).substring(0,result.get(i).indexOf(','));
                String playerScore = result.get(i).substring(result.get(i).indexOf(',') +1);
                leadText.setText(leadText.getText() + name + "  - - -  " + playerScore + "\n");
            }
        }
        else{
            leadText.setText("No scores yet!");
        }
        leadText.relocate(canvas.getWidth()/2 - leadText.getLayoutBounds().getWidth()/2, 270);

    }

    /**
     * Exits the app completely when the window is closed.
     */
    @Override
    public void stop() {
        System.exit(0);
    }
}
