package com.savoirfairelinux.liferay.client;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import com.liferay.client.soap.portal.service.ServiceContext;
import com.liferay.client.soap.portlet.asset.model.AssetCategorySoap;
import com.liferay.client.soap.portlet.asset.model.AssetVocabularySoap;
import com.liferay.client.soap.portlet.asset.service.http.Portlet_Asset_AssetCategoryServiceSoapBindingStub;
import com.liferay.client.soap.portlet.asset.service.http.Portlet_Asset_AssetVocabularyServiceSoapBindingStub;
import com.liferay.portal.kernel.util.LocaleUtil;

public class UpdateCategories
{
	public static void main(String[] argv) throws Exception
	{
		Portlet_Asset_AssetVocabularyServiceSoapBindingStub vocabularyClient = ClientFactory.getInstance().getVocabularyClient(null);
		Portlet_Asset_AssetCategoryServiceSoapBindingStub categoryClient = ClientFactory.getInstance().getCategoryClient(null);

		ServiceContext ctx = new ServiceContext();
		ctx.setCompanyId(Configuration.getInstance().getCompanyId());

		for (long groupId : Configuration.getInstance().getGroupIds()) {
			ctx.setScopeGroupId(groupId);
			AssetVocabularySoap[] vocabularies = vocabularyClient.getGroupVocabularies(groupId);
			
			String[] vocabulariesNameList = Configuration.getInstance().getCategories();

			for (String vocabularyName : vocabulariesNameList) {
				List<String> content = null;
				try
				{
					content = Util.readList("/category/" + groupId + "/" + vocabularyName + ".txt");
				}
				catch (FileNotFoundException fnfe)
				{
					try
					{
						content = Util.readList("/category/" + vocabularyName + ".txt");
					}
					catch (FileNotFoundException fnfe2)
					{
						continue;
					}
				}
				
				AssetVocabularySoap vocabulary = null;
				//in case there already exists a vocabulary
				for (AssetVocabularySoap v : vocabularies) {

					if (v.getName().equals(vocabularyName)) {
						System.out.println("EXISTING VOCABULARY FOUND:" + v.getName());
						vocabulary = v;
					}
				}
				
				//if no vocabulary was found, create it
				if (vocabulary == null) {
					System.out.println("CREATING NEW VOCABULARY:" + vocabularyName);
					vocabulary = vocabularyClient.addVocabulary(
							Arrays.asList("fr_CA").toArray(new String[1]),
							Arrays.asList(vocabularyName).toArray(new String[1]),
							new String[]{LocaleUtil.getDefault().getLanguage()}, 
							new String[]{""}, 
							"",
							ctx);
				}
				
				for (String categoryName : content) {
					//THIS HAS TO BE USED
					//the strings cannot be compared directly as there seems to be a different way the indexes in the DB store the name
					//ignoring capital/small letters and accents
					AssetCategorySoap[] existingCategories = categoryClient.getVocabularyCategories(groupId, categoryName, vocabulary.getVocabularyId(), -1, -1, null);
					AssetCategorySoap category = null;
					
					if (existingCategories.length > 0) {
						category = existingCategories[0];
					}
					
					if (category == null) {
						System.out.println("ADDING CATEGORY:" + categoryName);
						categoryClient.addCategory(
								0, 
								Arrays.asList("fr_CA").toArray(new String[1]),
								Arrays.asList(categoryName).toArray(new String[1]),
								new String[]{LocaleUtil.getDefault().getLanguage()}, 
								new String[]{""}, 
								vocabulary.getVocabularyId(), 
								new String[]{""}, 
								ctx);
					}
					

				}
				
			}

		}
	}
}
