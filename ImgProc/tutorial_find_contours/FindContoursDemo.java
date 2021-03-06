import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import java.util.*;
import java.util.List;

class FindContoursDemoRun implements ChangeListener {

    /// Global Variables
    Mat src = new Mat(); Mat src_gray = new Mat();
    int min_thresh = 0;
    int max_thresh = 255;
    int thresh = 100;

    JLabel contoursDisplay = new JLabel();

    public void run() {

        /// Load image
        src = Imgcodecs.imread( "../data/fish.jpg" , Imgcodecs.IMREAD_COLOR );
        if(src.empty()) {
            System.out.println("Error opening image");
            System.exit(-1);
        }

        /// Convert image to gray and blur it
        Imgproc.cvtColor( src, src_gray, Imgproc.COLOR_BGR2GRAY );
        Imgproc.blur( src_gray, src_gray, new Size(3,3) );

        threshCallback();
        createJFrame();
    }

    private void threshCallback() {

        Mat canny_output = new Mat();
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();

        /// Detect edges using canny
        Imgproc.Canny( src_gray, canny_output, thresh, thresh*2, 3, false);
        /// Find contours
        Imgproc.findContours( canny_output, contours, hierarchy, Imgproc.RETR_TREE,
                Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0) );

        /// Draw contours
        Mat drawing = Mat.zeros( canny_output.size(), CvType.CV_8UC3 );
        Random rand = new Random();
        for( int i = 0; i< contours.size(); i++ )
        {
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();

            Scalar color = new Scalar( (int)(r*255), (int)(g*255), (int)(b*255) );
            Imgproc.drawContours( drawing, contours, (int)i, color, 2, 8, hierarchy, 0, new Point() );
        }

        Image tmpImg = toBufferedImage(drawing);
        ImageIcon icon = new ImageIcon(tmpImg);
        contoursDisplay.setIcon(icon);
    }

    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            thresh = (int)source.getValue();
            threshCallback();
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

    private void createJFrame() {

        /// Create JFrame
        String title = "Source image; Control; Contours Result";
        JFrame frame = new JFrame(title);
        frame.setLayout(new GridLayout(1, 3));

        Image tmpImg = toBufferedImage(src);
        ImageIcon icon = new ImageIcon(tmpImg);
        JLabel originalImage = new JLabel(icon);
        frame.add(originalImage);

        JSlider slider = new JSlider(JSlider.VERTICAL,
                min_thresh, max_thresh, thresh);

        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        // Set the spacing for either the major and minor tick mark
        slider.setMajorTickSpacing(50);
        slider.setMinorTickSpacing(25);

        // Customizing the labels
        Hashtable labelTable = new Hashtable();
        labelTable.put( new Integer( 0 ), new JLabel("0") );
        labelTable.put( new Integer( 50 ), new JLabel("50") );
        labelTable.put( new Integer( 100 ), new JLabel("100") );
        labelTable.put( new Integer( 150 ), new JLabel("150") );
        labelTable.put( new Integer( 200 ), new JLabel("200") );
        labelTable.put( new Integer( 255 ), new JLabel("255 : (Canny thresh)") );
        slider.setLabelTable( labelTable );

        slider.addChangeListener(this);

        frame.add(slider);

        frame.add(contoursDisplay);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

public class FindContoursDemo {
    public static void main(String[] args) {

        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new FindContoursDemoRun().run();
    }
}
