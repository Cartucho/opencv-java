import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

class HoughCirclesRun {

    public void run() {

        //! [load_image]
        Mat src = Imgcodecs.imread( "../data/smarties.png");
        if(src.empty()) {
            System.out.println("Error opening image");
            System.exit(-1);
        }
        //! [load_image]

        Mat src_gray = new Mat();
        //! [gray]
        Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_BGR2GRAY);
        //! [gray]
        //! [gaussian_blur]
        Imgproc.GaussianBlur( src_gray, src_gray, new Size(9, 9), 2, 2 );
        //! [gaussian_blur]

        Mat circles = new Mat();
        double minDist = (double)src_gray.rows()/8;
        // If unknown the min and max radius, put zero as default.
        int minRadius = 0, maxRadius = 0;
        //! [hough_circles]
        Imgproc.HoughCircles(src_gray, circles, Imgproc.HOUGH_GRADIENT, 1.0, minDist, 100.0, 30.0, minRadius, maxRadius);
        //! [hough_circles]

        //! [loop]
        for (int x = 0; x < circles.cols(); x++) {
            double[] c = circles.get(0, x);

            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            int radius = (int) Math.round(c[2]);
            // circle center
            Imgproc.circle(src, center, 3, new Scalar(0,100,100), -1, 8, 0 );
            // circle outline
            Imgproc.circle(src, center, radius, new Scalar(255,0,255), 3, 8, 0 );
        }
        //! [loop]

        //! [imshow]
        Image tmpImg = toBufferedImage(src);
        displayImage("Hough Circle Transform Demo", tmpImg);
        //! [imshow]
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

    public void displayImage(String title, Image img)
    {
        ImageIcon icon=new ImageIcon(img);
        JFrame frame=new JFrame(title);
        JLabel lbl=new JLabel(icon);
        frame.add(lbl);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

public class HoughCircles {
    public static void main(String[] args) {

        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new HoughCirclesRun().run();

    }
}
