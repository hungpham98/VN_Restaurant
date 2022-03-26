/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.Product;
import fxui.EditProductUIController;
import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import memberfxui.Cart;
import memberfxui.CartUIController;
import memberfxui.EditMemberUIController;
import memberfxui.IndexUIController;


/**
 *
 * @author pc
 */
public class Navigator {

    public static final String ADMIN_FXML = "/fxui/AdminUI.fxml";
    public static final String PRODUCT_FXML = "/fxui/ProductUI.fxml";
    public static final String EDIT_PRODUCT_FXML = "/fxui/EditProductUI.fxml";
    public static final String CATEGORY_FXML = "/fxui/CategoryUI.fxml";
    public static final String MEMBER_FXML = "/fxui/MemberUI.fxml";
    public static final String ORDER_FXML = "/fxui/OrderUI.fxml";
    public static final String REGISTER_MEMBER_FXML = "/memberfxui/RegisterMemberUI.fxml";
    public static final String LOGIN_FXML = "/fxui/LoginUI.fxml";
    public static final String INDEX_FXML = "/memberfxui/IndexUI.fxml";
    public static final String EDIT_MEMBER_FXML = "/memberfxui/EditMemberUI.fxml";
    public static final String CART_FXML = "/memberfxui/CartUI.fxml";

    private FXMLLoader loader;
    private Stage stage = null;

    private static Navigator nav = null;

    private Navigator() {
    }

    public static Navigator getInstance() {
        if (nav == null) {
            nav = new Navigator();
        }
        return nav;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return this.stage;
    }

    private void goTo(String fxml) throws IOException {
        this.loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxml));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        this.stage.setScene(scene);
        this.stage.centerOnScreen();
    }

    public void goToAdmin() throws IOException {
        this.goTo(ADMIN_FXML);
    }

    public void goToProduct() throws IOException {
        this.goTo(PRODUCT_FXML);
    }

    public void goToCategory() throws IOException {
        this.goTo(CATEGORY_FXML);
    }

    public void goToMember() throws IOException {
        this.goTo(MEMBER_FXML);
    }

    public void goToEdit(Product editProduct) throws IOException {
        this.goTo(EDIT_PRODUCT_FXML);
        EditProductUIController ctrl = loader.getController();
        ctrl.initialize(editProduct);
    }

    public void goToMemberRegister() throws IOException {
        this.goTo(REGISTER_MEMBER_FXML);
    }

    public void goToLogin() throws IOException {
        this.goTo(LOGIN_FXML);
    }

    public void goToOrder() throws IOException {
        this.goTo(ORDER_FXML);
    }

    public void goToIndex(String username) throws IOException {
        this.goTo(INDEX_FXML);
        IndexUIController ctrl = loader.getController();
        ctrl.initialize(username);
    }
    
     public void goToIndex(String username,ObservableList<Cart> cart) throws IOException {
        this.goTo(INDEX_FXML);
        IndexUIController ctrl = loader.getController();
        ctrl.initialize(username);
        ctrl.initialize(cart);
    }
    
    public void goToMemEdit(String username) throws IOException, SQLException{
        this.goTo(EDIT_MEMBER_FXML);
        EditMemberUIController ctrl = loader.getController();
        ctrl.initialize(username);
    }
    
    public void goToCart(ObservableList<Cart> cart, String username) throws IOException{
        this.goTo(CART_FXML);
        CartUIController ctrl = loader.getController();
        ctrl.initialize(cart, username);
    }
}
