package be.pxl.student.entity;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO implements DAO<Account, AccountException> {

    Logger logger = LogManager.getLogger(AccountDAO.class);

    private Connection connection;

    private static final String SELECT_ALL = "select * from Account;";

    private DAOManager manager;

    public AccountDAO(DAOManager manager) {
        this.manager = manager;
    }



    @Override
    public Account getById(int id) throws AccountException {

        try (PreparedStatement preparedStatement = manager.getConnection().prepareStatement("select * from Account where id = ?")) {
            preparedStatement.setInt(1, id); // telt vanaf 1 niet 0
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.first()) {
                return new Account(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("IBAN")
                );
            } else {
                throw new AccountNotFoundException(String.format("Account with id [%d] not found", id));
            }

        } catch (SQLException e) {
            throw new AccountException(String.format("Exception while retrieving account with id [%d]", id), e);
        }

    }


    @Override
    public Account create(Account account) throws AccountException {
        try(PreparedStatement preparedStatement = manager.getConnection().prepareStatement("insert into account (`IBAN`, `name`) values(?,?)");) {


            preparedStatement.setString(2, account.getIBAN());
            preparedStatement.setString(3, account.getName());
            int result = preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.first()){
                int accountId = generatedKeys.getInt(1);
                account.setId(accountId);
            }
            if(result == 1){
                return account;
            }
            manager.commit();

        } catch (SQLException e) {
            manager.rollback(e);
            throw new AccountException(String.format("Error creating account [%s]", account), e);
        }
        throw new AccountException("not yet implemented");
    }

    @Override
    public List<Account> getAll() throws AccountException {
        List<Account> accountList = new ArrayList<>();
        try (PreparedStatement preparedStatement = manager.getConnection().prepareStatement(SELECT_ALL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                accountList.add(
                        new Account(
                                resultSet.getInt("id"),
                                resultSet.getString("name"),
                                resultSet.getString("IBAN")
                        )
                );
            }

        } catch (SQLException e) {
            throw new AccountException("Error retrieving accounts", e);
        }
        return accountList;
    }

    @Override
    public Account update(Account account) throws AccountException {
        throw new AccountException("not yet implemented");
    }

    @Override
    public Account delete(Account account) throws AccountException {
        throw new AccountException("not yet implemented");
    }



}
