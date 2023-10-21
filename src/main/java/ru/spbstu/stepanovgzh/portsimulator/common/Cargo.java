package ru.spbstu.stepanovgzh.portsimulator.common;

public class Cargo implements Cloneable
{
    private final CargoType type;
    private final int weight;

    public enum CargoType
    {
        LIQUID, BULK, CONTAINER
    }

    public Cargo(Cargo.CargoType type, int weight)
    {
        assert weight > 0.0;
        this.type = type;
        this.weight = weight;
    }

    @Override
    public Cargo clone() throws CloneNotSupportedException
    {
        return (Cargo) super.clone();
    }

    public int getWeight()
    {
        return weight;
    }

    public CargoType getType()
    {
        return type;
    }
}
