/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memberfxui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import dao.Product;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author pc
 */
public class CartItemsController implements Initializable {

    @FXML
    private ImageView imgProduct;
    @FXML
    private Text txtProductName;
    @FXML
    private Label lbPrice;
    @FXML
    Spinner<Integer> spAmount;
    @FXML
    JFXCheckBox cbProduct;
    @FXML
    JFXButton btnDelete;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    int price = 0;
    int amount = 0;
    Cart cart = new Cart();
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    public void setData(Cart cart) {
        this.cart = cart;
        Product selectProduct = Product.selectProductByID(cart.getProductID());
        txtProductName.setText(selectProduct.getName());
        price = selectProduct.getPrice();
        lbPrice.setText(String.valueOf(price));
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,99);
        spAmount.setValueFactory(valueFactory);
        spAmount.getValueFactory().setValue(cart.getAmount());
        Class<?> clazz = this.getClass();
        InputStream input = clazz.getResourceAsStream("/img/" + selectProduct.getImg());
        imgProduct.setImage(new Image(input));
    }
    
}
