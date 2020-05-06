package be.pxl.student.entity.jdbc;

import be.pxl.student.entity.Account;
import be.pxl.student.entity.exception.AccountException;
import be.pxl.student.entity.jdbc.AccountDAO;
import be.pxl.student.entity.jdbc.DAOManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountDAOTest {

    private static final String DB_URL = "jdbc:h2:mem:test;MODE=MYSQL;INIT=RUNSCRIPT FROM 'classpath:BudgetPlannerTest.sql'";
    DAOManager manager;
    AccountDAO dao;
    @BeforeEach
    void setUp() {
        manager = new DAOManager(DB_URL);
        dao= new AccountDAO(manager);

    }

    @AfterEach
    void tearDown() {
        manager.close();
    }

    @Test
    void it_should_return_3_items() throws AccountException {
       List<Account> accounts = dao.getAll();
       assertEquals(2, accounts.size());

    }

    @Test
    void it_should_return_account_with_id_1() throws AccountException, SQLException {


        Account account = dao.getById(1);
        Account expected = new Account(1, "DummyName", "DummyIBAN");
        assertEquals(expected, account);
    }

    @Test
    void it_should_return_the_updated_account() throws AccountException {
        Account accountToUpdate = dao.getById(1);
        accountToUpdate.setName("EDITED");
        accountToUpdate.setIBAN("EDITED");
        Account updatedAccount = dao.update(accountToUpdate);
        assertEquals(updatedAccount, accountToUpdate);
    }

    @Test
    void it_should_return_the_created_account() throws AccountException {
        Account accountToCreate = new Account("IBANDUMMYCREATE", "NAMECREATE");
        Account createdAccount = dao.create(accountToCreate);
        assertEquals(accountToCreate, createdAccount);
    }

    @Test
    void it_should_return_with_deleted_account() throws AccountException {
        int accountCount = dao.getAll().size();
        dao.delete(dao.getById(1));
        int accountCountAfterDelete = dao.getAll().size();
        assertEquals(accountCountAfterDelete, accountCount - 1);
    }
}