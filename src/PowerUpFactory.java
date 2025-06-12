import java.util.ArrayList;
import java.util.Random;

public class PowerUpFactory {

    private static final Random random = new Random();
    private static ArrayList<PowerUp> powerWeightedList = new ArrayList<>();

    public static void initializePowerUps() {
        for (PowerUp powerUp : PowerUp.values()) {
            int weight = powerUp.getRarity().getWeight();
            for (int i = 0; i < weight; i++) {
                powerWeightedList.add(powerUp);
            }
        }
    }

    public static PowerUp getRandomPowerUp(GameContext gtx) {
        PowerUp p = powerWeightedList.get(random.nextInt(powerWeightedList.size())); 
        while (!p.isValid(gtx))
            p = powerWeightedList.get(random.nextInt(powerWeightedList.size()));

            if (p.equals(PowerUp.all_or_nothing))
                gtx.obtainAON();
        return p;
    }

    public static ArrayList<PowerUp> getWeightedList() {
        return powerWeightedList;
    }
}
