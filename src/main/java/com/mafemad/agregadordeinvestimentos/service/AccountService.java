package com.mafemad.agregadordeinvestimentos.service;

import com.mafemad.agregadordeinvestimentos.client.BrapiClient;
import com.mafemad.agregadordeinvestimentos.controller.dto.AccountStockResponseDTO;
import com.mafemad.agregadordeinvestimentos.controller.dto.AssociateAccountStockDTO;
import com.mafemad.agregadordeinvestimentos.entity.AccountStock;
import com.mafemad.agregadordeinvestimentos.entity.AccountStockId;
import com.mafemad.agregadordeinvestimentos.repository.AccountRepository;
import com.mafemad.agregadordeinvestimentos.repository.AccountStockRepository;
import com.mafemad.agregadordeinvestimentos.repository.StockRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    @Value("#{environment.TOKEN}")
    private String TOKEN;

    private final AccountStockRepository accountStockRepository;
    private final BrapiClient brapiClient;
    private AccountRepository accountRepository;
    private StockRepository stockRepository;

    public AccountService(AccountRepository accountRepository, StockRepository stockRepository, AccountStockRepository accountStockRepository, BrapiClient brapiClient) {
        this.accountRepository = accountRepository;
        this.stockRepository = stockRepository;
        this.accountStockRepository = accountStockRepository;
        this.brapiClient = brapiClient;
    }


    public void associateStock(String accountId, AssociateAccountStockDTO dto) {

        var account = accountRepository.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "account not found"));

        var stock = stockRepository.findById(dto.stockId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "stock not found"));

        //DTO -> Entity
        var id = new AccountStockId(account.getAccountId(),stock.getStockId());
        var accountStockEntity = new AccountStock(
                id,
                account,
                stock,
                dto.quantity()
        );

        accountStockRepository.save(accountStockEntity);

    }

    public List<AccountStockResponseDTO> listStocks(String accountId) {
        var account = accountRepository.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "account not found"));

        return account.getAccountStocks().stream()
                .map(aStock ->  new AccountStockResponseDTO(
                        aStock.getStock().getStockId(),
                        aStock.getQuantity(), 
                        getTotal(aStock.getQuantity(), aStock.getStock().getStockId())))
                .toList();
    }

    private Double getTotal(Integer quantity, String stockId) {

        var response = brapiClient.getQuote(TOKEN, stockId);

        var price = response.results().getFirst().regularMarketPrice();

        return quantity * price;
    }

}
