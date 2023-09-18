package cs211.project.controllers;

import cs211.project.models.Team;
import cs211.project.models.TeamList;
import cs211.project.models.TeamUser;
import cs211.project.models.TeamUserList;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import cs211.project.services.TeamListFileDatasource;
import cs211.project.services.TeamUserListFileDatasource;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class TeamController {
    @FXML private Label teamNameLabel;
    @FXML private Label teamMaxSeatLabel;
    @FXML private Label availableSeatLabel;
    @FXML private Label teamRegisOpenLabel;
    @FXML private Label teamRegisCloseLabel;
    Team team;
    TeamList teamList;
    TeamUser teamUser;
    TeamUserList teamUserList;
    Datasource<TeamList> datasource;
    Datasource<TeamUserList> teamUserListDatasource;
    DataContainer dataContainer;
    String eventName;
    String teamName;
    String username;

    @FXML
    public void initialize(){
        datasource = new TeamListFileDatasource("data", "team-list.csv");
        teamUserListDatasource = new TeamUserListFileDatasource("data", "team-user-list.csv");
        teamUserList = teamUserListDatasource.readData();
        dataContainer = (DataContainer) FXRouter.getData();
        eventName = (String) dataContainer.get("eventName");
        teamName = (String) dataContainer.get("teamName");
        username = (String) dataContainer.get("username");
        teamList = datasource.readData();
        team = teamList.findTeamByTeamName(teamName);
        showTeam(team);
    }
    private void showTeam(Team team){
        teamNameLabel.setText(team.getTeamName());
        availableSeatLabel.setText(String.valueOf(team.getAvailableSeat()));
        teamMaxSeatLabel.setText(String.valueOf(team.getTeamMaxSeat()));
        teamRegisOpenLabel.setText(team.getRegistrationOpenDate());
        teamRegisCloseLabel.setText(team.getRegistrationCloseDate());
    }

    @FXML
    private void onBackBtnClick(){
        try {
            FXRouter.goTo("event-detail");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void onJoinTeamBtnClick(){
        if (teamUserList.findTeamUser(eventName, teamName, username) == null && !team.getAvailableSeat().equals(0)){
            teamUserList.addNewTeamUser(eventName, teamName, username);
            teamUserListDatasource.writeData(teamUserList);
            try {
                FXRouter.goTo("event-detail"); //todo goto team edit(create activity, team chat)
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

