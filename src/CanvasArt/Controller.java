package CanvasArt;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.Stack;

public class Controller {

    @FXML private Slider redSlider;
    @FXML private Slider greenSlider;
    @FXML private Slider blueSlider;
    @FXML private Slider thicknessSlider;
    @FXML private Rectangle colorRectangle;
    @FXML private RadioButton lineRadioButton;
    @FXML private ToggleGroup shapeToggleGroup;
    @FXML private RadioButton rectangleRadioButton;
    @FXML private RadioButton ovalRadioButton;
    @FXML private RadioButton eraserRadioButton;
    @FXML private Button clearButton;
    @FXML private Canvas drawingAreaCanvas;

    private int red =0;
    private int green =0;
    private int blue =0;
    private double alpha =1.0;

    private GraphicsContext gc;

    Line line = new Line();
    Rectangle rectangle = new Rectangle();
    Ellipse ellipse = new Ellipse();

    Stack<Shape> history = new Stack();

    public void initialize(){

        gc = drawingAreaCanvas.getGraphicsContext2D();

        redSlider.valueProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                        red = newValue.intValue();
                        colorRectangle.setFill(Color.rgb(red,green,blue,alpha));

                    }
                }
        );
        greenSlider.valueProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                        green = newValue.intValue();
                        colorRectangle.setFill(Color.rgb(red,green,blue,alpha));

                    }
                }
        );
        blueSlider.valueProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                        blue = newValue.intValue();
                        colorRectangle.setFill(Color.rgb(red,green,blue,alpha));

                    }
                }
        );


        drawingAreaCanvas.setOnMousePressed(e -> {
            if (lineRadioButton.isSelected()){
                line.setStartX(e.getX());
                line.setEndY(e.getY());
                line.setEndX(e.getX());
                line.setEndY(e.getY());

            }else if (rectangleRadioButton.isSelected()){
                rectangle.setX(e.getX());
                rectangle.setY(e.getY());

            }else if(ovalRadioButton.isSelected()){
                ellipse.setCenterX(e.getX());
                ellipse.setCenterY(e.getY());
            }else if (eraserRadioButton.isSelected()){
                erase(e.getX(),e.getY());
            }
        });

        drawingAreaCanvas.setOnMouseDragged(e->{
            if(eraserRadioButton.isSelected()){
                erase(e.getX(),e.getY());
            }
        });

        drawingAreaCanvas.setOnMouseReleased(e->{
            gc.setStroke(Color.rgb(red,green,blue));

            if (lineRadioButton.isSelected()){
                line.setStrokeWidth(thicknessSlider.getValue());
                line.setFill(Color.rgb(red,green,blue));
                line.setEndX(e.getX());
                line.setEndY(e.getY());

                gc.setLineWidth(thicknessSlider.getValue());
                gc.strokeLine(line.getStartX(),line.getStartY(),line.getEndX(),line.getEndY());
                Line templine = new Line(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
                templine.setStroke(Color.rgb(red,green,blue));
                templine.setStrokeWidth(line.getStrokeWidth());
                history.push(templine);

            }
            else if(rectangleRadioButton.isSelected()){

                rectangle.setWidth(Math.abs(e.getX()-rectangle.getX()));
                rectangle.setHeight(Math.abs(e.getY()-rectangle.getY()));
                rectangle.setX(Math.min(rectangle.getX(),rectangle.getY()));
                rectangle.setY(Math.min(rectangle.getX(),rectangle.getY()));
                rectangle.setFill(Color.rgb(red,green,blue));
                gc.setFill(Color.rgb(red,green,blue));
                gc.fillRect(rectangle.getX(),rectangle.getY(),rectangle.getWidth(),rectangle.getHeight());
                gc.strokeRect(rectangle.getX(),rectangle.getY(),rectangle.getWidth(),rectangle.getHeight());

                Rectangle tempRect  = new Rectangle(rectangle.getX(),rectangle.getY(),rectangle.getWidth(),rectangle.getHeight());
                history.push(tempRect);


            }else if (ovalRadioButton.isSelected()){
                ellipse.setRadiusX((Math.abs(e.getX()-ellipse.getCenterX())));
                ellipse.setRadiusY(Math.abs(e.getY()-ellipse.getCenterY()));

                ellipse.setCenterX(Math.min(ellipse.getCenterX(),e.getX()));
                ellipse.setCenterY(Math.min(ellipse.getCenterY(),e.getY()));

                ellipse.setFill(Color.rgb(red,green,blue));
                gc.setFill(Color.rgb(red,green,blue));

                gc.fillOval(ellipse.getCenterX(), ellipse.getCenterY(), ellipse.getRadiusX(), ellipse.getRadiusY());
                gc.strokeOval(ellipse.getCenterX(), ellipse.getCenterY(), ellipse.getRadiusX(), ellipse.getRadiusY());
                Ellipse tempOval = new Ellipse(ellipse.getCenterX(), ellipse.getCenterY(), ellipse.getRadiusX(), ellipse.getRadiusY());
                tempOval.setFill(Color.rgb(red, green, blue));
                history.push(tempOval);

            } else if (eraserRadioButton.isSelected()) erase(e.getX(), e.getY());
        });


    }

    private void erase(double x, double y){
        double size = thicknessSlider.getValue();
        gc.clearRect(x-size/2,y-size/2,size,size);
    }



    @FXML void clearButtonPressed() {
        gc.clearRect(0, 0, drawingAreaCanvas.getWidth(), drawingAreaCanvas.getHeight());
        history.clear();

    }

}