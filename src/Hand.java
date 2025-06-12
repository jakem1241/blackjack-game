import java.util.ArrayList;

public class Hand {

    private ArrayList<Card> hand;

    public Hand() {
        this.hand = initHand();
    }

    public ArrayList<Card> initHand() { return new ArrayList<Card>(); }
    public ArrayList<Card> getHand() { return hand; }
    public void setHand(ArrayList<Card> newHand) { hand = new ArrayList<>(newHand); }

    public Card getCard(int x) { return hand.get(x); }
    public void addCard(Card card) { hand.add(0, card); }
    public boolean removeCard(Card card) { return hand.remove(card); }
    public int size() { return hand.size(); }
    public boolean isEmpty() { return hand.isEmpty(); }

    public int getTotalValue() {
        int total = 0;
        for (Card c : hand) {
            total += c.getValue();
        }
        return total;
    }
    
    public static void printBothHands(Hand player, Hand house, boolean houseActive, int housePointGoal) {
        
        System.out.println("--------------------------------------------------------------------------------------------------\n");
        
        if (!houseActive)
            System.out.println("The house stands as the value of their hand has reached " + housePointGoal + " or higher.\n");

        System.out.println("Player Hand:\t\t\t\tHouse Hand:");

        int maxSize = Math.max(player.size(), house.size());
        for (int i = 0; i < maxSize; i++) {
            String playerCard = i < player.size() ? (i + 1) + ") " + player.getCard(i).toString() : "";
            String houseCard = i < house.size() ? (i + 1) + ") " + house.getCard(i).toString() : "";
            
            //Adjust spacing based on longest possible card string to keep columns aligned
            System.out.printf("%-40s%s%n", playerCard, houseCard);
        }

        System.out.println();
        System.out.printf("Total Hand Value: %-19dTotal Hand Value: %d%n", 
        player.getTotalValue(), house.getTotalValue());
        System.out.println();
    }   

}
