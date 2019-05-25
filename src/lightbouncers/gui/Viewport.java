package lightbouncers.gui;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import lightbouncers.vectormath.Vector2D;
import org.omg.CORBA.PUBLIC_MEMBER;

public class Viewport extends Canvas
{
    protected Vector2D cursorPosition;

    public Viewport(int width, int height)
    {
        this.setWidth(width);
        this.setHeight(height);

        this.setOnMousePressed(event -> onMousePressed(event));
        this.setOnMouseReleased(event -> onMouseReleased(event));
        this.setOnMouseMoved(event -> onMouseMoved(event));
        this.setOnKeyPressed(event -> onKeyPressed(event));
        this.setOnKeyReleased(event -> onKeyReleased(event));

        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1) {
                    last = now;
                }
                update((now - last) / 1000000000.0);
                last = now;
                draw();
            }
        }.start();

    }

    private void update(double deltaTime)
    {

    }

    private void draw()
    {
        this.clear();
    }

    private void updateCursorPosition(Vector2D mousePosition)
    {
        this.cursorPosition = mousePosition;
    }

    private void onMousePressed(MouseEvent event)
    {
        updateCursorPosition(new Vector2D((float)event.getX(), (float)event.getY()));
    }

    private void onMouseReleased(MouseEvent event)
    {
        updateCursorPosition(new Vector2D((float)event.getX(), (float)event.getY()));
    }

    private void onMouseMoved(MouseEvent event)
    {
        updateCursorPosition(new Vector2D((float)event.getX(), (float)event.getY()));
    }

    private void onKeyPressed(KeyEvent event)
    {

    }

    private void onKeyReleased(KeyEvent event)
    {

    }

    public void onWidthChanged()
    {

    }

    public void onHeightChanged()
    {

    }

    private void clear()
    {
        this.getGraphicsContext2D().setFill(Color.BLACK);
        this.getGraphicsContext2D().fillRect(0, 0, this.getWidth(), this.getHeight());
    }
}