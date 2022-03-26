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
import dao.Order;
import dao.OrderDetail;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
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
public class OrderUIController implements Initializable {

    @FXML
    private BorderPane borderPane;
    @FXML
    private JFXButton btnAdmin;
    @FXML
    private JFXButton btnProduct;
    @FXML
    private JFXButton btnCategory;
    @FXML
    private JFXButton btnMember;
    @FXML
    private JFXButton btnLogout;
    @FXML
    private Label total_price_order;

    @FXML
    private JFXTreeTableView<Order> tvOrder;

    @FXML
    private JFXTreeTableView<OrderDetail> tvOrder_detail;

    @FXML
    private JFXButton deleteorderbtn;
    
    @FXML
    private JFXButton cancelorder;

    @FXML
    private JFXButton approveorder;

    @FXML
    private void btnAdminClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToAdmin();
    }

    @FXML
    void dlorderclick(ActionEvent event) {
        if (tvOrder.getSelectionModel().getSelectedItem() == null) {
            selectOrderWarning();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Deleting a order");
            alert.setHeaderText("Are you sure you want to delete this selected order?");
            Optional<ButtonType> comfirmationResponse = alert.showAndWait();

            if (comfirmationResponse.get() == ButtonType.OK) {
                Order deleteOrder = tvOrder.getSelectionModel().getSelectedItem().getValue();
                if (OrderDetail.delete(deleteOrder) || Order.delete(deleteOrder)) {
                    tvOrder_detail.getColumns().clear();
                    content.remove(deleteOrder);

                    System.out.println("An order is deleted");
                } else {
                    System.err.println("No order is deleted");
                }
            }
        }
    }

    @FXML
    private void btnProductClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToProduct();
    }
    
    @FXML
    void cancelclick(ActionEvent event) throws IOException {
        Order.cancelOrderbyID(tvOrder.getSelectionModel().getSelectedItem().getValue().getOrderID());
        Navigator.getInstance().goToOrder();
        tvOrder.getColumns().clear();
        buildOrderTable();
    }
    
    @FXML
    void approveclck(ActionEvent event) throws IOException {
        Order.approveOrderbyID(tvOrder.getSelectionModel().getSelectedItem().getValue().getOrderID());
        Navigator.getInstance().goToOrder();
        tvOrder.getColumns().clear();
        buildOrderTable();
    }

    @FXML
    private void btnCategoryClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToCategory();
    }

    @FXML
    private void btnMemberClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToMember();
    }

    @FXML
    private void btnLogoutClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToLogin();
    }

    ObservableList<Order> content;
    ObservableList<OrderDetail> content_detail;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buildOrderTable();
        tvOrder.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                buildOrderDetail(newSelection.getValue().getOrderID());
            }
        });
    }

    public void buildOrderTable() {
        JFXTreeTableColumn<Order, Integer> orderID = new JFXTreeTableColumn<>("OrderID:");
        orderID.setCellValueFactory((TreeTableColumn.CellDataFeatures<Order, Integer> param) -> param.getValue().getValue().getOrderIDProperty());

        JFXTreeTableColumn<Order, String> memberName = new JFXTreeTableColumn<>("Member name:");
        memberName.setCellValueFactory((TreeTableColumn.CellDataFeatures<Order, String> param) -> param.getValue().getValue().getMemberNameProperty());

        JFXTreeTableColumn<Order, String> date = new JFXTreeTableColumn<>("Order date");
        date.setCellValueFactory((TreeTableColumn.CellDataFeatures<Order, String> param) -> param.getValue().getValue().getDateProperty());

        JFXTreeTableColumn<Order, String> phone = new JFXTreeTableColumn<>("Phone Number");
        phone.setCellValueFactory((TreeTableColumn.CellDataFeatures<Order, String> param) -> param.getValue().getValue().getMemberPhoneProperty());
        
        JFXTreeTableColumn<Order, Integer> total_price = new JFXTreeTableColumn<>("Total Price");
        total_price.setCellValueFactory((TreeTableColumn.CellDataFeatures<Order, Integer> param) -> param.getValue().getValue().get_total_priceProperty());
        
        JFXTreeTableColumn<Order, String> status = new JFXTreeTableColumn<>("Status");
        status.setCellValueFactory((TreeTableColumn.CellDataFeatures<Order, String> param) -> param.getValue().getValue().getStatusProperty());
        tvOrder.getColumns().addAll(orderID, memberName, phone,date, total_price, status);

        content = Order.selectAllincludePhone();

        TreeItem<Order> root = new RecursiveTreeItem<>(content, RecursiveTreeObject::getChildren);
        tvOrder.setRoot(root);
        tvOrder.setShowRoot(false);
    }

    public void buildOrderDetail(int ID) {
        JFXTreeTableColumn<OrderDetail, String> productname = new JFXTreeTableColumn<>("Name:");
        productname.setCellValueFactory((TreeTableColumn.CellDataFeatures<OrderDetail, String> param) -> param.getValue().getValue().getProductNameProperty());

        JFXTreeTableColumn<OrderDetail, Integer> amount = new JFXTreeTableColumn<>("Amount");
        amount.setCellValueFactory((TreeTableColumn.CellDataFeatures<OrderDetail, Integer> param) -> param.getValue().getValue().getAmountProperty());

        tvOrder_detail.getColumns().clear();
        tvOrder_detail.getColumns().addAll(productname, amount);

        content_detail = OrderDetail.selectbyorderID(ID);

        TreeItem<OrderDetail> root = new RecursiveTreeItem<>(content_detail, RecursiveTreeObject::getChildren);

        tvOrder_detail.setRoot(root);
        tvOrder_detail.setShowRoot(false);
    }

    private void selectOrderWarning() {
        String alertTitle = "Please select a order";
        String alertText = "A order must be selected for the operation";
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(alertTitle);
        alert.setHeaderText(alertText);
        alert.showAndWait();
    }
}
