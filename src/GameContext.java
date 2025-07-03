import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GameContext {

    private GameEnvironment genv;
    private RoundContext rtx;
    
    private static final int MAX_POWERUPS = 6, POWERUPS_PER_TURN = 1, PUP_PER_ROUND = 15;
    private final Scanner scan;
    private static final Random random = new Random();

    private int lives = 3;
    private int money = 500;    
    private int powerUpPts = 25;
    ArrayList<PowerUp> powerUps = new ArrayList<>();
    
    private Card vault;



    public GameContext(Scanner scan, GameEnvironment genv) {
        PowerUpFactory.initializePowerUps();
        this.scan = scan;
        this.genv = genv;
    }



    /*      GETTERS AND SETTERS        */

    public void initRound() { this.rtx = new RoundContext(); }
    public RoundContext getRoundContext() {return rtx; }
    public GameEnvironment getGameEnvironment() { return genv; }

    public Scanner getScanner() { return scan; }
    public Random getRandom() { return random; }

    public int getLives() { return lives; }
    public void setLives(int lives) { this.lives = lives; }

    public int getMoney() { return money; }
    public void setMoney(int money) { this.money = money; }

    public void setVault(Card c) { vault = c; }
    public Card getVault() { return vault; }
    public boolean vaultUsed() { return vault != null; }
    public void removeFromVault() { vault = null; }

    /*  POWER-UP HANDLING   */

    public void drawPowerUps() {
        for (int i = 0; i < POWERUPS_PER_TURN; i++) {
            if (powerUps.size() == MAX_POWERUPS) {
                powerUpPts += 10;
                return;
            }
            powerUps.add(PowerUpFactory.getRandomPowerUp(this));
        }
    }

    public boolean inPowerUpHand(PowerUp p) {
        for (PowerUp pwr : powerUps) 
            if (p.equals(pwr)) return true;
        return false;
    }

    public int getPowerUpPts() { return powerUpPts; }
    public void incrementPowerUpPts() { this.powerUpPts += PUP_PER_ROUND; }
    public void setPowerUpPts(int p) { this.powerUpPts = p; }


    /*  CHANGES FROM REFACTORING   */


    public boolean canPlayAllOrNothing(int cost) { return inPowerUpHand(PowerUp.all_or_nothing) && powerUpPts >= cost; }
    public void applyAllOrNothing(int cost) { powerUpPts -= cost; }

    public void applyRoundResult(int result, int wager, boolean usedDoubleJeopardy) {
        if (result == 1) money += wager;
        else if (result == -1) { money -= wager; lives -= (usedDoubleJeopardy ? 2 : 1); }
        //Tie results in no changes
    }
    public void applyAONResult(int result) {
        if (result == 1) lives *= 2;
        else if (result == -1) lives = 0;
        //Tie results in no changes
    }

    public boolean isGameOver() { return money <= 0 || lives <= 0; }
    
}
