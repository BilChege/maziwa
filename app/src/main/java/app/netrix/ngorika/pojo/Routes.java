package app.netrix.ngorika.pojo;

/**
 * Created by chrissie on 12/4/15.
 */
public class Routes {
    private int id;
    private String route;

    public Routes(String name,int id){
    this.id=id;
    this.route=name;
    }
    public Routes(){

    }
    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
