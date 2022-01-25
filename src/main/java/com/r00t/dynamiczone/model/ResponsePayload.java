package com.r00t.dynamiczone.model;

import com.r00t.dynamiczone.util.Zoned;
import lombok.Data;

import java.util.Date;

@Data
public class ResponsePayload {

    private long originalStartTimestamp;

    private Date originalBirthDate;

    @Zoned
    private long translatedStartTimestamp;

    @Zoned
    private Date translatedBirthDate;

}
