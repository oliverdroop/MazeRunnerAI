package org.example;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.function.IntToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BrainFactory {

    public static Brain createRandomBrain(BufferedImage image, Position position) {
        Brain brain = new Brain(position);
        List<Neuron> inputNeurons = getInputNeurons(image, brain);

        double[][] randomDoubles = getRandomDoubles(inputNeurons.size());

        brain.addDecisionNeuron(new DecisionNeuron(inputNeurons, Arrays.stream(randomDoubles[0]).boxed().collect(Collectors.toList())));
        brain.addDecisionNeuron(new DecisionNeuron(inputNeurons, Arrays.stream(randomDoubles[1]).boxed().collect(Collectors.toList())));
        brain.addDecisionNeuron(new DecisionNeuron(inputNeurons, Arrays.stream(randomDoubles[2]).boxed().collect(Collectors.toList())));
        brain.addDecisionNeuron(new DecisionNeuron(inputNeurons, Arrays.stream(randomDoubles[3]).boxed().collect(Collectors.toList())));

        return brain;
    }

    public static Brain createCapableBrain(BufferedImage image, Position position) {
        Brain brain = new Brain(position);
        List<Neuron> inputNeurons = getInputNeurons(image, brain);

        brain.addDecisionNeuron(new DecisionNeuron(inputNeurons, Arrays.asList(-1.0, 0.0, 0.0, 0.5, -1.0, 0.0, 0.0, 0.0)));
        brain.addDecisionNeuron(new DecisionNeuron(inputNeurons, Arrays.asList(0.5, -1.0, 0.0, 0.0, 0.0, -1.0, 0.0, 0.0)));
        brain.addDecisionNeuron(new DecisionNeuron(inputNeurons, Arrays.asList(0.0, 0.5, -1.0, 0.0, 0.0, 0.0, -1.0, 0.0)));
        brain.addDecisionNeuron(new DecisionNeuron(inputNeurons, Arrays.asList(0.0, 0.0, 0.5, -1.0, 0.0, 0.0, 0.0, -1.0)));

        return brain;
    }

    public static Brain breedBrains(BufferedImage image, Position position, Brain brain1, Brain brain2) {
        Brain newBrain = new Brain(position);
        List<Neuron> inputNeurons = getInputNeurons(image, newBrain);
        for(int direction = 0; direction < 4; direction++) {
            List<Double> averageWeighting = IntStream.range(0, inputNeurons.size())
                    .mapToDouble(combineWeightingsForInput(brain1, brain2, direction))
                    .boxed()
                    .collect(Collectors.toList());

            newBrain.addDecisionNeuron(new DecisionNeuron(inputNeurons, averageWeighting));
        }
        return newBrain;
    }

    /**
     * Combines the weighting values of two brains for a particular direction/decisionNeuron
     *
     * @param brain1
     * @param brain2
     * @param direction
     * @return
     */
    private static IntToDoubleFunction combineWeightingsForInput(Brain brain1, Brain brain2, int direction) {
        Random rnd = new Random();
        return i -> {
            double weighting1 = getWeightingForInput(brain1, direction, i);
            double weighting2 = getWeightingForInput(brain2, direction, i);
            double minWeighting = Math.min(weighting1, weighting2);
            double maxWeighting = Math.max(weighting1, weighting2);
            double diff = maxWeighting - minWeighting;
            return minWeighting + (rnd.nextDouble() * diff);
        };
    }

    private static double getWeightingForInput(Brain brain, int direction, int inputNumber) {
        return brain.getDecisionNeurons().get(direction).getWeighting().get(inputNumber);
    }

    private static List<Neuron> getInputNeurons(BufferedImage image, Brain brain) {
        List<Neuron> inputNeurons = new ArrayList<>();
        inputNeurons.add(new SensoryNeuron(0, image, brain));
        inputNeurons.add(new SensoryNeuron(1, image, brain));
        inputNeurons.add(new SensoryNeuron(2, image, brain));
        inputNeurons.add(new SensoryNeuron(3, image, brain));
        inputNeurons.add(new MemoryNeuron(0, brain));
        inputNeurons.add(new MemoryNeuron(1, brain));
        inputNeurons.add(new MemoryNeuron(2, brain));
        inputNeurons.add(new MemoryNeuron(3, brain));
        return inputNeurons;
    }

    private static double[][] getRandomDoubles(int inputCount) {
        int directionCount = 4;
        double[][] array = new double[directionCount][inputCount];
        Random rnd = new Random();
        for(int i1 = 0; i1 < directionCount; i1++) {
            for(int i2 = 0; i2 < inputCount; i2++) {
                double d = (rnd.nextDouble() * 2) - 1;
                array[i1][i2] = d;
            }
        }
        return array;
    }
}
