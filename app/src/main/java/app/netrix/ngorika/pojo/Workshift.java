package app.netrix.ngorika.pojo;

public class Workshift {

    private int id;
    private String shiftname;

    public Workshift(int id, String shiftname) {
        this.id = id;
        this.shiftname = shiftname;
    }

    public Workshift() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShiftname() {
        return shiftname;
    }

    public void setShiftname(String shiftname) {
        this.shiftname = shiftname;
    }

    @Override
    public String toString() {
        return shiftname;
    }
}
