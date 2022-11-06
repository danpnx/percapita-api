package br.com.project.service;

import br.com.project.controllers.FinancialTransactionController;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Transactional
public class FinancialTransactionService {

    private final FinancialTransactionRepository transactionRepository;
    private final UserService userService;

    public FinancialTransactionService(FinancialTransactionRepository transactionRepository, UserService userService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    // Método privado para retornar uma transação financeira se ela pertencer ao usuário autenticado
    private FinancialTransaction getFinancialTransaction(UUID id, String username) {
        try{
            Optional<FinancialTransaction> opt = transactionRepository.findById(id);

            if(opt.isEmpty()) {
                throw new NoSuchElementException();
            }

            FinancialTransaction t = opt.get();

            if(!t.getUser().getUsername().equals(username)) {
                throw new EmptyResultDataAccessException(1);
            }

            return t;
        } catch(EmptyResultDataAccessException | NoSuchElementException e) {
            throw new DatabaseException("Não foi possível realizar a ação");
        }
    }

    public void registerTransaction(FinancialTransaction transaction, String username) {
        if(!Utilities.isGreaterThanZero(transaction.getTransactionValue())) {
            throw new BadCredentialsException("Digite um valor maior que zero");
        }

        User u = userService.findByUsername(username);
        transaction.setUser(u);
        transactionRepository.save(transaction);
    }

    public void deleteTransaction(UUID id, String username) {
        FinancialTransaction t = getFinancialTransaction(id, username);

        transactionRepository.deleteById(t.getTransaction_id());
    }

    public FinancialTransaction getLastTransaction(String username) throws ParseException {
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();

        try{
            Date d = DateUtils.parseDate("01/" + month + "/" + year, "dd/MM/yyyy");
            List<FinancialTransaction> list = findByYearAndMonth(d, username);
            FinancialTransaction t = list.get(list.size() - 1);
            t.add(linkTo(methodOn(FinancialTransactionController.class)
                    .getAllTransactions()).withRel("Transações financeiras"));
            transactionRepository.save(t);
            return t;
        } catch(ParseException e) {
            throw new ParseException("Digite uma data válida", e.getErrorOffset());
        }
    }

    public FinancialTransaction getTransactionById(UUID id, String username) throws ParseException {
        FinancialTransaction transaction = getFinancialTransaction(id, username);
        transaction.add(linkTo(methodOn(FinancialTransactionController.class)
                .getAllTransactions()).withRel("Transações financeiras"));
        transactionRepository.save(transaction);
        return transaction;
    }

    public List<FinancialTransaction> findAllByCategory(TransactionCategory category, String username) throws ParseException {
        User u = userService.findByUsername(username);

        List<FinancialTransaction> list = u.getTransactions().stream().filter(t -> t.getTransactionCategory().equals(category)).toList();

        if(list.isEmpty()) {
            throw new DatabaseException("Você não possui nenhuma transação com esta categoria");
        }

        addLinksInList(list);

        return list;
    }

    public List<FinancialTransaction> getAllTransactions(String username) throws ParseException {
        User u = userService.findByUsername(username);
        List<FinancialTransaction> list = transactionRepository.findByUser(u);

        if(list.isEmpty()) {
            throw new DatabaseException("Você não possui nenhuma transação registrada");
        }

        addLinksInList(list);

        return list;
    }

    public void editValue(BigDecimal value, UUID id, String username) {
        FinancialTransaction transaction = getFinancialTransaction(id, username);
        transaction.setTransactionValue(value);
        transactionRepository.save(transaction);
    }

    public void editCategory(TransactionCategory category, UUID id, String username) {
        FinancialTransaction transaction = getFinancialTransaction(id, username);
        transaction.setTransactionCategory(category);
        transactionRepository.save(transaction);
    }

    public void editDate(Date date, UUID id, String username) {
        FinancialTransaction transaction = getFinancialTransaction(id, username);
        transaction.setTransactionDate(date);
        transactionRepository.save(transaction);
    }

    public void editDescription(String description, UUID id, String username) {
        FinancialTransaction transaction = getFinancialTransaction(id, username);
        transaction.setTransactionDescription(description);
        transactionRepository.save(transaction);
    }

    public void changeTag(UUID id, String tagName, String username) {
        User u = userService.findByUsername(username);
        FinancialTransaction transaction = getFinancialTransaction(id, username);

        try{
            transaction.setTag(u.getTags().stream().filter(t -> t.getTagName().equals(tagName)).findFirst().get());
            transactionRepository.save(transaction);
        }catch(NoSuchElementException e) {
            throw new DatabaseException("Insira uma tag existente");
        }
    }

    public List<FinancialTransaction> findByTag(String tagName, String username) throws ParseException {
        User u = userService.findByUsername(username);

        List<FinancialTransaction> list = u.getTransactions().stream().filter(t -> t.getTag().getTagName().equals(tagName)).toList();

        if(list.isEmpty()) {
            throw new DatabaseException("Você não possui nenhuma transação registrada com esta tag");
        }

        addLinksInList(list);

        return list;
    }

    public List<FinancialTransaction> findByYearAndMonth(Date date, String username) throws ParseException {
        List<FinancialTransaction> list = transactionRepository.findAllByTransactionDate(date)
                .stream()
                .filter(t -> t.getUser().getUsername().equals(username))
                .toList();

        if(list.isEmpty()) {
            throw new DatabaseException("Você não possui nenhuma transação registrada nesta data");
        }

        addLinksInList(list);

        return list;
    }

    private void addLinksInList(List<FinancialTransaction> list) throws ParseException {
        for(FinancialTransaction t: list) {
            t.add(linkTo(methodOn(FinancialTransactionController.class)
                    .getTransactionById(t.getTransaction_id().toString())).withSelfRel());

            t.add(linkTo(methodOn(FinancialTransactionController.class)
                    .deleteTransaction(t.getTransaction_id().toString())).withSelfRel());

            t.add(linkTo(methodOn(FinancialTransactionController.class)
                    .editValue(null, t.getTransaction_id().toString())).withSelfRel());

            t.add(linkTo(methodOn(FinancialTransactionController.class)
                    .editCategory(null, t.getTransaction_id().toString())).withSelfRel());

            t.add(linkTo(methodOn(FinancialTransactionController.class)
                    .editDate(null, t.getTransaction_id().toString())).withSelfRel());

            t.add(linkTo(methodOn(FinancialTransactionController.class)
                    .editDescription(null, t.getTransaction_id().toString())).withSelfRel());

            t.add(linkTo(methodOn(FinancialTransactionController.class)
                    .editTag(t.getTransaction_id().toString(), null)).withSelfRel());

            transactionRepository.save(t);
        }
    }
}