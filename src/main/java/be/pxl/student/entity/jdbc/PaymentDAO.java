package be.pxl.student.entity.jdbc;

import be.pxl.student.entity.DAO;
import be.pxl.student.entity.Payment;
import be.pxl.student.entity.exception.PaymentException;
import be.pxl.student.entity.exception.PaymentNotFoundException;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO implements DAO<Payment, PaymentException> {

    private static final String SELECT_BY_ID = "select * from Payment where id = ?";
    private static final String CREATE_PAYMENT = "insert into Payment (`IBAN`, `date`, `amount`, `currency`, `detail`, `accountId`, `counterAccountId`) values(?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL = "select * from Payment";
    private static final String UPDATE_PAYMENT = "update Payment set IBAN = ?, date = ?, amount = ?, currency = ?, detail = ? where id = ?";
    private static final String DELETE_PAYMENT = "delete from Payment where id = ?";

    private DAOManager manager;

    public PaymentDAO(DAOManager manager) {
        this.manager = manager;
    }

    @Override
    public Payment create(Payment payment) throws PaymentException {
        try(PreparedStatement preparedStatement = manager.getConnection().prepareStatement(CREATE_PAYMENT)) {
            preparedStatement.setString(1, payment.getIBAN());
            preparedStatement.setDate(2, new Date(payment.getDate().getTime()));
            preparedStatement.setFloat(3, payment.getAmount());
            preparedStatement.setString(4, payment.getCurrency());
            preparedStatement.setString(5, payment.getDetail());
            preparedStatement.setInt(6, payment.getAccount().getId());
            preparedStatement.setInt(7, payment.getCounterAccount().getId());
            int result = preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.first()) {
                int paymentId = generatedKeys.getInt(1);
                payment.setId(paymentId);
            }
            if (result == 1) {
                return payment;
            }
            manager.commit();
        } catch (SQLException e) {
            manager.rollback(e);
            throw new PaymentException(String.format("Error creating payment [%s]", payment.toString()), e);
        }
        throw new PaymentException("Could not create Payment");
    }

    @Override
    public Payment getById(int id) throws PaymentException {
        try (PreparedStatement preparedStatement = manager.getConnection().prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.first()) {
                return new Payment(
                    resultSet.getInt("id"),
                    resultSet.getString("IBAN"),
                    resultSet.getDate("date"),
                    resultSet.getFloat("amount"),
                    resultSet.getString("currency"),
                    resultSet.getString("detail")
                );
            } else {
                throw new PaymentNotFoundException(String.format("Account with id [%d] not found", id));
            }
        } catch (SQLException e) {
            throw new PaymentException(String.format("Error while retrieving account with id [%d]", id), e);
        }
    }

    @Override
    public List<Payment> getAll() throws PaymentException {
        List<Payment> paymentList = new ArrayList<>();
        try (PreparedStatement preparedStatement = manager.getConnection().prepareStatement(SELECT_ALL)) {
            ResultSet resultSet =preparedStatement.executeQuery();
            while (resultSet.next()) {
                paymentList.add(new Payment (
                        resultSet.getString("IBAN"),
                        resultSet.getDate("date"),
                        resultSet.getFloat("amount"),
                        resultSet.getString("currency"),
                        resultSet.getString("detail")
                    )
                );
            }

        } catch (SQLException e) {
            throw new PaymentException("Error retrieving payments", e);
        }
        return paymentList;
    }

    @Override
    public Payment update(Payment payment) throws PaymentException {
        try (PreparedStatement preparedStatement = manager.getConnection().prepareStatement(UPDATE_PAYMENT)) {
            preparedStatement.setString(1, payment.getIBAN());
            preparedStatement.setDate(2, new Date(payment.getDate().getTime()));
            preparedStatement.setFloat(3, payment.getAmount());
            preparedStatement.setString(4, payment.getCurrency());
            preparedStatement.setString(5, payment.getDetail());
            preparedStatement.setInt(6, payment.getId());
            int result = preparedStatement.executeUpdate();
            if (result == 1) {
                return payment;
            }
        } catch (SQLException e) {
            throw new PaymentException(String.format("Error while updating Payment [%s]", payment.toString()), e);
        }
        throw new PaymentException("Could not update Payment");
    }

    @Override
    public Payment delete(Payment payment) throws PaymentException {
        try (PreparedStatement preparedStatement = manager.getConnection().prepareStatement(DELETE_PAYMENT)) {
            preparedStatement.setInt(1, payment.getId());
            int result = preparedStatement.executeUpdate();
            if (result ==1) {
                return payment;
            }
        } catch (SQLException e) {
            throw new PaymentException(String.format("Error while deleting Payment [%s]", payment.toString()), e);
        }
        throw new PaymentException("Could not delete Payment");
    }
}
