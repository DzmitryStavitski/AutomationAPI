package utils;

import aquality.selenium.core.utilities.ISettingsFile;
import aquality.selenium.core.utilities.JsonSettingsFile;

public class TestDataUtils {
    public static String getValue(String key) {
        ISettingsFile environment = new JsonSettingsFile("testUserInfo.json");
        return environment.getValue("/" + key).toString();
    }
}
