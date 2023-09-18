package cs211.project.models;

public class TimeSchedule {
    private String activityName;
    private String activityDetail;
    private String activityStart;
    private String activityFinish;
    private String scheduleInEvent;
    private String activityStatus; // active or finished

    public TimeSchedule(String activityName, String activityDetail, String activityStart, String activityFinish, String scheduleInEvent, String activityStatus) {
        this.activityName = activityName;
        this.activityDetail = activityDetail;
        this.activityStart = activityStart;
        this.activityFinish = activityFinish;
        this.scheduleInEvent = scheduleInEvent;
        this.activityStatus = activityStatus;
    }

    public String getActivityName() { return activityName; }
    public String getActivityDetail() { return activityDetail; }
    public String getActivityStart() { return activityStart; }
    public String getActivityFinish() { return activityFinish; }
    public String getScheduleInEvent() { return scheduleInEvent; }
    public String getActivityStatus() {
        return activityStatus;
    }
    public void setActivityStatus(String activityStatus) {
        this.activityStatus = activityStatus;
    }

    public boolean isActivityName(String activityName){
        return this.activityName.equals(activityName);
    }

}
