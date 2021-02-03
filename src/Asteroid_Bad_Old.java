import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Asteroid_Bad_Old {
    private double x;
    private double y;
    private double z = 500;
    private double toX;
    private double toY;
    private double deltaX;
    private double deltaY;

    private double playerPos;

    private double xIndex;
    private double yIndex;

    private double speed;
    private double speedMap;

    private double size;
    private double width;
    private double height;
    private double asteroidSize;
    private double oWidth;
    private double oHeight;
    private double distance;
    private double distanceToC;

    private int quadrant;
    private double slope;
    private String name = "asteroid";
    static int id = 1;
    private ImageView graphic;
    private GraphicsContext gc;

    public Asteroid_Bad_Old(GraphicsContext gc, double width, double height){
        this.width = width;
        this.height = height;
        //x = Math.random()*width;
        //y = Math.random()*height;
        x = 800;
        y = 200;
        toX = x;
        toY = y;
        name = name + id + ".gif";
        id++;
        graphic = new ImageView(name);
        graphic.setSmooth(true);
        this.gc = gc;
        size = graphic.getImage().getWidth()*2;
        asteroidSize = 0;
        oWidth = graphic.getImage().getWidth();
        oHeight = graphic.getImage().getHeight();

    }

    public void update(double xIndex, double yIndex, double speed, double playerPos){
        this.xIndex = xIndex;
        this.yIndex = yIndex;
        this.speed = speed;
        this.playerPos = playerPos * 10;

        SetQuadrant();
        calcDistance();
        SetSlope();
        NextStep();
        Draw();

    }

    public void Draw(){
        //if(asteroidWidth < size)
        //{
        asteroidSize =  size / (distance/100);
        //}

        gc.drawImage(graphic.getImage(), x-asteroidSize/2, y-asteroidSize/2, asteroidSize, asteroidSize);

        gc.setFill(Color.WHITE);
        gc.fillText("size: " + asteroidSize + "distance: " + distance, x + (width/2 - x)/2, y + (height/2 - y)/2);

/*
        gc.setStroke(Color.RED);
        gc.strokeLine(width/2, height/2, x, y);
        gc.setStroke(Color.GREEN);
        gc.strokeLine(x, y, toX, toY);
        gc.setFill(Color.WHITE);
        gc.fillText(""+distance, x + (width/2 - x)/2, y + (height/2 - y)/2);
        gc.fillText(""+slope, width/2, height/2);*/

    }

    public void calcDistance() {
        distance = (Math.pow((Math.pow(width/2 - x, 2) + Math.pow(height/2 - y, 2) + Math.pow(z - playerPos, 2)), 0.5));
        distanceToC = (Math.pow((Math.pow(width/2 - x, 2) + Math.pow(height/2 - y, 2)), 0.5));
        speedMap = distanceToC / 150;

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

    //calculate the slope of the stars angle in relation to the center of the screen
    void SetSlope() {
        slope = ((height/2) - y) / (x - (width/2));
    }



    //calculate the new position of the star
    void NextStep(){
        toX = x;
        toY = y;
        while(toX >= 0 && toX <= width && toY >= 0 && toY <= height) {
            switch (quadrant) {
                case 1:
                case 3:
                    toX -= 1 * speed;
                    toY += slope * speed;
                    break;
                case 2:
                case 4:
                    toX += 1 * speed;
                    toY -= slope * speed;
                    break;
            }
        }

        switch (quadrant) {
            case 1:
            case 3:
                x -= 1 * speed * speedMap;
                y += slope * speed * speedMap;
                break;
            case 2:
            case 4:
                x += 1 * speed * speedMap;
                y -= slope * speed * speedMap;
                break;
        }

        x += xIndex;
        y += yIndex;
/*
        if(x < width * -1){
            x = width;
        }if(y < height * -1){
            y = height;
        }*/
    }

    /*
    //check if the star is within the canvas bounds
    void CheckOnScreen(){
        if((x - 100) > width || (x + 100) < 0 || (y - 100) > height || (y + 100) < 0){

           Respawn();

        }
    }

    void Respawn(){
        graphic.setScaleY(0.1);
        graphic.setScaleX(0.1);
        x = Math.random()*width;
        y = Math.random()*height;
        speed = 0;
    }*/
}
