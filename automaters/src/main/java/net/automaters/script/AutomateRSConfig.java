package net.automaters.script;

import net.runelite.client.config.Button;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("automaters")
public interface AutomateRSConfig extends Config
{
	@ConfigItem(
			keyName = "start",
			name = "Start - AutomateRS",
			description = "Start button",
			position = 0
	)
	default Button startButton()
	{
		return new Button();
	}

	@ConfigItem(
			keyName = "pause",
			name = "Pause - AutomateRS",
			description = "Pause button",
			position = 1
	)
	default Button pauseButton()
	{
		return new Button();
	}

	@ConfigItem(
			keyName = "stop",
			name = "Stop - AutomateRS",
			description = "Stop button",
			position = 2
	)
	default Button stopButton()
	{
		return new Button();
	}
}
