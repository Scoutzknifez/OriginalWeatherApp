package com.example.cody.weathertime;

/**
 * Created by Cody on 12/24/2017.
 * Weather for day object
 */

public class WeatherForDay
{
    private String DayOfWeek = "";
    private int highTemp = 0;
    private int lowTemp = 0;
    private int precipitationChance = 0;
    private int windSpeed = 0;
    private String directionOfWind = "";
    private String iconName = "";
    private String date = "00/00";

    public WeatherForDay(String DayOfWeek, int highTemp, int lowTemp, int precipitationChance, int windSpeed, String directionOfWind, String iconName, String date)
    {
        setDayOfWeek(DayOfWeek);
        setHighTemp(highTemp);
        setLowTemp(lowTemp);
        setPrecipitationChance(precipitationChance);
        setWindSpeed(windSpeed);
        setDirectionOfWind(directionOfWind);
        setIconName(iconName);
        setDate(date);
    }
    public WeatherForDay()
    {
        setDayOfWeek("null");
        setHighTemp(-999);
        setLowTemp(-999);
        setPrecipitationChance(-1);
        setWindSpeed(-1);
        setDirectionOfWind("null");
        setIconName("null");
        setDate("00/00");
    }
    public String getDayOfWeek()
    {
        return DayOfWeek;
    }
    public void setDayOfWeek(String dayOfWeek)
    {
        DayOfWeek = dayOfWeek;
    }
    public int getHighTemp()
    {
        return highTemp;
    }
    public void setHighTemp(int highTemp)
    {
        this.highTemp = highTemp;
    }
    public int getLowTemp()
    {
        return lowTemp;
    }
    public void setLowTemp(int lowTemp)
    {
        this.lowTemp = lowTemp;
    }
    public int getPrecipitationChance()
    {
        return precipitationChance;
    }
    public void setPrecipitationChance(int precipitationChance)
    {
        this.precipitationChance = precipitationChance;
    }
    public int getWindSpeed()
    {
        return windSpeed;
    }
    public void setWindSpeed(int windSpeed)
    {
        this.windSpeed = windSpeed;
    }
    public String getDirectionOfWind()
    {
        return directionOfWind;
    }
    public void setDirectionOfWind(String directionOfWind)
    {
        this.directionOfWind = directionOfWind;
    }
    public String getIconName()
    {
        return iconName;
    }
    public void setIconName(String iconName)
    {
        this.iconName = iconName;
    }
    public String getDate()
    {
        return date;
    }
    public void setDate(String date)
    {
        this.date = date;
    }
    public String toString()
    {
        return "Day Of Week:" + getDayOfWeek() + "|High:" + getHighTemp() + "|Low:" + getLowTemp() + "|Precipitation Chance:" + getPrecipitationChance() + "|Wind Speed:" + getWindSpeed() + "|Direction Of Wind:" +  getDirectionOfWind() + "|Icon Name:" + getIconName() + "|Date:" + getDate();
    }
}