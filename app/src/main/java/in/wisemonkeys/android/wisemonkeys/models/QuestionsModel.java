package in.wisemonkeys.android.wisemonkeys.models;

import java.util.List;

/**
 * Created by HSBC on 18-10-2017.
 */

public class QuestionsModel {
    private List<Latest> latest;

    public List<Latest> getLatest() {
        return latest;
    }

    public void setLatest(List<Latest> latest) {
        this.latest = latest;
    }

    public QuestionsModel(List<Latest> latest) {

        this.latest = latest;
    }
}
