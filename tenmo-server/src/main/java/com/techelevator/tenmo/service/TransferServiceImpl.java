package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDetails;
import com.techelevator.tenmo.model.ViewTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransferServiceImpl implements TransferService {

    @Autowired
    private TransferDao transferDao;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private UserDao userDao;

    @Override
    public TransferDetails getTransferDetailsById(int transferId) {
        TransferDetails transferDetails = new TransferDetails();
        Transfer transfer = transferDao.getTransferById(transferId);

        transferDetails.setTransferId(transfer.getTransferId());
        transferDetails.setAmount(transfer.getTransferAmount());
        transferDetails.setUsernameFrom(accountDao.getUsernameById(transfer.getAccountFromId()));
        transferDetails.setUsernameTo(accountDao.getUsernameById(transfer.getAccountToId()));
        transferDetails.setTransferStatus(transferDao.getTransferStatus(transfer.getTransferId()));
        transferDetails.setTransferType(transferDao.getTransferType(transfer.getTransferId()));

        return transferDetails;
    }

    @Override
    public List<ViewTransfer> getApprovedTransfers(String username) {
        int userId = userDao.findIdByUsername(username);
        List<Transfer> transfersList = transferDao.getAllApprovedTransfersByUserId(userId);

        List<ViewTransfer> transfersToView = new ArrayList<>();

        for (Transfer transfer : transfersList) {
            ViewTransfer viewTransfer = new ViewTransfer();

            viewTransfer.setTransferId(transfer.getTransferId());
            viewTransfer.setAmount(transfer.getTransferAmount());
            viewTransfer.setUsernameFrom(accountDao.getUsernameById(transfer.getAccountFromId()));
            viewTransfer.setUsernameTo(accountDao.getUsernameById(transfer.getAccountToId()));

            transfersToView.add(viewTransfer);
        }
        return transfersToView;
    }

    @Override
    public List<ViewTransfer> getPendingSends(String username) {
        int userId = userDao.findIdByUsername(username);
        List<Transfer> transfersList = transferDao.getPendingSendsByUserId(userId);

        List<ViewTransfer> pendingTransfers = new ArrayList<>();

        for (Transfer transfer : transfersList) {
            ViewTransfer viewTransfer = new ViewTransfer();

            viewTransfer.setTransferId(transfer.getTransferId());
            viewTransfer.setAmount(transfer.getTransferAmount());
            viewTransfer.setUsernameFrom(accountDao.getUsernameById(transfer.getAccountFromId()));
            viewTransfer.setUsernameTo(accountDao.getUsernameById(transfer.getAccountToId()));

            pendingTransfers.add(viewTransfer);
        }
        return pendingTransfers;
    }

    @Override
    public List<ViewTransfer> getPendingRequests(String username) {
        int userId = userDao.findIdByUsername(username);
        List<Transfer> transfersList = transferDao.getPendingRequestsByUserId(userId);

        List<ViewTransfer> pendingTransfers = new ArrayList<>();

        for (Transfer transfer : transfersList) {
            ViewTransfer viewTransfer = new ViewTransfer();

            viewTransfer.setTransferId(transfer.getTransferId());
            viewTransfer.setAmount(transfer.getTransferAmount());
            viewTransfer.setUsernameFrom(accountDao.getUsernameById(transfer.getAccountFromId()));
            viewTransfer.setUsernameTo(accountDao.getUsernameById(transfer.getAccountToId()));

            pendingTransfers.add(viewTransfer);
        }
        return pendingTransfers;
    }

    @Override
    public void sendMoney(Transfer transfer) {
        transfer.setAccountFromId(accountDao.getAccountIdByUserId(transfer.getAccountFromId()));
        transfer.setAccountToId(accountDao.getAccountIdByUserId(transfer.getAccountToId()));

        transferDao.createSendLog(transfer);
        transferDao.sendMoney(transfer);
    }

    @Override
    public void sendRequest(Transfer transfer) {
        transfer.setAccountFromId(accountDao.getAccountIdByUserId(transfer.getAccountFromId()));
        transfer.setAccountToId(accountDao.getAccountIdByUserId(transfer.getAccountToId()));

        transferDao.requestMoney(transfer);
    }

    @Override
    public void approveRequest(int transferId) {
        transferDao.approveTransfer(transferId);
    }

    @Override
    public void denyRequest(int transferId) {
        transferDao.rejectTransfer(transferId);
    }
}
