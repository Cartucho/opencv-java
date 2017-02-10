import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import java.util.Random;

class CopyMakeBorderRun {

    JLabel imageView;

    public void run() {

        //! [declare_variables]
        Mat src = new Mat(), dst = new Mat();
        int top, bottom, left, right;
        String window_name_1 = "copyMakeBorder Demo - random constant value";
        String window_name_2 = "copyMakeBorder Demo - replicated";
        //! [declare_variables]

        //! [load_image]
        src = Imgcodecs.imread( "../data/dog.jpg" );
        if(src.empty()) {
            System.out.println("Error opening image");
            System.exit(-1);
        }
        //! [load_image]

        //! [initialize_arguments]
        top = (int) (0.05*src.rows()); bottom = (int) (0.05*src.rows());
        left = (int) (0.05*src.cols()); right = (int) (0.05*src.cols());
        //! [initialize_arguments]

        Core.copyMakeBorder( src, dst, top, bottom, left, right, Core.BORDER_REPLICATE);
        ImageIcon icon = new ImageIcon( toBufferedImage(dst) );

        //! [create_window]
        // In window1 we store the JLabel in the variable imageView
        // since we will keep on changing the border color.
        displayImage(window_name_1, 0, 0, imageView = new JLabel(icon));
        displayImage(window_name_2, 400, 400, new JLabel(icon));
        //! [create_window]

        Random rand = new Random();

        //! [loop]
        while( true ) {

            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();

            //! [value]
            Scalar value = new Scalar( (int)(r*255), (int)(g*255), (int)(b*255) );
            //! [value]
            //! [copy_make_border]
            Core.copyMakeBorder( src, dst, top, bottom, left, right, Core.BORDER_CONSTANT, value);
            //! [copy_make_border]
            //! [imshow]
            Image outputImage = toBufferedImage(dst);
            imageView.setIcon(new ImageIcon(outputImage));
            //! [imshow]
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //! [loop]

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

    public void displayImage(String title, int x, int y, JLabel jlabel)
    {
        JFrame frame=new JFrame(title);
        frame.add(jlabel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocation(x, y);
        frame.setVisible(true);
    }
}

public class CopyMakeBorder {
    public static void main(String[] args) {

        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new CopyMakeBorderRun().run();

    }
}
