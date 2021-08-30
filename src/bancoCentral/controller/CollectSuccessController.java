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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.io.IOException;

public class CollectSuccessController {
    Database db = new Database();

    @FXML
    Label collectValueLabel;
    @FXML
    TextField keyCollectLabel;
    @FXML
    Button btnGoToMenu;
    @FXML
    public void initialize() {
        this.setAmountDeposit();
        this.setBalance();
    }

    private Costumer costumerLogged = db.getCostumerLogged();
    private Account costumerAccount = costumerLogged.getAccount();

    public void setAmountDeposit() {
        JSONObject lastCollect = db.findLastCollectOfCostumer(costumerAccount.getPixKey());
        this.collectValueLabel.setText(lastCollect.get("amount").toString());
    }

    public void goToMainScreen(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/bancoCentral/view/MainMenuView.fxml"));
        Stage window = (Stage) btnGoToMenu.getScene().getWindow();
        window.setScene(new Scene(root));
    }

    public void setBalance() {
        JSONObject lastCollect = db.findLastCollectOfCostumer(costumerAccount.getPixKey());
        this.keyCollectLabel.setText(lastCollect.get("id").toString());
    }
}
