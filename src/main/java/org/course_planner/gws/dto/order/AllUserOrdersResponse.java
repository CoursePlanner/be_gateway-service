package org.course_planner.gws.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllUserOrdersResponse {
    private List<OrderDTO> orders;
    private Page pagination;
}
