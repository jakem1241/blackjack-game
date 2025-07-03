public class RoundEnvironment {

     private static final int HOUSE_DRAW_MARGIN = 4;
     
    private GameContext gtx;
    private RoundContext rtx;
    private GameEnvironment genv;
    private int housePointGoal;

    public RoundEnvironment(GameContext gtx) {
        this.gtx = gtx;
        this.rtx = this.gtx.getRoundContext();
        this.genv = this.gtx.getGameEnvironment();
    }

   public void startRound(int wager) {
        if (wager == -1) {
            System.out.println("Starting All or Nothing round! You wagered " + gtx.getLives() + " lives.");
            System.out.println("No power-ups will be available to use.");
        } else {
            System.out.println("\nNew round started! You wagered $" + wager + ".");
        }
        // Update the game state after printing
        rtx.startRoundLogic(wager, gtx.getLives());
    }


    public int playRound(boolean isFirstDraw) {

        /*   PRE-TURN   */

        housePointGoal = rtx.getPointGoal() - HOUSE_DRAW_MARGIN;

        if (!isFirstDraw) {
            
            //Check if player, house busts
            rtx.checkBusts(); 

            //Ask player if they want to stop
            if (rtx.isPlayerActive()) {
                int res = InputHelper.getValidatedInput(
                    gtx.getScanner(), 0, 1,
                    "Do you want to keep drawing cards? 1 to continue, 0 to stop: ",
                    "Enter only 1 or 0 to continue or stop."
                );
                if (res == 0) {
                    rtx.setPlayerActive(false);
                }
            }

            //Base Case: If both done, determine winner
            if (!rtx.isPlayerActive() && !rtx.isHouseActive()) {
                return determineWinner();
            }
        }

        /*     TURN    */

        if (rtx.isPlayerActive()) {

            // 1. Increment power-up points, draw powerups
            gtx.drawPowerUps();

            if (isFirstDraw) {
                gtx.drawPowerUps();
            } else {
                gtx.incrementPowerUpPts();

                // 2. Play powerups before the draw
                // DELETED
            }

            // 3. Player draws
            rtx.drawPlayer();
            if (rtx.isAONused()) {
                System.out.println("\n--------------------------------------------------------------------------------------------------\n");
                printBothHands(21);
            }

        } else {
            System.out.println("\n--------------------------------------------------------------------------------------------------\n");
            System.out.println("The player has stopped drawing cards and playing powerups.");
        }

        // 4. If house will play, draw 
        if (rtx.shouldHouseDraw(housePointGoal)) {
            rtx.setHouseActive(true);
            rtx.drawHouse();
            if (!rtx.isPlayerActive()) {
                System.out.println();
                printBothHands(housePointGoal);
            }
        } else {
            rtx.setHouseActive(false);
        }

         // 5. Play powerups after the draw if house doesn't bust
        if (rtx.checkPlayerBust() || !rtx.checkHouseBust()) {
            if (rtx.isPlayerActive() && !rtx.isAONused()) {
                playPowerUpsPhase(housePointGoal);
            }
        }

        // 6. Recursively play next round
        return playRound(false);
    }



    private void printBothHands(int housePointGoal) {
        System.out.println("--------------------------------------------------------------------------------------------------\n");

        if (!rtx.isHouseActive()) {
            System.out.println("The house stands as the value of their hand has reached " + housePointGoal + " or higher.\n");
        }

        Hand playerHand = rtx.getPlayerHand();
        Hand houseHand = rtx.getHouseHand();

        System.out.println("Player Hand:\t\t\t\tHouse Hand:");

        int maxSize = Math.max(playerHand.size(), houseHand.size());
        for (int i = 0; i < maxSize; i++) {
            String playerCard = i < playerHand.size() ? (i + 1) + ") " + playerHand.getCard(i).toString() : "";
            String houseCard = i < houseHand.size() ? (i + 1) + ") " + houseHand.getCard(i).toString() : "";

            System.out.printf("%-40s%s%n", playerCard, houseCard);
        }

        System.out.println();
        System.out.printf("Total Hand Value: %-19dTotal Hand Value: %d%n",
            playerHand.getTotalValue(), houseHand.getTotalValue());
        System.out.println();
    }

    


    private void playPowerUpsPhase(int housePointGoal) {

        if (rtx.isAONused()) return;

        // Allow player to choose and activate powerups, loop until player ends
        int choice = -1;
        while (true) {

            // Show hands, available power-ups, power-up points
            System.out.println();
            printBothHands(housePointGoal);
            printPowerUps();

            choice = InputHelper.getValidatedInput(gtx.getScanner(), 0, gtx.powerUps.size(),
                    "\nEnter a power-up number to play, or 0 to continue: ",
                    "Enter a number between 0 and " + gtx.powerUps.size() + ".");

            if (choice == 0) break;

            // Handle invalid play (e.g. not enough points or trying to play AON)
            switch (rtx.playPowerUp(choice, gtx, genv)) {
                case 1: 
                    break;
                case 0:
                    System.out.println("ERROR: Not enough Power-Up Points.");
                    break;
                case -1:
                    System.out.println("ERROR: Cannot play All or Nothing mid-round.");
                    break;
            }
        }
    }



    public int determineWinner() {
        System.out.println("\n--------------------------------------------------------------------------------------------------\n");

        RoundContext.RoundResult result = rtx.determineWinnerLogic();
        System.out.println(result.message);
        return result.result;
    }



    public void printPowerUps() {
        System.out.println("---------- POWER-UPS ----------");
        for (int i = 0; i < gtx.powerUps.size(); i++)
            System.out.println((i+1) + ") " + gtx.powerUps.get(i));
        
        System.out.println();
        if (rtx.pocketUsed())
            System.out.println("Card in Pocket: " + rtx.getPocket());
        if(gtx.vaultUsed())
            System.out.println("Card in Vault: " + gtx.getVault());
        System.out.println("Power-Up Points: " + gtx.getPowerUpPts());
    }
}
