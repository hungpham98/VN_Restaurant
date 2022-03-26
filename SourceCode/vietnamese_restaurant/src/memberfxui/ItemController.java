/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memberfxui;

import com.jfoenix.controls.JFXButton;
import dao.Product;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import main.MyListener;

/**
 * FXML Controller class
 *
 * @author Hung
 */
public class ItemController implements Initializable {

    @FXML
    private Label itemName;
    @FXML
    private Label itemPrice;
    @FXML
    private ImageView itemImg;
    @FXML
    JFXButton btnAdd;
    @FXML
    FontAwesomeIconView icon;
//    @FXML
//    void click(MouseEvent event) {
//        myListener.onClickListener(product);
//    }
    @FXML
    private void click(MouseEvent mouseEvent){
        myListener.onClickListener(product);
    }
    
    public Product product;
    private MyListener myListener;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setData(Product product, MyListener myListener) {
        this.product = product;
        this.myListener = myListener;
        this.itemName.setText(product.getName());
        this.itemPrice.setText(product.getPrice().toString());
        Class<?> clazz = this.getClass();
        InputStream input = clazz.getResourceAsStream("/img/" + product.getImg());
        this.itemImg.setImage(new Image(input));
    }
}
