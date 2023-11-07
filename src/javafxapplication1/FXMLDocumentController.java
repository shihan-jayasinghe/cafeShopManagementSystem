/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafxapplication1.util.AlertProvider;
import javafxapplication1.util.NowDate;

/**
 *
 * @author SHIHAN
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private AnchorPane id_loginForm;

    @FXML
    private TextField id_userName;

    @FXML
    private PasswordField id_password;

    @FXML
    private Button id_login;

    @FXML
    private Hyperlink id_forgotPWD;

    @FXML
    private AnchorPane reg_form;

    @FXML
    private TextField reg_userName;

    @FXML
    private PasswordField reg_pwd;

    @FXML
    private Button reg_signBtn;

    @FXML
    private ComboBox<?> reg_quiz;

    @FXML
    private TextField reg_ans;

    @FXML
    private AnchorPane question_form;

    @FXML
    private Button question_proceed;

    @FXML
    private ComboBox<?> question_question;

    @FXML
    private TextField question_answer;

    @FXML
    private TextField question_uname;

    @FXML
    private Button question_back;

    @FXML
    private AnchorPane changePWD_form;

    @FXML
    private Button changePWD_change;

    @FXML
    private Button changePWD_back;

    @FXML
    private PasswordField changePWD_new;

    @FXML
    private PasswordField changePWD_confirm;

    @FXML
    private AnchorPane side_Form;

    @FXML
    private Button side_createBtn;

    @FXML
    private Button side_already;

    private Connection con;
    private PreparedStatement pst;
    private Alert alert;

    public void loginBtn() {
        String uname = id_userName.getText();
        String pwd = id_password.getText();

        if (uname.isEmpty() || pwd.isEmpty()) {
            AlertProvider.errorAlert("Incorrect Username/Password");
        } else {
            String query = "select uname,password from employee where uname=? and password=?;";
            try {
                con = Database.connect();
                pst = con.prepareStatement(query);
                pst.setString(1, uname);
                pst.setString(2, pwd);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    
                    //GET THE 'USE NAME' THAT PROVIDED BY THE USER
                    Data.username=uname;
                    
                    AlertProvider.infoAlert("Login success!");

                    try {//LOAD MAIN FRAME
                        
                        Parent root = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
                        Stage stage=new Stage();
                        Scene scene = new Scene(root);
                        stage.setTitle("Cafe Shop Management System");
                        stage.setMinWidth(1100);
                        stage.setMinHeight(600);
                        stage.setScene(scene);
                        stage.show();
                        id_login.getScene().getWindow().hide();
                        
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    AlertProvider.errorAlert("Incorrect Username/Password");
                }
            } catch (SQLException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    public void regBtn() {
        if (reg_userName.getText().isEmpty() || reg_pwd.getText().isEmpty() || reg_quiz.getSelectionModel().getSelectedItem() == null || reg_ans.getText().isEmpty()) {
            AlertProvider.errorAlert("please fill all blank fields");
        } else {
            String regData = "INSERT INTO employee(uname,password,question,answer,ddate)VALUES(?,?,?,?,?);";
            try {

                if (reg_pwd.getText().length() < 8) {
                    AlertProvider.errorAlert("Please provide atlease 8 character for the password!");
                } else {

                    con = Database.connect();
                    pst = con.prepareStatement(regData);
                    pst.setString(1, reg_userName.getText());
                    pst.setString(2, reg_pwd.getText());
                    pst.setString(3, (String) reg_quiz.getSelectionModel().getSelectedItem());
                    pst.setString(4, reg_ans.getText());
                    pst.setString(5, (String.valueOf(NowDate.getSqlDate())));
                    pst.executeUpdate();

                    AlertProvider.infoAlert("Successfully registerd account!");

                    reg_userName.setText("");
                    reg_pwd.setText("");
                    reg_quiz.getSelectionModel().clearSelection();
                    reg_ans.setText("");

                    TranslateTransition slide = new TranslateTransition();
                    slide.setNode(side_Form);
                    slide.setToX(0);
                    slide.setDuration(Duration.seconds(0.5));
                    slide.setOnFinished((ActionEvent ae) -> {
                        side_already.setVisible(false);
                        side_createBtn.setVisible(true);
                    });

                    slide.play();
                }
            } catch (SQLException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    private final String[] questionList = {"What is your name?", "What is your favorire food?", "What's your first pet's name?"};

    public void regQuestionList() {
        List<String> listQ = new ArrayList<>();
        listQ.addAll(Arrays.asList(questionList));

        ObservableList listData = FXCollections.observableArrayList(listQ);
        reg_quiz.setItems(listData);
    }

    public void switchForgotPass() {
        id_loginForm.setVisible(false);
        question_form.setVisible(true);
        forgotPasswordQuestionList();
    }

    int proceedID = 0;

    public void proceedBtn() {
        String question = (String) question_question.getSelectionModel().getSelectedItem();
        String ans = question_answer.getText();
        String uname = question_uname.getText();

        if (uname.isEmpty() || question == null || ans.isEmpty()) {
            AlertProvider.errorAlert("Please fill all blank fields!");

        } else {
            String query = "select id,question,answer,uname from employee where question=? and answer=? and uname=?;";
            try {
                con = Database.connect();
                pst = con.prepareStatement(query);
                pst.setString(1, question);
                pst.setString(2, ans);
                pst.setString(3, uname);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    changePWD_form.setVisible(true);
                    question_form.setVisible(false);

                    //to make available retrived column's id to changePWDbtn()
                    proceedID = rs.getInt("id");
                } else {
                    AlertProvider.errorAlert("Invalid information");
                }
            } catch (SQLException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void changePWDbtn() {
        String newPWD = changePWD_new.getText();
        String confirmPWD = changePWD_confirm.getText();

        if (newPWD.isEmpty() || confirmPWD.isEmpty()) {
            AlertProvider.errorAlert("Please fill all blank fields!");
        } else {
            //check whether the new one and confirm one is equals
            if (newPWD.equals(confirmPWD)) {
                String query = "update employee set password=? where id=?";
                try {
                    con = Database.connect();
                    pst = con.prepareStatement(query);
                    pst.setString(1, newPWD);
                    //the id assign to proceedID, when the proceedBtn() is executed 
                    pst.setInt(2, proceedID);
                     int updateCount = pst.executeUpdate();

                    //print message if the query affects only on one row
                    if (updateCount == 1) {
                        AlertProvider.infoAlert("Password is changed!");
                    }

                    id_loginForm.setVisible(true);
                    changePWD_form.setVisible(false);
                    question_form.setVisible(false);

                    changePWD_new.setText("");
                    changePWD_confirm.setText("");
                    question_uname.setText("");
                    question_answer.setText("");
                    question_question.getSelectionModel().clearSelection();
                } catch (SQLException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                AlertProvider.errorAlert("Password do not match!");
            }
        }
    }

    public void forgotPasswordQuestionList() {
        List<String> listQ = new ArrayList<>();
        listQ.addAll(Arrays.asList(questionList));

        ObservableList listData = FXCollections.observableArrayList(listQ);
        question_question.setItems(listData);
    }

    public void backToLogingForm() {
        id_loginForm.setVisible(true);
        question_form.setVisible(false);
    }

    public void backToQuestionForm() {
        question_form.setVisible(true);
        changePWD_form.setVisible(false);
        id_loginForm.setVisible(false);
    }

    public void switchForm(ActionEvent e) {
        TranslateTransition slide = new TranslateTransition();
        if (e.getSource() == side_createBtn) {
            slide.setNode(side_Form);
            slide.setToX(300);
            slide.setDuration(Duration.seconds(0.5));
            slide.setOnFinished((ActionEvent ae) -> {
                side_already.setVisible(true);
                side_createBtn.setVisible(false);

                id_loginForm.setVisible(true);
                question_form.setVisible(false);
                changePWD_form.setVisible(false);
            });

            slide.play();
        } else if (e.getSource() == side_already) {
            slide.setNode(side_Form);
            slide.setToX(0);
            slide.setDuration(Duration.seconds(0.5));
            slide.setOnFinished((ActionEvent ae) -> {
                side_already.setVisible(false);
                side_createBtn.setVisible(true);

                id_loginForm.setVisible(true);
                question_form.setVisible(false);
                changePWD_form.setVisible(false);
            });

            slide.play();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
