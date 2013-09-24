package com.savoirfairelinux.liferay.client;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import com.liferay.portal.kernel.util.Validator;

public class Configuration
{
	private static Configuration instance;

	private final Properties properties = new Properties();

	private Configuration()
	{
		try
		{
			InputStream stream = getClass().getResourceAsStream("/configuration.properties");
			if (stream == null)
				throw new FileNotFoundException("Cannot load resource configuration.properties");

			properties.load(stream);
		}
		catch (Exception e)
		{
			System.err.println("Error loading configuration: " + e.getMessage());
		}
	}

	public synchronized static Configuration getInstance()
	{
		if (instance == null)
			instance = new Configuration();
		return instance;
	}
	
	public String[] getCategories()
	{
		String categoriesStr = properties.getProperty("categories", "");
		if (Validator.isNotNull(categoriesStr))
			return categoriesStr.split(",");
		else
			return null;
	}

	public String[] getTemplates()
	{
		String templatesStr = properties.getProperty("templates", "");
		if (Validator.isNotNull(templatesStr))
			return templatesStr.split(",");
		else
			return null;
	}

	public String[] getStructures()
	{
		String structuresStr = properties.getProperty("structures", "");
		if (Validator.isNotNull(structuresStr))
			return structuresStr.split(",");
		else
			return null;
	}

	public String getServerURL()
	{
		return properties.getProperty("server.url", "");
	}

	public String getServerPort()
	{
		return properties.getProperty("server.port", "");
	}

	public String getUsername()
	{
		return properties.getProperty("username", "");
	}

	public String getPassword()
	{
		return properties.getProperty("password", "");
	}

	public long[] getGroupIds()
	{
		String[] ids = properties.getProperty("group.ids", "").split(",");
		long[] idsLong = new long[ids.length];
		for (int i = 0; i < ids.length; i++)
		{
			idsLong[i] = Long.parseLong(ids[i]);
		}
		return idsLong;
	}

	public long getCompanyId()
	{
		return Long.parseLong(properties.getProperty("company.id", ""));
	}

	public boolean getTemplateCache()
	{
		return Boolean.parseBoolean(properties.getProperty("template.cache", ""));
	}
}
