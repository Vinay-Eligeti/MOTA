package com.example.mota.AdminFragments.TestScheduleAdmin;

import com.google.firebase.Timestamp;

public class TestList {

    Timestamp created_timestamp;
    String examName;
    String instructions;

    public TestList() {
    }


    public Timestamp getCreated_timestamp() {
        return created_timestamp;
    }

    public void setCreated_timestamp(Timestamp created_timestamp) {
        this.created_timestamp = created_timestamp;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

}