package app.netrix.ngorika.pojo;

public class MaterialRequisitionDetail {

    private int id;
    private int inv;
    private double qty;
    private double price;
    private double total;
    private String desc;
    private String uom;
    private String curr;

    public MaterialRequisitionDetail(int id, int inv, double qty, double price, double total) {
        this.id = id;
        this.inv = inv;
        this.qty = qty;
        this.price = price;
        this.total = total;
    }

    public MaterialRequisitionDetail() {
    }

    public int getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getCurr() {
        return curr;
    }

    public void setCurr(String curr) {
        this.curr = curr;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInv() {
        return inv;
    }

    public void setInv(int inv) {
        this.inv = inv;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "MaterialRequisitionDetail{" +
                "id=" + id +
                ", inv=" + inv +
                ", qty=" + qty +
                ", price=" + price +
                ", total=" + total +
                ", desc='" + desc + '\'' +
                ", uom='" + uom + '\'' +
                ", curr='" + curr + '\'' +
                '}';
    }
}
