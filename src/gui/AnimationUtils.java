package gui;

import java.awt.IllegalComponentStateException;

import javax.swing.JFrame;
import javax.swing.Timer;

public final class AnimationUtils {

    private AnimationUtils() {
    }

    public static void showWithFade(JFrame frame) {
        try {
            frame.setOpacity(0.0f);
            frame.setVisible(true);

            Timer fadeTimer = new Timer(25, null);
            final float[] opacity = {0.0f};

            fadeTimer.addActionListener(e -> {
                opacity[0] += 0.1f;

                if (opacity[0] >= 1.0f) {
                    frame.setOpacity(1.0f);
                    fadeTimer.stop();
                } else {
                    frame.setOpacity(opacity[0]);
                }
            });

            fadeTimer.start();
        } catch (UnsupportedOperationException | IllegalComponentStateException e) {
            frame.setVisible(true);
        }
    }
}