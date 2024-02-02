package com.d101.frientree.dto.calendar.dto;

import com.d101.frientree.entity.juice.JuiceDetail;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CalendarWeeklyJuiceDetailDTO {
    private String juiceName;
    private String juiceImageUrl;
    private String juiceDescription;
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
