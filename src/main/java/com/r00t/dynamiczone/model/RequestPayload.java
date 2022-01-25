package com.r00t.dynamiczone.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.r00t.dynamiczone.util.Zoned;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RequestPayload extends BasePayload {

    @Zoned
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date birthDate;

    private RequestPayload requestPayload;

}
