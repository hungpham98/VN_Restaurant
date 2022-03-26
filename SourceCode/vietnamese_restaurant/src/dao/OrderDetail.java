/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import dbconnection.DbService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import memberfxui.Cart;

/**
 *
 * @author pc
 */
public class OrderDetail extends RecursiveTreeObject<OrderDetail> {

    private ObjectProperty<Integer> orderID;
    private ObjectProperty<Integer> productID;
    private ObjectProperty<Integer> amount;
    private StringProperty product_name;

    public OrderDetail() {
        orderID = new SimpleObjectProperty<>();
        productID = new SimpleObjectProperty<>();
        amount = new SimpleObjectProperty<>();
        product_name = new SimpleStringProperty();
    }

    public int getOrderID() {
        return orderID.get();
    }

    public int getProductID() {
        return orderID.get();
    }

    public int getAmount() {
        return orderID.get();
    }

    public String getProductName() {
        return product_name.get();
    }

    public void setOrderID(int orderID) {
        this.orderID.set(orderID);
    }

    public void setProductID(int productID) {
        this.productID.set(productID);
    }

    public void setAmount(int amount) {
        this.amount.set(amount);
    }

    public void setProductName(String product_name) {
        this.product_name.set(product_name);
    }

    public ObjectProperty<Integer> getOrderIDProperty() {
        return orderID;
    }

    public ObjectProperty<Integer> getProductIDProperty() {
        return productID;
    }

    public ObjectProperty<Integer> getAmountProperty() {
        return amount;
    }

    public StringProperty getProductNameProperty() {
        return product_name;
    }

    public void setOrderIDProperty(ObjectProperty<Integer> orderID) {
        this.orderID = orderID;
    }

    public void setProductIDProperty(ObjectProperty<Integer> productID) {
        this.productID = productID;
    }

    public void setAmountProperty(ObjectProperty<Integer> amount) {
        this.amount = amount;
    }

    public void setProductName(StringProperty product_name) {
        this.product_name = product_name;
    }

    public static ObservableList<OrderDetail> selectbyorderID(int orderID) {
        ObservableList<OrderDetail> orderDetail = FXCollections.observableArrayList();

        try (
                Connection conn = DbService.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT od.*, p.name FROM `order_detail` as od join product as p on p.productID = od.productID where orderID =  " + orderID + " ");) {
            while (rs.next()) {
                OrderDetail od = new OrderDetail();
                od.setOrderID(rs.getInt("orderID"));
                od.setAmount(rs.getInt("amount"));
                od.setProductID(rs.getInt("productID"));
                od.setProductName(rs.getString("name"));
                orderDetail.add(od);
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return orderDetail;  
    }
    public static int selecttotal_pricebyorderID(int orderID) {
        int total_price = 0;
        try (
                Connection conn = DbService.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT SUM(p.price * od.amount) as `total_price`  FROM order_detail AS od JOIN product AS p ON p.productID = od.productID WHERE od.orderID =  " + orderID + " ");) {
            while (rs.next()) {
                total_price = rs.getInt("total_price");
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return total_price;
    }
    
    public static void insert(Order order, Cart cart) throws SQLException {
        String sql = "INSERT INTO order_detail(orderID,amount,productID) "
                + "VALUES (?,?,?)";
        try (
                Connection conn = DbService.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setInt(1, order.getOrderID());
            stmt.setInt(2, cart.getAmount());
            stmt.setInt(3, cart.getProductID());

            int rowInserted = stmt.executeUpdate();

            if (rowInserted == 1) {
                System.out.println("success" + cart.getProductID());
            } else {
                System.out.println("No product inserted" + cart.getProductID());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static boolean delete(Order deleteOrderDetail) {
        String sql = "DELETE FROM `order_detail` WHERE orderID = ?";
        try (
                Connection conn = DbService.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) { 
            stmt.setInt(1, deleteOrderDetail.getOrderID());


            int rowDeleted = stmt.executeUpdate();

            if (rowDeleted > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }
}
