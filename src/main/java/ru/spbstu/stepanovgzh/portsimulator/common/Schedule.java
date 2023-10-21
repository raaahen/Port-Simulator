package ru.spbstu.stepanovgzh.portsimulator.common;

import java.util.*;

public class Schedule implements Cloneable
{
    protected static final int MIN_NUMBER_OF_SHIPS_ARRIVING_IN_ONE_DAY = 10;
    protected static final int MAX_NUMBER_OF_SHIPS_ARRIVING_IN_ONE_DAY = 25;
    protected static final int NUMBER_OF_DAYS_OF_THE_SCHEDULE = 30;
    protected static final int MIN_WEIGHT_OF_CARGO = 2500;
    protected static final int MAX_WEIGHT_OF_CARGO = 5000;
    protected static final int EFFICIENCY_PER_HOUR_OF_CRANE_FOR_BULK = 1000;
    protected static final int EFFICIENCY_PER_HOUR_OF_CRANE_FOR_LIQUID = 800;
    protected static final int EFFICIENCY_PER_HOUR_OF_CRANE_FOR_CONTAINER = 1200;

    private final ArrayList<Ship> listOfShips = new ArrayList<>();

    private Schedule()
    {
    }

    public static Schedule generateRandomSchedule()
    {
        Schedule schedule = new Schedule();
        int numberOfShips = createRandomNumberOfShips();

        for (int i = 0; i < numberOfShips; i++)
        {
            GregorianCalendar timeShipArrival = createRandomDateOfShipArrival();
            String nameOfShip = createNameOfShipWithNumber(i);
            Cargo cargo = createRandomCargo();
            long timeForUnloading = calculateTimeForUnloadingShipWith(cargo);
            schedule.listOfShips.add(new Ship(timeShipArrival, nameOfShip, cargo, timeForUnloading));
        }

        return schedule;
    }

    public ArrayList<Ship> getListOfShips()
    {
        return listOfShips;
    }

    private static int createRandomNumberOfShips()
    {
        return (MIN_NUMBER_OF_SHIPS_ARRIVING_IN_ONE_DAY
                + new Random().nextInt(MAX_NUMBER_OF_SHIPS_ARRIVING_IN_ONE_DAY) * NUMBER_OF_DAYS_OF_THE_SCHEDULE);
    }

    private static String createNameOfShipWithNumber(int number)
    {
        return "Ship_#" + number;
    }

    private static GregorianCalendar createRandomDateOfShipArrival()
    {
        GregorianCalendar randomDate = new GregorianCalendar();
        randomDate.add(Calendar.MINUTE, new Random().nextInt(NUMBER_OF_DAYS_OF_THE_SCHEDULE) * 24 * 60);

        return randomDate;
    }

    private static Cargo createRandomCargo()
    {
        Cargo.CargoType cargoType = Cargo.CargoType.values()[new Random().nextInt(Cargo.CargoType.values().length)];

        return new Cargo(cargoType, MIN_WEIGHT_OF_CARGO + new Random().nextInt(MAX_WEIGHT_OF_CARGO));
    }

    private static long calculateTimeForUnloadingShipWith(Cargo cargo)
    {
        long cargoWeight = cargo.getWeight();
        long efficiencyOfCrane = 0;

        switch (cargo.getType())
        {
            case BULK -> efficiencyOfCrane = EFFICIENCY_PER_HOUR_OF_CRANE_FOR_BULK;
            case LIQUID -> efficiencyOfCrane = EFFICIENCY_PER_HOUR_OF_CRANE_FOR_LIQUID;
            case CONTAINER -> efficiencyOfCrane = EFFICIENCY_PER_HOUR_OF_CRANE_FOR_CONTAINER;
        }

        //ВНИМАНИЕ: если cargoWeight < efficiencyOfCrane, то метод вернет 0 из-за округления в приведении типов
        return cargoWeight / efficiencyOfCrane * 60 * 60 * 1000;
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();

        for (Ship ship : listOfShips)
        {
            stringBuilder.append(ship.toString()).append("\n\n");
        }

        return stringBuilder.toString();
    }

    @Override
    public Schedule clone() throws CloneNotSupportedException
    {
        return (Schedule) super.clone();
    }
}
