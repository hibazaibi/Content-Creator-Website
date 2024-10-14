package com.app.cc.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Role {

    @JsonProperty("ADMIN")
    ADMIN,

    @JsonProperty("CREATOR")
    CREATOR,

    @JsonProperty("CLIENT")
    CLIENT
  }
