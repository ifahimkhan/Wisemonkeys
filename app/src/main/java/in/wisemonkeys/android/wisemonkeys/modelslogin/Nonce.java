package in.wisemonkeys.android.wisemonkeys.modelslogin;

/**
 * Created by HSBC on 17-10-2017.
 */

public class Nonce {
    private String status;
    private String controller;
    private String method;
    private String nonce;

    public Nonce(String status, String controller, String method, String nonce) {
        this.status = status;
        this.controller = controller;
        this.method = method;
        this.nonce = nonce;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

}
