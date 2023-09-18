package cs211.project.controllers;

import cs211.project.models.Events;
import cs211.project.models.EventsList;
import cs211.project.services.Datasource;
import cs211.project.services.EventsListFileDatasource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import cs211.project.services.FXRouter;
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

public class CreateEventController {
    @FXML
    private TextField eventNameTextField;
    @FXML
    private TextField detailsTextField;
    @FXML
    private TextField maxSeatTextField;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker finishDate;
    @FXML
    private ImageView uploadEventImageView;

    private EventsList eventsList;
    private Events events;
    private Datasource<EventsList> datasource;
    private String currentUser;
    private String eventImagePathText;

    @FXML
    public void initialize() {
        datasource = new EventsListFileDatasource("data", "events-list.csv");
        eventsList = datasource.readData();
        currentUser = (String) FXRouter.getData();

        // Assuming you want to retrieve the first event from the list
        if (!eventsList.isEmpty()) {
            events = new Events();
            events.copyFrom(eventsList.getEvents().get(0));
        } else {
            events = new Events();
            events.setEventImagePath("images/event/samplePic.png"); // Set default image path if no events exist
        }
    }




    @FXML
    protected void uploadEventImageBtnClick(ActionEvent event){ //ปุ่มของ upload file
        FileChooser chooser = new FileChooser();
        // Set FileChooser initial directory
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        // Define acceptable file extension
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images PNG JPG", "*.png", "*.jpg", "*.jpeg"));
        // Get file from FileChooser with JavaFX component window
        Node source = (Node) event.getSource();
        File file = chooser.showOpenDialog(source.getScene().getWindow());

        if (file != null){
            try {
                // CREATE FOLDER IF NOT EXIST
                File destDir = new File("images/event");
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
                uploadEventImageView.setImage(new Image(target.toUri().toString()));
                events.setEventImagePath(destDir + "/" + filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    protected void createButtonClick() { //ปุ่มของ create
        String eventNameText = eventNameTextField.getText();
        String eventDetailText = detailsTextField.getText();
        Integer maxSeatTextFieldText = Integer.valueOf(maxSeatTextField.getText());
        String startDateText = String.valueOf(startDate.getValue());
        String finishDateText = String.valueOf(finishDate.getValue());
        String eventCreatorUsernameText = currentUser;
        String eventImagePathText = events.getEventImagePath();

        // Make sure events is not null before accessing its properties
        if (events != null) {
//            EventsList eventsList = new EventsList();
            eventsList.addNewEvent(eventNameText, eventDetailText, maxSeatTextFieldText, maxSeatTextFieldText, startDateText, finishDateText, eventImagePathText, eventCreatorUsernameText);

            try {
                datasource.writeData(eventsList);
                FXRouter.goTo("user-profile", currentUser);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // Handle the case where events is null, e.g., show an error message or log it.
            System.err.println("events is null, cannot create event.");
        }
    }

    @FXML
    private void onBackBtnClick(){
        try {
            FXRouter.goTo("user-profile",currentUser );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}