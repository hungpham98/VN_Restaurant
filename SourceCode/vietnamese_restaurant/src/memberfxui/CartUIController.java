/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memberfxui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import dao.Member;
import dao.Order;
import dao.OrderDetail;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import service.Navigator;

/**
 * FXML Controller class
 *
 * @author pc
 */
public class CartUIController implements Initializable {

    @FXML
    private Label lbAccount;
    @FXML
    private ScrollPane scCartItems;
    @FXML
    private GridPane gd;
    @FXML
    private JFXCheckBox cbAll;
    @FXML
    private Label lbTotalPrice;
    @FXML
    private JFXButton btnBack;
    @FXML
    private AnchorPane anchor;
    @FXML
    private JFXButton btnSubmit;
    @FXML
    private VBox acVbox;
    @FXML
    private JFXButton editacBtn;
    @FXML
    private JFXButton logoutacBtn;
    @FXML
    private AnchorPane acEmpty;
    
    @FXML
    private void editacBtnClick(ActionEvent event) throws IOException, SQLException {
        Navigator.getInstance().goToMemEdit(username);
    }

    @FXML
    private void logoutacBtnClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToLogin();
    }

    /**
     * Initializes the controller class.
     */
    ObservableList<Cart> cart = FXCollections.observableArrayList();
    String username;
    ObjectProperty<Integer> total_price = new SimpleObjectProperty<>();
    BooleanProperty isChecked = new SimpleBooleanProperty();
    ObservableList<Cart> comfirmCart = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        total_price.set(0);
        total_price.addListener((observable, oldValue, newValue) -> {
            lbTotalPrice.setText(newValue.toString() + "đ");
        });
        cbAll.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                for (Node node : gd.getChildren()) {

                }
            } else {
                for (Node node : gd.getChildren()) {
                    if (node instanceof CheckBox) {
                        ((CheckBox) node).setSelected(false);
                    }
                }
            }
        });
        lbAccount.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (acVbox.isVisible()) {
                acVbox.setVisible(false);
                acVbox.setMouseTransparent(true);
            } else {
                acVbox.setVisible(true);
                acVbox.setMouseTransparent(false);
            }
        });
        if(cart.isEmpty()){
            acEmpty.setVisible(true);
        }
        cart.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable c) {
                if(cart.isEmpty()){
                    acEmpty.setVisible(true);
                }else acEmpty.setVisible(false);
            }
        });
        
    }

    public void initialize(ObservableList<Cart> cart, String username) {
        this.cart.addAll(cart);
        this.username = username;
        this.lbAccount.setText(username);
        buildCartList(this.cart);
    }

    @FXML
    private void btnSubmitClick(ActionEvent event) throws SQLException {
        if (!comfirmCart.isEmpty()) {
            addToOrder(comfirmCart);
            gd.getChildren().clear();
        }
    }

    @FXML
    private void btnBackClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToIndex(username, cart);
    }

    public void buildCartList(ObservableList<Cart> cart) {
        gd.getChildren().clear();

        int column = 0;
        int row = 0;

        try {
            for (int i = 0; i < cart.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("CartItems.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                CartItemsController cartItemsController = fxmlLoader.getController();
                cartItemsController.setData(cart.get(i));
                cartItemsController.cbProduct.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (newValue) {
                        cartItemsController.spAmount.valueProperty().addListener((observable1, oldValue1, newValue1) -> {
                            cartItemsController.cart.setAmount(newValue1);
                        });
                        cartItemsController.cart.getAmountProperty().addListener((observable2, oldValue2, newValue2) -> {
                            total_price.set(total_price.get() - (cartItemsController.price * oldValue2));
                            total_price.set(total_price.get() + (cartItemsController.price * newValue2));
                        });
                        total_price.set(total_price.get() + (cartItemsController.price * cartItemsController.cart.getAmount()));
                        comfirmCart.add(cartItemsController.cart);
                    } else {
                        total_price.set(total_price.get() - (cartItemsController.price * cartItemsController.cart.getAmount()));
                        comfirmCart.remove(cartItemsController.cart);
                    }
                });
                cartItemsController.btnDelete.setOnAction((ActionEvent event) -> {
                    this.cart.remove(cartItemsController.cart);
                    gd.getChildren().remove(anchorPane);
                    if (cartItemsController.cbProduct.selectedProperty().get()) {
                        total_price.set(total_price.get() - (cartItemsController.price * cartItemsController.cart.getAmount()));
                    }
                });
                row++;
                gd.add(anchorPane, column, row);
                gd.setMinWidth(Region.USE_COMPUTED_SIZE);
                gd.setMinHeight(Region.USE_COMPUTED_SIZE);
                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addToOrder(ObservableList<Cart> cart) throws SQLException {
        Member orderMember = Member.selectbyUsername(username);
        Order newOrder = new Order();
        newOrder.setMemberID(orderMember.getMemId());
        newOrder.setDate(LocalDate.now().toString());
        newOrder = Order.insert(newOrder);
        for (Cart c : cart) {
            OrderDetail.insert(newOrder, c);
        }
        successAlert();
    }

    public void successAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("SUCCESS");
        alert.setHeaderText("Your order have been sent!");
        ImageView icon = new ImageView("/img/5.png");
        icon.setFitHeight(48);
        icon.setFitWidth(48);
        alert.getDialogPane().setGraphic(icon);
        alert.show();
    }

    
}
