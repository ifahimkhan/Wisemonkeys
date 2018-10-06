package in.wisemonkeys.android.wisemonkeys.models;

/**
 * Created by HSBC on 18-10-2017.
 */

public class Latest {
    private int ID ;
    private int post_author ;
    private String post_title ;
    private String display_name ;

    public boolean isVotedown() {
        return votedown;
    }

    public void setVotedown(boolean votedown) {
        this.votedown = votedown;
    }

    private boolean votedown;

    public boolean isVoteup() {
        return voteup;
    }

    public void setVoteup(boolean voteup) {
        this.voteup = voteup;
    }

    private boolean voteup;

    public Latest(int ID, int post_author, String post_title, String display_name) {
        this.ID = ID;
        this.post_author = post_author;
        this.post_title = post_title;
        this.display_name = display_name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getPost_author() {
        return post_author;
    }

    public void setPost_author(int post_author) {
        this.post_author = post_author;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

}
