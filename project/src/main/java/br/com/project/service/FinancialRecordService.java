package br.com.project.service;

import br.com.project.controllers.FinancialRecordController;
import br.com.project.enums.TransactionCategory;
import br.com.project.exceptions.*;
import br.com.project.models.FinancialRecord;
import br.com.project.models.Tag;
import br.com.project.models.User;
import br.com.project.repositories.FinancialTransactionRepository;
import br.com.project.utils.Utilities;
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
public class FinancialRecordService {

    private final FinancialTransactionRepository transactionRepository;
    private final UserService userService;
    private final TagService tagService;

    public FinancialRecordService(FinancialTransactionRepository transactionRepository, UserService userService, TagService tagService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.tagService = tagService;
    }

    public void registerTransaction(FinancialRecord transaction, String username, String tagName) {
        if(!Utilities.isGreaterThanZero(transaction.getTransactionValue())) {
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
        FinancialRecord transaction = getTransaction(id, username);
        transactionRepository.deleteById(transaction.getTransactionId());
    }

    public FinancialRecord getTransactionById(UUID id, String username) {
        FinancialRecord transaction = getTransaction(id, username);

        String date = new SimpleDateFormat("dd/MM/yyyy").format(transaction.getTransactionDate());

        transaction.add(linkTo(methodOn(FinancialRecordController.class).getAllTransactions(date)).withRel("Transações financeiras"));

        transactionRepository.save(transaction);
        return transaction;
    }

    public List<FinancialRecord> findAllByCategory(TransactionCategory category, Date date, String username) throws ParseException {
        User u = userService.findByUsername(username);

        List<FinancialRecord> list = transactionRepository.findAllByTransactionCategoryAndTransactionDateAndUser(category, date, u);

        if(list.isEmpty()) {
            throw new ResourceNotFoundException("Você não possui nenhuma transação registrada com esta categoria");
        }

        addLinksInList(list);
        return list;
    }

    public List<FinancialRecord> getAllTransactions(String username, Date date) throws ParseException {
        User u = userService.findByUsername(username);
        List<FinancialRecord> list = transactionRepository.findAllByTransactionDateAndUser(date, u);

        if(list.isEmpty()) {
            throw new ResourceNotFoundException("Você não possui nenhuma transação registrada neste mês");
        }

        addLinksInList(list);

        return list;
    }

    public List<FinancialRecord> findByTag(String tagName, String username, Date date) throws ParseException {
        if(!tagService.existsByTagNameAndUser(tagName, username)) {
            throw new DatabaseException("Essa tag não existe");
        }

        User u = userService.findByUsername(username);

        Tag t = tagService.getTagByTagNameAndUser(tagName, u);
        List<FinancialRecord> list = transactionRepository.findAllByTagAndTransactionDateAndUser(t, date, u);

        if(list.isEmpty()) {
            throw new ResourceNotFoundException("Você não possui nenhuma transação registrada com esta tag");
        }

        addLinksInList(list);

        return list;
    }

    public List<FinancialRecord> findByYearAndMonth(Date date, String username) throws ParseException {
        User u = userService.findByUsername(username);

        List<FinancialRecord> list = transactionRepository.findAllByTransactionDateAndUser(date, u);

        if(list.isEmpty()) {
            throw new ResourceNotFoundException("Você não possui nenhuma transação registrada neste mês");
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
        if(!Utilities.isGreaterThanZero(value)) {
            throw new InvalidInputException("Digite um valor maior que zero");
        }

        FinancialRecord transaction = getTransaction(id, username);

        transaction.setTransactionValue(value);
        transactionRepository.save(transaction);
    }

    public void editCategory(TransactionCategory category, UUID id, String username) {
        FinancialRecord transaction = getTransaction(id, username);
        transaction.setTransactionCategory(category);
        transactionRepository.save(transaction);
    }

    public void editDate(Date date, UUID id, String username) {
        FinancialRecord transaction = getTransaction(id, username);
        transaction.setTransactionDate(date);
        transactionRepository.save(transaction);
    }

    public void editDescription(String description, UUID id, String username) {
        FinancialRecord transaction = getTransaction(id, username);
        transaction.setTransactionDescription(description);
        transactionRepository.save(transaction);
    }

    public void changeTag(UUID id, String tagName, String username) {
        if(Utilities.isExceedingTagNameSize(tagName)) {
            throw new InvalidInputException("O nome da tag não deve exceder 25 caracteres");
        }

        FinancialRecord transaction = getTransaction(id, username);
        User u = userService.findByUsername(username);

        transaction.setTag(
                u.getTags().stream()
                        .filter(t -> t.getTagName().equals(tagName))
                        .findFirst().orElseThrow(() -> new InvalidInputException("Insira uma tag existente"))
        );

        transactionRepository.save(transaction);
    }

    // Método privado para retornar uma transação financeira do usuario autenticado
    private FinancialRecord getTransaction(UUID id, String username) {
        FinancialRecord transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("O recurso buscado não existe"));

        if(!transaction.getUser().getUsername().equals(username)) {
            throw new AuthorizationException("Essa transação não pertence ao usuário autenticado");
        }

        return transaction;
    }

    private void addLinksInList(List<FinancialRecord> list) {
        for(FinancialRecord t: list) {
            t.add(linkTo(methodOn(FinancialRecordController.class)
                    .getTransactionById(t.getTransactionId().toString())).withSelfRel());

            t.add(linkTo(methodOn(FinancialRecordController.class)
                    .deleteTransaction(t.getTransactionId().toString())).withSelfRel());

            t.add(linkTo(methodOn(FinancialRecordController.class)
                    .editValue(null, t.getTransactionId().toString())).withSelfRel());

            t.add(linkTo(methodOn(FinancialRecordController.class)
                    .editCategory(null, t.getTransactionId().toString())).withSelfRel());

            t.add(linkTo(methodOn(FinancialRecordController.class)
                    .editDate(null, t.getTransactionId().toString())).withSelfRel());

            t.add(linkTo(methodOn(FinancialRecordController.class)
                    .editDescription(null, t.getTransactionId().toString())).withSelfRel());

            t.add(linkTo(methodOn(FinancialRecordController.class)
                    .editTag(t.getTransactionId().toString(), null)).withSelfRel());

            transactionRepository.save(t);
        }
    }
}