package javafxapplication1;

import java.sql.Date;

/**
 *
 * @author SHIHAN
 */
public class ProductData {

    private Integer id;
    private String pro_id;
    private String pro_name;
    private String type;
    private int stock;
    private Double price;
    private String status;
    private String image;
    private Date date;

    public ProductData(Integer id, String pro_id, String pro_name,String type, int stock, Double price, String status, String image, Date date) {
        this.id = id;
        this.pro_id = pro_id;
        this.pro_name = pro_name;
        this.type=type;
        this.stock = stock;
        this.price = price;
        this.status = status;
        this.image = image;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public String getPro_id() {
        return pro_id;
    }

    public String getPro_name() {
        return pro_name;
    }

    public String getType() {
        return type;
    }
    
    public int getStock() {
        return stock;
    }

    public Double getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public String getImage() {
        return image;
    }

    public Date getDate() {
        return date;
    }

}
