import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;

public class HealthBar {
    private double value = 0;
    private GraphicsContext gc;
    private double width;
    private double height;
    private double xIndex;
    private double yIndex;

    private boolean loaded = false;
    private double barWidth = 100;

    public HealthBar(GraphicsContext gc, double width, double height){
        this.gc = gc;
        this.height = height;
        this.width = width;
    }

    public void update(int health, double xIndex, double yIndex) {
        if(loaded) {
            if (health >= 0) {
                if (health < value) {
                    value -= (value - health) / 5;
                }
            }
        }
        this.xIndex = xIndex / 2;
        this.yIndex = yIndex / 2;

        draw();
    }

    public void startUp() {
        if(value < 100 && !loaded){
            value += (101-value)/10;
        }
        if(value >= 100 && !loaded){
            value = 100;
            loaded = true;
        }
    }

    public void draw()
    {
        gc.setFill(Color.rgb(60,100,228));
        gc.setFont(Font.font(24));
        gc.fillText("Health", width/2-36 + xIndex, height - 5 + yIndex - 210);
        gc.setFill(Color.rgb(255-(int)(255*(value/100)), (int)(255 * (value/100) ),75, 0.5));
        gc.fillRect(width/2 - barWidth/2 + xIndex, height - 5 + yIndex - value*2, barWidth, value * 2);
    }
}
