package com.davidremington.stormy.models;


class Hourly {
    public Long time;
    public String summary;
    public String icon;
    public Integer nearestStormDistance;
    public Double precipIntensity;
    public Double precipIntensityError;
    public Double precipProbability;
    public String precipType;
    public Double temperature;
    public Double apparentTemperature;
    public Double dewPoint;
    public Double humidity;
    public Double windSpeed;
    public Integer windBearing;
    public Double visablity;
    public Double cloudCover;
    public Double pressure;
    public Double ozone;
    public Minutely minutely;
}
