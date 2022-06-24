package models;

public class Comment {
    private int authorId;
    private String message;

    public Comment(int authorId, String message) {
        this.authorId = authorId;
        this.message = message;
    }

    public int getAuthorId() {
        return authorId;
    }

    public String getMessage() {
        return message;
    }
}
