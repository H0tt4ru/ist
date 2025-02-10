package com.example.service_user.response;

import java.util.List;

import com.example.base_domain.response.BaseResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ListUserResponse extends BaseResponse {

    private List<UsersResponse> users;
}
