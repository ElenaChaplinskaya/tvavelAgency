package com.project.travelAgency.service.impl;


import com.project.travelAgency.dto.CartDetailDto;
import com.project.travelAgency.dto.CartDto;
import com.project.travelAgency.model.entity.Cart;
import com.project.travelAgency.model.entity.Tour;
import com.project.travelAgency.model.entity.User;
import com.project.travelAgency.model.repository.CartRepository;
import com.project.travelAgency.model.repository.TourRepository;
import com.project.travelAgency.service.CartService;
import com.project.travelAgency.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final TourRepository tourRepository;
    private final UserService userService;


    @Override
    public Cart createCart(User user, List<Long> tourIds) { //метод отработает, когда нужно будет добавлять туры в корзину
        Cart cart = new Cart(); //создает новую корзину
        cart.setUser(user); // сеттим юзера в корзину
        List<Tour> tourList = getCollectRefToursByIds(tourIds); //у нас есть список туров, который лежит в определенной корзине определенного пользователя
        cart.setTours(tourList);
        return cartRepository.save(cart);
    }

    //getOne берет ссылку на объект, findById- берет сам объект
    private List<Tour> getCollectRefToursByIds(List<Long> tourIds) {
        return tourIds.stream()
                .map(tourRepository::getOne)
                .collect(Collectors.toList());
    }

    //туры мы берем по id
    @Override
    public void addTours(Cart cart, List<Long> tourIds) {
        List<Tour> tours = cart.getTours();
        List<Tour> newTourList = tours == null ? new ArrayList<>() : new ArrayList<>(tours);
        newTourList.addAll(getCollectRefToursByIds(tourIds));
        cart.setTours(newTourList);
        cartRepository.save(cart);

    }

    @Override
    public Cart getById(Long id) {
        return cartRepository.getById(id);
    }

    @Override
    public void save(Cart cart) {
        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUser(String name) {
        User user = userService.findByName(name); // берем юзера из БД
        if (user == null || user.getCart() == null) { // если нет юзера или нет юзера с определ корзиной
            return new CartDto(); // возвращаем новую корзину ДТО
        }

        CartDto cardDto = new CartDto();  // создаем новую корзину ДТО
        Map<Long, CartDetailDto> mapByTourId = new HashMap<>();// создаем список туров где по id тура у нас есть определенное описание тура

        List<Tour> tours = user.getCart().getTours(); // создаем список туров у определенного юзера
        for (Tour tour : tours) {
            CartDetailDto detail = mapByTourId.get(tour.getId());  // detail это тур по id c описанием
            if (detail == null) {
                mapByTourId.put(tour.getId(), new CartDetailDto(tour));
            } else {
                detail.setAmount(detail.getAmount().add(new BigDecimal(1.0)));
                detail.setSum(detail.getSum() + Double.valueOf(tour.getPrice().toString()));
            }
        }

        cardDto.setCartDetails(new ArrayList<>(mapByTourId.values()));
        cardDto.aggregate();

        return cardDto;
    }

    @Override
    public CartDto deleteTourByUser(String name, Long tourId) {
        Cart cart = cartRepository.findByUserName(name);
        Tour tour = cart.getTours().stream().filter(t -> tourId.equals(t.getId())).findFirst().orElseThrow(NoSuchElementException::new);
        cart.getTours().remove(tour);
        Cart savedCart = cartRepository.save(cart);
        CartDto cardDto = new CartDto();
        Map<Long, CartDetailDto> mapByTourId = new HashMap<>();
        List<Tour> tours = savedCart.getTours();
        for (Tour t : tours) {
            CartDetailDto detail = mapByTourId.get(tour.getId());
            if (detail == null) {
                mapByTourId.put(tour.getId(), new CartDetailDto(tour));
            } else {
                detail.setAmount(detail.getAmount().add(new BigDecimal(1.0)));
                detail.setSum(detail.getSum() + Double.valueOf(tour.getPrice().toString()));
            }
        }

        cardDto.setCartDetails(new ArrayList<>(mapByTourId.values()));
        cardDto.aggregate();

        return cardDto;

    }

}
