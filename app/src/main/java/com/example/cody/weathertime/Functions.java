package com.example.cody.weathertime;

/**
 * Created by Cody on 1/3/2018.
 * deals with updating and parsing information for weather
 */

import android.location.Location;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Functions
{
    public static ArrayList<Integer> precipChances = null;
    public static byte forecastedDays = 7;

    public static JSONObject currentJSON = null;
    public static JSONArray dailyForecastJSONArray = null;
    public static JSONArray hourlyForecastJSONArray = null;

    public static boolean updateWeather()
    {
        try {
            if(!getContentsFromJSONFile())
            {
                return false;
            }
        } catch(JSONException | IOException e) {
            e.printStackTrace();
            return false;
        }

        //does forecast

        //updates the precipitation chances for each day of forecast correctly,
        // the precipProbability in each daily JSONObject from the file is inaccurate.
        try {
            updatePrecipitationChanceList();

            ArrayList<WeatherForDay> newForecastArray = new ArrayList<>();
            for(int i = 0; i < dailyForecastJSONArray.length(); i++)
            {
                newForecastArray.add(getWeather(dailyForecastJSONArray.getJSONObject(i), i));
            }
            RetrievedToDisplayInfo.dailyForecast = newForecastArray;

            //does current
            RetrievedToDisplayInfo.current = getCurrentWeather(currentJSON);


            // debugging stuff
            System.out.println("FORECAST:");
            for(WeatherForDay w: RetrievedToDisplayInfo.dailyForecast)
            {
                System.out.println(w);
            }
            System.out.println("CURRENT");
            System.out.println(RetrievedToDisplayInfo.current);
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
    public static boolean getContentsFromJSONFile() throws JSONException, IOException
    {
        Location loc = WeatherMain.appMain.mCurrentLocation;
        if(loc == null)
        {
            WeatherMain.appMain.NotificationForLocationServicesBeingDisabled.setVisibility(View.VISIBLE);
            WeatherMain.appMain.shouldViewsGoVisible = false;
            return false;
        }
        double lat = loc.getLatitude();
        double lon = loc.getLongitude();
        System.out.println(lat + ":" + lon);
        // location overrides
        /*lat = 21.578;
        lon = -80.2955;*/

        String requestURL = Constants.url + lat + "," + lon + Constants.api_args;

        JSONObject json;

        json = JsonReader.readJsonFromUrl(requestURL);

		/*String[] elements = json.toString().split(",");
		for(int i = 0; i < elements.length; i++)
		{
			System.out.println(i + ": " + elements[i]);
		}*/

        currentJSON = (JSONObject) json.get("currently");

        JSONObject dailyJSON = (JSONObject) json.get("daily");

        dailyForecastJSONArray = (JSONArray) dailyJSON.get("data");

        json = (JSONObject) json.get("hourly");

        hourlyForecastJSONArray = (JSONArray) json.get("data");

        return true;
    }
    public static WeatherForDay getWeather(JSONObject j, int dayKey)
    {
        String dayOfWeek = "Error Fetching";
        Calendar calendar = Calendar.getInstance();
        int dayIndex = (calendar.get(Calendar.DAY_OF_WEEK) + (dayKey)) % 7;
        switch(dayIndex)
        {
            case Calendar.SUNDAY:
                dayOfWeek = "Sunday";
                break;
            case Calendar.MONDAY:
                dayOfWeek = "Monday";
                break;
            case Calendar.TUESDAY:
                dayOfWeek = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                dayOfWeek = "Wednesday";
                break;
            case Calendar.THURSDAY:
                dayOfWeek = "Thursday";
                break;
            case Calendar.FRIDAY:
                dayOfWeek = "Friday";
                break;
            case 0:
                //Calendar.SATURDAY
                dayOfWeek = "Saturday";
                break;
        }
        /*try{
            day = (listOfOutput.get(getIndexOf("weekday", listOfOutput, referenceIndex)).split(">")[1].trim());
        } catch(IndexOutOfBoundsException e) {
            e.printStackTrace();
        }*/

        //try to get high from certain period of information
        double realHigh = 0;
        int high = 0;
        try{
            realHigh = (double) j.get("temperatureMax");
            high = (int) realHigh;
            if((realHigh % 1)  + .000000000001 >= .5)
            {
                high++;
            }
        } catch(NumberFormatException e) {
            e.printStackTrace();
        } catch(JSONException e) {
            e.printStackTrace();
        } catch(NullPointerException e) {
            e.printStackTrace();
        } catch(ClassCastException e) {
            try {
                high = (int) j.get("temperatureMax");
            } catch(JSONException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

        //try to get low from certain period of information
        double realLow = 0;
        int low = 0;
        try{
            realLow = (double) j.getDouble("temperatureMin");
            low = (int) realLow;
            if((realLow % 1)  + .000000000001 >= .5)
            {
                low++;
            }
        } catch(NumberFormatException e) {
            e.printStackTrace();
        } catch(JSONException e) {
            e.printStackTrace();
        } catch(NullPointerException e) {
            e.printStackTrace();
        } catch(ClassCastException e) {
            try {
                low = (int) j.get("temperatureMin");
            } catch(JSONException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

        //try to get chance of precipitation from certain period of information
        int percipitationChance = 0;
        if(dayKey < precipChances.size())
        {
            try {
                percipitationChance = precipChances.get(dayKey);
            } catch(IndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch(NullPointerException e) {
                e.printStackTrace();
            }
        }
        /*double realChance = 0;
        int percipitationChance = 0;
        try{
            realChance = j.getDouble("precipProbability") * 100;
            percipitationChance = (int) realChance;
            if((realChance % 1)  + .000000000001 >= .5)
            {
                percipitationChance++;
            }
        } catch(NumberFormatException e) {
            e.printStackTrace();
        } catch(NullPointerException e) {
            e.printStackTrace();
        } catch(ClassCastException e) {
            e.printStackTrace();
        }*/

        //try to get wind speed from certain period of information
        double realSpeed = 0;
        int windSpeed = 0;
        try {
            realSpeed = Double.valueOf((double) j.get("windSpeed"));
            //realSpeed = new Double((double) j.get("windSpeed"));
            windSpeed = (int) realSpeed;
            if((realSpeed % 1)  + .000000000001 >= .5)
            {
                windSpeed++;
            }
        } catch(NumberFormatException e) {
            e.printStackTrace();
        } catch(JSONException e) {
            e.printStackTrace();
        } catch(NullPointerException e) {
            e.printStackTrace();
        } catch(ClassCastException e) {
            try {
                windSpeed = (int) j.get("windSpeed");
            } catch(JSONException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

        //try to get wind direction from certain period of information
        String windDir = "Error Fetching";
        try {
            windDir = getCardinalDirection(j.getInt("windBearing"));
        } catch(IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch(JSONException e) {
            e.printStackTrace();
        }

        //try to get wind direction from certain period of information
        String iconName = "Error Fetching";
        try {
            iconName = (String) j.get("icon");
            iconName = getRealIconName(iconName);
            if(iconName.contains("night"))
            {
                iconName = iconName.replaceAll("night","day");
            }
        } catch(IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch(JSONException e) {
            e.printStackTrace();
        }

        //try to get date from unix time
        long unixTime = 0;
        try {
            unixTime = new Long((int) j.get("time"));
        } catch(NumberFormatException e) {
            e.printStackTrace();
        } catch(JSONException e) {
            e.printStackTrace();
        }

        String date = new java.text.SimpleDateFormat("M/d").format(new java.util.Date(unixTime*1000));

        //String DayOfWeek, int highTemp, int lowTemp, int percipitationChance, int windSpeed, String directionOfWind, String iconName, String date
        return (new WeatherForDay(dayOfWeek, high, low, percipitationChance, windSpeed, windDir, iconName, date));
    }
    public static WeatherForDay getCurrentWeather(JSONObject j)
    {
        String dayOfWeek = "Error Fetching";
        Calendar calendar = Calendar.getInstance();
        int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
        switch(dayIndex)
        {
            case Calendar.SUNDAY:
                dayOfWeek = "Sunday";
                break;
            case Calendar.MONDAY:
                dayOfWeek = "Monday";
                break;
            case Calendar.TUESDAY:
                dayOfWeek = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                dayOfWeek = "Wednesday";
                break;
            case Calendar.THURSDAY:
                dayOfWeek = "Thursday";
                break;
            case Calendar.FRIDAY:
                dayOfWeek = "Friday";
                break;
            case Calendar.SATURDAY:
                dayOfWeek = "Saturday";
                break;
        }

        //try to get high from certain period of information
        double realHigh = 0;
        int high = 0;
        try {
            realHigh = (double) j.get("temperature");
            //realHigh = (double) j.get("temperature");
            high = (int) realHigh;
            if((realHigh % 1)  + .000000000001 >= .5)
            {
                high++;
            }
        } catch(NumberFormatException e) {
            e.printStackTrace();
        } catch(JSONException e) {
            e.printStackTrace();
        } catch(NullPointerException e) {
            e.printStackTrace();
        } catch(ClassCastException e) {
            try {
                high = (int) j.get("temperature");
            } catch(JSONException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

        //try to get current wind speed from certain period of information
        double realWind =  0;
        int windSpeed = 0;
        try {
            realWind = (double) j.get("windSpeed");
            windSpeed = (int) realWind;
            if((realWind % 1)  + .000000000001 >= .5)
            {
                windSpeed ++;
            }
        } catch(NumberFormatException e) {
            e.printStackTrace();
        } catch(JSONException e) {
            e.printStackTrace();
        } catch(NullPointerException e) {
            e.printStackTrace();
        } catch(ClassCastException e) {
            try {
                windSpeed = (int) j.get("windSpeed");
            } catch(JSONException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

        //try to get current wind direction from certain period of information
        String windDir = "Error Fetching";
        try {
            windDir = getCardinalDirection(j.getInt("windBearing"));
        } catch(ClassCastException e) {
            e.printStackTrace();
        } catch(JSONException e) {
            e.printStackTrace();
        }

        /* try to get current icon type from certain period of information
         * NOTE: doesn't fetch from the icon section but instead the icon_url section,
         * which requires it to be broken down further by passing into getIconName(urlofimage);
         */
        String iconName = "Error Fetching";
        try {
            iconName = (String) j.get("icon");
            iconName = getRealIconName(iconName);
        } catch(ClassCastException e) {
            e.printStackTrace();
        } catch(JSONException e) {
            e.printStackTrace();
        }

        //try to get observation time from certain period of information
        long unixTime = 0;
        try {
            unixTime = new Long((int) j.get("time"));
        } catch(NumberFormatException e) {
            e.printStackTrace();
        } catch(JSONException e) {
            e.printStackTrace();
        }

        String date = new java.text.SimpleDateFormat("MMMM d H:mm a z").format(new java.util.Date(unixTime*1000));

        try {
            String[] dateSplits = date.split(" ");
            if(dateSplits[3].equalsIgnoreCase("PM"))
            {
                try {
                    int extractedHour = 0;

                    extractedHour = Integer.parseInt(dateSplits[2].split(":")[0]);
                    if(extractedHour > 12)
                    {
                        extractedHour -= 12;

                        dateSplits[2] = extractedHour + ":" + dateSplits[2].split(":")[1];
                    }
                } catch(NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            else if(dateSplits[3].equalsIgnoreCase("AM"))
            {
                try {
                    int extractedHour = 0;

                    extractedHour = Integer.parseInt(dateSplits[2].split(":")[0]);
                    if(extractedHour == 0)
                    {
                        extractedHour = 12;

                        dateSplits[2] = extractedHour + ":" + dateSplits[2].split(":")[1];
                    }
                } catch(NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            date = "Last Updated on " + dateSplits[0] + " " + dateSplits[1] + ", " + dateSplits[2] + " " + dateSplits[3] + " " + dateSplits[4];
        } catch(IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        //date = getMonthDay(date);

        //creates and returns object, high is current temp.
        return (new WeatherForDay(dayOfWeek, high, 0, 0, windSpeed, windDir, iconName, date));
    }
    public static String getCardinalDirection(int bearingInDegrees)
    {
        if (bearingInDegrees > 11.25 && bearingInDegrees < 33.75)
        {
            return "NNE";
        } else if (bearingInDegrees > 33.75 && bearingInDegrees < 56.25) {
            return "NE";
        } else if (bearingInDegrees > 56.25 && bearingInDegrees < 78.75) {
            return "ENE";
        } else if (bearingInDegrees > 78.75 && bearingInDegrees < 101.25) {
            return "E";
        } else if (bearingInDegrees > 101.25 && bearingInDegrees < 123.75) {
            return "ESE";
        } else if (bearingInDegrees > 123.75 && bearingInDegrees < 146.25) {
            return "SE";
        } else if (bearingInDegrees > 146.25 && bearingInDegrees < 168.75) {
            return "SSE";
        } else if (bearingInDegrees > 168.75 && bearingInDegrees < 191.25) {
            return "S";
        } else if (bearingInDegrees > 191.25 && bearingInDegrees < 213.75) {
            return "SSW";
        } else if (bearingInDegrees > 213.75 && bearingInDegrees < 236.25) {
            return "SW";
        } else if (bearingInDegrees > 236.25 && bearingInDegrees < 258.75) {
            return "WSW";
        } else if (bearingInDegrees > 258.75 && bearingInDegrees < 281.25) {
            return "W";
        } else if (bearingInDegrees > 281.25 && bearingInDegrees < 303.75) {
            return "WNW";
        } else if (bearingInDegrees > 303.75 && bearingInDegrees < 326.25) {
            return "NW";
        } else if (bearingInDegrees > 326.25 && bearingInDegrees < 348.75) {
            return "NNW";
        } else {
            return "N";
        }
    }
    public static void updatePrecipitationChanceList()
    {
        precipChances = new ArrayList<>();

        for(int i = 0; i < forecastedDays; i++)
        {
            precipChances.add(0);
        }

        ArrayList<Integer> indexOfMidnights = new ArrayList<>();
        for(int i = 0; i < hourlyForecastJSONArray.length(); i++)
        {
            JSONObject Index = null;
            try {
                Index = (JSONObject) hourlyForecastJSONArray.get(i);
            } catch(JSONException e) {
                e.printStackTrace();
            }
            long unixTime = 0;
            try {
                unixTime = new Long((int) Index.get("time"));
            } catch(JSONException e) {
                e.printStackTrace();
            }

            String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date(unixTime*1000));

            date = date.split(" ")[1];
            date = date.split(":")[0];
            if(date.equalsIgnoreCase("00"))
            {
                indexOfMidnights.add(i);
            }
        }

        if(indexOfMidnights.get(0) != 0)
        {
            indexOfMidnights.add(0, 0);
        }
        indexOfMidnights.add(168);

        /*for(int i = 0; i < indexOfMidnights.size(); i++)
        {
            System.out.print(indexOfMidnights.get(i) + ", ");
        }
        System.out.println();*/

        for(int onDay = 0; onDay < forecastedDays; onDay++)
        {
            int startIndex = indexOfMidnights.get(onDay);
            int endIndex = indexOfMidnights.get(onDay + 1);
            int precipForDay = 0;
            for(int i = startIndex; i < endIndex; i++)
            {
                JSONObject hourInIndex = null;
                try {
                    hourInIndex = (JSONObject) hourlyForecastJSONArray.get(i);
                } catch(JSONException e) {
                    e.printStackTrace();
                }

                double hourPrecipChance = 0;
                try {
                    hourPrecipChance = hourInIndex.getDouble("precipProbability") * 100;
                } catch(JSONException e) {
                    e.printStackTrace();
                } catch(NullPointerException e) {
                    e.printStackTrace();
                }
                if(hourPrecipChance > precipForDay)
                {
                    precipForDay = (int) (hourPrecipChance);
                }
            }
            precipChances.set(onDay, precipForDay);

            //System.out.println(startIndex + " -> " + endIndex + " : " + precipForDay);
        }
    }
    public static String getRealIconName(String s)
    {
        StringBuilder sb = new StringBuilder(s);

        for(int i = 0; i < sb.length(); i++)
        {
            if(sb.charAt(i) == '-')
            {
                sb.deleteCharAt(i);
            }
        }

        return sb.toString();
    }
}