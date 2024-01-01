package org.example;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Brain {

    private final List<DecisionNeuron> decisionNeurons = new ArrayList<>();

    private final Position position;

    private final List<Position> history = new ArrayList<>();

    public Brain(Position position) {
        this.position = position;
    }

    public void addDecisionNeuron(DecisionNeuron decisionNeuron) {
        decisionNeurons.add(decisionNeuron);
    }

    public List<DecisionNeuron> getDecisionNeurons() {
        return decisionNeurons;
    }

    public Position getPosition() {
        return position;
    }

    public List<Position> getHistory() {
        return history;
    }

    public void setPosition(int x, int y){
        history.add(new Position(position.getX(), position.getY()));
        position.setX(x);
        position.setY(y);
    }

    public int[] chooseDirection() {
        Map<Double, List<Integer>> directionPreferences = IntStream.range(0, 4)
                .boxed()
                .collect(Collectors.groupingBy(direction -> decisionNeurons.get(direction).getSignal()));

        double highestSignal = directionPreferences.keySet().stream().max(Double::compareTo).get();
        List<Integer> favouredDirections = directionPreferences.get(highestSignal);
        int chosenIndex = 0;
        if (favouredDirections.size() > 1) {
            chosenIndex = new Random().nextInt(favouredDirections.size());
        }
        return Translator.translate(0, 0, favouredDirections.get(chosenIndex));
    }
}
