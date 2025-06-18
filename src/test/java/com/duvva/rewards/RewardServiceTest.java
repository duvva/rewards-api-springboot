package com.duvva.rewards;
import com.duvva.rewards.service.RewardService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RewardServiceTest {
    private final RewardService rewardService = new RewardService();

    @Test
    public void testCalculatePointsBelow50() {
        assertEquals(0, rewardService.calculatePoints(40));
    }

    @Test
    public void testCalculatePointsBetween50And100() {
        assertEquals(30, rewardService.calculatePoints(80));
    }

    @Test
    public void testCalculatePointsAbove100() {
        assertEquals(90, rewardService.calculatePoints(120)); // 2x20 + 1x50 = 90
    }
}
