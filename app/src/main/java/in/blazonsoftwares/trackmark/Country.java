package in.blazonsoftwares.trackmark;

/**
 * Created by Android Developer on 16-Jun-16.
 */

public class Country {

    String code = null;
    String name = null;
    String Price = null;
    String qty = null;
    String Total = null;
    String ProductCode=null;



    boolean selected = false;

    public Country(String code, String name, boolean selected,String Price,String qty,String Total,String ProductCode) {
        super();
        this.code = code;
        this.name = name;
        this.selected = selected;
        this.Price = Price;
        this.qty = qty;
        this.Total = Total;
        this.ProductCode = ProductCode;

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String Price) {
        this.Price = Price;
    }

    public String getqty() {
        return qty;
    }

    public void setqty(String qty) {
        this.qty = qty;
    }

    public String getTotal() {return Total;}
    public void setTotal(String Total) {
        this.Total = Total;
    }

    public String getProductCode() {return ProductCode;}
    public void setProductCode(String ProductCode) {
        this.ProductCode = ProductCode;
    }


}
