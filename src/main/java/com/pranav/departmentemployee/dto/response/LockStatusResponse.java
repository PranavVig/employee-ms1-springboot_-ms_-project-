package com.pranav.departmentemployee.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LockStatusResponse {

    private boolean locked;

    private long remainingSeconds;

}