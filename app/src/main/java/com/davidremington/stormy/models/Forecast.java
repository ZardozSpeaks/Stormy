package com.davidremington.stormy.models;


public class Forecast {
    public Double latitude;
    public Double longitude;
    public String timezone;
    public Integer offset;
    public Currently currently;
    public Hourly hourly;
    public Minutely minutely;
}
