package br.com.project.service;

import br.com.project.controllers.FinancialTransactionController;
import br.com.project.enums.TransactionCategory;
import br.com.project.exceptions.*;
import br.com.project.models.FinancialTransaction;
import br.com.project.models.Tag;
import br.com.project.models.User;
import br.com.project.repositories.FinancialTransactionRepository;
import br.com.project.utils.InputUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Transactional
public class FinancialTransactionService {

    private final FinancialTransactionRepository transactionRepository;
    private final UserService userService;
    private final TagService tagService;

    public FinancialTransactionService(FinancialTransactionRepository transactionRepository, UserService userService, TagService tagService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.tagService = tagService;
    }

    public void registerTransaction(FinancialTransaction transaction, String username, String tagName) {
        if(!InputUtils.isGreaterThanZero(transaction.getTransactionValue())) {
            throw new InvalidInputException("Digite um valor maior que zero");
        }

        User u = userService.findByUsername(username);

        // Salva uma tag se ela existir para o usuário, cria uma tag se não existir
        if(tagService.existsByTagNameAndUser(tagName, username)) {
            transaction.setTag(tagService.getTagByTagNameAndUser(tagName, u));
        } else {
            transaction.setTag(tagService.registerTagInFinancialTransaction(tagName, u));
        }

        transaction.setUser(u);
        transactionRepository.save(transaction);
    }

    public void deleteTransaction(UUID id, String username) {
        FinancialTransaction transaction = getTransaction(id, username);
        transactionRepository.deleteById(transaction.getTransactionId());
    }

    public FinancialTransaction getTransactionById(UUID id, String username) {
        FinancialTransaction transaction = getTransaction(id, username);

        String date = new SimpleDateFormat("dd/MM/yyyy").format(transaction.getTransactionDate());

        transaction.add(linkTo(methodOn(FinancialTransactionController.class).getAllTransactions(date)).withRel("Transações financeiras"));

        transactionRepository.save(transaction);
        return transaction;
    }

    public List<FinancialTransaction> findAllByCategory(TransactionCategory category, Date date, String username) throws ParseException {
        User u = userService.findByUsername(username);

        List<FinancialTransaction> list = transactionRepository.findAllByTransactionCategoryAndTransactionDateAndUser(category, date, u);

        if(list.isEmpty()) {
            throw new ResourceNotFoundException("Você não possui nenhuma transação registrada com esta categoria");
        }

        addLinksInList(list);
        return list;
    }

    public List<FinancialTransaction> getAllTransactions(String username, Date date) throws ParseException {
        User u = userService.findByUsername(username);
        List<FinancialTransaction> list = transactionRepository.findAllByTransactionDateAndUser(date, u);

        if(list.isEmpty()) {
            throw new ResourceNotFoundException("Você não possui nenhuma transação registrada neste mês");
        }

        addLinksInList(list);

        return list;
    }

    public List<FinancialTransaction> findByTag(String tagName, String username, Date date) throws ParseException {
        if(!tagService.existsByTagNameAndUser(tagName, username)) {
            throw new DatabaseException("Essa tag não existe");
        }

        User u = userService.findByUsername(username);

        Tag t = tagService.getTagByTagNameAndUser(tagName, u);
        List<FinancialTransaction> list = transactionRepository.findAllByTagAndTransactionDateAndUser(t, date, u);

        if(list.isEmpty()) {
            throw new ResourceNotFoundException("Você não possui nenhuma transação registrada com esta tag");
        }

        addLinksInList(list);

        return list;
    }

//    public FinancialTransaction getLastTransaction(String username) {
//        int year = LocalDate.now().getYear();
//        int month = LocalDate.now().getMonthValue();
//
//        try{
//            Date d = DateUtils.parseDate("01/" + month + "/" + year, "dd/MM/yyyy");
//            User u = userService.findByUsername(username);
//
//            List<FinancialTransaction> list = transactionRepository.findAllByTransactionDateAndUser(d, u);
//
//            if(list.isEmpty()) {
//                throw new ResourceNotFoundException("Você ainda não possui nenhuma transação cadastrada neste mês");
//            }
//
//            FinancialTransaction transaction = list.get(list.size() - 1);
//
//            transaction.add(linkTo(methodOn(FinancialTransactionController.class).getAllTransactions(d.toString())).withRel("Transações financeiras"));
//
//            transactionRepository.save(transaction);
//            return transaction;
//        } catch(ParseException e) {
//            throw new InvalidInputException("Não foi possível converter a data");
//        }
//    }

    public void editValue(BigDecimal value, UUID id, String username) {
        if(!InputUtils.isGreaterThanZero(value)) {
            throw new InvalidInputException("Digite um valor maior que zero");
        }

        FinancialTransaction transaction = getTransaction(id, username);

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

    // Método privado para retornar uma transação financeira do usuario autenticado
    private FinancialTransaction getTransaction(UUID id, String username) {
        FinancialTransaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("O recurso buscado não existe"));

        if(!transaction.getUser().getUsername().equals(username)) {
            throw new AuthorizationException("Essa transação não pertence ao usuário autenticado");
        }

        return transaction;
    }

    private void addLinksInList(List<FinancialTransaction> list) {
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

    // Método usado em ReportService
    public List<FinancialTransaction> findByDateAndUser(Date date, User user) {
        return transactionRepository.findAllByTransactionDateAndUser(date, user);
    }
}