package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.*;
import jdk.swing.interop.SwingInterOpUtils;

import javax.swing.table.TableRowSorter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);
    private final BigDecimal ZERO = new BigDecimal("0");

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void printAccountBalance(BigDecimal balance) {
        System.out.println("Your current account balance is: $" + balance);
    }

    public void printListOfUsers(List<User> users) {
        System.out.println("----------------------------");
        System.out.println("Users");
        System.out.printf("%-15s %-15s", "ID", "Name");
        System.out.println();
        System.out.println("----------------------------");

        for(User user : users) {
            System.out.printf("%-15s %-15s", user.getId(), user.getUsername());
            System.out.println();
        }

        System.out.println("----------------------------");
        System.out.println();
    }

    public void printListOfTransfers(List<Transfer> transfers) {
        System.out.println("----------------------------");
        System.out.println("Transfers");
        System.out.printf("%-14s %-18s %-10s", "ID", "From/To", "Amount"  );
        System.out.println();
        System.out.println("----------------------------");

        for(Transfer transfer : transfers) {
            System.out.printf("%-15s", transfer.getTransferId());
            System.out.printf("%-2s", transfer.getTransferType());
            if (transfer.getTransferType().equalsIgnoreCase("To: ")) {
                System.out.printf("%-15s", transfer.getUsernameTo());
            } else {
                System.out.printf("%-13s", transfer.getUsernameFrom());
            }
            System.out.printf("%-10s", "$ " + transfer.getTransferAmount());
            System.out.println();
        }

    }


    public Transfer getNewTransfer() {

        Transfer transfer = new Transfer();

        String accountPrompt = "Enter ID of user you are sending to (0 to cancel):";
        transfer.setAccountTo(promptForInt(accountPrompt));


        String amountPrompt = "Enter amount:";
        transfer.setTransferAmount(promptForBigDecimal(amountPrompt));

        while (transfer.getTransferAmount().compareTo(ZERO) <= 0) {
            sendMoreThanZeroMessage();
            transfer.setTransferAmount(promptForBigDecimal(amountPrompt));
        }

        transfer.setTypeID(2);
        transfer.setStatusID(2);
        return transfer;
    }

    public void sendMoreThanZeroMessage() {
        System.out.println("You must enter an amount greater than zero.");
    }

    public boolean checkTransferLessThanUserBalance(BigDecimal accountBalance, BigDecimal transferAmount) {
       if (accountBalance.compareTo(transferAmount) < 0) {
           System.out.println("You cannot send more than you have in your account.");
           return false;
       } else {
           return true;
       }
    }

    public Transfer getTransferDetails() {

        Transfer transfer = new Transfer();

        String transferPrompt = "Please enter transfer ID to view details (0 to cancel): ";
        transfer.setTransferId(promptForInt(transferPrompt));

        return transfer;
    }

    public void printRequestedTransfer(Transfer transfer) {
        System.out.println("----------------------------");
        System.out.println("Transfer Details");
        System.out.println("----------------------------");
        System.out.println("Id: " + transfer.getTransferId());
        System.out.println("From: " + transfer.getUsernameFrom());
        System.out.println("To: " + transfer.getUsernameTo());
        System.out.println("Type: " + transfer.getTransferType());
        System.out.println("Status: " + transfer.getTransferStatus());
        System.out.println("Amount: " + transfer.getTransferAmount());
    }
}
