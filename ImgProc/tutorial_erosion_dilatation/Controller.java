package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayInputStream;

import static org.opencv.imgcodecs.Imgcodecs.imread;

public class Controller {

 Mat src = new Mat(), dst = new Mat();

 int type = 0;
 int size = 0;

 ObservableList < String > dilationOrErosionList =
  FXCollections.observableArrayList("Erosion", "Dilation");

 ObservableList < String > elementList =
  FXCollections.observableArrayList("Rect", "Cross", "Ellipse");

 //! [fxml_variables]
 @FXML
 private ImageView image;

 @FXML
 private ChoiceBox dilationOrErosion;

 @FXML
 private ChoiceBox element;

 @FXML
 private TextField kernelSize;
 //! [fxml_variables]

 @FXML
 private void initialize() {
  dilationOrErosion.setValue("Erosion");
  dilationOrErosion.setItems(dilationOrErosionList);

  element.setValue("Rect");
  element.setItems(elementList);

  kernelSize.setText("0");

  src = imread("../images/cat.jpg");
  image.setImage(mat2Image(src));
 }

 public void Show(ActionEvent actionEvent) {

  if (dilationOrErosion.getValue().toString() == "Erosion") {
   //! [erosion]
   if (element.getValue().toString() == "Rect")
    type = Imgproc.MORPH_RECT;
   else if (element.getValue().toString() == "Cross")
    type = Imgproc.MORPH_CROSS;
   else
    type = Imgproc.MORPH_ELLIPSE;

   size = Integer.parseInt(kernelSize.getText());

   //! [element]
   Mat element = Imgproc.getStructuringElement(type,
    new Size(2 * size + 1, 2 * size + 1),
    new Point(size, size));
   //! [element]
   Imgproc.erode(src, dst, element);
   //! [erosion]
  } else {
   //! [dilation]
   if (element.getValue().toString() == "Rect")
    type = Imgproc.MORPH_RECT;
   else if (element.getValue().toString() == "Cross")
    type = Imgproc.MORPH_CROSS;
   else
    type = Imgproc.MORPH_ELLIPSE;

   size = Integer.parseInt(kernelSize.getText());

   Mat element = Imgproc.getStructuringElement(type,
    new Size(2 * size + 1, 2 * size + 1),
    new Point(size, size));
   Imgproc.dilate(src, dst, element);
   //! [dilation]
  }

  image.setImage(mat2Image(dst));
 }

 private Image mat2Image(Mat frame) {
  // create a temporary buffer
  MatOfByte buffer = new MatOfByte();
  // encode the frame in the buffer
  Imgcodecs.imencode(".png", frame, buffer);
  // build and return an Image created from the image encoded in the buffer
  return new Image(new ByteArrayInputStream(buffer.toArray()));
 }
}
