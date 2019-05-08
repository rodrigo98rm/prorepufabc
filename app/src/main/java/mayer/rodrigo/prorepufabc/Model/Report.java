package mayer.rodrigo.prorepufabc.Model;

import java.util.ArrayList;

public class Report {

    private String id;
    private User user;
    private String title, description;
    private int upvotes;
    private long timestamp;
    private ArrayList<String> imgs;
    private ArrayList<String> resolvedUsers;
    private double latitude, longitude;

    public Report() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public ArrayList<String> getImgs() {
        return imgs;
    }

    public ArrayList<String> getResolvedUsers() {
        return resolvedUsers;
    }
}
