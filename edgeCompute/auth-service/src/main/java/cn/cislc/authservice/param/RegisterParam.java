package cn.cislc.authservice.param;

import lombok.Data;

/**
 * @author conghuhu
 * @create 2022-03-10 15:43
 */
@Data
public class RegisterParam {

    private String username;

    private String password;

    private String email;

}
