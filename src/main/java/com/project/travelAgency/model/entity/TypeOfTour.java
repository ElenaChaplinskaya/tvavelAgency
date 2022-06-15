package com.project.travelAgency.model.entity;

import com.project.travelAgency.dto.TypeOfTourDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.integration.annotation.Default;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
public class TypeOfTour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String title;


    public TypeOfTour(TypeOfTourDto typeOfTourDto) {
        this.title = typeOfTourDto.getTitle();

    }
}
