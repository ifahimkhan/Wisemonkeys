package in.wisemonkeys.android.wisemonkeys.modelslogin;

/**
 * Created by HSBC on 16-10-2017.
 */

public class User {
    private int id;
    private String username;
    private String nicename;
    private String email;
    private String url;
    private String registered;
    private String displayname;
    private String firstname;
    private String lastname;
    private String nickname;
    private String description;
    private Capabilities capabilities;
    private Object avatar;

    public User(int id, String username, String nicename, String email, String url, String registered, String displayname, String firstname, String lastname, String nickname, String description, Capabilities capabilities, Object avatar) {
        this.id = id;
        this.username = username;
        this.nicename = nicename;
        this.email = email;
        this.url = url;
        this.registered = registered;
        this.displayname = displayname;
        this.firstname = firstname;
        this.lastname = lastname;
        this.nickname = nickname;
        this.description = description;
        this.capabilities = capabilities;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNicename() {
        return nicename;
    }

    public void setNicename(String nicename) {
        this.nicename = nicename;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Capabilities getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Capabilities capabilities) {
        this.capabilities = capabilities;
    }

    public Object getAvatar() {
        return avatar;
    }

    public void setAvatar(Object avatar) {
        this.avatar = avatar;
    }
}
