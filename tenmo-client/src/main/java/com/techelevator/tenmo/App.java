package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL);
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
            String token = currentUser.getToken();
            accountService.setAuthToken(token);
            transferService.setAuthToken(token);
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
                viewPendingRequestsSent();
            } else if (menuSelection == 4) {
                viewPendingRequests();
            } else if (menuSelection == 5) {
                sendBucks();
            } else if (menuSelection == 6) {
                requestBucks();
            } else if (menuSelection == 0) {
                currentUser = null;
                run();
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        // TODO Auto-generated method stub
        BigDecimal balance = accountService.getBalance();
        consoleService.printBalance(balance);

    }

    private void viewTransferHistory() {
        // TODO Auto-generated method stub
        ViewTransfer[] transferHistory = transferService.transferHistory();
        if (transferHistory.length > 0) {
            List<Integer> transferIds = new ArrayList<>();
            String currentUsername = currentUser.getUser().getUsername();
            consoleService.printTransferHeader();
            for (ViewTransfer transfer : transferHistory) {
                transferIds.add(transfer.getTransferId());

                if (currentUsername.equals(transfer.getUsernameFrom())) {
                    consoleService.formattedTransferColumnsFromUser(transfer);
                } else {
                    consoleService.formattedTransferColumnsToUser(transfer);
                }
            }
            System.out.println("----------");

            int transferToView = consoleService.promptForInt("\nPlease enter transfer ID to view details (0 to cancel): ");
            while (transferToView != 0) {
                if (transferIds.contains(transferToView)) {
                    viewTransferDetails(transferToView);
                    transferToView = 0;
                } else {
                    transferToView = consoleService.promptForInt("\nPlease select a valid transfer ID");
                }
            }
        } else {
            System.out.println("----------");
            System.out.println("You have not made any transfers yet.");
            System.out.println("----------");
        }
    }

    private void viewTransferDetails(int transferToView) {
        TransferDetails transferDetails = transferService.transferDetails(transferToView);
        consoleService.printTransferDetails(transferDetails);
    }

    private void viewPendingRequestsSent() {
        // TODO Auto-generated method stub
        ViewTransfer[] pendingTransfers = transferService.pendingRequestsSent();
        if (pendingTransfers.length > 0) {
            consoleService.printPendingRequestsSentHeader();
            for (ViewTransfer pendingTransfer : pendingTransfers) {
                consoleService.printPendingTransferSent(pendingTransfer);
            }

        } else {
            System.out.println("----------");
            System.out.println("You have not sent any requests.");
        }
        System.out.println("----------");

    }

    private void viewPendingRequests() {
        // TODO Auto-generated method stub
        ViewTransfer[] pendingTransfers = transferService.pendingRequestsReceived();
        if (pendingTransfers.length > 0) {
            List<Integer> transferIds = new ArrayList<>();
            consoleService.printPendingTransferHeader();
            for (ViewTransfer pendingTransfer : pendingTransfers) {
                transferIds.add(pendingTransfer.getTransferId());
                consoleService.printPendingTransferReceived(pendingTransfer);
            }
            int transferToView = consoleService.promptForInt("\nPlease enter transfer ID to approve/reject (0 to cancel): ");
            while (transferToView != 0) {
                if (transferIds.contains(transferToView)) {
                    approveOrDenyTransfer(transferToView);
                    transferToView = 0;
                } else {
                    transferToView = consoleService.promptForInt("\nPlease select a valid transfer ID");
                }
            }
        } else {
            System.out.println("----------");
            System.out.println("You have no requests pending.");
        }
        System.out.println("----------");

    }

    public void approveOrDenyTransfer(int transferId) {
        TransferDetails transferDetails = transferService.transferDetails(transferId);
        consoleService.printApproveOrDenyMessage();
        int approveOrDeny = consoleService.promptForInt("Please choose an option: ");
        while (approveOrDeny != 0) {
            if (approveOrDeny == 1) {
                if (transferDetails.getAmount().compareTo(accountService.getBalance()) < 0) {
                    transferService.approveTransfer(transferId);
                    approveOrDeny = 0;
                } else {
                    System.out.println("You do not have enough money in your account to approve this transfer.");
                    approveOrDeny = consoleService.promptForInt("Please select a different option: ");
                }
            } else if (approveOrDeny == 2) {
                transferService.rejectTransfer(transferId);
                approveOrDeny = 0;
            } else {
                approveOrDeny = consoleService.promptForInt("Please select a valid option: ");
            }
        }
    }

    private void sendBucks() {
        // TODO Auto-generated method stub
        OtherUser[] otherUsers = accountService.otherUsers();
        if (otherUsers.length > 0) {
            List<Integer> userIds = new ArrayList<>();

            consoleService.printUserHeader();

            for (OtherUser otherUser : otherUsers) {
                userIds.add(otherUser.getUserId());
                consoleService.formattedUserColumn(otherUser);
            }
            System.out.println("----------");

            int userToId = consoleService.promptForInt("\nEnter the ID for user you are sending to (0 to cancel): ");
            while (userToId != 0) {
                if (userIds.contains(userToId)) {
                    BigDecimal amount = consoleService.promptForBigDecimal("\nEnter amount: $");
                    if (amount.compareTo(accountService.getBalance()) < 0) {
                        Transfer transfer = new Transfer(amount, currentUser.getUser().getId(), userToId);

                        transferService.sendMoney(transfer);
                        userToId = 0;
                    } else {
                        System.out.println("You do not have enough money in your account to make this transfer.");
                    }

                } else {
                    userToId = consoleService.promptForInt("Please select a valid user (0 to cancel): ");
                }
            }
        } else {
            System.out.println("----------");
            System.out.println("There are no other accounts for you to send money to.");
            System.out.println("----------");
        }
    }

    private void requestBucks() {
        // TODO Auto-generated method stub
        OtherUser[] otherUsers = accountService.otherUsers();
        if (otherUsers.length > 0) {
            List<Integer> userIds = new ArrayList<>();

            consoleService.printUserHeader();

            for (OtherUser otherUser : otherUsers) {
                userIds.add(otherUser.getUserId());
                consoleService.formattedUserColumn(otherUser);
            }
            System.out.println("----------");
            int userFromId = consoleService.promptForInt("\nEnter ID of user you are requesting from (0 to cancel): ");
            while (userFromId != 0) {
                if (userIds.contains(userFromId)) {
                    BigDecimal amount = consoleService.promptForBigDecimal("\nEnter amount: $");

                    Transfer transfer = new Transfer(amount, userFromId, currentUser.getUser().getId());

                    transferService.requestMoney(transfer);
                    userFromId = 0;
                } else {
                    userFromId = consoleService.promptForInt("Please select a valid user (0 to cancel): ");
                }
            }
        } else {
            System.out.println("----------");
            System.out.println("There are no other accounts for you to request money from.");
            System.out.println("----------");
        }
    }

}
