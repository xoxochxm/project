package cs211.project.controllers;

import cs211.project.models.EventUser;
import cs211.project.models.EventUserList;
import cs211.project.models.Events;
import cs211.project.models.EventsList;
import cs211.project.services.Datasource;
import cs211.project.services.EventUserFileDatasource;
import cs211.project.services.EventsListFileDatasource;
import cs211.project.services.FXRouter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import cs211.project.models.Team;
import cs211.project.models.TeamList;
import cs211.project.services.TeamListFileDatasource;

import java.io.IOException;

public class EventController {
    @FXML
    private Label eventNameLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label seatLabel;
    @FXML
    private Label detailLabel;
    @FXML
    private ImageView eventImageView;
    @FXML
    private TableView teamTableView;

    private Datasource<EventsList> datasource;
    private EventsList eventsList;
    private Events events;
    private Datasource<EventUserList> eventUserListDatasource;
    private EventUserList eventUserList;
    private EventUser eventUser;
    private String currentUser;

    private Datasource<TeamList> datasource1;
    private TeamList teamList;
    Image image;

    @FXML
    public void initialize() {
        datasource = new EventsListFileDatasource("data", "events-list.csv");
        eventsList = datasource.readData();

        eventUserListDatasource = new EventUserFileDatasource("data", "event-user-list.csv");
        eventUserList = eventUserListDatasource.readData();

        eventUser = (EventUser) FXRouter.getData();
        currentUser = eventUser.getUser();
        events = eventsList.findEventByName(eventUser.getEvent());
        image = new Image("file:" + events.getEventImagePath());
        System.out.println(events.getEventImagePath());
        System.out.println(image.getClass());
        showEvents(events);

        datasource1 = new TeamListFileDatasource("data", "team-list.csv");
        teamList = datasource1.readData();
        showTable(teamList);
        teamTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Team>() { //ยังกดตารางไปอีกหน้าไม่ได้
            public void changed (ObservableValue observable, Team oldValue, Team newValue){
                if (newValue != null) {
                    try {
                        DataContainer dataContainer = new DataContainer();
                        dataContainer.put("eventName", eventUser.getEvent());
                        dataContainer.put("teamName", newValue.getTeamName());
                        dataContainer.put("username", eventUser.getUser());
                        // FXRouter.goTo สามารถส่งข้อมูลไปยังหน้าที่ต้องการได้ โดยกำหนดเป็น parameter ที่สอง
                        FXRouter.goTo("team-detail", dataContainer);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }



    private void showEvents (Events events){
        eventNameLabel.setText(events.getEventName());
        dateLabel.setText(events.getStartDate());
        seatLabel.setText(events.getMaxSeat() - events.getAvailableSeat() + "/" + events.getMaxSeat());
        detailLabel.setText(events.getEventDetail());
        eventImageView.setImage(image);
    }
    @FXML
    protected void joinBtnClick() {
        if(eventUserList.findEventUser(eventUser.getEvent(), currentUser)==null && !events.getAvailableSeat().equals(0)) {
            eventUserList.addEventUser(eventUser.getEvent(), currentUser);
            eventUserListDatasource.writeData(eventUserList);
            events.userJoinEvent();
            datasource.writeData(eventsList);
            try {
                FXRouter.goTo("event-schedule", eventUser);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            //todo
        }
    }
    @FXML
    public void showTable(TeamList teamList){
        TableColumn<Team, String> teamNameColumn = new TableColumn<>("Team Name");
        teamNameColumn.setCellValueFactory(new PropertyValueFactory<>("teamName"));

        TableColumn<Team, String> teamDateColumn = new TableColumn<>("Team Day");
        teamDateColumn.setCellValueFactory(new PropertyValueFactory<>("registrationOpenDate"));

        TableColumn<Team, String> teamEndColumn = new TableColumn<>("End Day");
        teamDateColumn.setCellValueFactory(new PropertyValueFactory<>("registrationCloseDate"));


        TableColumn<Team, String> seatAvailableColumn = new TableColumn<>("Seat Available");
        seatAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("teamMaxSeat"));

        teamTableView.getColumns().clear();
        teamTableView.getColumns().add(teamNameColumn);
        teamTableView.getColumns().add(teamDateColumn);
        teamTableView.getColumns().add(teamEndColumn);
        teamTableView.getColumns().add(seatAvailableColumn);

        teamTableView.getItems().clear();
        for (Team team: teamList.getTeams()){
            if (team.getTeamInEvent().equals(events.getEventName())){
                teamTableView.getItems().add(team);
            }
        }
    }
        @FXML
        protected void backBtnClick () {
            try {
                FXRouter.goTo("events");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

