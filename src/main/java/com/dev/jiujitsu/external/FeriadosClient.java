package com.dev.jiujitsu.external;

import com.dev.jiujitsu.constants.APIConstants;
import com.dev.jiujitsu.domain.vo.FeriadosResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name= APIConstants.FERIADOS_CLIENT_NAME, url = "${url}")
public interface FeriadosClient {
    @GetMapping("/feriados/v1/{ano}")
    List<FeriadosResponse> getFeriados(@PathVariable int ano);
}
