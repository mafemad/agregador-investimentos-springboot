package com.mafemad.agregadordeinvestimentos.client.dto;

import java.util.List;

public record BrapiResponseDTO(List<StockDTO> results) {
}
