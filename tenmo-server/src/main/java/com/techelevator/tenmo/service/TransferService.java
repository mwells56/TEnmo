package com.techelevator.tenmo.service;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDetails;
import com.techelevator.tenmo.model.ViewTransfer;

import java.util.List;

public interface TransferService {

    TransferDetails getTransferDetailsById(int transferId);

    List<ViewTransfer> getApprovedTransfers(String username);

    List<ViewTransfer> getPendingSends(String username);

    List<ViewTransfer> getPendingRequests(String username);

    void sendMoney(Transfer transfer);

    void sendRequest(Transfer transfer);

    void approveRequest(int transferId);

    void denyRequest(int transferId);

}
