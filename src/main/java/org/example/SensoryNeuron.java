package org.example;

import java.awt.image.BufferedImage;

public class SensoryNeuron implements Neuron {

    private final int direction;

    private final BufferedImage image;

    private final Brain brain;

    public SensoryNeuron(int direction, BufferedImage image, Brain brain) {
        this.direction = direction;
        this.image = image;
        this.brain = brain;
    }

    @Override
    public double getSignal() {
        final Position position = brain.getPosition();
        int[] translation = Translator.translate(position.getX(), position.getY(), direction);
        int sensoryResult = PixelSensor.sense(image, translation);
        return sensoryResult <= 1 ? sensoryResult : 0;
    }
}
