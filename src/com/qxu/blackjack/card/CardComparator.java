package com.qxu.blackjack.card;

import java.util.Comparator;

public enum CardComparator implements Comparator<Card> {
	ORDINAL_COMPARATOR {
		@Override
		public int compare(Card c0, Card c1) {
			return c0.hashCode() - c1.hashCode();
		}
	},
	RANK_COMPARATOR {
		@Override
		public int compare(Card c0, Card c1) {
			return c0.getRank().ordinal() - c1.getRank().ordinal();
		}
	};
}
