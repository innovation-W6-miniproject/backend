package com.example.miniproject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    @NotBlank
    private String userId;

    @NotBlank
    private String password;

}
