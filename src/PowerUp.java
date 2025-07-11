import java.util.ArrayList;
import java.util.Random;

public enum PowerUp {
    

    /*  LIST OF POWER-UPS   */
    

    burn_top("Burn Top Card", "Burns the top card from the deck", Rarity.COMMON) {
        @Override
        public void apply(GameContext gtx, GameEnvironment genv) {
            Deck d = deck(gtx);

            if (d.isEmpty()) {
                genv.showMessage("Not enough cards in the deck.");
                return;
            }

            Card burned = d.draw();
            genv.showMessage("Burned the card " + burned + ".");
        }

        @Override
        public boolean isValid(GameContext gtx) {
            return true;
        }
    },

    pick_two("Pick Two", "Choose one of the top two drawn cards to keep", Rarity.COMMON) {
        @Override
        public void apply(GameContext gtx, GameEnvironment genv) {
            Deck d = deck(gtx);

            if (d.size() < 2) {
                genv.showMessage("Not enough cards in the deck.");
                return;
            }

            Card c1 = d.draw();
            Card c2 = d.draw();

            PowerUpAction pua = new PowerUpAction(
                "Pick Two: Choose one of the top two drawn cards to keep",
                new String[]{c1.toString(), c2.toString()}
            );

            int choice = pua.execute(genv);

            Card chosen = (choice == 1) ? c1 : c2;
            hand(gtx).addCard(chosen);

            genv.showMessage("Kept " + chosen + ".");
        }

        @Override
        public boolean isValid(GameContext gtx) {
            return true;
        }
    },


    pick_three("Pick Three", "Choose one of the top three drawn cards to keep", Rarity.RARE) {
        @Override
        public void apply(GameContext gtx, GameEnvironment genv) {
            Deck d = deck(gtx);

            if (d.size() < 3) {
                genv.showMessage("Not enough cards in the deck.");
                return;
            }

            Card c1 = d.draw();
            Card c2 = d.draw();
            Card c3 = d.draw();

            String prompt = "Keep one of the following cards:";
            String[] options = new String[] { c1.toString(), c2.toString(), c3.toString() };

            int choice = new PowerUpAction(prompt, options).execute(genv);

            if (choice == 1) {
                genv.showMessage("Kept " + c1 + ".");
                hand(gtx).addCard(c1);
            } else if (choice == 2) {
                genv.showMessage("Kept " + c2 + ".");
                hand(gtx).addCard(c2);
            } else {
                genv.showMessage("Kept " + c3 + ".");
                hand(gtx).addCard(c3);
            }
        }

        @Override
        public boolean isValid(GameContext gtx) {
            return true;
        }
    },


    double_value("Double Value", "Doubles the value of your most recent card", Rarity.COMMON) {
        @Override
        public void apply(GameContext gtx, GameEnvironment genv) {
            Hand h = hand(gtx);

            if (h.isEmpty()) {
                genv.showMessage("Hand is empty.");
                return;
            }

            Card c = h.getCard(0);

            int oldVal = c.getValue();
            c.setValue(oldVal * 2);

            genv.showMessage("Doubled the value of " + c + " from " + oldVal + " to " + c.getValue() + ".");
        }

        @Override
        public boolean isValid(GameContext gtx) {
            return true;
        }
    },

    half_hand("Half Hand Total", "Halves the value of all cards in your hand", Rarity.LEGENDARY) {
        @Override
        public void apply(GameContext gtx, GameEnvironment genv) {
            Hand h = hand(gtx);
            if (h.isEmpty()) {
                genv.showMessage("Hand is empty.");
                return;
            }

            int prevTotal = h.getTotalValue();
            for (Card c : h.getHand()) {
                c.setValue(c.getValue() / 2);
            }

            genv.showMessage("Total hand value changed from " + prevTotal + " to " + h.getTotalValue() + ".");
        }

        @Override
        public boolean isValid(GameContext gtx) {
            return true;
        }
    },


    peek("Peek", "View the top card of the deck", Rarity.RARE) {
        @Override
        public void apply(GameContext gtx, GameEnvironment genv) {
            Deck d = deck(gtx);
            if (d.isEmpty()) {
                genv.showMessage("Deck is empty.");
                return;
            }
            genv.showMessage("The next card is " + d.peek() + ".");
        }

        @Override
        public boolean isValid(GameContext gtx) {
            return true;
        }
    },

    future_peek("Future Peek", "Randomly reveals one of the next two cards in the deck", Rarity.COMMON) {
        @Override
        public void apply(GameContext gtx, GameEnvironment genv) {
            Deck d = deck(gtx);

            if (d.size() < 2) {
                genv.showMessage("Not enough cards in the deck.");
                return;
            }

            Random random = gtx.getRandom();
            genv.showMessage("One of the next 2 cards is " + d.peekAtIndex(random.nextInt(2)) + ".");
        }

        @Override
        public boolean isValid(GameContext gtx) {
            return true;
        }
    },

    omni_peek("Omni-Peek", "Reveal the next five cards in the deck", Rarity.LEGENDARY) {
        @Override
        public void apply(GameContext gtx, GameEnvironment genv) {
            Deck d = deck(gtx);

            if (d.size() < 5) {
                genv.showMessage("Not enough cards in the deck.");
                return;
            }

            genv.showMessage("The next 5 cards are:");
            for (int i = 0; i < 5; i++) {
                genv.showMessage(d.peekAtIndex(i).toString());
            }
        }

        @Override
        public boolean isValid(GameContext gtx) {
            return true;
        }
    },

    slash_val("Slash Value", "Reduce the value of a card to a random lower number", Rarity.RARE) {
        @Override
        public void apply(GameContext gtx, GameEnvironment genv) {
            Hand h = hand(gtx);
            if (h.isEmpty()) {
                genv.showMessage("Hand is empty.");
                return;
            }

            int num = InputHelper.getValidatedInput(
                gtx.getScanner(),
                1, h.size(),
                "Which card to slash? ",
                "There are only " + h.size() + " items in your hand."
            );

            Card c = h.getCard(num - 1);
            int originalValue = c.getValue();

            if (originalValue <= 1) {
                genv.showMessage("Card is already at minimum slashable value.");
                return;
            }

            Random random = gtx.getRandom();
            int newVal = random.nextInt(originalValue - 1) + 1;
            c.setValue(newVal);

            genv.showMessage("Slashed " + c + "'s value from " + originalValue + " to " + newVal + ".");
        }

        @Override
        public boolean isValid(GameContext gtx) {
            return true;
        }
    },

    turn_two("Turn to Two", "Change a selected card's value to two", Rarity.LEGENDARY) {
        @Override
        public void apply(GameContext gtx, GameEnvironment genv) {
            Hand h = hand(gtx);
            if (h.isEmpty()) {
                genv.showMessage("Hand is empty.");
                return;
            }

            int num = InputHelper.getValidatedInput(
                gtx.getScanner(),
                1, h.size(),
                "Which card to Turn to Two? ",
                "There are only " + h.size() + " items in your hand."
            );

            Card c = h.getCard(num - 1);
            c.setValue(2);
            genv.showMessage("The value of " + c + " has been Turned to Two.");
        }

        @Override
        public boolean isValid(GameContext gtx) {
            return true;
        }
    },


    re_insert("Re-Insert", "Return the top card in your hand to one of the top four spots in the deck", Rarity.RARE) {
        @Override
        public void apply(GameContext gtx, GameEnvironment genv) {
            Hand h = hand(gtx);
            Deck d = deck(gtx);

            if (h.isEmpty()) {
                genv.showMessage("Hand is empty.");
                return;
            }
            if (d.size() < 4) {
                genv.showMessage("Not enough cards in deck.");
                return;
            }

            Card c = h.getCard(0);
            h.removeCard(c);

            PowerUpAction action = new PowerUpAction(
                "Choose a position (1-4) to return " + c + " to the top of the deck:",
                new String[] {"Position 1", "Position 2", "Position 3", "Position 4"}
            );

            int pos = action.execute(genv);
            d.addToIndex(c, pos - 1);

            genv.showMessage(c + " added to position " + pos + " in the top four cards of the deck.");
        }

        @Override
        public boolean isValid(GameContext gtx) {
            return true;
        }
    },

    reshuffle("Reshuffle", "Shuffle the remaining deck", Rarity.COMMON) {
        @Override
        public void apply(GameContext gtx, GameEnvironment genv) {
            deck(gtx).shuffle();
            genv.showMessage("Deck reshuffled.");
        }

        @Override
        public boolean isValid(GameContext gtx) {
            return true;
        }
    },

    swap("Swap", "Swap a random card from hand with one from the deck", Rarity.COMMON) {
        @Override
        public void apply(GameContext gtx, GameEnvironment genv) {
            Hand h = hand(gtx);
            Deck d = deck(gtx);

            if (h.isEmpty()) {
                genv.showMessage("Hand is empty.");
                return;
            }
            if (d.isEmpty()) {
                genv.showMessage("Deck is empty.");
                return;
            }

            Random random = gtx.getRandom();
            Card c = h.getCard(random.nextInt(h.size()));
            h.removeCard(c);

            Card drawn = d.draw();
            h.addCard(drawn);

            genv.showMessage("Lost " + c + " and drew " + drawn + " from the deck.");
        }

        @Override
        public boolean isValid(GameContext gtx) {
            return true;
        }
    },

    pick_swap("Pick-Swap", "Choose which card to swap from hand with deck", Rarity.RARE) {
        @Override
        public void apply(GameContext gtx, GameEnvironment genv) {
            Hand h = hand(gtx);
            Deck d = deck(gtx);

            if (h.isEmpty()) {
                genv.showMessage("Hand is empty.");
                return;
            }
            if (d.isEmpty()) {
                genv.showMessage("Deck is empty.");
                return;
            }

            int choice = InputHelper.getValidatedInput(
                gtx.getScanner(), 
                1, h.size(),
                "Which card to lose? ", 
                "There are only " + h.size() + " cards in your hand."
            );

            Card c = h.getCard(choice - 1);
            h.removeCard(c);

            Card drawn = d.draw();
            h.addCard(drawn);

            genv.showMessage("Lost " + c + " and drew " + drawn + " from the deck.");
        }

        @Override
        public boolean isValid(GameContext gtx) {
            return true;
        }
    },

    sabotage("Sabotage", "Lose a card and steal one from the house's hand", Rarity.LEGENDARY) {
    @Override
    public void apply(GameContext gtx, GameEnvironment genv) {
        Hand h = hand(gtx);
        Hand houseHand = rtx(gtx).getHouseHand();
        Deck d = deck(gtx);

        if (h.isEmpty()) {
            genv.showMessage("Hand is empty.");
            return;
        }
        if (houseHand.isEmpty()) {
            genv.showMessage("House's hand is empty.");
            return;
        }
        if (d.isEmpty()) {
            genv.showMessage("Deck is empty.");
            return;
        }

        int loseChoice = InputHelper.getValidatedInput(
            gtx.getScanner(), 1, h.size(),
            "Which card to lose? ", 
            "There are only " + h.size() + " cards in your hand."
        );
        Card lostCard = h.getCard(loseChoice - 1);
        h.removeCard(lostCard);

        int stealChoice = InputHelper.getValidatedInput(
            gtx.getScanner(), 1, houseHand.size(),
            "Which card to take from the house? ",
            "There are only " + houseHand.size() + " cards in the house's hand."
        );
        Card stolenCard = houseHand.getCard(stealChoice - 1);
        houseHand.removeCard(stolenCard);

        h.addCard(stolenCard);

        genv.showMessage("Lost " + lostCard + " and took " + stolenCard + " from the house.");
    }

    @Override
    public boolean isValid(GameContext gtx) {
        return true;
    }
},

    house_gamble("House Gamble", "Swap entire hand with the house's hand; hand value must be less than the house's standing point", Rarity.LEGENDARY) {
        @Override
        public void apply(GameContext gtx, GameEnvironment genv) {
            Hand h = hand(gtx);
            Hand houseHand = rtx(gtx).getHouseHand();
            int pointGoal = rtx(gtx).getPointGoal();

            if (h.getTotalValue() > pointGoal - 4) {
                genv.showMessage("Hand's value is greater than the standing points, cannot swap.");
                return;
            }

            ArrayList<Card> temp = new ArrayList<>(h.getHand());

            h.setHand(houseHand.getHand());
            houseHand.setHand(temp);

            genv.showMessage("Swapped hands with the house.");
        }

        @Override
        public boolean isValid(GameContext gtx) {
            return true;
        }
    },

   double_bjk("Double Blackjack", "Double the point goal for the game", Rarity.LEGENDARY) {
        @Override
        public void apply(GameContext gtx, GameEnvironment genv) {
            RoundContext rtx = rtx(gtx);
            if (rtx.isDBJused()) {
                genv.showMessage("Double Blackjack has already been used");
                return;
            }
            rtx.useDBJ();
            genv.showMessage("Point goal doubled!");
        }

        @Override
        public boolean isValid(GameContext gtx) {
            return !rtx(gtx).isDBJused();
        }
    },


    dump_hand("Dump Hand", "Discard entire hand immediately", Rarity.LEGENDARY) {
        @Override
        public void apply(GameContext gtx, GameEnvironment genv) {
            Hand h = hand(gtx);
            int dumped = h.size();

            h.setHand(new ArrayList<>());
            genv.showMessage("Hand dumped! You lost " + dumped + " cards.");
        }

        @Override
        public boolean isValid(GameContext gtx) {
            return true;
        }
    },

    backfire("Backfire", "Both player and house lose their highest-value card", Rarity.RARE) {
        @Override
        public void apply(GameContext gtx, GameEnvironment genv) {
            Hand h = hand(gtx);
            Hand houseHand = rtx(gtx).getHouseHand();

            if (h.isEmpty()) {
                genv.showMessage("Hand is empty.");
                return;
            }
            if (houseHand.isEmpty()) {
                genv.showMessage("House's hand is empty.");
                return;
            }

            Card handHighest = h.getHand().get(0);
            Card houseHighest = houseHand.getHand().get(0);

            for (Card c : h.getHand()) {
                if (c.getValue() > handHighest.getValue()) handHighest = c;
            }
            for (Card c : houseHand.getHand()) {
                if (c.getValue() > houseHighest.getValue()) houseHighest = c;
            }

            h.removeCard(handHighest);
            houseHand.removeCard(houseHighest);

            genv.showMessage("You lost " + handHighest + " of value " + handHighest.getValue() + 
                            " and the house lost " + houseHighest + " of value " + houseHighest.getValue() + ".");
        }

        @Override
        public boolean isValid(GameContext gtx) {
            return true;
        }
    },


    // Pocket power-up removed! 
    
    // pocket("Pocket", "Store a card from hand into pocket", Rarity.COMMON) {
    //     @Override
    //     public void apply(GameContext gtx, GameEnvironment genv) {
    //         Hand h = hand(gtx);
    //         if (h.isEmpty()) {
    //             System.out.println("Hand is empty.");
    //             return;
    //         }

    //         int num = InputHelper.getValidatedInput(gtx.getScanner(), 1, h.size(), 
    //                 "Which card to store? ", 
    //                 "There are only " + h.size() + " items in your hand.");
    //         Card c = h.getCard(num - 1);
    //         rtx(gtx).setPocket(c);
    //         h.removeCard(c);
    //         gtx.powerUps.add(use_pocket);

    //         System.out.println("Stored " + c + " in your pocket.");
    //     }

    //     @Override
    //     public boolean isValid(GameContext gtx) {
    //         return true;
    //     }
    // },

    // use_pocket("Use Pocket", "Retrieve card from pocket back to hand", Rarity.COMMON) {
    //     @Override
    //     public void apply(GameContext gtx, GameEnvironment genv) {
    //         Hand h = hand(gtx);
    //         if (!rtx(gtx).pocketUsed()) {
    //             System.out.println("Pocket is empty.");
    //             return;
    //         }

    //         Card c = rtx(gtx).getPocket();
    //         h.addCard(c);
    //         rtx(gtx).removeFromPocket();
    //         System.out.println(c + " added to hand from pocket.");
    //     }

    //     @Override
    //     public boolean isValid(GameContext gtx) {
    //         return false;
    //     }
    // },

    vault("Vault", "Store a card from hand into the vault, lasting into new matches", Rarity.RARE) {
        @Override
        public void apply(GameContext gtx, GameEnvironment genv) {
            Hand h = hand(gtx);
            if (h.isEmpty()) {
                genv.showMessage("Hand is empty.");
                return;
            }

            int num = InputHelper.getValidatedInput(gtx.getScanner(), 1, h.size(), 
                    "Which card to vault? ", 
                    "There are only " + h.size() + " items in your hand.");
            Card c = h.getCard(num - 1);
            gtx.setVault(c);
            h.removeCard(c);

            genv.showMessage("Stored " + c + " in your vault.");
        }

        @Override
        public boolean isValid(GameContext gtx) {
            return true;
        }
    },

    use_vault("Use Vault", "Retrieve card from vault back to hand", Rarity.COMMON) {
        @Override
        public void apply(GameContext gtx, GameEnvironment genv) {
            Hand h = hand(gtx);
            if (!gtx.vaultUsed()) {
                genv.showMessage("Vault is empty.");
                return;
            }

            Card c = gtx.getVault();
            h.addCard(c);
            gtx.removeFromVault();

            genv.showMessage(c + " added to hand from vault.");
        }

        @Override
        public boolean isValid(GameContext gtx) {
            return gtx.vaultUsed();
        }
    },

    bet_boost("Bet Boost", "Increase wager amount by a fixed amount of $100", Rarity.COMMON) {
        @Override
        public void apply(GameContext gtx, GameEnvironment genv) {
            RoundContext rtx = rtx(gtx);
            rtx.setWager(rtx.getWager() + WAGER_BOOST);
            genv.showMessage("Wager boosted by $" + WAGER_BOOST + ". Your wager is now $" + rtx.getWager() + ".");
        }

        @Override
        public boolean isValid(GameContext gtx) {
            return true;
        }
    },

    losing_bet("Losing Bet", "Lose 1/4th of the wager to dump your hand", Rarity.RARE) {
        @Override
        public void apply(GameContext gtx, GameEnvironment genv) {
            Hand h = hand(gtx);
            int dumped = h.size();

            RoundContext rtx = rtx(gtx);
            rtx.setWager(rtx.getWager() * 3 / 4);
            h.setHand(new ArrayList<>());

            genv.showMessage("Hand reset! You lost " + dumped + " cards and the wager is now $" + rtx.getWager() + ".");
        }

        @Override
        public boolean isValid(GameContext gtx) {
            return true;
        }
    },


    all_or_nothing("All or Nothing", "Wager all lives, double your lives if hand is won. Locks powerups!", Rarity.LEGENDARY) {
        @Override
        public void apply(GameContext gtx, GameEnvironment genv) {
            RoundContext rtx = rtx(gtx);
            rtx.useAON();
        }

        @Override
        public boolean isValid(GameContext gtx) {
            return !rtx(gtx).isAONused();
        }
    },

    gain_life("Life Boost", "Gain a life", Rarity.LEGENDARY) {
        @Override
        public void apply(GameContext gtx, GameEnvironment genv) {
            gtx.setLives(gtx.getLives() + 1);
            genv.showMessage("Gained a life! You now have " + gtx.getLives() + " lives.");
        }

        @Override
        public boolean isValid(GameContext gtx) {
            return true;
        }
    },

    double_jeopardy("Double Jeopardy", "Double your wager, but double the lives lost if hand is lost", Rarity.RARE) {
        @Override
        public void apply(GameContext gtx, GameEnvironment genv) {
            RoundContext rtx = rtx(gtx);
            rtx.useDJ();
            rtx.setWager(rtx.getWager() * 2);
            genv.showMessage("Double Jeopardy activated! Your wager is now $" + rtx.getWager() + ", but you'll lose 2 lives if you lose this round.");
        }

        @Override
        public boolean isValid(GameContext gtx) {
            return !rtx(gtx).isDJused();
        }
    },


    reset_powerups("Reset Powerups", "Trash all powerups, including this one; gain power-up points for each discarded", Rarity.RARE) {
        @Override
        public void apply(GameContext gtx, GameEnvironment genv) {
            int newPts = 0;
            ArrayList<PowerUp> pw = gtx.powerUps;
            for (PowerUp p : pw)
                newPts += p.getCost();
            pw.clear();
            int addedPoints = (newPts / 2) + Rarity.RARE.getDefaultCost();
            gtx.setPowerUpPts(gtx.getPowerUpPts() + addedPoints);

            genv.showMessage("Removed all powerups! You gained " + newPts + " points for a new total of " + (gtx.getPowerUpPts() - Rarity.RARE.getDefaultCost()) + ".");
        }

        @Override
        public boolean isValid(GameContext gtx) {
            return true;
        }
    };



    //Helper methods to cleanly access deck, hand
    private static RoundContext rtx(GameContext gtx) { return gtx.getRoundContext(); }
    private static Deck deck(GameContext gtx) { return rtx(gtx).getDeck(); }
    private static Hand hand(GameContext gtx) { return rtx(gtx).getPlayerHand(); }


    /*  Rarity Handling  */


    public enum Rarity {
    COMMON(10, 10),
    RARE(25, 5),
    LEGENDARY(75, 2);

    private final int defaultCost, weight;

    Rarity(int defaultCost, int weight) {
        this.defaultCost = defaultCost;
        this.weight = weight;
    }

    public int getDefaultCost() { return defaultCost; }
    public int getWeight() { return weight; }
}


    /*  PowerUp property definition   */


    private static int WAGER_BOOST = 100;
    private final String name;
    private final String desc;
    private final Rarity rarity;
    private int cost;

    PowerUp(String name, String desc, Rarity rarity) {
        this.name = name;
        this.desc = desc;
        this.rarity = rarity;
        this.cost = rarity.getDefaultCost();
    }

    public abstract void apply(GameContext gtx, GameEnvironment genv);
    public abstract boolean isValid(GameContext gtx);

    public String getName() { return name; }
    public String getDesc() { return desc; }
    public Rarity getRarity() { return rarity; }

    public void setCost(int c) { cost = c; }
    public int getCost() { return cost; }

    public String toString() { return rarity +  "[" + cost + "]" + ": " + name + " --- " + desc; }
}