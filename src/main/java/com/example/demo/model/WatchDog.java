package com.example.demo.model;

import javax.persistence.*;

@Entity
@Table(name = "watch_dog")
public class WatchDog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //Path,Date,Time,FileName,Action

    private String path;

    private String time;

    @Column(name = "file_name")
    private String fileName;

    private String action;

    private String date;


    public WatchDog() {
    }

    public WatchDog(String path, String time, String fileName, String action, String date) {
        this.path = path;
        this.time = time;
        this.fileName = fileName;
        this.action = action;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
