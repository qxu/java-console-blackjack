package com.qxu.blackjack.table;

public class Dealer extends Player {

	public Dealer() {
		super("Dealer");
	}

	@Override
	public long queryBet(Hand h) {
		throw new UnsupportedOperationException();
	}

	@Override
	public long queryInsuranceBet(Hand h) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean querySurrender() {
		throw new UnsupportedOperationException();
	}

	@Override
	public PlayerDecision queryHandDecision(Hand h) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
