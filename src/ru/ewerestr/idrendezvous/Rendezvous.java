package ru.ewerestr.idrendezvous;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rendezvous
{
    private Thread _counterThread;

    private int _limit;
    private int _leaseTime;
    private int _quantizationLevel;
    private int _lastid;
    private List<Integer> _freeid;
    private Map<Integer, Integer> _leaseTimeMap;

    public Rendezvous(int limit, int leaseTime, int quantizationLevel)
    {
        _limit = limit;
        _leaseTime = leaseTime;
        _quantizationLevel = quantizationLevel;
        _lastid = 0;
        _freeid = new ArrayList<Integer>();
        _leaseTimeMap = new HashMap<Integer, Integer>();
        _counterThread = new Thread(new RendezvousCounter(this, _leaseTime/_quantizationLevel));
        // load actual data
        System.out.print("Starting RendezvousCounter thread... ");
        _counterThread.start();
        System.out.println("Done!");
        System.out.println("Rendezvous has been started");
    }

    public void stop()
    {
        try
        {
            if (_counterThread.isAlive()) _counterThread.stop();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public int leaseId()
    {
        if (_freeid.size() > 0)
        {
            int id = _freeid.get(0);
            _freeid.remove(0);
            _leaseTimeMap.put(id, ((int)(System.currentTimeMillis()/1000))+_leaseTime);
            return id;
        }
        if (_lastid < _limit)
        {
            _leaseTimeMap.put(_lastid, ((int)(System.currentTimeMillis()/1000))+_leaseTime);
            return _lastid++;
        }
        else return -1;
    }

    public boolean updateLeaseTime(int id)
    {
        if (_leaseTimeMap.containsKey(id))
        {
            _leaseTimeMap.put(id, ((int)(System.currentTimeMillis()/1000))+_leaseTime);
            return true;
        }
        return false;
    }

    public boolean releaseId(int id)
    {
        if (_lastid <= id || _freeid.contains(id)) return false;
        _freeid.add(id);
        _leaseTimeMap.remove(id);
        return true;
    }

    public boolean isFree(int id)
    {
        if (_lastid <= id || _freeid.contains(id)) return true;
        return false;
    }

    public Map<Integer, Integer> getLeaseTimeMap()
    {
        return _leaseTimeMap;
    }

    public String getFree()
    {
        String free = "";
        if (_freeid.size() > 0) for (int i = 0; i < _freeid.size(); i++) free += _freeid.get(i) + ", ";
        if (_lastid < _limit) free += _lastid == _limit-1 ? _lastid : _lastid + "-" + (_limit-1);
        else free = free.substring(0, free.length()-2);
        return free;
    }

    public String getLeasing(int id)
    {
        if (_leaseTimeMap.containsKey(id)) return _leaseTimeMap.get(id).toString();
        return ("id:" + id + " isn't leased");
    }

    public String getLeasingAll()
    {
        if (_leaseTimeMap.size() > 0)
        {
            String leases = "Current time: " + (int)(System.currentTimeMillis()/1000) + "\n";
            for (int k : _leaseTimeMap.keySet()) leases += ("id:" + k + " leased until " + _leaseTimeMap.get(k) + "\n");
            return leases;
        }
        return "There's no leased ids now";
    }
}
