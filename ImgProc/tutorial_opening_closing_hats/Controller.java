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

    ObservableList<String> morphOperatorList =
            FXCollections.observableArrayList("Opening", "Closing", "Gradient",
                    "Top Hat", "Black Hat");

    ObservableList<String> elementList =
            FXCollections.observableArrayList("Rect", "Cross", "Ellipse");

    @FXML
    private ImageView image;

    @FXML
    private ChoiceBox morphOperator;

    @FXML
    private ChoiceBox element;

    @FXML
    private TextField kernelSize;

    @FXML
    private void initialize(){
        morphOperator.setValue("Opening");
        morphOperator.setItems(morphOperatorList);

        element.setValue("Rect");
        element.setItems(elementList);

        kernelSize.setText("0");

        src = imread("../images/baboon.jpg");
        image.setImage(mat2Image(src));
    }

    public void Show(ActionEvent actionEvent) {

        int morph_operator = morphOperator.getSelectionModel().getSelectedIndex();
        int morph_elem = element.getSelectionModel().getSelectedIndex();
        int morph_size = Integer.parseInt(kernelSize.getText());

        // Since MORPH_X : 2,3,4,5 and 6
        int operation = morph_operator + 2;

        Mat element = Imgproc.getStructuringElement( morph_elem,
                new Size( 2*morph_size + 1, 2*morph_size+1 ), new Point( morph_size, morph_size ) );

        Imgproc.morphologyEx( src, dst, operation, element );
        image.setImage(mat2Image(dst));
    }

    private Image mat2Image(Mat frame)
    {
        // create a temporary buffer
        MatOfByte buffer = new MatOfByte();
        // encode the frame in the buffer
        Imgcodecs.imencode(".png", frame, buffer);
        // build and return an Image created from the image encoded in the buffer
        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }
}
