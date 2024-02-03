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
import java.util.List;

@Service
@RequiredArgsConstructor
public class TermsServiceImpl implements TermsService {

    private final TermsRepository termsRepository;

    @Override
    public ResponseEntity<TermsResponse> confirm() {
        // 약관
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UserNotFoundException("해당하는 유저를 찾을 수 없습니다.");
        }

        // 이용약관을 google 사이트 도구 주소 https://sites.google.com/view/frientree 로 해놓으니까
        // 로그인하라는 페이지가 떠서 url이랑 약관 번호는 임시값으로 넣어두었습니다.
        Terms term = new Terms(1L, "frientree", "https://www.naver.com");

        List<TermsResponseDTO> data = new ArrayList<>();

        data.add(TermsResponseDTO.createTermsResponseDTO(term));

        TermsResponse response = TermsResponse.createTermsResponse(
                "Success", data

        );

        return ResponseEntity.ok(response);
    }
}
