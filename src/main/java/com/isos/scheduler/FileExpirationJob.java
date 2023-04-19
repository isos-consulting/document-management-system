/**
 * OpenKM, Open Document Management System (http://www.openkm.com)
 * Copyright (c) 2006-2017 Paco Avila & Josep Llort
 * <p>
 * No bytes were intentionally harmed during the development of this application.
 * <p>
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.isos.scheduler;

import com.isos.dao.NodeDocumentExpirePropertyDAO;
import com.openkm.automation.AutomationException;
import com.openkm.bean.Repository;
import com.openkm.core.*;
import com.openkm.dao.NodeDocumentDAO;
import com.openkm.dao.bean.NodeDocument;
import com.openkm.extension.core.ExtensionException;
import com.openkm.module.DocumentModule;
import com.openkm.module.ModuleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class FileExpirationJob {
	private static final Logger log = LoggerFactory.getLogger(FileExpirationJob.class);

	public static void run() throws DatabaseException, PathNotFoundException, AutomationException, LockException, AccessDeniedException, RepositoryException, ExtensionException {
		LocalDate today = LocalDate.now();

		NodeDocumentExpirePropertyDAO nodeDocumentExpirePropertyDAO = NodeDocumentExpirePropertyDAO.getInstance();
		List<String> nodeUuids = nodeDocumentExpirePropertyDAO.findByToday(today);

		log.info("File Expiration Job: {} files to delete", nodeUuids.size());
		for(String nodeUuid: nodeUuids) {
			NodeDocument nodeBase = NodeDocumentDAO.getInstance().findByPk(nodeUuid);

			DocumentModule documentModule = ModuleManager.getDocumentModule();
			if(!documentModule.getPath(null, nodeUuid).contains(Repository.TRASH)) {
				log.info("node info uuid: {}, path: {}", nodeUuid, documentModule.getPath(null, nodeUuid));
				log.info("document info name: {}, uuid: {}", nodeBase.getName(), nodeBase.getUuid());
				documentModule.delete(null, nodeBase.getUuid());
			}
		}
	}
}
