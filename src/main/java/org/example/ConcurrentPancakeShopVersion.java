package org.example;

import java.time.Instant;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ConcurrentPancakeShopVersion {
    private static final int MAX_PANCAKES_PER_USER = 5;
    private static final int MAX_PANCAKES_PER_SLOT = 12;
    private static final int NUM_USERS = 3;
    private static final int TOTAL_TIME_SECONDS = 30;
    private Random random;
    public ConcurrentPancakeShopVersion() {
        random = new Random();
    }
    public void servePancakes() {
        Instant startTime = Instant.now();
        Instant endTime = startTime.plusSeconds(TOTAL_TIME_SECONDS);
        int totalPancakesRequested = 0;
        final int[] totalPancakesServed = {0};
        int[] pancakesRequested = new int[NUM_USERS];
        int[] pancakesServed = new int[NUM_USERS];
        // Generate pancake requests for each user
        for (int i = 0; i < NUM_USERS; i++) {
            pancakesRequested[i] = random.nextInt(MAX_PANCAKES_PER_USER + 1);
            totalPancakesRequested += pancakesRequested[i];
        }
        CompletableFuture<Void>[] completableFutures = new CompletableFuture[NUM_USERS];
        // Serve pancakes to users concurrently using CompletableFuture
        for (int i = 0; i < NUM_USERS; i++) {
            int userIndex = i;
            completableFutures[userIndex] = CompletableFuture.runAsync(() -> {
                int pancakesToServe = Math.min(MAX_PANCAKES_PER_USER, pancakesRequested[userIndex]);
                synchronized (pancakesServed) {
                    if (totalPancakesServed[0] + pancakesToServe <= MAX_PANCAKES_PER_SLOT) {
                        pancakesServed[userIndex] = pancakesToServe;
                        totalPancakesServed[0] += pancakesToServe;
                    } else {
                        pancakesServed[userIndex] = 0;
                    }
                }
            });
        }
        try {
            CompletableFuture.allOf(completableFutures).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        int pancakesWasted = totalPancakesServed[0] - totalPancakesRequested;
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
        ConcurrentPancakeShopVersion pancakeShop = new ConcurrentPancakeShopVersion();
        pancakeShop.servePancakes();
    }
}

