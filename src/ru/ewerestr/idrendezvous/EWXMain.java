package ru.ewerestr.idrendezvous;

import java.util.Scanner;

public class EWXMain
{
    private static Scanner sc;
    private static Rendezvous rd;
    private static boolean _rdAlive;
    private static int _limit;
    private static int _leaseTime;
    private static int _quantizationLevel;

    public static void main(String[] args)
    {
        System.out.println("Program has been started");
        _limit = 100; // Default value
        _leaseTime = 3600; // Default value
        _quantizationLevel = 10; // Default value
        _rdAlive = false; // Default value
        sc = new Scanner(System.in);
        System.out.println("Modules initialized");
        commandListener();
    }

    private static void commandListener()
    {
        System.out.println("Command listener has been started");
        while (true)
        {
            String line = sc.nextLine();
            if (line.equalsIgnoreCase("close") || line.equalsIgnoreCase("quit")) return;
            if (!checkCommand(line)) continue;
            handle(parseCommand(line));
        }
        /*
         * setlimit <INT>
         * setleasetime <INT>
         * setquantlevel <INT> (Must be < than leasetime)
         * start
         * stop
         *
         * lease
         * release <INT>
         * updatelease <INT>
         * isleased <INT> (Or better use isfree <INT> ?)
         * showfree \ getfree
         * showleasetime <INT>
         * showallleasetime
         *
         */
    }

    private static boolean checkCommand(String line)
    {
        if (line.contains(" ") || line.length() > 0)
        {
            String[] array = line.split(" ");
            if (array.length > 1) if (!isInt(array[1])) return false;
            return true;
        }
        return false;
    }

    private static Command parseCommand(String line)
    {
        String[] array = line.split(" ");
        if (line.contains(" ")) return new Command(array[0], Integer.parseInt(array[1]));
        return new Command(array[0], -1);
    }

    private static void handle(Command cmd)
    {
        switch(cmd.getCommand().toLowerCase())
        {
            case "setlimit":
            case "sl":
            {
                if (cmd.getArgs() < 0)
                {
                    System.out.println("Incorrect command usage! Use \"" + cmd.getCommand() + " <number>\"");
                    break;
                }
                _limit = cmd.getArgs();
                System.out.println("Success! New limit value has been set");
                if (_rdAlive) System.out.println("but the Rendezvous is working now, you need to restart it");
                break;
            }
            case "setleasetime":
            case "slt":
            {
                if (cmd.getArgs() < 0)
                {
                    System.out.println("Incorrect command usage! Use \"" + cmd.getCommand() + " <number>\"");
                    break;
                }
                _leaseTime = cmd.getArgs();
                System.out.println("Success! New leasing time value has been set");
                if (_rdAlive) System.out.println("but the Rendezvous is working now, you need to restart it");
                break;
            }
            case "setquantizationlevel":
            case "setquantlevel":
            case "sql":
            {
                if (cmd.getArgs() < 0)
                {
                    System.out.println("Incorrect command usage! Use \"" + cmd.getCommand() + " <number>\"");
                    break;
                }
                if (cmd.getArgs() > _leaseTime/2)
                {
                    System.out.println("Invalid value! Quantization level cannot be greater than half of leasing time value");
                    break;
                }
                _quantizationLevel = cmd.getArgs();
                System.out.println("Success! New quantization level value has been set");
                if (_rdAlive) System.out.println("but the Rendezvous is working now, you need to restart it");
                break;
            }
            case "start":
            {
                if (_rdAlive)
                {
                    System.out.println("Error! The Rendezvous is already started");
                    break;
                }
                start();
                break;
            }
            case "stop":
            {
                if (_rdAlive)
                {
                    stop();
                    System.out.println("Success! The Rendezvous has been stopped");
                    break;
                }
                System.out.println("The Rendezvous wasn't started before, there's nothing to stop");
                break;
            }
            case "lease":
            case "ls":
            {
                if (!_rdAlive)
                {
                    System.out.println("The Rendezvous doesn't work. You have to start it before");
                    break;
                }
                int id1 = rd.leaseId();
                if (id1 < 0)
                {
                    System.out.println("Cannot get free Id, may be they're over");
                    break;
                }
                System.out.println("Id:" + id1 + " has been leased");
                break;
            }
            case "release":
            case "rls":
            {
                if (!_rdAlive)
                {
                    System.out.println("The Rendezvous doesn't work. You have to start it before");
                    break;
                }
                if (cmd.getArgs() < 0)
                {
                    System.out.println("Incorrect command usage! Use \"" + cmd.getCommand() + " <number>\"");
                    break;
                }
                boolean flag = rd.releaseId(cmd.getArgs());
                System.out.println("Id:" + cmd.getArgs() + (flag ? " has been successfully released" : " failed to release because it's already free"));
                break;
            }
            case "updateleasetime":
            case "updatelease":
            case "ult":
            case "ul":
            {
                if (!_rdAlive)
                {
                    System.out.println("The Rendezvous doesn't work. You have to start it before");
                    break;
                }
                if (cmd.getArgs() < 0)
                {
                    System.out.println("Incorrect command usage! Use \"" + cmd.getCommand() + " <number>\"");
                    break;
                }
                if (rd.updateLeaseTime(cmd.getArgs())) System.out.println("Leasing time for id:" + cmd.getArgs() + " has been succesfully updated");
                else System.out.println("Cannot update leasing time for id:" + cmd.getArgs() + ". That Id isn't assigned");
                break;
            }
            case "showfree":
            case "getfree":
            case "shf":
            case "sf":
            case "gf":
            {
                if (!_rdAlive)
                {
                    System.out.println("The Rendezvous doesn't work. You have to start it before");
                    break;
                }
                String free = rd.getFree();
                System.out.println("Free Ids: " + free);
                break;
            }
            case "showleasetime":
            case "shlt":
            {
                if (!_rdAlive)
                {
                    System.out.println("The Rendezvous doesn't work. You have to start it before");
                    break;
                }
                if (cmd.getArgs() < 0)
                {
                    System.out.println("Incorrect command usage! Use \"" + cmd.getCommand() + " <number>\"");
                    break;
                }
                System.out.println(rd.getLeasing(cmd.getArgs()));

                break;
            }
            case "showallleasetime":
            case "shalt":
            {
                if (!_rdAlive)
                {
                    System.out.println("The Rendezvous doesn't work. You have to start it before");
                    break;
                }
                System.out.println(rd.getLeasingAll());
                break;
            }
            default:
            {
                System.out.println("Unrecognized command: " + cmd.getCommand().toLowerCase());
                break;
            }
        }
    }

    private static boolean isInt(String arg)
    {
        try
        {
            Integer.parseInt(arg);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    private static void start()
    {
        rd = new Rendezvous(_limit, _leaseTime, _quantizationLevel);
        _rdAlive = true;
    }

    private static void stop()
    {
        rd.stop();
        _rdAlive = false;
    }
}
