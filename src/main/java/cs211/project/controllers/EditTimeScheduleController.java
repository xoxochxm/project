package cs211.project.controllers;

import cs211.project.models.Events;
import cs211.project.models.TimeSchedule;
import cs211.project.models.TimeScheduleList;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import cs211.project.services.TimeScheduleListFileDatasource;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;

public class EditTimeScheduleController {
    @FXML  private TableView timeScheduleTable;
    private TimeScheduleList timeScheduleList;
    private Datasource<TimeScheduleList> datasource;
    private String eventName;

    @FXML
    public void initialize(){
        eventName = (String) FXRouter.getData();
        datasource = new TimeScheduleListFileDatasource("data", "schedule-list.csv");
        timeScheduleList = datasource.readData();
        showTable(timeScheduleList);
    }

    @FXML
    public void showTable(TimeScheduleList timeScheduleList){
        TableColumn<TimeSchedule, String> activityNameColumn = new TableColumn<>("Activity Name");
        activityNameColumn.setCellValueFactory(new PropertyValueFactory<>("activityName"));

        TableColumn<TimeSchedule, String> activityDetailColumn = new TableColumn<>("Activity Detail");
        activityDetailColumn.setCellValueFactory(new PropertyValueFactory<>("activityDetail"));

        TableColumn<TimeSchedule, String> activityStartColumn = new TableColumn<>("Activity Start");
        activityStartColumn.setCellValueFactory(new PropertyValueFactory<>("activityStart"));

        TableColumn<TimeSchedule, String> activityFinishColumn = new TableColumn<>("Activity Finish");
        activityFinishColumn.setCellValueFactory(new PropertyValueFactory<>("activityFinish"));

        TableColumn<TimeSchedule, String> activityStatusColumn = new TableColumn<>("Activity Status");
        activityStatusColumn.setCellValueFactory(new PropertyValueFactory<>("activityStatus"));

        timeScheduleTable.getColumns().clear();
        timeScheduleTable.getColumns().add(activityNameColumn);
        timeScheduleTable.getColumns().add(activityDetailColumn);
        timeScheduleTable.getColumns().add(activityStartColumn);
        timeScheduleTable.getColumns().add(activityFinishColumn);
        timeScheduleTable.getColumns().add(activityStatusColumn);

        timeScheduleTable.getItems().clear();

        for(TimeSchedule timeSchedule: timeScheduleList.getTimeSchedules()){
            LocalDate finDate = LocalDate.parse(timeSchedule.getActivityFinish());
            if (timeSchedule.getScheduleInEvent().equals(eventName)){
                if (timeSchedule.getScheduleInEvent().equals(eventName)){
                    if(finDate.isBefore(LocalDate.now())) {
                        timeSchedule.setActivityStatus("finished");
                    }
                    timeScheduleTable.getItems().add(timeSchedule);
                }
            }
        }
    }
    @FXML
    private void onBackBtnClick(){
        try {
            FXRouter.goTo("edit-event");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void onCreateScheduleBtnClick(){
        try {
            FXRouter.goTo("create-schedule", eventName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
