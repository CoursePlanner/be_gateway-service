package org.course_planner.gws.controller;

import lombok.RequiredArgsConstructor;
import org.course_planner.gws.constants.AuthenticationConstants;
import org.course_planner.gws.dto.order.AllUserOrdersRequest;
import org.course_planner.gws.dto.order.AllUserOrdersResponse;
import org.course_planner.gws.dto.order.OrderDTO;
import org.course_planner.gws.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping(value = "/item/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDTO> loadOrderDetailsById(@PathVariable(name = "orderId") String orderId,
                                                         @RequestHeader(name = AuthenticationConstants.CONST_USER_ID_HEADER_NAME) String userId) {
        return new ResponseEntity<>(orderService.loadOrderForUser(userId, Long.parseLong(orderId)), HttpStatus.OK);
    }

    @PostMapping(value = "/all", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AllUserOrdersResponse> loadAllOrders(
            @RequestHeader(name = AuthenticationConstants.CONST_USER_ID_HEADER_NAME) String userId,
            @RequestBody AllUserOrdersRequest request) {
        request.setUserId(userId);
        return new ResponseEntity<>(orderService.loadAllOrders(request), HttpStatus.OK);
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDTO> createOrder(
            @RequestHeader(name = AuthenticationConstants.CONST_USER_ID_HEADER_NAME) String userId,
            @RequestBody OrderDTO request) {
        request.setUserId(userId);
        return new ResponseEntity<>(orderService.createOrder(request), HttpStatus.CREATED);
    }
}
