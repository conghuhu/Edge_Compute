package cn.cislc.dockerservice.param;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;

/**
 * @author conghuhu
 * @create 2022-03-07 17:03
 */
@Data
public class NetWorkParam {

    private String id;

    private String netWorkName;

    private String drive;

    private Boolean checkDuplicate;
}
