package cs211.project.controllers;

import cs211.project.models.User;
import cs211.project.services.Datasource;
import cs211.project.services.UserListFileDatasource;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import cs211.project.services.FXRouter;

import cs211.project.models.UserList;

import java.io.IOException;
public class RegistrationPageController {
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField createPasswordTextField;
    @FXML
    private TextField comfirmPasswordTextField;
    @FXML
    private Label errorLabel;

    private UserList userList;
    private User user;
    private Datasource<UserList> datasource;


    @FXML
    public void initialize() {
        datasource = new UserListFileDatasource("data", "user-list.csv");
        userList = datasource.readData();
        errorLabel.setText("");
    }

    @FXML
    protected void onBackBtnClick() {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onRegisterBtnClick (){
        String usernameTextRegister = usernameTextField.getText();
        String nameTextRegister = nameTextField.getText();
        String passwordTextRegister = createPasswordTextField.getText();
        String confirmPasswordTextRegister = comfirmPasswordTextField.getText();
        String loginTimeTextRegister = "00-00-0000 00:00:00";
        String rank = "user";
        String userImagePath = "images/user/default.png";
        boolean passwordStatus;
        boolean usernameStatus;

        if(usernameTextRegister != null && passwordTextRegister != null && confirmPasswordTextRegister != null){ // Check if Text Field is not blank

            if (!passwordTextRegister.equals(confirmPasswordTextRegister)){ // Check if new confirmed password is the same as new password
                createPasswordTextField.setText("");
                comfirmPasswordTextField.setText("");
                passwordStatus = false;
            }   else{
                // todo write data user password
                passwordStatus = true;
            }

            user = userList.findUserByUsername(usernameTextRegister); // Check in datasource if new username is not the same as previous
            if (user != null) {
                usernameTextField.setText("");
                usernameStatus = false;
            }   else{
                // todo write data user username
                usernameStatus = true;
            }

            if (!passwordStatus && !usernameStatus) {
               errorLabel.setText("Username Is Taken And Password Are Not Match");
            } else if (!passwordStatus) {
                errorLabel.setText("Password Are Not Match");
            } else if (!usernameStatus) {
                errorLabel.setText("This Username Is Taken");
            } else {
                userList.addNewUser(usernameTextRegister, nameTextRegister, passwordTextRegister, rank, userImagePath, loginTimeTextRegister);
                datasource.writeData(userList);
                try {
                    FXRouter.goTo("login");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
