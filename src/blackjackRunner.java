import java.util.Scanner;

public class blackjackRunner {

    public static void main(String[] args) {

        //Initialize a game
        Scanner scan = new Scanner(System.in);
        GameContext gtx = new GameContext(scan);

        System.out.println("Welcome Jake!");
        System.out.println("\nYou are starting with..." + "\nMoney: $" + gtx.getMoney() + "\nLives: " + gtx.getLives() + "\nPower-Up Points: " + gtx.getPwrUpPts());
        System.out.println();

        //Round loop
        while (gtx.getMoney() > 0 && gtx.getLives() > 0) {
            gtx.initRound();
            RoundEnvironment renv = new RoundEnvironment(gtx);

            int wager = 0;

            if (gtx.powerUps.contains(PowerUp.use_pocket))
                gtx.powerUps.remove(PowerUp.use_pocket);


            /*   AON GAME FLOW   */

            int res = 0;
            if (gtx.isAONobtained() && gtx.getPwrUpPts() >= PowerUp.Rarity.LEGENDARY.getDefaultCost()) {
                System.out.println("You have All or Nothing in your Power-Up Hand and have enough Power-Up Points to use it.");
                System.out.println(PowerUp.all_or_nothing.getDesc());

                res = InputHelper.getValidatedInput(gtx.getScanner(), 0, 1, 
                        "\n Do you wish to activate All or Nothing? Enter 1 to use, enter 0 to cancel: ", 
                  "Enter only 0 or 1.");
            }
            if (res == 1) {
                    wager = -1;

                    //Play round
                    renv.startRound(wager);
                    int result = renv.playRound(true);

                    //Update lives
                    if (result == 1) {
                        gtx.setLives(gtx.getLives() * 2);
                        System.out.println("\nAll or Nothing hand won! you now have " + gtx.getLives() + " lives.");
                    } 
                    
                    else if (result == -1) {
                        gtx.setLives(0);
                        System.out.println("\nAll or nothing hand lost...");
                    }
                    
                    else
                        System.out.println("\nRound resulted in a tie. No lives are gained or lost.");
                } 


            /*  REGULAR GAME FLOW   */


            else {

                //Obtain money wager
                boolean validInput = false;

                while (!validInput) {
                    System.out.print("Enter your wager or EXIT to exit: ");
                    String s = scan.nextLine();
                    if (s.equalsIgnoreCase("exit")) {
                        System.out.println("\nYou ended with $" + gtx.getMoney() + " and " + gtx.getLives() + " lives.");
                        scan.close();
                        System.exit(0);
                    }
                    try {
                        wager = Integer.parseInt(s);

                        if (wager <= 0) {
                            System.out.println("Wager must be greater than 0.");
                        } else if (wager > gtx.getMoney()) {
                            System.out.println("You don't have enough money to wager that amount.");
                        } else {
                            validInput = true;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                    }
                }

                //Play round, update the wager amount after game ends
                renv.startRound(wager);
                int result = renv.playRound(true);
                wager = gtx.getRoundContext().getWager();

                //Update money/lives
                if (result == 1) {
                    gtx.setMoney(gtx.getMoney() + wager);
                    System.out.println("\nYou won this round!");
                } 
                
                else if (result == -1) {
                    gtx.setMoney(gtx.getMoney() - wager);
                    gtx.setLives(gtx.getLives() - 1);
                    System.out.println("\nYou lost this round. You lost a life.");
                    if (gtx.getRoundContext().isDJused()) {
                        gtx.setLives(gtx.getLives() - 1);
                        System.out.println("You lost an additional life due to the use of Double Jeopardy.");
                    }
                } 
                
                else
                    System.out.println("\nRound resulted in a tie.");
            }

            //Show current stats
            System.out.println("Money remaining: $" + gtx.getMoney());
            System.out.println("Lives remaining: " + gtx.getLives());
            System.out.println("Power-Up Points remaining: " + gtx.getPwrUpPts());

            System.out.println("\n--------------------------------------------------------------------------------------------------\n");
        }

        //End of game
        if (gtx.getMoney() <= 0) {
            System.out.println("You ran out of money! GAME OVER");
            System.out.println("You ended with " + gtx.getLives() + " lives.");
        }
        if (gtx.getLives() <= 0) {
            System.out.println("You ran out of lives! GAME OVER");
            System.out.println("You ended with $" + gtx.getMoney() + ".");
        }

        scan.close();
    }
}
