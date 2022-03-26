/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memberfxui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import dao.Member;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;

/**
 * FXML Controller class
 *
 * @author Hung
 */
public class RegisterMemberUIController implements Initializable {

    @FXML
    private JFXTextField txtMemName;
    @FXML
    private JFXTextField txtMemAddress;
    @FXML
    private JFXTextField txtMemPhone;
    @FXML
    private JFXTextField txtMemUsername;
    @FXML
    private JFXPasswordField txtMemPassword;
    @FXML
    private JFXButton btnMemSubmit;
    @FXML
    public JFXButton btnMemBack;
    @FXML
    private JFXTextField txtMemEmail;
    @FXML
    private Label lbMessage;

    @FXML
    private void btnSubmitClick(ActionEvent event) throws SQLException {
        if (validateForm()) {
            Member insertMember = extractMemberFromFields();
            Member.insert(insertMember);
            lbMessage.setText("SUCCESSFULLY SIGN UP");
        }
    }


    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    private Member extractMemberFromFields() {
        Member member = new Member();
        member.setMemName(txtMemName.getText());
        member.setMemAddress(txtMemAddress.getText());
        member.setMemPhone(txtMemPhone.getText());
        member.setMemUsername(txtMemUsername.getText());
        member.setMemPw(txtMemPassword.getText());
        member.setMemEmail(txtMemEmail.getText());

        return member;
    }

    public boolean validateForm() {
        boolean val = true;

        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Member name must be\ngiven");
        validator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE));
        txtMemName.getValidators().add(validator);
        txtMemName.validate();
        txtMemName.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                txtMemName.validate();
            }
        });

        validator = new RequiredFieldValidator();
        validator.setMessage("Address must be given");
        validator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE));
        txtMemAddress.getValidators().add(validator);
        txtMemAddress.validate();
        txtMemAddress.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                txtMemAddress.validate();
            }
        });

        validator = new RequiredFieldValidator();
        validator.setMessage("Phone number must be\ngiven");
        validator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE));
        txtMemPhone.getValidators().add(validator);
        txtMemPhone.validate();
        txtMemPhone.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                txtMemPhone.validate();
            }
        });

        validator = new RequiredFieldValidator();
        validator.setMessage("Member Username must\nbe given");
        validator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE));
        txtMemUsername.getValidators().add(validator);
        txtMemUsername.validate();
        txtMemUsername.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                txtMemUsername.validate();
            }
        });

        validator = new RequiredFieldValidator();
        validator.setMessage("Member password must\nbe given");
        validator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE));
        txtMemPassword.getValidators().add(validator);
        txtMemPassword.validate();
        txtMemPassword.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                txtMemPassword.validate();
            }
        });

        validator = new RequiredFieldValidator();
        validator.setMessage("Email must be given");
        validator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE));
        txtMemEmail.getValidators().add(validator);
        txtMemEmail.validate();
        txtMemEmail.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                txtMemEmail.validate();
            }
        });
        
        ValidatorBase v = new ValidatorBase() {
            @Override
            protected void eval() {
                if (srcControl.get() instanceof TextInputControl) {
                    TextInputControl textField = (TextInputControl) srcControl.get();
                    if (!textField.getText().matches("\\w{5,29}")) {
                        hasErrors.set(true);
                    } else {
                        hasErrors.set(false);
                    }
                }
            }
        };
        v.setMessage("Username must consist\nof more than 6 or greater\nthan 30 characters");
        v.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE));
        txtMemUsername.getValidators().add(v);
        txtMemUsername.validate();
        
        v = new ValidatorBase() {
            @Override
            protected void eval() {
                if (srcControl.get() instanceof TextInputControl) {
                    TextInputControl textField = (TextInputControl) srcControl.get();
                    if (!textField.getText().matches("\\w{5,29}")) {
                        hasErrors.set(true);
                    } else {
                        hasErrors.set(false);
                    }
                }
            }
        };
        v.setMessage("Password must consist\nof more than 6 or greater\nthan 30 characters");
        v.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE));
        txtMemPassword.getValidators().add(v);
        txtMemPassword.validate();
        
        if (!txtMemEmail.validate() || !txtMemPassword.validate() || !txtMemUsername.validate() || !txtMemPhone.validate() || !txtMemAddress.validate() || !txtMemName.validate()) {
            val = false;
        }
        return val;
    }

}
