package in.wisemonkeys.android.wisemonkeys.modelslogin;

/**
 * Created by HSBC on 16-10-2017.
 */

public class RootObject {
    private String status;
    private String cookie;
    private String cookie_name;
    private User user;

    public RootObject() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getCookie_name() {
        return cookie_name;
    }

    public void setCookie_name(String cookie_name) {
        this.cookie_name = cookie_name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RootObject(String status, String cookie, String cookie_name, User user) {

        this.status = status;
        this.cookie = cookie;
        this.cookie_name = cookie_name;
        this.user = user;
    }
}
