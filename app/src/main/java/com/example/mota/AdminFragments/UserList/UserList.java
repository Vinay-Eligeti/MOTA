package com.example.mota.AdminFragments.UserList;

public class UserList {
    private String name;
    private String rollno;

    public UserList(String name, String rollno){
        this.name = name;
        this.rollno = rollno;

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

}
