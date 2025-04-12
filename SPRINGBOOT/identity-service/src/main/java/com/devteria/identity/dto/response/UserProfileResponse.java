package com.devteria.identity.dto.response;

import java.time.LocalDate;

import com.devteria.identity.constant.Roles;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse {
    String id;
    String firstName;
    String lastName;
    LocalDate birthDate;
    String city;
    Roles role;
}
