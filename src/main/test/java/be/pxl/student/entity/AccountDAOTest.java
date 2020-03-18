package be.pxl.student.entity;

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
    void getAll() throws AccountException {
       List<Account> accounts = dao.getAll();
       assertEquals(2, accounts.size());

    }

    @Test
    void it_should_return_account_with_id_1() throws AccountException, SQLException {


        Account account = dao.getById(1);
        Account expected = new Account(1, "dummyName", "dummyIBAN");
        assertEquals(expected, account);
    }

    @Test
    void update() {
        fail("not yet implemented");
    }

    @Test
    void create() {
        fail("not yet implemented");
    }

    @Test
    void delete() {
        fail("not yet implemented");
    }
}