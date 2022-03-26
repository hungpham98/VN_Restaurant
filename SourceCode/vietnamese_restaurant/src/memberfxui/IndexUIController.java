/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memberfxui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import com.jfoenix.validation.RequiredFieldValidator;
import dao.Product;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import main.MyListener;
import service.Navigator;

/**
 * FXML Controller class
 *
 * @author Hung
 */
public class IndexUIController implements Initializable {

    @FXML
    private AnchorPane anchorpane;
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private JFXDrawer drawer;
    @FXML
    private ScrollPane scroll;
    @FXML
    private GridPane grid;
    @FXML
    private Label lbAccount;
    @FXML
    private Label food2;
    @FXML
    private Label price2;
    @FXML
    private ImageView img2;
    @FXML
    private Text ingre2;
    @FXML
    private JFXButton btnFood;
    @FXML
    private JFXButton btnDrink;
    @FXML
    private VBox vbox;
    @FXML
    private JFXButton editacBtn;
    @FXML
    private JFXButton logoutacBtn;
    @FXML
    private VBox acVbox;
    @FXML
    private TextField txtAmount;
    @FXML
    private JFXButton btnAddToCart;
    @FXML
    private Text itemsCount;
    @FXML
    private StackPane noti;
    @FXML
    private JFXButton btnCart;
    @FXML
    private JFXTextField searchbar;
    @FXML
    private Label lbTitle;
    @FXML
    private HBox detail;
    @FXML
    private JFXButton minus;
    @FXML
    private JFXButton add;

    @FXML
    private void search(ActionEvent event) {
        product = Product.searchProductByname(searchbar.getText());
        buildProductList();
        lbTitle.setText("Result for: " + searchbar.getText());
        if (product.isEmpty()) {
            lbTitle.setText("No result found");
        }
    }

    @FXML
    private void editacBtnClick(ActionEvent event) throws IOException, SQLException {
        Navigator.getInstance().goToMemEdit(username);
    }

    @FXML
    private void logoutacBtnClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToLogin();
    }

    @FXML
    private void btnFoodClick(ActionEvent event) {
        product = Product.selectProductByCategory(1);
        buildProductList();
        lbTitle.setText("FOOD SECTION");
    }

    @FXML
    private void btnDrinkClick(ActionEvent event) {
        product = Product.selectProductByCategory(2);
        buildProductList();
        lbTitle.setText("DRINK SECTION");
    }

    @FXML
    private void btnCartClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToCart(cart, username);
    }

    /**
     * Initializes the controller class.
     */
    ObservableList<Product> product;
    private MyListener myListener;
    ObservableList<Cart> cart = FXCollections.observableArrayList();
    ObjectProperty<Integer> countItems = new SimpleObjectProperty<>();
    BooleanProperty visible = new SimpleBooleanProperty(false);
    private String username;

    public void initialize(ObservableList<Cart> cart) {
        this.cart.addAll(cart);
        itemsCount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (Integer.parseInt(itemsCount.getText()) > 0) {
                visible.set(true);
            } else {
                visible.set(false);
            }
        });   
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        drawer.setSidePane(vbox);
        HamburgerBackArrowBasicTransition burgerTask2 = new HamburgerBackArrowBasicTransition(hamburger);
        burgerTask2.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            burgerTask2.setRate(burgerTask2.getRate() * -1);
            burgerTask2.play();

            if (drawer.isOpened()) {
                drawer.close();
                drawer.setMouseTransparent(true);
            } else {
                drawer.open();
                drawer.setMouseTransparent(false);
            }
        });

        product = Product.selectProductByCategory(1);
        buildProductList();

        lbAccount.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (acVbox.isVisible()) {
                acVbox.setVisible(false);
                acVbox.setMouseTransparent(true);
            } else {
                acVbox.setVisible(true);
                acVbox.setMouseTransparent(false);
            }
        });
        try {
            itemsCount.textProperty().bind(new StringBinding() {
                {
                    bind(cart);
                }

                @Override
                protected String computeValue() {
                    return String.valueOf(cart.size());
                }
            });
            itemsCount.textProperty().addListener((observable, oldValue, newValue) -> {
                if (Integer.parseInt(itemsCount.getText()) > 0) {
                    visible.set(true);
                } else {
                    visible.set(false);
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        noti.visibleProperty().bind(visible);
        
        txtAmount.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtAmount.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        add.setOnAction((ActionEvent event) -> {
            int amount = Integer.parseInt(txtAmount.getText());
            amount++;
            txtAmount.setText(String.valueOf(amount));
        });
        minus.setOnAction((event) -> {
            int amount = Integer.parseInt(txtAmount.getText());
            amount--;
            txtAmount.setText(String.valueOf(amount));
        });
    }

    public void initialize(String username) {
        lbAccount.setText(username);
        this.username = username;
    }

    public void initialize(int productÌD) {
        addProductToCart(productÌD, 1);
    }

    public void buildProductList() {
        grid.getChildren().clear();
//        product = Product.selectProductByCategory(categoryID);
        if (product.size() > 0) {
            myListener = (Product product1) -> {
                setChosenProduct(product1);
            };
        }
        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < product.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("Item.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                ItemController itemController = (ItemController) fxmlLoader.getController();
                itemController.setData(product.get(i), myListener);
                itemController.btnAdd.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        addProductToCart(itemController.product.getProductID(), 1);
                        itemController.icon.setVisible(true);
                        FadeTransition fade = new FadeTransition();
                        fade.setDuration(Duration.millis(2000));
                        fade.setFromValue(10);
                        fade.setToValue(0.1);
                        fade.setNode(itemController.icon);
                        fade.setOnFinished((event2) -> {
                            itemController.icon.setVisible(false);
                        });
                        fade.play();
                    }
                });
                if (column == 3) {
                    column = 0;
                    row++;
                }
                grid.add(anchorPane, column++, row);
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                GridPane.setMargin(anchorPane, new Insets(20));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setChosenProduct(Product product) {
        detail.setVisible(true);
        food2.setText(product.getName());
        price2.setText(product.getPrice().toString() + "vnđ");
        Class<?> clazz = this.getClass();
        InputStream input = clazz.getResourceAsStream("/img/" + product.getImg());
        img2.setImage(new Image(input));
        if (product.getIngre() != null) {
            ingre2.setText("Ingredient:\n" + product.getIngre());
        } else {
            ingre2.setText(null);
        }
        btnAddToCart.setOnAction(((event) -> {
            addProductToCart(product.getProductID(), Integer.parseInt(txtAmount.getText()));
        }));
    }

    public void addProductToCart(int productID, int amount) {
        Cart insertCart = new Cart();
        insertCart.setProductID(productID);
        insertCart.setAmount(amount);
        Boolean bool = true;
        if (cart.isEmpty()) {
            cart.add(insertCart);
        } else {
            for (Cart c : cart) {
                if (productID == c.getProductID()) {
                    c.setAmount(c.getAmount() + amount);
                    bool = false;
                    break;
                }
            }
            if (bool) {
                this.cart.add(insertCart);
            }
        }
    }

    public boolean validateForm() {
        boolean val = true;

        RequiredFieldValidator validator = new RequiredFieldValidator();
        searchbar.getValidators().add(validator);
        searchbar.validate();
        searchbar.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                searchbar.validate();
            }

        });
        if (!searchbar.validate()) {
            val = false;
        }
        if (" ".equals(searchbar.getText())) {
            val = false;
        }
//        String x = searchbar.getText();
//        int stringLength = x.length();
//        for (int i = 0; i < stringLength; ++i) {
//            if("'".equals(x.charAt(i))){
//            val = false;
//        }
//        }
        return val;
    }

}
