/*
 * Copyright parcIT GmbH
 * 
 * Dieser Source-Code steht unter dem alleinigen Urheberschutz der parcIT GmbH.
 * Die Nutzung und Weitergabe ist nur mit ausdr�cklicher Erlaubnis der parcIT GmbH gestattet.
 * 
 * Created by: eb
 */
package com.eb.base.crypt.algorithmes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;

import com.eb.base.Logger;

public class Twofish_Crypter
	extends AbstractCrypter {

	private Object getTwofishKey(String str)
	{		
		byte[] keyBytes = getSha256BitKeyBytes(str);

		Object key = null;
		try {
			key = Twofish_Algorithm.makeKey(keyBytes);
		}
		catch (InvalidKeyException e) {
			key = null;
			
		}
		return key;
	}

	public byte[] encryptBytes(byte[] data, String keys)
	{

		int i;

		int len = data.length;
		int divider = len / 16;
		if (len % 16 == 0)
			divider++;

		int newLen = divider * 16;

		Object key = getTwofishKey(keys);

		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream(newLen);

			for (i = 0; i + 16 <= data.length; i += 16)
			{
				out.write(Twofish_Algorithm.blockEncrypt(data, i, key));
			}
			byte[] buffer = new byte[16];

			for (int j = 0; j < 16; j++, i++)
			{
				buffer[j] = i < data.length ? data[i] : (i == data.length ? (byte) 13 : 0);
			}
			out.write(Twofish_Algorithm.blockEncrypt(buffer, 0, key));

			return out.toByteArray();
		}
		catch (IOException e) {
			Logger.logError(e);
		}
		return null;

	}

	public byte[] decrypt_bytes(byte[] data, String keys)
	{

		int i;
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream(data.length);
			Object key = getTwofishKey(keys);

			for (i = 0; i + 16 < data.length; i += 16)
			{
				out.write(Twofish_Algorithm.blockDecrypt(data, i, key));
			}

			byte[] outdata = Twofish_Algorithm.blockDecrypt(data, i, key);

			int j;
			//noinspection StatementWithEmptyBody
			int n = outdata.length;
			for (j = 15; j > 0 && outdata[j] != (byte) 13; j--);
			if (j>0)
				out.write(outdata, 0, j);
			out.close();
			return out.toByteArray();
		}
		catch (IOException e) {

			Logger.log(e);
		}
		return null;

	}
}
