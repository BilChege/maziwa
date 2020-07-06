package app.netrix.ngorika.pojo;

/**
 * Created by CW on 11/12/2015.
 */
public class DataBundle {
    int userID;
    int routeId;
    int farmerID;
    int recordId;
    private boolean alcohal;
    private boolean peroxide;
    private int cannumber;
    private int shift;
    String weight;
    String farmerName;
    long time;
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public int getFarmerID() {
        return farmerID;
    }

    public boolean isAlcohal() {
        return alcohal;
    }

    public void setAlcohal(boolean alcohal) {
        this.alcohal = alcohal;
    }

    public boolean isPeroxide() {
        return peroxide;
    }

    public void setPeroxide(boolean peroxide) {
        this.peroxide = peroxide;
    }

    public int getCannumber() {
        return cannumber;
    }

    public void setCannumber(int cannumber) {
        this.cannumber = cannumber;
    }

    public int getShift() {
        return shift;
    }

    public void setShift(int shift) {
        this.shift = shift;
    }

    public void setFarmerID(int farmerID) {
        this.farmerID = farmerID;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }
}
