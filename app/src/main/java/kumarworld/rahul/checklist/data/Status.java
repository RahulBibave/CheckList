package kumarworld.rahul.checklist.data;

import java.io.Serializable;

public class Status implements Serializable {
    String remark,status,userName,date,role,location,image,checklist_id,question_id,flat_no,building_id,project_id;


    public Status(String remark, String status, String userName, String date, String role, String location, String image, String checklist_id, String question_id, String flat_no, String building_id, String project_id) {
        this.remark = remark;
        this.status = status;
        this.userName = userName;
        this.date = date;
        this.role = role;
        this.location = location;
        this.image = image;
        this.checklist_id = checklist_id;
        this.question_id = question_id;
        this.flat_no = flat_no;
        this.building_id = building_id;
        this.project_id = project_id;
    }


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

    public String getChecklist_id() {
        return checklist_id;
    }

    public void setChecklist_id(String checklist_id) {
        this.checklist_id = checklist_id;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getFlat_no() {
        return flat_no;
    }

    public void setFlat_no(String flat_no) {
        this.flat_no = flat_no;
    }

    public String getBuilding_id() {
        return building_id;
    }

    public void setBuilding_id(String building_id) {
        this.building_id = building_id;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }
}
