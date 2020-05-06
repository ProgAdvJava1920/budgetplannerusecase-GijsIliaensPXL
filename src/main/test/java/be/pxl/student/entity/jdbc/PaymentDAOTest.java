package be.pxl.student.entity.jdbc;

import be.pxl.student.entity.Account;
import be.pxl.student.entity.DAO;
import be.pxl.student.entity.Payment;
import be.pxl.student.entity.exception.PaymentException;
import be.pxl.student.util.BudgetPlannerMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaymentDAOTest {

    private static final String DB_URL = "jdbc:h2:mem:test;MODE=MYSQL;INIT=RUNSCRIPT FROM 'classpath:BudgetPlannerTest.sql'";
    private DAO<Payment, PaymentException> dao;
    private DAOManager manager;


    @BeforeEach
    void setUp() {
        manager = new DAOManager(DB_URL);
        dao = new PaymentDAO(manager);
    }

    @AfterEach
    void TearDown() {
        manager.close();
    }

    @Test
    void it_should_return_the_created_payment() throws ParseException, PaymentException {
        Payment paymentToCreate = new Payment("BE99454556567878", BudgetPlannerMapper.convertToDate("Thu Feb 13 05:47:35 CET 2020"), Float.parseFloat("100.50"), "EUR", "paymentdetails");
        paymentToCreate.setAccount(new Account(1, "accountname", "accountIBAN"));
        paymentToCreate.setCounterAccount(new Account(2, "counteriban", "countername"));
        Payment paymentCreated = dao.create(paymentToCreate);
        assertEquals(paymentCreated, paymentToCreate);
    }

    @Test
    void it_should_return_the_account_with_given_id() throws PaymentException {
        int id = 1;
        Payment paymentWithId = dao.getById(id);
        assertEquals(paymentWithId.getId(), id);
    }

    @Test
    void it_should_return_2_items() throws PaymentException {
        int expectedItems = 2;
        List<Payment> paymentList = dao.getAll();
        assertEquals(paymentList.size(), expectedItems);
    }

    @Test
    void it_should_return_the_updated_payment() throws PaymentException, ParseException {
        String newDetails = "Muy importante!!";
        Payment paymentToUpdate = dao.getById(1);
        paymentToUpdate.setDate(BudgetPlannerMapper.convertToDate("Thu Feb 13 05:47:35 CET 2020"));
        paymentToUpdate.setDetail(newDetails);
        Payment paymentUpdated = dao.update(paymentToUpdate);
        assertEquals(paymentUpdated.getDetail(), newDetails);
    }

    @Test
    void it_should_delete_the_payment_with_given_id() throws PaymentException {
        int paymentCount = dao.getAll().size();
        dao.delete(dao.getById(1));
        int paymentCountAfterDelete = dao.getAll().size();
        assertEquals(paymentCountAfterDelete, (paymentCount - 1));
    }
}