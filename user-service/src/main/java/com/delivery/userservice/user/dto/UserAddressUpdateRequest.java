package com.delivery.userservice.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressUpdateRequest {
    private String addressName;
    private String city;
    private String street;
    private String zipcode;
    private String detailAddress;
}
