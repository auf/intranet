package com.savoirfairelinux.liferay.client;

import java.io.FileNotFoundException;
import java.util.Arrays;

import com.liferay.client.soap.portal.service.ServiceContext;
import com.liferay.client.soap.portlet.journal.model.JournalTemplateSoap;
import com.liferay.client.soap.portlet.journal.service.http.Portlet_Journal_JournalTemplateServiceSoapBindingStub;
import com.liferay.portal.kernel.util.Validator;

public class UpdateTemplates
{
	private static void updateOrCreateTemplate(Portlet_Journal_JournalTemplateServiceSoapBindingStub client, long orgId, String templateId,
			String structureId, String templateName, String templateDescription, String templateContent, boolean cacheable)
	{
		try
		{
			ServiceContext ctx = new ServiceContext();
			ctx.setCompanyId(Configuration.getInstance().getCompanyId());

			// check if exists
			JournalTemplateSoap template = null;
			try
			{
				template = client.getTemplate(orgId, templateId);
			}
			catch (Exception e)
			{
				// ignore journal structure not found
				if (e.getMessage().indexOf("No JournalTemplate exists with the key") < 0)
					throw e;
			}
			if (template != null)
			{
				client.updateTemplate(
						orgId,
						templateId,
						structureId,
						Arrays.asList("fr_CA").toArray(new String[1]),
						Arrays.asList(templateName).toArray(new String[1]),
						Arrays.asList("fr_CA").toArray(new String[1]),
						Arrays.asList(templateDescription).toArray(new String[1]), templateContent, true, "vm",
						cacheable, ctx);
				System.out.println("Template '" + templateId + "' successfully updated for organization '" + orgId + "'");
			}
			else
			{
				client.addTemplate(
						orgId,
						templateId,
						false,
						structureId,
						Arrays.asList("fr_CA").toArray(new String[1]),
						Arrays.asList(templateName).toArray(new String[1]),
						Arrays.asList("fr_CA").toArray(new String[1]),
						Arrays.asList(templateDescription).toArray(new String[1]), templateContent, true, "vm",
						cacheable, ctx);
				System.out.println("Template '" + templateId + "' successfully added for organization '" + orgId + "'");
			}
		}
		catch (Exception e)
		{
			System.err.println("Template '" + templateId + "' failed creation/update for organization '" + orgId + "': " + e.getMessage());
		}
	}

	public static void main(String[] argv) throws Exception
	{
		Portlet_Journal_JournalTemplateServiceSoapBindingStub client = ClientFactory.getInstance().getTemplateClient(null);
		boolean templateCache = Configuration.getInstance().getTemplateCache();

		for (long groupId : Configuration.getInstance().getGroupIds())
		{

			Template[] templates;
			String[] templatesStr = Configuration.getInstance().getTemplates();
			if (Validator.isNotNull(templatesStr))
			{
				templates = new Template[templatesStr.length];
				for (int i = 0; i < templatesStr.length; i++)
				{
					templates[i] = Template.valueOf(templatesStr[i].toUpperCase());
				}
			}
			else
				templates = Template.values();

			for (Template template : templates)
			{
				String content = null;
				try
				{
					content = Util.readFile("/template/" + groupId + "/" + template.getId().toLowerCase() + ".vm");
				}
				catch (FileNotFoundException fnfe)
				{
					try
					{
						content = Util.readFile("/template/" + template.getId().toLowerCase() + ".vm");
					}
					catch (FileNotFoundException fnfe2)
					{
						continue;
					}
				}
				
				updateOrCreateTemplate(client, groupId, template.getId(), template.getStructureId(), template.getName(), template.getDescription(),
						content, templateCache);
				System.out.println("structure " +  template.getName() + " updated");
			}
		}
	}
}
