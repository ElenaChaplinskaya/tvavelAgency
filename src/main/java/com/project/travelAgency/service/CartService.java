package com.project.travelAgency.service;


import com.project.travelAgency.dto.CartDto;
import com.project.travelAgency.model.entity.Cart;
import com.project.travelAgency.model.entity.User;

import java.util.List;

public interface CartService {

    Cart createCart(User user, List<Long> tourIds);

    void addTours(Cart cart, List<Long> tourIds);

    CartDto deleteTourByUser(String name, Long id);

    void save(Cart cart);

    CartDto getCartByUser(String name);// поиск корзины по user

    void commitCartToOrder(String username);
}
