package com.techelevator.tenmo;


import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;

    Scanner scanner = new Scanner(System.in);


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
        // TODO Auto-generated method stub
        try {
            BigDecimal balance = restTemplate.getForObject(API_BASE_URL + "user/balance/" + currentUser.getUser().getId(), BigDecimal.class);
            System.out.println("------------------------------------");
            System.out.println("Your current balance is: $" + balance);
            System.out.println("------------------------------------");
        } catch (RestClientException e) {
            consoleService.printErrorMessage();
        }

    }

    private void viewTransferHistory() {
        // TODO Auto-generated method stub

    }

    private void viewPendingRequests() {
        // TODO Auto-generated method stub

    }

    private void sendBucks() {
        // TODO Auto-generated method stub
        try {
            Map<Long, String> mappedUsers = restTemplate.getForObject(API_BASE_URL + "/user", HashMap.class);
            System.out.println("------------------------------------");
            System.out.println("User");
            System.out.println("ID\t Name");
            System.out.println("------------------------------------");
            for (Map.Entry<Long, String> user : mappedUsers.entrySet()) {
                System.out.println(user.getKey() + "\t" + user.getValue());
            }
            System.out.println("------------------------------------");
            System.out.println("Enter ID of user you are sending to (0 to cancel): ");
            String userId = scanner.nextLine();
            System.out.println("Enter amount: ");
            String amount = scanner.nextLine();
            BigDecimal transferAmount = new BigDecimal(amount);

                HttpHeaders headers = new HttpHeaders();
                headers.setBearerAuth(currentUser.getToken());
                HttpEntity entity = new HttpEntity(headers);
            restTemplate.put(API_BASE_URL + "user/" + currentUser.getUser().getId()
                    + "/transfer/" + userId + "/" + transferAmount, entity);
        } catch (RestClientException e) {
            consoleService.printErrorMessage();
        }
    }


    private void requestBucks() {
        // TODO Auto-generated method stub

    }

}

// System.out.println("------------------------------------");
//         System.out.println("------------------------------------");
//         System.out.println("------------------------------------");
//         System.out.println("------------------------------------");
//         System.out.println("------------------------------------");
//         System.out.println("------------------------------------");
//         System.out.println("------------------------------------");