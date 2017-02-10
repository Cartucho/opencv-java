import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.Hashtable;

class CannyDetectorDemoRun implements ChangeListener {

    //! [global_variables]
    JLabel imageView;

    Mat src = new Mat(), src_gray = new Mat();
    Mat dst = new Mat(), detected_edges = new Mat();

    int edgeThresh = 1;
    int init_value = 0;
    int lowThreshold = 0;
    int  max_lowThreshold = 100;
    int ratio = 3;
    int kernel_size = 3;
    String window_name = "Edge Map";
    //! [global_variables]

    public void run() {

        //! [load_image]
        src = Imgcodecs.imread( "../data/windows.jpg" );
        if(src.empty()) {
            System.out.println("Error opening image");
            System.exit(-1);
        }
        //! [load_image]

        //! [create]
        /// Create a matrix of the same type and size as src (for dst)
        dst.create( src.size(), src.type() );
        //! [create]

        //! [grayscale]
        /// Convert the image to grayscale
        Imgproc.cvtColor( src, src_gray, Imgproc.COLOR_BGR2GRAY );
        //! [grayscale]

        //! [window]
        Image tmpImg = toBufferedImage(src);
        JFrame jframe = displayImage(window_name, tmpImg, 0, 0);

        jframe.setLayout(new GridLayout(1, 2));

        //! [trackbar]
        JSlider slider = new JSlider(JSlider.VERTICAL,
                lowThreshold, max_lowThreshold, init_value);

        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        // Set the spacing for either the major and minor tick mark
        slider.setMajorTickSpacing(25);
        slider.setMinorTickSpacing(10);

        // Customizing the labels
        Hashtable labelTable = new Hashtable();
        labelTable.put( new Integer( 0 ), new JLabel("0") );
        labelTable.put( new Integer( 25 ), new JLabel("25") );
        labelTable.put( new Integer( 50 ), new JLabel("50") );
        labelTable.put( new Integer( 75 ), new JLabel("75") );
        labelTable.put( new Integer( 100 ), new JLabel("100 : (Min Threshold)") );
        slider.setLabelTable( labelTable );

        slider.addChangeListener(this);
        //! [trackbar]

        jframe.add(slider);

        // Resize the JFrame
        jframe.pack();
        //! [window]
    }

    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            lowThreshold = (int)source.getValue();

            //! [blur]
            /// Reduce noise with a kernel 3x3
            Imgproc.blur( src_gray, detected_edges, new Size(3,3) );
            //! [blur]

            //! [canny]
            /// Canny detector
            Imgproc.Canny( detected_edges, detected_edges, lowThreshold, lowThreshold*ratio, kernel_size, false );
            //! [canny]

            //! [output]
            /// Using Canny's output as a mask, we display our result
            dst.setTo(Scalar.all(0));
            //! [output]

            //! [copy_to]
            src.copyTo( dst, detected_edges);
            //! [copy_to]
            //! [imshow]
            Image outputImage = toBufferedImage(dst);
            imageView.setIcon(new ImageIcon(outputImage));
            //! [imshow]
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

    public JFrame displayImage(String title, Image img, int x, int y)
    {
        ImageIcon icon=new ImageIcon(img);
        JFrame frame=new JFrame(title);
        imageView =new JLabel(icon);
        frame.add(imageView);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocation(x, y);
        frame.setVisible(true);

        return frame;
    }
}

public class CannyDetectorDemo {
    public static void main(String[] args) {

        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new CannyDetectorDemoRun().run();

    }
}
