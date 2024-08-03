package com.example.Amazonia_Online_Store.dto.orders;

import com.example.Amazonia_Online_Store.models.EStatus;
import jakarta.validation.constraints.NotNull;

public record OrderStatusDTO(@NotNull Long id,
                             @NotNull EStatus status) {
}
