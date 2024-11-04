package com.jcondotta.helper;

import java.util.UUID;

public enum TestAccountHolder {

    JEFFERSON(UUID.fromString("0192ed2d-263a-7303-88dc-c3cadbfef622"));

    private final UUID accountHolderId;

    TestAccountHolder(UUID accountHolderId) {
        this.accountHolderId = accountHolderId;
    }

    public UUID getAccountHolderId() {
        return accountHolderId;
    }
}