package bancoCentral.controller;

import bancoCentral.model.Account;
import bancoCentral.model.Costumer;
import bancoCentral.model.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.io.IOException;

public class TransferSucessController {
    Database db = new Database();

    @FXML
    Label transferValueLabel;
    @FXML
    Label currentBalanceLabel;
    @FXML
    Label toCostumer;
    @FXML
    Button btnGoToMenu;
    @FXML
    public void initialize() {
        this.setAmountDeposit();
        this.setBalance();
        this.setToCostumer();
    }

    private Costumer costumerLogged = db.getCostumerLogged();
    private Account costumerAccount = costumerLogged.getAccount();

    public void setAmountDeposit() {
        JSONObject lastTransaction = costumerAccount.getLastTransaction();
        this.transferValueLabel.setText(lastTransaction.get("amount").toString());
    }

    public void setToCostumer() {
        JSONObject lastTransaction = costumerAccount.getLastTransaction();
        if(lastTransaction.get("pixKey").toString().isEmpty()){
            this.toCostumer.setText(lastTransaction.get("number").toString());
        }else{
            this.toCostumer.setText(lastTransaction.get("pixKey").toString());
        }
    }

    public void goToMainScreen(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/bancoCentral/view/MainMenuView.fxml"));
        Stage window = (Stage) btnGoToMenu.getScene().getWindow();
        window.setScene(new Scene(root));
    }

    public void setBalance() {
        this.currentBalanceLabel.setText(Float.toString(costumerAccount.getBalance()));
    }
}
