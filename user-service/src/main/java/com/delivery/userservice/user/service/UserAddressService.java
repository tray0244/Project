package com.delivery.userservice.user.service;

import com.delivery.userservice.user.domain.User;
import com.delivery.userservice.user.domain.UserAddress;
import com.delivery.userservice.user.dto.UserAddressRequest;
import com.delivery.userservice.user.dto.UserAddressResponse;
import com.delivery.userservice.user.dto.UserAddressUpdateRequest;
import com.delivery.userservice.user.repository.UserAddressRepository;
import com.delivery.userservice.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAddressService {
    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;

    // 주소 등록
    @Transactional
    public UserAddressResponse registerAddress(String email, UserAddressRequest request){
        // 유저 찾기
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 주소 생성 및 저장
        UserAddress address = new UserAddress(
                user,
                request.getAddressName(),
                request.getCity(),
                request.getStreet(),
                request.getZipcode(),
                request.getDetailAddress()
        );
        userAddressRepository.save(address);

        // 응답 반환
        return UserAddressResponse.from(address);
    }

    // 주소 목록 조회
    @Transactional(readOnly = true)
    public List<UserAddressResponse> getUserAddress(String email){
        // 유저가 존재하는지 확인
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new  IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<UserAddressResponse> responsesList = new ArrayList<>();

        for(UserAddress address : user.getUserAddresses()){
            responsesList.add(UserAddressResponse.from(address));
        }

        return responsesList;
    }

    // 주소 수정
    @Transactional
    public UserAddressResponse updateAddress(String email, Long addressId, UserAddressUpdateRequest request){
        // 수정할 주소 찾기
        UserAddress address = userAddressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("주소를 찾을 수 없습니다."));

        // 주소 소유자 검증
        if(!address.getUser().getEmail().equals(email)){
            throw new IllegalArgumentException("해당 주소를 수정할 권한이 없습니다.");
        }

        // Entity 내부 메서드를 통해 수정
        address.updateAddress(
                request.getAddressName(),
                request.getCity(),
                request.getStreet(),
                request.getZipcode(),
                request.getDetailAddress()
        );
        return UserAddressResponse.from(address);
    }

    // 주소 삭제
    @Transactional
    public void deleteAddress(String email, Long addressId){
        // 삭제할 주소 찾기
        UserAddress address = userAddressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("주소를 찾을 수 없습니다."));

        // 주소 소유자 검증
        if(!address.getUser().getEmail().equals(email)){
            throw new IllegalArgumentException("해당 주소를 삭제할 권한이 없습니다.");
        }

        // 삭제
        userAddressRepository.delete(address);
    }
}
