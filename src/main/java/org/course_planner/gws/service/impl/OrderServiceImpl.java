package org.course_planner.gws.service.impl;

import lombok.RequiredArgsConstructor;
import org.course_planner.gws.constants.AuthenticationConstants;
import org.course_planner.gws.constants.OrderConstants;
import org.course_planner.gws.dto.order.AllUserOrdersRequest;
import org.course_planner.gws.dto.order.AllUserOrdersResponse;
import org.course_planner.gws.dto.order.OrderDTO;
import org.course_planner.gws.service.OrderService;
import org.course_planner.utils.rest.GenericResponseTemplate;
import org.course_planner.utils.rest.RESTClient;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final RESTClient restClient;

    @Override
    public OrderDTO loadOrderForUser(String userId, Long orderId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(AuthenticationConstants.CONST_USER_ID_HEADER_NAME, userId);
        headers.add(OrderConstants.CONST_ORDER_ID_HEADER_NAME, orderId + "");
        GenericResponseTemplate<OrderDTO> httpResponse = restClient.execute(
                OrderConstants.CONST_ORDER_SERVICE_PROP_REF, OrderConstants.CONST_RETRIEVE_ORDER_PROP_REF,
                headers, null, null, null);
        return httpResponse.getBody(OrderDTO.class);
    }

    @Override
    public AllUserOrdersResponse loadAllOrders(AllUserOrdersRequest request) {
        HttpHeaders headers = new HttpHeaders();
        GenericResponseTemplate<AllUserOrdersResponse> httpResponse = restClient.execute(
                OrderConstants.CONST_ORDER_SERVICE_PROP_REF, OrderConstants.CONST_LOAD_ALL_USER_ORDERS_PROP_REF,
                headers, request, null, null);
        return httpResponse.getBody(AllUserOrdersResponse.class);
    }

    @Override
    public OrderDTO createOrder(OrderDTO request) {
        HttpHeaders headers = new HttpHeaders();
        GenericResponseTemplate<OrderDTO> httpResponse = restClient.execute(
                OrderConstants.CONST_ORDER_SERVICE_PROP_REF, OrderConstants.CONST_CREATE_USER_ORDER_PROP_REF,
                headers, request, null, null);
        return httpResponse.getBody(OrderDTO.class);
    }
}
