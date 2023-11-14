package ru.ewerestr.idrendezvous;

public class Command
{
    private String _command;
    private String _arg;

    public Command(String command, String arg)
    {
        _command = command;
        _arg = arg;
    }

    public String getCommand()
    {
        return _command;
    }

    public String getArgs()
    {
        return _arg;
    }
}
