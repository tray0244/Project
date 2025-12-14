package com.delivery.userservice.user.controller;

import com.delivery.userservice.user.dto.UserAddressRequest;
import com.delivery.userservice.user.dto.UserAddressResponse;
import com.delivery.userservice.user.dto.UserAddressUpdateRequest;
import com.delivery.userservice.user.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/addresses")
@RequiredArgsConstructor
public class UserAddressController {
    private final UserAddressService userAddressService;

    // 주소 등록
    @PostMapping
    public ResponseEntity<UserAddressResponse> registerAddress(
            @RequestBody UserAddressRequest request) {

        // 토큰에서 이메일 추출
        String email = getEmailFromToken();

        UserAddressResponse response = userAddressService.registerAddress(email, request);
        return ResponseEntity.ok(response);
    }

    // 주소 목록 조회
    @GetMapping
    public ResponseEntity<List<UserAddressResponse>> getUserAddresses() {
        String email = getEmailFromToken();
        List<UserAddressResponse> responses = userAddressService.getUserAddress(email);
        return ResponseEntity.ok(responses);
    }

    // 주소 수정
    @PutMapping("/{addressId}")
    public ResponseEntity<UserAddressResponse> updateAddress(
            @PathVariable Long addressId,
            @RequestBody UserAddressUpdateRequest request){

        String email = getEmailFromToken();
        UserAddressResponse response = userAddressService.updateAddress(email, addressId, request);
        return ResponseEntity.ok(response);
    }

    // 주소 삭제
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long addressId){

        String email = getEmailFromToken();
        userAddressService.deleteAddress(email, addressId);
        return ResponseEntity.noContent().build();
    }

    private String getEmailFromToken(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication.getName() == null){
            throw new IllegalArgumentException("인증 정보가 없습니다.");
        }
        return authentication.getName();
    }
}