package lightbouncers.gui;

import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public abstract class Window extends Stage
{
    private Group content;
    private AnchorPane gui;
    private Viewport viewport;

    public Window(int width, int height)
    {
        this.setWidth(width);
        this.setHeight(height);
        this.setupScene();
        this.gui = new AnchorPane();
        this.viewport = new Viewport(width, height);
        this.content = new Group(this.viewport, this.gui);
    }

    protected abstract void setupScene();
}
