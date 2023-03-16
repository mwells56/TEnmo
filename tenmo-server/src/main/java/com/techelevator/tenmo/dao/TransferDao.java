package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    Transfer getTransferById(int transferId);

    String getTransferStatus(int transferId);

    String getTransferType(int transferId);

    List<Transfer> getAllApprovedTransfersByUserId(int userId);

    List<Transfer> getPendingSendsByUserId(int userId);

    List<Transfer> getPendingRequestsByUserId (int userId);

    void sendMoney(Transfer transfer);

    void createSendLog(Transfer transfer);

    void requestMoney(Transfer transfer);

    void approveTransfer(int transferId);

    void rejectTransfer(int transferId);

}
