package com.qxu.blackjack.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.qxu.blackjack.card.Card;

public class Hand implements Iterable<Card> {
	private List<Card> cards;
	private long bet;
	private long insuranceBet;
	private boolean stand = false;
	private boolean dead = false;
	private int splitDepth;

	public Hand() {
		this.cards = new ArrayList<Card>();
	}

	public void addCard(Card c) {
		cards.add(c);
	}

	public Card getCard(int index) {
		return cards.get(index);
	}

	public int getSize() {
		return cards.size();
	}

	public long getBet() {
		return bet;
	}

	public long getInsuranceBet() {
		return insuranceBet;
	}

	public boolean isBlackjack() {
		return cards.size() == 2 && getSum() == 21;
	}

	public boolean contains(Card.Rank rank) {
		for (Card c : this) {
			if (c.getRank().equals(rank)) {
				return true;
			}
		}
		return false;
	}

	public int getSum() {
		int numOfAces = 0;
		int sum = 0;
		for (Card c : this) {
			if (c.getRank().equals(Card.Rank.ACE)) {
				sum += 1;
				numOfAces++;
			} else {
				sum += BlackjackUtils.getRankValue(c.getRank());
			}
		}
		while (numOfAces > 0 && sum <= (21 - 10)) {
			sum += 10;
			numOfAces--;
		}
		return sum;
	}

	public int getHardSum() {
		int sum = 0;
		for (Card c : this) {
			sum += BlackjackUtils.getRankValue(c.getRank());
		}
		return sum;
	}

	public void addBet(long bet) {
		this.bet += bet;
	}

	public void addInsuranceBet(long bet) {
		this.insuranceBet += bet;
	}

	@Override
	public Iterator<Card> iterator() {
		return new HandIterator();
	}

	public boolean isStand() {
		return stand;
	}

	public void stand() {
		this.stand = true;
	}

	public int getSplitDepth() {
		return splitDepth;
	}

	public void setSplitDepth(int splitDepth) {
		this.splitDepth = splitDepth;
	}

	public boolean isDead() {
		return dead;
	}

	public void die() {
		this.dead = true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (bet > 0) {
			sb.append("Bet ");
			sb.append(bet);
			if (insuranceBet > 0) {
				sb.append(", Insurance ");
				sb.append(insuranceBet);
			}
			sb.append(" ");
		}
		sb.append(cards);
		sb.append("=");
		sb.append(getSum());
		return sb.toString();
	}

	public List<Card> getModifiableCardList() {
		return cards;
	}

	private class HandIterator implements Iterator<Card> {
		Iterator<Card> iter;

		HandIterator() {
			iter = cards.iterator();
		}

		@Override
		public boolean hasNext() {
			return iter.hasNext();
		}

		@Override
		public Card next() {
			return iter.next();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
