package com.ktoto.bazio.chargercontrol.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ChargingOperationGet {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("carModel")
    @Expose
    private String carModel;
    @SerializedName("capacityCharged")
    @Expose
    private Double capacityCharged;
    @SerializedName("averagePower")
    @Expose
    private Double averagePower;
    @SerializedName("cost")
    @Expose
    private Double cost;
    @SerializedName("elapsedTime")
    @Expose
    private Double elapsedTime;

    @SerializedName("initialCapacity")
    @Expose
    private Double initialCapacity;

    @SerializedName("dateAndTime")
    @Expose
    private String dateAndTime;

    public Double getInitialCapacity() {
        return initialCapacity;
    }

    public void setInitialCapacity(Double initialCapacity) {
        this.initialCapacity = initialCapacity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public Double getCapacityCharged() {
        return capacityCharged;
    }

    public void setCapacityCharged(Double capacityCharged) {
        this.capacityCharged = capacityCharged;
    }

    public Double getAveragePower() {
        return averagePower;
    }

    public void setAveragePower(Double averagePower) {
        this.averagePower = averagePower;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(Double elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public ChargingOperationGet(Integer id, String carModel, Double capacityCharged, Double averagePower, Double cost, Double elapsedTime) {
        this.id = id;
        this.carModel = carModel;
        this.capacityCharged = capacityCharged;
        this.averagePower = averagePower;
        this.cost = cost;
        this.elapsedTime = elapsedTime;
    }
}
