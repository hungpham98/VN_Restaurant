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

/**
 *
 * @author pc
 */
public class Order extends RecursiveTreeObject<Order> {

    private ObjectProperty<Integer> orderID;
    private ObjectProperty<Integer> memberID;
    private StringProperty memberName;
    private StringProperty date;
    private ObjectProperty<Integer> total_price;
    private StringProperty memberPhone;
    private StringProperty status;

    public Order() {
        orderID = new SimpleObjectProperty<>();
        memberID = new SimpleObjectProperty<>();
        memberName = new SimpleStringProperty();
        date = new SimpleStringProperty();
        total_price = new SimpleObjectProperty<>();
        memberPhone = new SimpleStringProperty();
        status = new SimpleStringProperty();
    }

    public int getOrderID() {
        return this.orderID.get();
    }

    public int getMemberID() {
        return this.memberID.get();
    }

    public String getMemberName() {
        return this.memberName.get();
    }

    public String getMemberPhone() {
        return this.memberPhone.get();
    }

    public String getDate() {
        return this.date.get();
    }

    public int get_total_price() {
        return this.total_price.get();
    }

    public String getStatus() {
        return this.status.get();
    }

    public ObjectProperty<Integer> getOrderIDProperty() {
        return orderID;
    }

    public ObjectProperty<Integer> getMemberIDProperty() {
        return memberID;
    }

    public StringProperty getMemberNameProperty() {
        return memberName;
    }

    public StringProperty getMemberPhoneProperty() {
        return memberPhone;
    }

    public StringProperty getDateProperty() {
        return date;
    }

    public ObjectProperty<Integer> get_total_priceProperty() {
        return total_price;
    }

    public StringProperty getStatusProperty() {
        return status;
    }

    public void setOrderID(int orderID) {
        this.orderID.set(orderID);
    }

    public void setMemberID(int memberID) {
        this.memberID.set(memberID);
    }

    public void setMemberName(String memberName) {
        this.memberName.set(memberName);
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone.set(memberPhone);
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public void settotal_price(int total_price) {
        this.total_price.set(total_price);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public void setOrderIDProperty(ObjectProperty<Integer> orderID) {
        this.orderID = orderID;
    }

    public void setMemberIDProperty(ObjectProperty<Integer> memberID) {
        this.memberID = memberID;
    }

    public void setMemberNameProperty(StringProperty memberName) {
        this.memberName = memberName;
    }

    public void setMemberPhoneProperty(StringProperty memberPhone) {
        this.memberPhone = memberPhone;
    }

    public void setDateProperty(StringProperty date) {
        this.date = date;
    }

    public void settotal_priceProperty(ObjectProperty<Integer> total_price) {
        this.total_price = total_price;
    }

    public void setStatusProperty(StringProperty status) {
        this.status = status;
    }

    public static ObservableList<Order> selectAll() {
        ObservableList<Order> order = FXCollections.observableArrayList();

        try (
                Connection conn = DbService.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT o.*,m.name FROM `order` AS o JOIN member AS m ON m.id = o.memberID");) {
            while (rs.next()) {
                Order o = new Order();
                o.setOrderID(rs.getInt("orderID"));
                o.setMemberID(rs.getInt("memberID"));
                o.setMemberName(rs.getString("name"));
                o.setDate(rs.getString("date"));
                order.add(o);
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return order;
    }

    public static ObservableList<Order> selectAllincludePhone() {
        ObservableList<Order> order = FXCollections.observableArrayList();

        try (
                Connection conn = DbService.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT o.*,m.name,m.phone FROM `order` AS o JOIN member AS m ON m.id = o.memberID ");) {
            while (rs.next()) {
                Order o = new Order();
                o.setOrderID(rs.getInt("orderID"));
                o.setMemberID(rs.getInt("memberID"));
                o.setMemberName(rs.getString("name"));
                o.setDate(rs.getString("date"));
                o.setMemberPhone(rs.getString("phone"));
                o.settotal_price(OrderDetail.selecttotal_pricebyorderID(rs.getInt("orderID")));
                switch (rs.getInt("status")) {
                    case 1:
                        o.setStatus("Pending");
                        break;
                    case 2:
                        o.setStatus("Approved");
                        break;
                    case 3:
                        o.setStatus("Cancelled");
                        break;
                }
                order.add(o);
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return order;
    }

    public static Order insert(Order newOrder) throws SQLException {
        String sql = "INSERT INTO `order`(`memberID`,`status`, `date`) "
                + "VALUES (?,?,?)";
        ResultSet key = null;
        try (
                Connection conn = DbService.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setInt(1, newOrder.getMemberID());
            stmt.setInt(2, 1);
            stmt.setString(3, newOrder.getDate());

            int rowInserted = stmt.executeUpdate();

            if (rowInserted == 1) {
                key = stmt.getGeneratedKeys();
                key.next();
                int newKey = key.getInt(1);
                newOrder.setOrderID(newKey);
                return newOrder;
            } else {
                System.out.println("No product inserted");
                return null;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        } finally {
            if (key != null) {
                key.close();
            }
        }
    }

    public static boolean delete(Order deleteOrder) {
        String sql = "DELETE FROM `order` WHERE orderID = ?";
        try (
                Connection conn = DbService.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setInt(1, deleteOrder.getOrderID());

            int rowDeleted = stmt.executeUpdate();

            if (rowDeleted == 1) {
                return true;
            } else {
                System.err.println("No Order deleted");
                return false;
            }
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

    public static ObservableList<Order> totalpricebyorderID(int orderID) {
        ObservableList<Order> order = FXCollections.observableArrayList();

        try (
                Connection conn = DbService.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT SUM(p.price * o.amount)  FROM order_detail AS o JOIN product AS p ON p.productID = o.productID WHERE o.orderID =  " + orderID + " ");) {
            while (rs.next()) {
                Order o = new Order();
                o.settotal_price(rs.getInt("total_price"));
                order.add(o);
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return order;
    }

    public static void approveOrderbyID(int orderID) {
        try (
                Connection conn = DbService.getConnection();
                PreparedStatement stmt = conn.prepareStatement("UPDATE `order` set `status` = 2 WHERE orderID = ?", Statement.RETURN_GENERATED_KEYS);) {
            stmt.setInt(1, orderID);
            int rowUpdated = stmt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void cancelOrderbyID(int orderID) {
        try (
                Connection conn = DbService.getConnection();
                PreparedStatement stmt = conn.prepareStatement("UPDATE `order` set `status` = 3 WHERE orderID = ?");) {
            stmt.setInt(1, orderID);
            int rowUpdated = stmt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
