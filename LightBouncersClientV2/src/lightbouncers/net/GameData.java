package lightbouncers.net;

import lightbouncers.game.Ball;
import lightbouncers.game.Player;

import java.io.Serializable;

public class GameData implements Serializable
{
    private Player player1;
    private Player player2;
    private Ball ball;

    public GameData(Player player1, Player player2, Ball ball)
    {
        this.player1 = player1;
        this.player2 = player2;
        this.ball = ball;
    }

    public Player getPlayer1()
    {
        return this.player1;
    }

    public Player getPlayer2()
    {
        return this.player2;
    }

    public Ball getBall()
    {
        return this.ball;
    }
}
