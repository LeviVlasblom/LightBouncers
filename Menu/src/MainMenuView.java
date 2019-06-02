import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;

public class MainMenuView extends View {

    private Pane root;
    private VBox menuBox;
    private Line line;

    private List<Pair<String, Runnable>> menuData;


    public MainMenuView(String title, int width, int height) {
        super(title, width, height);
    }

    @Override
    void setupScene() {
       menuData = Arrays.asList(
                new Pair<String, Runnable>("Single Player", () -> {}),
                new Pair<String, Runnable>("Multiplayer", () -> {multiStage();}),
                new Pair<String, Runnable>("Game Options", () -> {}),
                new Pair<String, Runnable>("Exit to Desktop", Platform::exit)

        );

        menuBox = new VBox(-5);
        root = new Pane();
        Scene scene = new Scene(createContent());
        this.setScene((scene));
        this.show();
    }





    private Parent createContent() {
        addBackground();
        addTitle();

        double lineX = this.getWidth() / 2 - 100;
        double lineY = this.getHeight() / 3 + 30;

        addLine(lineX, lineY);
        addMenu(lineX + 5, lineY + 5);

        startAnimation();

        return root;
    }

    private void addBackground() {
        ImageView imageView = new ImageView(new Image(getClass().getResource("NEON.png").toExternalForm()));
        imageView.setFitWidth(this.getWidth());
        imageView.setFitHeight(this.getHeight());

        root.getChildren().add(imageView);
    }

    private void addTitle() {
        LightBounceTitle title = new LightBounceTitle("Light Bouncers");
        title.setTranslateX(this.getWidth() / 2 - title.getTitleWidth() / 2);
        title.setTranslateY(this.getHeight() / 3);

        root.getChildren().add(title);
    }

    private void addLine(double x, double y) {
        line = new Line(x, y, x, y + 150);
        line.setStrokeWidth(3);
        line.setStroke(javafx.scene.paint.Color.color(1, 1, 1, 0.75));
        line.setEffect(new DropShadow(5, Color.BLACK));
        line.setScaleY(0);

        root.getChildren().add(line);
    }

    private void startAnimation() {
        ScaleTransition st = new ScaleTransition(Duration.seconds(1), line);
        st.setToY(1);
        st.setOnFinished(e -> {

            for (int i = 0; i < menuBox.getChildren().size(); i++) {
                Node n = menuBox.getChildren().get(i);

                TranslateTransition tt = new TranslateTransition(Duration.seconds(1 + i * 0.15), n);
                tt.setToX(0);
                tt.setOnFinished(e2 -> n.setClip(null));
                tt.play();
            }
        });
        st.play();
    }

    private void addMenu(double x, double y) {
        menuBox.setTranslateX(x);
        menuBox.setTranslateY(y);
        menuData.forEach(data -> {
            LightBouncMenuItem item = new LightBouncMenuItem(data.getKey());
            item.setOnAction(data.getValue());
            item.setTranslateX(-300);

            Rectangle clip = new Rectangle(300, 30);
            clip.translateXProperty().bind(item.translateXProperty().negate());

            item.setClip(clip);

            menuBox.getChildren().addAll(item);
        });

        root.getChildren().add(menuBox);
    }


    private void multiStage(){
        MultiplayerMenu mpm = new MultiplayerMenu("Light Bouncers", 1920, 1080);
        this.close();
    }
}
