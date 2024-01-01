package org.example;

import java.util.List;

public class MemoryNeuron implements Neuron {

    private static final int MAX_HISTORY_SIZE = 512;

    private static final int MAX_VISITS_PER_POSITION = 8;

    private final int direction;

    private final Brain brain;

    public MemoryNeuron(int direction, Brain brain) {
        this.direction = direction;
        this.brain = brain;
    }

    @Override
    public double getSignal() {
        final Position position = brain.getPosition();
        final List<Position> history = brain.getHistory();
        if (history.isEmpty()) {
            return 0;
        } else if (history.size() > MAX_HISTORY_SIZE) {
            throw new RuntimeException(String.format("This brain has failed to find the end in %d moves", MAX_HISTORY_SIZE));
        }
        int[] translation = Translator.translate(position.getX(), position.getY(), direction);
        long timesPreviouslyVisited = history.stream()
                .filter(historicalPosition ->
                        historicalPosition.getX() == translation[0]
                                && historicalPosition.getY() == translation[1])
                .count();

        if (timesPreviouslyVisited > MAX_VISITS_PER_POSITION) {
            throw new RuntimeException(String.format("This brain has visited position [%d, %d] more than %d times",
                    translation[0], translation[1], MAX_VISITS_PER_POSITION));
        }

        return timesPreviouslyVisited / (double) history.size();
    }
}
