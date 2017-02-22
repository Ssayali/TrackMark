package in.blazonsoftwares.trackmark;

public class Model {

    private String shop_name;
    private String shop_desc;
    private String logo;
    private String Latitude;
    private String Langitude;
    private String Shop_no;




    //shop variblke
    private String Product_Image;
    private String Product_No;
    private String Product_Name;
    private String Product_price;
    private String Product_Spec;
    private String Product_Shopcode;
    private  String Product_MasterId;


    public String getLink() {
        return link;
    }
    private String link;

    public String getShop_name() {
        return shop_name;
    }
    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_desc() {
        return shop_desc;
    }
    public void setShop_desc(String shop_desc) {
        this.shop_desc = shop_desc;
    }

    public String getLogo() {
        return logo;
    }
    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setLink(String link) {
        this.link = link;
    }



    //product
    public String getProduct_Image() {
        return Product_Image;
    }
    public void setProduct_Image(String Product_Image) {
        this.Product_Image = Product_Image;
    }

    public String getProduct_No() {
        return Product_No;
    }
    public void setProduct_No(String Product_No) {
        this.Product_No = Product_No;
    }

    public String getProduct_Name() {
        return Product_Name;
    }
    public void setProduct_Name(String Product_Name) {
        this.Product_Name = Product_Name;
    }


    public String getProduct_price() {
        return Product_price;
    }
    public void setProduct_price(String Product_price) {
        this.Product_price = Product_price;
    }

    public String getProduct_Spec() {
        return Product_Spec;
    }
    public void setProduct_Spec(String Product_Spec) {
        this.Product_Spec = Product_Spec;
    }

    public String getProduct_Shopcode() {
        return Product_Shopcode;
    }
    public void setProduct_Shopcode(String Product_Shopcode) {this.Product_Shopcode = Product_Shopcode; }

    public String getProduct_MasterId() {
        return Product_MasterId;
    }
    public void setProduct_MasterId(String Product_MasterId) {this.Product_MasterId = Product_MasterId; }


    //mapactivity page
    public String getLatitude() {
        return Latitude;
    }
    public void setLatitude(String Shop_Latval) {this.Latitude = Latitude; }

    public String getLangitude() {
        return Langitude;
    }
    public void setLangitude(String Langitude) {this.Langitude = Langitude; }

    public String getShop_no() {
        return Shop_no;
    }
    public void setShop_no(String Shop_no) {this.Shop_no = Shop_no; }




}