package cs211.project.controllers;

import cs211.project.models.Events;
import cs211.project.models.EventsList;
import cs211.project.models.Team;
import cs211.project.models.TeamList;
import cs211.project.services.Datasource;
import cs211.project.services.EventsListFileDatasource;
import cs211.project.services.FXRouter;
import cs211.project.services.TeamListFileDatasource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

public class EditEventController {
    @FXML
    private Label eventNameLabel;
    @FXML
    private Label eventDetailLabel;
    @FXML
    private Label maxSeatLabel;
    @FXML
    private Label eventStartDateLabel;
    @FXML
    private Label eventFinishDateLabel;
    @FXML
    private TextField editEventNameTextField;
    @FXML
    private TextField editDetailTextField;
    @FXML
    private TextField editMaxSeatTextField;
    @FXML
    private DatePicker editDateStart;
    @FXML
    private DatePicker editDateFinish;
    @FXML
    private TableView teamTableView;
    @FXML
    private ImageView eventImageView;

    private Datasource<EventsList> datasource;
    private Datasource<TeamList> teamListDatasource;
    private EventsList eventsList;
    private TeamList teamList;
    private Events events;
    private String eventName;
    Image image;

    @FXML
    public void initialize(){
        datasource = new EventsListFileDatasource("data", "events-list.csv");
        teamListDatasource = new TeamListFileDatasource("data", "team-list.csv");
        eventsList = datasource.readData();
        teamList = teamListDatasource.readData();
        eventName = (String) FXRouter.getData();
        events = eventsList.findEventByName(eventName);
        image = new Image("file:"+events.getEventImagePath(), true);
        showEvents(events);
        showTable(teamList);
    }

    private void showEvents(Events events){
        eventNameLabel.setText(events.getEventName());
        eventDetailLabel.setText(events.getEventDetail());
        maxSeatLabel.setText(String.valueOf(events.getMaxSeat()));
        eventStartDateLabel.setText(events.getStartDate());
        eventFinishDateLabel.setText(events.getFinishDate());
        eventImageView.setImage(image);
    }

    private void showTable(TeamList teamList){
        TableColumn<Events, String> teamNameColumn = new TableColumn<>("Team Name");
        teamNameColumn.setCellValueFactory(new PropertyValueFactory<>("teamName"));

        TableColumn<Events, String> teamMaxSeatColumn = new TableColumn<>("Team Max seat");
        teamMaxSeatColumn.setCellValueFactory(new PropertyValueFactory<>("teamMaxSeat"));

        teamTableView.getColumns().clear();
        teamTableView.getColumns().add(teamNameColumn);
        teamTableView.getColumns().add(teamMaxSeatColumn);

        teamTableView.getItems().clear();

        for (Team team: teamList.getTeams()){
            if (team.getTeamInEvent().equals(eventName)){
                teamTableView.getItems().add(team);
            }
        }
    }


    @FXML
    private void onEditEventPictureBtnClick(ActionEvent event){
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images PNG JPG", "*.png", "*.jpg", "*.jpeg"));
        Node source = (Node) event.getSource();
        File file = chooser.showOpenDialog(source.getScene().getWindow());
        if (file != null){
            try {
                // CREATE FOLDER IF NOT EXIST
                File destDir = new File("images/events");
                if (!destDir.exists()) destDir.mkdirs();
                // RENAME FILE
                String[] fileSplit = file.getName().split("\\.");
                String filename = LocalDate.now() + "_"+System.currentTimeMillis() + "."
                        + fileSplit[fileSplit.length - 1];
                Path target = FileSystems.getDefault().getPath(
                        destDir.getAbsolutePath()+System.getProperty("file.separator")+filename
                );
                // COPY WITH FLAG REPLACE FILE IF FILE IS EXIST
                Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING );
                // SET NEW FILE PATH TO IMAGE
                eventImageView.setImage(new Image(target.toUri().toString()));
                events.setEventImagePath(destDir + "/" + filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onCreateNewTeamBtnClick(){
        try {
            FXRouter.goTo("create-team", eventName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void onSaveBtnClick(){
        String newEventNameText = editEventNameTextField.getText();
        String newEventDetailText = editDetailTextField.getText();
        String newMaxSeatText = editMaxSeatTextField.getText();
        String newstartDateText = String.valueOf(editDateStart.getValue());
        String newFinishDateText = String.valueOf(editDateFinish.getValue());

        events.setEventName(newEventNameText);
        events.setEventDetail(newEventDetailText);
        events.setMaxSeat(Integer.valueOf(newMaxSeatText));
        events.setStartDate(newstartDateText);
        events.setFinishDate(newFinishDateText);


        try {
            FXRouter.goTo("user-profile");
            datasource.writeData(eventsList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onViewActivityTableBtnClick(){
        try {
            FXRouter.goTo("edit-schedule", eventName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onManageUserBtnClick(){
        try {
            FXRouter.goTo("event-manage-user", eventName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onBackBtnClick(){
        try {
            FXRouter.goTo("user-profile");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
