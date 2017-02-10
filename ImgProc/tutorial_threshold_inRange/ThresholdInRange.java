import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.Hashtable;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class ThresholdInRangeRun implements ChangeListener {

    int low_r=30, low_g=30, low_b=30;
    int high_r=100, high_g=100, high_b=100;
    
    JLabel videoCapture, objectDetection;

    //! [mat]
    Mat frame = new Mat(), frame_threshold = new Mat();
    //! [mat]
    Image imageToShow;

    // the OpenCV object that realizes the video capture
    VideoCapture cap = new VideoCapture();

    // a timer for acquiring the video stream
    ScheduledExecutorService timer;

    public void run() {
        //! [cap]
        // open the default camera
        cap.open(0);
        //! [cap]

        if (cap.isOpened()) {

            createJFrames();

            //! [while]
            // grab a frame every 33 ms (30 frames/sec)
            Runnable frameGrabber = new Runnable() {

                @Override
                public void run() {
                    grabFrame();
                    videoCapture.setIcon(new ImageIcon(imageToShow));
                    Core.inRange(frame, new Scalar(low_b,low_g,low_r),
                            new Scalar(high_b,high_g,high_r), frame_threshold);
                    //! [show]
                    objectDetection.setIcon(new ImageIcon(toBufferedImage(frame_threshold)));
                    //! [show]
                }
            };
            //! [while]

            timer = Executors.newSingleThreadScheduledExecutor();
            timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

        } else {
                // log the error
                System.err.println("Impossible to open the camera connection...");
        }
    }

    public void grabFrame() {

        if (cap.isOpened()) {
            try {
                // read the current frame
                cap.read(frame);

                // if the frame is not empty, process it
                if (!frame.empty()) {
                    // convert the Mat object (OpenCV) to Image
                    imageToShow = toBufferedImage(frame);
                }

            } catch (Exception e) {
                // log the error
                System.err.println("Exception during the image elaboration: " + e);
            }
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

    //! [window]
    private void createJFrames() {

        grabFrame();

        // Video Capture JFrame

        ImageIcon icon=new ImageIcon(imageToShow);
        JFrame frame=new JFrame("Video Capture");
        videoCapture =new JLabel(icon);
        frame.add(videoCapture);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocation(0, 0);
        frame.setVisible(true);

        // Object Detection JFrame

        icon=new ImageIcon(imageToShow);
        JFrame frame2=new JFrame("Object Detection");
        objectDetection =new JLabel(icon);
        frame2.add(objectDetection);
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.pack();
        frame2.setLocation(800, 0);
        frame2.setVisible(true);

        // Control JFrame

        JFrame frame3 = new JFrame("Controls");
        frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame3.setLocation(400, 400);
        // 6 = Low R + High R + Low G + High G + Low B + High B
        frame3.setLayout(new GridLayout(1, 6));
        //! [trackbar]
        frame3.add(newSlider("Low R", low_r));
        frame3.add(newSlider("High R", high_r));
        frame3.add(newSlider("Low G", low_g));
        frame3.add(newSlider("High G", high_g));
        frame3.add(newSlider("Low B", low_b));
        frame3.add(newSlider("High B", high_b));
        //! [trackbar]
        frame3.pack();
        frame3.setVisible(true);

    }
    //! [window]

    private JSlider newSlider(String name, int init_value) {

        JSlider slider = new JSlider(JSlider.VERTICAL,
                0, 255, init_value);

        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        // Set the spacing for either the major and minor tick mark
        slider.setMajorTickSpacing(50);
        slider.setMinorTickSpacing(10);

        // Customizing the labels
        Hashtable labelTable = new Hashtable();
        labelTable.put( new Integer( 0 ), new JLabel("0") );
        labelTable.put( new Integer( 50 ), new JLabel("50") );
        labelTable.put( new Integer( 100 ), new JLabel("100") );
        labelTable.put( new Integer( 150 ), new JLabel("150") );
        labelTable.put( new Integer( 200 ), new JLabel("200") );
        labelTable.put( new Integer( 255 ), new JLabel("255 : (" + name + ")" ) );
        slider.setLabelTable( labelTable );

        slider.setName(name);
        slider.addChangeListener(this);

        return slider;
    }

    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        //! [low]
        if (source.getName() == "Low R") {
            if (!source.getValueIsAdjusting()) {
                low_r = Math.min(high_r-1, (int)source.getValue());
                source.setValue(low_r);
            }
        //! [low]
        //! [high]
        }else if (source.getName() == "High R") {
            if (!source.getValueIsAdjusting()) {
                high_r = Math.max((int)source.getValue(), low_r+1);
                source.setValue(high_r);
            }
        //! [high]
        }else if (source.getName() == "Low G") {
            if (!source.getValueIsAdjusting()) {
                low_g = Math.min(high_g-1, (int)source.getValue());
                source.setValue(low_g);
            }
        }else if (source.getName() == "High G") {
            if (!source.getValueIsAdjusting()) {
                high_g = Math.max((int)source.getValue(), low_g+1);
                source.setValue(high_g);
            }
        }else if (source.getName() == "Low B") {
            if (!source.getValueIsAdjusting()) {
                low_b = Math.min(high_b-1, (int)source.getValue());
                source.setValue(low_b);
            }
        }else if (source.getName() == "High B") {
            if (!source.getValueIsAdjusting()) {
                high_b = Math.max((int)source.getValue(), low_b+1);
                source.setValue(high_b);
            }
        }
    }
}

public class ThresholdInRange {
    public static void main(String[] args) {

        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new ThresholdInRangeRun().run();

    }
}
