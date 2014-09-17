package com.qxu.blackjack.table;

import java.util.ArrayList;
import java.util.List;

import com.qxu.blackjack.card.Card;
import com.qxu.blackjack.card.Deck;
import com.qxu.blackjack.table.TableRules.BlackjackPayoutRatio;

public class Table implements Runnable {
	private TableRules rules;
	private Deck deck;
	private Player dealer;
	private List<Player> players;
	private boolean dealerTurn;
	private boolean inRound;

	private TableEventHandler listener;

	public Table() {
		this(new TableRules());
	}

	public Table(TableRules rules) {
		this.rules = rules;
		this.deck = new Deck(rules.getNumOfDecks());
		this.dealer = new Dealer();
		this.players = new ArrayList<Player>();
	}

	public void addPlayer(Player p) {
		if (p == null) {
			throw new IllegalArgumentException("null player");
		}
		this.players.add(p);
		p.setTable(this);
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			doRound();
		}
	}

	protected void doRound() {
		startRound();
		queryBets();
		dealDealer();

		if (!dealerTurn) {
			dealPlayers();
		}
		while (!dealerTurn) {
			doPlayerTurns();
		}
		notifyListener();

		Hand hand = dealer.getHands().get(0);
		while (!hand.isStand() && !hand.isDead()) {
			doDealerTurn();
			notifyListener();
		}
		if (!hand.isDead()) {
			int sum = hand.getSum();
			boolean blackjack = hand.isBlackjack();
			for (Player p : players) {
				for (Hand h : p.getHands()) {
					if (!h.isDead()) {
						long bet = h.getBet();
						h.die();
						notifyListener();
						if (h.getSum() > sum) {
							p.addBank(bet + bet);
							dealer.addBank(-bet);
						} else if (blackjack || rules.dealerWinsTies()
								|| h.getSum() < sum) {
							dealer.addBank(bet);
						} else {
							p.addBank(bet);
						}
						notifyListener();
					}
				}
			}
		} else {
			for (Player p : players) {
				for (Hand h : p.getHands()) {
					if (!h.isDead()) {
						long bet = h.getBet();
						h.die();
						notifyListener();
						p.addBank(bet + bet);
						dealer.addBank(-bet);
						notifyListener();
					}
				}
			}
		}

		endRound();
	}

	protected void startRound() {
		deck.shuffle();
		this.dealerTurn = false;
		inRound = true;
		notifyListener();
	}

	protected void queryBets() {
		for (Player p : players) {
			Hand h = new Hand();
			p.addHand(h);
			p.placeBet(h);
			notifyListener();
		}
	}

	protected void dealDealer() {
		Hand hand = new Hand();
		dealer.addHand(hand);

		hand.addCard(deck.deal());
		notifyListener();

		Card.Rank firstCardRank = hand.getCard(0).getRank();

		if (rules.getSurrenderType().equals(TableRules.SurrenderType.EARLY)) {
			if (firstCardRank.equals(Card.Rank.ACE)
					|| BlackjackUtils.getRankValue(firstCardRank) == 10) {
				for (Player p : players) {
					for (Hand h : p.getHands()) {
						if (p.querySurrender()) {
							long halfBet = h.getBet() / 2;
							h.stand();
							h.die();
							notifyListener();
							p.addBank(halfBet);
							dealer.addBank(h.getBet() - halfBet);
						}
						notifyListener();
					}
				}
			}
		}

		if (firstCardRank.equals(Card.Rank.ACE)) {
			for (Player p : players) {
				for (Hand h : p.getHands()) {
					p.placeInsuranceBet(h);
					notifyListener();
				}
			}
		}

		switch (rules.getHoleCardGameType()) {
		case NO_HOLE_CARD_GAME:
		case ORIGINAL_BETS_ONLY:
			break;
		case HOLE_CARD_GAME:
			hand.addCard(deck.deal());
			notifyListener();
			if (hand.isBlackjack()) {
				dealerTurn = true;
				notifyListener();
			}
			break;
		default:
			throw new AssertionError();
		}
	}

	protected void dealPlayers() {
		this.dealerTurn = true;
		for (Player p : players) {
			for (Hand h : p.getHands()) {
				h.addCard(deck.deal());
				notifyListener();
				h.addCard(deck.deal());
				notifyListener();
				if (h.isBlackjack()) {
					doBlackjackPayout(p, h);
				} else {
					this.dealerTurn = false;
				}
			}
		}
	}

	protected void doBlackjackPayout(Player p, Hand h) {
		long bet = h.getBet();
		h.stand();
		h.die();
		notifyListener();
		BlackjackPayoutRatio ratio = rules.getBlackjackPayoutRatio();
		long payout = bet * ratio.getNumerator() / ratio.getDenominator();
		p.addBank(payout + h.getBet());
		dealer.addBank(-payout);
		notifyListener();
	}

	protected void doPlayerTurns() {
		this.dealerTurn = true;
		for (Player p : players) {
			for (Hand h : p.getHands()) {
				if (!h.isDead() && !h.isStand()) {
					PlayerDecision move = p.queryHandDecision(h);
					notifyListener();
					switch (move) {
					case HIT:
						h.addCard(deck.deal());
						notifyListener();
						if (h.getSum() > 21) {
							h.die();
							notifyListener();
							dealer.addBank(h.getBet());
							notifyListener();
						} else {
							this.dealerTurn = false;
						}
						break;
					case STAND:
						h.stand();
						notifyListener();
						break;
					case DOUBLE_DOWN:
						h.addCard(deck.deal());
						notifyListener();

						if (h.getSum() > 21) {
							h.die();
							notifyListener();
							dealer.addBank(h.getBet());
							notifyListener();
						} else {
							h.stand();
							notifyListener();
						}
						break;
					case SPLIT:
						p.removeHand(h);
						long bet = h.getBet();
						p.addBank(-bet);
						// TODO if bet > bank
						Hand split1 = new Hand();
						split1.addBet(bet);
						split1.addCard(h.getCard(0));
						split1.addCard(deck.deal());
						p.addHand(split1);

						Hand split2 = new Hand();
						split2.addBet(bet);
						split2.addCard(h.getCard(1));
						split2.addCard(deck.deal());
						p.addHand(split2);

						notifyListener();

						if (split1.isBlackjack()) {
							doBlackjackPayout(p, split1);
						} else {
							this.dealerTurn = false;
						}
						if (split2.isBlackjack()) {
							doBlackjackPayout(p, split2);
						} else {
							this.dealerTurn = false;
						}
						break;
					default:
						throw new AssertionError();
					}
				}
			}
		}
	}

	protected void doDealerTurn() {
		Hand hand = dealer.getHands().get(0);

		if (hand.isBlackjack()) {
			hand.stand();
		} else {
			int sum = hand.getSum();
			if (sum > 21) {
				hand.die();
			} else if (sum < 17
					|| (sum == 17 && rules.dealerHitsOnSoft17()
							&& hand.contains(Card.Rank.ACE) && sum == hand
							.getHardSum())) {
				hand.addCard(deck.deal());
			} else {
				hand.stand();
			}
		}
	}

	protected void endRound() {
		inRound = false;
		notifyListener();
		dealer.clearHands();
		for (Player p : players) {
			p.clearHands();
		}
	}

	protected void notifyListener() {
		if (listener != null) {
			listener.tableChanged();
		}
	}

	public TableRules getRules() {
		return rules;
	}

	public Player getDealer() {
		return this.dealer;
	}

	public List<Player> getPlayers() {
		return new ArrayList<Player>(players);
	}

	public Deck getDeck() {
		return deck;
	}

	public boolean isDealerTurn() {
		return dealerTurn;
	}

	public boolean isInRound() {
		return inRound;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(dealer);
		sb.append("\n");
		for (Player p : players) {
			sb.append(p);
			sb.append("\n");
		}
		return sb.toString();
	}

	public TableEventHandler getListener() {
		return listener;
	}

	public void setListener(TableEventHandler listener) {
		this.listener = listener;
	}
}
