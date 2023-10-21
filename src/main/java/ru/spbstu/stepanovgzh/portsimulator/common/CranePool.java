package ru.spbstu.stepanovgzh.portsimulator.common;

import java.util.*;
import java.util.concurrent.locks.*;
import org.apache.commons.lang3.RandomUtils;

public class CranePool
{
    public static final int START_COUNT_OF_CRANES_FOR_LIQUID = 1;
    public static final int START_COUNT_OF_CRANES_FOR_BULK = 1;
    public static final int START_COUNT_OF_CRANES_FOR_CONTAINER = 1;

    public static final long DELAY_OF_UNLOADING = 1440 * 60 * 1000;
    public static final int FINE_AMOUNT_PER_HOUR = 100;
    public static final int COST_OF_THE_CRANE = 30000;

    private Schedule schedule;
    private final ArrayList<Thread> allCranes = new ArrayList<>();
    private int countOfCranesForLiquid;
    private int countOfCranesForBulk;
    private int countOfCranesForContainer;
    private long fineForLiquidCranes;
    private long fineForBulkCranes;
    private long fineForContainerCranes;
    private final Lock lockerForCranesForLiquid = new ReentrantLock();
    private final Lock lockerForCranesForBulk = new ReentrantLock();
    private final Lock lockerForCranesForContainer = new ReentrantLock();

    private GregorianCalendar finishTimeOfUnloadingTheLastShipWithLiquid = null;
    private GregorianCalendar finishTimeOfUnloadingTheLastShipWithBulk = null;
    private GregorianCalendar finishTimeOfUnloadingTheLastShipWithContainer = null;

    public CranePool()
    {
        this.countOfCranesForLiquid = START_COUNT_OF_CRANES_FOR_LIQUID;
        this.countOfCranesForBulk = START_COUNT_OF_CRANES_FOR_BULK;
        this.countOfCranesForContainer = START_COUNT_OF_CRANES_FOR_CONTAINER;
        fineForLiquidCranes = 0;
        fineForBulkCranes = 0;
        fineForContainerCranes = 0;
    }

    private class UnloadingTask implements Runnable
    {
        @Override
        public void run()
        {
            for (Ship ship : schedule.getListOfShips())
            {
                if (ship.getCargo().getType() == Cargo.CargoType.LIQUID
                        && Thread.currentThread().getName().startsWith("craneForLiquid"))
                {
                    unloadShipWitLiquid(ship);
                }

                if (ship.getCargo().getType() == Cargo.CargoType.BULK
                        && Thread.currentThread().getName().startsWith("craneForBulk"))
                {
                    unloadShipWitBulk(ship);
                }

                if (ship.getCargo().getType() == Cargo.CargoType.CONTAINER
                        && Thread.currentThread().getName().startsWith("craneForContainer"))
                {
                    unloadShipWitContainer(ship);
                }
            }
        }
    }

    private void unloadShipWitContainer(Ship ship)
    {
        try
        {
            lockerForCranesForContainer.lock();

            if (ship.notUnloaded())
            {
                simulateShipUnloadingWithFillingInData(ship);
                updateFinishTimeOfUnloadingTheLastShip(ship);
                checkFineAndBuyCranesForContainer(ship);
            }
        }
        finally
        {
            lockerForCranesForContainer.unlock();
        }
    }

    private void unloadShipWitBulk(Ship ship)
    {
        try
        {
            lockerForCranesForBulk.lock();

            if (ship.notUnloaded())
            {
                simulateShipUnloadingWithFillingInData(ship);
                updateFinishTimeOfUnloadingTheLastShip(ship);
                checkFineAndBuyCranesForBulk(ship);
            }
        }
        finally
        {
            lockerForCranesForBulk.unlock();
        }
    }

    private synchronized void unloadShipWitLiquid(Ship ship)
    {
        try
        {
            lockerForCranesForLiquid.lock();

            if (ship.notUnloaded())
            {
                simulateShipUnloadingWithFillingInData(ship);
                updateFinishTimeOfUnloadingTheLastShip(ship);
                checkFineAndBuyCranesForLiquid(ship);
            }
        }
        finally
        {
            lockerForCranesForLiquid.unlock();
        }
    }

    private void simulateShipUnloadingWithFillingInData(Ship ship)
    {
        int countOfCranes = 0;

        switch (ship.getCargo().getType())
        {
            case LIQUID -> countOfCranes = countOfCranesForLiquid;
            case BULK -> countOfCranes = countOfCranesForBulk;
            case CONTAINER -> countOfCranes = countOfCranesForContainer;
        }

        long actualTimeForUnloading = calculateActualTimeForUnloadingForShip(ship) / countOfCranes;
        ship.setActualTimeForUnloading(actualTimeForUnloading);

        long waitingTimeForTheStartUnloading = calculateWaitingTimeForTheStartUnloadingForShip(ship);
        ship.setWaitingTimeForTheStartOfUnloading(waitingTimeForTheStartUnloading);

        GregorianCalendar unloadingStartTime = (GregorianCalendar) ship.getActualTimeShipArrival().clone();
        unloadingStartTime.add(Calendar.MINUTE, (int) (waitingTimeForTheStartUnloading / 1000 / 60));
        ship.setUnloadingStartTime(unloadingStartTime);

        GregorianCalendar unloadingFinishTime = (GregorianCalendar) unloadingStartTime.clone();
        unloadingFinishTime.add(Calendar.SECOND, (int) (actualTimeForUnloading / 1000));
        ship.setUnloadingFinishTime(unloadingFinishTime);
        
        long fine = calculateFineForShip(ship);
        ship.setFine(fine);

        ship.setUnloaded(true);
    }

    private void checkFineAndBuyCranesForContainer(Ship ship)
    {
        fineForContainerCranes += ship.getFine();

        int countOfNewCranes = (int) fineForContainerCranes / COST_OF_THE_CRANE;

        for (int i = 0; i < countOfNewCranes; i++)
        {
            countOfCranesForContainer++;
            fineForContainerCranes -= COST_OF_THE_CRANE;
        }
    }

    private void checkFineAndBuyCranesForBulk(Ship ship)
    {
        fineForBulkCranes += ship.getFine();

        int countOfNewCranes = (int) fineForBulkCranes / COST_OF_THE_CRANE;

        for (int i = 0; i < countOfNewCranes; i++)
        {
            countOfCranesForBulk++;
            fineForBulkCranes -= COST_OF_THE_CRANE;
        }
    }

    private void checkFineAndBuyCranesForLiquid(Ship ship)
    {
        fineForLiquidCranes += ship.getFine();

        int countOfNewCranes = (int) fineForLiquidCranes / COST_OF_THE_CRANE;

        for (int i = 0; i < countOfNewCranes; i++)
        {
            countOfCranesForLiquid++;
            fineForLiquidCranes -= COST_OF_THE_CRANE;
        }
    }

    private long calculateFineForShip(Ship ship)
    {
        long fine = (ship.getActualTimeForUnloading() + ship.getWaitingTimeForTheStartOfUnloading()
                - ship.getScheduledTimeForUnloading()) / 1000 / 60 / 60 * FINE_AMOUNT_PER_HOUR;

        return Math.max(fine, 0);
    }

    private void updateFinishTimeOfUnloadingTheLastShip(Ship ship)
    {
        switch (ship.getCargo().getType())
        {
            case LIQUID ->
            {
                finishTimeOfUnloadingTheLastShipWithLiquid = (GregorianCalendar) ship.getActualTimeShipArrival().clone();
                finishTimeOfUnloadingTheLastShipWithLiquid.add(Calendar.SECOND, (int) (ship.getWaitingTimeForTheStartOfUnloading() / 1000));
                finishTimeOfUnloadingTheLastShipWithLiquid.add(Calendar.SECOND, (int) (ship.getActualTimeForUnloading() / 1000));
            }
            case BULK ->
            {
                finishTimeOfUnloadingTheLastShipWithBulk = (GregorianCalendar) ship.getActualTimeShipArrival().clone();
                finishTimeOfUnloadingTheLastShipWithBulk.add(Calendar.SECOND, (int) (ship.getWaitingTimeForTheStartOfUnloading() / 1000));
                finishTimeOfUnloadingTheLastShipWithBulk.add(Calendar.SECOND, (int) (ship.getActualTimeForUnloading() / 1000));
            }
            case CONTAINER ->
            {
                finishTimeOfUnloadingTheLastShipWithContainer = (GregorianCalendar) ship.getActualTimeShipArrival().clone();
                finishTimeOfUnloadingTheLastShipWithContainer.add(Calendar.SECOND, (int) (ship.getWaitingTimeForTheStartOfUnloading() / 1000));
                finishTimeOfUnloadingTheLastShipWithContainer.add(Calendar.SECOND, (int) (ship.getActualTimeForUnloading() / 1000));
            }
        }
    }

    private long calculateActualTimeForUnloadingForShip(Ship ship)
    {
        return ship.getScheduledTimeForUnloading() + RandomUtils.nextLong(0, DELAY_OF_UNLOADING + 1);
    }

    private long calculateWaitingTimeForTheStartUnloadingForShip(Ship ship)
    {
        long waitingTimeForTheStartOfUnloading = 0;
        GregorianCalendar endOfUnloadingTheLastShip = new GregorianCalendar();

        switch(ship.getCargo().getType())
        {
            case LIQUID -> endOfUnloadingTheLastShip = finishTimeOfUnloadingTheLastShipWithLiquid;
            case BULK -> endOfUnloadingTheLastShip = finishTimeOfUnloadingTheLastShipWithBulk;
            case CONTAINER -> endOfUnloadingTheLastShip = finishTimeOfUnloadingTheLastShipWithContainer;
        }

        if (endOfUnloadingTheLastShip != null && ship.getActualTimeShipArrival().before(endOfUnloadingTheLastShip))
        {
            waitingTimeForTheStartOfUnloading =
                    endOfUnloadingTheLastShip.getTimeInMillis()
                            - ship.getActualTimeShipArrival().getTimeInMillis();
        }

        return waitingTimeForTheStartOfUnloading;
    }

    public void startUnloadBySchedule(Schedule schedule)
    {
        setSchedule(schedule);
        startAllCranes();
    }

    private void startAllCranes()
    {
        for (int i = 0; i < countOfCranesForLiquid; i++)
        {
            Thread t = new Thread(new UnloadingTask(), "craneForLiquid_#" + i);
            allCranes.add(t);
            t.start();
        }

        for (int i = 0; i < countOfCranesForBulk; i++)
        {
            Thread t = new Thread(new UnloadingTask(), "craneForBulk_#" + i);
            allCranes.add(t);
            t.start();
        }

        for (int i = 0; i < countOfCranesForContainer; i++)
        {
            Thread t = new Thread(new UnloadingTask(), "craneForContainer_#" + i);
            allCranes.add(t);
            t.start();
        }

        for (Thread t : allCranes)
        {
            try
            {
                t.join();
            }
            catch (InterruptedException iE)
            {
                iE.printStackTrace();
            }
        }
    }

    private void setSchedule(Schedule schedule)
    {
        for (Ship ship : schedule.getListOfShips())
        {
            assert ship.isArrived();
            assert ship.notUnloaded();
        }

        this.schedule = schedule;
    }

    public int getCountOfCranesForLiquid()
    {
        return countOfCranesForLiquid;
    }

    public int getCountOfCranesForBulk()
    {
        return countOfCranesForBulk;
    }

    public int getCountOfCranesForContainer()
    {
        return countOfCranesForContainer;
    }
}
