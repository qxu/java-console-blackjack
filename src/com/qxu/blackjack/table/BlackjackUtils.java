package com.qxu.blackjack.table;

import java.util.EnumSet;
import java.util.Set;

import com.qxu.blackjack.card.Card;

public class BlackjackUtils {

	public static int getRankValue(Card.Rank rank) {
		if (rank.equals(Card.Rank.ACE)) {
			return 11;
		}
		int o = rank.ordinal();
		if (o >= Card.Rank.TWO.ordinal() && o <= Card.Rank.TEN.ordinal()) {
			return o - Card.Rank.TWO.ordinal() + 2;
		} else {
			return 10;
		}
	}

	public static Set<PlayerDecision> getPossibleDecisions(TableRules rules,
			Hand h) {
		EnumSet<PlayerDecision> moves = EnumSet.of(PlayerDecision.HIT,
				PlayerDecision.STAND);
		if (h.getSplitDepth() == 0 || rules.canDoubleAfterSplit()) {
			moves.add(PlayerDecision.DOUBLE_DOWN);
		}
		if (h.getSize() == 2
				&& h.getCard(0).getRank().equals(h.getCard(1).getRank())) {
			if (!h.getCard(0).getRank().equals(Card.Rank.ACE)
					|| rules.canResplitSplitAces()) {
				moves.add(PlayerDecision.SPLIT);
			}
		}
		return moves;
	}

	private BlackjackUtils() {
	}
}
