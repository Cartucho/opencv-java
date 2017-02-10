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

class CalcBackProjectDemo1Run implements ChangeListener {

    //! [declare]
    /// Global Variables
    Mat src = new Mat(),
            hsv = new Mat(),
            hue = new Mat();

    int bins = 25;

    JLabel histogram  = new JLabel(),
            backprojection  = new JLabel();
    //! [declare]

    public void run() {

        //! [load_image]
        /// Read the image
        src = Imgcodecs.imread( "../data/hand.jpg", Imgcodecs.IMREAD_COLOR );
        if(src.empty()) {
            System.out.println("Error opening image");
            System.exit(-1);
        }

        /// Transform it to HSV
        Imgproc.cvtColor( src, hsv, Imgproc.COLOR_BGR2HSV );
        //! [load_image]

        //! [hue]
        /// Use only the Hue value
        hue.create( hsv.size(), hsv.depth() );
        MatOfInt ch = new MatOfInt(0, 0);
        Core.mixChannels(new ArrayList<Mat>(Arrays.asList(hsv)),
                new ArrayList<Mat>(Arrays.asList(hue)),
                ch);
        //! [hue]

        createJFrames();

    }

    private void createJFrames() {

        // Histogram & Backproj
        histAndBackProj();

        /// Create main JFrame
        String title = "Source image; Histogram; BackProj";
        Image tmpImg = toBufferedImage(src);

        ImageIcon icon = new ImageIcon(tmpImg);
        JFrame frame = new JFrame(title);
        JLabel originalImage = new JLabel(icon);
        frame.setLayout(new GridLayout(1, 3));
        frame.add(originalImage);
        frame.add(histogram);
        frame.add(backprojection);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setLocation(0, 0);
        frame.pack();
        frame.setVisible(true);

        //! [trackbar]
        /// Create Trackbar JFrame to enter the number of bins
        JFrame controlFrame = new JFrame("Hue Bins");
        int min = 0, max = 180;
        JSlider slider = new JSlider(JSlider.VERTICAL,
                min, max, bins);
        //! [trackbar]

        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        // Set the spacing for either the major and minor tick mark
        slider.setMajorTickSpacing(45);
        slider.setMinorTickSpacing(5);

        // Customizing the labels
        Hashtable labelTable = new Hashtable();
        labelTable.put( new Integer( 0 ), new JLabel("0") );
        labelTable.put( new Integer( 45 ), new JLabel("45") );
        labelTable.put( new Integer( 90 ), new JLabel("90") );
        labelTable.put( new Integer( 135 ), new JLabel("135") );
        labelTable.put( new Integer( 180 ), new JLabel("180 : (Hue Bins)") );
        slider.setLabelTable( labelTable );

        slider.addChangeListener(this);

        controlFrame.add(slider);
        controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        controlFrame.setLocation(400, 400);
        controlFrame.pack();
        controlFrame.setVisible(true);

    }

    //! [hist_and_backproj]
    private void histAndBackProj() {
        Mat hist = new Mat();
        int size = Math.max( bins, 2 );
        MatOfInt histSize = new MatOfInt(size);
        MatOfFloat range = new MatOfFloat(0.0f, 180.0f);
        MatOfInt channels = new MatOfInt(0);
        //! [hist_and_backproj]

        //! [hist_normalize]
        Imgproc.calcHist(new ArrayList<Mat>(Arrays.asList(hue)), channels,
                new Mat(), hist, histSize, range, false);
        Core.normalize(hist, hist, 0, 255, Core.NORM_MINMAX, -1, new Mat());
        //! [hist_normalize]

        //! [backproj]
        Mat backproj = new Mat();
        Imgproc.calcBackProject(new ArrayList<Mat>(Arrays.asList(hue)), channels,
                hist, backproj, range, 1);
        //! [backproj]

        //! [draw]
        int w = 400; int h = 400;
        int bin_w = (int) Math.round( (double) w / size );
        Mat histImg = Mat.zeros( w, h, CvType.CV_8UC3 );

        for( int i = 0; i < bins; i ++ )
        {
            Imgproc.rectangle( histImg, new Point( i*bin_w, h ),
                    new Point( (i+1)*bin_w, h - Math.round( hist.get(i, 0)[0]*h/255.0 ) ),
                    new Scalar( 0, 0, 255 ), -1 );
        }
        //! [draw]

        Image tmpImg = toBufferedImage(histImg);
        ImageIcon icon = new ImageIcon(tmpImg);
        histogram.setIcon(icon);

        tmpImg = toBufferedImage(backproj);
        icon = new ImageIcon(tmpImg);
        backprojection.setIcon(icon);
    }

    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            bins = (int)source.getValue();
            histAndBackProj();
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

}
public class CalcBackProjectDemo1 {
    public static void main(String[] args) {

        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new CalcBackProjectDemo1Run().run();
    }
}
