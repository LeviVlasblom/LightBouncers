package lightbouncers.gui;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import lightbouncers.Main;
import lightbouncers.net.client.Client;
import lightbouncers.objects.Actor;
import lightbouncers.objects.environment.EnvironmentObject;
import lightbouncers.objects.environment.WallBox;
import lightbouncers.objects.lights.Light;
import lightbouncers.objects.pawns.characters.LightBouncer;
import lightbouncers.math.Vector2D;
import lightbouncers.world.LevelBuilder;
import lightbouncers.world.World;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;

public class Viewport extends Canvas
{
    protected Vector2D cursorPosition;
    //private LightBouncer player;
    //private LightBouncer test;
    //private Light light;
    //private ArrayList<Actor> environmentObjects;
    private World world;

    public Viewport(int width, int height)
    {
        this.setWidth(width);
        this.setHeight(height);

        this.setOnMousePressed(event -> onMousePressed(event));
        this.setOnMouseReleased(event -> onMouseReleased(event));
        this.setOnMouseMoved(event -> onMouseMoved(event));
        this.setOnMouseDragged(event -> onMouseMoved(event));

        this.world = new World();
        this.world.changeLevel(LevelBuilder.loadLevelFromFile(new File("src/lightbouncers/resources/levels/LevelStandard.json"), this.world));
//        this.player = new LightBouncer(new Vector2D(100, 100), 0.0, world, 2.0, 40.0, 1.0, Main.username);
//        this.world.setPlayer(this.player);

        Client client = Client.getInstance("localhost", 4509, null);
        client.connect("Kstrik");
        //client.setWorld(this.world);
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
        this.world.update(deltaTime);
    }

    private void draw()
    {
        this.clear();
        this.world.draw(this.getGraphicsContext2D());
    }

    private void updateCursorPosition(Vector2D mousePosition)
    {
        this.cursorPosition = mousePosition;
    }

    private void onMousePressed(MouseEvent event)
    {
        //this.player.onMousePressed(event);
        if(this.world.getPlayer() != null)
        {
            this.world.getPlayer().onMousePressed(event);
        }
        updateCursorPosition(new Vector2D((float)event.getX(), (float)event.getY()));
    }

    private void onMouseReleased(MouseEvent event)
    {
        //this.player.onMouseReleased(event);
        if(this.world.getPlayer() != null)
        {
            this.world.getPlayer().onMouseReleased(event);
        }
        updateCursorPosition(new Vector2D((float)event.getX(), (float)event.getY()));
    }

    private void onMouseMoved(MouseEvent event)
    {
        //this.player.onMouseMoved(event);
        if(this.world.getPlayer() != null)
        {
            this.world.getPlayer().onMouseMoved(event);
        }
        updateCursorPosition(new Vector2D((float)event.getX(), (float)event.getY()));
    }

    public void onKeyPressed(KeyEvent event)
    {
        //this.player.onKeyPressed(event);
        if(this.world.getPlayer() != null)
        {
            this.world.getPlayer().onKeyPressed(event);
        }
    }

    public void onKeyReleased(KeyEvent event)
    {
        //this.player.onKeyReleased(event);
        if(this.world.getPlayer() != null)
        {
            this.world.getPlayer().onKeyReleased(event);
        }
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