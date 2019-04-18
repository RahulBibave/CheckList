package kumarworld.rahul.checklist;

import java.io.Serializable;

public class Form implements Serializable {
    String mQuation;
    String mID;

    public Form(String mQuation, String mID) {
        this.mQuation = mQuation;
        this.mID = mID;
    }

    public String getmQuation() {
        return mQuation;
    }

    public void setmQuation(String mQuation) {
        this.mQuation = mQuation;
    }

    public String getmID() {
        return mID;
    }

    public void setmID(String mID) {
        this.mID = mID;
    }
}
