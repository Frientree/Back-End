package com.d101.frientree.dto.calendar.dto;

import com.d101.frientree.entity.juice.JuiceDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CalendarWeeklyJuiceDetailDTO {
    @Schema(description = "주스 이름", example = "딸기 주스")
    private String juiceName;
    @Schema(description = "주스 이미지 URL", example = "www.zzz.com")
    private String juiceImageUrl;
    @Schema(description = "주스 세부 내용", example = "딸기 주스는 하빈이 최애 음료~!")
    private String juiceDescription;
    @Schema(description = "위로 메시지", example = "딸기 먹으면 오늘도 힘이 불끈불끈!!")
    private String condolenceMessage;

    public static CalendarWeeklyJuiceDetailDTO createCalendarWeeklyJuiceDetailDTO(JuiceDetail juiceDetail, String condolenceMessage){
        return CalendarWeeklyJuiceDetailDTO.builder()
                .juiceName(juiceDetail.getJuiceName())
                .juiceImageUrl(juiceDetail.getJuiceImageUrl())
                .juiceDescription(juiceDetail.getJuiceInfo())
                .condolenceMessage(condolenceMessage)
                .build();
    }
}
