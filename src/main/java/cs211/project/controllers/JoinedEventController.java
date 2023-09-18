package cs211.project.controllers;

import cs211.project.models.Events;
import cs211.project.models.EventsList;
import cs211.project.models.TimeSchedule;
import cs211.project.models.TimeScheduleList;
import cs211.project.services.Datasource;
import cs211.project.services.EventsListFileDatasource;
import cs211.project.services.FXRouter;
import cs211.project.services.TimeScheduleListFileDatasource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;

public class JoinedEventController {
    @FXML
    private TableView activeEventTableView;
    @FXML
    private TableView finishedEventTableView;
    private ObservableList<Events> activeEventObservableList = FXCollections.observableArrayList();
    private ObservableList<Events> finishedEventObservableList = FXCollections.observableArrayList();
    // สร้าง ObservableList แยก activeEventTableView และ finishedEventTableView จะไม่เห็นข้อมูลซ้ำในตาราง
    private Datasource<EventsList> datasource;
    private EventsList eventsList;
    private String currentUser;

    public void initialize() {
        datasource = new EventsListFileDatasource("data", "events-list.csv");
        eventsList = datasource.readData();
        showActiveEventTable(eventsList);
        showFinishedEventTable(eventsList);

        currentUser = (String) FXRouter.getData();
    }
    @FXML
    public void showActiveEventTable(EventsList eventsList){
        TableColumn<Events, String> eventNameColumn = new TableColumn<>("Event Name");
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));

        activeEventTableView.getColumns().clear();
        activeEventTableView.getColumns().add(eventNameColumn);
        activeEventTableView.getItems().clear();



        for (Events event : eventsList.getEvents()) {
            LocalDate eventDate = LocalDate.parse(event.getFinishDate()); // แปลงวันที่เหตุการณ์เป็น LocalDate

            if (eventDate.isAfter(LocalDate.now())) {
                // ถ้าเวลายังไม่เลย (ก่อนวันปัจจุบัน) ให้เพิ่มเหตุการณ์ลงใน activeEventTableView
                activeEventTableView.getItems().add(event);
                activeEventObservableList.add(event);
            }

        }
        // กำหนด ObservableList ให้กับ activeEventTableView
        activeEventTableView.setItems(activeEventObservableList);

    }
    @FXML
    public void showFinishedEventTable(EventsList eventsList){
        TableColumn<Events, String> eventNameColumn = new TableColumn<>("Event Name");
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));

        finishedEventTableView.getColumns().clear();
        finishedEventTableView.getColumns().add(eventNameColumn);
        finishedEventTableView.getItems().clear();

        for (Events event : eventsList.getEvents()) {
            LocalDate eventDate = LocalDate.parse(event.getFinishDate()); // แปลงวันที่เหตุการณ์เป็น LocalDate

            if (eventDate.isBefore(LocalDate.now())) {
                // ถ้าเวลาเลย ให้เพิ่มเหตุการณ์ลงใน finishedEventTableView
                finishedEventTableView.getItems().add(event);
                finishedEventObservableList.add(event);
            }
        }
        // กำหนด ObservableList ให้กับ finishedEventTableView
        finishedEventTableView.setItems(finishedEventObservableList);
    }
    public void onBackBtnClick() {
        try {
            FXRouter.goTo("user-profile", currentUser);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
