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
}