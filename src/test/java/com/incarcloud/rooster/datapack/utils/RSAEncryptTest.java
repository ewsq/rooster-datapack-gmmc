package com.incarcloud.rooster.datapack.utils;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import com.github.io.protocol.utils.HexStringUtil;

/**
 * @Title: RSAEncryptTest.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.utils
 * @Description: rsa加解密测试
 * @author: chenz
 * @date: 2018年1月11日 上午11:32:22
 * @version: V1.0
 */
public class RSAEncryptTest {

	/**
	 * rsa非对称加解密
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLoadPublicKeyByStr() throws Exception {
		// 加载密钥
		String privateKeyStr = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAk"
				+ "EAuPA7OwN6dAsRywMRyEjZmwN/81QZVPD37X2gGmHnKvpJduX0YxdPxr"
				+ "2xbosafiZckpYiS4SmLU/omTSX7rx25QIDAQABAkBzURCN1rhnXXOl0+"
				+ "8lpQ9TElaZQqpdBbiCtN+9M6jyaXkiTcV2C7oRiV++rk4dN0zcZ7rQ57"
				+ "Q1F7rYE4yQeK2hAiEA4JZc+4w6J8RFXWOtBSGhFa2REYEIrm6CoxKZQl"
				+ "jdOPkCIQDSzi/USXQX2HN4tVmgpMkfDdX0dwzL9SDUCe8Ur030TQIgN/"
				+ "7FaCJqTYfuE37KoMDlvNXtmYOmencrMyVr+NLXNWECICkVDbUta3aKNF"
				+ "a94O2xr5BhveSant/Qsckg9nrtfx+xAiEA0UzZSScryMou1Br0ljh/K+" + "nHCDYV7S0Xv8hCHcAFpjA=";
		RSAPrivateKey privateKey = RSAEncrypt.loadPrivateKeyByStr(privateKeyStr);
		// 加密报文
		byte[] bytes = RSAEncrypt.encrypt(privateKey, HexStringUtil.parseBytes("383632323334303231303432343730"));
		System.out.println(HexStringUtil.toHexString(bytes));
		// 加载公钥
		String publicKeyStr = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALjwOzsDenQL"
				+ "EcsDEchI2ZsDf/NUGVTw9+19oBph5yr6SXbl9GMXT8a9sW6LGn4mXJKW" + "IkuEpi1P6Jk0l+68duUCAwEAAQ==";
		RSAPublicKey publicKey = RSAEncrypt.loadPublicKeyByStr(publicKeyStr);

		// 解密报文-得到报文解密密钥
		byte[] desc = RSAEncrypt.decrypt(publicKey, bytes);
		System.out.println(Hex.encodeHexString(desc));
	}

	@Test
	public void testGenAES() {
		try {
			KeyGenerator kg = KeyGenerator.getInstance("AES");
			kg.init(128);// 要生成多少位，只需要修改这里即可128, 192或256
			SecretKey sk = kg.generateKey();
			byte[] b = sk.getEncoded();
			String s = Hex.encodeHexString(b);
			System.out.println(s);
			System.out.println("十六进制密钥长度为" + s.length());
			System.out.println("二进制密钥的长度为" + s.length() * 4);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.out.println("没有此算法。");
		}
	}

	/**
	 * aes128对称加解密
	 */
	@Test
	public void testDec() {
		String securityKeyStr = "1234567890";
		byte[] content = RSAEncrypt.AESEncode(securityKeyStr.getBytes(),
				HexStringUtil.parseBytes("383632323334303231303432343730"));
		System.out.println(Hex.encodeHexString(content));

		byte[] dec = RSAEncrypt.AESDecode(securityKeyStr.getBytes(), content);
		System.out.println(Hex.encodeHexString(dec));
	}
}
