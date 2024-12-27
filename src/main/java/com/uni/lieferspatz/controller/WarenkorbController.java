package com.uni.lieferspatz.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uni.lieferspatz.domain.WarenkorbItem;
import com.uni.lieferspatz.dto.api.WarenkorbApi;
import com.uni.lieferspatz.dto.payload.WarenkorbItemPayload;
import com.uni.lieferspatz.service.WarenkorbService;
import com.uni.lieferspatz.service.mapper.WarenkorbMapper;

@RestController
@RequestMapping(value = "/api/v1/warenkorb")
public class WarenkorbController {
    private final WarenkorbService warenkorbService;

    public WarenkorbController(WarenkorbService warenkorbService) {
        this.warenkorbService = warenkorbService;
    }

    @GetMapping
    public ResponseEntity<List<WarenkorbApi>> getWarenkorbItems() {
        List<WarenkorbItem> warenkorbItems = this.warenkorbService.getWarenkorbItems();
        List<WarenkorbApi> result = WarenkorbMapper.mapToWarenkorbApi(warenkorbItems);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addOrUpdateWarenkorbItem(@RequestBody WarenkorbItemPayload warenkorbItemPayload) {
        this.warenkorbService.addOrUpdateWarenkorbItem(warenkorbItemPayload);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> removeWarenkorbItem(@PathVariable Long itemId) {
        this.warenkorbService.deleteByItemId(itemId);
        return ResponseEntity.ok().build();
    }
}
