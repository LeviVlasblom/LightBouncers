package lightbouncers.menu;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import lightbouncers.Main;
import lightbouncers.net.client.Client;
import lightbouncers.ui.GameView;

public class ConnectedNameMenu extends View {
    public ConnectedNameMenu(String title, int width, int height) {
        super(title, width, height);
    }

    private Pane root;
    private TextField txtName;
    private Text name;
    private Button join;
    private String userName;

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

    private void savedName(){
        SessionData.name = txtName.getText();
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
        txtName = new TextField();
        name = new Text("Username");
        join = new Button("Join");

        txtName.setTranslateX(this.getWidth() / 2 - 100);
        txtName.setTranslateY(this.getHeight() / 3 + 100);

        name.setFont(new Font("Arial", 24));
        name.setFill(Color.WHITE);
        name.setEffect(new DropShadow(30, Color.BLACK));
        name.setTranslateX(this.getWidth() / 2 - 90);
        name.setTranslateY(this.getHeight() / 3 + 70);


        join.setTranslateX(this.getWidth() / 2 - 130);
        join.setTranslateY(this.getHeight() / 3 + 140);

        txtName.getStyleClass().add("TextFieldMenu");
        join.getStyleClass().add("btnConnect");

        join.setOnAction(event -> {
            if (txtName.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ur username is EMPTY!", ButtonType.OK);
                alert.show();
            }
            else {
                userName = txtName.getText();
                if (Client.getInstance(SessionData.host, Integer.parseInt(SessionData.port), false).connect(txtName.getText())) {
                    System.out.println("Client connected!");
                    GameView gameView = new GameView(510, 645);
                    gameView.display();
                    this.close();
                }
            }
        });


        join.setFont(new Font("Arial", 28));
        join.setTextFill(Color.WHITE);



        root.getChildren().add(txtName);
        root.getChildren().add(name);
        root.getChildren().add(join);
    }
}
