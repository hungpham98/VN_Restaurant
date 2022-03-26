/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import dao.Category;
import dao.Product;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import service.Navigator;

/**
 * FXML Controller class
 *
 * @author pc
 */
public class EditProductUIController implements Initializable {

    @FXML
    private Label lbTitle;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtPrice;

    @FXML
    private JFXTextField txtIngredient;

    @FXML
    private JFXTextField txtAmount;

    @FXML
    private JFXTextField txtImg;

    @FXML
    private JFXComboBox<Category> cbCategory;

    @FXML
    private JFXButton btnSubmit;

    @FXML
    private JFXButton btnBack;

    @FXML
    private Label lbMessage;

    @FXML
    private JFXButton btnUpload;

    @FXML
    void btnSubmitClick(ActionEvent event) throws SQLException, IOException {
        if (this.editProduct == null) {
            if (validateForm()) {
                Product insertProduct = extractProductFromFields();
                insertProduct = Product.insert(insertProduct);

                String msg = "New product added with new id: " + insertProduct.getProductID();
                lbMessage.setText(msg);
            }
        } else {
            if (validateForm()) {
                Product updateProduct = extractProductFromFields();
                updateProduct.setProductID(this.editProduct.getProductID());
                boolean result = Product.update(updateProduct);

                if (result) {
                    lbMessage.setText("Update Success");
                }
            }
        }
        try {
           saveToFile(img, txtImg.getText());  
        } catch (Exception e) {
        }
    }

    @FXML
    void btnBackClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToProduct();
    }

    @FXML
    void btnUploadClick(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new ExtensionFilter("JPEG", "*.jpg"));
        File f = fc.showOpenDialog(stage);

        if (f != null) {
            img = new Image(f.toURI().toString());
            txtImg.setText(f.getName());
        }
    }

    /**
     * Initializes the controller class.
     */
    private Product editProduct;
    private Image img;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<Category> category = Category.selectAll();
        cbCategory.getItems().addAll(category);

    }

    public void initialize(Product editProduct) {
        this.editProduct = editProduct;
        String msg;

        if (this.editProduct == null) {
            msg = "Create new product";
        } else {
            msg = "Edit existing product";
            txtName.setText(this.editProduct.getName());
            txtPrice.setText(this.editProduct.getPrice().toString());
            txtIngredient.setText(this.editProduct.getIngre());
            txtAmount.setText(this.editProduct.getAmount().toString());
            txtImg.setText(this.editProduct.getImg());
            cbCategory.getSelectionModel().select(this.editProduct.getCategory().getCategoryID() - 1);
        }
        lbTitle.setText(msg);
    }

    private Product extractProductFromFields() {
        Product product = new Product();
        product.setName(txtName.getText());
        product.setPrice(Integer.parseInt(txtPrice.getText()));
        product.setIngre(txtIngredient.getText());
        product.setAmount(Integer.parseInt(txtAmount.getText()));
        product.setImg(txtImg.getText());
        product.setCategory(cbCategory.getValue());

        return product;
    }

    public boolean validateForm() {
        boolean val = true;

        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Product name must be given");
        validator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE));
        txtName.getValidators().add(validator);
        txtName.validate();
        txtName.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                txtName.validate();
            }
        });

        validator = new RequiredFieldValidator();
        validator.setMessage("Image must be given");
        validator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE));
        txtImg.getValidators().add(validator);
        txtImg.validate();
        txtImg.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                txtImg.validate();
            }
        });

        validator = new RequiredFieldValidator();
        validator.setMessage("Price must be given");
        validator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE));
        txtPrice.getValidators().add(validator);
        txtPrice.validate();
        txtPrice.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                txtPrice.validate();
            }
        });

        validator = new RequiredFieldValidator();
        validator.setMessage("Ingredient must be given");
        validator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE));
        txtIngredient.getValidators().add(validator);
        txtIngredient.validate();
        txtIngredient.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                txtIngredient.validate();
            }
        });

        validator = new RequiredFieldValidator();
        validator.setMessage("Serving must be given");
        validator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE));
        txtAmount.getValidators().add(validator);
        txtAmount.validate();
        txtAmount.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                txtAmount.validate();
            }
        });

        validator = new RequiredFieldValidator();
        validator.setMessage("Category must be given");
        validator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE));
        cbCategory.getValidators().add(validator);
        cbCategory.validate();
        cbCategory.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                cbCategory.validate();
            }
        });

        if (!txtName.validate() || !txtImg.validate() || !txtPrice.validate()
                || !txtIngredient.validate() || !txtAmount.validate() || !cbCategory.validate()) {
            val = false;
        }
        return val;
    }

    public void saveToFile(Image image, String name) throws IOException {
        File newFile = new File(System.getProperty("user.dir") + "\\src\\img\\" + name);
        BufferedImage bi = SwingFXUtils.fromFXImage(image, null);

        ImageIO.write(bi, "jpg", newFile);
    }
}
