package net.automaters.script;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("automaters")
public interface AutomateRSConfig extends Config
{
    @ConfigItem(
            keyName = "profilesData",
            name = "",
            description = "",
            hidden = true
    )
    default String profilesData()
    {
        return "";
    }

    @ConfigItem(
            keyName = "profilesData",
            name = "",
            description = ""
    )
    void profilesData(String str);

    @ConfigItem(
            keyName = "salt",
            name = "",
            description = "",
            hidden = true
    )
    default String salt()
    {
        return "";
    }

    @ConfigItem(
            keyName = "salt",
            name = "",
            description = ""
    )
    void salt(String key);

    @ConfigItem(
            keyName = "rememberPassword",
            name = "Remember Password",
            description = "Remembers passwords for accounts",
            position = 0
    )
    default boolean rememberPassword()
    {
        return true;
    }

    @ConfigItem(
            keyName = "displayEmailAddress",
            name = "Display email field",
            description = "Displays the email address field",
            position = 1
    )
    default boolean displayEmailAddress()
    {
        return false;
    }

    @ConfigItem(
            keyName = "streamerMode",
            name = "Hide email addresses",
            description = "Hides your account emails",
            position = 2,
            hidden = true,
            unhide = "displayEmailAddress"
    )
    default boolean streamerMode()
    {
        return false;
    }

    @ConfigItem(
            keyName = "switchPanel",
            name = "Auto-open Panel",
            description = "Automatically switch to the account switcher panel on the login screen",
            position = 3
    )
    default boolean switchPanel()
    {
        return false;
    }

    @ConfigItem(
            keyName = "auth",
            name = "Authenticator",
            description = "Authenticator",
            secret = true,
            position = 2
    )
    default String auth()
    {
        return "Authenticator";
    }


    @ConfigItem(
            keyName = "welcomeScreen",
            name = "Complete Welcome screen",
            description = "Automatically presses the 'Click here to Play' button after login",
            position = 6,
            hidden = true
    )
    default boolean welcomeScreen()
    {
        return true;
    }
}