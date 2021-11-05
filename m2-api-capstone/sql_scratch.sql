SELECT accounts.account_id, accounts.user_id, accounts.balance, users.username
FROM accounts
JOIN users ON accounts.user_id = users.user_id
WHERE accounts.user_id = 1002;

SELECT transfers.transfer_id, transfers.transfer_type_id, transfers.transfer_status_id, transfers.account_from, users.username, transfers.amount
FROM transfers
JOIN accounts ON accounts.account_id = transfers.account_from
JOIN users ON users.user_id = accounts.user_id
WHERE transfers.transfer_id = 3021;

SELECT transfers.account_to, users.username
FROM transfers
JOIN accounts ON accounts.account_id = transfers.account_to
JOIN users ON users.user_id = accounts.user_id
WHERE transfers.transfer_id = 3021;

SELECT *  
FROM accounts 
JOIN users ON accounts.user_id = users.user_id 
JOIN transfers t1 ON t1.account_to = accounts.account_id 
JOIN transfers t2 ON t2.account_from = accounts.account_id 
WHERE users.user_id = 1002 

SELECT transfers.transfer_id, transfers.transfer_type_id, transfers.transfer_status_id, transfers.account_from, u3.username AS from_user, transfers.account_to, u2.username AS to_user, transfers.amount 
FROM users u1
JOIN accounts a1 ON u1.user_id = a1.user_id
JOIN transfers ON a1.account_id = transfers.account_from OR a1.account_id = transfers.account_to
JOIN accounts a2 ON transfers.account_to = a2.account_id
JOIN users u2 ON a2.user_id = u2.user_id
JOIN accounts a3 ON transfers.account_from = a3.account_id
JOIN users u3 ON a3.user_id = u3.user_id
WHERE u1.user_id = 1002 
