package com.upgrade.campsite.service;

import com.upgrade.campsite.Constant;
import com.upgrade.campsite.dto.ReservationDto;
import com.upgrade.campsite.entity.User;
import com.upgrade.campsite.exception.ApiErrorException;
import com.upgrade.campsite.repository.UserRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUser(ReservationDto dto) throws ApiErrorException {

        if (Strings.isEmpty(dto.getUserEmailAddress())) {
            throw new ApiErrorException(Constant.USER_DATA_REQUIRED_ERROR_CODE, Constant.USER_DATA_REQUIRED_ERROR_MSG);
        }

        User user = userRepository.findByEmail(dto.getUserEmailAddress());
        if (Objects.isNull(user)) {
            user = createUser(dto);
        }

        return user;
    }

    private User createUser(ReservationDto dto) throws ApiErrorException {

        if (dto.getUserEmailAddress().isEmpty() || dto.getUserFullName().isEmpty()) {
            throw new ApiErrorException(Constant.USER_DATA_REQUIRED_ERROR_CODE, Constant.USER_DATA_REQUIRED_ERROR_MSG);
        }

        User user = new User(dto.getUserEmailAddress(), dto.getUserFullName());
        return userRepository.save(user);
    }

}
