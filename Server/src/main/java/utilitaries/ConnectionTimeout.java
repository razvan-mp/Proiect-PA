package utilitaries;

import static java.lang.Thread.sleep;

public class ConnectionTimeout implements Runnable {
    private final int defaultTimer;
    private int seconds;
    private boolean connectionTimedOut = false;

    public ConnectionTimeout(int seconds) {
        this.seconds = seconds;
        this.defaultTimer = seconds;
    }

    public void resetTimeout() {
        this.seconds = this.defaultTimer;
    }

    public boolean isConnectionTimedOut() {
        return connectionTimedOut;
    }

    @Override
    public void run() {
        while (seconds != 0) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            seconds--;
        }
        connectionTimedOut = true;
    }
}
