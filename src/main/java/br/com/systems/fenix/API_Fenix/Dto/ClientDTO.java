package br.com.systems.fenix.API_Fenix.Dto;

import java.util.List;

import br.com.systems.fenix.API_Fenix.Model.Promotion;

public record ClientDTO(

        String firstName,
        String lastName,
        Integer age,
        String telephone,
        String email,
        String imageURL,
        List<Promotion> promotions) {

}