package forms;

import aquality.selenium.browser.AqualityServices;
import aquality.selenium.elements.ElementType;
import aquality.selenium.elements.interfaces.IElement;
import aquality.selenium.forms.Form;
import org.openqa.selenium.By;
import utils.TestDataUtils;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class WallForm extends Form {
    private static final String POSTS_CONTAINER = "//div[contains(@class, '_post post page_block')]";

    public WallForm() {
        super(By.xpath("//div[@id='page_wall_posts']"), "Post wall");
    }

    public ArrayList<PostForm> getAllPostForms() {
        ArrayList<PostForm> posts = new ArrayList<>();
        for (IElement element : AqualityServices.getElementFactory().findElements(By.xpath(POSTS_CONTAINER), ElementType.LABEL)) {
            posts.add(new PostForm(element.getAttribute("id")));
        }
        return posts;
    }

    public PostForm getPostWithText(String text) {
        for (PostForm postForm : getAllPostForms()) {
            if (postForm.getPost().getMessage().equals(text))
                return postForm;
        }
        return null;
    }

    public PostForm getPostWithId(int id) {
        for (PostForm postForm : getAllPostForms()) {
            if (postForm.getPost().getId().equals("post" + TestDataUtils.getValue("id") + "_" + id))
                return postForm;
        }
        return null;
    }

    public boolean hasPost(int id) {
       return !getFormLabel().findChildElements(By.id("post" + TestDataUtils.getValue("id") + "_" + id), ElementType.LABEL)
               .isEmpty();
    }

    public static void waitForDeletePost(int id) {
        try {
            AqualityServices.getConditionalWait().waitForTrue(() -> AqualityServices
                    .getBrowser()
                    .getDriver()
                    .findElementsById("post" + TestDataUtils.getValue("id") + "_" + id)
                    .get(0).isDisplayed());
        } catch (TimeoutException e) {
            AqualityServices.getLogger().error(e.getMessage());
        }
    }

    public static void waitForNewPostDisplayed() {
        try {
            AqualityServices.getConditionalWait().waitForTrue(() -> AqualityServices
                    .getBrowser()
                    .getDriver()
                    .findElements(By.xpath(POSTS_CONTAINER))
                    .get(0).isDisplayed());
        } catch (TimeoutException e) {
            AqualityServices.getLogger().error(e.getMessage());
        }
    }
}
