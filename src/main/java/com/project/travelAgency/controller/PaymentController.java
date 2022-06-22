package com.project.travelAgency.controller;

import com.project.travelAgency.model.entity.Order;
import com.project.travelAgency.model.entity.OrderStatus;
import com.project.travelAgency.model.repository.OrderRepository;
import com.project.travelAgency.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/payment/{id}")
public class PaymentController {

    private final OrderRepository orderRepository;
    private final OrderService orderService;

    @GetMapping
    public String makeAPayment(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderRepository.findById(id).get());
        orderRepository.findById(id).get();

        return "payment";
    }

    @PostMapping
    public String makeAPayment(@ModelAttribute Order order) {

        order.setStatus(OrderStatus.PAID);
        orderService.saveOrder(order);

        return "redirect:/order";
    }
}
