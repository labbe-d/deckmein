package com.cardmein.drawroyale.game.service.shuffle;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Shuffles a list of object randomly.
 */
public class ObjectShuffler<T> {

    /**
     * Randomly shuffle a list of object into a new list.
     * @param input List to shuffle
     * @return Shuffled list
     */
    public List<T> shuffle(List<T> input) {
        return input.stream()
                .map(o -> new ShuffleOrder<T>(o, Math.random()))
                .sorted((o1, o2) -> Double.compare(o1.order, o2.order))
                .map(o -> o.obj)
                .collect(Collectors.toList());
    }

    private class ShuffleOrder<U> {
        U obj;
        double order;

        public ShuffleOrder(U obj, double order) {
            this.obj = obj;
            this.order = order;
        }
    }

}