package app.netrix.ngorika.pojo;

public class MrResponse {

    private int resultcode;
    private String status;

    public MrResponse(int resultcode, String status) {
        this.resultcode = resultcode;
        this.status = status;
    }

    public MrResponse() {
    }

    public int getResultcode() {
        return resultcode;
    }

    public void setResultcode(int resultcode) {
        this.resultcode = resultcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
