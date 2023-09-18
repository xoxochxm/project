package cs211.project.controllers;

import cs211.project.models.Events;
import cs211.project.models.EventsList;
import cs211.project.models.User;
import cs211.project.models.UserList;
import cs211.project.services.Datasource;
import cs211.project.services.EventsListFileDatasource;
import cs211.project.services.FXRouter;
import cs211.project.services.UserListFileDatasource;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class UserProfileController {
    @FXML private Label nameLabel;
    @FXML private Label usernameLabel;
    @FXML private ImageView profilePicImageView;
    @FXML private TableView myEventTableview;
    private Datasource<UserList> datasource;
    private Datasource<EventsList> eventsListDatasource;
    private UserList userList;
    private User user;
    private EventsList eventsList;
    private Event event;
    private String currentUser;

    @FXML
    public void initialize() {
        datasource = new UserListFileDatasource("data", "user-list.csv");
        eventsListDatasource = new EventsListFileDatasource("data", "events-list.csv");
        userList = datasource.readData();
        eventsList = eventsListDatasource.readData();

        currentUser = (String) FXRouter.getData();
        user = userList.findUserByUsername(currentUser);
        nameLabel.setText(user.getName());
        usernameLabel.setText(user.getUsername());
        Image image = new Image("file:"+user.getUserImagePath(), true);
        profilePicImageView.setImage(image);
        showTable(eventsList);

        myEventTableview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Events>() {
            @Override
            public void changed(ObservableValue observable, Events oldValue, Events newValue) {
                if (newValue != null) {
                    try {
                        // FXRouter.goTo สามารถส่งข้อมูลไปยังหน้าที่ต้องการได้ โดยกำหนดเป็น parameter ที่สอง
                        FXRouter.goTo("edit-event", newValue.getEventName());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

    }

    @FXML
    public void showTable(EventsList eventsList){
        TableColumn<Events, String> eventNameColumn = new TableColumn<>("Event Name");
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));

        TableColumn<Events, String> eventDetailColumn = new TableColumn<>("Event Detail");
        eventDetailColumn.setCellValueFactory(new PropertyValueFactory<>("eventDetail"));

        TableColumn<Events, String> eventDateColumn = new TableColumn<>("Event Day");
        eventDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        TableColumn<Events, String> seatAvailableColumn = new TableColumn<>("Available Seat");
        seatAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeat"));

        myEventTableview.getColumns().clear();
        myEventTableview.getColumns().add(eventNameColumn);
        myEventTableview.getColumns().add(eventDetailColumn);
        myEventTableview.getColumns().add(eventDateColumn);
        myEventTableview.getColumns().add(seatAvailableColumn);

        myEventTableview.getItems().clear();

        for(Events events: eventsList.getEvents()){
            if (events.getEventCreatorUsername().equals(currentUser)){
                myEventTableview.getItems().add(events);
            }

        }

    }
    @FXML
    void eventsBtnClick() {
        try {
            FXRouter.goTo("events");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onEditProfileBtnClick(){
        try {
            FXRouter.goTo("edit-profile", currentUser);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onCreateEventBtnClick(){
        try {
            FXRouter.goTo("create-event", user.getUsername());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void joinedEventBtnClick() {
        try {
            FXRouter.goTo("joined-event", user.getUsername());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
