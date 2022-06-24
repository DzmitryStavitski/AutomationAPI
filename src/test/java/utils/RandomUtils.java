package utils;

import aquality.selenium.browser.AqualityServices;
import aquality.selenium.core.utilities.ISettingsFile;
import me.xdrop.jrand.JRand;

public class RandomUtils {
    public static String randomString(String parameter) {
        String random = JRand.sentence().words(Integer.parseInt(AqualityServices
                .get(ISettingsFile.class)
                .getValue("/" + parameter)
                .toString()))
                .gen();
        return random;
    }
}