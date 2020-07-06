package app.netrix.ngorika.pojo;

/**
 * Created by chrissie on 12/2/15.
 */
public class Farmer {
    public Farmer(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Farmer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int id;
    private String name;
}
