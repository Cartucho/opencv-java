import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import java.util.*;

class HitMiss{

    public void run() {

        Mat input_image = new Mat( 8, 8, CvType.CV_8UC1 );
        int row = 0, col = 0;
        input_image.put(row ,col,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 255, 255, 255, 0, 0, 0, 255,
                0, 255, 255, 255, 0, 0, 0, 0,
                0, 255, 255, 255, 0, 255, 0, 0,
                0, 0, 255, 0, 0, 0, 0, 0,
                0, 0, 255, 0, 0, 255, 255, 0,
                0, 255, 0, 255, 0, 0, 255, 0,
                0, 255, 255, 255, 0, 0, 0, 0);

        Mat kernel = new Mat( 3, 3, CvType.CV_16S );
        kernel.put(row ,col,
                0, 1, 0,
                1, -1, 1,
                0, 1, 0 );

        Mat output_image = new Mat();
        Imgproc.morphologyEx(input_image, output_image, Imgproc.MORPH_HITMISS, kernel);

        int rate = 50;
        Core.add(kernel, new Scalar(1), kernel);
        Core.multiply(kernel, new Scalar(127), kernel);
        kernel.convertTo(kernel, CvType.CV_8U);

        Imgproc.resize(kernel, kernel, new Size(), rate, rate, Imgproc.INTER_NEAREST);
        displayImage("kernel", toBufferedImage(kernel), 0, 0);

        Imgproc.resize(input_image, input_image, new Size(), rate, rate, Imgproc.INTER_NEAREST);
        displayImage("Original", toBufferedImage(input_image), 0, 200);

        Imgproc.resize(output_image, output_image, new Size(), rate, rate, Imgproc.INTER_NEAREST);
        displayImage("Hit or Miss", toBufferedImage(output_image), 500, 200);
    }

    public Image toBufferedImage(Mat m) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if ( m.channels() > 1 ) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels()*m.cols()*m.rows();
        byte [] b = new byte[bufferSize];
        m.get(0,0,b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }

    public void displayImage(String title, Image img, int x, int y)
    {
        ImageIcon icon = new ImageIcon(img);
        JFrame frame = new JFrame(title);
        JLabel lbl = new JLabel(icon);
        frame.add(lbl);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocation(x, y);
        frame.setVisible(true);
    }
}

public class HitOrMiss
{
    public static void main(String[] args) {
        // load the native OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // run code
        new HitMiss().run();
    }
}
