package com.qxu.blackjack.console;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.qxu.blackjack.card.Card;
import com.qxu.blackjack.table.BlackjackUtils;
import com.qxu.blackjack.table.Hand;
import com.qxu.blackjack.table.Player;
import com.qxu.blackjack.table.PlayerDecision;

public class ConsolePlayer extends Player {

	private ConsoleDisplay console;

	public ConsolePlayer(ConsoleDisplay console) {
		super("You");
		this.console = console;
	}

	@Override
	public PlayerDecision queryHandDecision(Hand h) {
		List<PlayerDecision> options = getOptions(h);
		Collections.sort(options, new Comparator<PlayerDecision>() {
			@Override
			public int compare(PlayerDecision o1, PlayerDecision o2) {
				return o1.ordinal() - o2.ordinal();
			}
		});

		String message = getMessage(options);
		boolean inputSuccess = false;
		int optionIndex = 0;
		do {
			String input = console.promptUser(message);
			if (input.startsWith("c")) {
				String[] params = input.split("=");
				int cIndex = Integer.parseInt(params[0].substring(1));
				List<Card> cards = h.getModifiableCardList();
				cards.set(cIndex, Card.fromString(params[1]));
				options = getOptions(h);
				message = getMessage(options);
				console.reprint();
				continue;
			}
			int inputInt;
			try {
				inputInt = Integer.parseInt(input);
			} catch (NumberFormatException e) {
				continue;
			}
			if (inputInt > 0 && inputInt <= options.size()) {
				optionIndex = inputInt - 1;
				inputSuccess = true;
			}
		} while (!inputSuccess);
		return options.get(optionIndex);
	}

	private List<PlayerDecision> getOptions(Hand h) {
		return new ArrayList<PlayerDecision>(
				BlackjackUtils.getPossibleDecisions(getTable().getRules(), h));
	}

	private String getMessage(List<PlayerDecision> options) {
		StringBuilder messageSb = new StringBuilder();
		messageSb.append("Choose an option: [");
		for (int i = 0; i < options.size(); i++) {
			messageSb.append(i + 1);
			messageSb.append(": ");
			messageSb.append(options.get(i));
			if (i == options.size() - 1) {
				break;
			}
			messageSb.append(", ");
		}
		messageSb.append("]");
		return messageSb.toString();
	}

	@Override
	public long queryBet(Hand h) {
		boolean inputSuccess = false;
		long bet = 0;
		do {
			String input = console.promptUser("Enter a bet: ");
			long inputLong;
			try {
				inputLong = Long.parseLong(input);
			} catch (NumberFormatException e) {
				continue;
			}
			if (inputLong >= 0) {
				bet = inputLong;
				inputSuccess = true;
			}
		} while (!inputSuccess);
		return bet;
	}

	@Override
	public long queryInsuranceBet(Hand h) {
		String input = console.promptUser("Would you like insurance? [y/N]");
		if (input.toLowerCase().startsWith("y")) {
			boolean inputSuccess = false;
			long bet = 0;
			do {
				input = console.promptUser("Place an insurance bet: ");
				long inputLong;
				try {
					inputLong = Long.parseLong(input);
				} catch (NumberFormatException e) {
					continue;
				}
				if (inputLong >= 0) {
					bet = inputLong;
					inputSuccess = true;
				}
			} while (!inputSuccess);
			return bet;
		} else {
			return 0;
		}
	}

	@Override
	public boolean querySurrender() {
		String input = console.promptUser("Would you like to surrender? [y/N]");
		return input.toLowerCase().startsWith("y");
	}

}
