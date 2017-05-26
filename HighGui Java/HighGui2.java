import org.opencv.core.Mat;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.lang.reflect.InvocationTargetException;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

public class HighGui2 {

    public static void main(String[] args) {
        new HighGui2("main", null);
    }

    public static void imshow(String title, Mat imgMat) {

        if (imgMat.empty()){
            System.err.println("Error: Empty Mat entered in imshow");
            System.exit(-1);
        }

        new HighGui2(title, imgMat);
    }

    public HighGui2(String title, Mat imgMat) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    BufferedImage img = (BufferedImage) toBufferedImage(imgMat);
                    ImageShower.show(null, img, title);
                }
            });
        } catch (InvocationTargetException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Converts Mat m to BufferedImage
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

    public static class ImageShower extends JPanel {

        private JLabel label = new JLabel();

        public ImageShower() {
            setLayout(new BorderLayout());
            add(label);

            InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
            ActionMap am = getActionMap();

            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), "close");
            am.put("close", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Window window = SwingUtilities.windowForComponent(ImageShower.this);
                    if (window != null) {
                        window.dispose();
                    }
                }
            });
        }

        public void setImage(Image img) {
            label.setIcon(new ImageIcon(img));
        }

        public static void show(Component owner, Image img, String title) {
            Window parent = null;
            if (owner != null) {
                parent = SwingUtilities.windowForComponent(owner);
            }

            JButton close = new JButton("Close");
            close.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton btn = (JButton) e.getSource();
                    Window window = SwingUtilities.windowForComponent(btn);
                    if (window != null) {
                        window.dispose();
                    }
                }
            });

            JDialog dialog = new JDialog(parent, Dialog.ModalityType.APPLICATION_MODAL);
            ImageShower shower = new ImageShower();
            shower.setImage(img);
            dialog.setTitle(title);
            dialog.add(shower);
            dialog.add(close, BorderLayout.SOUTH);
            dialog.getRootPane().setDefaultButton(close);
            dialog.pack();
            dialog.setLocationRelativeTo(owner);
            dialog.setVisible(true);
        }

    }

}

