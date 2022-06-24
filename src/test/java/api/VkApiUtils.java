package api;

import aquality.selenium.browser.AqualityServices;
import aquality.selenium.core.utilities.ISettingsFile;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import constants.VkApiMethods;
import models.UploadServer;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import utils.TestDataUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class VkApiUtils {
    private final static String URL;
    private final static String token;
    private final static String v;

    static {
        URL = AqualityServices.get(ISettingsFile.class).getValue("/api.url").toString();
        token = TestDataUtils.getValue("token");
        v = TestDataUtils.getValue("v");
    }

    private static URIBuilder returnBuilderWithCommonParameters(String method) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(method);
        uriBuilder.addParameter("access_token", token);
        uriBuilder.addParameter("v", v);

        return uriBuilder;
    }

    public static int makePost(String content) {
        try {
            URIBuilder uriBuilder = returnBuilderWithCommonParameters(VkApiMethods.WALL_POST);
            uriBuilder.addParameter("message", content);

            String request = uriBuilder.build().toString();
            String responseContent = EntityUtils.toString(execute(request).getEntity());
            JsonObject jsonObject = new Gson()
                    .fromJson(responseContent, JsonObject.class)
                    .getAsJsonObject("response");
            return jsonObject.get("post_id").getAsInt();
        } catch (IOException | URISyntaxException e) {
            AqualityServices.getLogger().error(e.getMessage());
        }

        return -1;
    }

    public static int editPost(int id, String content, File image) {
        try {
            URIBuilder uriBuilder = returnBuilderWithCommonParameters(VkApiMethods.WALL_EDIT);
            uriBuilder.addParameter("post_id", Integer.toString(id));
            uriBuilder.addParameter("message", content);
            uriBuilder.addParameter("attachments", saveWallPhoto(image));

            String responseContent = EntityUtils.toString(execute(uriBuilder.build().toString()).getEntity());
            JsonObject jsonObject = new Gson()
                    .fromJson(responseContent, JsonObject.class)
                    .getAsJsonObject("response");
            return jsonObject.get("post_id").getAsInt();
        } catch (IOException | URISyntaxException e) {
            AqualityServices.getLogger().error(e.getMessage());
        }

        return -1;
    }

    private static UploadServer getWallUploadServer() {
        try {
            URIBuilder uriBuilder = returnBuilderWithCommonParameters(VkApiMethods.PHOTOS_GET_WALL_UPLOAD_SERVER);
            uriBuilder.addParameter("user_id", TestDataUtils.getValue("id"));

            String responseContent = EntityUtils.toString(execute(uriBuilder.build().toString()).getEntity());

            JsonObject jsonObject = new Gson()
                    .fromJson(responseContent, JsonObject.class)
                    .getAsJsonObject("response");

            String uploadUrl = jsonObject.get("upload_url").getAsString();
            int albumId = jsonObject.get("album_id").getAsInt();
            int userId = jsonObject.get("user_id").getAsInt();

            return new UploadServer(uploadUrl, albumId, userId);

        } catch (IOException | URISyntaxException e) {
            AqualityServices.getLogger().error(e.getMessage());
        }

        return null;
    }

    protected static String saveWallPhoto(File file) {
        HttpEntity entity = MultipartEntityBuilder.create().addBinaryBody("photo", file).build();
        HttpPost post = new HttpPost(getWallUploadServer().getUploadUrl());
        post.setEntity(entity);
        String responseContent = execute(post);

        JsonObject jsonObject = new Gson()
                .fromJson(responseContent, JsonObject.class);

        String server = jsonObject.get("server").getAsString();
        String photo = jsonObject.get("photo").getAsString();
        String hash = jsonObject.get("hash").getAsString();

        try {
            URIBuilder uriBuilder = returnBuilderWithCommonParameters(VkApiMethods.PHOTOS_SAVE_WALL_PHOTO);
            uriBuilder.addParameter("user_id", TestDataUtils.getValue("id"));
            uriBuilder.addParameter("photo", photo);
            uriBuilder.addParameter("server", server);
            uriBuilder.addParameter("hash", hash);

            String responseBody = EntityUtils.toString(execute(uriBuilder.build().toString()).getEntity());
            System.out.println(responseBody);
            JsonObject json1 = new Gson()
                    .fromJson(responseBody, JsonObject.class);
            JsonArray jsonArray = json1.getAsJsonArray("response");
            String photoId = jsonArray.get(0).getAsJsonObject().get("id").getAsString();
            String ownerId = jsonArray.get(0).getAsJsonObject().get("owner_id").getAsString();

            return "photo" + ownerId + "_" + photoId;
        } catch (IOException | URISyntaxException e) {
            AqualityServices.getLogger().error(e.getMessage());
            return null;
        }
    }

    public static void createReply(int postId, String message) {
        try {
            URIBuilder uriBuilder = returnBuilderWithCommonParameters(VkApiMethods.WALL_CREATE_COMMENT);
            uriBuilder.addParameter("post_id", Integer.toString(postId));
            uriBuilder.addParameter("message", message);

            execute(uriBuilder.build().toString());
        } catch (Exception e) {
            AqualityServices.getLogger().error(e.getMessage());
        }
    }

    public static boolean getIsLiked(int postId) {
        try {
            URIBuilder uriBuilder = returnBuilderWithCommonParameters(VkApiMethods.LIKES_IS_LIKED);
            uriBuilder.addParameter("type", "post");
            uriBuilder.addParameter("item_id", Integer.toString(postId));

            String responseContent = EntityUtils.toString(execute(uriBuilder.build().toString()).getEntity());
            JsonObject jsonObject = new Gson()
                    .fromJson(responseContent, JsonObject.class)
                    .getAsJsonObject("response");
            return jsonObject.get("liked").getAsInt() == 1;
        } catch (IOException | URISyntaxException e) {
            AqualityServices.getLogger().error(e.getMessage());
        }

        return false;
    }

    public static boolean deletePost(int postId) {
        try {
            URIBuilder uriBuilder = returnBuilderWithCommonParameters(VkApiMethods.WALL_DELETE);
            uriBuilder.addParameter("post_id", Integer.toString(postId));

            String responseContent = EntityUtils.toString(execute(uriBuilder.build().toString()).getEntity());
            JsonObject jsonObject = new Gson()
                    .fromJson(responseContent, JsonObject.class);
            return jsonObject.get("response").getAsInt() == 1;
        } catch (IOException | URISyntaxException e) {
            AqualityServices.getLogger().error(e.getMessage());
        }

        return false;
    }

    private static HttpResponse execute(String req) throws IOException {
        AqualityServices.getLogger().info("Build request and send it: " + req);
        return Request.Get(URL + req)
                .execute()
                .returnResponse();
    }

    private static String execute(HttpPost httpPost) {
        String body = "";
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            body = EntityUtils.toString(client.execute(httpPost).getEntity());
        } catch (IOException e) {
            AqualityServices.getLogger().error(e.getMessage());
        }
        return body;
    }
}
