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
public class Category extends RecursiveTreeObject<Category> {

    private ObjectProperty<Integer> categoryID;
    private StringProperty categoryname;

    public Category() {
        categoryID = new SimpleObjectProperty<>(null);
        categoryname = new SimpleStringProperty();
    }

    public int getCategoryID() {
        return categoryID.get();
    }

    public String getCategoryName() {
        return categoryname.get();
    }

    public void setCategoryID(int categoryID) {
        this.categoryID.set(categoryID);
    }

    public void setCategoryName(String categoryname) {
        this.categoryname.set(categoryname);
    }

    public ObjectProperty<Integer> getCategoryIDProperty() {
        return categoryID;
    }

    public StringProperty getCategoryNameProperty() {
        return categoryname;
    }

    public void setCategoryIDProperty(ObjectProperty<Integer> categoryID) {
        this.categoryID = categoryID;
    }

    public void setCategoryNameProperty(StringProperty name) {
        this.categoryname = name;
    }

    public static ObservableList<Category> selectAll() {
        ObservableList<Category> category = FXCollections.observableArrayList();

        try (
                Connection conn = DbService.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM category;");) {
            while (rs.next()) {
                Category c = new Category();
                c.setCategoryID(rs.getInt("categoryID"));
                c.setCategoryName(rs.getString("categoryname"));
                category.add(c);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return category;
    }

    public static Category insert(Category newCategory) throws SQLException {
        String sql = "INSERT INTO category(categoryname) "
                + "VALUES (?)";
        ResultSet key = null;
        try (
                Connection conn = DbService.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setString(1, newCategory.getCategoryName());

            int rowInserted = stmt.executeUpdate();

            if (rowInserted == 1) {
                key = stmt.getGeneratedKeys();
                key.next();
                int newKey = key.getInt(1);
                newCategory.setCategoryID(newKey);
                return newCategory;

            } else {
                System.out.println("No category inserted");
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

    public static boolean update(Category updateCategory) {
        String sql = "UPDATE category SET "
                + "categoryname = ? "
                + "WHERE categoryID = ?";
        try (
                Connection conn = DbService.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            stmt.setString(1, updateCategory.getCategoryName());

            stmt.setInt(2, updateCategory.getCategoryID());

            int rowUpdated = stmt.executeUpdate();

            if (rowUpdated == 1) {
                return true;
            } else {
                System.err.println("No category updated");
                return false;
            }

        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }
    
    @Override
    public String toString(){
        return getCategoryName();
    }
    public static boolean delete(Category deleteCategory) {
        String sql = "DELETE FROM category WHERE categoryID = ?";
        try (
                Connection conn = DbService.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) { 
            stmt.setInt(1, deleteCategory.getCategoryID());

            int rowDeleted = stmt.executeUpdate();

            if (rowDeleted == 1) {
                return true;
            } else {
                System.err.println("No Category deleted");
                return false;
            }
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }
}
