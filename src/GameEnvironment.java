import java.util.Scanner;

public class GameEnvironment {

    private GameContext gtx;

    public void startGame() {
        Scanner scan = new Scanner(System.in);
        gtx = new GameContext(scan, this);

        printStartStats(gtx);

        gtx.powerUps.add(PowerUp.pick_two);
    
        while (!gtx.isGameOver()) {
            gtx.initRound();
            RoundEnvironment renv = new RoundEnvironment(gtx);

            int wager = 0;

            // Pocket power-up removed!

            // if (gtx.powerUps.contains(PowerUp.use_pocket)) {
            //     gtx.powerUps.remove(PowerUp.use_pocket);
            // }

            if (handleAllOrNothingFlow(gtx, renv)) {
                continue;
            }

            wager = getRegularWager(scan, gtx);

            renv.startRound(wager);
            int result = renv.playRound(true);
            wager = gtx.getRoundContext().getWager();

            gtx.applyRoundResult(result, wager, gtx.getRoundContext().isDJused());
            printRoundResult(result, gtx.getRoundContext().isDJused());

            printCurrentStats(gtx);
            System.out.println("\n--------------------------------------------------------------------------------------------------\n");
        }

        printEndGameMessage(gtx);
        scan.close();
    }

    private void printStartStats(GameContext gtx) {
        System.out.println("\nYou are starting with..." +
                "\nMoney: $" + gtx.getMoney() +
                "\nLives: " + gtx.getLives() +
                "\nPower-Up Points: " + gtx.getPowerUpPts() + "\n");
    }

    private boolean handleAllOrNothingFlow(GameContext gtx, RoundEnvironment renv) {
        if (gtx.canPlayAllOrNothing(PowerUp.Rarity.LEGENDARY.getDefaultCost())) {
            System.out.println("You have All or Nothing in your Power-Up Hand and have enough Power-Up Points to use it.");
            System.out.println(PowerUp.all_or_nothing.getDesc());

            int res = InputHelper.getValidatedInput(gtx.getScanner(), 0, 1,
                    "\nDo you wish to activate All or Nothing? Enter 1 to use, enter 0 to cancel: ",
                    "Enter only 0 or 1.");

            if (res == 1) {
                gtx.applyAllOrNothing(PowerUp.Rarity.LEGENDARY.getDefaultCost());
                PowerUp.all_or_nothing.apply(gtx, this);
                gtx.powerUps.remove(PowerUp.all_or_nothing);

                renv.startRound(-1); //AON uses -1 as wager
                int result = renv.playRound(true);

                gtx.applyAONResult(result);
                printAONResult(result, gtx);
                printCurrentStats(gtx);

                return true; //Skip regular flow
            }
        }
        return false;
    }

    private int getRegularWager(Scanner scan, GameContext gtx) {
        while (true) {
            System.out.print("Enter your wager or EXIT to exit: ");
            String s = scan.nextLine();
            if (s.equalsIgnoreCase("exit")) {
                System.out.println("\nYou ended with $" + gtx.getMoney() + " and " + gtx.getLives() + " lives.");
                scan.close();
                System.exit(0);
            }
            try {
                int wager = Integer.parseInt(s);
                if (wager <= 0) {
                    System.out.println("Wager must be greater than 0.");
                } else if (wager > gtx.getMoney()) {
                    System.out.println("You don't have enough money to wager that amount.");
                } else {
                    return wager;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private void printAONResult(int result, GameContext gtx) {
        if (result == 1) {
            System.out.println("\nAll or Nothing hand won! You now have " + gtx.getLives() + " lives.");
        } else if (result == -1) {
            System.out.println("\nAll or Nothing hand lost...");
        } else {
            System.out.println("\nRound resulted in a tie. No lives are gained or lost.");
        }
    }

    private void printRoundResult(int result, boolean usedDJ) {
        if (result == 1) {
            System.out.println("\nYou won this round!");
        } else if (result == -1) {
            System.out.println("\nYou lost this round. You lost a life.");
            if (usedDJ) {
                System.out.println("You lost an additional life due to the use of Double Jeopardy.");
            }
        } else {
            System.out.println("\nRound resulted in a tie.");
        }
    }

    private void printCurrentStats(GameContext gtx) {
        System.out.println("Money remaining: $" + gtx.getMoney());
        System.out.println("Lives remaining: " + gtx.getLives());
        System.out.println("Power-Up Points remaining: " + gtx.getPowerUpPts());
    }

    private void printEndGameMessage(GameContext gtx) {
        if (gtx.getMoney() <= 0) {
            System.out.println("You ran out of money! GAME OVER");
            System.out.println("You ended with " + gtx.getLives() + " lives.");
        }
        if (gtx.getLives() <= 0) {
            System.out.println("You ran out of lives! GAME OVER");
            System.out.println("You ended with $" + gtx.getMoney() + ".");
        }
    }

    public int askChoice(String prompt, String[] options) {
        System.out.println(prompt);
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ") " + options[i]);
        }

        return InputHelper.getValidatedInput(
            gtx.getScanner(),
            1,
            options.length,
            "Choose an option: ",
            "Please enter a number between 1 and " + options.length
        );
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}
