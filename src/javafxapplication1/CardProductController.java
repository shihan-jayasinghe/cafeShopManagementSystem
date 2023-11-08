package javafxapplication1;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafxapplication1.util.AlertProvider;
import javafxapplication1.util.NowDate;

/**
 *
 * @author SHIHAN
 */
public class CardProductController implements Initializable {

    @FXML
    private AnchorPane card_form;

    @FXML
    private Label prod_name;

    @FXML
    private Label prod_price;

    @FXML
    private ImageView prod_imgView;

    @FXML
    private Spinner<Integer> prod_spinner;

    @FXML
    private Button prod_addBtn;

    private ProductData prodData;
    private Image image;
    private String prodID;
    private String type;
    private String prod_image;
    private String prod_date;
    private double totalPrice;

    private Connection con;
    private ResultSet rs;
    private PreparedStatement pst;

    private SpinnerValueFactory<Integer> spin;

    public void setData(ProductData prodData) {
        type = prodData.getType();
        prod_image = prodData.getImage();
        this.prodData = prodData;
        prod_date = String.valueOf(prodData.getDate());
        prodID = prodData.getPro_id();
        prod_name.setText(prodData.getPro_name());
        prod_price.setText("$" + String.valueOf(prodData.getPrice()));
        String path = "File:" + prodData.getImage();
        image = new Image(path, 160, 80, false, true);
        prod_imgView.setImage(image);
        pr = prodData.getPrice();
    }

    private int qty;

    public void setQuantity() {
        spin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        prod_spinner.setValueFactory(spin);
    }

    private double totalp;
    private double pr;

    public void addBtn() {

        MainFormController mForm = new MainFormController();
        mForm.customerID();//C001-->ASSIGN MAX "customer_id" VALUE TO "Data" class "cID" VARIABLE

        qty = prod_spinner.getValue();
        String check = "";
        String checkAvailable = "select status from product where pro_id='" + prodID + "';";
        try {

            con = Database.connect();

            int checkSTK = 0;
            String checkStock = "select stock from product where pro_id='" + prodID + "'";
            pst = con.prepareStatement(checkStock);
            rs = pst.executeQuery();

            if (rs.next()) {
                checkSTK = rs.getInt("stock");
            }

            if (checkSTK == 0) {
                prod_image = prod_image.replace("\\", "\\\\");
                String updateStock = "update product set pro_name='" + prod_name.getText() + "',"
                        + "type = '" + type + "',"
                        + "stock = 0,"
                        + "price = " + pr + ","
                        + "status = 'Unavailable',"
                        + "image = '" + prod_image + "',"
                        + "date = '" + prod_date + "'"
                        + "where pro_id='" + prodID + "';";
                pst = con.prepareStatement(updateStock);
                pst.executeUpdate();
            }

            pst = con.prepareStatement(checkAvailable);
            rs = pst.executeQuery();
            if (rs.next()) {
                check = rs.getString("status");
            }

            if ((qty == 0)) {
                AlertProvider.errorAlert("Selec to add");
            } else {

                if ((check.equals("Unavailable")) || checkSTK < qty) {
                    AlertProvider.errorAlert("Out of stock!");
                } else {
                    prod_image = prod_image.replace("\\", "\\\\");
                    String insertData = "insert into customer(customer_id,prod_id,prod_name,type,quantity,price,image,date,em_username)"
                            + " values(?,?,?,?,?,?,?,?,?);";
                    pst = con.prepareStatement(insertData);
                    pst.setString(1, String.valueOf(Data.cID)/*C0001*/);
                    pst.setString(2, prodID);
                    pst.setString(3, prod_name.getText());
                    pst.setString(4, type);
                    pst.setString(5, String.valueOf(qty));

                    totalp = (qty * pr);
                    pst.setString(6, String.valueOf(totalp));

                    pst.setString(7, prod_image);
                    pst.setString(8, String.valueOf(NowDate.getSqlDate()));
                    pst.setString(9, Data.username);
                    pst.executeUpdate();

                    int upStock = (checkSTK - qty);

                    String updateStock = "update product set pro_name='" + prod_name.getText() + "',"
                            + "type = '" + type + "',"
                            + "stock = " + upStock + ","
                            + "price = " + pr + ","
                            + "status = '" + check + "',"
                            + "image = '" + prod_image + "',"
                            + "date = '" + prod_date + "'"
                            + "where pro_id='" + prodID + "';";
                    pst = con.prepareStatement(updateStock);
                    pst.executeUpdate();

                    AlertProvider.infoAlert("Successfully added!");

                    mForm.customerID();
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(CardProductController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setQuantity();
    }

}
