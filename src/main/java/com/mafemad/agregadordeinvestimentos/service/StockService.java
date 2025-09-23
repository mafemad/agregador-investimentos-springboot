package com.mafemad.agregadordeinvestimentos.service;


import com.mafemad.agregadordeinvestimentos.controller.dto.CreateStockDTO;
import com.mafemad.agregadordeinvestimentos.entity.Stock;
import com.mafemad.agregadordeinvestimentos.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {


    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public void createStock(CreateStockDTO createStockDto) {

        // DTO -> entity
        var stock = new Stock(createStockDto.stockId(), createStockDto.description());

        stockRepository.save(stock);
    }

    public List<Stock> getStocks(){
        return stockRepository.findAll();
    }
}
