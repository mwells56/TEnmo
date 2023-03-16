package com.techelevator.tenmo.controller;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDetails;
import com.techelevator.tenmo.model.ViewTransfer;
import com.techelevator.tenmo.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/transfer")
@PreAuthorize("isAuthenticated()")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @RequestMapping(path = "/details/{transferId}", method = RequestMethod.GET)
    public TransferDetails getTransferDetails(@PathVariable int transferId) {
        return transferService.getTransferDetailsById(transferId);
    }

    @RequestMapping(path = "/getApproved", method = RequestMethod.GET)
    public List<ViewTransfer> listUserTransfers(Principal principal) {
        return transferService.getApprovedTransfers(principal.getName());
    }

    @RequestMapping(path = "/request/pending/sent", method = RequestMethod.GET)
    public List<ViewTransfer> getPendingSends(Principal principal) {
        return transferService.getPendingSends(principal.getName());
    }

    @RequestMapping(path = "/request/pending/received", method = RequestMethod.GET)
    public List<ViewTransfer> getPendingRequests(Principal principal) {
        return transferService.getPendingRequests(principal.getName());
    }

    @RequestMapping(path = "/send", method = RequestMethod.POST)
    public void sendMoney(@RequestBody Transfer transfer) {
        transferService.sendMoney(transfer);
    }

    @RequestMapping(path = "/request", method = RequestMethod.POST)
    public void sendRequest(@RequestBody Transfer transfer) {
        transferService.sendRequest(transfer);
    }

    @RequestMapping(path = "request/{transferId}/approve", method = RequestMethod.PUT)
    public void approveTransfer (@PathVariable int transferId) {
        transferService.approveRequest(transferId);
    }

    @RequestMapping(path = "request/{transferId}/reject", method = RequestMethod.PUT)
    public void rejectTransfer (@PathVariable int transferId) {
        transferService.denyRequest(transferId);
    }

}
