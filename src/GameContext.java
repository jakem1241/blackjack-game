import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GameContext {

    private final int MAX_POWERUPS = 6, POWERUPS_PER_TURN = 1, PUP_PER_ROUND = 15;
    private final Scanner scan;
    private String player;
    private static final Random random = new Random();

    private int lives = 3;
    private int money = 500;    
    private int pwrUpPts = 25;
    ArrayList<PowerUp> powerUps = new ArrayList<>();
    
    private Card vault;
    private boolean itemInVault = false;
    private boolean hasAON = false;

    private RoundContext rtx;

    public GameContext(Scanner scan) {
        this.scan = scan;
        PowerUpFactory.initializePowerUps();
    }


        /*      GETTERS AND SETTERS        */


    public void initRound() { this.rtx = new RoundContext(); }
    public RoundContext getRoundContext() {return rtx; }

    public Scanner getScanner() { return scan; }
    public Random getRandom() { return random; }
    public void createPlayer(String p) { player = p; }
    public String getPlayer() { return player; }

    public int getLives() { return lives; }
    public void setLives(int lives) { this.lives = lives; }

    public int getMoney() { return money; }
    public void setMoney(int money) { this.money = money; }

    public void setVault(Card c) { vault = c; itemInVault = true; }
    public Card getVault() { return vault; }
    public boolean vaultUsed() { return itemInVault; }
    public void removeFromVault() { setVault(null); itemInVault = false; }

    public void obtainAON() { hasAON = true; }
    public boolean isAONobtained() { return hasAON; }


    /*  POWER-UP HANDLING   */


    public void printPwrUps() {
        System.out.println("---------- POWER-UPS ----------");
        for (int i = 0; i < powerUps.size(); i++)
            System.out.println((i+1) + ") " + powerUps.get(i));
        
        System.out.println();
            if (rtx.pocketUsed())
            System.out.println("Card in Pocket: " + rtx.getPocket());
        if(vaultUsed())
            System.out.println("Card in Vault: " + getVault());
        System.out.println("Power-Up Points: " + getPwrUpPts());
    }

    public void drawPowerUps() {
        for (int i = 0; i < POWERUPS_PER_TURN; i++) {
            if (powerUps.size() == MAX_POWERUPS) {
                pwrUpPts += 10;
                return;
            }
            powerUps.add(PowerUpFactory.getRandomPowerUp(this));
        }
    }

    public int getPwrUpPts() { return pwrUpPts; }
    public void incrementPwrUpPts() { this.pwrUpPts += PUP_PER_ROUND; }
    public void setPwrUpPts(int p) { this.pwrUpPts = p; }
}    