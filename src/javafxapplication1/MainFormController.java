package javafxapplication1;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author SHIHAN
 */
public class MainFormController implements Initializable {

    @FXML
    private AnchorPane main_form;

    @FXML
    private Label username;

    @FXML
    private Button dashboard_btn;

    @FXML
    private Button inventory_btn;

    @FXML
    private Button menu_btn;

    @FXML
    private Button customers_btn;

    @FXML
    private Button logout_btn;

    @FXML
    private AnchorPane inventory_form;

    @FXML
    private TableView<ProductData> inventory_tableView;

    @FXML
    private TableColumn<ProductData, String> inventory_col_idProduct;

    @FXML
    private TableColumn<ProductData, String> inventory_col_pName;

    @FXML
    private TableColumn<ProductData, String> inventory_col_type;

    @FXML
    private TableColumn<ProductData, String> inventory_col_stock;

    @FXML
    private TableColumn<ProductData, String> inventory_col_price;

    @FXML
    private TableColumn<ProductData, String> inventory_col_status;

    @FXML
    private TableColumn<ProductData, String> inventory_col_date;

    @FXML
    private TextField inventory_productID;

    @FXML
    private TextField inventory_productName;

    @FXML
    private ComboBox<String> inventory_type;

    @FXML
    private TextField inventory_stock;

    @FXML
    private TextField inventory_price;

    @FXML
    private ImageView inventory_imageView;

    @FXML
    private Button inventory_importBtn;

    @FXML
    private Button inventory_addBtn;

    @FXML
    private Button inventory_updateBtn;

    @FXML
    private Button inventory_clearBtn;

    @FXML
    private Button inventory_deleteBtn;

    @FXML
    private ComboBox<String> inventory_status;

    @FXML
    private AnchorPane menu_form;

    @FXML
    private ScrollPane menu_scrollPane;

    @FXML
    private GridPane menu_gridPane;

    @FXML
    private TableView<?> menu_tableView;

    @FXML
    private TableColumn<?, ?> menu_col_productName;

    @FXML
    private TableColumn<?, ?> menu_col_quantity;

    @FXML
    private TableColumn<?, ?> menu_col_price;

    @FXML
    private Label menu_total;

    @FXML
    private TextField menu_amount;

    @FXML
    private Label menu_change;

    @FXML
    private Button menu_payBtn;

    @FXML
    private Button menu_removeBtn;

    @FXML
    private Button menu_receiptBtn;

    private Alert alert;
    private Connection con;
    private PreparedStatement pst;
    private Statement st;
    private ResultSet rs;

    private Image image;

    private ObservableList<ProductData> cardListData = FXCollections.observableArrayList();

    public void inventoryAddBtn() {
        if (inventory_productID.getText().isEmpty()
                || inventory_productName.getText().isEmpty()
                || inventory_type.getSelectionModel().getSelectedItem() == null
                || inventory_stock.getText().isEmpty()
                || inventory_price.getText().isEmpty()
                || inventory_status == null
                || Data.path == null) {

            alert = new Alert(AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
            String checkProdID = "select pro_id from product where pro_id='" + inventory_productID.getText() + "'";
            try {
                con = Database.connect();
                st = con.createStatement();
                rs = st.executeQuery(checkProdID);

                if (rs.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText(inventory_productID.getText() + " is alreay taken");
                    alert.showAndWait();
                } else {
                    String query = "insert into product(pro_id,pro_name,type,stock,price,status,image,date)values(?,?,?,?,?,?,?,?)";
                    pst = con.prepareStatement(query);
                    pst.setString(1, inventory_productID.getText());
                    pst.setString(2, inventory_productName.getText());
                    pst.setString(3, (String) inventory_type.getSelectionModel().getSelectedItem());
                    pst.setString(4, inventory_stock.getText());
                    pst.setString(5, inventory_price.getText());
                    pst.setString(6, (String) inventory_status.getSelectionModel().getSelectedItem());

                    String path = Data.path;
                    path = path.replace("\\", "\\\\");
                    pst.setString(7, path);

                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    pst.setString(8, String.valueOf(sqlDate));

                    pst.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully added!");
                    alert.showAndWait();

                    inventoryShowData();
                    inventoryClearBtn();
                }
            } catch (SQLException ex) {
                Logger.getLogger(MainFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void inventoryUpdateBtn() {
        if (inventory_productID.getText().isEmpty()
                || inventory_productName.getText().isEmpty()
                || inventory_type.getSelectionModel().getSelectedItem() == null
                || inventory_stock.getText().isEmpty()
                || inventory_price.getText().isEmpty()
                || inventory_status == null
                || Data.path == null || Data.id == 0) {

            alert = new Alert(AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
            String path = Data.path;
            path = path.replace("\\", "\\\\");

            String updateQuery = "update product set ";
            updateQuery += "pro_id=" + "'" + inventory_productID.getText() + "',";
            updateQuery += "pro_name=" + "'" + inventory_productName.getText() + "',";
            updateQuery += "type=" + "'" + inventory_type.getSelectionModel().getSelectedItem() + "',";
            updateQuery += "stock=" + "'" + inventory_stock.getText() + "',";
            updateQuery += "price=" + "'" + inventory_price.getText() + "',";
            updateQuery += "status=" + "'" + inventory_status.getSelectionModel().getSelectedItem() + "',";
            updateQuery += "image=" + "'" + path + "',";
            updateQuery += "date=" + "'" + Data.date + "' ";
            updateQuery += "where id=" + Data.id + ";";

            try {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to update Product ID: " + inventory_productID.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    con = Database.connect();
                    pst = con.prepareStatement(updateQuery);
                    pst.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully updated");
                    alert.showAndWait();

                    //TO REFRESH THE DATA & CLEAR THE FIELDS
                    inventoryShowData();
                    inventoryClearBtn();
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancelled");
                    alert.showAndWait();
                }

            } catch (SQLException ex) {
                Logger.getLogger(MainFormController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void inventoryDeleBtn() {
        if (inventory_productID.getText().isEmpty()) {

            alert = new Alert(AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("selct data to delete");
            alert.showAndWait();
        } else {
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete Prouct ID: " + inventory_productID.getText());
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                try {
                    con = Database.connect();
                    String deleteQuery = "delete from product where id=" + Data.id;

                    pst = con.prepareStatement(deleteQuery);
                    pst.executeUpdate();

                    alert = new Alert(AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("successfully deleted!");
                    alert.showAndWait();

                    //TO REFRESH THE DATA & CLEAR THE FIELDS
                    inventoryShowData();
                    inventoryClearBtn();

                } catch (SQLException ex) {
                    Logger.getLogger(MainFormController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("cancelled!");
                alert.showAndWait();
            }
        }
    }

    public void inventoryClearBtn() {
        inventory_productID.setText("");
        inventory_productName.setText("");
        inventory_type.getSelectionModel().clearSelection();
        inventory_stock.setText("");
        inventory_price.setText("");
        inventory_status.getSelectionModel().clearSelection();
        Data.path = "";
        Data.id = 0;
        inventory_imageView.setImage(null);
    }

    public void inventoryImortBtn() {
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open image file", "*png", "*jpg"));
        File file = openFile.showOpenDialog(main_form.getScene().getWindow());

        if (file != null) {
            Data.path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(), 118, 135, false, true);
            inventory_imageView.setImage(image);
        }
    }

    //FETCHING PRODUCT DATA FROM THE TABLE & CREATE "ProductData" OBJECT & RETURN IT
    public ObservableList<ProductData> inventoryDataList() {
        ObservableList<ProductData> listData = FXCollections.observableArrayList();
        String sql = "select * from product";
        try {
            con = Database.connect();
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            ProductData proData;
            while (rs.next()) {
                proData = new ProductData(
                        rs.getInt("id"),
                        rs.getString("pro_id"),
                        rs.getString("pro_name"),
                        rs.getString("type"),
                        rs.getInt("stock"),
                        rs.getDouble("price"),
                        rs.getString("status"),
                        rs.getString("image"),
                        rs.getDate("date")
                );
                listData.add(proData);
            }

        } catch (SQLException ex) {
            Logger.getLogger(MainFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listData;
    }

    //SHOW DATA ON THE TABLE
    private ObservableList<ProductData> inventoryListData;

    public void inventoryShowData() {
        inventoryListData = inventoryDataList();//inventoryDataList()INTERNALLY HAS THE "ProductData" OBJECT.

        inventory_col_idProduct.setCellValueFactory(new PropertyValueFactory<>("pro_id"));
        inventory_col_pName.setCellValueFactory(new PropertyValueFactory<>("pro_name"));
        inventory_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        inventory_col_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        inventory_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        inventory_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        inventory_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        inventory_tableView.setItems(inventoryListData);
    }

    //ADD LIST ITEMS TO "Type" COMBOBOX
    private String[] typeList = {"Meals", "Drinks"};

    public void inventorySelectData() {
        ProductData prodData = inventory_tableView.getSelectionModel().getSelectedItem();
        int num = inventory_tableView.getSelectionModel().getSelectedIndex();

        if ((num) < 0) {
            return;
        }

        inventory_productID.setText(prodData.getPro_id());
        inventory_productName.setText(prodData.getPro_name());
        inventory_stock.setText(String.valueOf(prodData.getStock()));
        inventory_price.setText(String.valueOf(prodData.getPrice()));

        Data.date = String.valueOf(prodData.getDate());
        Data.id = prodData.getId();

        Data.path = prodData.getImage();
        String path = "File:" + prodData.getImage();
        image = new Image(path, 118, 135, false, true);
        inventory_imageView.setImage(image);

    }

    public void inventoryTypeLis() {
        List<String> typeL = new ArrayList<>();
        typeL.addAll(Arrays.asList(typeList));
        ObservableList listData = FXCollections.observableArrayList(typeL);
        inventory_type.setItems(listData);
    }

    ////ADD LIST ITEMS TO "Status" COMBOBOX
    private String[] statusList = {"Available", "Unavailable"};

    public void inventoryStatusLis() {
        List<String> typeL = new ArrayList<>();
        typeL.addAll(Arrays.asList(statusList));
        ObservableList listData = FXCollections.observableArrayList(typeL);
        inventory_status.setItems(listData);
    }

    public ObservableList<ProductData> menuGetData() {
        String sql = "select * from product";
        ObservableList<ProductData> listData = FXCollections.observableArrayList();
        try {
            con = Database.connect();
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            ProductData prod;
            while (rs.next()) {
                prod = new ProductData(rs.getInt("id"), rs.getString("pro_id"), rs.getString("pro_name"), rs.getDouble("price"), rs.getString("image"));
                listData.add(prod);
            }

        } catch (SQLException ex) {
            Logger.getLogger(MainFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listData;
    }

    public void menuDisplayCard() {
        cardListData.clear();
        cardListData.addAll(menuGetData());

        int row = 0;
        int column = 0;

        menu_gridPane.getRowConstraints().clear();
        menu_gridPane.getColumnConstraints().clear();
        
        for (int a = 0; a < cardListData.size(); a++) {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource("CardProduct.fxml"));
            try {
                AnchorPane pane = load.load();
                CardProductController prodD = load.getController();
                prodD.setData(cardListData.get(a));

                if (column == 3) {
                    column = 0;
                    row += 1;
                }
                menu_gridPane.add(pane, column++, row);
                
            } catch (IOException ex) {
                Logger.getLogger(MainFormController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    //"Logout" BTN
    public void logout() {
        try {
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                //TO HIDE MAIN FORM
                logout_btn.getScene().getWindow().hide();

                //LINK YOUR LOGINFORM AND SHOW IT
                Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setTitle("Cafe Shop Management System");
                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TAKE THE LOGGED USE NAME & DISPLAY THE NAME ON THE LEFT PANEL
    public void displayUsereName() {
        String user = Data.username;
        user = user.substring(0, 1).toUpperCase() + (user.substring(1));
        username.setText(user);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayUsereName();
        
        inventoryTypeLis();
        inventoryStatusLis();
        inventoryShowData();
        
        menuDisplayCard();
    }

}
