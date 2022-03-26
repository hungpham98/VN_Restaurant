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
 * @author Hung
 */
public class Member extends RecursiveTreeObject<Member> {

    private ObjectProperty<Integer> id;
    private StringProperty name;
    private StringProperty address;
    private StringProperty phone;
    private StringProperty username;
    private StringProperty password;
    private StringProperty email;

    public Member() {
        id = new SimpleObjectProperty<>();
        name = new SimpleStringProperty();
        address = new SimpleStringProperty();
        phone = new SimpleStringProperty();
        username = new SimpleStringProperty();
        password = new SimpleStringProperty();
        email = new SimpleStringProperty();
    }

    public ObjectProperty<Integer> getIdProperty() {
        return id;
    }

    public StringProperty getNameProperty() {
        return name;
    }

    public StringProperty getAddressProperty() {
        return address;
    }

    public StringProperty getPhoneProperty() {
        return phone;
    }

    public StringProperty getUsernameProperty() {
        return username;
    }

    public StringProperty getPasswordProperty() {
        return password;
    }

    public StringProperty getEmailProperty() {
        return email;
    }

    public void setIdProperty(ObjectProperty<Integer> id) {
        this.id = id;
    }

    public void setNameProperty(StringProperty name) {
        this.name = name;
    }

    public void setAddressProperty(StringProperty address) {
        this.address = address;
    }

    public void setPhoneProperty(StringProperty phone) {
        this.phone = phone;
    }

    public void setUsernameProperty(StringProperty username) {
        this.username = username;
    }

    public void setPasswordProperty(StringProperty password) {
        this.password = password;
    }

    public void setEmailProperty(StringProperty email) {
        this.email = email;
    }

    public int getMemId() {
        return id.get();
    }

    public String getMemName() {
        return name.get();
    }

    public String getMemAddress() {
        return address.get();
    }

    public String getMemPhone() {
        return phone.get();
    }

    public String getMemUsername() {
        return username.get();
    }

    public String getMemPw() {
        return password.get();
    }

    public String getMemEmail() {
        return email.get();
    }

    public void setMemId(int id) {
        this.id.set(id);
    }

    public void setMemName(String name) {
        this.name.set(name);
    }

    public void setMemAddress(String address) {
        this.address.set(address);
    }

    public void setMemPhone(String phone) {
        this.phone.set(phone);
    }

    public void setMemUsername(String username) {
        this.username.set(username);
    }

    public void setMemPw(String password) {
        this.password.set(password);
    }
    public void setMemEmail(String email) {
        this.email.set(email);
    }
    public static ObservableList<Member> selectAll() {
        ObservableList<Member> member = FXCollections.observableArrayList();

        try (
                Connection conn = DbService.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM member");) {
            while (rs.next()) {
                Member m = new Member();
                m.setMemId(rs.getInt("id"));
                m.setMemName(rs.getString("name"));
                m.setMemAddress(rs.getString("address"));
                m.setMemPhone(rs.getString("phone"));
                m.setMemUsername(rs.getString("username"));
                m.setMemPw(rs.getString("password"));
                m.setMemEmail(rs.getString("email"));
                member.add(m);
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return member;
    }
    public boolean checkLogin(String username, String password) throws SQLException {
        String sql = "SELECT * FROM member where username = ? and password = ?";
        try (
                Connection conn = DbService.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return false;
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
    public static Member insert(Member newMember) throws SQLException {
        String sql = "INSERT INTO member(name,address,phone,username,password,email) "
                + "VALUES (?,?,?,?,?,?)";
        ResultSet key = null;
        try (
                Connection conn = DbService.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setString(1, newMember.getMemName());
            stmt.setString(2, newMember.getMemAddress());
            stmt.setString(3, newMember.getMemPhone());
            stmt.setString(4, newMember.getMemUsername());
            stmt.setString(5, newMember.getMemPw());
            stmt.setString(6, newMember.getMemEmail());

            int rowInserted = stmt.executeUpdate();

            if (rowInserted == 1) {
                key = stmt.getGeneratedKeys();
                key.next();
                int newKey = key.getInt(1);
                newMember.setMemId(newKey);
                return newMember;

            } else {
                System.out.println("No member inserted");
                return null;
            }

        } catch (Exception e) {
            System.out.println(e);
            return null;
        } finally {
            if (key != null) {
                key.close();
            }
        }
    }
    public static Member selectbyUsername(String username)throws SQLException {
        Member member = new Member();

        try (
                Connection conn = DbService.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM member WHERE username ='" + username + "' ;");) {
            while (rs.next()) {
                member.setMemId(rs.getInt("id"));
                member.setMemName(rs.getString("name"));
                member.setMemAddress(rs.getString("address"));
                member.setMemPhone(rs.getString("phone"));
                member.setMemUsername(rs.getString("username"));
                member.setMemPw(rs.getString("password"));
                member.setMemEmail(rs.getString("email"));

            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return member;
    }
        
    public static boolean update(Member updateMember) {
        String sql = "UPDATE member SET "
                + "name = ?, "
                + "address = ?, "
                + "phone = ?, "
                + "email = ? "
                + "WHERE id = ?";
        try (
                Connection conn = DbService.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            stmt.setString(1, updateMember.getMemName());
            stmt.setString(2, updateMember.getMemAddress());
            stmt.setString(3, updateMember.getMemPhone());
            stmt.setString(4, updateMember.getMemEmail());
            stmt.setInt(5, updateMember.getMemId());

            int rowUpdated = stmt.executeUpdate();

            if (rowUpdated == 1) {
                return true;
            } else {
                System.err.println("No member updated");
                return false;
            }

        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }
}
