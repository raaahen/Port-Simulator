package ru.spbstu.stepanovgzh.portsimulator.common;

import java.util.*;

public class Port
{
    public static final int MAX_ARRIVING_DELAY_FROM_SCHEDULE_IN_SEC = 7 * 24 * 60 * 60;
    public static final int MIN_ARRIVING_DELAY_FROM_SCHEDULE_IN_SEC = -7 * 24 * 60 * 60;
    private Schedule schedule;
    private CranePool cranePool;

    private Port()
    {
    }

    public static Port simulateWorkingAndGetData(Schedule schedule, CranePool cranePool)
    {
        Port port = new Port();

        for (Ship ship : schedule.getListOfShips())
        {
            assert ship.isArrived();
        }

        port.schedule = schedule;
        port.cranePool = cranePool;

        setActualTimeShipArrivalAndSort(schedule);
        cranePool.startUnloadBySchedule(schedule);

        return port;
    }

    private static void setActualTimeShipArrivalAndSort(Schedule schedule)
    {
        ArrayList<Ship> listOfShips = schedule.getListOfShips();
        for (int i = 0; i < listOfShips.size(); i++)
        {
            listOfShips.get(i).setArrived(true);

            //setting actual time ship arrival
            GregorianCalendar scheduledTimeShipArrival = listOfShips.get(i).getScheduledTimeShipArrival();
            listOfShips.get(i).setActualTimeShipArrival(generateTimeShipArrivalWithDelayOf(scheduledTimeShipArrival));

            //sort list by actual time ship arrival
            for (int j = 0; j < listOfShips.size(); j++)
            {
                if (listOfShips.get(i).getActualTimeShipArrival().before(listOfShips.get(j).getActualTimeShipArrival()))
                {
                    Collections.swap(listOfShips, i, j);
                }
            }
        }
    }

    private static GregorianCalendar generateTimeShipArrivalWithDelayOf(GregorianCalendar timeShipArrival)
    {
        var time = (GregorianCalendar) timeShipArrival.clone();
        time.add((GregorianCalendar.SECOND), MIN_ARRIVING_DELAY_FROM_SCHEDULE_IN_SEC
                + new Random().nextInt(MAX_ARRIVING_DELAY_FROM_SCHEDULE_IN_SEC - MIN_ARRIVING_DELAY_FROM_SCHEDULE_IN_SEC));
        return time;
    }

    public Schedule getSchedule()
    {
        return schedule;
    }

    public CranePool getCranePool()
    {
        return cranePool;
    }
}
