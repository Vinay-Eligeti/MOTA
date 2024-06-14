package com.example.mota.UserFragments.TestSchedules;

public class TestList {
    private String time;
    private String examName;
    private String status;
    private String subjectName;

    private String documentId;

    public TestList(String time, String examName, String subjectName, String status, String documentId) {
        this.time = time;
        this.examName = examName;
        this.status = status;
        this.subjectName = subjectName;
        this.documentId = documentId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
