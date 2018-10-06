package in.wisemonkeys.android.wisemonkeys.usermodel;

import java.util.List;

/**
 * Created by HSBC on 21-10-2017.
 */

public class UserProfile{
    public Boolean status ;
    public List<String> error ;
    public List<Meta> data ;

    public UserProfile() {
    }

    public UserProfile(Boolean status, List<String> error, List<Meta> data) {
        this.status = status;
        this.error = error;
        this.data = data;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<String> getError() {
        return error;
    }

    public void setError(List<String> error) {
        this.error = error;
    }

    public List<Meta> getData() {
        return data;
    }

    public void setData(List<Meta> data) {
        this.data = data;
    }
}
