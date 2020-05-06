package be.pxl.student.jpa;

import be.pxl.student.entity.Account;
import be.pxl.student.entity.exception.AccountException;
import be.pxl.student.entity.jpa.AccountJPA;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import be.pxl.student.entity.DAO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AccountJPATest {
    DAO<Account, AccountException> dao;
    EntityManagerFactory entityManagerFactory;
    EntityManager entityManager;

    @BeforeEach
    void setUp() {
        entityManagerFactory = Persistence.createEntityManagerFactory("budgetplanner_test");
        entityManager = entityManagerFactory.createEntityManager();
        dao = new AccountJPA(entityManager);
    }

    @AfterEach
    void tearDown() {
        entityManager.close();
        entityManagerFactory.close();
    }

    @Test
    void it_should_create_the_given_account() throws AccountException {
        Account newAccount = new Account("DummyIBAN", "DummyName");
        Account account = dao.create(newAccount);
        assertEquals(account, newAccount);
    }

    @Test
    void it_should_return_the_account_with_given_id() throws AccountException {
        Account account = dao.getById(1);
        Account expected = new Account(1, "DummyName", "DummyIBAN");
        assertEquals(expected, account);
    }

    @Test
    void it_should_return_2_items() throws AccountException {
        List<Account> accounts = dao.getAll();
        assertEquals(2, accounts.size());
    }

    @Test
    void it_should_update_the_inserted_account() throws AccountException {
        String updater = "someOtherName";
        Account account = dao.getById(1);
        account.setName(updater);
        dao.update(account);
        entityManager.clear();

        assertEquals(updater, dao.getById(1).getName());
    }

    @Test
    void it_should_delete_the_inserted_account() throws AccountException {
        Account dummyAccount = dao.getById(1);
        dao.delete(dummyAccount);
        assertNull(dao.getById(1));
    }
}
