package sample;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.concurrent.atomic.AtomicReference;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Paint");

        ToggleButton drawButton = new ToggleButton();
        drawButton.setText("Draw");
        drawButton.setMinSize(185, 20);
        drawButton.setSelected(true);

        ToggleButton eraserButton = new ToggleButton();
        eraserButton.setText("Eraser");
        eraserButton.setMinSize(185, 20);

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

        ColorPicker lineColor = new ColorPicker(Color.BLACK);
        ColorPicker backgroundColor = new ColorPicker(Color.WHITE);

        Canvas drawingArea = new Canvas(800, 720);
        GraphicsContext gc = drawingArea.getGraphicsContext2D();
        gc.setLineWidth(2);

        AtomicReference<StackPane> drawingBackground = new AtomicReference<>(new StackPane(drawingArea));
        BackgroundFill drawingAreaBackground = new BackgroundFill(backgroundColor.getValue(),
                new CornerRadii(0), new Insets(0));
        drawingBackground.get().setBackground(new Background(drawingAreaBackground));
        drawingArea.setOnMousePressed(e->{
            if (eraserButton.isSelected()) {
                gc.setLineWidth(10);
                gc.setStroke(backgroundColor.getValue());
            } else {
                gc.setLineWidth(2);
                gc.setStroke(lineColor.getValue());
                gc.beginPath();
                gc.lineTo(e.getX(), e.getY());
            }
        });

        drawingArea.setOnMouseDragged(e->{
            if (eraserButton.isSelected()) {
                gc.setLineWidth(10);
                gc.setStroke(backgroundColor.getValue());
            } else {
                gc.setLineWidth(2);
                gc.setStroke(lineColor.getValue());
                gc.lineTo(e.getX(), e.getY());
                gc.stroke();
            }
        });

        drawingArea.setOnMouseReleased(e->{
            if (eraserButton.isSelected()) {
                gc.setLineWidth(10);
                gc.setStroke(backgroundColor.getValue());
            } else {
                gc.setLineWidth(2);
                gc.setStroke(lineColor.getValue());
                gc.lineTo(e.getX(), e.getY());
                gc.stroke();
                gc.closePath();
            }
            });

        backgroundColor.setOnAction(e->{
            BackgroundFill drawingAreaBackground_ = new BackgroundFill(backgroundColor.getValue(),
                    new CornerRadii(0), new Insets(0));
            drawingBackground.get().setBackground(new Background(drawingAreaBackground_));
            gc.clearRect(0, 0, 800, 720);
        });

        TilePane tileButtons = new TilePane();
        tileButtons.setPadding(new Insets(10, 100, 10, 100));
        tileButtons.setHgap(40);
        BackgroundFill backgroundFill = new BackgroundFill(Color.PINK, new CornerRadii(0),
                new Insets(0));
        tileButtons.setBackground(new Background(backgroundFill));
        tileButtons.getChildren().addAll(clearButton, saveButton, openButton);

        TilePane paintButtons = new TilePane();
        paintButtons.setPadding(new Insets(10, 100, 10, 100));
        paintButtons.setHgap(20);
        paintButtons.setBackground(new Background(backgroundFill));
        paintButtons.getChildren().addAll(drawButton, eraserButton, lineColor, backgroundColor);

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
                    WritableImage writableImage = new WritableImage(1080, 790);
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
        pane.setCenter(drawingBackground.get());

        Scene scene = new Scene(pane, 1000, 810);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
