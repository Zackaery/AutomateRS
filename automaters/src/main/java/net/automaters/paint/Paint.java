package net.automaters.paint;
//
//import org.osbot.rs07.Bot;
//import org.osbot.rs07.canvas.paint.Painter;
//import org.osbot.rs07.input.mouse.BotMouseListener;
//import net.unethicalite.plugins.automaters.tasks.Task;
//import net.unethicalite.plugins.automaters.util.PaintUtil;
//import net.unethicalite.plugins.automaters.util.file_managers.FontManager;
//import net.unethicalite.plugins.automaters.util.file_managers.ImageManager;
//
//import java.awt.*;
//import java.awt.event.MouseEvent;
//
//public class Paint implements Painter {
//
////    private final SkillTracker skillTracker;
//    private final Color automateOrange = Color.decode("#ff4200");
//    private final Rectangle toggleButton = new Rectangle(435, 7, 76, 24);
//    private int offsetY;
//    private int offsetX;
//    private long lastCheckTime = -1;
//    private long elapsedTime = -1;
//    private Task currentTask;
//    private boolean paintHidden;
//    private boolean toggleButtonHovered;
//
//    private static final Image mouseImage = ImageManager.getInstance().loadImage("images/cursor.png");
//    private static final Image title = ImageManager.getInstance().loadImage("images/AutomateRS.png");
//
//    private boolean paused;
//
//    public Paint() {
//
//        bot.addMouseListener(new BotMouseListener() {
//            @Override
//            public void checkMouseEvent(final MouseEvent e) {
//                if (e.getID() == MouseEvent.MOUSE_CLICKED) {
//                    if (getToggleButton().contains(e.getPoint())) {
//                        paintHidden = !paintHidden;
//                    }
//                    e.consume();
//                } else if (e.getID() == MouseEvent.MOUSE_MOVED) {
//                    toggleButtonHovered = getToggleButton().contains(e.getPoint());
//                    e.consume();
//                }
//            }
//
//            @Override
//            public void mouseMoved(final MouseEvent e) {
//                checkMouseEvent(e);
//            }
//        });
//    }
//
//    public void pause() {
//        paused = true;
//    }
//
//    public void resume() {
//        paused = false;
//        lastCheckTime = System.currentTimeMillis();
//    }
//
//    public void setCurrentTask(final Task currentTask) {
//        this.currentTask = currentTask;
//    }
//
//    @Override
//    public void onPaint(Graphics2D g) {
//        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
//        if (!paused) {
//            drawMouse(g);
//        }
//        drawScriptInfo(g);
//    }
//
//    private void setOffset() {
//        offsetX = 0;
//        offsetY = bot.getCanvas().getHeight() - (502 - 337);
//    }
//
//    private Rectangle getToggleButton() {
//        return new Rectangle(offsetX + toggleButton.x, offsetY + toggleButton.y, toggleButton.width, toggleButton.height);
//    }
//
//    private void drawScriptInfo(Graphics2D g) {
//        setOffset();
//        if (!paintHidden) {
//            drawScriptInfoBackground(g);
//            drawTitle(g);
//            g.setFont(FontManager.ROBOTO_REGULAR);
//            drawRunTime(g);
//            drawTaskInfo(g);
//            drawActivityInfo(g);
////            drawSkillsInfo(g);
//        }
//        g.setFont(FontManager.ROBOTO_REGULAR);
//        drawTogglePaintButton(g);
//    }
//
//    private void drawScriptInfoBackground(Graphics2D g) {
//        g.setColor(Color.WHITE);
//        g.fillRect(offsetX, offsetY, 518, 143);
//        g.setColor(Color.decode("#1b1919"));
//        g.fillRect(offsetX + 2, offsetY + 2, 514, 139);
//    }
//
//    private void drawTitle(Graphics2D g) {
//        g.setColor(automateOrange);
//        g.setFont(g.getFont().deriveFont(20f));
//        g.drawImage(title, offsetX + 10, offsetY + 30, null);
////        g.drawString("Automate", offsetX + 10, offsetY + 30);
////        g.setColor(Color.WHITE);
////        g.drawString("RS", offsetX + 10 + g.getFontMetrics().stringWidth("Explv"), offsetY + 30);
//    }
//
//    private void drawRunTime(Graphics2D g) {
//        if (lastCheckTime == -1) {
//            lastCheckTime = System.currentTimeMillis();
//            elapsedTime = 0;
//        }
//
//        if (!paused) {
//            long currentTime = System.currentTimeMillis();
//            elapsedTime += currentTime - lastCheckTime;
//            lastCheckTime = currentTime;
//            elapsedTime += System.currentTimeMillis() - lastCheckTime;
//        }
//
//        g.drawString("Run time: " + PaintUtil.formatTime(elapsedTime), offsetX + 10, offsetY + 60);
//    }
//
//    private void drawTaskInfo(Graphics2D g) {
//        if (currentTask != null) {
//            g.drawString(currentTask.toString(), offsetX + 10, offsetY + 80);
//        }
//    }
//
//    private void drawActivityInfo(Graphics2D g) {
//        if (currentTask != null ) {
//            g.drawString("Activity: " + currentTask, offsetX + 10, offsetY + 100);
//        }
//    }
//
////    private void drawSkillsInfo(Graphics2D g) {
////        int x = offsetX + 10;
////        int y = offsetY + 120;
////        for (final Skill skill : skillTracker.getTrackedSkills()) {
////            String output = String.format("%s lvl %d (+%d lvls) +%s xp (%s xp / hr) (%s)",
////                    skill.toString(),
////                    skillTracker.getLevel(skill),
////                    skillTracker.getGainedLevels(skill),
////                    RSUnits.valueToFormatted(skillTracker.getGainedXP(skill)),
////                    RSUnits.valueToFormatted(skillTracker.getGainedXPPerHour(skill)),
////                    PaintUtil.formatTime(skillTracker.getTimeToNextLevel(skill))
////            );
////
////            g.drawString(output, x, y);
////            y += 20;
////        }
////    }
//
//    private void drawTogglePaintButton(Graphics2D g) {
//        String text = paintHidden ? "Maximise" : "Minimise";
//
//        if (toggleButtonHovered) {
//            g.setColor(automateOrange);
//        } else {
//            g.setColor(Color.WHITE);
//        }
//        Rectangle toggleButton = getToggleButton();
//        g.fill(toggleButton);
//
//        g.setColor(Color.decode("#1b1919"));
//        g.fillRect(toggleButton.x + 1, toggleButton.y + 1, toggleButton.width - 2, toggleButton.height - 2);
//
//        if (toggleButtonHovered) {
//            g.setColor(automateOrange);
//        } else {
//            g.setColor(Color.WHITE);
//        }
//        g.drawString(text, toggleButton.x + 10, toggleButton.y + (toggleButton.height / 2) + 5);
//    }
//
//    private void drawMouse(Graphics2D g) {
//        final Point mousePos = bot.getMethods().getMouse().getPosition();
//
//        if (mouseImage == null) {
//            g.setColor(Color.WHITE);
//            g.drawLine(mousePos.x - 5, mousePos.y - 5, mousePos.x + 5, mousePos.y + 5);
//            g.drawLine(mousePos.x - 5, mousePos.y + 5, mousePos.x + 5, mousePos.y - 5);
//        } else {
//            g.drawImage(mouseImage, mousePos.x, mousePos.y, null);
//        }
//    }
//}
