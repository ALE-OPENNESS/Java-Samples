/*
* Copyright 2022 ALE International
*
* Permission is hereby granted, free of charge, to any person obtaining a copy of this 
* software and associated documentation files (the "Software"), to deal in the Software 
* without restriction, including without limitation the rights to use, copy, modify, merge, 
* publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons 
* to whom the Software is furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in all copies or 
* substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING 
* BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
* NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
* DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.ale.o2g.sample.routing.util;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 */
public class CipherUtil {

	private final static String KEY = "13459872654241498765243132525525";
	private final static String SALT = "1345987265424149";
	

	private Cipher cipher;
	private SecretKey secretKey;
	private IvParameterSpec IvSpec;
	
	private static CipherUtil _instance = null;
	
	private CipherUtil(String key, String salt) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {

		cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
	    KeySpec spec = new PBEKeySpec(key.toCharArray(), salt.getBytes(), 65536, 256);
		
	    secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
	    IvSpec = new IvParameterSpec(salt.getBytes());
	}
	
	
	public static CipherUtil get() {

		if (_instance == null) {
			try {
				_instance = new CipherUtil(KEY, SALT);				
			} 
			catch (Exception e) {
				return null;
			}
		}
		
		return _instance;		
	}
		
	public String encrypt(String toEncrypt) {
		
		try {
		    cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvSpec);
		    byte[] cipherText = cipher.doFinal(toEncrypt.getBytes());
		    return Base64.getEncoder().encodeToString(cipherText);
		} 
		catch (Exception e) {
			return null;
		}
	}
	
	public String decrypt(String toDecrypt) {

		try {
			cipher.init(Cipher.DECRYPT_MODE, secretKey, IvSpec);
		    byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(toDecrypt));
		    return new String(plainText);
		}
		catch (Exception e) {
			return null;
		}
	}
}
