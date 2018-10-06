package in.wisemonkeys.android.wisemonkeys.modelslogin;

/**
 * Created by HSBC on 16-10-2017.
 */

public class Capabilities {
    private boolean author;


    public boolean isAuthor() {
        return author;
    }

    public void setAuthor(boolean author) {
        this.author = author;
    }

    public Capabilities(boolean author) {

        this.author = author;
    }

}
