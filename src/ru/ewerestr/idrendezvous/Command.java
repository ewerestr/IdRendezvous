package ru.ewerestr.idrendezvous;

public class Command
{
    private String _command;
    private int _arg;

    public Command(String command, int arg)
    {
        _command = command;
        _arg = arg;
    }

    public String getCommand()
    {
        return _command;
    }

    public int getArgs()
    {
        return _arg;
    }
}
