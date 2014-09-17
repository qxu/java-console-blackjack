package com.qxu.blackjack.console;

import java.util.Scanner;

import com.qxu.blackjack.table.Table;
import com.qxu.blackjack.table.TableEventHandler;

public class ConsoleDisplay implements TableEventHandler {

	private Scanner consoleIn = new Scanner(System.in);

	private Table table;

	public ConsoleDisplay(Table t) {
		this.table = t;
	}

	public void print() {
		System.out.println(table);
	}

	public void reprint() {
		print();
	}

	@Override
	public void tableChanged() {
		if (!table.isInRound()) {
			reprint();
		}
	}

	public Table getTable() {
		return this.table;
	}

	public String promptUser(String message) {
		System.out.println(table);
		System.out.println(message);
		return consoleIn.nextLine();
	}
}
