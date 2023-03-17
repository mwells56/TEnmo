package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.*;

import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);

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
        System.out.println("3: View your pending requests sent");
        System.out.println("4: View your pending requests received");
        System.out.println("5: Send TE bucks");
        System.out.println("6: Request TE bucks");
        System.out.println("0: Log out");
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

    public void printHeaderMessage(String formattedHeaderTop, String formattedHeaderBottom) {
        System.out.println("--------------------------------------------");
        System.out.println(formattedHeaderTop);
        System.out.println(formattedHeaderBottom);
        System.out.println("--------------------------------------------");
    }

    public void printTransferHeader() {
        String transfers = "Transfers";
        String id = "ID";
        String fromTo = "From/To";
        String amount = "Amount";
        String formattedHeaderTop = String.format("%-15s", transfers);
        String formattedHeaderBottom = String.format("%-15s %-20s %s", id, fromTo, amount);
        printHeaderMessage(formattedHeaderTop, formattedHeaderBottom);
    }

    public void printUserHeader() {
        String users = "Users";
        String id = "ID";
        String name = "Name";
        String formattedHeaderTop = String.format("%-15s", users);
        String formattedHeaderBottom = String.format("%-15s %s", id, name);
        printHeaderMessage(formattedHeaderTop, formattedHeaderBottom);
    }

    public void printPendingTransferHeader() {
        String pendingTransfers = "Pending Transfers";
        String id = "ID";
        String to = "To";
        String amount = "Amount";
        String formattedHeaderTop = String.format("%-15s", pendingTransfers);
        String formattedHeaderBottom = String.format("%-15s %-20s %s", id, to, amount);
        printHeaderMessage(formattedHeaderTop, formattedHeaderBottom);
    }

    public void printPendingRequestsSentHeader() {
        String requestsSent = "Pending Requests Sent";
        String id = "ID";
        String from = "From";
        String amount = "Amount";
        String formattedHeaderTop = String.format("%-15s", requestsSent);
        String formattedHeaderBottom = String.format("%-15s %-20s %s", id, from, amount);
        printHeaderMessage(formattedHeaderTop, formattedHeaderBottom);
    }

    public void formattedTransferColumnsFromUser(ViewTransfer transfer) {
        String formattedId = String.format("%-16s", transfer.getTransferId());
        String dollarSign = "$";
        String to = "To: " + transfer.getUsernameTo();
        String formattedColumns = String.format("%-20s %s %s", to, dollarSign, transfer.getAmount().toString());

        System.out.print(formattedId);
        System.out.println(formattedColumns);
    }

    public void formattedTransferColumnsToUser(ViewTransfer transfer) {
        String formattedId = String.format("%-16s", transfer.getTransferId());
        String dollarSign = "$";
        String from = "From: " + transfer.getUsernameFrom();
        String formattedColumns = String.format("%-20s %s %s", from, dollarSign, transfer.getAmount().toString());

        System.out.print(formattedId);
        System.out.println(formattedColumns);
    }

    public void printTransferDetails(TransferDetails transferDetails) {
        System.out.println("--------------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("--------------------------------------------");
        System.out.println("Id:\t\t" + transferDetails.getTransferId());
        System.out.println("From:\t" + transferDetails.getUsernameFrom());
        System.out.println("To:\t\t" + transferDetails.getUsernameTo());
        System.out.println("Type:\t" + transferDetails.getTransferType());
        System.out.println("Status:\t" + transferDetails.getTransferStatus());
        System.out.println("Amount:\t$" + transferDetails.getAmount());
        System.out.println("----------");
    }

    public void printPendingTransferSent(ViewTransfer pendingTransfer) {
        int id = pendingTransfer.getTransferId();
        String from = pendingTransfer.getUsernameFrom();
        String dollarSign = "$";
        String formattedId = String.format("%-16s", id);
        String formattedColumns = String.format("%-20s %s %s", from, dollarSign, pendingTransfer.getAmount().toString());

        System.out.print(formattedId);
        System.out.println(formattedColumns);
    }

    public void printPendingTransferReceived(ViewTransfer pendingTransfer) {
        int id = pendingTransfer.getTransferId();
        String to = pendingTransfer.getUsernameTo();
        String dollarSign = "$";
        String formattedId = String.format("%-16s", id);
        String formattedColumns = String.format("%-20s %s %s", to, dollarSign, pendingTransfer.getAmount().toString());

        System.out.print(formattedId);
        System.out.println(formattedColumns);
    }

    public void formattedUserColumn (OtherUser otherUser) {
        int otherUserId = otherUser.getUserId();
        String otherUserName = otherUser.getUsername();
        String formattedColumns = String.format("%-15s %s", otherUserId, otherUserName);
        System.out.println(formattedColumns);
    }

    public void printBalance(BigDecimal balance) {
        System.out.println("--------------------------------------------");
        System.out.println("Current Balance");
        System.out.println("--------------------------------------------\n");
        System.out.println("Your current account balance is: $" + balance);
        System.out.println("----------");
    }

    public void printApproveOrDenyMessage () {
        System.out.println("1: Approve \n2: Reject \n0: Don't approve or reject");
        System.out.println("----------");
    }

}
