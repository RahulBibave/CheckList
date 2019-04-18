package kumarworld.rahul.checklist.data;

import java.io.Serializable;

public class Status implements Serializable {
    String remark,status,userName,date,role,location,image;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Status(String remark, String status, String userName, String date, String role, String location, String image) {
        this.remark = remark;
        this.status = status;
        this.userName = userName;
        this.date = date;
        this.role = role;
        this.location = location;
        this.image = image;
    }
}
