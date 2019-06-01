package lightbouncers.net;

import java.io.Serializable;

public class CommandData<T extends Serializable> implements Serializable
{
    private String command;
    private T object;

    public CommandData(String command, T object)
    {
        this.command = command;
        this.object = object;
    }

    public String getCommand()
    {
        return this.command;
    }

    public T getObject()
    {
        return this.object;
    }

    public void setCommand(String command)
    {
        this.command = command;
    }

    public void setObject(T object)
    {
        this.object = object;
    }
}
