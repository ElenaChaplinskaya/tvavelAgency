package com.project.travelAgency.controller;

import com.project.travelAgency.dto.CountryDto;
import com.project.travelAgency.dto.TourDto;
import com.project.travelAgency.dto.TypeOfTourDto;
import com.project.travelAgency.service.impl.TourServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tours")
public class TourController {

    private final TourServiceImpl tourService;


    @GetMapping
    public String list(Model model) {
        List<TourDto> list = tourService.getAll();
        model.addAttribute("tours", list);
        return "tours";
    }

    //добавляем туры в заказ
    @GetMapping("/{id}/order")
    public String addOrder(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/tours";
        }
        tourService.addToUserOrder(id, principal.getName());
        return "redirect:/tours";
    }

    @GetMapping("/showCreateTour")
    public String showCreateTour(Model model) {

        model.addAttribute("typeOfTour", new TypeOfTourDto());
        model.addAttribute("country", new CountryDto());
        model.addAttribute("tour", new TourDto());
        return "createTour";
    }

    @PostMapping("/createTour")
    public String createTour(@ModelAttribute TourDto tourDto,
                             @ModelAttribute TypeOfTourDto typeOfTourDto,
                             @ModelAttribute CountryDto countryDto,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "tours";
        }
        tourService.save(tourDto, typeOfTourDto, countryDto);
        List<TourDto> list = tourService.getAll();
        model.addAttribute("tours", list);
        return "tours";
    }


}