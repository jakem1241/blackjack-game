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
        for (Card c : hand)
            total += c.getValue();
        return total;
    }
}
