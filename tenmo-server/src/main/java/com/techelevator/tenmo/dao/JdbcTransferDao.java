package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private final JdbcTemplate jdbcTemplate;
    private final int TRANSFER_TYPE_ID_REQUEST = 1;
    private final int TRANSFER_TYPE_ID_SEND = 2;
    private final int TRANSFER_STATUS_ID_PENDING = 1;
    private final int TRANSFER_STATUS_ID_APPROVED = 2;
    private final int TRANSFER_STATUS_ID_REJECTED = 3;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer getTransferById(int transferId) {
        Transfer transfer = new Transfer();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer " +
                "WHERE transfer_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            transfer = mapRowToTransfer(results);
        }

        return transfer;
    }

    @Override
    public String getTransferStatus(int transferId) {
        String sql = "SELECT transfer_status_desc " +
                "FROM transfer_status " +
                "JOIN transfer " +
                "ON transfer.transfer_status_id = transfer_status.transfer_status_id " +
                "WHERE transfer_id = ?;";

        return jdbcTemplate.queryForObject(sql, String.class, transferId);
    }

    @Override
    public String getTransferType(int transferId) {
        String sql = "SELECT transfer_type_desc " +
                "FROM transfer_type " +
                "JOIN transfer " +
                "ON transfer.transfer_type_id = transfer_type.transfer_type_id " +
                "WHERE transfer_id = ?;";

        return jdbcTemplate.queryForObject(sql, String.class, transferId);
    }

    @Override
    public List<Transfer> getAllApprovedTransfersByUserId (int userId) {
        List<Transfer> transferList = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer " +
                "JOIN account " +
                "ON (transfer.account_from = account.account_id) OR (transfer.account_to = account.account_id) " +
                "JOIN tenmo_user " +
                "ON tenmo_user.user_id = account.user_id " +
                "WHERE tenmo_user.user_id = ? AND transfer_status_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, TRANSFER_STATUS_ID_APPROVED);
        while (results.next()) {
            transferList.add(mapRowToTransfer(results));
        }

        return transferList;
    }

    @Override
    public List<Transfer> getPendingSendsByUserId (int userId) {
        List<Transfer> pendingSends = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer " +
                "JOIN account " +
                "ON account.account_id = transfer.account_to " +
                "JOIN tenmo_user " +
                "ON tenmo_user.user_id = account.user_id " +
                "WHERE tenmo_user.user_id = ? AND transfer_status_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, TRANSFER_STATUS_ID_PENDING);
        while (results.next()) {
            pendingSends.add(mapRowToTransfer(results));
        }

        return pendingSends;
    }

    @Override
    public List<Transfer> getPendingRequestsByUserId (int userId) {
        List<Transfer> pendingRequests = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer " +
                "JOIN account " +
                "ON account.account_id = transfer.account_from " +
                "JOIN tenmo_user " +
                "ON tenmo_user.user_id = account.user_id " +
                "WHERE tenmo_user.user_id = ? AND transfer_status_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, TRANSFER_STATUS_ID_PENDING);
        while (results.next()) {
            pendingRequests.add(mapRowToTransfer(results));
        }

        return pendingRequests;
    }

    @Override
    public void sendMoney(Transfer transfer) {
        String sql = "BEGIN TRANSACTION; " +
                "UPDATE account " +
                "SET balance = (balance - ?) " +
                "WHERE account_id = ?; " +
                "UPDATE account " +
                "SET balance = (balance + ?) " +
                "WHERE account_id = ?; " +
                "COMMIT;";
        jdbcTemplate.update(sql, transfer.getTransferAmount(), transfer.getAccountFromId(), transfer.getTransferAmount(), transfer.getAccountToId());
    }

    @Override
    public void createSendLog(Transfer transfer) {
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?);";
        jdbcTemplate.update(sql, TRANSFER_TYPE_ID_SEND, TRANSFER_STATUS_ID_APPROVED, transfer.getAccountFromId(), transfer.getAccountToId(), transfer.getTransferAmount());
    }

    @Override
    public void requestMoney(Transfer transfer) {
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?);";
        jdbcTemplate.update(sql, TRANSFER_TYPE_ID_REQUEST, TRANSFER_STATUS_ID_PENDING, transfer.getAccountFromId(), transfer.getAccountToId(), transfer.getTransferAmount());
    }

    @Override
    public void approveTransfer(int transferId) {
        String sql = "UPDATE transfer " +
                "SET transfer_status_id = ? " +
                "WHERE transfer_id = ?;";
        sendMoney(getTransferById(transferId));
        jdbcTemplate.update(sql, TRANSFER_STATUS_ID_APPROVED, transferId);
    }

    @Override
    public void rejectTransfer(int transferId) {
        String sql = "UPDATE transfer " +
                "SET transfer_status_id = ? " +
                "WHERE transfer_id = ?;";
        jdbcTemplate.update(sql, TRANSFER_STATUS_ID_REJECTED, transferId);
    }

    private Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setTransferTypeId(rowSet.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rowSet.getInt("transfer_status_id"));
        transfer.setAccountFromId(rowSet.getInt("account_from"));
        transfer.setAccountToId(rowSet.getInt("account_to"));
        transfer.setTransferAmount(rowSet.getBigDecimal("amount"));
        return transfer;
    }
}





