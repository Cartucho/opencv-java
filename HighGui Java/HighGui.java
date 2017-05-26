import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class HighGui {

    public static int count = 0;
    static ArrayList<JFrame> framesList = new ArrayList<JFrame>();

    public static void imshow(String winname, Mat _img) {

        if (_img.empty()){
            System.err.println("OpenCV Error: Empty image in imshow");
            System.exit(-1);
        }

        Image tmpImg = toBufferedImage(_img);
            displayImage(winname, tmpImg);
        count++;
    }

    public static Image toBufferedImage(Mat m) {
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

    public static void displayImage(String title, Image img)
    {
        ImageIcon icon=new ImageIcon(img);
        JFrame frame=new JFrame(title);
        JLabel lbl=new JLabel(icon);
        frame.add(lbl);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // num is a reference to each image shown
        int index = count;

        framesList.add(count, frame);
    }

    public static void waitKey(int delay) {

        final boolean[] flag = {true};
        final CountDownLatch latch = new CountDownLatch(1);

        for (JFrame frame : framesList) {

            //frame.getInputMap()
            //JPanel content = (JPanel) frame.getContentPane();
            //content.getInputMap().put(KeyStroke.);

            frame.addKeyListener(new KeyListener() {

                @Override
                public void keyTyped(KeyEvent e) {
                }

                @Override
                public void keyReleased(KeyEvent e) {
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    for (JFrame frame : framesList) {
                        frame.setVisible(false);
                        frame.dispose();
                        flag[0] = false;
                        latch.countDown();
                    }
                    framesList.clear();
                    count = 0;
                }
            });

            if (delay != 0) {
                Timer timer = new Timer(delay, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        frame.setVisible(false);
                        frame.dispose();
                        framesList.remove(frame);
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }

            frame.pack();
            frame.setVisible(true);
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
