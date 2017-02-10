import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

class Filter2D_DemoRun {

    JLabel imageView;

    public void run() {

        Mat src = new Mat(), dst = new Mat();

        Mat kernel = new Mat();
        //! [initialize]
        Point anchor = new Point( -1, -1);
        double delta = 0.0;
        int ddepth = -1;
        //! [initialize]
        int kernel_size;
        String window_name = "filter2D Demo";

        //! [load_image]
        /// Load an image
        src = Imgcodecs.imread( "../data/apple.jpg" );
        if(src.empty()) {
            System.out.println("Error opening image");
            System.exit(-1);
        }
        //! [load_image]

        //! [create_window]
        Image tmpImg = toBufferedImage(src);
        displayImage(window_name, tmpImg, 0, 0);
        //! [create_window]

        int ind = 0;

        /// Loop - Will filter the image with different kernel sizes each 0.5 seconds
        while( true )
        {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //! [kernel]
            /// Update kernel size for a normalized box filter
            kernel_size = 3 + 2*( ind%5 );
            Mat ones = Mat.ones( kernel_size, kernel_size, CvType.CV_32F );
            //! [kernel]
            Core.multiply(ones, new Scalar(1/(double)(kernel_size*kernel_size)), kernel);
            //! [apply_filter]
            /// Apply filter
            Imgproc.filter2D(src, dst, ddepth , kernel, anchor, delta, Core.BORDER_DEFAULT );
            //! [apply_filter]
            Image outputImage = toBufferedImage(dst);
            imageView.setIcon(new ImageIcon(outputImage));
            ind++;
        }
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
        ImageIcon icon=new ImageIcon(img);
        JFrame frame=new JFrame(title);
        imageView =new JLabel(icon);
        frame.add(imageView);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocation(x, y);
        frame.setVisible(true);
    }
}

public class Filter2D_Demo {
    public static void main(String[] args) {

        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new Filter2D_DemoRun().run();

    }
}
