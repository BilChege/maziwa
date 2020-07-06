package app.netrix.ngorika.pojo;

public class MilkCan {

    private int id;
    private String cannumber;
    private int route;
    private String description;

    public MilkCan(int id, String cannumber, int route, String description) {
        this.id = id;
        this.cannumber = cannumber;
        this.route = route;
        this.description = description;
    }

    public MilkCan() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCannumber() {
        return cannumber;
    }

    public void setCannumber(String cannumber) {
        this.cannumber = cannumber;
    }

    public int getRoute() {
        return route;
    }

    public void setRoute(int route) {
        this.route = route;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return cannumber;
    }
}
