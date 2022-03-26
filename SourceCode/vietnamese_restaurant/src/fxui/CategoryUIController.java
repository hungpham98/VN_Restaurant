/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.validation.RequiredFieldValidator;
import dao.Category;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.BorderPane;
import service.Navigator;

/**
 * FXML Controller class
 *
 * @author pc
 */
public class CategoryUIController implements Initializable {

    @FXML
    private BorderPane borderPane;

    @FXML
    private JFXButton btnAdmin;

    @FXML
    private JFXButton btnProduct;

    @FXML
    private JFXButton btnMember;

    @FXML
    private JFXButton btnLogout;

    @FXML
    private Label lbMessage;

    @FXML
    private JFXTreeTableView<Category> tvCategory;

    @FXML
    private JFXTextField txtCategoryName;

    @FXML
    private JFXButton btnInsertCategory;

    @FXML
    private JFXButton btnUpdateCategory;

    @FXML
    private JFXButton btnDeleteCategory;
    @FXML
    private JFXButton btnOrder;

    @FXML
    void btnAdminClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToAdmin();
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

    @FXML
    void btnCategoryClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToCategory();
    }

    @FXML
    void btnDeleteCategoryClick(ActionEvent event) {
        if (tvCategory.getSelectionModel().getSelectedItem() == null) {
            selectCategoryWarning();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Deleting a Category");
            alert.setHeaderText("Are you sure you want to delete this selected Category?");
            Optional<ButtonType> comfirmationResponse = alert.showAndWait();

            if (comfirmationResponse.get() == ButtonType.OK) {
                Category deleteCategory = selectedCategory.get();
                boolean result = Category.delete(deleteCategory);
                if (result) {
                    content.remove(deleteCategory);
                    lbMessage.setText("Delete Success");
                    System.out.println("A Category is deleted");
                    tvCategory.getColumns().clear();
                    selectedCategory = null;
                    buildTable();
                } else {
                    System.err.println("No Category is deleted");
                }
            }
        }
    }

    @FXML
    void btnInsertCategoryClick(ActionEvent event) throws SQLException {
        if (validateForm()) {
            Category insertCategory = extractCategoryFromFields();
            insertCategory = Category.insert(insertCategory);
            content.add(insertCategory);

            String msg = "New category added with new id: " + insertCategory.getCategoryID();
            lbMessage.setText(msg);
            tvCategory.getColumns().clear();
            selectedCategory = null;
            buildTable();
        }
    }

    @FXML
    void btnUpdateCategoryClick(ActionEvent event) {
        if (tvCategory.getSelectionModel().getSelectedItem() == null) {
            selectCategoryWarning();
        } else {
            if (validateForm()) {
                Category updateCategory = extractCategoryFromFields();
                updateCategory.setCategoryID(this.selectedCategory.getValue().getCategoryID());
                boolean result = Category.update(updateCategory);

                if (result) {
                    lbMessage.setText("Update Success");
                    tvCategory.getColumns().clear();
                    selectedCategory = null;
                    buildTable();
                }
            }
        }
    }
    ObservableList<Category> content;
    ObjectProperty<Category> selectedCategory = new SimpleObjectProperty<>();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buildTable();
        selectedCategory.bind(Bindings.createObjectBinding(() -> {
            TreeItem<Category> selectedItem = tvCategory.getSelectionModel().getSelectedItem();
            return selectedItem == null ? null : selectedItem.getValue();
        }, tvCategory.getSelectionModel().selectedItemProperty()));

        selectedCategory.addListener((obs, oldCategory, newCategory) -> {
            txtCategoryName.setText(newCategory.getCategoryName());
        });

    }

    public boolean validateForm() {

        boolean val = true;

        RequiredFieldValidator validator = new RequiredFieldValidator();
        FontAwesomeIconView fa = new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE);
        txtCategoryName.getValidators().add(validator);
        validator.setMessage("Category name must be given");
        validator.setIcon(fa);
        txtCategoryName.getValidators().add(validator);
        txtCategoryName.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                txtCategoryName.validate();
            }
        });
        if (!txtCategoryName.validate()) {
            val = false;
        }
        return val;
    }

    private Category extractCategoryFromFields() {
        Category category = new Category();
        category.setCategoryName(txtCategoryName.getText());

        return category;
    }

    private void selectCategoryWarning() {
        String alertTitle = "Please select a category";
        String alertText = "A category must be selected for the operation";
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(alertTitle);
        alert.setHeaderText(alertText);
        alert.showAndWait();
    }

    public void buildTable() {
        JFXTreeTableColumn<Category, String> categoryname = new JFXTreeTableColumn<>("Category name:");
        categoryname.setPrefWidth(350);
        categoryname.setCellValueFactory((TreeTableColumn.CellDataFeatures<Category, String> param) -> param.getValue().getValue().getCategoryNameProperty());
        tvCategory.getColumns().addAll(categoryname);

        content = Category.selectAll();
        TreeItem<Category> root = new RecursiveTreeItem<>(content, RecursiveTreeObject::getChildren);
        tvCategory.setRoot(root);
        tvCategory.setShowRoot(false);
    }

}
