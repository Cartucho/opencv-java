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

class BoundingRectsCirclesRun implements ChangeListener{

    //! [declare]
    /// Global Variables
    Mat src = new Mat(); Mat src_gray = new Mat();
    int thresh = 100;
    int max_thresh = 255;

    JLabel imgDisplay = new JLabel(), resultDisplay = new JLabel();
    //! [declare]

    public void run(String[] args) {

        if (args.length < 1)
        {
            System.out.println("Not enough parameters");
            System.out.println("Program arguments:\n<image_name>");
            System.exit(-1);
        }

        //! [load_image]
        /// Load image and template
        src = Imgcodecs.imread( args[0], Imgcodecs.IMREAD_COLOR );

        Imgproc.cvtColor( src, src_gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.blur( src_gray, src_gray, new Size(3,3) );

        Image tmpImg = toBufferedImage(src);
        ImageIcon icon = new ImageIcon(tmpImg);
        imgDisplay = new JLabel(icon);

        thresh_callback();
        createJFrame();
    }

    private void thresh_callback() {

        Mat threshold_output = new Mat();
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();

        Imgproc.threshold( src_gray, threshold_output, thresh, 255,
                Imgproc.THRESH_BINARY);
        Imgproc.findContours( threshold_output, contours, hierarchy, Imgproc.RETR_TREE,
                Imgproc.CHAIN_APPROX_SIMPLE, new Point(0,0));

        List<MatOfPoint2f> contours_poly = new ArrayList<MatOfPoint2f>(contours.size());
        Rect[] boundRect = new Rect[contours.size()];
        Point[] center = new Point[contours.size()];
        float[] radius = new float[contours.size()];
        float[] tmpRadius = new float[1];

        for( int i = 0; i < contours.size(); i++ )
        {
            MatOfPoint2f tmpContour = new MatOfPoint2f( contours.get(i).toArray() );
            MatOfPoint2f tmpCont_poly = new MatOfPoint2f();

            Imgproc.approxPolyDP(tmpContour, tmpCont_poly, 3, true );
            contours_poly.add(i, tmpCont_poly);

            boundRect[i] = Imgproc.boundingRect(new MatOfPoint(tmpCont_poly.toArray()));

            Point tmpCenter = new Point();
            Imgproc.minEnclosingCircle( tmpCont_poly, tmpCenter, tmpRadius);
            center[i] = tmpCenter;
            radius[i] = tmpRadius[0];
        }

        Mat drawing = Mat.zeros( threshold_output.size(), CvType.CV_8UC3 );

        Random generator = new Random();
        // Convert list of MatOfPoint2f to list of MatOfPoint
        List<MatOfPoint> cont_poly = new ArrayList<>();
        for(MatOfPoint2f point2f : contours_poly) {
            MatOfPoint newPoint = new MatOfPoint(point2f.toArray());
            cont_poly.add(newPoint);
        }

        for( int i = 0; i < contours.size(); i++ )
        {
            Scalar color = new Scalar(generator.nextInt(256), generator.nextInt(256), generator.nextInt(256) );
            Imgproc.drawContours( drawing, cont_poly, i, color, 1, 8, new Mat(), 0,  new Point() );
            Imgproc.rectangle( drawing, boundRect[i].tl(), boundRect[i].br(), color, 2, 8, 0 );
            Imgproc.circle( drawing, center[i], (int)radius[i], color, 2, 8, 0 );
        }

        Image tmpImg = toBufferedImage(drawing);
        ImageIcon icon = new ImageIcon(tmpImg);
        resultDisplay.setIcon(icon);
    }

    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            thresh = (int)source.getValue();
            thresh_callback();
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

        String title = "Source; Control; Contours";
        JFrame frame = new JFrame(title);
        frame.setLayout(new GridLayout(2, 2));
        frame.add(imgDisplay);

        //! [create_trackbar]
        int min = 0, max = max_thresh;
        JSlider slider = new JSlider(JSlider.VERTICAL,
                min, max, thresh);
        //! [create_trackbar]

        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        // Set the spacing for the minor tick mark
        slider.setMinorTickSpacing(50);

        // Customizing the labels
        Hashtable labelTable = new Hashtable();
        labelTable.put( new Integer( 0 ), new JLabel("0") );
        labelTable.put( new Integer( 50 ), new JLabel("50") );
        labelTable.put( new Integer( 100 ), new JLabel("100") );
        labelTable.put( new Integer( 155 ), new JLabel("150") );
        labelTable.put( new Integer( 200 ), new JLabel("200") );
        labelTable.put( new Integer( 250 ), new JLabel("250") );
        slider.setLabelTable( labelTable );

        slider.addChangeListener(this);

        frame.add(slider);

        frame.add(resultDisplay);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

public class BoundingRectsCircles
{
    public static void main(String[] args) {
        // load the native OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // run code
        new BoundingRectsCirclesRun().run(args);
    }
}
