package com.pranav.departmentemployee.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CurrentUserResponse {

    private String username;
    private String role;
}