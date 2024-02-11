package com.d101.frientree.serviceImpl.app;

import com.d101.frientree.dto.terms.response.TermsResponse;
import com.d101.frientree.dto.terms.response.dto.TermsResponseDTO;
import com.d101.frientree.entity.app.Terms;
import com.d101.frientree.repository.app.TermsRepository;
import com.d101.frientree.service.app.TermsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TermsServiceImpl implements TermsService {
    private final TermsRepository termsRepository;

    @Override
    public ResponseEntity<TermsResponse> confirm() {
        List<Terms> termsList = termsRepository.findAllOrderByTermsNecessaryDescAndTermsNumAsc();

        List<TermsResponseDTO> termsResponseDTOList = new ArrayList<>();
        for(Terms terms : termsList){
                termsResponseDTOList.add(
                        TermsResponseDTO.createTermsResponseDTO(terms)
                );
        }
        TermsResponse response =
                TermsResponse.createTermsResponse("Success", termsResponseDTOList);

        return ResponseEntity.ok(response);
    }
}
