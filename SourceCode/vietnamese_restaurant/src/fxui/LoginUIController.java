/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import dao.Admin;
import dao.Member;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import memberfxui.RegisterMemberUIController;
import service.Navigator;

/**
 * FXML Controller class
 *
 * @author pc
 */
public class LoginUIController implements Initializable {

    @FXML
    private JFXTextField txtUsername;
    @FXML
    private JFXPasswordField pwfPassword;
    @FXML
    private JFXComboBox<String> cbUser;
    @FXML
    private JFXButton btnLogin;
    @FXML
    private JFXButton btnRegister;
    @FXML
    private StackPane spTransition;
    @FXML
    private AnchorPane ac;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    String user;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbUser.getItems().addAll("Admin", "Member");
        cbUser.getSelectionModel().select(1);
        user = cbUser.getSelectionModel().getSelectedItem();

        cbUser.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            user = newValue;
            if (newValue.equals("Member")) {
                btnRegister.setVisible(true);
            } else {
                btnRegister.setVisible(false);
            }
        });

    }

    @FXML
    private void btnLoginClick(ActionEvent event) throws IOException, SQLException {
        String username = txtUsername.getText();
        String password = pwfPassword.getText();
        if (user.equals("Admin")) {
            if (validateLogin()) {
                Admin admin = new Admin();
                if (!admin.checkLogin(username, password)) {
                    infoBox("Username or password is incorrect", null, "Failed");
                } else {
                    Navigator.getInstance().goToAdmin();
                }
            }
        } else {
            if (validateLogin()) {
                Member member = new Member();
                if(!member.checkLogin(username, password)){
                    infoBox("Username or password is incorrect", null, "Failed");
                }else {
                    Navigator.getInstance().goToIndex(username);
                }
            }
        }
    }

    @FXML
    private void btnRegisterClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/memberfxui/RegisterMemberUI.fxml"));
        AnchorPane root = fxmlLoader.load();
        RegisterMemberUIController ctrl = fxmlLoader.getController();
        ctrl.btnMemBack.setOnAction((ActionEvent event1) -> {
            spTransition.getChildren().setAll(ac);
        });
        spTransition.getChildren().setAll(root);
    }

    public boolean validateLogin() {
        boolean val = true;

        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Username must be given");
        validator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE));
        txtUsername.getValidators().add(validator);
        txtUsername.validate();
        txtUsername.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                txtUsername.validate();
            }
        });

        validator = new RequiredFieldValidator();
        validator.setMessage("Password must be given");
        validator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE));
        pwfPassword.getValidators().add(validator);
        pwfPassword.validate();
        pwfPassword.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                pwfPassword.validate();
            }
        });
        if (!txtUsername.validate() || !pwfPassword.validate()) {
            val = false;
        }
        return val;
    }

    public static void infoBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }
}
