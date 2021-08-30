package bancoCentral.controller;

import bancoCentral.model.Account;
import bancoCentral.model.Costumer;
import bancoCentral.model.Database;
import bancoCentral.model.Response;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class PayCollectController {
    Database db = new Database();
    private Costumer costumerLogged = db.getCostumerLogged();
    private Account costumerAccount = costumerLogged.getAccount();

    @FXML
    public TextField keyCollectValueInput;
    public Button btnGoToMenu;
    public Button btnGoToSucess;


    public Response payCollect(String key){
        return costumerAccount.payCollect(key);
    }


    public void goToSuccess(ActionEvent actionEvent) throws IOException {
        Response depositResponse = payCollect(keyCollectValueInput.getText());
        if(!depositResponse.getSucess()){
            System.out.println(depositResponse.getMessage());
            return;
        }
        Parent root = FXMLLoader.load(getClass().getResource("/bancoCentral/view/PayCollectViewSuccess.fxml"));
        Stage window = (Stage) btnGoToSucess.getScene().getWindow();
        window.setScene(new Scene(root));
        System.out.println("Testee");
    }

    public void goToMainScreen(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/bancoCentral/view/MainMenuView.fxml"));
        Stage window = (Stage) btnGoToMenu.getScene().getWindow();
        window.setScene(new Scene(root));
    }
}
