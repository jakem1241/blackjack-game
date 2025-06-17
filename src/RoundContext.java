public class RoundContext {

    private final int POINT_GOAL = 21;

    private final Deck deck = new Deck();
    private final Hand playerHand = new Hand(), houseHand = new Hand();
    private Card pocket;
    private boolean itemInPocket = false;

    private int wager = 0;
    private int pointGoal = POINT_GOAL;

    private boolean usedDBJ = false;
    private boolean usedAON = false;
    private boolean usedDJ = false;
    

        /*      GETTERS AND SETTERS        */


    public Deck getDeck() { return deck; }
    public Hand getPlayerHand() { return playerHand; }
    public Hand getHouseHand() { return houseHand; }

    public int getWager() { return wager; }
    public void setWager(int wager) { this.wager = wager; }

    public int getPointGoal() { return pointGoal; }
    public void doublePointGoal() { this.pointGoal *= 2; }

    

    /*  POWER-UP HANDLING   */


    public void setPocket(Card c) { pocket = c; itemInPocket = true; }
    public Card getPocket() { return pocket; }
    public boolean pocketUsed() { return itemInPocket; }
    public void removeFromPocket() { setPocket(null); itemInPocket = false; }

    public boolean isDBJused() { return usedDBJ; }
    public void useDBJ() { usedDBJ = true; doublePointGoal(); }

    public boolean isAONused() { return usedAON; }
    public void useAON() { usedAON = true; }

    public boolean isDJused() { return usedDJ; }
    public void useDJ() { usedDJ = true; }


    /*  CHANGES FROM REFACTORING   */


    public void drawPlayer() { playerHand.addCard(deck.draw()); }
    public void drawHouse() { houseHand.addCard(deck.draw()); }

    public void startRoundLogic(int wager, int playerLives) {
        if (wager == -1) { setWager(playerLives); } 
        else { setWager(wager); }
    }

    private boolean playerActive = true, houseActive = true;

    public void checkBusts() {
        if (playerHand.getTotalValue() > pointGoal) playerActive = false;
        if (houseHand.getTotalValue() > pointGoal) houseActive = false;
    }

    public boolean shouldHouseDraw(int houseGoal) {
        return houseHand.getTotalValue() < houseGoal;
    }

    public boolean isPlayerActive() { return playerActive; }
    public boolean isHouseActive() { return houseActive; }
    public void setPlayerActive(boolean b) { playerActive = b; }
    public void setHouseActive(boolean b) { houseActive = b; }
}
