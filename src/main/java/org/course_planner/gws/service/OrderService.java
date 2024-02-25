package org.course_planner.gws.service;

import org.course_planner.gws.dto.order.AllUserOrdersRequest;
import org.course_planner.gws.dto.order.AllUserOrdersResponse;
import org.course_planner.gws.dto.order.OrderDTO;

public interface OrderService {
    OrderDTO loadOrderForUser(String userId, Long orderId);

    AllUserOrdersResponse loadAllOrders(AllUserOrdersRequest request);

    OrderDTO createOrder(OrderDTO request);
}
