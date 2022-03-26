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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Hung
 */
public class Product extends RecursiveTreeObject<Product> {

    public ObjectProperty<Integer> productID;
    public StringProperty name;
    public ObjectProperty<Integer> price;
    public StringProperty ingre;
    public ObjectProperty<Integer> amount;
    public StringProperty img;
    public ObjectProperty<Category> category;

    public Product() {
        productID = new SimpleObjectProperty<>(null);
        name = new SimpleStringProperty();
        price = new SimpleObjectProperty<>(0);
        ingre = new SimpleStringProperty();
        amount = new SimpleObjectProperty<>(0);
        img = new SimpleStringProperty();
        category = new SimpleObjectProperty<>();
    }

    public Integer getProductID() {
        return productID.get();
    }

    public String getName() {
        return name.get();
    }

    public Integer getPrice() {
        return price.get();
    }

    public String getIngre() {
        return ingre.get();
    }

    public Integer getAmount() {
        return amount.get();
    }

    public String getImg() {
        return img.get();
    }

    public Category getCategory() {
        return category.get();
    }

    public ObjectProperty<Integer> getProductIDProperty() {
        return productID;
    }

    public StringProperty getNameProperty() {
        return name;
    }

    public ObjectProperty<Integer> getPriceProperty() {
        return price;
    }

    public StringProperty getIngreProperty() {
        return ingre;
    }

    public ObjectProperty<Integer> getAmountProperty() {
        return amount;
    }

    public StringProperty getImgProperty() {
        return img;
    }

    public ObjectProperty<Category> getCategoryProperty() {
        return category;
    }

    public void setProductID(int productID) {
        this.productID.set(productID);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setPrice(Integer price) {
        this.price.set(price);
    }

    public void setIngre(String ingre) {
        this.ingre.set(ingre);
    }

    public void setAmount(Integer amount) {
        this.amount.set(amount);
    }

    public void setImg(String img) {
        this.img.set(img);
    }

    public void setCategory(Category category) {
        this.category.set(category);
    }

    public void setIdProperty(ObjectProperty<Integer> productID) {
        this.productID = productID;
    }

    public void setNameProperty(StringProperty name) {
        this.name = name;
    }

    public void setPriceProperty(ObjectProperty<Integer> price) {
        this.price = price;
    }

    public void setIngreProperty(StringProperty ingre) {
        this.ingre = ingre;
    }

    public void setAmountProperty(ObjectProperty<Integer> amount) {
        this.amount = amount;
    }

    public void setImgProperty(StringProperty img) {
        this.img = img;
    }

    public void setCategoryProperty(ObjectProperty<Category> category) {
        this.category = category;
    }

    public static ObservableList<Product> selectAll() {
        ObservableList<Product> product = FXCollections.observableArrayList();

        try (
                Connection conn = DbService.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT p.*, c.categoryname FROM product AS p JOIN category AS c ON  p.categoryID = c.categoryID;");) {
            while (rs.next()) {
                Product p = new Product();
                Category c = new Category();
                c.setCategoryID(rs.getInt("categoryID"));
                c.setCategoryName(rs.getString("categoryname"));
                p.setProductID(rs.getInt("productID"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getInt("price"));
                p.setIngre(rs.getString("ingredients"));
                p.setAmount(rs.getInt("amount"));
                p.setImg(rs.getString("img"));
                p.setCategory(c);
                product.add(p);
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return product;
    }

    public static Product insert(Product newProduct) throws SQLException {
        String sql = "INSERT INTO product(name,price,ingredients,amount,img,categoryID) "
                + "VALUES (?,?,?,?,?,?)";
        ResultSet key = null;
        try (
                Connection conn = DbService.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setString(1, newProduct.getName());
            stmt.setInt(2, newProduct.getPrice());
            stmt.setString(3, newProduct.getIngre());
            stmt.setInt(4, newProduct.getAmount());
            stmt.setString(5, newProduct.getImg());
            stmt.setInt(6, newProduct.getCategory().getCategoryID());

            int rowInserted = stmt.executeUpdate();

            if (rowInserted == 1) {
                key = stmt.getGeneratedKeys();
                key.next();
                int newKey = key.getInt(1);
                newProduct.setProductID(newKey);
                return newProduct;
            } else {
                System.out.println("No product inserted");
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

    public static boolean update(Product updateProduct) {
        String sql = "UPDATE product SET "
                + "name = ?, "
                + "price = ?, "
                + "ingredients = ?, "
                + "amount = ?, "
                + "img = ?, "
                + "categoryID = ? "
                + "WHERE productID = ?";
        try (
                Connection conn = DbService.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            stmt.setString(1, updateProduct.getName());
            stmt.setInt(2, updateProduct.getPrice());
            stmt.setString(3, updateProduct.getIngre());
            stmt.setInt(4, updateProduct.getAmount());
            stmt.setString(5, updateProduct.getImg());
            stmt.setInt(6, updateProduct.getCategory().getCategoryID());
            stmt.setInt(7, updateProduct.getProductID());

            int rowUpdated = stmt.executeUpdate();

            if (rowUpdated == 1) {
                return true;
            } else {
                System.err.println("No product updated");
                return false;
            }

        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

    public static boolean delete(Product deleteProduct) {
        String sql = "DELETE FROM product WHERE productID = ?";
        try (
                Connection conn = DbService.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setInt(1, deleteProduct.getProductID());

            int rowDeleted = stmt.executeUpdate();

            if (rowDeleted == 1) {
                return true;
            } else {
                System.err.println("No product deleted");
                return false;
            }
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }
    public static ObservableList<Product> searchProductByname(String name){
         ObservableList<Product> product = FXCollections.observableArrayList();

        try (
                Connection conn = DbService.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM product WHERE name LIKE '" + "%" + name + "%" +"' ;");) {
            while (rs.next()) {
                Product p = new Product();
                p.setProductID(rs.getInt("productID"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getInt("price"));
                p.setIngre(rs.getString("ingredients"));
                p.setAmount(rs.getInt("amount"));
                p.setImg(rs.getString("img"));
                product.add(p);
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return product;
    }
    public static ObservableList<Product> selectProductByCategory(int categoryID){
        ObservableList<Product> product = FXCollections.observableArrayList();

        try (
                Connection conn = DbService.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT p.*, c.categoryname FROM product AS p JOIN category AS c ON  p.categoryID = c.categoryID WHERE p.categoryID = " + categoryID + " ;");) {
            while (rs.next()) {
                Product p = new Product();
                Category c = new Category();
                c.setCategoryID(rs.getInt("categoryID"));
                c.setCategoryName(rs.getString("categoryname"));
                p.setProductID(rs.getInt("productID"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getInt("price"));
                p.setIngre(rs.getString("ingredients"));
                p.setAmount(rs.getInt("amount"));
                p.setImg(rs.getString("img"));
                p.setCategory(c);
                product.add(p);
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return product;
    } 
    public static Product selectProductByID(int ProductID){
        Product product = new Product();

        try (
                Connection conn = DbService.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM product WHERE productID = " + ProductID + " ;");) {
            while (rs.next()) {
                product.setProductID(rs.getInt("productID"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getInt("price"));
                product.setIngre(rs.getString("ingredients"));
                product.setAmount(rs.getInt("amount"));
                product.setImg(rs.getString("img"));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return product;
    } 
}
