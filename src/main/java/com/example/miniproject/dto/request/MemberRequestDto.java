package com.example.miniproject.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter

public class MemberRequestDto {

    @NotBlank
    @Size(min = 4, max = 12)
    @Pattern(regexp = "[a-zA-Z\\d]*${3,12}")
    private String userId;

    @NotBlank
    @Size(min = 4, max = 12)
    @Pattern(regexp = "[a-zA-Z가-힣\\d]*${3,12}")
    private String nickname;


    @NotBlank
    @Size(min = 4, max = 32)
    @Pattern(regexp = "[a-zA-Z\\d]*${3,32}")
    private String password;


    @NotBlank
    private String passwordConfirm;

}
