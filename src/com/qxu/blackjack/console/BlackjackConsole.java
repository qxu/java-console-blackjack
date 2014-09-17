package com.qxu.blackjack.console;

import com.qxu.blackjack.table.Player;
import com.qxu.blackjack.table.Table;

public class BlackjackConsole {
	public static void main(String[] args) {
		Table t = new Table();
		ConsoleDisplay console = new ConsoleDisplay(t);
		t.setListener(console);
		Player p = new ConsolePlayer(console);
		t.addPlayer(p);
		t.run();
	}
}
