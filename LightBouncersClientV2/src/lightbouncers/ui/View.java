package lightbouncers.ui;

import javafx.stage.Stage;

public abstract class View extends Stage
{
    protected int width;
    protected int height;

    public View(int width, int height)
    {
        this.width = width;
        this.height = height;
        this.setWidth(width);
        this.setHeight(height);
        this.setOnCloseRequest(event -> onClose());
    }

    protected abstract void setupScene();

    public void display()
    {
        setupScene();
        this.show();
    }

    private void onClose()
    {
        System.exit(0);
    }
}
