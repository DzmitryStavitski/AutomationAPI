package constants;

public class VkApiMethods {
    private static final String SEPARATOR = ".";

    private static final String WALL = "wall";
    private static final String PHOTOS = "photos";
    private static final String LIKES = "likes";

    public static final String WALL_POST = getMethod(WALL, "post");
    public static final String WALL_EDIT = getMethod(WALL, "edit");
    public static final String PHOTOS_GET_WALL_UPLOAD_SERVER = getMethod(PHOTOS, "getWallUploadServer");
    public static final String PHOTOS_SAVE_WALL_PHOTO = getMethod(PHOTOS, "saveWallPhoto");
    public static final String WALL_CREATE_COMMENT = getMethod(WALL, "createComment");
    public static final String LIKES_IS_LIKED = getMethod(LIKES, "isLiked");
    public static final String WALL_DELETE = getMethod(WALL, "delete");

    private static String getMethod(String type, String method) {
        return type + SEPARATOR + method;
    }
}
