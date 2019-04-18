package kumarworld.rahul.checklist.data;

import java.io.Serializable;

public class Question implements Serializable {

    String mQueId,mHeading,mQue,mRemark,mCheckingStatus;


    public Question(String mQueId, String mHeading, String mQue, String mRemark, String mCheckingStatus) {
        this.mQueId = mQueId;
        this.mHeading = mHeading;
        this.mQue = mQue;
        this.mRemark = mRemark;
        this.mCheckingStatus = mCheckingStatus;
    }

    public String getmQueId() {
        return mQueId;
    }

    public void setmQueId(String mQueId) {
        this.mQueId = mQueId;
    }

    public String getmHeading() {
        return mHeading;
    }

    public void setmHeading(String mHeading) {
        this.mHeading = mHeading;
    }

    public String getmQue() {
        return mQue;
    }

    public void setmQue(String mQue) {
        this.mQue = mQue;
    }

    public String getmRemark() {
        return mRemark;
    }

    public void setmRemark(String mRemark) {
        this.mRemark = mRemark;
    }

    public String getmCheckingStatus() {
        return mCheckingStatus;
    }

    public void setmCheckingStatus(String mCheckingStatus) {
        this.mCheckingStatus = mCheckingStatus;
    }
}
