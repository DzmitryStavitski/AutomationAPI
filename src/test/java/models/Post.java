package models;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Post {
    private String id;
    private String message;
    private String authorName;
    private String authorId;
    private BufferedImage img;
    private ArrayList<Comment> comments;

    public Post(String id, String message, String authorName, String authorId) {
        this.id = id;
        this.message = message;
        this.authorName = authorName;
        this.authorId = authorId;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorId() {
        return authorId;
    }
}
