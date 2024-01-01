package org.example;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class DecisionNeuron implements Neuron {

    private final List<Neuron> inputs;

    private final List<Double> weighting;

    private final int inputCount;

    public DecisionNeuron(List<Neuron> inputs, List<Double> weighting) {
        this.inputs = inputs;
        this.weighting = weighting;
        this.inputCount = inputs.size();
        if (weighting.size() != inputCount) {
            throw new RuntimeException(String.format("Input count was %d but weighting size was %d", inputCount, weighting.size()));
        }
    }

    public List<Neuron> getInputs() {
        return inputs;
    }

    public List<Double> getWeighting() {
        return weighting;
    }

    @Override
    public double getSignal() {
        return IntStream.range(0, inputCount)
                .mapToDouble(i -> inputs.get(i).getSignal() * weighting.get(i))
                .sum() / inputCount;
    }
}
