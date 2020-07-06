package app.netrix.ngorika.pojo;

public class InvClass {

    private int id;
    private String name;

    public InvClass(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public InvClass() {
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

    @Override
    public String toString() {
        return name;
    }
}