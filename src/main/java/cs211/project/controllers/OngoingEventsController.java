package cs211.project.controllers;

import cs211.project.models.*;
import cs211.project.services.Datasource;
import cs211.project.services.EventsListFileDatasource;
import cs211.project.services.FXRouter;
import cs211.project.services.UserListFileDatasource;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class OngoingEventsController {
    @FXML private TextField searchEventsTextField;
    @FXML private TableView eventsTableView;
    private ObservableList<Events> eventsObservableList = FXCollections.observableArrayList();
    private Datasource<EventsList> datasource;
    private EventsList eventsList;
    private String currentUser;


    @FXML
    public void initialize(){
        datasource = new EventsListFileDatasource("data", "events-list.csv");
        eventsList = datasource.readData();
        showTable(eventsList);

        currentUser = (String) FXRouter.getData();

        FilteredList<Events> filteredEvents = new FilteredList<>(eventsObservableList, b -> true);
                searchEventsTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredEvents.setPredicate(events -> {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        String lowerCase = newValue.toLowerCase();

                        if (events.getEventName().toLowerCase().indexOf(lowerCase) != -1) {
                            return true;
                        } else {
                            return false;
                        }
                    });
                });

        SortedList<Events> sortedEvents = new SortedList<>(filteredEvents);
        sortedEvents.comparatorProperty().bind(eventsTableView.comparatorProperty());
        eventsTableView.setItems(sortedEvents);

        eventsTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Events>() {
            @Override
            public void changed(ObservableValue observable, Events oldValue, Events newValue) {
                if (newValue != null) {
                    try {
                        // FXRouter.goTo สามารถส่งข้อมูลไปยังหน้าที่ต้องการได้ โดยกำหนดเป็น parameter ที่สอง
                        EventUser eventUser = new EventUser(newValue.getEventName(), currentUser);
                        FXRouter.goTo("event-detail", eventUser);
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

        TableColumn<Events, String> seatAvailableColumn = new TableColumn<>("Seat Available");
        seatAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeat"));

        TableColumn<Events, Image> eventImageColumn = new TableColumn<>("Event Image"); // สร้างคอลัมน์สำหรับรูปภาพ
        eventImageColumn.setPrefWidth(80.0);
        eventImageColumn.setCellValueFactory(new PropertyValueFactory<>("eventImage")); // กำหนด PropertyValueFactory สำหรับรูปภาพ


        eventImageColumn.setCellFactory(column -> {
            return new TableCell<Events, Image>() {
                private final ImageView imageView = new ImageView();

                @Override
                protected void updateItem(Image item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setGraphic(null);
                    } else {
                        imageView.setImage(item);
                        imageView.setFitWidth(50); // กำหนดความกว้างของรูปภาพในเซลล์
                        imageView.setPreserveRatio(true);
                        setGraphic(imageView);
                    }
                }
            };
        });


        eventsTableView.getColumns().clear();
        eventsTableView.getColumns().add(eventNameColumn);
        eventsTableView.getColumns().add(eventDetailColumn);
        eventsTableView.getColumns().add(eventDateColumn);
        eventsTableView.getColumns().add(seatAvailableColumn);
        eventsTableView.getColumns().add(eventImageColumn);
        eventsTableView.getItems().clear();

        for(Events events: eventsList.getEvents()){
            eventsTableView.getItems().add(events);
            eventsObservableList.add(events);
        }


    }

    @FXML
    private void onProfileButtonClick(){
        try {
            FXRouter.goTo("user-profile", currentUser);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onLogOutBtnClick(){
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
