package com.savoirfairelinux.liferay.client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
	
	public static List<String> readList(String name) throws IOException
	{
		InputStream stream = Util.class.getResourceAsStream(name);
		if (stream == null)
			throw new FileNotFoundException("Cannot load resource " + name);

		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		List<String> sb = new ArrayList<String>();

		String line = null;
		while ((line = reader.readLine()) != null)
		{
			sb.add(line);
		}
		stream.close();

		return sb;
	}
}
