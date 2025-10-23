// ...existing code...
package com.broker.inghub.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private String role;
    private String customerId;
}
// ...existing code...

