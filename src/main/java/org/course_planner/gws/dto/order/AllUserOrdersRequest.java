package org.course_planner.gws.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllUserOrdersRequest {
    private String userId;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private Page pagination;
}
