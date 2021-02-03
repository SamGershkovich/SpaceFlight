import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;

public class Crack {
    private double x;
    private double y;
    private ImageView img;
    private GraphicsContext gc;
    private double size = 200;

    public Crack(GraphicsContext gc) {
        this.gc = gc;
        int id = (int)(Math.random() * ((2 - 1 )+ 1) + 1);
        img = new ImageView("images/crack"+ id + ".png");
    }

    public void setX(double x){
        this.x = x - size/2;
    }

    public void setY(double y){
        this.y = y - size/4;
    }

    public void setSize(double size){
        this.size = size*2;
    }
    public void draw(double xIndex, double yIndex) {
        gc.drawImage(img.getImage(), x + xIndex, y + yIndex, size, size/1.66);

    }
}
