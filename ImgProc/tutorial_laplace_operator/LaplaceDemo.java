import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

class LaplaceDemoRun {

    public void run() {
        //! [declare_variables]
        Mat src = new Mat(), src_gray = new Mat(), dst = new Mat();
        int kernel_size = 3;
        int scale = 1;
        int delta = 0;
        int ddepth = CvType.CV_16S;
        String window_name = "Laplace Demo";
        //! [declare_variables]

        //! [load_image]
        src = Imgcodecs.imread( "../data/cow.jpg" );
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

        Mat abs_dst = new Mat();

        //! [laplacian]
        Imgproc.Laplacian( src_gray, dst, ddepth, kernel_size, scale, delta, Core.BORDER_DEFAULT );
        //! [laplacian]
        //! [convert_scale]
        Core.convertScaleAbs( dst, abs_dst );
        //! [convert_scale]

        //! [imshow]
        Image tmpImg = toBufferedImage(abs_dst);
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

public class LaplaceDemo {
    public static void main(String[] args) {

        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new LaplaceDemoRun().run();

    }
}
