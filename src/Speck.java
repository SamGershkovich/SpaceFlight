import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Speck {

    private double x;
    private double y;
    private double size;

    private double diameter;
    private double distance;
    private double slope;
    private int quadrant;

    private double xIndex;
    private double yIndex;
    private double speed;

    private double speedMap;

    private double width;
    private double height;

    GraphicsContext gc;
    Canvas canvas;


    //constructor gets the canvas width and height and generates a random staring position for the star
    public Speck(double canvasWidth, double canvasHeight, GraphicsContext gc, Canvas canvas){
        this.gc = gc;
        this.canvas = canvas;
        width = canvasWidth;
        height = canvasHeight;
        size = Math.random()*((0.1 - (-100))+1 + (-100));
        x = Math.random()*width;
        y = Math.random()*height;
    }

    //run all the update functions to calculate the new position, then update the position to the new position
    void UpdateStar(double xNum, double yNum, double speed){
        this.speed = speed;
        size += speed/10;

        SetQuadrant();
        SetDistance();
        Parallax(xNum, yNum);
        SetSlope();
        CheckOnScreen();
        SetDiameter();
        NextStep();
        Draw();
    }

    //set the parallax factor based on the users turn vector
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

    //calculate the new position of the star
    void NextStep(){
        switch (quadrant){
            case 1:
            case 3:
                x -= 1 * (speed * speedMap);
                y += slope * (speed * speedMap);
                break;
            case 2:
            case 4:
                x += 1 * (speed * speedMap);
                y -= slope * (speed * speedMap);
                break;
        }
    }

    //calculate the current quadrant the star is in
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

    //set the size of the star based on how close it is supposed to be to the player
    void SetDiameter(){
        if(size <= 0) {
            diameter=1;
        }
        if(size > 5){
            diameter = 5;
        }
        else {
            diameter = size;
        }
    }

    //calculate the distance from the star, to the center of the screen
    void SetDistance() {
        distance = (Math.pow((Math.pow(width/2 - x, 2) + Math.pow(height/2 - y, 2)), 0.5));
        speedMap = distance / (150);
    }

    //calculate the slope of the stars angle in relation to the center of the screen
    void SetSlope() {
        slope = ((height/2) - y) / (x - (width/2));
    }

    //check if the star is within the canvas bounds
    void CheckOnScreen(){
        if((x - 100) > width || (x + 100) < 0 || (y - 100) > height || (y + 100) < 0){
            Respawn();
        }
    }

    //generate a new random position of the star
    void Respawn(){
        size = Math.random()*((0.1 - (-100))+1 + (-100));
        x = Math.random()*width;
        y = Math.random()*height;
        speed = 0;
    }

    //return the x coordinate for the center of the star
    double UdjustedX(){
        return x - size/2;
    }

    //return the y coordinate for the center of the star
    double UdjustedY(){
        return y - size/2;
    }


    //Function to draw a star
    public void Draw() {
        /*//Create the gradient white fill that will be used for the star
        RadialGradient gradientWhite = new RadialGradient(0,
                0,
                x + 0,
                y + 0,
                diameter / 2
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.WHITE),
                new Stop(1, Color.rgb(255, 255, 255, 0)));

        gc.setFill(gradientWhite);//Set the fill to the gradient created*/

        gc.setFill(Color.WHITE);
        gc.fillOval(UdjustedX(), UdjustedY(), diameter, diameter);//Draw the star

    }

}
