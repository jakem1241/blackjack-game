public class RoundContext {

    private static final int POINT_GOAL = 21;

    private final Deck deck = new Deck();
    private final Hand playerHand = new Hand(), houseHand = new Hand();
    private Card pocket;
    private boolean itemInPocket = false;

    private int wager = 0;
    private int pointGoal = POINT_GOAL;
    private boolean playerActive = true, houseActive = true;

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
        //All-or-Nothing wager
        if (wager == -1) { setWager(playerLives); } 
        //Regular wager
        else { setWager(wager); }
    }



    public int playPowerUp(int index, GameContext gtx, GameEnvironment genv) {

        PowerUp p = gtx.powerUps.get(index - 1);
        if (p.equals(PowerUp.all_or_nothing)) { return -1; } //Logic rejects mid-round AON

        if (gtx.getPowerUpPts() >= p.getCost()) {
            p.apply(gtx, genv);
            gtx.powerUps.remove(p);
            gtx.setPowerUpPts(gtx.getPowerUpPts() - p.getCost());
            return 1;
        }

        return 0; //Not enough points
    }



    public static class RoundResult {
        public final int result; // 1 = player win, 0 = tie, -1 = house win
        public final String message;

        public RoundResult(int result, String message) {
            this.result = result;
            this.message = message;
        }
    }

    public RoundResult determineWinnerLogic() {
        int pv = playerHand.getTotalValue();
        int hv = houseHand.getTotalValue();
        int goal = pointGoal;

        boolean playerBust = pv > goal;
        boolean houseBust = hv > goal;

        if (playerBust && houseBust) {
            return new RoundResult(0, "Both player and house busted!");
        }
        if (playerBust) {
            return new RoundResult(-1, "Player busts with " + pv + "! House wins.");
        }
        if (houseBust) {
            return new RoundResult(1, "House busts with " + hv + "! Player wins.");
        }

        if (pv > hv) {
            return new RoundResult(1, "Player wins with " + pv + " against house's " + hv + "!");
        }
        if (pv == hv) {
            return new RoundResult(0, "It's a tie at " + pv + "!");
        }
        return new RoundResult(-1, "House wins with " + hv + " against player's " + pv + "!");
    }

    public boolean checkPlayerBust() { return playerHand.getTotalValue() > pointGoal; }
    public boolean checkHouseBust() { return houseHand.getTotalValue() > pointGoal; }
    public void checkBusts() {
        if (checkPlayerBust()) playerActive = false;
        if (checkHouseBust()) houseActive = false;
    }

    public boolean shouldHouseDraw(int houseGoal) {
        return houseHand.getTotalValue() < houseGoal;
    }

    public boolean isPlayerActive() { return playerActive; }
    public boolean isHouseActive() { return houseActive; }
    public void setPlayerActive(boolean b) { playerActive = b; }
    public void setHouseActive(boolean b) { houseActive = b; }
}
