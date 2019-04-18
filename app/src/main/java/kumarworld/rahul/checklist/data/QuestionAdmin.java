package kumarworld.rahul.checklist.data;

import java.io.Serializable;

public class QuestionAdmin implements Serializable {
    String que_id,heading,que;

    public QuestionAdmin(String que_id, String heading, String que) {
        this.que_id = que_id;
        this.heading = heading;
        this.que = que;
    }

    public String getQue_id() {
        return que_id;
    }

    public void setQue_id(String que_id) {
        this.que_id = que_id;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getQue() {
        return que;
    }

    public void setQue(String que) {
        this.que = que;
    }
}
