package com.uni.lieferspatz.service.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.uni.lieferspatz.domain.WarenkorbItem;
import com.uni.lieferspatz.dto.api.WarenkorbApi;

public class WarenkorbMapper {
    public static WarenkorbApi mapToWarenkorbApi(WarenkorbItem warenkorbItem) {
        WarenkorbApi warenkorbApi = new WarenkorbApi();
        warenkorbApi.setItemId(warenkorbItem.getItem().getId());
        warenkorbApi.setPreis(warenkorbItem.getItem().getPreis());
        warenkorbApi.setQuantity(warenkorbItem.getQuantity());
        return warenkorbApi;
    }

    public static List<WarenkorbApi> mapToWarenkorbApi(List<WarenkorbItem> warenkorbItems) {
        return warenkorbItems.stream()
                .map(WarenkorbMapper::mapToWarenkorbApi)
                .collect(Collectors.toList());
    }
}
