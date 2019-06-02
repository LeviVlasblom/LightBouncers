package lightbouncers.menu;

import javafx.stage.Stage;

abstract class View extends Stage {

    public View(String title, int width, int height){
        this.setTitle(title);
        this.setWidth(width);
        this.setHeight(height);
        setupScene();
    }

    abstract void setupScene();
}
