package app.netrix.ngorika.pojo;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Objects;

public class InventoryItem {

    private int id;
    private String currency;
    private String uom;
    private String desc;
    private double vat;
    private double price;
    private String internalCode;

    public InventoryItem(int id, String currency, String uom, String desc, double vat, String internalCode) {
        this.id = id;
        this.currency = currency;
        this.uom = uom;
        this.desc = desc;
        this.vat = vat;
        this.internalCode = internalCode;
    }

    public InventoryItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }

    public String getInternalCode() {
        return internalCode;
    }

    public void setInternalCode(String internalCode) {
        this.internalCode = internalCode;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InventoryItem)) return false;
        InventoryItem that = (InventoryItem) o;
        return getId() == that.getId() &&
                getVat() == that.getVat() &&
                getCurrency().equals(that.getCurrency()) &&
                Objects.equals(getUom(), that.getUom()) &&
                Objects.equals(getDesc(), that.getDesc()) &&
                Objects.equals(getInternalCode(), that.getInternalCode());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCurrency(), getUom(), getDesc(), getVat(), getInternalCode());
    }
}
