package com.mafemad.agregadordeinvestimentos.controller;

import com.mafemad.agregadordeinvestimentos.controller.dto.CreateStockDTO;
import com.mafemad.agregadordeinvestimentos.controller.dto.CreateUserDto;
import com.mafemad.agregadordeinvestimentos.entity.Stock;
import com.mafemad.agregadordeinvestimentos.entity.User;
import com.mafemad.agregadordeinvestimentos.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/stocks")
public class StockController {


    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody CreateStockDTO createStockDto) {

        stockService.createStock(createStockDto);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Stock>> getStocks(){
        return ResponseEntity.ok(stockService.getStocks());
    }
}
