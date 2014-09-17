package com.qxu.blackjack.card;

/**
 * Immutable cards
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Card {
	// rgb(153, 0, 0)
	public static enum Suit {
		CLUBS("\u2663"), DIAMONDS("\u2666"), HEARTS("\u2665"), SPADES("\u2660");

		private final String symbol;

		private Suit(String sym) {
			this.symbol = sym;
		}

		public String getSymbol() {
			return symbol;
		}

		@Override
		public String toString() {
			// return name().substring(0, 1);
			return String.valueOf(symbol);
		}
	}

	public static enum Rank {
		TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"), EIGHT(
				"8"), NINE("9"), TEN("10"), JACK("J"), QUEEN("Q"), KING("K"), ACE(
				"A");

		private final String symbol;

		private Rank(String s) {
			this.symbol = s;
		}

		@Override
		public String toString() {
			return symbol;
		}
	}

	/* constants */
	public static final int NUM_OF_CARDS = 52;
	public static final int NUM_OF_SUITS = 4;
	public static final int NUM_OF_RANKS = 13;

	/* cache for quick lookup */
	private static final Card[] values;
	static {
		values = new Card[NUM_OF_CARDS];
		for (Suit s : Suit.values())
			for (Rank r : Rank.values()) {
				Card c = new Card(s, r);
				values[c.ordinal] = c;
			}
	}

	/*
	 * static methods
	 */
	public static Card fromOrdinal(int ordinal) {
		if (ordinal < 0 || ordinal >= NUM_OF_CARDS)
			throw new IllegalArgumentException("Invalid ordinal " + ordinal);
		return values[ordinal].copy();
	}

	public static Card from(Suit suit, Rank rank) {
		return fromOrdinal(suit.ordinal() * NUM_OF_RANKS + rank.ordinal());
	}

	public static Card[] getAllCards() {
		return values.clone();
	}

	/*
	 * instance variables and methods
	 */

	private final Suit suit;
	private final Rank rank;
	private final int ordinal;

	private Card(Suit suit, Rank rank) {
		this(suit, rank, suit.ordinal() * NUM_OF_RANKS + rank.ordinal());
	}

	private Card(Suit suit, Rank value, int ordinal) {
		this.suit = suit;
		this.rank = value;
		this.ordinal = ordinal;
	}

	public Card copy() {
		return new Card(suit, rank, ordinal);
	}

	public Suit getSuit() {
		return suit;
	}

	public Rank getRank() {
		return rank;
	}

	public int getOrdinal() {
		return ordinal;
	}

	@Override
	public int hashCode() {
		return getOrdinal();
	}

	@Override
	public String toString() {
		return rank.toString() + suit.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Card))
			return false;
		return o.hashCode() == this.hashCode();
	}

	/*
	 * -------- DEBUGGING CODE --------
	 */
	public static List<Card> fromStrings(String... cards) {
		List<Card> cardList = new ArrayList<Card>(cards.length);
		for (String s : cards) {
			cardList.add(Card.fromString(s));
		}
		return cardList;
	}

	public static Card fromString(String s) {
		Card c = STRING_MAP.get(s.toUpperCase());

		if (c == null)
			throw new IllegalArgumentException("Cannot match a Card from \""
					+ s + "\"");
		return c.copy();
	}

	private static final Map<String, Card> STRING_MAP = new HashMap<String, Card>(
			NUM_OF_CARDS * 3);
	static {
		for (Card c : values) {
			STRING_MAP.put(c.toString(), c);
			STRING_MAP.put(c.rank.toString() + c.suit.name().charAt(0), c);
			STRING_MAP.put(c.rank.toString() + " OF " + c.suit.name(), c);
		}
	}

	// END DEBUG
}
