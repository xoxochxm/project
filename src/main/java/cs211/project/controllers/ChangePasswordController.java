package cs211.project.controllers;

import cs211.project.models.User;
import cs211.project.models.UserList;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import cs211.project.services.UserListFileDatasource;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


import java.io.IOException;

public class ChangePasswordController {
    @FXML private TextField currentPasswordTextField;
    @FXML private TextField newPasswordTextField;
    @FXML private TextField confirmNewPasswordTextField;
    @FXML private Label errorLabel;
    private Datasource<UserList> datasource;
    private UserList userList;
    private User user;
    @FXML
    public void initialize() {
        datasource = new UserListFileDatasource("data", "user-list.csv");
        userList = datasource.readData();
        errorLabel.setText("");

        String currPass = (String) FXRouter.getData();
        user = userList.findUserByUsername(currPass);




    }
    @FXML
    private void onSaveChangePasswordBtnClick(){
        String currPassText = currentPasswordTextField.getText();
        String newPassText = newPasswordTextField.getText();
        String confirmPassText = confirmNewPasswordTextField.getText();

        if (!currPassText.equals("") && !newPassText.equals("") && !confirmPassText.equals("")) {
            if (newPassText.equals(currPassText)) {
                errorLabel.setText("password cannot be same");
            }
            if (!currPassText.equals(user.getPassword())) {
                errorLabel.setText("wrong current password");
            }
            if (!confirmPassText.equals(newPassText)) {
                errorLabel.setText("password not match");
            }
            if (!newPassText.equals(currPassText) && currPassText.equals(user.getPassword()) && confirmPassText.equals(newPassText)){
                //write new data to csv
                user.setPassword(newPassText);

            }
        }

        try {
            datasource.writeData(userList);
            FXRouter.goTo("user-profile", user.getPassword());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
