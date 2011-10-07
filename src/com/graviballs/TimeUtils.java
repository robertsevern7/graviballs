package com.graviballs;

public class TimeUtils {
	public static String justParsingTheTime(int seconds) {
        int minutes = seconds / 60;
        seconds     = seconds % 60;
        
        if (seconds < 10) {
            return minutes + ":0" + seconds;
        } else {
            return minutes + ":" + seconds;            
        }
	}
}
