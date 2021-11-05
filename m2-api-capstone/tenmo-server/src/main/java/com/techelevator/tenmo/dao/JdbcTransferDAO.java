package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDAO implements TransferDAO {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //TODO check amount being sent is valid

    @Override
    public Transfer sendBucks(Transfer transferToExecute) {
        //Double-check that From Account amount <= balance
        SqlRowSet resultForFromBalance = jdbcTemplate.queryForRowSet("SELECT account_id, balance FROM accounts WHERE user_id = ?",transferToExecute.getAccountFrom());
        if (resultForFromBalance.next()) {
            Long fromAccountId = resultForFromBalance.getLong("account_id");
            BigDecimal fromBalance = resultForFromBalance.getBigDecimal("balance");
            if (transferToExecute.getAmount().compareTo(fromBalance) == -1 || transferToExecute.getAmount().compareTo(fromBalance) == 0) {
                //Subtract amount from FromAccount balance
                BigDecimal updatedFromBalance = fromBalance.subtract(transferToExecute.getAmount());
                jdbcTemplate.update("UPDATE accounts SET balance = ? WHERE user_id = ?",updatedFromBalance,transferToExecute.getAccountFrom());

                //Add amount to ToAccount balance
                SqlRowSet resultForToBalance = jdbcTemplate.queryForRowSet("SELECT account_id, balance FROM accounts WHERE user_id = ?",transferToExecute.getAccountTo());
                if (resultForToBalance.next()) {
                    Long toAccountId = resultForToBalance.getLong("account_id");
                    BigDecimal toBalance = resultForToBalance.getBigDecimal("balance");
                    BigDecimal updatedToBalance = toBalance.add(transferToExecute.getAmount());
                    jdbcTemplate.update("UPDATE accounts SET balance = ? WHERE user_id = ?",updatedToBalance,transferToExecute.getAccountTo());

                    //Insert completed transfer into table
                    int nextTransferId = retrieveNextVenueId();
                    jdbcTemplate.update(
                            "INSERT INTO transfers (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                            "VALUES (?,2,2,?,?,?::decimal(9,2))",
                            nextTransferId,
                            fromAccountId,
                            toAccountId,
                            transferToExecute.getAmount()
                            );

                    //Get transfer out and return it
                    Long transferId = null;
                    Long transferTypeId = null;
                    Long transferStatusId = null;
                    Long accountFromId = null;
                    String accountFromName = null;
                    Long accountToId = null;
                    String accountToName = null;
                    BigDecimal amount = null;

                    SqlRowSet finalResult1 = jdbcTemplate.queryForRowSet(
                        "SELECT transfers.transfer_id, transfers.transfer_type_id, transfers.transfer_status_id, transfers.account_from, users.username, transfers.amount " +
                            "FROM transfers " +
                            "JOIN accounts ON accounts.account_id = transfers.account_from " +
                            "JOIN users ON users.user_id = accounts.user_id " +
                            "WHERE transfers.transfer_id = ?",
                            nextTransferId
                    );
                    if (finalResult1.next()) {
                        transferId = finalResult1.getLong("transfer_id");
                        transferTypeId = finalResult1.getLong("transfer_type_id");
                        transferStatusId = finalResult1.getLong("transfer_status_id");
                        accountFromId = finalResult1.getLong("account_from");
                        accountFromName = finalResult1.getString("username");
                        amount = finalResult1.getBigDecimal("amount");
                    }

                    SqlRowSet finalResult2 = jdbcTemplate.queryForRowSet(
                            "SELECT transfers.account_to, users.username " +
                                "FROM transfers " +
                                "JOIN accounts ON accounts.account_id = transfers.account_to " +
                                "JOIN users ON users.user_id = accounts.user_id " +
                                "WHERE transfers.transfer_id = ?",
                            nextTransferId
                    );
                    if (finalResult2.next()) {
                        accountToId = finalResult2.getLong("account_to");
                        accountToName = finalResult2.getString("username");
                    }

                    return this.makeReturnTransfer(
                            transferId,
                            transferTypeId,
                            transferStatusId,
                            accountFromId,
                            accountFromName,
                            accountToId,
                            accountToName,
                            amount
                    );
                }
                else {
                    return null; //If null on resultForToBalance
                }

            }
            else {
                return null; //If amount is greater than balance
            }
        }
        else {
            return null; //If null on resultForFromBalance
        }

    }

    @Override
    public List<Transfer> retrieveTransferHistory(Long userId) {

        List<Transfer> listOfTransfers = new ArrayList<>();

        SqlRowSet results = jdbcTemplate.queryForRowSet(
                    "SELECT transfers.transfer_id, transfers.transfer_type_id, transfers.transfer_status_id, transfers.account_from, u3.username AS from_user , transfers.account_to, u2.username AS to_user, transfers.amount " +
                            "FROM users u1 " +
                            "JOIN accounts a1 ON u1.user_id = a1.user_id " +
                            "JOIN transfers ON a1.account_id = transfers.account_from OR a1.account_id = transfers.account_to " +
                            "JOIN accounts a2 ON transfers.account_to = a2.account_id " +
                            "JOIN users u2 ON a2.user_id = u2.user_id " +
                            "JOIN accounts a3 ON transfers.account_from = a3.account_id " +
                            "JOIN users u3 ON a3.user_id = u3.user_id " +
                            "WHERE u1.user_id = ?",
                            userId
        );

         while(results.next()) {


             listOfTransfers.add(this.makeReturnTransfer(
             results.getLong("transfer_id"),
             results.getLong("transfer_type_id"),
             results.getLong("transfer_status_id"),
             results.getLong("account_From"),
             results.getString("from_user"),
             results.getLong("account_To"),
             results.getString("to_user"),
             results.getBigDecimal("amount")
             ));

         }

         return listOfTransfers;
    }

    @Override
    public List<Transfer> retrievePendingRequests(int userId) {
        return null;
    }

    private int retrieveNextVenueId() {
        SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('seq_transfer_id')");
        if(nextIdResult.next()) {
            return nextIdResult.getInt("nextval");
        } else {
            throw new RuntimeException("Something went wrong while getting an id for the new Venue");
        }
    }

    private Transfer makeReturnTransfer(Long transferId, Long transferTypeId, Long transferStatusId, Long accountFrom, String nameAccountFrom, Long accountTo, String nameAccountTo, BigDecimal amount) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(transferId);
        transfer.setTransferTypeId(transferTypeId);
        transfer.setTransferStatusId(transferStatusId);
        transfer.setAccountFrom(accountFrom);
        transfer.setNameAccountFrom(nameAccountFrom);
        transfer.setAccountTo(accountTo);
        transfer.setNameAccountTo(nameAccountTo);
        transfer.setAmount(amount);
        return transfer;
    }

}
