package com.example.cody.weathertime;

/**
 * Created by Cody on 12/29/2017.
 * The custom runnable that runs refreshes on separate thread.
 */

public class RefreshRunnable implements Runnable
{
    public static int timeInSeconds = 0;

    @Override
    public void run()
    {
        //android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        long futureTime = System.currentTimeMillis() + (timeInSeconds * 1000);
        while(System.currentTimeMillis() < futureTime)
        {
            synchronized(this)
            {
                try
                {
                    wait(futureTime - System.currentTimeMillis());
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        //get info and then check if they are good sets of data, then set to the displayed set
        boolean shouldUpdate = Functions.updateWeather();
        if(RetrievedToDisplayInfo.WeatherListIsValid() && shouldUpdate)
        {
            WeatherMain.displayedWeathers = RetrievedToDisplayInfo.dailyForecast;
        }
        if(RetrievedToDisplayInfo.WeatherCurrentIsValid() && shouldUpdate)
        {
            WeatherMain.displayedCurrent = RetrievedToDisplayInfo.current;
        }
        if(shouldUpdate)
        {
            Handlers.Refresher.sendEmptyMessage(0);
        } else {
            RetrievedToDisplayInfo.timesCaught++;
        }
    }
}
