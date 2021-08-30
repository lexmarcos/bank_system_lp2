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

public class WithdrawController {
    @FXML
    public Button btnGoToMenu, btnGoToSuccess;
    public TextField depositValueInput;

    Database db = new Database();
    private Costumer costumerLogged = db.getCostumerLogged();
    private Account costumerAccount = costumerLogged.getAccount();

    public Response withdraw(String amount){
        if(amount.isEmpty()) return new Response("Insira um valor válido", false);
        float amountConverted;
        try {
            amountConverted = Float.parseFloat(amount);
        } catch(NumberFormatException e){
            return new Response("Insira um valor válido", false);
        }
        return costumerAccount.withdraw(amountConverted);
    }

    public void goToMainScreen(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/bancoCentral/view/MainMenuView.fxml"));
        Stage window = (Stage) btnGoToMenu.getScene().getWindow();
        window.setScene(new Scene(root));
    }

    public void goToSuccessWithdraw(ActionEvent actionEvent) throws IOException {
        Response depositResponse = withdraw(depositValueInput.getText());
        if(!depositResponse.getSucess()){
            System.out.println(depositResponse.getMessage());
            return;
        }
        Parent root = FXMLLoader.load(getClass().getResource("/bancoCentral/view/WithdrawSucessView.fxml"));
        Stage window = (Stage) btnGoToSuccess.getScene().getWindow();
        window.setScene(new Scene(root));
    }
}
