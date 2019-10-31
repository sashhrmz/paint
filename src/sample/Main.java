package sample;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Paint");

        ToggleButton drawButton = new ToggleButton();
        drawButton.setText("Draw");
        drawButton.setMinSize(140, 20);
        drawButton.setSelected(true);

        ToggleButton eraserButton = new ToggleButton();
        eraserButton.setText("Eraser");
        eraserButton.setMinSize(140, 20);

        Button clearButton = new Button();
        clearButton.setText("Clear");
        clearButton.setMinSize(240, 20);

        Button saveButton = new Button();
        saveButton.setText("Save");
        saveButton.setMinSize(240, 20);

        Button openButton = new Button();
        openButton.setText("Open");
        openButton.setMinSize(240, 20);

        ToggleGroup selectedButtons = new ToggleGroup();
        drawButton.setToggleGroup(selectedButtons);
        eraserButton.setToggleGroup(selectedButtons);

        Label label = new Label("brush size:");
        TextField textField = new TextField ();
        textField.setMaxWidth(100);
        textField.setText("2");
        HBox hb = new HBox();
        hb.getChildren().addAll(label, textField);

        ColorPicker lineColor = new ColorPicker(Color.BLACK);
        ColorPicker backgroundColor = new ColorPicker(Color.WHITE);

        Canvas drawingArea = new Canvas(800, 720);
        GraphicsContext gc = drawingArea.getGraphicsContext2D();
        gc.setLineWidth(2);

        gc.clearRect(0, 0, 800, 720);
        gc.setFill(backgroundColor.getValue());
        gc.fillRect(0, 0, 800, 720);


        gc.setLineJoin(StrokeLineJoin.ROUND);
        gc.setLineCap(StrokeLineCap.ROUND);

        drawingArea.setOnMousePressed(e->{
            double size = Double.parseDouble(textField.getText());
            gc.setLineWidth(size);
            textField.end();
            if (eraserButton.isSelected()) {
                gc.setStroke(backgroundColor.getValue());
            } else {
                gc.setStroke(lineColor.getValue());
            }
            gc.beginPath();
            gc.lineTo(e.getX(), e.getY());
        });

        drawingArea.setOnMouseDragged(e->{
            if (eraserButton.isSelected()) {
                gc.setStroke(backgroundColor.getValue());
            } else {
                gc.setStroke(lineColor.getValue());
            }
            gc.lineTo(e.getX(), e.getY());
            gc.stroke();
        });

        drawingArea.setOnMouseReleased(e->{
            if (eraserButton.isSelected()) {
                gc.setStroke(backgroundColor.getValue());
            } else {
                gc.setStroke(lineColor.getValue());
            }
            gc.lineTo(e.getX(), e.getY());
            gc.stroke();
            gc.closePath();
            });

        backgroundColor.setOnAction(e->{
            gc.clearRect(0, 0, 800, 720);
            gc.setFill(backgroundColor.getValue());
            gc.fillRect(0, 0, 800, 720);
        });

        TilePane tileButtons = new TilePane();
        tileButtons.setPadding(new Insets(10, 100, 10, 100));
        tileButtons.setHgap(40);
        BackgroundFill backgroundFill = new BackgroundFill(Color.PINK, new CornerRadii(0),
                new Insets(0));
        tileButtons.setBackground(new Background(backgroundFill));
        tileButtons.getChildren().addAll(clearButton, saveButton, openButton);

        TilePane paintButtons = new TilePane();
        paintButtons.setPadding(new Insets(10, 60, 10, 60));
        paintButtons.setHgap(1);
        paintButtons.setBackground(new Background(backgroundFill));
        paintButtons.getChildren().addAll(drawButton, eraserButton, lineColor, backgroundColor, hb);

        Pane leftLine = new Pane();
        leftLine.setMinWidth(100);
        leftLine.setBackground(new Background(backgroundFill));

        Pane rightLine = new Pane();
        rightLine.setMinWidth(100);
        rightLine.setBackground(new Background(backgroundFill));


        clearButton.setOnAction((e)->{
            gc.clearRect(0, 0, 800, 720);
        });

        openButton.setOnAction((e)->{
            FileChooser openFile = new FileChooser();
            openFile.setTitle("Open File");
            File file = openFile.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    InputStream io = new FileInputStream(file);
                    Image img = new Image(io);
                    gc.drawImage(img, 0, 0);
                } catch (IOException ex) {
                    System.out.println("Error!");
                }
            }
        });

        saveButton.setOnAction((e)->{
            FileChooser savefile = new FileChooser();
            savefile.setTitle("Save File");

            File file = savefile.showSaveDialog(primaryStage);
            if (file != null) {
                try {
                    WritableImage writableImage = new WritableImage(800, 720);
                    drawingArea.snapshot(null, writableImage);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    ImageIO.write(renderedImage, "png", file);
                } catch (IOException ex) {
                    System.out.println("Error!");
                }
            }

        });

        BorderPane pane = new BorderPane();
        pane.setBottom(tileButtons);
        pane.setTop(paintButtons);
        pane.setLeft(leftLine);
        pane.setRight(rightLine);
        pane.setCenter(drawingArea);

        Scene scene = new Scene(pane, 1000, 810);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
