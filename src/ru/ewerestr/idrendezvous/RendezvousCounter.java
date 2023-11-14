package ru.ewerestr.idrendezvous;

import java.util.Map;

public class RendezvousCounter implements Runnable
{
    private Rendezvous _instance;
    private int _peekInterval;

    public RendezvousCounter(Rendezvous instance, Integer peekInterval)
    {
        _instance = instance;
        _peekInterval = peekInterval;
    }

    public void run()
    {
        try
        {
            count();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private void count() throws InterruptedException
    {
        int currentTime;
        Map<Integer, Integer> map;
        while (true)
        {
            currentTime = (int)(System.currentTimeMillis()/1000);
            map = _instance.getLeaseTimeMap();
            for (int id : map.keySet()) if (currentTime >= map.get(id)) _instance.releaseId(id);
            Thread.sleep(_peekInterval * 1000L);
        }
    }
}
