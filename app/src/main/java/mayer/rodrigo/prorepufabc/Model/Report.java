package mayer.rodrigo.prorepufabc.Model;

public class Report {

    private User user;
    private String title, description;
    private int upvotes;
    private long timestamp;


    public Report(User user, String title, String description, int upvotes, long timestamp) {
        this.title = title;
        this.description = description;
        this.upvotes = upvotes;
        this.timestamp = timestamp;
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
}
