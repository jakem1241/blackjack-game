import java.util.ArrayList;
import java.util.Collections;

public class Deck {

    final int INIT_SIZE = 52;
    private ArrayList<Card> deck;

    public Deck() {
        this.deck = initDeck();
    }

    public ArrayList<Card> initDeck() {
        
        ArrayList<Card> d = new ArrayList<>();
        for (Card.Suit s : Card.Suit.values()) {
            for (Card.Rank r : Card.Rank.values()) 
                {d.add(new Card(s, r)); }
        }
        Collections.shuffle(d);
        return d;
    }
    public Card draw() {
        if (isEmpty()) return null;
        Card c = peek();
        deck.remove(0);
        return c;
    }
    public Card peek() {
        if (isEmpty()) return null;
        return deck.get(0);
    }
    public Card peekAtIndex(int x) {
        if (x > size()-1) return null;
        return deck.get(x); 
    }

    public boolean removeCard(Card c) { return deck.remove(c); }
    public void addToTop(Card c) { deck.add(0, c); }
    public void addToIndex(Card c, int x) { deck.add(x, c); }
    public void shuffle() { Collections.shuffle(deck); }

    public int size() { return deck.size(); }
    public boolean isEmpty() { return deck.isEmpty(); }
}
