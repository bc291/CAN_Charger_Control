package com.ktoto.bazio.chargercontrol.Model;

/**
 * Created by bazio on 09.05.2018.
 */

public class Statistics {
    private long id;

    private long totalChargingTime;
    private long totalOperations;
    private double averagePowerOfAll;
    private double totalCost;
    private double averageCost;
    private double averageInitialCapacity;

    public Statistics(long id, long totalChargingTime, long totalOperations, double averagePowerOfAll, double totalCost, double averageCost, double averageInitialCapacity) {
        this.id = id;
        this.totalChargingTime = totalChargingTime;
        this.totalOperations = totalOperations;
        this.averagePowerOfAll = averagePowerOfAll;
        this.totalCost = totalCost;
        this.averageCost = averageCost;
        this.averageInitialCapacity = averageInitialCapacity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTotalChargingTime() {
        return totalChargingTime;
    }

    public void setTotalChargingTime(long totalChargingTime) {
        this.totalChargingTime = totalChargingTime;
    }

    public long getTotalOperations() {
        return totalOperations;
    }

    public void setTotalOperations(long totalOperations) {
        this.totalOperations = totalOperations;
    }

    public double getAveragePowerOfAll() {
        return averagePowerOfAll;
    }

    public void setAveragePowerOfAll(double averagePowerOfAll) {
        this.averagePowerOfAll = averagePowerOfAll;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public double getAverageCost() {
        return averageCost;
    }

    public void setAverageCost(double averageCost) {
        this.averageCost = averageCost;
    }

    public double getAverageInitialCapacity() {
        return averageInitialCapacity;
    }

    public void setAverageInitialCapacity(double averageInitialCapacity) {
        this.averageInitialCapacity = averageInitialCapacity;
    }
}
