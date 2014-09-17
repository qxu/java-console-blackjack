package com.qxu.blackjack.card;

import java.util.Arrays;
import java.util.Random;

public final class Deck {
	private static final Random SHUFFLER = new Random();

	private Card[] cards;
	private int currentIndex;

	public Deck() {
		this(1);
	}

	public Deck(int numOfDecks) {
		if (numOfDecks < 0) {
			throw new IllegalArgumentException("negative numOfDecks");
		}
		this.cards = new Card[numOfDecks * Card.NUM_OF_CARDS];
		for (int i = 0; i < cards.length; ++i)
			cards[i] = Card.fromOrdinal(i);
		currentIndex = cards.length;
	}

	public static Deck shuffledDeck() {
		Deck deck = new Deck();
		deck.shuffle();
		return deck;
	}

	public int size() {
		return currentIndex;
	}

	public void shuffle() {
		for (int i = cards.length - 1; i > 0; --i) {
			swap(i, SHUFFLER.nextInt(i + 1));
		}
	}

	public void sort() {
		Arrays.sort(cards, CardComparator.ORDINAL_COMPARATOR);
	}

	public Card deal() {
		if (currentIndex <= 0)
			throw new EmptyDeckException("Cannot deal from an empty deck");
		return cards[--currentIndex];
	}

	public Card[] deal(int n) {
		if (n < 0)
			throw new IllegalArgumentException("Cannot deal " + n + " cards");
		if (n > currentIndex)
			throw new EmptyDeckException("Cannot deal " + n + " cards");

		Card[] buf = new Card[n];
		int newCurrentIndex = currentIndex - n;
		System.arraycopy(cards, newCurrentIndex, buf, 0, n);
		currentIndex = newCurrentIndex;
		return buf;
	}

	public boolean hasDealt(Card c) {
		for (int i = currentIndex; i < cards.length; ++i)
			if (c == cards[i])
				return true;
		return false;
	}

	public void putBack(Card... c) {
		for (Card cur : c) {
			if (!hasDealt(cur))
				throw new IllegalArgumentException("Card " + cur
						+ " has not been dealt");
		}
		for (Card cur : c) {
			int i = currentIndex;
			while (cur != cards[i])
				++i;

			swap(i, currentIndex);
			++currentIndex;
		}
	}

	public void putFront(Card c) {
		if (!hasDealt(c))
			throw new IllegalArgumentException("Card " + c
					+ " has not been dealt");

		int i = currentIndex;
		while (c != cards[i])
			++i;

		System.arraycopy(cards, 0, cards, 1, i);
		cards[0] = c;
		++currentIndex;
	}

	public String toString() {
		int iMax = size() - 1;
		if (iMax == -1)
			return "[]";

		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (int i = 0;; i++) {
			sb.append(String.valueOf(cards[i]));
			if (i == iMax)
				return sb.append(']').toString();
			sb.append(", ");
		}
	}

	public void restore() {
		currentIndex = cards.length;
	}

	public Card[] toArray() {
		Card[] buf = new Card[size()];
		System.arraycopy(cards, 0, buf, 0, currentIndex);
		return buf;
	}

	public Card getCardInstance(int hash) {
		for (Card c : cards) {
			if (c.hashCode() == hash)
				return c;
		}
		throw new IllegalArgumentException("Illegal hash " + hash);
	}

	private void swap(int index0, int index1) {
		Card tmp = cards[index0];
		cards[index0] = cards[index1];
		cards[index1] = tmp;
	}
}
