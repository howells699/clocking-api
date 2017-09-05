package ClockingAPI.models;

public class TimeInput {

    private String userId = "";
    private String clockingStatus = "";
    private String date = "";
    private String time = "";

    public TimeInput(String time,String date) {
        this.time = time;
        this.date = date;
    }

    public void setClockingStatus(String clockingStatus) {
        this.clockingStatus = clockingStatus;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
