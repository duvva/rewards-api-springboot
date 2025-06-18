package com.duvva.rewards.service;

import org.springframework.stereotype.Service;

@Service
public class RewardService {
    public int calculatePoints(double amount) {
        int points = 0;
        if (amount > 100) {
            points += (int) ((amount - 100) * 2);
            points += 50; 
        } else if (amount > 50) {
            points += (int) (amount - 50);
        }
        return points;
    }
}
