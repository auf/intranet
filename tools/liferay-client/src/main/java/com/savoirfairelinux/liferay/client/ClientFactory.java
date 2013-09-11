package com.savoirfairelinux.liferay.client;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Stub;

import com.liferay.client.soap.portal.model.UserSoap;
import com.liferay.client.soap.portal.service.http.GroupServiceSoapServiceLocator;
import com.liferay.client.soap.portal.service.http.OrganizationServiceSoapServiceLocator;
import com.liferay.client.soap.portal.service.http.Portal_GroupServiceSoapBindingStub;
import com.liferay.client.soap.portal.service.http.Portal_OrganizationServiceSoapBindingStub;
import com.liferay.client.soap.portal.service.http.Portal_RoleServiceSoapBindingStub;
import com.liferay.client.soap.portal.service.http.Portal_UserServiceSoapBindingStub;
import com.liferay.client.soap.portal.service.http.RoleServiceSoapServiceLocator;
import com.liferay.client.soap.portal.service.http.UserServiceSoapServiceLocator;
import com.liferay.client.soap.portlet.asset.service.http.AssetCategoryServiceSoapServiceLocator;
import com.liferay.client.soap.portlet.asset.service.http.AssetVocabularyServiceSoapServiceLocator;
import com.liferay.client.soap.portlet.asset.service.http.Portlet_Asset_AssetCategoryServiceSoapBindingStub;
import com.liferay.client.soap.portlet.asset.service.http.Portlet_Asset_AssetVocabularyServiceSoapBindingStub;
import com.liferay.client.soap.portlet.blogs.service.http.BlogsEntryServiceSoapServiceLocator;
import com.liferay.client.soap.portlet.blogs.service.http.Portlet_Blogs_BlogsEntryServiceSoapBindingStub;
import com.liferay.client.soap.portlet.journal.service.http.JournalArticleServiceSoapServiceLocator;
import com.liferay.client.soap.portlet.journal.service.http.JournalStructureServiceSoapServiceLocator;
import com.liferay.client.soap.portlet.journal.service.http.JournalTemplateServiceSoapServiceLocator;
import com.liferay.client.soap.portlet.journal.service.http.Portlet_Journal_JournalArticleServiceSoapBindingStub;
import com.liferay.client.soap.portlet.journal.service.http.Portlet_Journal_JournalStructureServiceSoapBindingStub;
import com.liferay.client.soap.portlet.journal.service.http.Portlet_Journal_JournalTemplateServiceSoapBindingStub;

public class ClientFactory
{
	private static ClientFactory instance;

	private String serverUrl;
	private String serverport;
	private String username;
	private String password;

	private ClientFactory()
	{
		// Read properties file.
		Configuration cfg = Configuration.getInstance();
		serverUrl = cfg.getServerURL();
		serverport = cfg.getServerPort();
		username = cfg.getUsername();
		password = cfg.getPassword();

		System.out.println("============================");
		System.out.println("CONFIGURATION (DEFAULT):");
		System.out.println("- server URL: " + serverUrl);
		System.out.println("- server port: " + serverport);
		System.out.println("- username: " + username);
		System.out.println("- password: " + "(secret)");
		System.out.println("============================");
	}
	
	public void changeServerPort(String serverUrl, String serverport)
	{
		this.serverUrl = serverUrl;
		this.serverport = serverport;
	}

	public synchronized static ClientFactory getInstance()
	{
		if (instance == null)
			instance = new ClientFactory();
		return instance;
	}

	public String getUserEmail() {
		return this.username;
	}
	
	private void setUser(Stub port, UserSoap user)
	{
		if (user != null)
		{
			port.setUsername(user.getEmailAddress());
			port.setPassword(user.getPassword());
		}
		else
		{
			port.setUsername(username);
			port.setPassword(password);
		}
	}

	private String getBaseURL()
	{
		String url = serverUrl;
		if (serverport != null && !"".equals(serverport))
			url += ":" + serverport;
		return url;
	}

	public Portlet_Journal_JournalArticleServiceSoapBindingStub getArticleClient(UserSoap user) throws MalformedURLException, ServiceException
	{
		String url = getBaseURL() + "/api/secure/axis/Portlet_Journal_JournalArticleService";

		Portlet_Journal_JournalArticleServiceSoapBindingStub port = (Portlet_Journal_JournalArticleServiceSoapBindingStub) new JournalArticleServiceSoapServiceLocator()
				.getPortlet_Journal_JournalArticleService(new URL(url));

		setUser(port, user);

		return port;
	}

	public Portlet_Journal_JournalStructureServiceSoapBindingStub getStructureClient(UserSoap user) throws MalformedURLException, ServiceException
	{

		String url = getBaseURL() + "/api/secure/axis/Portlet_Journal_JournalStructureService";

		Portlet_Journal_JournalStructureServiceSoapBindingStub port = (Portlet_Journal_JournalStructureServiceSoapBindingStub) new JournalStructureServiceSoapServiceLocator()
				.getPortlet_Journal_JournalStructureService(new URL(url));

		setUser(port, user);

		return port;
	}

	public Portlet_Journal_JournalTemplateServiceSoapBindingStub getTemplateClient(UserSoap user) throws MalformedURLException, ServiceException
	{

		String url = getBaseURL() + "/api/secure/axis/Portlet_Journal_JournalTemplateService";

		Portlet_Journal_JournalTemplateServiceSoapBindingStub port = (Portlet_Journal_JournalTemplateServiceSoapBindingStub) new JournalTemplateServiceSoapServiceLocator()
				.getPortlet_Journal_JournalTemplateService(new URL(url));

		setUser(port, user);

		return port;
	}

	public Portlet_Blogs_BlogsEntryServiceSoapBindingStub getBlogClient(UserSoap user) throws MalformedURLException, ServiceException
	{

		String url = getBaseURL() + "/api/secure/axis/Portlet_Blogs_BlogsEntryService";

		Portlet_Blogs_BlogsEntryServiceSoapBindingStub port = (Portlet_Blogs_BlogsEntryServiceSoapBindingStub) new BlogsEntryServiceSoapServiceLocator()
				.getPortlet_Blogs_BlogsEntryService(new URL(url));

		setUser(port, user);

		return port;
	}

	public Portal_UserServiceSoapBindingStub getUserClient(UserSoap user) throws MalformedURLException, ServiceException
	{
		String url = getBaseURL() + "/api/secure/axis/Portal_UserService";

		Portal_UserServiceSoapBindingStub port = (Portal_UserServiceSoapBindingStub) new UserServiceSoapServiceLocator()
				.getPortal_UserService(new URL(url));

		setUser(port, user);

		return port;
	}

	public Portlet_Asset_AssetVocabularyServiceSoapBindingStub getVocabularyClient(UserSoap user) throws MalformedURLException, ServiceException
	{

		String url = getBaseURL() + "/api/secure/axis/Portlet_Asset_AssetVocabularyService";

		Portlet_Asset_AssetVocabularyServiceSoapBindingStub port = (Portlet_Asset_AssetVocabularyServiceSoapBindingStub) new AssetVocabularyServiceSoapServiceLocator()
				.getPortlet_Asset_AssetVocabularyService(new URL(url));

		setUser(port, user);

		return port;
	}

	public Portlet_Asset_AssetCategoryServiceSoapBindingStub getCategoryClient(UserSoap user) throws MalformedURLException, ServiceException
	{
		String url = getBaseURL() + "/api/secure/axis/Portlet_Asset_AssetCategoryService";

		Portlet_Asset_AssetCategoryServiceSoapBindingStub port = (Portlet_Asset_AssetCategoryServiceSoapBindingStub) new AssetCategoryServiceSoapServiceLocator()
				.getPortlet_Asset_AssetCategoryService(new URL(url));

		setUser(port, user);

		return port;
	}
	
	public Portal_GroupServiceSoapBindingStub getGroupClient(UserSoap user) throws MalformedURLException, ServiceException
	{

		String url = getBaseURL() + "/api/secure/axis/Portal_GroupService";

		Portal_GroupServiceSoapBindingStub port = (Portal_GroupServiceSoapBindingStub) new GroupServiceSoapServiceLocator()
				.getPortal_GroupService(new URL(url));

		setUser(port, user);

		return port;
	}
	
	public Portal_RoleServiceSoapBindingStub getRoleClient(UserSoap user) throws MalformedURLException, ServiceException
	{

		String url = getBaseURL() + "/api/secure/axis/Portal_RoleService";

		Portal_RoleServiceSoapBindingStub port = (Portal_RoleServiceSoapBindingStub) new RoleServiceSoapServiceLocator()
				.getPortal_RoleService(new URL(url));

		setUser(port, user);

		return port;
	}
	
	public Portal_OrganizationServiceSoapBindingStub getOrganizationClient(UserSoap user) throws MalformedURLException, ServiceException
	{

		String url = getBaseURL() + "/api/secure/axis/Portal_OrganizationService";

		Portal_OrganizationServiceSoapBindingStub port = (Portal_OrganizationServiceSoapBindingStub) new OrganizationServiceSoapServiceLocator()
				.getPortal_OrganizationService(new URL(url));

		setUser(port, user);

		return port;
	}
	
}
