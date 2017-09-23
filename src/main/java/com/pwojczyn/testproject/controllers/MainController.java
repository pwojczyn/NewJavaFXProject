package com.pwojczyn.testproject.controllers;


import com.pwojczyn.testproject.models.UserSession;
import com.pwojczyn.testproject.models.dao.impl.ContactDaoImpl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable{

    @FXML
    ListView<String> listContacts;
@FXML
    Button buttonLogout, newButtonContact,buttonEditContact, buttonEraseContact;
@FXML
    TextField textName, textNumber, newContact, newNumber;




private UserSession session = UserSession.getInstance();
private ContactDaoImpl contactDao = new ContactDaoImpl();

private ObservableList contactsItems;

    public void initialize(URL location, ResourceBundle resources) {
        // buttonHello.setOnMouseClicked(s -> System.out.println("Hello!"));
       // MysqlConnector.getInstance();
        textName.setEditable(false);
        textNumber.setEditable(false);

        loadContacts();

        listContacts.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                textName.setText(t1);
                textNumber.setText(contactDao.getNumber(t1));
            }
        });

        buttonLogout.setOnMouseClicked(e -> logout());

        newButtonContact.setOnMouseClicked(e -> addNewContact());

        buttonEraseContact.setOnMouseClicked(e -> eraseConatct());

        updateAction();

     }

    private void eraseConatct() {
        //System.out.println("usuwanie konatkt"+listContacts.getSelectionModel().getSelectedItem());
        contactDao.removeContact(listContacts.getSelectionModel().getSelectedItem());
        loadContacts();
    }

    private void addNewContact() {

        contactDao.addContact(newContact.getText(),newNumber.getText());
        loadContacts();
    }

    private void updateAction() {
        textName.setOnMouseClicked(e -> {
            if (e.getClickCount() >= 2){
                 textName.setEditable(true);
            }
        });
        textNumber.setOnMouseClicked(e -> {
            if (e.getClickCount() >= 2){
                textNumber.setEditable(true);
            }
        });

        textName.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (!t1){

                    contactDao.editContact(textName.getText(),textNumber.getText(), listContacts.getSelectionModel().getSelectedItem());
                    loadContacts();
                    textName.setEditable(false);
                }
            }
        });
        textNumber.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                contactDao.editContact(textName.getText(),textNumber.getText(), listContacts.getSelectionModel().getSelectedItem());
                loadContacts();
                textNumber.setEditable(false);
            }
        });

    }

    private void logout() {
        session.setLogedIn(false);
        session.setUsername(null);
        session.setId(0);
        Stage stage = (Stage) buttonLogout.getScene().getWindow();
         try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("loginView.fxml"));
            stage.setScene(new Scene(root, 600, 400));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void loadContacts() {
        contactsItems = FXCollections.observableArrayList(contactDao.getAllContactsNames(session.getUsername()));
        listContacts.setItems(contactsItems);

//        if(session.isLogedIn()){
//            System.out.println("Uzytkownik zalogowany");
//            //test
//            usernameView.setText("test");
//
//            // add list of contact of user
//
//            ObservableList<String> items = FXCollections.observableArrayList(contactDao.getAllContactsNames(session.getUsername()));
//            listContacts.setItems(items);
//
//
//        }
    }


}