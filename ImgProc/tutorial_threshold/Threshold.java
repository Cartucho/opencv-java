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

class ThresholdRun implements ChangeListener {

    int min_type = 0;
    int max_type = 4;
    int init_type = 3;

    int min_value = 0;
    int max_value = 255;
    int init_value = 0;

    int threshold_type = 3;
    int threshold_value = 0;
    int max_BINARY_value = 255;

    Mat src = new Mat(), src_gray = new Mat(), dst = new Mat();

    String window_name = "Threshold Demo";
    JLabel imageView;
    JFrame frame;

    public void run() {

        //![load_image]
        // Load an image
        src = Imgcodecs.imread( "../data/dog.jpg" );
        if(src.empty()) {
            System.out.println("Error opening image");
            System.exit(-1);
        }

        // Convert the image to Gray
        Imgproc.cvtColor( src, src_gray, Imgproc.COLOR_BGR2GRAY );
        //![load_image]

        //![create_window]
        // Create a window to display results
        Image imgShow = toBufferedImage( src );
        frame = displayImage(window_name , imgShow, 0, 0);
        //![create_window]

        // Create Sliders to choose Type of Threshold
        frame.setLayout(new GridLayout(1, 3)); // 3 = photo + typeSlider + valueSlider

        //![create_trackbars]
        // typeSlider -> Slider to choose Type of Threshold
        JSlider typeSlider = new JSlider(JSlider.VERTICAL,
                min_type, max_type, init_type);

        typeSlider.setPaintTicks(true);
        typeSlider.setPaintLabels(true);

        // Set the spacing for the major tick mark
        typeSlider.setMajorTickSpacing(1);

        // Customizing the labels
        Hashtable labelTypeTable = new Hashtable();
        labelTypeTable.put( new Integer( 0 ), new JLabel("Binary") );
        labelTypeTable.put( new Integer( 1 ), new JLabel("Binary Inverted") );
        labelTypeTable.put( new Integer( 2 ), new JLabel("Truncate") );
        labelTypeTable.put( new Integer( 3 ), new JLabel("To Zero") );
        labelTypeTable.put( new Integer( 4 ), new JLabel("To Zero Inverted : (Types)") );
        typeSlider.setLabelTable( labelTypeTable );

        // valueSlider -> Slider to choose Type of Threshold
        JSlider valueSlider = new JSlider(JSlider.VERTICAL,
                min_value, max_value, init_value);

        valueSlider.setPaintTicks(true);
        valueSlider.setPaintLabels(true);

        // Set the spacing for either the major and minor tick mark
        valueSlider.setMajorTickSpacing(50);
        valueSlider.setMinorTickSpacing(10);

        // Customizing the labels
        Hashtable labelValueTable = new Hashtable();
        labelValueTable.put( new Integer( 0 ), new JLabel("0") );
        labelValueTable.put( new Integer( 50 ), new JLabel("50") );
        labelValueTable.put( new Integer( 100 ), new JLabel("100") );
        labelValueTable.put( new Integer( 150 ), new JLabel("150") );
        labelValueTable.put( new Integer( 200 ), new JLabel("200") );
        labelValueTable.put( new Integer( 255 ), new JLabel("255 : (Values)") );
        valueSlider.setLabelTable( labelValueTable );

        // Set names and add change listener
        typeSlider.setName("Type");
        valueSlider.setName("Value");
        typeSlider.addChangeListener(this);
        valueSlider.addChangeListener(this);
        //![create_trackbars]

        // Add the sliders to the frame
        frame.add(typeSlider);
        frame.add(valueSlider);

        // Resize the frame
        frame.pack();
    }

    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (source.getName() == "Type") {
            if (!source.getValueIsAdjusting()) {
                threshold_type = (int)source.getValue();
                updateImage();
            }
        }else if (source.getName() == "Value") {
            if (!source.getValueIsAdjusting()) {
                threshold_value = (int)source.getValue();
                updateImage();
            }
        }
    }

    //![threshold_demo]
    private void updateImage() {

        /* 0: Binary
           1: Binary Inverted
           2: Threshold Truncated
           3: Threshold to Zero
           4: Threshold to Zero Inverted
        */

        // Apply Threshold
        Imgproc.threshold( src_gray, dst, threshold_value, max_BINARY_value, threshold_type );

        // Update Image
        Image outputImage = toBufferedImage(dst);
        imageView.setIcon(new ImageIcon(outputImage));
    }
    //![threshold_demo]

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

public class Threshold {
    public static void main(String[] args) {

        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new ThresholdRun().run();

    }
}
