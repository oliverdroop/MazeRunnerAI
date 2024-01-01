package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;

public class MovementHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovementHandler.class);

    private static int move(BufferedImage image, Brain brain) {
        int[] coordinates = brain.chooseDirection();
        coordinates[0] += brain.getPosition().getX();
        coordinates[1] += brain.getPosition().getY();
        brain.setPosition(coordinates[0], coordinates[1]);
        return PixelSensor.sense(image, coordinates);
    }

    public static int escapeOrDie(BufferedImage image, Brain brain) {
        // While the brain is alive, it makes decisions on where to move
        int liveness = 1;
        int rewardValue = 0;
        while (liveness > 0) {
            // Move the brain
            liveness = 1 - move(image, brain);
            LOGGER.debug("[{}, {}]", brain.getPosition().getX(), brain.getPosition().getY());
            if (liveness > 0) {
                // Increment the reward if the brain has not hit a wall
                rewardValue++;
            } else if (liveness == -1) {
                // Give a big reward for escaping the maze
                rewardValue = Integer.MAX_VALUE - rewardValue;
                LOGGER.info("A brain escaped the maze");
                brain.getDecisionNeurons()
                        .forEach(decisionNeuron -> decisionNeuron.getWeighting()
                                .forEach(weighting -> LOGGER.info("Weighting: {}", weighting)));
                brain.getHistory().forEach(position -> LOGGER.info("[{}, {}]", position.getX(), position.getY()));
            }
        }
        return rewardValue;
    }
}
