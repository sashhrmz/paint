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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Paint");

        ToggleButton drawButton = new ToggleButton();
        drawButton.setText("Draw");
        drawButton.setMinSize(300, 20);

        ToggleButton eraserButton = new ToggleButton();
        eraserButton.setText("Eraser");
        eraserButton.setMinSize(300, 20);

        Button saveButton = new Button();
        saveButton.setText("Save");
        saveButton.setMinSize(300, 20);

        Button openButton = new Button();
        openButton.setText("Open");
        openButton.setMinSize(300, 20);

        ToggleGroup selectedButtons = new ToggleGroup();
        drawButton.setToggleGroup(selectedButtons);
        eraserButton.setToggleGroup(selectedButtons);

        Canvas drawingArea = new Canvas(800, 720);
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
        tileButtons.setPadding(new Insets(10, 100, 10, 100));
        tileButtons.setHgap(200);
        BackgroundFill backgroundFill = new BackgroundFill(Color.PINK, new CornerRadii(0),
                new Insets(0));
        tileButtons.setBackground(new Background(backgroundFill));
        tileButtons.getChildren().addAll(saveButton, openButton);

        TilePane paintButtons = new TilePane();
        paintButtons.setPadding(new Insets(10, 100, 10, 100));
        paintButtons.setHgap(200);
        paintButtons.setBackground(new Background(backgroundFill));
        paintButtons.getChildren().addAll(drawButton, eraserButton);

        Pane leftLine = new Pane();
        leftLine.setMinWidth(100);
        leftLine.setBackground(new Background(backgroundFill));

        Pane rightLine = new Pane();
        rightLine.setMinWidth(100);
        rightLine.setBackground(new Background(backgroundFill));

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
                    WritableImage writableImage = new WritableImage(800, 740);
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
        pane.setCenter(drawingBackground);

        Scene scene = new Scene(pane, 1000, 810);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
