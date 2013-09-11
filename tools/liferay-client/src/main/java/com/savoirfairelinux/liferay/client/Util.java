package com.savoirfairelinux.liferay.client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Util
{
	public static String readFile(String name) throws IOException
	{
		InputStream stream = Util.class.getResourceAsStream(name);
		if (stream == null)
			throw new FileNotFoundException("Cannot load resource " + name);

		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		StringBuilder sb = new StringBuilder();

		String line = null;
		while ((line = reader.readLine()) != null)
		{
			sb.append(line + "\n");
		}
		stream.close();

		return sb.toString();
	}
}
