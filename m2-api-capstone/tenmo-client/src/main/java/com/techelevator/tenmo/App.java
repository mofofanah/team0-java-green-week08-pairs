package com.techelevator.tenmo;

import com.techelevator.tenmo.auth.models.AuthenticatedUser;
import com.techelevator.tenmo.auth.models.UserCredentials;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.auth.services.AuthenticationService;
import com.techelevator.tenmo.auth.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.UserService;
import io.cucumber.java.cy_gb.A;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private AccountService accountService = new AccountService(API_BASE_URL);
    private UserService userService = new UserService(API_BASE_URL);
	private TransferService transferService = new TransferService(API_BASE_URL);

    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService) {
		this.console = console;
		this.authenticationService = authenticationService;
	}

	public void run() {
		console.showWelcomeBanner();
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		String balanceNumber = accountService.retrieveAccountBalance(currentUser.getUser().getId()).getBalance().toString();
		console.displayMessage("Your current account balance is: $" + balanceNumber);
	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
		List<Transfer> transferHistoryList = transferService.retrieveTransferHistory((long)currentUser.getUser().getId());

		while(true) {
			List<String> selectableIds = console.displayTransferList(transferHistoryList, currentUser.getUser().getUsername());
			String validSelection = this.selectValidId(selectableIds, "Please enter transfer ID to view details (0 to cancel)");

			if (!validSelection.equals("0")) {

				for (Transfer transfer : transferHistoryList) {
					if (transfer.getTransferId().toString().equals(validSelection)) {

						console.displayTransfer(transfer);
						console.waitForEnter();
					}
				}

			}
			else {
				break;
			}
		}

	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		//Non-MVP

	}

	private void sendBucks() {
		// TODO Auto-generated method stub
		List<String> selectableIds = console.displayUserList(userService.retrieveAllUsers(), (long)currentUser.getUser().getId());
		String validSelection = this.selectValidId(selectableIds, "Enter ID of user you are sending to (0 to cancel)");

		if (!validSelection.equals("0")) {

			BigDecimal amount = new BigDecimal(console.getUserInputDouble("Enter amount"));
			BigDecimal correctedAmount = amount.setScale(2, RoundingMode.FLOOR);
			BigDecimal balance = new BigDecimal(accountService.retrieveAccountBalance(currentUser.getUser().getId()).getBalance().toString());

			if (correctedAmount.compareTo(balance) == -1 || correctedAmount.compareTo(balance) == 0) {
				Transfer finalTransfer = transferService.sendBucks((long)currentUser.getUser().getId(),Long.valueOf(validSelection), correctedAmount);
				if (finalTransfer == null) {
					System.out.println("null");
				}
				else {
					console.displayTransfer(finalTransfer);
				}

			}

			else {
				console.displayMessage("Insufficient funds.");
			}

		}

	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}
	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		console.displayMessage("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
				console.displayMessage("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
				console.showRegistrationFailed(e.getMessage());
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
				accountService.setAUTH_TOKEN(currentUser.getToken());
				userService.setAUTH_TOKEN(currentUser.getToken());
				transferService.setAUTH_TOKEN(currentUser.getToken());
				//TODO - Pass token to service classes
			} catch (AuthenticationServiceException e) {
				console.showLoginFailed(e.getMessage());
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}

	private String selectValidId(List<String> selectableUsers, String prompt) {

    	while (true) {

    		String input = console.getUserInput(prompt);

    		if (selectableUsers.contains(input) || input.equals("0")) {

    			return input;
			}
			else {

				console.displayMessage("Please enter a valid ID. ");

			}


		}

	}
}
