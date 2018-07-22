package ldap_opa;

public class GroupResult extends Group {
    private Integer status = 200;
    private String message = "Success";

    public Integer getStatus() { return status; }

    public void setStatus(Integer status) { this.status = status; }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
