package com.bist.api.util;


import com.bist.api.rest.dto.ShareGenericModel;

import java.util.List;

public class UtilHelper {

    public static List<ShareGenericModel> getBistList(){
        return List.of(getBist(),getBist2());
    }

    public static ShareGenericModel getBist() {
        return ShareGenericModel.builder()
                .name("BIST100")
                .value("100.0")
                .dailyVolume("100.0")
                .dailyChangePercentage("100.0")
                .description("BIST100")
                .poster("BIST100")
                .build();
    }

    public static ShareGenericModel getBist2() {
        return ShareGenericModel.builder()
                .name("BIST200")
                .value("200.0")
                .dailyVolume("200.0")
                .dailyChangePercentage("200.0")
                .description("BIST200")
                .poster("BIST200")
                .build();
    }
}
