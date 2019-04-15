package mayer.rodrigo.prorepufabc.Model;

import java.util.ArrayList;

public class Report {

    private User user;
    private String title, description;
    private int upvotes;
    private long timestamp;
    private ArrayList<String> photosUrls;


    public Report(User user, String title, String description, int upvotes, long timestamp, ArrayList<String> photosUrls) {
        this.user = user;
        this.title = title;
        this.description = description;
        this.upvotes = upvotes;
        this.timestamp = timestamp;
        this.photosUrls = photosUrls;
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

    public ArrayList<String> getPhotosUrls() {
        return photosUrls;
    }
}
