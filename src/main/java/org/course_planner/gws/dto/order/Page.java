package org.course_planner.gws.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Page {
    private Integer page;
    private Integer size;
    private Long totalRecords;
    private Long totalPages;
    private String direction;
}
