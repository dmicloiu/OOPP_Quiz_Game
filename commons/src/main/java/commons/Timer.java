package commons;

public class Timer {

    private int seconds;
    private int milliSeconds;

    public Timer(int seconds, int milliSeconds) {
        this.seconds = seconds;
        this.milliSeconds = milliSeconds;
    }

    /**
     * To get the current time of the timer.
     * @return - returns the current time in String and parseable double format.
     */
    public String getCurrentTime() {
        return "" + seconds + "." + milliSeconds;
    }

    /**
     * Decreases the timer by one millisecond.
     */
    public void timerDec() {
        if (milliSeconds == 0) {
            seconds--;
            milliSeconds = milliSeconds + 9;
        } else {
            milliSeconds--;
        }
    }

    /**
     * Can be used to set a time for the timer.
     * @param seconds - the time to be set in the timer.
     */
    public void setTime(int seconds, int milliSeconds) {
        this.seconds = seconds;
        this.milliSeconds = milliSeconds;
    }

    /**
     * Gets the time
     * @return the current time
     */
    public int getTime() {
        return seconds;
    }

    /**
     * Gets the amound of milliseconds
     * @return the current value of the milliSeconds
     */
    public int getMilliSeconds() {
        return milliSeconds;
    }

    /**
     * Decreases the time to a half
     */
    public void halveTime() {
        this.seconds /= 2;
        this.milliSeconds /= 2;
    }
}
