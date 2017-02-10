import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

class GaussianMedianBlurBilateralFilterRun {

 JLabel imageView;

 int DELAY_CAPTION = 1500;
 int DELAY_BLUR = 100;
 int MAX_KERNEL_LENGTH = 31;

 Mat src = new Mat(), dst = new Mat();
 String windowName = "Filter Demo 1";

 public void run() {

  src = Imgcodecs.imread("../images/lena.jpg", 1);

  // Initialize windows with black image
  Image img = toBufferedImage(Mat.zeros(src.size(), src.type()));
  displayImage(windowName, img, 0, 200);

  displayCaption("Original Image");

  dst = src.clone();
  displayDst(DELAY_CAPTION);

  displayCaption("Homogeneous Blur");

  //! [blur]
  for (int i = 1; i < MAX_KERNEL_LENGTH; i = i + 2) {
   Imgproc.blur(src, dst, new Size(i, i), new Point(-1, -1));
   displayDst(DELAY_BLUR);
  }
  //! [blur]

  displayCaption("Gaussian Blur");

  //! [gaussian_blur]
  for (int i = 1; i < MAX_KERNEL_LENGTH; i = i + 2) {
   Imgproc.GaussianBlur(src, dst, new Size(i, i), 0, 0);
   displayDst(DELAY_BLUR);
  }
  //! [gaussian_blur]

  displayCaption("Median Blur");

  //! [median_blur]
  for (int i = 1; i < MAX_KERNEL_LENGTH; i = i + 2) {
   Imgproc.medianBlur(src, dst, i);
   displayDst(DELAY_BLUR);
  }
  //! [median_blur]

  displayCaption("Bilateral Blur");

  //! [bilateral_blur]
  for (int i = 1; i < MAX_KERNEL_LENGTH; i = i + 2) {
   Imgproc.bilateralFilter(src, dst, i, i * 2, i / 2);
   displayDst(DELAY_BLUR);
  }
  //! [bilateral_blur]

  displayCaption("End: Close window!");

 }

 void displayCaption(String caption) {
  dst = Mat.zeros(src.size(), src.type());
  Imgproc.putText(dst, caption,
   new Point(src.cols() / 4, src.rows() / 2),
   Core.FONT_HERSHEY_COMPLEX, 1, new Scalar(255, 255, 255));

  Image img = toBufferedImage(dst);
  imageView.setIcon(new ImageIcon(img));
  //JFrame frame = displayImage(windowName, img, 0, 200);

  try {
   Thread.sleep(DELAY_CAPTION);
  } catch (InterruptedException e) {
   e.printStackTrace();
  }
 }

 void displayDst(int delay) {
  Image img = toBufferedImage(dst);
  imageView.setIcon(new ImageIcon(img));

  try {
   Thread.sleep(delay);
  } catch (InterruptedException e) {
   e.printStackTrace();
  }
 }

 public Image toBufferedImage(Mat m) {
  int type = BufferedImage.TYPE_BYTE_GRAY;
  if (m.channels() > 1) {
   type = BufferedImage.TYPE_3BYTE_BGR;
  }
  int bufferSize = m.channels() * m.cols() * m.rows();
  byte[] b = new byte[bufferSize];
  m.get(0, 0, b); // get all the pixels
  BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
  final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
  System.arraycopy(b, 0, targetPixels, 0, b.length);
  return image;
 }

 public void displayImage(String title, Image img, int x, int y) {
  ImageIcon icon = new ImageIcon(img);
  JFrame frame = new JFrame(title);
  imageView = new JLabel(icon);
  frame.add(imageView);
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  frame.pack();
  frame.setLocation(x, y);
  frame.setVisible(true);
 }
}

public class GaussianMedianBlurBilateralFilter {
 public static void main(String[] args) {

  // Load the native library.
  System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
  new GaussianMedianBlurBilateralFilterRun().run();

 }
}
