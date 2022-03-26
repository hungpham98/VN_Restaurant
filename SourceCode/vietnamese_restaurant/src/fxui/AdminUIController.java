/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import dao.Admin;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.BorderPane;
import service.Navigator;

/**
 * FXML Controller class
 *
 * @author pc
 */
public class AdminUIController implements Initializable {

    @FXML
    private BorderPane borderPane;

    @FXML
    private JFXButton btnProduct;

    @FXML
    private JFXButton btnLogout;

    @FXML
    private JFXTreeTableView<Admin> tvAdmin;

    @FXML
    private JFXButton btnCategory;
    @FXML
    private JFXButton btnMember;
    @FXML
    private JFXTextField txtUsername;
    @FXML
    private JFXTextField txtName;
    @FXML
    private JFXTextField txtEmail;
    @FXML
    private JFXDatePicker dateDob;
    @FXML
    private JFXPasswordField pwdPassword;
    @FXML
    private JFXButton btnInsertAdmin;
    @FXML
    private JFXButton btnDeleteAdmin;
    @FXML
    private JFXButton btnUpdateAdmin;
    @FXML
    private Label lbMessage;

    ObservableList<Admin> content;
    Admin selectedAdmin = new Admin();
    @FXML
    private JFXButton btnOrder;
    @FXML
    private JFXButton btnSubmit;

    @FXML
    void btnCategoryClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToCategory();
    }

    @FXML
    void btnDeleteAdminClick(ActionEvent event) {
        if (tvAdmin.getSelectionModel().getSelectedItem() == null) {
            selectAdminWarning();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Deleting a admin");
            alert.setHeaderText("Are you sure you want to delete this selected admin?");
            Optional<ButtonType> comfirmationResponse = alert.showAndWait();

            if (comfirmationResponse.get() == ButtonType.OK) {
                Admin deleteAdmin = tvAdmin.getSelectionModel().getSelectedItem().getValue();
                boolean result = Admin.delete(deleteAdmin);
                if (result) {
                    content.remove(deleteAdmin);
                    lbMessage.setText("Delete Success");
                    System.out.println("An admin is deleted");
                    tvAdmin.getColumns().clear();
                    buildTable();
                } else {
                    System.err.println("No admin is deleted");
                }
            }
        }
    }

    @FXML
    void btnInsertAdminClick(ActionEvent event) {
        tvAdmin.getSelectionModel().clearSelection();
        txtUsername.setText(null);
        pwdPassword.setText(null);
        txtName.setText(null);
        txtEmail.setText(null);
        dateDob.setValue(null);

    }

    @FXML
    void btnUpdateAdminClick(ActionEvent event) {
        if (tvAdmin.getSelectionModel().getSelectedItem() == null) {
            selectAdminWarning();
        } else {
            selectedAdmin = tvAdmin.getSelectionModel().getSelectedItem().getValue();
            txtUsername.setText(selectedAdmin.getUsername());
            txtName.setText(selectedAdmin.getName());
            txtEmail.setText(selectedAdmin.getEmail());
            dateDob.setValue(LocalDate.parse(selectedAdmin.getBirthday(), DateTimeFormatter.ISO_DATE));

        }
    }

    @FXML
    void btnSubmitClick(ActionEvent event) throws SQLException {
        if (tvAdmin.getSelectionModel().getSelectedItem() == null) {
            System.out.println("true");
            if (validateForm()) {
                Admin insertAdmin = extractAdminFromFields();
                insertAdmin = Admin.insert(insertAdmin);

                String msg = "New admin added with new id: " + insertAdmin.getId();
                lbMessage.setText(msg);
                tvAdmin.getColumns().clear();
                buildTable();
            }
        } else {
            if (validateForm()) {
                Admin updateAdmin = extractAdminFromFields();
                updateAdmin.setId(this.selectedAdmin.getId());
                boolean result = Admin.update(updateAdmin);
                if (result) {
                    lbMessage.setText("Update Success");
                    tvAdmin.getColumns().clear();
                    buildTable();
                }
            }
        }
    }

    @FXML
    void btnLogoutClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToLogin();
    }

    @FXML
    void btnMemberClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToMember();
    }

    @FXML
    void btnProductClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToProduct();
    }

    @FXML
    private void btnOrderClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToOrder();
    }

    @FXML
    void btnRefeshClick(ActionEvent event) {
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buildTable();
    }

    public void buildTable() {
        JFXTreeTableColumn<Admin, String> username = new JFXTreeTableColumn<>("Username");
        username.setCellValueFactory((TreeTableColumn.CellDataFeatures<Admin, String> param) -> param.getValue().getValue().getUsernameProperty());

        JFXTreeTableColumn<Admin, String> password = new JFXTreeTableColumn<>("Password");
        password.setCellValueFactory((TreeTableColumn.CellDataFeatures<Admin, String> param) -> param.getValue().getValue().getPasswordProperty());

        JFXTreeTableColumn<Admin, String> name = new JFXTreeTableColumn<>("Full name");
        name.setCellValueFactory((TreeTableColumn.CellDataFeatures<Admin, String> param) -> param.getValue().getValue().getNameProperty());

        JFXTreeTableColumn<Admin, String> email = new JFXTreeTableColumn<>("Email");
        email.setCellValueFactory((TreeTableColumn.CellDataFeatures<Admin, String> param) -> param.getValue().getValue().getEmailProperty());

        JFXTreeTableColumn<Admin, String> birthday = new JFXTreeTableColumn<>("Birthday");
        birthday.setCellValueFactory((TreeTableColumn.CellDataFeatures<Admin, String> param) -> param.getValue().getValue().getBirthdayProperty());

        tvAdmin.getColumns().addAll(username, password, name, email, birthday);
        content = Admin.selectAll();

        TreeItem<Admin> root = new RecursiveTreeItem<>(content, RecursiveTreeObject::getChildren);
        tvAdmin.setRoot(root);
        tvAdmin.setShowRoot(false);
    }

    public boolean validateForm() {

        boolean val = true;

        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("User must be given");
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
        pwdPassword.getValidators().add(validator);
        pwdPassword.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                pwdPassword.validate();
            }
        });

        validator = new RequiredFieldValidator();
        validator.setMessage("Name must be given");
        validator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE));
        txtName.getValidators().add(validator);
        txtName.validate();
        txtName.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                txtName.validate();
            }
        });

        validator = new RequiredFieldValidator();
        validator.setMessage("Email must be given");
        validator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE));
        txtEmail.getValidators().add(validator);
        txtEmail.validate();
        txtEmail.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                txtEmail.validate();
            }
        });

        validator = new RequiredFieldValidator();
        validator.setMessage("Birthday must be given");
        validator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE));
        dateDob.getValidators().add(validator);
        dateDob.validate();
        dateDob.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                dateDob.validate();
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
        v.setMessage("Username must consist of more than 6 or greater than 30 characters");
        v.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE));
        txtUsername.getValidators().add(v);
        txtUsername.validate();

        if (!pwdPassword.validate() || !txtUsername.validate() || !txtName.validate()
                || !txtEmail.validate() || !dateDob.validate() || !txtUsername.validate()) {
            val = false;
        }
        return val;
    }

    private Admin extractAdminFromFields() {
        Admin admin = new Admin();
        admin.setUsername(txtUsername.getText());
        admin.setPasword(pwdPassword.getText());
        admin.setName(txtName.getText());
        admin.setEmail(txtEmail.getText());
        admin.setBirthday(dateDob.getValue().toString());

        return admin;
    }

    private void selectAdminWarning() {
        String alertTitle = "Please select an admin";
        String alertText = "An admin must be selected for the operation";
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(alertTitle);
        alert.setHeaderText(alertText);
        alert.showAndWait();
    }
}
