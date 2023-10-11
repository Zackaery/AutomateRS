package net.automaters.api.utils.timers;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GlobalTimers {
    public static Timer timer1;
    public static Timer timer2;

    public static void startTimers() {
        // Timer 1: Executes every 1000 milliseconds (1 second)
        timer1 = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Timer 1 tick!");
                // Add your timer 1 logic here
            }
        });
        timer1.start();

        // Timer 2: Executes every 2000 milliseconds (2 seconds)
        timer2 = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Timer 2 tick!");
                // Add your timer 2 logic here
            }
        });
        timer2.start();
    }

    public static void stopTimers() {
        if (timer1 != null) {
            timer1.stop();
        }
        if (timer2 != null) {
            timer2.stop();
        }
    }

    // Add getter methods for timer1 and timer2
    public static Timer getTimer1() {
        return timer1;
    }

    public static Timer getTimer2() {
        return timer2;
    }
}
