/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memberfxui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author pc
 */
public class Cart {
    private ObjectProperty<Integer> productID;
    private ObjectProperty<Integer> amount;
    
    public Cart(){
        this.productID = new SimpleObjectProperty<>();
        this.amount = new SimpleObjectProperty<>();
    }

    
    public int getProductID() {
        return productID.get();
    }


    public int getAmount() {
        return amount.get();
    }
    
    public ObjectProperty<Integer> getProductIDProperty() {
        return productID;
    }
    
    public ObjectProperty<Integer> getAmountProperty() {
        return amount;
    }

    public void setProductID(int productID) {
        this.productID.set(productID); 
    }

    public void setAmount(int amount) {
        this.amount.set(amount);
    }
    
    public void setProductID(ObjectProperty<Integer> productID) {
        this.productID = productID;
    }

    public void setAmount(ObjectProperty<Integer> amount) {
        this.amount = amount;
    }
}
