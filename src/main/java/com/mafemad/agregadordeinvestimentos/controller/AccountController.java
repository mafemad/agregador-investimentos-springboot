package com.mafemad.agregadordeinvestimentos.controller;


import com.mafemad.agregadordeinvestimentos.controller.dto.AccountStockResponseDTO;
import com.mafemad.agregadordeinvestimentos.controller.dto.AssociateAccountStockDTO;
import com.mafemad.agregadordeinvestimentos.controller.dto.CreateAccountDTO;
import com.mafemad.agregadordeinvestimentos.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/{accountId}/stocks")
    public ResponseEntity<Void> associateStock(@PathVariable("accountId") String accountId,
                                              @RequestBody AssociateAccountStockDTO dto){

        accountService.associateStock(accountId, dto);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{accountId}/stocks")
    public ResponseEntity<List<AccountStockResponseDTO>> getStocks(@PathVariable("accountId") String accountId){

        var stocks = accountService.listStocks(accountId);

        return ResponseEntity.ok(stocks);
    }

}
