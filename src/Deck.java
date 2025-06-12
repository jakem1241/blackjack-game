import java.util.ArrayList;
import java.util.Collections;

public class Deck {

    final int INIT_SIZE = 52;
    private ArrayList<Card> arr;

    public Deck() {
        this.arr = initDeck();
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
        arr.remove(0);
        return c;
    }
    public Card peek() {
        if (isEmpty()) return null;
        return arr.get(0);
    }
    public Card peekAtIndex(int x) {
        if (x > size()-1) return null;
        return arr.get(x); 
    }

    public boolean removeCard(Card c) { return arr.remove(c); }
    public void addToTop(Card c) { arr.add(0, c); }
    public void addToIndex(Card c, int x) { arr.add(x, c); }
    public void shuffle() { Collections.shuffle(arr); }

    public int size() { return arr.size(); }
    public boolean isEmpty() { return arr.isEmpty(); }
    public void reset() { arr = initDeck(); }

    public void printDeck() {
        for (Card c : arr) { System.out.println(c.toString()); }
    }


}
