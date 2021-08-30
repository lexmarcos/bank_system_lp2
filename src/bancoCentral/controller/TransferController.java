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

public class TransferController {
    Database db = new Database();
    private Costumer costumerLogged = db.getCostumerLogged();
    private Account costumerAccount = costumerLogged.getAccount();
    @FXML
    TextField keyInput;
    @FXML
    TextField valueInput;
    @FXML
    Button btnGoToMenu;
    @FXML
    Button btnGoToSucess;

    public void goToMainScreen(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/bancoCentral/view/MainMenuView.fxml"));
        Stage window = (Stage) btnGoToMenu.getScene().getWindow();
        window.setScene(new Scene(root));
    }

    public void goToSuccess(ActionEvent actionEvent) throws IOException {
        Response transferResponse = transfer(keyInput.getText(), valueInput.getText());
        if(!transferResponse.getSucess()){
            System.out.println(transferResponse.getMessage());
            return;
        }
        Parent root = FXMLLoader.load(getClass().getResource("/bancoCentral/view/TransferSucessView.fxml"));
        Stage window = (Stage) btnGoToMenu.getScene().getWindow();
        window.setScene(new Scene(root));
    }

    public Response transfer(String key, String amount){
        if(amount.isEmpty()) return new Response("Insira um valor válido", false);
        String type;
        if(key.length() == 5){
            type = "number";
        }else{
            type = "pixKey";
        }
        float amountConverted;
        try {
            amountConverted = Float.parseFloat(amount);
        } catch(NumberFormatException e){
            return new Response("Insira um valor válido", false);
        }
        return costumerAccount.transfer(amountConverted, key, type);
    }
}
