package app.netrix.ngorika.pojo;

public class User {

    private int userid;
    private String userName;
    private String email;
    private int department;
    private int hrEmployee;
    private int role;
    private boolean active;
    private boolean approved;
    private boolean salesHod;
    private boolean purchasesHod;
    private boolean storesHod;
    private boolean financeHod;
    private boolean productionHod;
    private String password;

    public User() {
    }

    public User(int userid, String password) {
        this.userid = userid;
        this.password = password;
    }

    public User(int userid, String userName, String email, int department, int hrEmployee, int role, boolean active, boolean approved, boolean salesHod, boolean purchasesHod, boolean storesHod, boolean financeHod, boolean productionHod, String password) {
        this.userid = userid;
        this.userName = userName;
        this.email = email;
        this.department = department;
        this.hrEmployee = hrEmployee;
        this.role = role;
        this.active = active;
        this.approved = approved;
        this.salesHod = salesHod;
        this.purchasesHod = purchasesHod;
        this.storesHod = storesHod;
        this.financeHod = financeHod;
        this.productionHod = productionHod;
        this.password = password;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getDepartment() {
        return department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }

    public int getHrEmployee() {
        return hrEmployee;
    }

    public void setHrEmployee(int hrEmployee) {
        this.hrEmployee = hrEmployee;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isSalesHod() {
        return salesHod;
    }

    public void setSalesHod(boolean salesHod) {
        this.salesHod = salesHod;
    }

    public boolean isPurchasesHod() {
        return purchasesHod;
    }

    public void setPurchasesHod(boolean purchasesHod) {
        this.purchasesHod = purchasesHod;
    }

    public boolean isStoresHod() {
        return storesHod;
    }

    public void setStoresHod(boolean storesHod) {
        this.storesHod = storesHod;
    }

    public boolean isFinanceHod() {
        return financeHod;
    }

    public void setFinanceHod(boolean financeHod) {
        this.financeHod = financeHod;
    }

    public boolean isProductionHod() {
        return productionHod;
    }

    public void setProductionHod(boolean productionHod) {
        this.productionHod = productionHod;
    }
}
