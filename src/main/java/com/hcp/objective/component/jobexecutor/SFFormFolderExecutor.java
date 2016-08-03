package com.hcp.objective.component.jobexecutor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hcp.objective.persistence.bean.FormFolder;
import com.hcp.objective.service.FormFolderService;
import com.hcp.objective.service.IODataService;

public class SFFormFolderExecutor extends HcpExecutor implements IExecutor {

	private String key_result = "results";
	private String key_d = "d";
	private String key_folderId = "folderId";
	private String key_userId = "userId";
	private String key_folderName = "folderName";

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		FormFolderService repositoryService = (FormFolderService) this.getBean(FormFolderService.class);

		IODataService oDataService = (IODataService) this.getBean(IODataService.class);

		if (oDataService != null) {
			String sData = oDataService.getFormFolder(null);
			System.out.println(sData);
			JSONObject object = new JSONObject(sData);
			JSONArray array = object.getJSONObject(key_d).getJSONArray(key_result);
			System.out.println(array.length());

			if (array != null) {
				List<FormFolder> folders = new ArrayList<FormFolder>();
				for (Iterator<Object> iterator = array.iterator(); iterator.hasNext();) {
					JSONObject one = (JSONObject) iterator.next();
					FormFolder bean = new FormFolder();
					bean.setFolderId(one.getLong(key_folderId));
					bean.setUserId(one.getString(key_userId));
					bean.setFolderName(one.getString(key_folderName));
					System.out.print("folder id:" + bean.getFolderId());
					System.out.print("user id:" + bean.getUserId());
					System.out.println("folder name:" + bean.getFolderName());
					folders.add(bean);
				}
				repositoryService.createMore(folders);
			}

		}
	}

}
