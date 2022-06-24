package tests;

import models.Post;
import api.VkApiUtils;
import aquality.selenium.browser.AqualityServices;
import aquality.selenium.core.utilities.ISettingsFile;
import constants.AssertMessages;
import forms.NavigateBarForm;
import forms.WallForm;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.RegistrationPage;
import utils.ImageUtils;
import utils.RandomUtils;
import utils.TestDataUtils;

import java.io.File;

public class TestSuite extends BaseTest {
    @Test
    public void test() {
        // STEP 1
        browser.goTo((String) AqualityServices.get(ISettingsFile.class).getValue("/url"));
        browser.waitForPageToLoad();

        // STEP 2
        RegistrationPage registrationPage = new RegistrationPage();
        registrationPage.getLoginForm().login(TestDataUtils.getValue("login"), TestDataUtils.getValue("password"));

        // STEP 3
        NavigateBarForm navigateBar = new NavigateBarForm();
        navigateBar.goToProfile();
        browser.waitForPageToLoad();

        // STEP 4
        String text = RandomUtils.randomString("max.random.length");
        int postId = VkApiUtils.makePost(text);

        // STEP 5
        WallForm wall = new WallForm();
        WallForm.waitForNewPostDisplayed();
        Post post = wall.getPostWithId(postId).getPost();
        Assert.assertNotNull(post, AssertMessages.POST_NOT_FOUND_ERROR);
        Assert.assertEquals(TestDataUtils.getValue("full.id"), post.getAuthorId(), AssertMessages.POST_AUTHOR_ERROR);

        // STEP 6
        String newText = RandomUtils.randomString("max.random.length");
        VkApiUtils.editPost(postId, newText, new File("src/test/resources/testImage.jpg"));

        // STEP 7
        Post editedPost = wall.getPostWithText(newText).getPost();
        Assert.assertEquals(newText, editedPost.getMessage(), AssertMessages.POST_TEXT_ERROR);
        Assert.assertTrue(ImageUtils.compareImages(editedPost.getImg(), ImageUtils.getImageFromTheFile("src/test/resources/testImage.jpg")),
                AssertMessages.IMAGE_ERROR);

        // STEP 8
        String comment = RandomUtils.randomString("max.random.length");
        VkApiUtils.createReply(postId, comment);

        // STEP 9
        Post postWithComment = wall.getPostWithId(postId).getPost();
        Assert.assertEquals(postWithComment.getAuthorId(), TestDataUtils.getValue("full.id"), AssertMessages.COMMENT_AUTHOR_ERROR);
        Assert.assertEquals(postWithComment.getComments().get(0).getMessage(), comment, AssertMessages.COMMENT_MESSAGE_ERROR);

        // STEP 10
        wall.getPostWithId(postId).clickLike();

        // STEP 11
        Assert.assertTrue(VkApiUtils.getIsLiked(postId), AssertMessages.LIKE_ERROR);

        // STEP 12
        VkApiUtils.deletePost(postId);

        // STEP 13
        WallForm.waitForDeletePost(postId);
        Assert.assertFalse(wall.hasPost(postId), AssertMessages.DELETE_POST_ERROR);
    }
}
