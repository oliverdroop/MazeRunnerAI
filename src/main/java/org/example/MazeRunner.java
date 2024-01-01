package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class MazeRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(MazeRunner.class);

    public static void main(String[] args) {
        final BufferedImage image = loadImage();
        final int maxCycles = 256;
        final int brainCountPerCycle = 256;
        final List<Brain> brains = new ArrayList<>();
        for (int brainNumber = 0; brainNumber < brainCountPerCycle; brainNumber++) {
            Position position = new Position(1, 1);
            brains.add(BrainFactory.createRandomBrain(image, position));
        }

        int cycleNumber = 0;
        while(cycleNumber < maxCycles) {
            LOGGER.info("Beginning cycle {} with {} new brains", cycleNumber, brainCountPerCycle);
            // Make a place to store data for comparing brain results
            Map<Brain, Integer> rewardMap = new HashMap<>();

            for(int brainNumber = 0; brainNumber < brains.size(); brainNumber++) {
                Brain brain = brains.get(brainNumber);
                LOGGER.debug("Brain {} is setting off", brainNumber);
                LOGGER.debug("[{}, {}]", brain.getPosition().getX(), brain.getPosition().getY());

                int rewardValue;
                try {
                    // Try to escape
                    rewardValue = MovementHandler.escapeOrDie(image, brain);
                } catch (Exception e) {
                    LOGGER.debug(e.getMessage());
                    rewardValue = 0;
                }
                rewardMap.put(brain, rewardValue);
            }

            evaluateAndBreedBrains(image, brains, rewardMap);
            cycleNumber++;
        }
    }

    private static void evaluateAndBreedBrains(BufferedImage image, List<Brain> brains, Map<Brain, Integer> rewardMap) {
        // Evaluate which brains were the best
        List<Brain> sortedBrains = rewardMap
                .keySet()
                .stream()
                .sorted(Comparator.comparing(rewardMap::get).reversed())
                .collect(Collectors.toList());

        Brain bestBrain = sortedBrains.get(0);

        if (rewardMap.get(bestBrain) > Short.MAX_VALUE) {
            LOGGER.info("The best brain escaped");
            bestBrain.getDecisionNeurons()
                    .forEach(decisionNeuron -> decisionNeuron.getWeighting()
                            .forEach(weighting -> LOGGER.debug("Weighting: {}", weighting)));
            bestBrain.getHistory().forEach(position -> LOGGER.info("[{}, {}]", position.getX(), position.getY()));
        }

        // Replace each brain in the list by breeding the best brain with a random brain
        brains.replaceAll(brain -> {
            Position position = new Position(1, 1);
            // Keep the best brain
            if (brain.equals(bestBrain)) {
                return BrainFactory.breedBrains(image, position, bestBrain, brain);
            }
            return BrainFactory.breedBrains(image, position, bestBrain, BrainFactory.createRandomBrain(image, position));
        });
    }

    private static BufferedImage loadImage() {
        try {
            ClassLoader classLoader = MazeRunner.class.getClassLoader();
            URL resource = classLoader.getResource("maze_1.png");
            return ImageIO.read(new File(resource.toURI()));
        } catch (IOException | URISyntaxException | NullPointerException e) {
            throw new RuntimeException("Unable to load image", e);
        }
    }
}