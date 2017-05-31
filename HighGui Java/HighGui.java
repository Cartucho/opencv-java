import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class HighGui {

    public static int closed_windows = 0;

    public static boolean calledWaitKey = false;
    public static boolean repeatingWindow = false;

    static CountDownLatch latch = new CountDownLatch(1);

    static ArrayList<JFrame> oldFramesList = new ArrayList<JFrame>();
    static ArrayList<JFrame> newFramesList = new ArrayList<JFrame>();
    static ArrayList<JLabel> labelList = new ArrayList<JLabel>();

    /*
       The function namedWindow just makes sure that if you wish to do something
       with that same window afterwards (eg move, resize, close that window),
       you can do it by referencing it with the same name.
     */
    public static void namedWindow() {
    }

    public static void imshow(String winname, Mat _img) {

        if (_img.empty()){
            System.err.println("Error: Empty image in imshow");
            System.exit(-1);
        }

        if(calledWaitKey) {
            newFramesList.clear();
            calledWaitKey = false;
        }

        if(!oldFramesList.isEmpty()){
            int i = 0;
            for (JFrame frame : oldFramesList) {
                if(Objects.equals(frame.getTitle(), winname)) {
                    repeatingWindow = true;

                    JLabel lbl = labelList.get(i);

                    Image tmpImg = toBufferedImage(_img);
                    ImageIcon icon = new ImageIcon(tmpImg);
                    lbl.setIcon(icon);

                    newFramesList.add(frame);

                    break;
                }
                i++;
            }
        }

        if(!repeatingWindow) {
            Image tmpImg = toBufferedImage(_img);
            displayImage(winname, tmpImg);
        }

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
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                closed_windows++;
                if(closed_windows == newFramesList.size())
                    latch.countDown();
            }
        });

        labelList.add(lbl);
        newFramesList.add(frame);
    }

    public static int waitKey(int delay) {

        latch = new CountDownLatch(1);
        closed_windows = 0;

        if(!repeatingWindow)
            if (!oldFramesList.isEmpty())
                closeOldFrames();

        calledWaitKey = true;

        final int[] pressedKey = {-1};

        for (JFrame frame : newFramesList) {

            if (delay > 0) {
                Timer timer = new Timer(delay, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        latch.countDown();
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }

            // só faço addKeyListener se não tiver já
            frame.addKeyListener(new KeyListener() {

                @Override
                public void keyTyped(KeyEvent e) {
                }

                @Override
                public void keyReleased(KeyEvent e) {
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    pressedKey[0] = e.getKeyCode();
                    latch.countDown();
                }
            });

            frame.pack();
            frame.setVisible(true);
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        oldFramesList = (ArrayList<JFrame>) newFramesList.clone();

        return pressedKey[0];

    }

    private static void closeOldFrames() {
        for (JFrame frame : oldFramesList) {
            frame.setVisible(false);
            frame.dispose();
        }
        oldFramesList.clear();
    }

}
