import org.opencv.core.Mat;
import javax.swing.*;

public class ImageWindow {

    String name;
    int flags;
    int x;
    int y;
    int width;
    int height;
    Mat img;
    Boolean alreadyUsed;
    JLabel lbl;

    public ImageWindow(String name, Mat img) {
        this.name = name;
        this.img = img;
        this.alreadyUsed = false;
        this.lbl = null;
    }

    public void setMat(Mat img) {
        this.img = img;
        this.alreadyUsed = false;
    }
}

