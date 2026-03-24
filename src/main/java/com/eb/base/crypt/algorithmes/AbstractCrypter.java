package com.eb.base.crypt.algorithmes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


public abstract class AbstractCrypter
{

	abstract public byte[] encryptBytes(byte[] newBytes, String newKeys);

	abstract public byte[] decrypt_bytes(byte[] newResult, String newKeys);
	
	public String loadEncryptedFile(String fileName, String password) throws IOException {
		byte[] data = loadFile(fileName);
		if (data==null)
			return null;
		byte[] decrypted = decrypt_bytes(data, password);
		return new String(decrypted);
	}
	
	public void saveEncryptedFile(String fileName, String password, String text)
	{
		byte[] data = encryptBytes(text.getBytes(), password);


		saveFile(fileName, data);

        try {
            String text2 = loadEncryptedFile(fileName, password);
			if (!text2.equals(text))
				throw new RuntimeException("The encrypted text does not match the decrypted text");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


	public byte[] getSha256BitKeyBytes(String str)
	{

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			// md.update(str.getBytes(Charset.forName("windows-1252")));
			md.update(str.getBytes(StandardCharsets.UTF_8));
			byte[] digest = md.digest();
			return digest;
		}
		catch (NoSuchAlgorithmException e) {
			com.eb.base.Logger.logError(e);
			}
		return null;

	}

	byte[] loadFile(String fileName) throws IOException {
		return Files.readAllBytes(Paths.get(fileName));
	}

	void saveFile(String fileName, byte[] data)
	{

		File outputFile = new File(fileName);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(outputFile);
			fos.write(data);
			fos.close();
		}
		catch (IOException e) {
			e.toString();
			
		}

	}

	public String encryptString(String string, String keys)
	{

		byte[] result = encryptBytes(string.getBytes(), keys);
		return new String(Base64.getEncoder().encode(result));
	}

	public String decryptString(String data, String keys)
	{

		byte[] result = Base64.getDecoder().decode(data);
		byte[] result2 = decrypt_bytes(result, keys);
		return new String(result2);
	}

	public void encryptFile(String fileName, String outfileName, String keys) throws IOException {

		byte[] data = loadFile(fileName);
		byte[] result = encryptBytes(data, keys);
		saveFile(outfileName, result);
	}

	public void decryptFile(String fileName, String outfileName, String keys) throws IOException {

		byte[] data = loadFile(fileName);
		byte[] result = decrypt_bytes(data, keys);
		saveFile(outfileName, result);
	}

}
