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
public class Admin extends RecursiveTreeObject<Admin> {

    private ObjectProperty<Integer> id;
    private StringProperty username;
    private StringProperty password;
    private StringProperty name;
    private StringProperty email;
    private StringProperty birthday;

    public Admin() {
        id = new SimpleObjectProperty<>();
        username = new SimpleStringProperty();
        password = new SimpleStringProperty();
        name = new SimpleStringProperty();
        email = new SimpleStringProperty();
        birthday = new SimpleStringProperty();
    }

    public int getId() {
        return id.get();
    }

    public String getUsername() {
        return username.get();
    }

    public String getPassword() {
        return password.get();
    }

    public String getName() {
        return name.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getBirthday() {
        return birthday.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public void setPasword(String password) {
        this.password.set(password);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setBirthday(String birthday) {
        this.birthday.set(birthday);
    }

    public ObjectProperty<Integer> getIdProperty() {
        return id;
    }

    public StringProperty getUsernameProperty() {
        return username;
    }

    public StringProperty getPasswordProperty() {
        return password;
    }

    public StringProperty getNameProperty() {
        return name;
    }

    public StringProperty getEmailProperty() {
        return email;
    }

    public StringProperty getBirthdayProperty() {
        return birthday;
    }

    public void setIdProperty(ObjectProperty<Integer> id) {
        this.id = id;
    }

    public void setUsernameProperty(StringProperty username) {
        this.username = username;
    }

    public void setPasswordProperty(StringProperty password) {
        this.password = password;
    }

    public void setNameProperty(StringProperty name) {
        this.name = name;
    }

    public void setEmailProperty(StringProperty email) {
        this.email = email;
    }

    public void setBirthdayProperty(StringProperty birthday) {
        this.birthday = birthday;
    }

    public boolean checkLogin(String username, String password) throws SQLException {
        String sql = "SELECT * FROM admin where username = ? and password = MD5(?)";
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

    public static ObservableList<Admin> selectAll() {
        ObservableList<Admin> admin = FXCollections.observableArrayList();

        try (
                Connection conn = DbService.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM admin;");) {
            while (rs.next()) {
                Admin a = new Admin();
                a.setId(rs.getInt("id"));
                a.setUsername(rs.getString("username"));
                a.setPasword(rs.getString("password"));
                a.setName(rs.getString("name"));
                a.setEmail(rs.getString("email"));
                a.setBirthday(rs.getString("birthday"));
                admin.add(a);
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return admin;
    }

    public static Admin insert(Admin newAdmin) throws SQLException {
        String sql = "INSERT INTO admin(username,password,name,email,birthday) "
                + "VALUES (?,MD5(?),?,?,?)";
        ResultSet key = null;
        try (
                Connection conn = DbService.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setString(1, newAdmin.getUsername());
            stmt.setString(2, newAdmin.getPassword());
            stmt.setString(3, newAdmin.getName());
            stmt.setString(4, newAdmin.getEmail());
            stmt.setString(5, newAdmin.getBirthday());

            int rowInserted = stmt.executeUpdate();

            if (rowInserted == 1) {
                key = stmt.getGeneratedKeys();
                key.next();
                int newKey = key.getInt(1);
                newAdmin.setId(newKey);
                return newAdmin;

            } else {
                System.out.println("No admin inserted");
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

    public static boolean update(Admin updateAdmin) {
        String sql = "UPDATE admin SET "
                + "username = ?, "
                + "password = MD5(?), "
                + "name = ?, "
                + "email = ?, "
                + "birthday = ? "
                + "WHERE id = ?";
        try (
                Connection conn = DbService.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            stmt.setString(1, updateAdmin.getUsername());
            stmt.setString(2, updateAdmin.getPassword());
            stmt.setString(3, updateAdmin.getName());
            stmt.setString(4, updateAdmin.getEmail());
            stmt.setString(5, updateAdmin.getBirthday());
            stmt.setInt(6, updateAdmin.getId());

            int rowUpdated = stmt.executeUpdate();

            if (rowUpdated == 1) {
                return true;
            } else {
                System.err.println("No admin updated");
                return false;
            }

        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

    public static boolean delete(Admin deleteAdmin) {
        String sql = "DELETE FROM admin WHERE id = ?";
        try (
                Connection conn = DbService.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setInt(1, deleteAdmin.getId());

            int rowDeleted = stmt.executeUpdate();

            if (rowDeleted == 1) {
                return true;
            } else {
                System.err.println("No Admin deleted");
                return false;
            }
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }
}
