package com.hapjusil.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    GUEST("ROLE_GUEST", "일반 유저"),
    USER("ROLE_OWNER", "합주실 사장님");

    private final String key;
    private final String title;
}