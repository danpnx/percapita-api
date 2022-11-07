package br.com.project.service;

import br.com.project.controllers.FinancialTransactionController;
import br.com.project.enums.TransactionCategory;
import br.com.project.exceptions.*;
import br.com.project.models.FinancialTransaction;
import br.com.project.models.User;
import br.com.project.repositories.FinancialTransactionRepository;
import br.com.project.utils.Utilities;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.dao.EmptyResultDataAccessException;
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

    public void registerTransaction(FinancialTransaction transaction, String username) {
        if(!Utilities.isGreaterThanZero(transaction.getTransactionValue())) {
            throw new InvalidInputException("Digite um valor maior que zero");
        }

        if(!userService.existsByUsername(username)) {
            throw new DatabaseException("Usuário não existe");
        }

        User u = userService.findByUsername(username);
        transaction.setUser(u);
        transactionRepository.save(transaction);
    }

    public void deleteTransaction(UUID id, String username) {
        FinancialTransaction transaction = getTransaction(id, username);
        transactionRepository.deleteById(transaction.getTransactionId());
    }

    public FinancialTransaction getLastTransaction(String username) throws ParseException {
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();

        try{
            Date d = DateUtils.parseDate("01/" + month + "/" + year, "dd/MM/yyyy");

            List<FinancialTransaction> list = findByYearAndMonth(d, username);

            if(list.isEmpty()) {
                throw new ResourceNotFoundException();
            }

            FinancialTransaction transaction = list.get(list.size() - 1);

            transaction.add(linkTo(methodOn(FinancialTransactionController.class).getAllTransactions()).withRel("Transações financeiras"));

            transactionRepository.save(transaction);
            return transaction;
        } catch(ParseException e) {
            throw new InvalidInputException("Não foi possível converter a data");
        } catch(ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Você não possui transação financeira registrada neste mês");
        }
    }

    public FinancialTransaction getTransactionById(UUID id, String username) throws ParseException {
        FinancialTransaction transaction = getTransaction(id, username);

        transaction.add(linkTo(methodOn(FinancialTransactionController.class).getAllTransactions()).withRel("Transações financeiras"));

        transactionRepository.save(transaction);
        return transaction;
    }

    public List<FinancialTransaction> findAllByCategory(TransactionCategory category, String username) throws ParseException {
        if(!userService.existsByUsername(username)) {
            throw new DatabaseException("Usuário não existe");
        }

        User u = userService.findByUsername(username);

        List<FinancialTransaction> list = u.getTransactions().stream().filter(t -> t.getTransactionCategory().equals(category)).toList();
        if(list.isEmpty()) {
            throw new ResourceNotFoundException("Você não possui nenhuma transação registrada com esta categoria");
        }

        addLinksInList(list);
        return list;
    }

    public List<FinancialTransaction> getAllTransactions(String username) throws ParseException {
        try{
            User u = userService.findByUsername(username);
            List<FinancialTransaction> list = transactionRepository.findByUser(u);

            if(list.isEmpty()) {
                throw new ResourceNotFoundException("Você não possui nenhuma transação registrada");
            }

            addLinksInList(list);

            return list;
        } catch(EmptyResultDataAccessException e) {
            throw new DatabaseException("Usuário não encontrado");
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void editValue(BigDecimal value, UUID id, String username) {
        FinancialTransaction transaction = getTransaction(id, username);

        if(!Utilities.isGreaterThanZero(value)) {
            throw new InvalidInputException("Digite um valor maior que zero");
        }

        transaction.setTransactionValue(value);
        transactionRepository.save(transaction);
    }

    public void editCategory(TransactionCategory category, UUID id, String username) {
        FinancialTransaction transaction = getTransaction(id, username);
        transaction.setTransactionCategory(category);
        transactionRepository.save(transaction);
    }

    public void editDate(Date date, UUID id, String username) {
        FinancialTransaction transaction = getTransaction(id, username);
        transaction.setTransactionDate(date);
        transactionRepository.save(transaction);
    }

    public void editDescription(String description, UUID id, String username) {
        FinancialTransaction transaction = getTransaction(id, username);
        transaction.setTransactionDescription(description);
        transactionRepository.save(transaction);
    }

    public void changeTag(UUID id, String tagName, String username) {
        FinancialTransaction transaction = getTransaction(id, username);
        User u = userService.findByUsername(username);

        transaction.setTag(
                u.getTags().stream()
                        .filter(t -> t.getTagName().equals(tagName))
                        .findFirst().orElseThrow(() -> new InvalidInputException("Insira uma tag existente"))
        );

        transactionRepository.save(transaction);
    }

    public List<FinancialTransaction> findByTag(String tagName, String username) throws ParseException {
        User u = userService.findByUsername(username);

        List<FinancialTransaction> list = u.getTransactions().stream().filter(t -> t.getTag().getTagName().equals(tagName)).toList();

        if(list.isEmpty()) {
            throw new ResourceNotFoundException("Você não possui nenhuma transação registrada com esta tag");
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
            throw new ResourceNotFoundException("Você não possui nenhuma transação registrada nesta data");
        }

        addLinksInList(list);

        return list;
    }

    // Método privado para retornar uma transação financeira do usuario autenticado
    private FinancialTransaction getTransaction(UUID id, String username) {
        FinancialTransaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("O recurso buscado não existe"));

        if(!transaction.getUser().getUsername().equals(username)) {
            throw new AuthorizationException("Essa transação não pertence ao usuário autenticado");
        }

        return transaction;
    }

    private void addLinksInList(List<FinancialTransaction> list) throws ParseException {
        for(FinancialTransaction t: list) {
            t.add(linkTo(methodOn(FinancialTransactionController.class)
                    .getTransactionById(t.getTransactionId().toString())).withSelfRel());

            t.add(linkTo(methodOn(FinancialTransactionController.class)
                    .deleteTransaction(t.getTransactionId().toString())).withSelfRel());

            t.add(linkTo(methodOn(FinancialTransactionController.class)
                    .editValue(null, t.getTransactionId().toString())).withSelfRel());

            t.add(linkTo(methodOn(FinancialTransactionController.class)
                    .editCategory(null, t.getTransactionId().toString())).withSelfRel());

            t.add(linkTo(methodOn(FinancialTransactionController.class)
                    .editDate(null, t.getTransactionId().toString())).withSelfRel());

            t.add(linkTo(methodOn(FinancialTransactionController.class)
                    .editDescription(null, t.getTransactionId().toString())).withSelfRel());

            t.add(linkTo(methodOn(FinancialTransactionController.class)
                    .editTag(t.getTransactionId().toString(), null)).withSelfRel());

            transactionRepository.save(t);
        }
    }
}