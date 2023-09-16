package net.automaters.script;

import lombok.experimental.Delegate;
import net.automaters.gui.GUI;
import net.automaters.gui.utils.EventDispatchThreadRunner;
import net.automaters.util.file_managers.ImageManager;
import net.runelite.api.Client;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.PluginChanged;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.IconTextField;
import net.runelite.client.ui.components.ToggleButton;
import net.runelite.client.util.ImageUtil;
import net.unethicalite.api.entities.Players;

import javax.inject.Inject;
import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static net.automaters.gui.GUI.*;
import static net.automaters.gui.GUI.started;
import static net.automaters.script.AutomateRS.debug;
import static net.automaters.script.AutomateRS.scriptStarted;

public class AutomateRSPanel extends PluginPanel {
    @Delegate
    private final AutomateRS plugin;

    private final JPanel container = new JPanel();

    private final JLabel titleLabel;
    private final JLabel usernameLabel;
    private final JLabel passwordLabel;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final ToggleButton selectWorld;
    private final JSpinner selectedWorld;
    private final JLabel worldLabel;
    private final ToggleButton saveLastWorld;

    private final JButton startButton;
    private final JButton stopButton;
    private final JButton pauseButton;

    static boolean selectWorldBool;

    private GUI GUI;

//    private final boolean selectWorld;
//    private final JSpinner selectedWorld;
//    private final boolean saveLastWorld;
//    private final boolean completeWelcomeScreen;

    @Inject
    public AutomateRSPanel(AutomateRS plugin) {
        this.plugin = plugin;
//
//        setBackground(ColorScheme.DARKER_GRAY_COLOR);

        titleLabel = new JLabel();
        setImage("images/panel/AutomateRS.png", titleLabel);
        titleLabel.setBounds(10, 10, PANEL_WIDTH, titleLabel.getHeight());
        debug(String.valueOf(titleLabel.getBounds()));


        usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(10, 200, PANEL_WIDTH, 30);
        usernameField = new JTextField();
        usernameField.setBounds(10, 234, PANEL_WIDTH, 30);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 274, PANEL_WIDTH, 30);
        passwordField = new JPasswordField();
        passwordField.setBounds(10, 308, PANEL_WIDTH, 30);

        selectWorld = new ToggleButton(": Select world");
        selectWorld.setBounds(10, 348, PANEL_WIDTH, 30);

        worldLabel = new JLabel("World:");
        worldLabel.setBounds(10, 388, 150, 30);
        SpinnerModel model = new SpinnerNumberModel(301, 301, 578, 1);
        selectedWorld = new JSpinner(model);
        selectedWorld.setBounds(150, 388, 75, 30);

        saveLastWorld = new ToggleButton(": Save last world");
        saveLastWorld.setBounds(10, 428, PANEL_WIDTH, 30);

        startButton = new JButton("Start");
        startButton.setBounds(62, 488, 100, 30);

        pauseButton = new JButton("Pause");
        pauseButton.setBounds(10, 488, 100, 30);
        stopButton = new JButton("Stop");
        stopButton.setBounds(115, 488, 100, 30);


        add(titleLabel);
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(selectWorld);
        add(startButton);

        selectWorld.addActionListener(e -> {
            if (!selectWorldBool) {
                add(worldLabel);
                add(selectedWorld);
                add(saveLastWorld);
                selectWorldBool = true;
                repaint();
                revalidate();
            } else {
                remove(worldLabel);
                remove(selectedWorld);
                remove(saveLastWorld);
                selectWorldBool = false;
                repaint();
                revalidate();
            }
        });

        startButton.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                var local = Players.getLocal();
                if (local == null) {
                    debug("Local Player not located");
                    return;
                }
                if (!started) {
                    selectedBuild = loadBuildFromGUI();
                    remove(startButton);
                    add(pauseButton);
                    add(stopButton);
                    repaint();
                    revalidate();
                } else {
                    scriptStarted = true;
                    remove(startButton);
                    add(pauseButton);
                    add(stopButton);
                    repaint();
                    revalidate();
                    debug("Started - AutomateRS");
                }
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                startButton.setBackground(new Color(60, 60, 60));
                startButton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                startButton.setBackground(null);
                startButton.repaint();
            }
        });

        pauseButton.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                scriptStarted = false;
                remove(pauseButton);
                startButton.setBounds(10, 488, 100, 30);
                add(startButton);
                add(stopButton);
                repaint();
                revalidate();
                debug("Paused - AutomateRS");
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                pauseButton.setBackground(new Color(60, 60, 60));
                pauseButton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                pauseButton.setBackground(null);
                pauseButton.repaint();
            }
        });

        stopButton.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                started = false;
                scriptStarted = false;
                remove(pauseButton);
                remove(stopButton);
                startButton.setBounds(62, 488, 100, 30);
                add(startButton);
                repaint();
                revalidate();
                debug("Stopped - AutomateRS");
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                stopButton.setBackground(new Color(60, 60, 60));
                stopButton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                stopButton.setBackground(null);
                stopButton.repaint();
            }
        });
    }

    @Override
    public void onActivate()
    {
        super.onActivate();
        titleLabel.requestFocusInWindow();
    }

    private String loadBuildFromGUI() {
        try {
            EventDispatchThreadRunner.runOnDispatchThread(() -> {
                try {
                    GUI = new GUI();
                    GUI.open();
                    debug("Launching AutomateRS - GUI");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }, true);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (started) {
            debug("SELECTED BUILD = " + selectedBuild);
            return selectedBuild;
        } else {
            return null;
        }
    }


    @Subscribe
    private void onPluginChanged(PluginChanged e)
    {
        if (e.getPlugin() == plugin)
        {
        }
    }
}
