package com.qxu.blackjack.table;

public class TableRules {
	private boolean dealerHitsOnSoft17 = false;
	private int numOfDecks = 1;
	private SurrenderType surrenderType = SurrenderType.EARLY;
	private int resplitLimit = Integer.MAX_VALUE;
	private boolean canHitSplitAces = true;
	private boolean canResplitSplitAces = true;
	private boolean canDoubleAfterSplit = true;
	private DoubleDownAllowance doubleDownAllowance = DoubleDownAllowance.ON_ALL;
	private HoleCardGameType holeCardGameType = HoleCardGameType.NO_HOLE_CARD_GAME;
	private BlackjackPayoutRatio blackjackPayoutRatio = new BlackjackPayoutRatio(
			3, 2);
	private boolean dealerWinsTies = false;

	public TableRules() {
	}

	public boolean dealerHitsOnSoft17() {
		return dealerHitsOnSoft17;
	}

	public void setDealerHitsOnSoft17(boolean dealerHitsOnSoft17) {
		this.dealerHitsOnSoft17 = dealerHitsOnSoft17;
	}

	public int getNumOfDecks() {
		return numOfDecks;
	}

	public void setNumOfDecks(int numOfDecks) {
		if (numOfDecks <= 0) {
			throw new IllegalArgumentException(
					"Number of decks must be positive.");
		}
		this.numOfDecks = numOfDecks;
	}

	public SurrenderType getSurrenderType() {
		return surrenderType;
	}

	public void setSurrenderType(SurrenderType surrenderType) {
		if (surrenderType == null) {
			throw new IllegalArgumentException("null surrenderType");
		}
		this.surrenderType = surrenderType;
	}

	public int getResplitLimit() {
		return resplitLimit;
	}

	public void setResplitLimit(int resplitLimit) {
		if (resplitLimit < 0) {
			resplitLimit = Integer.MAX_VALUE;
		} else {
			this.resplitLimit = resplitLimit;
		}
	}

	public boolean canHitSplitAces() {
		return canHitSplitAces;
	}

	public void setCanHitSplitAces(boolean canHitSplitAces) {
		this.canHitSplitAces = canHitSplitAces;
	}

	public boolean canResplitSplitAces() {
		return canResplitSplitAces;
	}

	public void setCanResplitSplitAces(boolean canResplitSplitAces) {
		this.canResplitSplitAces = canResplitSplitAces;
	}

	public boolean canDoubleAfterSplit() {
		return canDoubleAfterSplit;
	}

	public void setCanDoubleAfterSplit(boolean canDoubleAfterSplit) {
		this.canDoubleAfterSplit = canDoubleAfterSplit;
	}

	public DoubleDownAllowance getDoubleDownAllowance() {
		return doubleDownAllowance;
	}

	public void setDoubleDownAllowance(DoubleDownAllowance doubleDownAllowance) {
		if (doubleDownAllowance == null) {
			throw new IllegalArgumentException("null doubleDownAllowance");
		}
		this.doubleDownAllowance = doubleDownAllowance;
	}

	public HoleCardGameType getHoleCardGameType() {
		return holeCardGameType;
	}

	public void setHoleCardGameType(HoleCardGameType holeCardGameType) {
		if (holeCardGameType == null) {
			throw new IllegalArgumentException("null holeCardGameType");
		}
		this.holeCardGameType = holeCardGameType;
	}

	public BlackjackPayoutRatio getBlackjackPayoutRatio() {
		return blackjackPayoutRatio;
	}

	public void setBlackjackPayoutRatio(
			BlackjackPayoutRatio blackjackPayoutRatio) {
		if (blackjackPayoutRatio == null) {
			throw new IllegalArgumentException("null blackjackPayoutRatio");
		}
		this.blackjackPayoutRatio = blackjackPayoutRatio;
	}

	public boolean dealerWinsTies() {
		return dealerWinsTies;
	}

	public void setDealerWinsTies(boolean dealerWinsTies) {
		this.dealerWinsTies = dealerWinsTies;
	}

	public static enum SurrenderType {
		NONE, EARLY, LATE;
	}

	public static enum DoubleDownAllowance {
		ON_ALL, ON_9_10_11_ONLY, ON_10_11_ONLY;
	}

	public static enum HoleCardGameType {
		NO_HOLE_CARD_GAME, HOLE_CARD_GAME, ORIGINAL_BETS_ONLY;
	}

	public static class BlackjackPayoutRatio {
		private final int num;
		private final int den;

		public BlackjackPayoutRatio(int num, int den) {
			if (num < 0 || den <= 0) {
				throw new IllegalArgumentException(
						"Illegal numerator or denominator");
			}
			this.num = num;
			if (num == 0) {
				this.den = 1;
			} else {
				this.den = den;
			}
		}

		public int getNumerator() {
			return num;
		}

		public int getDenominator() {
			return den;
		}

		@Override
		public String toString() {
			return num + ":" + den;
		}
	}
}
