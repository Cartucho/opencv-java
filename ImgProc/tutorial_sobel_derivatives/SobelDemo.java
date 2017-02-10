import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;


class SobelDemoRun {

    public void run() {

        //! [declare_variables]
        Mat src = new Mat(), src_gray = new Mat();
        Mat grad = new Mat();
        String window_name = "Sobel Demo - Simple Edge Detector";
        int scale = 1;
        int delta = 0;
        int ddepth = CvType.CV_16S;
        //! [declare_variables]

        //! [load_image]
        src =  Imgcodecs.imread( "../data/lena.jpg" );
        if(src.empty()) {
            System.out.println("Error opening image");
            System.exit(-1);
        }
        //! [load_image]

        //! [gaussian_blur]
        Imgproc.GaussianBlur( src, src, new Size(3,3), 0, 0, Core.BORDER_DEFAULT );
        //! [gaussian_blur]

        //! [convert_to_gray]
        Imgproc.cvtColor( src, src_gray, Imgproc.COLOR_RGB2GRAY );
        //! [convert_to_gray]

        Mat grad_x = new Mat(), grad_y = new Mat();
        Mat abs_grad_x = new Mat(), abs_grad_y = new Mat();

        //! [derivatives]
        //Imgproc.Scharr( src_gray, grad_x, ddepth, 1, 0, scale, delta, Core.BORDER_DEFAULT );
        Imgproc.Sobel( src_gray, grad_x, ddepth, 1, 0, 3, scale, delta, Core.BORDER_DEFAULT );
        Core.convertScaleAbs( grad_x, abs_grad_x );

        //Imgproc.Scharr( src_gray, grad_y, ddepth, 0, 1, scale, delta, Core.BORDER_DEFAULT );
        Imgproc.Sobel( src_gray, grad_y, ddepth, 0, 1, 3, scale, delta, Core.BORDER_DEFAULT );
        Core.convertScaleAbs( grad_y, abs_grad_y );
        //! [derivatives]

        //! [add_weighted]
        Core.addWeighted( abs_grad_x, 0.5, abs_grad_y, 0.5, 0, grad );
        //! [add_weighted]

        //! [imshow]
        Image tmpImg = toBufferedImage(grad);
        displayImage(window_name, tmpImg);
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


public class SobelDemo {
    public static void main(String[] args) {

        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new SobelDemoRun().run();

    }
}
