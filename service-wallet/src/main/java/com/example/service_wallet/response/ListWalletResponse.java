package com.example.service_wallet.response;

import java.util.List;

import com.example.base_domain.response.BaseResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ListWalletResponse extends BaseResponse {

    private List<WalletsResponse> wallets;
}
