
public class Card {

    public enum Suit {
        HEARTS, DIAMONDS, CLUBS, SPADES
    }

    //Blackjack value of cards
    public enum Rank {
        TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), 
        NINE(9), TEN(10), JACK(10), QUEEN(10), KING(10), ACE(11);

        private final int value;

        Rank(int value) { this.value = value; }
        public int getRankValue() { return value; }
    }

    private Suit suit;
    private Rank rank;
    private int value;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
        this.value = rank.getRankValue();
    }

    public void setValue(int v) { value = v; }
    public int getValue() { return value; }
    public Rank getRank() { return rank; }
    public void setSuit(Suit s) { suit = s; }
    public Suit getSuit() { return suit; }
    public boolean isFaceCard() { return (rank.getRankValue() == 10 && rank != Rank.TEN); }
    public String toString() { return String.format("%s of %s (%d)", rank, suit, value); }
}