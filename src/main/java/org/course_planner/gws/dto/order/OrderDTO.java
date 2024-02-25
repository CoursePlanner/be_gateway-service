package org.course_planner.gws.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long orderId;
    private Long productId;
    private String productName;
    private Double productPrice;
    private Integer quantity;
    private LocalDateTime purchasedOn;
    private OrderStatus orderStatus;
    private Long paymentId;
    private String userId;
}
