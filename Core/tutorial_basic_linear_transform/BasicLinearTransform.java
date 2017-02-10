import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import java.util.Scanner;

class BasicLinearTransformRun{

  //! [parameters]
    double alpha;
    int beta;
  //! [parameters]

    public void run() {

        Scanner scan = new Scanner( System.in );

    //! [load_image]
        Mat image = Imgcodecs.imread("lena.jpg");
    //! [load_image]
    //! [mat_zeros]
        Mat new_image = Mat.zeros( image.size(), image.type() );
    //! [mat_zeros]

        System.out.println(" Basic Linear Transforms ");
        System.out.println("-------------------------");
        System.out.print("* Enter the alpha value [1.0-3.0]: ");
        alpha = scan.nextDouble();
        System.out.print("* Enter the beta value [0-100]: ");
        beta = scan.nextInt();

    //! [loop]
        for( int y = 0; y < image.rows(); y++ ) {
            for( int x = 0; x < image.cols(); x++ ) {

                double r = alpha * (image.get(y, x)[0]) + beta;
                double g = alpha * (image.get(y, x)[1]) + beta;
                double b = alpha * (image.get(y, x)[2]) + beta;

                new_image.put(y, x, new double[]{r, g, b});
            }
        }
    //! [loop]

    //! [show_images]
        Image img = toBufferedImage( image );
        displayImage( "Original Image", img, 0, 200 );

        Image img2 = toBufferedImage( new_image );
        displayImage( "New Image", img2, 400, 400 );
    //! [show_images]

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
        JLabel lbl=new JLabel(icon);
        frame.add(lbl);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocation(x, y);
        frame.setVisible(true);
    }

}

public class BasicLinearTransform {
    public static void main(String[] args) {

        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new BasicLinearTransformRun().run();
    }
}
