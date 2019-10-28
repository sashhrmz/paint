package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;
import javafx.scene.shape.Line;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Paint");

        ToggleButton drawButton = new ToggleButton();
        drawButton.setText("Draw");
        drawButton.setMinWidth(300);

        ToggleButton eraserButton = new ToggleButton();
        eraserButton.setText("Eraser");
        eraserButton.setMinWidth(300);

        Button saveButton = new Button();
        saveButton.setText("Save");
        saveButton.setMinWidth(300);

        Button openButton = new Button();
        openButton.setText("Open");
        openButton.setMinWidth(300);

        ToggleGroup selectedButtons = new ToggleGroup();
        drawButton.setToggleGroup(selectedButtons);
        eraserButton.setToggleGroup(selectedButtons);

        Canvas drawingArea = new Canvas(750, 500);
        GraphicsContext gc = drawingArea.getGraphicsContext2D();
        gc.setLineWidth(10);

        StackPane drawingBackground = new StackPane(drawingArea);
        BackgroundFill drawingAreaBackground = new BackgroundFill(Color.WHITE,
                new CornerRadii(0), new Insets(0));
        drawingBackground.setBackground(new Background(drawingAreaBackground));
        drawingArea.setOnMousePressed(e->{
            if (drawButton.isSelected()) {
                gc.setStroke(Color.BLACK);
                gc.beginPath();
                gc.lineTo(e.getX(), e.getY());
            } else if(eraserButton.isSelected()) {
                gc.setStroke(Color.WHITE);
                gc.beginPath();
                gc.lineTo(e.getX(), e.getY());
            }
        });

        drawingArea.setOnMouseDragged(e->{
            if(drawButton.isSelected()) {
                gc.setStroke(Color.BLACK);
                gc.lineTo(e.getX(), e.getY());
                gc.stroke();
            } else if(eraserButton.isSelected()) {
                gc.setStroke(Color.WHITE);
                gc.lineTo(e.getX(), e.getY());
                gc.stroke();
            }
        });

        drawingArea.setOnMouseReleased(e->{
            if(drawButton.isSelected()) {
                gc.setStroke(Color.BLACK);
                gc.lineTo(e.getX(), e.getY());
                gc.stroke();
                gc.closePath();
            } else if(eraserButton.isSelected()) {
                gc.setStroke(Color.WHITE);
                gc.lineTo(e.getX(), e.getY());
                gc.stroke();
                gc.closePath();
            }});

        TilePane tileButtons = new TilePane();
        tileButtons.setPadding(new Insets(10, 50, 10, 50));
        tileButtons.setHgap(100);
        BackgroundFill backgroundFill = new BackgroundFill(Color.PINK, new CornerRadii(0),
                new Insets(0));
        tileButtons.setBackground(new Background(backgroundFill));
        tileButtons.getChildren().addAll(saveButton, openButton);

        TilePane paintButtons = new TilePane();
        paintButtons.setPadding(new Insets(10, 50, 10, 50));
        paintButtons.setHgap(100);
        paintButtons.setBackground(new Background(backgroundFill));
        paintButtons.getChildren().addAll(drawButton, eraserButton);


        Pane leftLine = new Pane();
        leftLine.setPadding(new Insets(10, 10, 10, 10));
        leftLine.setBackground(new Background(backgroundFill));

        Pane rightLine = new Pane();
        rightLine.setPadding(new Insets(10, 10, 10, 10));
        rightLine.setBackground(new Background(backgroundFill));

        BorderPane pane = new BorderPane();
        pane.setBottom(tileButtons);
        pane.setTop(paintButtons);
        pane.setLeft(leftLine);
        pane.setRight(rightLine);
        pane.setCenter(drawingBackground);

        Scene scene = new Scene(pane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
