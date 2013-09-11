package com.savoirfairelinux.liferay.client;

import java.io.FileNotFoundException;
import java.util.Arrays;

import com.liferay.client.soap.portal.service.ServiceContext;
import com.liferay.client.soap.portlet.journal.model.JournalStructureSoap;
import com.liferay.client.soap.portlet.journal.service.http.Portlet_Journal_JournalStructureServiceSoapBindingStub;
import com.liferay.portal.kernel.util.Validator;

public class UpdateStructures
{
	private static void updateOrCreateStructure(Portlet_Journal_JournalStructureServiceSoapBindingStub client, long groupId, String structureId,
			String structureName, String structureDescription, String structureContent) throws Exception
	{
		try
		{
			ServiceContext ctx = new ServiceContext();
			ctx.setCompanyId(Configuration.getInstance().getCompanyId());

			// check if exists
			JournalStructureSoap structure = null;
			try
			{
				structure = client.getStructure(groupId, structureId);
			}
			catch (Exception e)
			{
				// ignore journal structure not found
				if (e.getMessage().indexOf("No JournalStructure exists with the key") < 0)
					throw e;
			}
			if (structure != null)
			{
				client.updateStructure(
						groupId,
						structureId,
						null,
						Arrays.asList("fr_CA").toArray(new String[1]),
						Arrays.asList(structureName).toArray(new String[1]),
						Arrays.asList("fr_CA").toArray(new String[1]),
						Arrays.asList(structureDescription).toArray(new String[1]), structureContent, ctx);
				System.out.println("Structure '" + structureId + "' successfully updated for organization '" + groupId + "'");
			}
			else
			{
				client.addStructure(
						groupId,
						structureId,
						false,
						null,
						Arrays.asList("fr_CA").toArray(new String[1]),
						Arrays.asList(structureName).toArray(new String[1]),
						Arrays.asList("fr_CA").toArray(new String[1]),
						Arrays.asList(structureDescription).toArray(new String[1]), structureContent, ctx);
				System.out.println("Structure '" + structureId + "' successfully added for organization '" + groupId + "'");
			}
		}
		catch (Exception e)
		{
			System.err.println("Structure '" + structureId + "' failed update for organization '" + groupId + "': " + e.getMessage());
			throw e;
		}
	}

	public static void main(String[] argv) throws Exception
	{
		Portlet_Journal_JournalStructureServiceSoapBindingStub client = ClientFactory.getInstance().getStructureClient(null);

		for (long groupId : Configuration.getInstance().getGroupIds())
		{
			Structure[] structures;
			String[] structuresStr = Configuration.getInstance().getStructures();
			if (Validator.isNotNull(structuresStr))
			{
				structures = new Structure[structuresStr.length];
				for (int i = 0; i < structuresStr.length; i++)
				{
					structures[i] = Structure.valueOf(structuresStr[i].toUpperCase());
				}
			}
			else
				structures = Structure.values();

			for (Structure structure : structures)
			{
				String content = null;
				try
				{
					content = Util.readFile("/structure/" + groupId + "/" + structure.getId().toLowerCase() + ".xml");
				}
				catch (FileNotFoundException fnfe)
				{
					try
					{
						content = Util.readFile("/structure/" + structure.getId().toLowerCase() + ".xml");
					}
					catch (FileNotFoundException fnfe2)
					{
						System.err.println("File " + structure.getId().toLowerCase() + " not found");
						continue;
					}
				}

				try {
					updateOrCreateStructure(client, groupId, structure.getId(), structure.getName(), structure.getDescription(), content);
					System.out.println("structure " +  structure.getName() + " updated");
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
	}
}
