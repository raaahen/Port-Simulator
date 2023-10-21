package ru.spbstu.stepanovgzh.portsimulator.common;

public class Statistic
{
    private final int countOfUnloadedShips;
    private final long averageWaitingTimeInQueue;
    private final long maxDelayTimeOfUnloading;
    private final long averageDelayTimeOfUnloading;
    private final int totalFineAmount;
    private final int totalRequiredNumberOfBulkCranes;
    private final int totalRequiredNumberOfLiquidCranes;
    private final int totalRequiredNumberOfContainerCranes;

    public Statistic(Port port)
    {
        countOfUnloadedShips = port.getSchedule().getListOfShips().size();
        averageWaitingTimeInQueue = calculateAverageWaitingTimeInQueue(port.getSchedule());
        maxDelayTimeOfUnloading = searchMaxDelayTimeOfUnloading(port.getSchedule());
        averageDelayTimeOfUnloading = calculateAverageDelayTimeOfUnloading(port.getSchedule());
        totalFineAmount = calculateTotalFineAmount(port.getSchedule());
        totalRequiredNumberOfBulkCranes = port.getCranePool().getCountOfCranesForBulk();
        totalRequiredNumberOfLiquidCranes = port.getCranePool().getCountOfCranesForLiquid();
        totalRequiredNumberOfContainerCranes = port.getCranePool().getCountOfCranesForContainer();
    }

    private int calculateTotalFineAmount(Schedule unloadedShips)
    {
        int totalFineAmount = 0;

        for (Ship unloadedShip : unloadedShips.getListOfShips())
        {
            totalFineAmount += unloadedShip.getFine();
        }

        return totalFineAmount;
    }

    private long calculateAverageDelayTimeOfUnloading(Schedule unloadedShips)
    {
        long sumDelayTimeOfUnloading = 0;

        for (Ship ship : unloadedShips.getListOfShips())
        {
            sumDelayTimeOfUnloading += ship.getActualTimeForUnloading() - ship.getScheduledTimeForUnloading();
        }

        return sumDelayTimeOfUnloading / unloadedShips.getListOfShips().size();
    }

    private long calculateAverageWaitingTimeInQueue(Schedule unloadedShips)
    {
        long sumOfWaitingTimeOfAllShips = 0;

        for (Ship ship : unloadedShips.getListOfShips())
        {
            sumOfWaitingTimeOfAllShips += ship.getActualTimeForUnloading();
        }

        return sumOfWaitingTimeOfAllShips / unloadedShips.getListOfShips().size();
    }

    private long searchMaxDelayTimeOfUnloading(Schedule unloadedShips)
    {
        long delayTimeOfUnloadingOfCurrentShip;
        long maxDelayTimeOfUnloading = 0;

        for (Ship ship : unloadedShips.getListOfShips())
        {
            delayTimeOfUnloadingOfCurrentShip = ship.getActualTimeForUnloading() - ship.getScheduledTimeForUnloading();

            if (delayTimeOfUnloadingOfCurrentShip > maxDelayTimeOfUnloading)
                maxDelayTimeOfUnloading = delayTimeOfUnloadingOfCurrentShip;
        }

        return maxDelayTimeOfUnloading;
    }

    @Override
    public String toString()
    {
        return "Statistic{" +
                "countOfUnloadedShips=" + countOfUnloadedShips +
                ", averageWaitingTimeInQueue=" + averageWaitingTimeInQueue +
                ", maxDelayTimeOfUnloading=" + maxDelayTimeOfUnloading +
                ", averageDelayTimeOfUnloading=" + averageDelayTimeOfUnloading +
                ", totalFineAmount=" + totalFineAmount +
                ", totalRequiredNumberOfBulkCranes=" + totalRequiredNumberOfBulkCranes +
                ", totalRequiredNumberOfLiquidCranes=" + totalRequiredNumberOfLiquidCranes +
                ", totalRequiredNumberOfContainerCranes=" + totalRequiredNumberOfContainerCranes +
                '}';
    }
}
