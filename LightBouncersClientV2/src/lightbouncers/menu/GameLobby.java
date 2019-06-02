package lightbouncers.menu;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.util.Pair;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameLobby extends View{
    public GameLobby(String title, int width, int height) {
        super(title, width, height);
    }

    private Pane root;
    private Rectangle rectangle;
    private Button btnReady;
    private List<Pair<String, Runnable>> menuData;
    private VBox menuBox;
    private VBox readyBox;
    private Line line;

    @Override
    void setupScene() {

        menuBox = new VBox(-5);
        readyBox = new VBox();
        root = new Pane();
        Scene scene = new Scene(createContent());
        scene.getStylesheets().add("lightbouncers/resources/StyleForm.css");
        this.setScene((scene));
        this.show();
    }

    private Parent createContent() {
        SessionData.playerNameSet.add(SessionData.name);
        showUsers(SessionData.playerNameSet);
        addBackground();
        addTitle();
        double lineX = 1920 / 2 - 200;
        double lineY = 1080 / 2 - 420;

        addMenu(lineX + 5, lineY + 5);
        startAnimation();

        return root;

    }

    private void addBackground() {
        ImageView imageView = new ImageView(new Image("lightbouncers/resources/NEON.png"));
        imageView.setFitWidth(this.getWidth());
        imageView.setFitHeight(this.getHeight());

        root.getChildren().add(imageView);
    }

    private void showUsers(ArrayList<String> username){
        menuData = new ArrayList<>();
        for (String names: username
             ) {
            menuData.add( new Pair<String, Runnable>(names, () -> {}));
        }
//        menuData = Arrays.asList(
//                new Pair<String, Runnable>("Single Player", () -> {}),
//                new Pair<String, Runnable>("Multiplayer", () -> {}),
//                new Pair<String, Runnable>("Game Options", () -> {}),
//                new Pair<String, Runnable>("Exit to Desktop", Platform::exit)
//
//        );
    }

    private void addTitle() {
        LightBounceTitle title = new LightBounceTitle("Light Bouncers");
        title.setTranslateX(this.getWidth() / 2 - title.getTitleWidth() / 2);
        title.setTranslateY(this.getHeight() / 3 - 300);

        root.getChildren().add(title);
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

        btnReady = new Button("Ready");


        menuBox.setTranslateX(x);
        menuBox.setTranslateY(y);
//        readyBox.setTranslateX(this.getWidth() /2 + 300);
//        readyBox.setTranslateY(menuBox.getTranslateY());
        menuData.forEach(data -> {
            LightBouncMenuItem item = new LightBouncMenuItem(data.getKey());
            item.setOnAction(data.getValue());
            item.setTranslateX(-300);

            javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(300, 30);
            clip.translateXProperty().bind(item.translateXProperty().negate());

            item.setClip(clip);

//            ImageView notReady = new ImageView(new Image(getClass().getResource("NoReady.png").toExternalForm()));
//            notReady.setFitWidth(50);
//            notReady.setFitHeight(50);
//
//            notReady.setTranslateX(item.getTranslateX() + item.getWidth() - 50);
//            notReady.setTranslateY(+35);
//
//            readyBox.getChildren().addAll(notReady);
            menuBox.getChildren().addAll(item);
        });

        rectangle = new Rectangle((int) this.getWidth() / 2 - 300, (int) this.getHeight() / 2 - 440, 500, 800 );
        rectangle.setEffect(new GaussianBlur(10 ));
        rectangle.setStroke(Color.color(1, 1, 1, 0.75));
        rectangle.setFill(Color.color(0,0,0,0.50));

        btnReady.setTranslateX(this.getWidth() / 2 - 130);
        btnReady.setTranslateY(this.getHeight() / 2 + 390);

        btnReady.getStyleClass().add("btnConnect");

        btnReady.setOnMouseClicked(event -> {

        });
        btnReady.setFont(new Font("Arial", 28));
        btnReady.setTextFill(Color.WHITE);

        root.getChildren().add(btnReady);
        root.getChildren().add(rectangle);
        root.getChildren().add(menuBox);
//        root.getChildren().add(readyBox);
    }
}
