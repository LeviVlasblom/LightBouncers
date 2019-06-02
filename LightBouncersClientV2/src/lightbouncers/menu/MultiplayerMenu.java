package lightbouncers.menu;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.Pair;

import javax.swing.text.Style;
import java.util.Arrays;
import java.util.List;

public class MultiplayerMenu extends View {
    public MultiplayerMenu(String title, int width, int height) {
        super(title, width, height);


    }

    private Effect shadow = new DropShadow(5, Color.BLACK);
    private Effect blur = new BoxBlur(1, 1, 3);

    private Pane root;

    private TextField txtIp;
    private TextField txtPoort;
    private Text textIp;
    private Text textPoort;
    private Button btnBack;
    private Button btnConnect;

    @Override
    void setupScene() {
        root = new Pane();
        Scene scene = new Scene(createContent());
        scene.getStylesheets().add("lightbouncers/resources/StyleForm.css");
        this.setScene((scene));
        this.show();
    }

    private Parent createContent() {
        addBackground();
        addTitle();
        addMenu();


        return root;
    }

    private void addBackground() {
        ImageView imageView = new ImageView(new Image("lightbouncers/resources/NEON.png"));
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

    private void addMenu() {
        textIp = new Text("IP");
        textPoort = new Text("PORT");
        txtPoort = new TextField();
        txtIp = new TextField();
        btnBack = new Button("Back");
        btnConnect = new Button("Connect");

        txtIp.setTranslateX(this.getWidth() / 2 - 100);
        txtIp.setTranslateY(this.getHeight() / 3 + 100);

        textIp.setFont(new Font("Arial", 24));
        textIp.setFill(Color.WHITE);
        textIp.setEffect(new DropShadow(30, Color.BLACK));
        textIp.setTranslateX(this.getWidth() / 2 - 100);
        textIp.setTranslateY(this.getHeight() / 3 + 70);

        textPoort.setFont(new Font("Arial", 24));
        textPoort.setFill(Color.WHITE);
        textPoort.setEffect(new DropShadow(30, Color.BLACK));
        textPoort.setTranslateX(this.getWidth() / 2 - 100);
        textPoort.setTranslateY(this.getHeight() / 3 + 170);

        btnBack.setTranslateX(this.getWidth() / 2 - 130);
        btnBack.setTranslateY(this.getHeight() / 3 + 330);
        btnConnect.setTranslateX(this.getWidth() / 2 - 130);
        btnConnect.setTranslateY(this.getHeight() / 3 + 250);

        txtIp.getStyleClass().add("TextFieldMenu");
        txtPoort.getStyleClass().add("TextFieldMenu");
        btnBack.getStyleClass().add("btnBack");
        btnConnect.getStyleClass().add("btnConnect");
        //btnBack.setStroke(Color.color(1, 1, 1, 0.75));

        txtPoort.setTranslateX(this.getWidth() / 2 - 100);
        txtPoort.setTranslateY(this.getHeight() / 3 + 200);

        btnBack.setOnMouseClicked(event -> {
            MainMenuView mmv = new MainMenuView("Light Bouncers", 1280, 720);
            this.close();
        });
        btnBack.setFont(new Font("Arial", 20));
        btnBack.setTextFill(Color.WHITE);

        btnConnect.setOnMouseClicked(event -> {
            if (txtPoort.getText().isEmpty() || txtIp.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Some Boxes Are EMPTY!", ButtonType.OK);
                alert.show();
            }
           else {
                SessionData.host = txtIp.getText();
                SessionData.port = txtPoort.getText();
                ConnectedNameMenu cnm = new ConnectedNameMenu("Light Bouncers", 1280, 720);
                this.close();
            }
        });
        btnConnect.setFont(new Font("Arial", 28));
        btnConnect.setTextFill(Color.WHITE);



        root.getChildren().add(textIp);
        root.getChildren().add(textPoort);
        root.getChildren().add(txtIp);
        root.getChildren().add(txtPoort);
        root.getChildren().add(btnBack);
        root.getChildren().add(btnConnect);
    }

}

