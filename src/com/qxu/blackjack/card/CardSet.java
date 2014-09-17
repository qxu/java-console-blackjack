package com.qxu.blackjack.card;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class CardSet extends AbstractSet<Card> implements Set<Card> {

	private long bits;

	public CardSet() {
		this.bits = 0;
	}

	public CardSet(Collection<Card> c) {
		if (c instanceof CardSet) {
			CardSet cs = (CardSet) c;
			this.bits = cs.bits;
		} else {
			this.bits = 0;
			addAll(c);
		}
	}

	@Override
	public boolean add(Card c) {
		long oldBits = bits;
		this.bits |= 1L << c.getOrdinal();
		return bits != oldBits;
	}

	@Override
	public boolean remove(Object o) {
		if (!(o instanceof Card)) {
			return false;
		}
		Card c = (Card) o;
		long oldBits = bits;
		this.bits &= ~(1L << c.getOrdinal());
		return bits != oldBits;
	}

	@Override
	public int size() {
		return Long.bitCount(bits);
	}

	@Override
	public boolean isEmpty() {
		return bits == 0;
	}

	@Override
	public boolean contains(Object o) {
		if (!(o instanceof Card)) {
			return false;
		}
		Card c = (Card) o;
		return (bits & (1L << c.getOrdinal())) != 0;
	}

	@Override
	public void clear() {
		this.bits = 0;
	}

	@Override
	public Iterator<Card> iterator() {
		return new CardSetIterator();
	}

	private class CardSetIterator implements Iterator<Card> {
		long unseenBits;
		long prevBit = 0;

		CardSetIterator() {
			this.unseenBits = bits;
		}

		public boolean hasNext() {
			return unseenBits != 0;
		}

		public Card next() {
			if (unseenBits == 0)
				throw new NoSuchElementException();
			prevBit = unseenBits & -unseenBits;
			unseenBits -= prevBit;
			return Card.fromOrdinal(Long.numberOfTrailingZeros(prevBit));
		}

		public void remove() {
			if (prevBit == 0)
				throw new IllegalStateException();
			bits &= ~prevBit;
			prevBit = 0;
		}
	}
}
