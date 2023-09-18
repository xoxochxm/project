package cs211.project.controllers;

import cs211.project.models.Events;
import cs211.project.models.EventsList;
import cs211.project.models.Team;
import cs211.project.models.TeamList;
import cs211.project.services.Datasource;
import cs211.project.services.EventsListFileDatasource;
import cs211.project.services.FXRouter;
import cs211.project.services.TeamListFileDatasource;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.security.PrivateKey;

public class CreateTeamController {
    @FXML
    private TextField teamNameTextField;
    @FXML
    private TextField teamMaxSeatTextField;
    @FXML
    private DatePicker registOpen;
    @FXML
    private DatePicker registClose;

    private TeamList teamList;
    private Team team;
    private Datasource<TeamList> datasource;
    private Datasource<EventsList> eventsListDatasource;
    private String eventName;
    private Events events;
    private EventsList eventsList;

    @FXML
    public void initialize(){
        datasource = new TeamListFileDatasource("data", "team-list.csv");
        eventsListDatasource = new EventsListFileDatasource("data", "events-list.csv");
        teamList = datasource.readData();
        eventsList = eventsListDatasource.readData();
        eventName = (String) FXRouter.getData();
        events = eventsList.findEventByName(eventName);
        // Assuming you want to retrieve the first event from the list
        if (!teamList.isEmpty()) {
            team = teamList.getEvents().get(0); // Retrieve the first event
        }
    }

    @FXML
    protected void onCreateTeamBtnClick(){
        String teamNameText = teamNameTextField.getText();
        Integer teamMaxSeatText = Integer.valueOf(teamMaxSeatTextField.getText());
        Integer teamAvailableSeatText = Integer.valueOf(team.getAvailableSeat());
        String registOpenText = String.valueOf(registOpen.getValue());
        String registCloseText = String.valueOf(registClose.getValue());
        String teamInEventText = eventName;

        if (team != null){

            teamList.addNewTeam(teamNameText, teamMaxSeatText, teamAvailableSeatText, registOpenText, registCloseText, teamInEventText);

            try {
                datasource.writeData(teamList);
                FXRouter.goTo("edit-event");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.err.println("Team can't create");
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
}
