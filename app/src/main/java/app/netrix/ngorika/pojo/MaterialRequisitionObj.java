package app.netrix.ngorika.pojo;

import java.util.List;

public class MaterialRequisitionObj {

    private int id;
    private int user;
    private int farmer;
    private String ref;
    private String mrType;
    private double subtotal;
    private double vat;
    private double total;
    private List<MaterialRequisitionDetail> materialRequisitionDetails;

    public MaterialRequisitionObj(int id, int user, String ref, String mrType, double subtotal, double vat, double total, List<MaterialRequisitionDetail> materialRequisitionDetails) {
        this.id = id;
        this.user = user;
        this.ref = ref;
        this.mrType = mrType;
        this.subtotal = subtotal;
        this.vat = vat;
        this.total = total;
        this.materialRequisitionDetails = materialRequisitionDetails;
    }

    public MaterialRequisitionObj() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser() {
        return user;
    }

    public int getFarmer() {
        return farmer;
    }

    public void setFarmer(int farmer) {
        this.farmer = farmer;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getMrType() {
        return mrType;
    }

    public void setMrType(String mrType) {
        this.mrType = mrType;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<MaterialRequisitionDetail> getMaterialRequisitionDetails() {
        return materialRequisitionDetails;
    }

    public void setMaterialRequisitionDetails(List<MaterialRequisitionDetail> materialRequisitionDetails) {
        this.materialRequisitionDetails = materialRequisitionDetails;
    }

    @Override
    public String toString() {
        return "MaterialRequisitionObj{" +
                "id=" + id +
                ", user=" + user +
                ", ref='" + ref + '\'' +
                ", mrType='" + mrType + '\'' +
                ", subtotal=" + subtotal +
                ", vat=" + vat +
                ", total=" + total +
                ", materialRequisitionDetails=" + materialRequisitionDetails +
                '}';
    }
}
