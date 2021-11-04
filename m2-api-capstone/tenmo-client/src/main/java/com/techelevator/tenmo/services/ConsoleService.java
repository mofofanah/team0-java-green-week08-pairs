package com.techelevator.tenmo.services;


import com.techelevator.tenmo.auth.models.User;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {

	private PrintWriter out;
	private Scanner in;

	public ConsoleService(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output, true);
		this.in = new Scanner(input);
	}

	public void showWelcomeBanner() {
		out.println("*********************");
		out.println("* Welcome to TEnmo! *");
		out.println("*********************");
		out.flush();
	}

	public void displayMessage(String message) {
		out.println(message);
		out.flush();
	}

	public void showRegistrationFailed(String message) {
		out.println("REGISTRATION ERROR: "+message);
		out.println("Please attempt to register again.");
		out.flush();
	}

	public void showLoginFailed(String message) {
		out.println("LOGIN ERROR: "+message);
		out.println("Please attempt to login again.");
		out.flush();
	}

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		out.println();
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if (selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if (choice == null) {
			out.println(System.lineSeparator() + "*** " + userInput + " is not a valid option ***" + System.lineSeparator());
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options) {
		out.println();
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			out.println(optionNum + ") " + options[i]);
		}
		out.print(System.lineSeparator() + "Please choose an option >>> ");
		out.flush();
	}

	public List<String> displayUserList(List<User> userList, Long userId) {

		List<String> selectableIds = new ArrayList<>();
		String leftColumn = "%-10s";
		for (int i = 0; i < 30; i++) {
			System.out.print("-");
		}
		System.out.print("\n");
		System.out.println(String.format(leftColumn, "Users"));
		System.out.println(String.format(leftColumn, "ID") + "Name");

		for (int i = 0; i < 30; i++) {
			System.out.print("-");
		}
		System.out.print("\n");

		for (User user: userList) {
			if((long)user.getId() != userId) {
				selectableIds.add(user.getId().toString());

				System.out.println(String.format(leftColumn, user.getId().toString()) + user.getUsername());

			}

		}

		for (int i = 0; i < 10; i++) {
			System.out.print("-");
		}
		System.out.print("\n");

		return selectableIds;
	}

	public String getUserInput(String prompt) {
		out.print(prompt+": ");
		out.flush();
		return in.nextLine();
	}

	public Integer getUserInputInteger(String prompt) {
		Integer result = null;
		do {
			out.print(prompt+": ");
			out.flush();
			String userInput = in.nextLine();
			try {
				result = Integer.parseInt(userInput);
			} catch(NumberFormatException e) {
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(result == null);
		return result;
	}

	public Double getUserInputDouble(String prompt) {
		Double result = null;
		do {
			out.print(prompt+": ");
			out.flush();
			String userInput = in.nextLine();
			try {
				result = Double.parseDouble(userInput);
			} catch(NumberFormatException e) {
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(result == null);
		return result;
	}
}
