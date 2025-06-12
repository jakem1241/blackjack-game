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
}
