package cn.cislc.authservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.PrivateKey;
import java.security.PublicKey;

@SpringBootTest
class AuthServiceApplicationTests {

    private static final String pubKeyPath = "E:\\桌面\\edge_of_computing\\cloud-edgeCompute\\auth-service\\src\\main\\resources\\static\\/ras.pub";

    private static final String priKeyPath = "E:\\桌面\\edge_of_computing\\cloud-edgeCompute\\auth-service\\src\\main\\resources\\static\\ras.pri";

    @Test
    public void testRas() throws Exception {
        //生产公钥和私钥
//        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    @Test
    public void testGetRas() throws Exception {
        //获得公钥和私钥
//        PublicKey publicKey = RsaUtils.getPublicKey(pubKeyPath);
//        PrivateKey privateKey = RsaUtils.getPrivateKey(priKeyPath);
//        System.out.println(publicKey.toString());
//        System.out.println(privateKey.toString());

    }
}
