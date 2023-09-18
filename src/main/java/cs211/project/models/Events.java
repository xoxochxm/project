package cs211.project.models;


import javafx.scene.image.ImageView;

public class Events {
    private String eventName;
    private String eventDetail;
    private Integer maxSeat;
    private Integer availableSeat;
    private String startDate;
    private String finishDate;
    private String eventImagePath = "/events/samplePic.png";
    private String eventCreatorUsername;
    private ImageView eventImage; // เก็บรูปภาพในรูปแบบของ JavaFX Image

    public Events(String eventName, String eventDetail, int maxSeat, int availableSeat, String startDate, String finishDate, String eventImagePath, String eventCreatorUsername) {
        this.eventName = eventName;
        this.eventDetail = eventDetail;
        this.maxSeat = maxSeat;
        this.availableSeat = availableSeat;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.eventImagePath = eventImagePath;
        this.eventCreatorUsername = eventCreatorUsername;
    }

    public Events() {
    }

    public ImageView getEventImage() {
        return eventImage;
    }
    public void setEventImage(ImageView eventImage) {
        this.eventImage = eventImage;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDetail() {
        return eventDetail;
    }

    public int getMaxSeat() {
        return maxSeat;
    }

    public Integer getAvailableSeat() {
        return availableSeat;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public String getEventImagePath() { return eventImagePath; }
    public String getEventCreatorUsername(){ return  eventCreatorUsername; }

    public void setEventImagePath(String eventImagePath) {
        this.eventImagePath = eventImagePath;
    }
    public void setEventName(String eventName) { this.eventName = eventName; }
    public void setEventDetail(String eventDetail) { this.eventDetail = eventDetail; }
    public void setMaxSeat(Integer maxSeat) { this.maxSeat = maxSeat; }
    public void setAvailableSeat(Integer availableSeat) {
        this.availableSeat = availableSeat;
    }

    public void setStartDate(String startDate) { this.startDate = startDate; }
    public void setFinishDate(String finishDate) { this.finishDate = finishDate; }

    public void userJoinEvent() {
        availableSeat--;
        this.setAvailableSeat(availableSeat);
    }
    public void userBan() {
        availableSeat++;
        this.setAvailableSeat(availableSeat);
    }

    public boolean isEventName(String eventName) {
        return this.eventName.equals(eventName);
    }
    public boolean isCreatorUsername(String eventCreatorUsername){
        return this.eventCreatorUsername.equals(eventCreatorUsername);
    }

    public void copyFrom(Events events) {
    }
}