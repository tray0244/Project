package com.delivery.userservice.user.dto;

import com.delivery.userservice.user.domain.User;
import com.delivery.userservice.user.domain.UserAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressRequest {
    private String addressName;
    private String city;
    private String street;
    private String zipcode;
    private String detailAddress;

    // DTO -> Entity 반환
    public UserAddress toEntity(User user){
        return new UserAddress(user,addressName,city,street,zipcode, detailAddress);
    }
}
