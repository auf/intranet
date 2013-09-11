package com.savoirfairelinux.liferay.client;

import java.util.HashMap;
import java.util.Map;

import com.liferay.client.soap.portal.service.ServiceContext;
import com.liferay.client.soap.portlet.asset.model.AssetCategorySoap;
import com.liferay.client.soap.portlet.asset.model.AssetVocabularySoap;
import com.liferay.client.soap.portlet.asset.service.http.Portlet_Asset_AssetCategoryServiceSoapBindingStub;
import com.liferay.client.soap.portlet.asset.service.http.Portlet_Asset_AssetVocabularyServiceSoapBindingStub;

public class UpdateCategories
{
	public static void main(String[] argv) throws Exception
	{
		Portlet_Asset_AssetVocabularyServiceSoapBindingStub vocabularyClient = ClientFactory.getInstance().getVocabularyClient(null);
		Portlet_Asset_AssetCategoryServiceSoapBindingStub categoryClient = ClientFactory.getInstance().getCategoryClient(null);

		ServiceContext ctx = new ServiceContext();
		ctx.setCompanyId(Configuration.getInstance().getCompanyId());

		for (long groupId : Configuration.getInstance().getGroupIds())
		{
			AssetVocabularySoap[] vocabularies = vocabularyClient.getGroupVocabularies(groupId);

			for (AssetVocabularySoap vocabulary : vocabularies)
			{
				System.out.println(vocabulary.getName());
				if (vocabulary.getName().equals("Nouvelles"))
				{
					Map<String, AssetCategorySoap> categoriesMap = new HashMap<String, AssetCategorySoap>();
					AssetCategorySoap[] categories = categoryClient.getVocabularyCategories(vocabulary.getVocabularyId(), -1, -1, null);

					for (AssetCategorySoap category : categories)
					{
						//String title = LocalizationUtil.getLocalization(category.getTitle(), LocaleUtil.toLanguageId(LocaleUtil.getDefault()));
						categoriesMap.put(category.getName(), category);
					}

					String name = "";
					if (categoriesMap.containsKey(name))
					{
						AssetCategorySoap category = categoriesMap.get(name);
						//Map<String>
					}

					//categoryClient.
				}
			}
		}
	}
}
