/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import dao.Category;
import dao.Product;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import service.Navigator;

/**
 * FXML Controller class
 *
 * @author pc
 */
public class ProductUIController implements Initializable {

    @FXML
    private BorderPane borderPane;

    @FXML
    private JFXButton btnAdmin;

    @FXML
    private JFXButton btnMember;

    @FXML
    private JFXButton btnLogout;

    @FXML
    private JFXTreeTableView<Product> tvProduct;

    @FXML
    private JFXButton btnInsertProduct;

    @FXML
    private JFXButton btnUpdateProduct;

    @FXML
    private JFXButton btnDeleteProduct;
    
    @FXML
    private JFXButton btnCategory;
    
    @FXML
    private JFXButton btnOrder;

    @FXML
    private ImageView imgProduct;

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
    void btnCategoryClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToCategory();
    }

    @FXML
    private void btnOrderClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToOrder();
    }

    @FXML
    void btnDeleteProductClick(ActionEvent event) {
        if (tvProduct.getSelectionModel().getSelectedItem() == null) {
            selectProductWarning();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Deleting a product");
            alert.setHeaderText("Are you sure you want to delete this selected product?");
            Optional<ButtonType> comfirmationResponse = alert.showAndWait();

            if (comfirmationResponse.get() == ButtonType.OK) {
                Product deleteProduct = tvProduct.getSelectionModel().getSelectedItem().getValue();
                boolean result = Product.delete(deleteProduct);
                if (result) {
                    content.remove(deleteProduct);
                    System.out.println("A product is deleted");
                } else {
                    System.err.println("No product is deleted");
                }
            }
        }
    }

    @FXML
    void btnInsertProductClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToEdit(null);
    }

    @FXML
    void btnUpdateProductClick(ActionEvent event) throws IOException {

        if (tvProduct.getSelectionModel().getSelectedItem() == null) {
            selectProductWarning();
        } else {
            Product updateProduct = tvProduct.getSelectionModel().getSelectedItem().getValue();
            Navigator.getInstance().goToEdit(updateProduct);
        }

    }

//    @FXML
//    void viewImg(ActionEvent event) throws MalformedURLException {
//        if (tvProduct.getSelectionModel().getSelectedItem() == null) {
//            selectProductWarning();
//        } else {
//            
//            Product selectProduct = tvProduct.getSelectionModel().getSelectedItem().getValue();
//            AnchorPane anchor = new AnchorPane();
//            Class<?> clazz = this.getClass();
//            InputStream input = clazz.getResourceAsStream("/img/" + selectProduct.getImg());
//            ImageView img = new ImageView(new Image(input));
//           
//            anchor.getChildren().add(img);
//           
//            Scene secondScene = new Scene(anchor,1180,680);
//
//            // New window (Stage)
//            Stage newWindow = new Stage();
//            img.fitWidthProperty().bind(newWindow.widthProperty()); 
//            img.setPreserveRatio(true);
//            newWindow.setTitle("Image");
//            newWindow.setScene(secondScene);
//        
//            newWindow.show();
//        }
//    }
    ObservableList<Product> content;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        JFXTreeTableColumn<Product, String> name = new JFXTreeTableColumn<>("Product Name");
        name.setCellValueFactory((TreeTableColumn.CellDataFeatures<Product, String> param) -> param.getValue().getValue().getNameProperty());

        JFXTreeTableColumn<Product, Integer> price = new JFXTreeTableColumn<>("Price");
        price.setCellValueFactory((TreeTableColumn.CellDataFeatures<Product, Integer> param) -> param.getValue().getValue().getPriceProperty());

        JFXTreeTableColumn<Product, String> ingredient = new JFXTreeTableColumn<>("Ingredient");
        ingredient.setCellValueFactory((TreeTableColumn.CellDataFeatures<Product, String> param) -> param.getValue().getValue().getIngreProperty());

        JFXTreeTableColumn<Product, Integer> amount = new JFXTreeTableColumn<>("Serving");
        amount.setCellValueFactory((TreeTableColumn.CellDataFeatures<Product, Integer> param) -> param.getValue().getValue().getAmountProperty());

        JFXTreeTableColumn<Product, String> img = new JFXTreeTableColumn<>("Img");
        img.setCellValueFactory((TreeTableColumn.CellDataFeatures<Product, String> param) -> param.getValue().getValue().getImgProperty());

        JFXTreeTableColumn<Product, Category> category = new JFXTreeTableColumn<>("Category");
        category.setCellValueFactory((TreeTableColumn.CellDataFeatures<Product, Category> param) -> param.getValue().getValue().getCategoryProperty());

        tvProduct.getColumns().addAll(name, price, ingredient, amount, img, category);

        content = Product.selectAll();

        TreeItem<Product> root = new RecursiveTreeItem<>(content, RecursiveTreeObject::getChildren);
        tvProduct.setRoot(root);
        tvProduct.setShowRoot(false);
        //select img 
        tvProduct.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends TreeItem<Product>> observable, TreeItem<Product> oldValue, TreeItem<Product> newValue) -> {
            Class<?> clazz = this.getClass();
            InputStream input = clazz.getResourceAsStream("/img/" + tvProduct.getSelectionModel().getSelectedItem().getValue().getImg());
            imgProduct.setImage(new Image(input)); ;
        });
    }

    private void selectProductWarning() {
        String alertTitle = "Please select a product";
        String alertText = "A product must be selected for the operation";
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(alertTitle);
        alert.setHeaderText(alertText);
        alert.showAndWait();
    }

}
