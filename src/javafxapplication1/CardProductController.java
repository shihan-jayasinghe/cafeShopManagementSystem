
package javafxapplication1;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author SHIHAN
 */
public class CardProductController implements Initializable{

    @FXML
    private AnchorPane card_form;

    @FXML
    private Label prod_name;

    @FXML
    private Label prod_price;

    @FXML
    private ImageView prod_imgView;

    @FXML
    private Spinner<?> prod_spinner;

    @FXML
    private Button prod_addBtn;
    
    private ProductData prodData;
    private Image image;
    
    public void setData(ProductData prodData)
    {
      this.prodData=prodData;
      prod_name.setText(prodData.getPro_name());
      prod_price.setText(String.valueOf(prodData.getPrice()));
      String path="File:"+prodData.getImage();
      image =new Image(path,207,80,false,true);
      prod_imgView.setImage(image);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
}
