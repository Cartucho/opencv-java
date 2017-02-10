import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

class EqualizeHistDemoRun{

    public void run() {

        //! [initial_variables]
        Mat src = new Mat(), dst = new Mat();

        String source_window = "Source image";
        String equalized_window = "Equalized Image";
        //! [initial_variables]

        //! [load_image]
        /// Load image
        src = Imgcodecs.imread( "../data/dog.jpg", Imgcodecs.IMREAD_COLOR );

        if(src.empty()) {
            System.out.println("Error opening image");
            System.exit(-1);
        }
        //! [load_image]

        //! [grayscale]
        /// Convert to grayscale
        Imgproc.cvtColor( src, src, Imgproc.COLOR_BGR2GRAY );
        //! [grayscale]

        //! [equalization]
        /// Apply Histogram Equalization
        Imgproc.equalizeHist( src, dst );
        //! [equalization]

        //! [imshow]
        /// Display results
        Image showImage;

        showImage = toBufferedImage(src);
        displayImage(source_window, showImage, 0, 0);

        showImage = toBufferedImage(dst);
        displayImage(equalized_window, showImage, 200, 200);
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

    public void displayImage(String title, Image img, int x, int y)
    {
        ImageIcon icon=new ImageIcon(img);
        JFrame frame=new JFrame(title);
        JLabel lbl=new JLabel(icon);
        frame.add(lbl);
        frame.setLocation(x, y);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

public class EqualizeHistDemo {
    public static void main(String[] args) {

        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new EqualizeHistDemoRun().run();
    }
}
