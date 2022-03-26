/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memberfxui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
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
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import service.Navigator;

/**
 * FXML Controller class
 *
 * @author Hung
 */
public class EditMemberUIController implements Initializable {

    @FXML
    private JFXTextField editname;
    @FXML
    private Text editusername;
    @FXML
    private JFXTextField editaddress;
    @FXML
    private JFXTextField editphone;
    @FXML
    private JFXTextField editemail;
    @FXML
    private JFXButton accsubmit;
    @FXML
    private JFXButton accbackbtn;
    @FXML
    private Label acclabeledit;
    @FXML
    private JFXButton btnMemBack;

    @FXML
    void accsubmitClick(ActionEvent event) {
        if (validateForm()) {
                Member updateMember = extractMemberFromFields();
                updateMember.setMemId(this.editMember.getMemId());
                boolean result = Member.update(updateMember);
                if (result) {
                    acclabeledit.setText("Update Success");
                }
            }
    }

    @FXML
    void accbackbtnClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToIndex(editMember.getMemUsername());
    }

    /**
     * Initializes the controller class.
     */
    private Member editMember;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void initialize(String username) throws SQLException {
        this.editMember = Member.selectbyUsername(username);
        editname.setText(this.editMember.getMemName());
        editusername.setText(this.editMember.getMemUsername());
        editaddress.setText(this.editMember.getMemAddress());
        editphone.setText(this.editMember.getMemPhone());
        editemail.setText(this.editMember.getMemEmail());
    }
    private Member extractMemberFromFields() {
        Member member = new Member();
        member.setMemName(editname.getText());
        member.setMemAddress(editaddress.getText());
        member.setMemEmail(editemail.getText());
        member.setMemPhone(editphone.getText());
        return member;
    }
    public boolean validateForm() {
        boolean val = true;

        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Member name must be given");
        validator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE));
        editname.getValidators().add(validator);
        editname.validate();
        editname.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                editname.validate();
            }
        });

        validator = new RequiredFieldValidator();
        validator.setMessage("Address must be given");
        validator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE));
        editaddress.getValidators().add(validator);
        editaddress.validate();
        editaddress.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                editaddress.validate();
            }
        });

        validator = new RequiredFieldValidator();
        validator.setMessage("Phone number must be given");
        validator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE));
        editphone.getValidators().add(validator);
        editphone.validate();
        editphone.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                editphone.validate();
            }
        });

        validator = new RequiredFieldValidator();
        validator.setMessage("Email must be given");
        validator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE));
        editemail.getValidators().add(validator);
        editemail.validate();
        editemail.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                editemail.validate();
            }
        });


        if (!editname.validate() || !editaddress.validate() || !editphone.validate()
                || !editemail.validate() ) {
            val = false;
        }
        return val;
    }

}
