package forms;

import models.Comment;
import models.Post;
import aquality.selenium.elements.ElementType;
import aquality.selenium.elements.interfaces.IButton;
import aquality.selenium.elements.interfaces.ILabel;
import aquality.selenium.elements.interfaces.ILink;
import aquality.selenium.forms.Form;
import org.openqa.selenium.By;
import utils.ImageUtils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostForm extends Form {
    private static final String POST_MESSAGE = "//div[contains(@class, 'wall_post_text')]";
    private static final String POST_LOGIN = "//*[@class='author']";
    private static final String POST_PHOTO = "//a[contains(@class, 'page_post_thumb_wrap')]";
    private static final String AUTHOR_ID = "//*[@class='post_author']//a";
    private static final String REPLY_AUTHOR = "//*[@class='reply_author']//a";
    private static final String REPLIES = "//div[contains(@class, 'replies_list _replies_list')]";
    private static final String messageLoc = "//div[contains(@class, 'wall_reply_text')]";
    private static final String showRepliesLoc = "//a[contains(@class, 'replies_next  replies_next_main')]";

    private final IButton btnLike = getFormLabel().findChildElement(By.className("like_button_icon"), "Like", ElementType.BUTTON);
    private final ILabel lblMessage = getFormLabel().findChildElement(By.xpath(messageLoc), "Message", ElementType.LABEL);
    private final ILabel messageLabel = getFormLabel().findChildElement(By.xpath(POST_MESSAGE), "Text from the post", ElementType.LABEL);
    private final ILabel authorLoginLabel = getFormLabel().findChildElement(By.xpath(POST_LOGIN), "Author of the post", ElementType.LABEL);
    private final IButton btnShowReplies = getFormLabel().findChildElement(By.xpath(showRepliesLoc), "Show comments", ElementType.BUTTON);


    public PostForm(String id) {
        super(By.id(id), String.format("Post (id = %s)", id));
    }

    public Post getPost() {
        Post post = new Post(getFormLabel().getAttribute("id"), messageLabel.getText(), authorLoginLabel.getText(),
                getFormLabel().findChildElement(By.xpath(AUTHOR_ID), ElementType.LINK).getAttribute("href"));

        if (getFormLabel().findChildElements(By.xpath(POST_PHOTO), "Photo link from the post", ElementType.LINK).size() > 0) {
            ILink link = getFormLabel().findChildElement(By.xpath(POST_PHOTO), "Photo link from the post", ElementType.LINK);
            Pattern pattern = Pattern.compile("https(.+)album");
            Matcher matcher = pattern.matcher(link.getAttribute("style"));
           if(matcher.find()) {
               post.setImg(ImageUtils.getImageFromUrl(matcher.group(0)));
           }
        }

        if(getFormLabel().findChildElements(By.xpath(REPLIES), "Comments from the post", ElementType.LABEL).size() > 0) {
            post.setComments(getComments());
        }

        return post;
    }

    public ArrayList<Comment> getComments() {
        ArrayList<Comment> comments = new ArrayList<>();
        showReplies();
        int authorId = Integer.parseInt(getFormLabel().findChildElement(By.xpath(REPLY_AUTHOR), ElementType.LINK).getAttribute("data-from-id"));
        String message = lblMessage.getText();
        comments.add(new Comment(authorId, message));
        return comments;
    }

    public void showReplies() {
        if (btnShowReplies.state().isExist()) {
            btnShowReplies.click();
        }
    }

    public void clickLike() {
        btnLike.click();
    }
}
