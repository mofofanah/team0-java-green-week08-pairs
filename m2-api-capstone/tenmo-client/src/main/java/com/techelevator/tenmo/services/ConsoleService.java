package com.techelevator.tenmo.services;


import com.techelevator.tenmo.auth.models.User;
import com.techelevator.tenmo.models.Transfer;

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

	} // end displayUserList()


	public List<String> displayTransferList(List<Transfer> listOfTransfer, String userName) {

		List<String> selectableIds = new ArrayList<>();
		String leftColumn = "%-10s";
		String middleColumn = "%-20s";

		for (int i = 0; i < 40; i++) {
			System.out.print("-");
		}
		System.out.print("\n");
		System.out.println(String.format(leftColumn, "Transfers"));
		System.out.println(String.format(leftColumn, "ID") + String.format(middleColumn, "From/To") + "Amount");

		for (int i = 0; i < 40; i++) {
			System.out.print("-");
		}
		System.out.print("\n");

		for (Transfer transfer: listOfTransfer) {

			String toOrFrom = "";

			if (transfer.getNameAccountFrom().equals(userName)){

				toOrFrom = "To:    " + transfer.getNameAccountTo();
			}
			else if (transfer.getNameAccountTo().equals(userName)) {

				toOrFrom = "From:  " + transfer.getNameAccountFrom();
			}
			else {
				toOrFrom = "Unspecified";
			}

			selectableIds.add(transfer.getTransferId().toString());
			System.out.println(String.format(leftColumn, transfer.getTransferId().toString()) + String.format(middleColumn, toOrFrom ) + "$" + transfer.getAmount());


		}

		for (int i = 0; i < 10; i++) {
			System.out.print("-");
		}
		System.out.print("\n");

		return selectableIds;

	} // end displayTransferList()

	public void displayTransfer(Transfer transfer) {

		for (int i = 0; i < 30; i++) {
			System.out.print("-");
		}
		System.out.print("\n");
		System.out.println("Transfer Details");
		for (int i = 0; i < 30; i++) {
			System.out.print("-");
		}
		System.out.print("\n");
		System.out.println("Id: " + transfer.getTransferId());
		System.out.println("From: " + transfer.getNameAccountFrom());
		System.out.println("To: " + transfer.getNameAccountTo());
		System.out.println("Type: " + this.transferTypeToString(transfer.getTransferTypeId()));
		System.out.println("Status: " + this.transferStatusToString(transfer.getTransferStatusId()));
		System.out.println("Amount: $" + transfer.getAmount());
		System.out.print("\n");
	}

	private String transferTypeToString(Long code) {
		if (code == 1) {
			return "Request";
		} else if (code == 2) {
			return "Send";
		} else {
			return "";
		}
	}

	private String transferStatusToString(Long code) {
		if (code == 1) {
			return "Pending";
		} else if (code == 2) {
			return "Approved";
		} else if (code == 3) {
			return "Rejected";
		}
		return "";
	}

	public void waitForEnter() {
		out.println("----------");
		out.println("Please hit enter to continue");
		out.flush();
		in.nextLine();
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
