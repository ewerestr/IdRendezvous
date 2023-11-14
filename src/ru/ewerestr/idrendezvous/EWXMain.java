package ru.ewerestr.idrendezvous;

import java.util.Scanner;

public class EWXMain
{
    private static Scanner sc;
    private static Rendezvous rd;

    public static void main(String[] args)
    {
        System.out.println("Program has been started");
        sc = new Scanner(System.in);
        rd = new Rendezvous(100, 3600, 10);
        System.out.println("Modules initialized");
        commandListener();
    }

    private static void commandListener()
    {
        System.out.println("Command listener has been started");
        while (true)
        {
            String line = sc.nextLine();
            if (line.toLowerCase().contains("stop")) break;
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
        if (line.contains(" ") || line.length() > 0) return true;
        return false;
    }

    private static Command parseCommand(String line)
    {
        String[] array = line.split(" ");
        if (line.contains(" ")) return new Command(array[0], array[1]);
        return new Command(array[0], "");
    }

    private static void handle(Command cmd)
    {
        switch(cmd.getCommand().toLowerCase())
        {
            case "setlimit":
            case "sl":
            {
                // temporary unused
                break;
            }
            case "setleasetime":
            case "slt":
            {
                // ad
                break;
            }
            case "setquantizationlevel":
            case "setquantlevel":
            case "sql":
            {
                //daw
                break;
            }
            case "start":
            {
                // waddaw
                break;
            }
            case "stop":
            {
                // dwdad
                break;
            }
            case "lease":
            case "ls":
            {
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
                boolean flag = rd.releaseId(Integer.parseInt(cmd.getArgs()));
                System.out.println("Id:" + cmd.getArgs() + (flag ? " has been successfully released" : " failed to release because it's already free"));
                break;
            }
            case "updatelease":
            case "ul":
            {
                // temporary unused
                break;
            }
            case "showfree":
            case "getfree":
            case "shf":
            case "sf":
            case "gf":
            {
                String free = rd.getFree();
                System.out.println("Free Ids: " + free);
                break;
            }
            case "showleasetime":
            case "shlt":
            {
                // dwaaa
                break;
            }
            case "showallleasetime":
            case "shalt":
            {
                // besa
                break;
            }
            default:
            {
                System.out.println("Unrecognized command: " + cmd.getCommand().toLowerCase());
                break;
            }
        }
    }
}
