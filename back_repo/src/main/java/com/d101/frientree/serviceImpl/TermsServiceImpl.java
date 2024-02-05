package com.d101.frientree.serviceImpl;

import com.d101.frientree.dto.terms.response.TermsResponse;
import com.d101.frientree.dto.terms.response.dto.TermsResponseDTO;
import com.d101.frientree.entity.app.Terms;
import com.d101.frientree.exception.user.UserNotFoundException;
import com.d101.frientree.repository.TermsRepository;
import com.d101.frientree.service.TermsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TermsServiceImpl implements TermsService {

    private final TermsRepository termsRepository;


    @Override
    public ResponseEntity<TermsResponse> confirm() {
        // 데이터베이스에서 모든 약관 정보 가져오기
        List<Terms> termsList = termsRepository.findAll();

        // 약관 정보를 DTO로 변환
        // termsList 리스트에 있는 Terms 객체들을 각각 TermsResponseDTO 객체로 변환하여 새로운 리스트인 data로 만들기
        List<TermsResponseDTO> data = termsList.stream()
                // map 함수를 통해 각 Terms 객체를 ResponseDTO객체로 변환시캬주기
                .map(TermsResponseDTO::createTermsResponseDTO)
                // isNecessary를 기준으로 내림차순으로 정렬 ( true를 우선으로 정렬 )
                .sorted(Comparator.comparing(TermsResponseDTO::isNecessary, Comparator.reverseOrder()))
                // 리스트로 만들기
                .toList();

        TermsResponse response = TermsResponse.createTermsResponse(
                "Success", data
        );

        return ResponseEntity.ok(response);
    }
}
