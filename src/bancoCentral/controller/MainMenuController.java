package bancoCentral.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {
    @FXML
    Button btnDeposit;
    @FXML
    Button btnWithdraw;
    @FXML
    Button btnTransfer;
    @FXML
    Button btnPayCollect;
    @FXML
    Button btnCollect;
    @FXML
    Button btnExtract;

    @FXML
    public void goToDepositScreen(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/bancoCentral/view/DepositView.fxml"));
        Stage window = (Stage) btnDeposit.getScene().getWindow();
        window.setScene(new Scene(root));
    }

    public void goToPayCollect(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/bancoCentral/view/PayCollectView.fxml"));
        Stage window = (Stage) btnDeposit.getScene().getWindow();
        window.setScene(new Scene(root));
    }

    public void goToCollect(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/bancoCentral/view/CollectView.fxml"));
        Stage window = (Stage) btnDeposit.getScene().getWindow();
        window.setScene(new Scene(root));
    }

    public void goToWithdrawScreen(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/bancoCentral/view/WithdrawView.fxml"));
        Stage window = (Stage) btnDeposit.getScene().getWindow();
        window.setScene(new Scene(root));
    }

    public void goToBalanceScreen(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/bancoCentral/view/BalanceView.fxml"));
        Stage window = (Stage) btnDeposit.getScene().getWindow();
        window.setScene(new Scene(root));
    }


    @FXML
    public void goToTransferScreen(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/bancoCentral/view/TransferView.fxml"));
        Stage window = (Stage) btnTransfer.getScene().getWindow();
        window.setScene(new Scene(root));
    }
}
