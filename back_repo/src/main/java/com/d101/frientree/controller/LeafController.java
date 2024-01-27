package com.d101.frientree.controller;

import com.d101.frientree.dto.leaf.response.dto.LeafCreateRequestDTO;
import com.d101.frientree.dto.leaf.response.dto.LeafCreateResponseDTO;
import com.d101.frientree.dto.leaf.response.dto.LeafReadResponseDTO;
import com.d101.frientree.service.LeafService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/leaf")
@CrossOrigin("*")
public class LeafController {

    private final LeafService leafService;

    @GetMapping("/{category}")
    private ResponseEntity<LeafReadResponseDTO> leafConfirmation(@PathVariable String category){
        LeafReadResponseDTO readByLeafCategory = leafService.readByLeafCategory(category);
        return ResponseEntity.status(HttpStatus.OK).body(readByLeafCategory);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<LeafCreateResponseDTO> leafGeneration(@RequestBody LeafCreateRequestDTO leafCreateRequestDTO) {
        LeafCreateResponseDTO createdLeaf = leafService.createLeaf(leafCreateRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLeaf);
    }

}
