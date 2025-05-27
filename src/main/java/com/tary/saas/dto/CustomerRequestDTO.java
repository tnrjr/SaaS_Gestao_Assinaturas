package com.tary.saas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomerRequestDTO(
        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
        String name,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "O email deve ser válido")
        String email,

        @NotBlank(message = "O telefone é obrigatório")
        String phone
) {}
