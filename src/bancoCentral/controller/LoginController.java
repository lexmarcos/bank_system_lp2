package bancoCentral.controller;

import bancoCentral.model.Costumer;
import bancoCentral.model.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class LoginController {
    Database db = new Database();
    Costumer costumerLogged;
    @FXML
    public javafx.scene.control.TextField bankLoginID;
    @FXML
    public javafx.scene.control.TextField emailInput;
    @FXML
    public javafx.scene.control.TextField passwordInput;
    @FXML
    public Button btnLogin;


    public LoginController(){}


    public Costumer loginDB(String bankLoginId, String email, String password){
        return db.loginCostumer(bankLoginId, email, password);
    }

    public Costumer logout(){
        return db.logout();
    }

    @FXML
    public void login(ActionEvent actionEvent) throws IOException {
        System.out.println(String.format("%s\n%s\n%s\n", bankLoginID.getText(), emailInput.getText(), passwordInput.getText()));
//        costumerLogged = loginDB(bankLoginID.getText(), emailInput.getText(), passwordInput.getText());
        costumerLogged = loginDB("1", "henrymedeiros@hotmail.com",  "hhsyehbd637");
        goToMainMenu(actionEvent);
    }

    public void goToMainMenu(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/bancoCentral/view/MainMenuView.fxml"));
        Stage window = (Stage) btnLogin.getScene().getWindow();
        window.setScene(new Scene(root));
    }
}
