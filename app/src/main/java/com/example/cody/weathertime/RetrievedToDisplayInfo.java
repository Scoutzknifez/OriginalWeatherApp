package com.example.cody.weathertime;

import java.util.ArrayList;

/**
 * Created by Cody on 12/30/2017.
 * Class for dealing with incoming data and checks that it parses correctly
 */

public class RetrievedToDisplayInfo
{
    public static ArrayList<WeatherForDay> dailyForecast = null;
    public static WeatherForDay current = null;

    public static int timesCaught = 0;

    public static boolean WeatherListIsValid()
    {
        if(dailyForecast == null)
        {
            return false;
        }

        // try to parse all the info in the polled info and overwrite what was there
        for (int i = 0; i < dailyForecast.size(); i++)
        {
            try {
                dailyForecast.get(i).setDayOfWeek(dailyForecast.get(i).getDayOfWeek().substring(0, 3));
            } catch(IndexOutOfBoundsException e) {
                timesCaught++;
                e.printStackTrace();
                return false;
            }
        }

        // if gets through all parsing, set return true which indicates to set the displayed to the new formatted poll lists, if returns false, the past sequence will still display
        return true;
    }
    public static boolean WeatherCurrentIsValid()
    {
        if (current == null)
        {
            return false;
        }

        // try to parse all the info in the polled info and overwrite what was there



        // if gets through all parsing, set return true which indicates to set the displayed to the new formatted poll lists, if returns false, the past sequence will still display

        return true;
    }
    public static void onRefresh()
    {
        //arrayList of forecast days
        if(WeatherMain.displayedWeathers == null)
        {
            return;
        } else {
            for(int i = 0; i < WeatherMain.displayedWeathers.size(); i++)
            {
                System.out.println(WeatherMain.displayedWeathers.get(i));
            }
            //today setting text
            WeatherMain.appMain.TodayHigh.setText("" + WeatherMain.displayedWeathers.get(0).getHighTemp());
            WeatherMain.appMain.TodayLow.setText("" + WeatherMain.displayedWeathers.get(0).getLowTemp());
            WeatherMain.appMain.TodayPercipitationChance.setText("" + WeatherMain.displayedWeathers.get(0).getPrecipitationChance() + "%");
            WeatherMain.appMain.TodayWind.setText(WeatherMain.displayedWeathers.get(0).getDirectionOfWind() + " at\n" + WeatherMain.displayedWeathers.get(0).getWindSpeed() + " mph");
            WeatherMain.appMain.TodayDayOfWeek.setText(WeatherMain.displayedWeathers.get(0).getDayOfWeek());
            WeatherMain.appMain.TodayDate.setText(WeatherMain.displayedWeathers.get(0).getDate());
            // day 1 setting text
            WeatherMain.appMain.DayHigh1.setText("" + WeatherMain.displayedWeathers.get(1).getHighTemp());
            WeatherMain.appMain.DayLow1.setText("" + WeatherMain.displayedWeathers.get(1).getLowTemp());
            WeatherMain.appMain.DayPercipitationChance1.setText("" + WeatherMain.displayedWeathers.get(1).getPrecipitationChance() + "%");
            WeatherMain.appMain.DayWind1.setText(WeatherMain.displayedWeathers.get(1).getDirectionOfWind() + " at\n" + WeatherMain.displayedWeathers.get(1).getWindSpeed() + " mph");
            WeatherMain.appMain.Day1.setText(WeatherMain.displayedWeathers.get(1).getDayOfWeek());
            WeatherMain.appMain.DayDate1.setText(WeatherMain.displayedWeathers.get(1).getDate());
            // day 2 setting text
            WeatherMain.appMain.DayHigh2.setText("" + WeatherMain.displayedWeathers.get(2).getHighTemp());
            WeatherMain.appMain.DayLow2.setText("" + WeatherMain.displayedWeathers.get(2).getLowTemp());
            WeatherMain.appMain.DayPercipitationChance2.setText("" + WeatherMain.displayedWeathers.get(2).getPrecipitationChance() + "%");
            WeatherMain.appMain.DayWind2.setText(WeatherMain.displayedWeathers.get(2).getDirectionOfWind() + " at\n" + WeatherMain.displayedWeathers.get(2).getWindSpeed() + " mph");
            WeatherMain.appMain.Day2.setText(WeatherMain.displayedWeathers.get(2).getDayOfWeek());
            WeatherMain.appMain.DayDate2.setText(WeatherMain.displayedWeathers.get(2).getDate());
            // day 3 setting text
            WeatherMain.appMain.DayHigh3.setText("" + WeatherMain.displayedWeathers.get(3).getHighTemp());
            WeatherMain.appMain.DayLow3.setText("" + WeatherMain.displayedWeathers.get(3).getLowTemp());
            WeatherMain.appMain.DayPercipitationChance3.setText("" + WeatherMain.displayedWeathers.get(3).getPrecipitationChance() + "%");
            WeatherMain.appMain.DayWind3.setText(WeatherMain.displayedWeathers.get(3).getDirectionOfWind() + " at\n" + WeatherMain.displayedWeathers.get(3).getWindSpeed() + " mph");
            WeatherMain.appMain.Day3.setText(WeatherMain.displayedWeathers.get(3).getDayOfWeek());
            WeatherMain.appMain.DayDate3.setText(WeatherMain.displayedWeathers.get(3).getDate());
            // day 4 setting text
            WeatherMain.appMain.DayHigh4.setText("" + WeatherMain.displayedWeathers.get(4).getHighTemp());
            WeatherMain.appMain.DayLow4.setText("" + WeatherMain.displayedWeathers.get(4).getLowTemp());
            WeatherMain.appMain.DayPercipitationChance4.setText("" + WeatherMain.displayedWeathers.get(4).getPrecipitationChance() + "%");
            WeatherMain.appMain.DayWind4.setText(WeatherMain.displayedWeathers.get(4).getDirectionOfWind() + " at\n" + WeatherMain.displayedWeathers.get(4).getWindSpeed() + " mph");
            WeatherMain.appMain.Day4.setText(WeatherMain.displayedWeathers.get(4).getDayOfWeek());
            WeatherMain.appMain.DayDate4.setText(WeatherMain.displayedWeathers.get(4).getDate());
            // day 5 setting text
            WeatherMain.appMain.DayHigh5.setText("" + WeatherMain.displayedWeathers.get(5).getHighTemp());
            WeatherMain.appMain.DayLow5.setText("" + WeatherMain.displayedWeathers.get(5).getLowTemp());
            WeatherMain.appMain.DayPercipitationChance5.setText("" + WeatherMain.displayedWeathers.get(5).getPrecipitationChance() + "%");
            WeatherMain.appMain.DayWind5.setText(WeatherMain.displayedWeathers.get(5).getDirectionOfWind() + " at\n" + WeatherMain.displayedWeathers.get(5).getWindSpeed() + " mph");
            WeatherMain.appMain.Day5.setText(WeatherMain.displayedWeathers.get(5).getDayOfWeek());
            WeatherMain.appMain.DayDate5.setText(WeatherMain.displayedWeathers.get(5).getDate());
            // day 6 setting text
            WeatherMain.appMain.DayHigh6.setText("" + WeatherMain.displayedWeathers.get(6).getHighTemp());
            WeatherMain.appMain.DayLow6.setText("" + WeatherMain.displayedWeathers.get(6).getLowTemp());
            WeatherMain.appMain.DayPercipitationChance6.setText("" + WeatherMain.displayedWeathers.get(6).getPrecipitationChance() + "%");
            WeatherMain.appMain.DayWind6.setText(WeatherMain.displayedWeathers.get(6).getDirectionOfWind() + " at\n" + WeatherMain.displayedWeathers.get(6).getWindSpeed() + " mph");
            WeatherMain.appMain.Day6.setText(WeatherMain.displayedWeathers.get(6).getDayOfWeek());
            WeatherMain.appMain.DayDate6.setText(WeatherMain.displayedWeathers.get(6).getDate());
            // single day used only for current image and current temp
            //set images with resourceIDs from getImageResource(index in weathers)
            WeatherMain.appMain.Day1Image.setImageResource(getImageResourceID(1));
            WeatherMain.appMain.Day2Image.setImageResource(getImageResourceID(2));
            WeatherMain.appMain.Day3Image.setImageResource(getImageResourceID(3));
            WeatherMain.appMain.Day4Image.setImageResource(getImageResourceID(4));
            WeatherMain.appMain.Day5Image.setImageResource(getImageResourceID(5));
            WeatherMain.appMain.Day6Image.setImageResource(getImageResourceID(6));
        }
        if(WeatherMain.displayedCurrent == null)
        {
            return;
        } else {
            WeatherMain.appMain.CurrentTemp.setText("" + WeatherMain.displayedCurrent.getHighTemp());
            WeatherMain.appMain.lastUpdated.setText(WeatherMain.displayedCurrent.getDate());
            // gets current
            WeatherMain.appMain.TodayImage.setImageResource(getImageResourceID(-1));
        }
    }
    public static int getImageResourceID(int IndexOfWeather)
    {
        int returned;
        if(IndexOfWeather == -1)
        {
            returned = WeatherMain.appMain.getResources().getIdentifier(WeatherMain.displayedCurrent.getIconName(), "drawable", WeatherMain.appMain.getPackageName());
        } else {
            returned = WeatherMain.appMain.getResources().getIdentifier(WeatherMain.displayedWeathers.get(IndexOfWeather).getIconName(), "drawable", WeatherMain.appMain.getPackageName());
        }
        return returned;
    }
}
