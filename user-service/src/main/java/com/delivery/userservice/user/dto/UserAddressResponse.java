package com.delivery.userservice.user.dto;

import com.delivery.userservice.user.domain.UserAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 응답용 DTO
@Getter
@AllArgsConstructor
public class UserAddressResponse {
    private Long id;
    private String addressName;
    private String fullAddress; // 편의로 합쳐서 보여줍니다.

    public static UserAddressResponse from(UserAddress address){
        return new UserAddressResponse(
                address.getId(),
                address.getAddressName(),
                address.getCity() + " " + address.getStreet() + " " + address.getDetailAddress()
        );
    }
}
