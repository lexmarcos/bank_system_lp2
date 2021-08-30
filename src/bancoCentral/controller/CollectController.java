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

public class CollectController {
    Database db = new Database();
    private Costumer costumerLogged = db.getCostumerLogged();
    private Account costumerAccount = costumerLogged.getAccount();

    @FXML
    public TextField collectValue;
    public Button btnGoToMenu;
    public Button btnGoToSuccess;


    public Response collect(String amount){
        if(amount.isEmpty()) return new Response("Insira um valor válido", false);
        float amountConverted;
        try {
            amountConverted = Float.parseFloat(amount);
        } catch(NumberFormatException e){
            return new Response("Insira um valor válido", false);
        }
        return costumerAccount.collect(amountConverted);
    }

    @FXML
    public void goToSucessCollect(ActionEvent actionEvent) throws IOException {
        Response collectResponse = collect(collectValue.getText());
        if(!collectResponse.getSucess()){
            System.out.println(collectResponse.getMessage());
            return;
        }
        Parent root = FXMLLoader.load(getClass().getResource("/bancoCentral/view/CollectSuccessView.fxml"));
        Stage window = (Stage) btnGoToSuccess.getScene().getWindow();
        window.setScene(new Scene(root));
    }

    public void goToMainScreen(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/bancoCentral/view/MainMenuView.fxml"));
        Stage window = (Stage) btnGoToMenu.getScene().getWindow();
        window.setScene(new Scene(root));
    }
}
