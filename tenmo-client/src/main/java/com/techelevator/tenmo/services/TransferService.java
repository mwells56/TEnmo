package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDetails;
import com.techelevator.tenmo.model.ViewTransfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferService {
    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private String authToken = null;

    public TransferService(String url) {
        this.baseUrl = url;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public ViewTransfer[] transferHistory() {
        ViewTransfer[] transferHistory = null;
        try {
            ResponseEntity<ViewTransfer[]> response = restTemplate.exchange(baseUrl + "transfer/getApproved", HttpMethod.GET, makeAuthEntity(), ViewTransfer[].class);
            transferHistory = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferHistory;
    }

    public TransferDetails transferDetails(int transferId) {
        TransferDetails transferDetails = null;
        try {
            ResponseEntity<TransferDetails> response = restTemplate.exchange(baseUrl + "transfer/details/" + transferId, HttpMethod.GET, makeAuthEntity(), TransferDetails.class);
            transferDetails = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferDetails;
    }

    public ViewTransfer[] pendingRequestsSent() {
        ViewTransfer[] pendingRequestsSent = null;
        try {
            ResponseEntity<ViewTransfer[]> response = restTemplate.exchange(baseUrl + "transfer/request/pending/sent", HttpMethod.GET, makeAuthEntity(), ViewTransfer[].class);
            pendingRequestsSent = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return pendingRequestsSent;
    }

    public ViewTransfer[] pendingRequestsReceived() {
        ViewTransfer[] pendingRequestsReceived = null;
        try {
            ResponseEntity<ViewTransfer[]> response = restTemplate.exchange(baseUrl + "transfer/request/pending/received", HttpMethod.GET, makeAuthEntity(), ViewTransfer[].class);
            pendingRequestsReceived = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return pendingRequestsReceived;
    }

    public boolean approveTransfer(int transferId) {
        boolean success = false;
        try {
            restTemplate.exchange(baseUrl + "transfer/request/" + transferId + "/approve", HttpMethod.PUT, makeAuthEntity(), Void.class);
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }

    public boolean rejectTransfer(int transferId) {
        boolean success = false;
        try {
            restTemplate.exchange(baseUrl + "transfer/request/" + transferId + "/reject", HttpMethod.PUT, makeAuthEntity(), Void.class);
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }

    public Transfer sendMoney(Transfer transfer) {
        HttpEntity<Transfer> entity = makeAuthEntityTransfer(transfer);

        Transfer returnedTransfer = null;
        try {
            returnedTransfer = restTemplate.postForObject(baseUrl + "transfer/send", entity, Transfer.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return returnedTransfer;
    }

    public Transfer requestMoney(Transfer transfer) {
        HttpEntity<Transfer> entity = makeAuthEntityTransfer(transfer);

        Transfer returnedTransfer = null;
        try {
            returnedTransfer = restTemplate.postForObject(baseUrl + "transfer/request", entity, Transfer.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return returnedTransfer;
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    private HttpEntity<Transfer> makeAuthEntityTransfer(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        HttpEntity<Transfer> entity = new HttpEntity<Transfer>(transfer, headers);
        return entity;
    }
}
