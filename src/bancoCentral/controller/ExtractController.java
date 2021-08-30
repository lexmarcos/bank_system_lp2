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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;

public class ExtractController {
    Database db = new Database();
    private Costumer costumerLogged = db.getCostumerLogged();
    private Account costumerAccount = costumerLogged.getAccount();

    @FXML
    Label currentBalanceLabel;

    @FXML
    public TextArea extractValueInput;
    public Button btnGoToMenu;

    @FXML
    public void initialize() {
        this.setBalance();
        this.setExtract();
    }


    public void setExtract(){
        JSONArray historic = costumerAccount.getHistoric();
        StringBuilder text = new StringBuilder();

        for (Object o : historic) {
            JSONObject currentTransaction = (JSONObject) o;
            if (currentTransaction.containsKey("pixKey")) {
                text.append(String.format("" +
                                "\nValor da transação: %s" +
                                "\nChave Pix: %s" +
                                "\nTipo: %s\n\n",
                        currentTransaction.get("amount").toString(), currentTransaction.get("pixKey").toString(), currentTransaction.get("type").toString()
                ));
            } else {
                text.append(String.format("" +
                                "\nValor da transação: %s" +
                                "\nTipo: %s\n\n",
                        currentTransaction.get("amount").toString(), currentTransaction.get("type").toString()
                ));
            }
        }

        this.extractValueInput.setText(text.toString());
    }


    public Response payCollect(String key){
        return costumerAccount.payCollect(key);
    }

    public void setBalance() {
        this.currentBalanceLabel.setText(Float.toString(costumerAccount.getBalance()));
    }

    public void goToMainScreen(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/bancoCentral/view/MainMenuView.fxml"));
        Stage window = (Stage) btnGoToMenu.getScene().getWindow();
        window.setScene(new Scene(root));
    }
}
