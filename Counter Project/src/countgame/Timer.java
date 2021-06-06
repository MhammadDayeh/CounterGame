package countgame;

public class Timer {
    private long startTime;

    public void startTimer() {
        this.startTime = System.currentTimeMillis();
	}
    
	public int[] getTime() {
        long milliTime = System.currentTimeMillis() - this.startTime;
        
        int[] time = new int[]{0, 0, 0};
        time[0] = (int) (milliTime / 3600000);
        time[1] = (int) (milliTime / 60000) % 60;
        time[2] = (int) (milliTime / 1000) % 60;

        return time;
    }

	public void stopTimer() {
		this.startTime = 0;
	}
}
