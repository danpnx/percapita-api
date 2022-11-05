package br.com.project.service;

import br.com.project.enums.TransactionCategory;
import br.com.project.exceptions.BadCredentialsException;
import br.com.project.exceptions.DatabaseException;
import br.com.project.models.FinancialTransaction;
import br.com.project.models.User;
import br.com.project.repositories.FinancialTransactionRepository;
import br.com.project.utils.Utilities;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class FinancialTransactionService {

    private final FinancialTransactionRepository transactionRepository;
    private final UserService userService;

    public FinancialTransactionService(FinancialTransactionRepository transactionRepository, UserService userService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    // Retorna uma transação se ela existir e pertencer ao usuário autenticado
    private FinancialTransaction getFinancialTransaction(UUID id, String username) {
        try{
            if(transactionRepository.findById(id).get().getUser().getUsername().equals(username)) {
                return transactionRepository.findById(id).get();
            }
        } catch(EmptyResultDataAccessException | NoSuchElementException e) {
            throw new DatabaseException("Não foi possível realizar a ação");
        }

        return null;
    }

    public void registerTransaction(FinancialTransaction transaction, String username) {
        if(!Utilities.isGreaterThanZero(transaction.getTransactionValue())) {
            throw new BadCredentialsException("Digite um valor maior que zero");
        }

        User u = userService.findByUsername(username);
        transaction.setUser(u);
        transactionRepository.save(transaction);
    }

    public HttpStatus deleteTransaction(UUID id, String username) {
        FinancialTransaction t = getFinancialTransaction(id, username);

        if(t == null) {
            return HttpStatus.UNAUTHORIZED;
        }
        transactionRepository.deleteById(t.getTransaction_id());

        return HttpStatus.OK;
    }

    public FinancialTransaction getLastTransaction(String username) throws ParseException {
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();

        try{
            Date d = DateUtils.parseDate("01/" + month + "/" + year, "dd/MM/yyyy");
            List<FinancialTransaction> list = findByYearAndMonth(d, username);
            return list.get(list.size() - 1);
        } catch(ParseException e) {
            throw new ParseException("Digite uma data válida", e.getErrorOffset());
        }
    }

    public List<FinancialTransaction> findAllByCategory(TransactionCategory category, String username) {
        User u = userService.findByUsername(username);

        List<FinancialTransaction> list = u.getTransactions().stream().filter(t -> t.getTransactionCategory().equals(category)).toList();

        if(list.isEmpty()) {
            throw new DatabaseException("Você não possui nenhuma transação com esta categoria");
        }

        return list;
    }

    public List<FinancialTransaction> getAllTransactions(String username) {
        User u = userService.findByUsername(username);
        List<FinancialTransaction> list = transactionRepository.findByUser(u);

        if(list.isEmpty()) {
            throw new DatabaseException("Você não possui nenhuma transação registrada");
        }

        return list;
    }

    public HttpStatus editValue(BigDecimal value, UUID id, String username) {
        FinancialTransaction transaction = getFinancialTransaction(id, username);

        if(transaction == null) {
            return HttpStatus.UNAUTHORIZED;
        }

        transaction.setTransactionValue(value);
        transactionRepository.save(transaction);
        return HttpStatus.OK;
    }

    public HttpStatus editCategory(TransactionCategory category, UUID id, String username) {
        FinancialTransaction transaction = getFinancialTransaction(id, username);

        if(transaction == null) {
            return HttpStatus.FORBIDDEN;
        }

        transaction.setTransactionCategory(category);
        transactionRepository.save(transaction);
        return HttpStatus.OK;
    }

    public HttpStatus editDate(Date date, UUID id, String username) {
        FinancialTransaction transaction = getFinancialTransaction(id, username);

        if(transaction == null) {
            return HttpStatus.UNAUTHORIZED;
        }

        transaction.setTransactionDate(date);
        transactionRepository.save(transaction);
        return HttpStatus.OK;
    }

    public HttpStatus editDescription(String description, UUID id, String username) {
        FinancialTransaction transaction = getFinancialTransaction(id, username);

        if(transaction == null) {
            return HttpStatus.UNAUTHORIZED;
        }

        transaction.setTransactionDescription(description);
        transactionRepository.save(transaction);
        return HttpStatus.OK;
    }

    public HttpStatus changeTag(UUID id, String tagName, String username) {
        User u = userService.findByUsername(username);

        FinancialTransaction transaction = getFinancialTransaction(id, username);

        if(transaction == null) {
            return HttpStatus.UNAUTHORIZED;
        }

        try{
            transaction.setTag(u.getTags().stream().filter(t -> t.getTagName().equals(tagName)).findFirst().get());
            transactionRepository.save(transaction);
            return HttpStatus.OK;
        }catch(NoSuchElementException e) {
            throw new DatabaseException("Insira uma tag existente");
        }
    }

    public List<FinancialTransaction> findByTag(String tagName, String username) {
        User u = userService.findByUsername(username);

        List<FinancialTransaction> list = u.getTransactions().stream().filter(t -> t.getTag().getTagName().equals(tagName)).toList();

        if(list.isEmpty()) {
            throw new DatabaseException("Você não possui nenhuma transação registrada com esta tag");
        }

        return list;
    }

    public List<FinancialTransaction> findByYearAndMonth(Date date, String username) {
        List<FinancialTransaction> list = transactionRepository.findAllByTransactionDate(date)
                .stream()
                .filter(t -> t.getUser().getUsername().equals(username))
                .toList();

        if(list.isEmpty()) {
            throw new DatabaseException("Você não possui nenhuma transação registrada nesta data");
        }

        return list;
    }
}