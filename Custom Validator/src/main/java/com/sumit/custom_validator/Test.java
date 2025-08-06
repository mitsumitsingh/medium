package com.sumit.custom_validator;

import java.util.List;
import java.util.random.RandomGenerator;

public class Test {
    public static void main(String[] args) {
        RandomGenerator rng = RandomGenerator.of("L64X256MixRandom");
        // Better for streams
        List<Integer> randomNumbers = rng.ints(10, 1, 100)
                .boxed()
                .toList();
        System.out.println("Random Numbers: " + randomNumbers);
    }
}
