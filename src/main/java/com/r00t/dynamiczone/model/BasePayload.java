package com.r00t.dynamiczone.model;

import com.r00t.dynamiczone.util.Zoned;
import lombok.Data;

@Data
public class BasePayload {

    private String table;

    @Zoned
    private long startTimestamp;

}
