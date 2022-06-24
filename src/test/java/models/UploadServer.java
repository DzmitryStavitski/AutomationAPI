package models;

public class UploadServer {
    private String upload_url;
    private int album_id;
    private int user_id;

    public UploadServer(String upload_url, int album_id, int user_id) {
        this.upload_url = upload_url;
        this.album_id = album_id;
        this.user_id = user_id;
    }

    public String getUploadUrl() {
        return upload_url;
    }

    public void setUploadUrl(String upload_url) {
        this.upload_url = upload_url;
    }

    public int getAlbumId() {
        return album_id;
    }

    public void setAlbumId(int album_id) {
        this.album_id = album_id;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }
}
