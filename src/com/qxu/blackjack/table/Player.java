package com.qxu.blackjack.table;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
	private String name;
	private List<Hand> hands;
	private long bank;
	private Table table;

	public Player(String name) {
		setName(name);
		this.hands = new ArrayList<Hand>();
	}

	public void removeHand(Hand h) {
		hands.remove(h);
	}

	public void clearHands() {
		this.hands = new ArrayList<Hand>();
	}

	public void addHand(Hand h) {
		if (h == null) {
			throw new IllegalArgumentException("null hand");
		}
		hands.add(h);
	}

	public List<Hand> getHands() {
		return hands;
	}

	public long getBank() {
		return this.bank;
	}

	public void addBank(long amount) {
		this.bank += amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name == null) {
			throw new IllegalArgumentException("null name");
		}
		this.name = name;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		if (table == null) {
			throw new IllegalArgumentException("null table");
		}
		this.table = table;
	}

	public void placeBet(Hand h) {
		long bet = queryBet(h);
		h.addBet(bet);
		addBank(-bet);
	}

	public void placeInsuranceBet(Hand h) {
		long bet = queryInsuranceBet(h);
		h.addInsuranceBet(bet);
	}

	public abstract long queryBet(Hand h);

	public abstract long queryInsuranceBet(Hand h);

	public abstract boolean querySurrender();

	public abstract PlayerDecision queryHandDecision(Hand h);

	@Override
	public String toString() {
		return name + " (" + bank + "): " + hands;
	}
}
