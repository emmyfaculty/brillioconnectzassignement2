package org.example;

import java.time.Instant;
import java.util.Random;

public class NonCuncurrentPancakeShopVersion {
    private static final int MAX_PANCAKES_PER_USER = 5;
    private static final int MAX_PANCAKES_PER_SLOT = 12;
    private static final int NUM_USERS = 3;
    private static final int TOTAL_TIME_SECONDS = 30;

    private Random random;

    public NonCuncurrentPancakeShopVersion() {
        random = new Random();
    }

    public void servePancakes() {
        Instant startTime = Instant.now();
        Instant endTime = startTime.plusSeconds(TOTAL_TIME_SECONDS);

        int totalPancakesRequested = 0;
        int totalPancakesServed = 0;

        int[] pancakesRequested = new int[NUM_USERS];
        int[] pancakesServed = new int[NUM_USERS];

        // Generate pancake requests for each user
        for (int i = 0; i < NUM_USERS; i++) {
            pancakesRequested[i] = random.nextInt(MAX_PANCAKES_PER_USER + 1);
            totalPancakesRequested += pancakesRequested[i];
        }

        // Serve pancakes to users
        for (int i = 0; i < NUM_USERS; i++) {
            int pancakesToServe = Math.min(MAX_PANCAKES_PER_USER, pancakesRequested[i]);
            if (totalPancakesServed + pancakesToServe <= MAX_PANCAKES_PER_SLOT) {
                pancakesServed[i] = pancakesToServe;
                totalPancakesServed += pancakesToServe;
            } else {
                pancakesServed[i] = 0;
            }
        }

        int pancakesWasted = totalPancakesServed - totalPancakesRequested;
        int unmetOrders = 0;
        for (int i = 0; i < NUM_USERS; i++) {
            if (pancakesServed[i] < pancakesRequested[i]) {
                unmetOrders++;
            }
        }

        // Output the results
        System.out.println("Start Time: " + startTime);
        System.out.println("End Time: " + endTime);
        System.out.println("Shopkeeper able to meet needs of all customers: " + (unmetOrders == 0));
        System.out.println("Pancakes wasted: " + pancakesWasted);
        System.out.println("Pancake orders not met: " + unmetOrders);
    }

    public static void main(String[] args) {
        NonCuncurrentPancakeShopVersion pancakeShop = new NonCuncurrentPancakeShopVersion();
        pancakeShop.servePancakes();
    }
}
