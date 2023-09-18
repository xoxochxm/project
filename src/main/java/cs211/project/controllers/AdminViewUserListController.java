package cs211.project.controllers;

import cs211.project.models.User;
import cs211.project.models.UserList;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import cs211.project.services.UserListFileDatasource;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class AdminViewUserListController {
    @FXML private TableView usersTableView;
    private UserList userList;
    private Datasource<UserList> datasource;
    String admin;

    @FXML
    public void initialize() {
        datasource = new UserListFileDatasource("data", "user-list.csv");
        userList = datasource.readData();
        admin = (String) FXRouter.getData();
        showTable(userList);
    }

    @FXML
    void showTable(UserList userList) { // todo: Must have user profile picture in table
        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<User, String> loginTimeColumn = new TableColumn<>("Latest Use");
        loginTimeColumn.setCellValueFactory(new PropertyValueFactory<>("loginTime"));

        // ล้าง column เดิมทั้งหมดที่มีอยู่ใน table แล้วเพิ่ม column ใหม่
        usersTableView.getColumns().clear();
        usersTableView.getColumns().add(usernameColumn); //add ตามลำดับ
        usersTableView.getColumns().add(nameColumn);
        usersTableView.getColumns().add(loginTimeColumn);

        usersTableView.getItems().clear();

        // ใส่ข้อมูลทั้งหมดจาก userList ไปแสดงใน TableView
        for (User user: userList.getUsers()) {
            if (user.getRank().equals("user")) {
                usersTableView.getItems().add(user);
            }
        }
    }
    @FXML
    private void onEditPasswordButtonClick(){
        //todo : admin info and change pass
        try {
            FXRouter.goTo("admin-change-password", admin);
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
