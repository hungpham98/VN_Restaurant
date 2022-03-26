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
import dao.Member;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.BorderPane;
import service.Navigator;

/**
 * FXML Controller class
 *
 * @author pc
 */
public class MemberUIController implements Initializable {

  
    @FXML
    private BorderPane borderPane;

    @FXML
    private JFXButton btnAdmin;

    @FXML
    private JFXButton btnProduct;

    @FXML
    private JFXButton btnLogout;
    
    @FXML
    private JFXButton btnCategory;
    
    @FXML
    private JFXButton btnMember;

    @FXML
    private JFXTreeTableView<Member> tvMember;
    @FXML
    private JFXButton btnOrder;
    
    @FXML
    void btnCategoryClick(ActionEvent event)throws IOException {
        Navigator.getInstance().goToCategory();
    }
    
    @FXML
    void btnMemberClick(ActionEvent event)throws IOException {
        Navigator.getInstance().goToMember();
    }


    @FXML
    void btnAdminClick(ActionEvent event)throws IOException {
        Navigator.getInstance().goToAdmin();
    }


    @FXML
    void btnLogoutClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToLogin();
    }

    @FXML
    void btnProductClick(ActionEvent event)throws IOException {
        Navigator.getInstance().goToProduct();
    }
    
    @FXML
    private void btnOrderClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToOrder();
    }
    
    ObservableList<Member> content;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        JFXTreeTableColumn<Member, String> name = new JFXTreeTableColumn<>("Member name:");
        name.setPrefWidth(100);
        name.setCellValueFactory((TreeTableColumn.CellDataFeatures<Member, String> param) -> param.getValue().getValue().getNameProperty());
       
        JFXTreeTableColumn<Member, String> address = new JFXTreeTableColumn<>("Address:");
        address.setPrefWidth(100);
        address.setCellValueFactory((TreeTableColumn.CellDataFeatures<Member, String> param) -> param.getValue().getValue().getAddressProperty());
   
        JFXTreeTableColumn<Member, String> phone = new JFXTreeTableColumn<>("Telephone:");
        phone.setPrefWidth(100);
        phone.setCellValueFactory((TreeTableColumn.CellDataFeatures<Member, String> param) -> param.getValue().getValue().getPhoneProperty());
    
        JFXTreeTableColumn<Member, String> username = new JFXTreeTableColumn<>("Username:");
        username.setPrefWidth(100);
        username.setCellValueFactory((TreeTableColumn.CellDataFeatures<Member, String> param) -> param.getValue().getValue().getUsernameProperty());

         JFXTreeTableColumn<Member, String> password = new JFXTreeTableColumn<>("Password:");
        password.setPrefWidth(100);
        password.setCellValueFactory((TreeTableColumn.CellDataFeatures<Member, String> param) -> param.getValue().getValue().getPasswordProperty());
 
         JFXTreeTableColumn<Member, String> email = new JFXTreeTableColumn<>("Email:");
        email.setPrefWidth(100);
        email.setCellValueFactory((TreeTableColumn.CellDataFeatures<Member, String> param) -> param.getValue().getValue().getEmailProperty());
 
        tvMember.getColumns().addAll(name,address,phone,username,password,email);
        
        content = Member.selectAll();
       
        TreeItem<Member> root = new RecursiveTreeItem<>(content, RecursiveTreeObject::getChildren);
        tvMember.setRoot(root);
        tvMember.setShowRoot(false);
    }    

    
}
