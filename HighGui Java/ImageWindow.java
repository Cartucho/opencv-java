import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;

public class ImageWindow {

    String name;
    Mat img = null;
    Boolean alreadyUsed = false;
    JFrame frame = null;
    JLabel lbl = null;
    int flag;
    int x;
    int y;
    int width = -1;
    int height = -1;

    public ImageWindow(String name, Mat img) {
        this.name = name;
        this.img = img;
        this.flag = HighGui.WINDOW_NORMAL;
    }

    public ImageWindow(String name, int flag) {
        this.name = name;
        this.flag = flag;
    }

    public void setMat(Mat img) {

        this.img = img;
        this.alreadyUsed = false;

        if(width != -1 && height != -1)
            resizeImage();
    }

    public void setFrameLabelVisible(JFrame frame, JLabel lbl) {
        this.frame = frame;
        this.lbl = lbl;

        if(width != -1 && height != -1)
            lbl.setPreferredSize(new Dimension(width, height));

        frame.add(lbl);
        frame.pack();
        frame.setVisible(true);
    }

    public void setNewDimension(int width, int height) {
        this.width = width;
        this.height = height;

        if(img != null)
            resizeImage();
    }

    private void resizeImage() {
        if(flag == HighGui.WINDOW_NORMAL){
            Size tmpSize = keepAspectRatioSize(img.width(), img.height(), width, height);
            Imgproc.resize(img, img, tmpSize);
        }
    }

    public static Size keepAspectRatioSize(int original_width,
                                           int original_height,
                                           int bound_width,
                                           int bound_height) {

        int new_width = original_width;
        int new_height = original_height;

        if (original_width > bound_width) {
            new_width = bound_width;
            new_height = (new_width * original_height) / original_width;
        }

        if (new_height > bound_height) {
            new_height = bound_height;
            new_width = (new_height * original_width) / original_height;
        }

        return new Size(new_width, new_height);
    }
}
