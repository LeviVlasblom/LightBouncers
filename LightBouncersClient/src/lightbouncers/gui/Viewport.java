package lightbouncers.gui;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import lightbouncers.objects.Actor;
import lightbouncers.objects.environment.EnvironmentObject;
import lightbouncers.objects.environment.WallBox;
import lightbouncers.objects.lights.Light;
import lightbouncers.objects.pawns.characters.LightBouncer;
import lightbouncers.math.Vector2D;

import java.util.ArrayList;

public class Viewport extends Canvas
{
    protected Vector2D cursorPosition;
    private LightBouncer player;
    //private LightBouncer test;
    private Light light;
    private ArrayList<Actor> environmentObjects;

    public Viewport(int width, int height)
    {
        this.setWidth(width);
        this.setHeight(height);

        this.setOnMousePressed(event -> onMousePressed(event));
        this.setOnMouseReleased(event -> onMouseReleased(event));
        this.setOnMouseMoved(event -> onMouseMoved(event));

        this.player = new LightBouncer(new Vector2D(100, 100), 0.0, 5.0, 40.0, 1.0);
        this.light = new Light(new Vector2D(100, 100), 600, 1.0, Color.rgb(173, 168, 65, 0.1), Color.YELLOW);

        this.environmentObjects = new ArrayList<Actor>();
        this.environmentObjects.add(new WallBox(new Vector2D(400, 400), 0, 50, 50));
        this.environmentObjects.add(new WallBox(new Vector2D(500, 400), 0, 50, 50));
        this.environmentObjects.add(new WallBox(new Vector2D(600, 400), 0, 50, 50));
        this.environmentObjects.add(new WallBox(new Vector2D(700, 400), 0, 50, 50));

//        this.test = new LightBouncer(new Vector2D(100, 100), 0.0, 5.0, 20.0, 1.0);
//        this.test.setParent(this.player);
//        this.test.setLocalPosition(new Vector2D(0.5, 0.5));

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
        this.player.update(deltaTime);
        this.light.setPosition(this.player.getWorldPosition());
        //this.test.update(deltaTime);
    }

    private void draw()
    {
        this.clear();
        this.light.draw(this.getGraphicsContext2D(), this.environmentObjects);
        this.player.draw(this.getGraphicsContext2D());
//        for(Actor actor : this.environmentObjects)
//        {
//            actor.draw(this.getGraphicsContext2D());
//        }
        //this.test.draw(this.getGraphicsContext2D());
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

    public void onKeyPressed(KeyEvent event)
    {
        this.player.onKeyPressed(event);
    }

    public void onKeyReleased(KeyEvent event)
    {
        this.player.onKeyReleased(event);
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