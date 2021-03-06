package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private AccountService accountService = new AccountService(API_BASE_URL);
    private UserService userService = new UserService(API_BASE_URL);
    private TransferService transferService = new TransferService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        } else {
            accountService.setUser(currentUser);
            userService.setUser(currentUser);
            transferService.setUser(currentUser);
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
        BigDecimal balance  = accountService.viewAccountBalance();
        consoleService.printAccountBalance(balance);
	}

	private void viewTransferHistory() {
		List<Transfer> transfers = transferService.getAllTransfers();
        consoleService.printListOfTransfers(transfers);
        Transfer transfer = consoleService.getTransferDetails();
        if (transfer.getTransferId() == 0) {
            mainMenu();
        } else {
            consoleService.printRequestedTransfer(transferService.getRequestedTransfer(transfer));
        }
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
        List<User> users = userService.getAllUsers();
        consoleService.printListOfUsers(users);
        Transfer transfer = consoleService.getNewTransfer();
        while(!consoleService.checkTransferLessThanUserBalance(accountService.viewAccountBalance(), transfer.getTransferAmount())) {
            transfer = consoleService.getNewTransfer();
        }
        if (transfer.getAccountTo() == 0) {
            mainMenu();
        } else {
            transferService.addNewTransfer(transfer);
        }
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}

}
