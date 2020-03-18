package be.pxl.student.util;

import be.pxl.student.entity.Account;
import be.pxl.student.entity.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BudgetPlannerMapperTest {

    BudgetPlannerMapper mapper;
    Path testCsvFile = Paths.get("C:\\Users\\gijsi\\IdeaProjects\\budgetplannerusecase-GijsIliaensPXL\\src\\main\\test\\resources\\account_payments_test.csv");
    List<String> accountLines;
    String testDataLine = "Jos,BE69771770897312,BE17795215960626,Thu Feb 13 05:47:35 CET 2020,265.8,EUR,Ut ut necessitatibus itaque ullam.";

    @BeforeEach
    void setUp() throws BudgetPlannerException {
        mapper = new BudgetPlannerMapper();
        accountLines = BudgetPlannerImporter.readCsvFile(testCsvFile);
    }

    @Test
    void it_should_return_non_empty_list() {
        List<Account> accountsList = mapper.mapAccounts(accountLines);
        assertFalse(accountsList.isEmpty());
    }

    @Test
    void it_should_map_to_account_list_with_1_account() {
        List<Account> accountList = mapper.mapAccounts(accountLines);
        assertEquals(1, accountList.size(), "it should have 1 account");
    }

    @Test
    void it_should_map_to_account_list_with_2_payments() {
        List<Account> accountList = mapper.mapAccounts(accountLines);
        assertEquals(2, accountList.get(0).getPayments().size(), "account should have 2 payments");
    }

    @Test
    void it_should_map_line_to_account_object() throws ParseException, BudgetPlannerException {
        Account lineToAccount = mapper.mapDataLineToAccount(testDataLine);
        Account expectedAccount = new Account("Jos", "BE69771770897312");

        List<Payment> payList = new ArrayList<Payment>();
        Payment payment = new Payment(
                "BE17795215960626",
                mapper.convertToDate("Thu Feb 13 05:47:35 CET 2020"),
                265.8f,
                "EUR",
                "Ut ut necessitatibus itaque ullam."
        );
        payList.add(payment);
        expectedAccount.setPayments(payList);

        assertEquals(expectedAccount, lineToAccount);
    }

    @Test
    void it_should_map_line_to_payment() throws ParseException, BudgetPlannerException {
        Payment expectedPayment = new Payment(
                "BE17795215960626",
                mapper.convertToDate("Thu Feb 13 05:47:35 CET 2020"),
                265.8f,
                "EUR",
                "Ut ut necessitatibus itaque ullam."
        );

        Payment actualPayment = mapper.mapItemsToPayment(testDataLine.split(","));

        assertEquals(expectedPayment, actualPayment);
    }

    @Test
    void it_should_convert_date_to_string_and_back_again() throws ParseException, BudgetPlannerException {
        String testDate = "Thu Feb 13 05:47:35 CET 2020";
        Date date = mapper.convertToDate(testDate);

        String dateToString = mapper.convertDateToString(date);
        assertEquals(testDate, dateToString);
    }
}