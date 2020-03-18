package be.pxl.student.entity;

import java.util.List;

public class PaymentDAO implements DAO<Payment, PaymentException>{



    @Override
    public Payment create(Payment payment) throws PaymentException {
        throw new PaymentException("not yet implemented");
    }

    @Override
    public Payment getById(int id) throws PaymentException {
        throw new PaymentException("not yet implemented");
    }

    @Override
    public List<Payment> getAll() throws PaymentException {
        throw new PaymentException("not yet implemented");
    }

    @Override
    public Payment update(Payment payment) throws PaymentException {
        throw new PaymentException("not yet implemented");
    }

    @Override
    public Payment delete(Payment payment) throws PaymentException {
        throw new PaymentException("not yet implemented");
    }
}
