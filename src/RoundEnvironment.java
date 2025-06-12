public class RoundEnvironment {

    private GameContext gtx;
    private RoundContext rtx;
    private Hand playerHand, houseHand;
    private boolean playerActive = true, houseActive = true;
    private int housePointGoal;

    public RoundEnvironment(GameContext gtx) {
        this.gtx = gtx;
        this.rtx = this.gtx.getRoundContext();
        this.playerHand = rtx.getPlayerHand();
        this.houseHand = rtx.getHouseHand();
    }

    public void startRound(int wager) {

        //AON special case
        if (wager == -1) {
            System.out.println("Starting All or Nothing round! You wagered " + gtx.getLives() + " lives.");
            System.out.println("No power-ups will be available to use.");
        }

        else {
            rtx.setWager(wager);
            System.out.println("\nNew round started! You wagered $" + wager + ".");
        }
    }

    public int playRound(boolean isFirstDraw) {

        /*   PRE-TURN   */

        housePointGoal = rtx.getPointGoal() - 4;

        if (!isFirstDraw) {
            //Check if player busts
            if (checkBust(playerHand)) {
                playerActive = false;
            }

            //Check if house busts
            if (checkBust(houseHand)) {
                houseActive = false;
            }

            //Ask player if they want to stop
            if (playerActive) {
                int res = InputHelper.getValidatedInput(
                    gtx.getScanner(), 0, 1,
                    "Do you want to keep drawing cards? 1 to continue, 0 to stop: ",
                    "Enter only 1 or 0 to continue or stop."
                );
                if (res == 0) {
                    playerActive = false;
                }
            }

            //Base Case: If both done, determine winner
            if (!playerActive && !houseActive) {
                return determineWinner(playerHand, houseHand);
            }
        }

        /*     TURN    */


        if (playerActive) {

            // 1. Increment power-up points, draw powerups
            gtx.drawPowerUps();

            if(isFirstDraw) {
                gtx.drawPowerUps();
            }
            else {
                gtx.incrementPwrUpPts();

                // 2. Play powerups before the draw
                //DELETED
            }

            // 3. Player draws
            if (playerActive) {
                drawPlayer();
                if(rtx.isAONused()) {
                    System.out.println("\n--------------------------------------------------------------------------------------------------\n");
                    Hand.printBothHands(playerHand, houseHand, houseActive, 21);
                }
            }
            else {
                System.out.println("\n--------------------------------------------------------------------------------------------------\n");
                System.out.println("The player has stopped drawing cards and playing powerups.");
            }
        }

        // 4. If house will play, draw 
        if (houseHand.getTotalValue() < housePointGoal) {
            houseActive = true;
            drawHouse();
            if (!playerActive) {
                System.out.println();
                Hand.printBothHands(playerHand, houseHand, houseActive, housePointGoal);
            }
        }
        else {
            houseActive = false;
        }

        // 5. Play powerups after the draw
        if (playerActive && !rtx.isAONused()) {
            playPowerUpsPhase(housePointGoal);
        }
        

        // 6. Recursively play next round
        return playRound(false);
    }



    private void playPowerUpsPhase(int housePointGoal) {

        if (rtx.isAONused()) return;
        
        //Allow player to choose and activate powerups, loop until player ends
        int choice = -1;
        while (true) {

            //Show hands, available power-ups, power-up points
            System.out.println();
            Hand.printBothHands(playerHand, houseHand, houseActive, housePointGoal);
            gtx.printPwrUps();

            choice = InputHelper.getValidatedInput(gtx.getScanner(), 0, gtx.powerUps.size(), 
                                            "\nEnter a power-up number to play, or 0 to continue: ", 
                                            "Enter a number between 0 and " + gtx.powerUps.size() + ".");
            if (choice == 0) break;

            PowerUp p = gtx.powerUps.get(choice - 1);
            if (p.equals(PowerUp.all_or_nothing)) {
                System.out.println("ERROR: All or Nothing cannot be played mid-round.");
                continue;
            }

            System.out.println();
            if (gtx.getPwrUpPts() >= p.getCost()) {
                p.apply(gtx);
                gtx.powerUps.remove(p);
                gtx.setPwrUpPts(gtx.getPwrUpPts() - p.getCost());
            }
            else {
                System.out.println("ERROR: You do not have enough Power-Up Points to play this power-up.");
            }
        }
    }



    public int determineWinner(Hand player, Hand house) {

        System.out.println("\n--------------------------------------------------------------------------------------------------\n");

        int pv = player.getTotalValue();
        int hv = house.getTotalValue();
        int goal = rtx.getPointGoal();

        boolean playerBust = pv > goal;
        boolean houseBust = hv > goal;

        if (playerBust && houseBust) {
            System.out.println("Both player and house busted!");
            return 0;
        }
        if (playerBust) {
            System.out.println("Player busts with " + pv + "! House wins.");
            return -1;
        }
        if (houseBust) {
            System.out.println("House busts with " + hv + "! Player wins.");
            return 1;
        }

        //Neither bust
        if (pv > hv) {
            System.out.println("Player wins with " + pv + " against house's " + hv + "!");
            return 1;
        } else if (pv == hv) {
            System.out.println("It's a tie at " + pv + "!");
            return 0;
        } else {
            System.out.println("House wins with " + hv + " against player's " + pv + "!");
            return -1;
        }
    }



    private boolean checkBust(Hand h) { return (h.getTotalValue() > rtx.getPointGoal()); }
    private void drawPlayer() { playerHand.addCard(rtx.getDeck().draw()); }
    private void drawHouse() { houseHand.addCard(rtx.getDeck().draw()); }
}
