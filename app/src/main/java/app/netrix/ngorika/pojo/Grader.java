package app.netrix.ngorika.pojo;

/**
 * Created by chrissie on 12/2/15.
 */
public class Grader {
    public Grader(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Grader() {
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
