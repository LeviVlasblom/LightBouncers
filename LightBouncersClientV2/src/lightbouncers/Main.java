package lightbouncers;

import javafx.application.Application;
import javafx.stage.Stage;
import lightbouncers.ui.MenuView;

public class Main extends Application
{
    public static String username;

    public static void main(String[] args)
    {
        launch(Main.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        MenuView menuView = new MenuView(510, 645);
        menuView.display();
    }
}
