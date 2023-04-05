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

package com.openkm.job;

import com.openkm.automation.AutomationException;
import com.openkm.bean.Repository;
import com.openkm.core.*;
import com.openkm.dao.NodeBaseDAO;
import com.openkm.dao.NodeDocumentDAO;
import com.openkm.dao.bean.NodeBase;
import com.openkm.dao.bean.NodeDocument;
import com.openkm.module.db.DbDocumentModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class FileExpirationJob {
	private static final Logger log = LoggerFactory.getLogger(FileExpirationJob.class);

	public static void run() throws DatabaseException, PathNotFoundException, AutomationException, LockException, AccessDeniedException, RepositoryException {
		LocalDate today = LocalDate.now();
		LocalDateTime midnight = LocalDateTime.of(today, LocalTime.MIDNIGHT);
		Date date = Date.from(midnight.atZone(ZoneId.systemDefault()).toInstant());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		NodeBaseDAO nodeBaseDAO = NodeBaseDAO.getInstance();
		List<NodeBase> removedNodeBaseList = nodeBaseDAO.findByPropertyGroup("okp:management.date", sdf.format(date));

		for(NodeBase nb: removedNodeBaseList) {
			NodeDocument nodeDocument = NodeDocumentDAO.getInstance().findByPk(nb.getUuid());
			NodeBase parentNode = nodeBaseDAO.findByPk(nb.getParent());

			DbDocumentModule dbDocumentModule = new DbDocumentModule();
			if(!dbDocumentModule.getPath(null, nb.getUuid()).contains(Repository.TRASH)) {
				log.info("node info uuid: {}, path: {}", nb.getUuid(), dbDocumentModule.getPath(null, nb.getUuid()));
				log.info("document info name: {}, uuid: {}", nodeDocument.getName(), nodeDocument.getUuid());
				dbDocumentModule.delete(null, nodeDocument.getUuid());
			}
		}
	}
}
