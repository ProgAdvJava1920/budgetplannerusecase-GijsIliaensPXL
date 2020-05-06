package be.pxl.student.entity.jpa;

import be.pxl.student.entity.Account;
import be.pxl.student.entity.DAO;
import be.pxl.student.entity.exception.AccountException;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class AccountJPA implements DAO<Account, AccountException> {

    private EntityManager entityManager;

    public AccountJPA(EntityManager manager) {
        this.entityManager = manager;
    }

    @Override
    public Account create(Account account) throws AccountException {
        entityManager.getTransaction().begin();
        entityManager.persist(account);
        entityManager.getTransaction().commit();
        return account;
    }

    @Override
    public Account getById(int id) throws AccountException {
        return entityManager.find(Account.class, id);
    }

    @Override
    public List<Account> getAll() throws AccountException {
        TypedQuery<Account> query = entityManager.createNamedQuery("findAllPayments", Account.class);
        return query.getResultList();
    }

    @Override
    public Account update(Account account) throws AccountException {
        entityManager.getTransaction().begin();
        entityManager.persist(account);
        entityManager.getTransaction().commit();
        return account;
    }

    @Override
    public Account delete(Account account) throws AccountException {
        entityManager.getTransaction().begin();
        Account accountToDelete = entityManager.find(Account.class, account.getId());
        entityManager.remove(accountToDelete);
        entityManager.getTransaction().commit();
        return account;
    }
}
