import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Asteroid {

    private double x;
    private double y;
    private double z;

    private double size;
    private double diameter;
    private double distance;

    private double slope;
    private int quadrant;

    private double xIndex;
    private double yIndex;
    private double zIndex;

    private double speedMap;

    private double width;
    private double height;

    private boolean shipHit = false;

    private int collisionDistance = 400;

    private GraphicsContext gc;
    private Canvas canvas;
    private ImageView graphic;

    /**constructor gets the canvas width and height and generates a random starting position for the asteroid */
    public Asteroid(double canvasWidth, double canvasHeight, GraphicsContext gc, Canvas canvas){
        this.gc = gc;
        this.canvas = canvas;
        width = canvasWidth;
        height = canvasHeight;
        z = Math.random()*(((10 - (-100))+1)  + (-100));
        x = Math.random()* width;
        y =  Math.random()* height;
        graphic = new ImageView("images/asteroid1.gif");
        size = Math.random() * (500 - 400 + 1) + 400;
    }

    /** run all the update functions to calculate the new position, then update the position to the new position */
    void UpdateAsteroid(double xNum, double yNum, double zNum){

        zIndex += zNum;
        z += zIndex;

        CheckOnScreen();
        SetQuadrant();
        SetDistance();
        Parallax(xNum, yNum);
        SetSlope();
        SetDiameter();
        NextStep();
        Draw();
    }

    /** check if the asteroid is within the canvas bounds */
    void CheckOnScreen(){
        if((x - diameter/2) > width || (x + diameter/2) < 0 || (y - diameter/2) > height || (y + diameter/2) < 0){
            Respawn();
        }
    }

    /** calculate the current quadrant the asteroid is in */
    void SetQuadrant(){
        if(x <= width/2 && y <= height/2) {
            quadrant = 1;
        }
        else if (x >= width/2 && y <= height/2) {
            quadrant = 2;
        }
        else if(x <= width/2 && y >= height/2) {
            quadrant =  3;
        }
        else{
            quadrant = 4;
        }
    }

    /** calculate the distance from the asteroid, to the center of the screen */
    void SetDistance() {
        distance = (Math.pow((Math.pow(width/2 - x, 2) + Math.pow(height/2 - y, 2)), 0.5));
        speedMap = distance / collisionDistance;
    }

    /** set the parallax factor based on the users turn vector */
    void Parallax(double xNum, double yNum){
        if(xNum != xIndex) {
            x += xNum;
        }
        else{
            xIndex = xNum;
        }
        if(yNum != yIndex) {
            y += yNum;
        }
        else {
            yIndex = yNum;
        }
    }

    /** calculate the slope of the asteroids angle in relation to the center of the screen */
    void SetSlope() {
        slope = ((height/2) - y) / (x - (width/2));
    }

    /** set the size of the asteroid based on how close it is supposed to be to the player */
    void SetDiameter(){
        if(z <= 10) {
            diameter = 10;
        }
        else if(z >= size){
            diameter = size;
        }
        else {
            diameter = z;
        }
    }
    /** calculate the new position of the asteroid */
    void NextStep(){

        /** If the asteroid hit the ship, increase its speed 2 fold to simulate it bouncing off the ship*/
        if(shipHit){
            speedMap = 2;
        }

        /**i don't know how to concisely describe whats going on here :) */
        switch (quadrant){
            case 1:
            case 3:
                x -= 1 * (zIndex * speedMap);
                y += slope * (zIndex * speedMap);
                break;
            case 2:
            case 4:
                x += 1 * (zIndex * speedMap);
                y -= slope * (zIndex * speedMap);
                break;
        }
    }
    /**Draw the asteroid*/
    public void Draw() {

        //draw the asteroid
        gc.drawImage(graphic.getImage(), x - diameter/2, y - diameter/2, diameter, diameter);

/*
         //draw center point of asteroid
         gc.setFill(Color.RED);
         gc.fillOval(x-5, y-5, 10 , 10);

         //draw collision zone of ship
         gc.setStroke(Color.rgb(150,150,200,0.5));
         gc.strokeOval(width/2 - collisionDistance + xIndex/2, height/2 - 50 - collisionDistance + yIndex/2, collisionDistance*2, collisionDistance*2);

         gc.strokeRect(x-diameter/2, y-diameter/2, diameter, diameter);

         gc.setFont(Font.font(16));
         gc.fillText(""+ (int)z, x-diameter/2, y + diameter/2);

         //draw line indicator for asteroid
         gc.setStroke(Color.rgb((int)(255*(diameter/size)), 255 - (int)(255*(diameter/size)), 0));
         gc.strokeLine(x, y, width/2, height/2);

         //show the distance of the asteroid to the center of the screen
         gc.setFill(Color.WHITE);
         gc.setFont(Font.font(18));
         gc.fillText("" + distance, x, y+diameter/2);
*/
    }

    public boolean checkHit(){

        //if the asteroid is within the ships collision zone, and the asteroid is at full size(simulating it being directly in front of the ship)
        //return true and set shipHit to true
        if(distance < collisionDistance && diameter >= size && !shipHit){
            shipHit = true;
            return true;
        }
        else{
            return false;
        }
    }

    public double[] getHitChords(){
        double[] chords = {x,y};
        return chords;
    }

    public double getSize() {
        return size;
    }

    /** generate a new random position of the asteroid and reset its size */
    void Respawn(){
        z = Math.random()*(((10 - (-100))+1) + (-100));
        x = Math.random()* width;
        y =  Math.random()* height;
        size = Math.random() * (500 - 400 + 1) + 400;
        zIndex = 0;
        shipHit = false;
    }
}