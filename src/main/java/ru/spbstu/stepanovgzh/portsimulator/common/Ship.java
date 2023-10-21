package ru.spbstu.stepanovgzh.portsimulator.common;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class Ship implements Cloneable
{
    private final GregorianCalendar scheduledTimeShipArrival;
    private GregorianCalendar actualTimeShipArrival;
    private final String name;
    private final Cargo cargo;
    private final long scheduledTimeForUnloading;
    private long actualTimeForUnloading;
    private GregorianCalendar unloadingStartTime;
    private GregorianCalendar unloadingFinishTime;
    private long waitingTimeForTheStartOfUnloading;
    private long fine;

    private boolean isArrived;
    private  boolean isUnloaded;

    public Ship(GregorianCalendar scheduledTimeShipArrival, String name, Cargo cargo, long scheduledTimeForUnloading)
    {
        assert scheduledTimeForUnloading > 0;

        this.scheduledTimeShipArrival = scheduledTimeShipArrival;
        actualTimeShipArrival = null;
        this.name = name;
        this.cargo = cargo;
        this.scheduledTimeForUnloading = scheduledTimeForUnloading;
        actualTimeForUnloading = 0;
        unloadingStartTime = null;
        unloadingFinishTime = null;
        waitingTimeForTheStartOfUnloading = 0;
        fine = 0;
        isArrived = false;
        isUnloaded = false;
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

        stringBuilder.append("name='").append(name).append('\'').append('\n')
                .append("scheduledTimeShipArrival=").append(sdf.format(scheduledTimeShipArrival.getTime())).append('\n')
                .append("cargo=" + "{type=").append(cargo.getType()).append(", weight=").append(cargo.getWeight()).append("}\n")
                .append("scheduledTimeForUnloading=").append(formatTime(scheduledTimeForUnloading)).append('\n');

        if (this.isArrived)
            stringBuilder.append("actualTimeShipArrival=").append(sdf.format(actualTimeShipArrival.getTime())).append('\n');

        if (this.isUnloaded)
            stringBuilder.append("actualTimeForUnloading=").append(formatTime(actualTimeForUnloading)).append('\n')
                    .append("unloadingStartTime=").append(sdf.format(unloadingStartTime.getTime())).append('\n')
                    .append("unloadingFinishTime=").append(sdf.format(unloadingFinishTime.getTime())).append('\n')
                    .append("waitingTimeForTheStartOfUnloading=").append(formatTime(waitingTimeForTheStartOfUnloading)).append('\n')
                    .append("fine=").append(fine);

        return stringBuilder.toString();
    }

    public GregorianCalendar getScheduledTimeShipArrival()
    {
        if (scheduledTimeShipArrival != null)
        {
            return (GregorianCalendar) scheduledTimeShipArrival.clone();
        }
        else
        {
            return null;
        }
    }

    public GregorianCalendar getActualTimeShipArrival()
    {
        if (actualTimeShipArrival != null)
        {
            return (GregorianCalendar) actualTimeShipArrival.clone();
        }
        else
        {
            return null;
        }
    }

    public Cargo getCargo()
    {
        return cargo;
    }

    public long getScheduledTimeForUnloading()
    {
        return scheduledTimeForUnloading;
    }

    public long getActualTimeForUnloading()
    {
        return actualTimeForUnloading;
    }

    public long getWaitingTimeForTheStartOfUnloading()
    {
        return waitingTimeForTheStartOfUnloading;
    }

    public long getFine()
    {
        return fine;
    }

    public boolean isArrived()
    {
        return isArrived;
    }

    public boolean notUnloaded()
    {
        return !isUnloaded;
    }

    public void setActualTimeShipArrival(GregorianCalendar actualTimeShipArrival)
    {
        this.actualTimeShipArrival = actualTimeShipArrival;
    }

    public void setActualTimeForUnloading(long actualTimeForUnloading)
    {
        assert actualTimeForUnloading > 0;
        this.actualTimeForUnloading = actualTimeForUnloading;
    }

    public void setUnloadingStartTime(GregorianCalendar unloadingStartTime)
    {
        this.unloadingStartTime = unloadingStartTime;
    }

    public void setUnloadingFinishTime(GregorianCalendar unloadingFinishTime)
    {
        this.unloadingFinishTime = unloadingFinishTime;
    }

    public void setWaitingTimeForTheStartOfUnloading(long waitingTimeForTheStartOfUnloading)
    {
        this.waitingTimeForTheStartOfUnloading = waitingTimeForTheStartOfUnloading;
    }

    public void setFine(long fine)
    {
        assert fine >= 0;
        this.fine = fine;
    }

    public void setUnloaded(boolean unloaded)
    {
        isUnloaded = unloaded;
    }

    public void setArrived(boolean arrived)
    {
        isArrived = arrived;
    }

    @Override
    public Ship clone() throws CloneNotSupportedException
    {
        return (Ship) super.clone();
    }

    public static String formatTime(long mss)
    {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        return days + " days " + hours + " hours " + minutes + " minutes "
                + seconds + " seconds ";
    }
}
