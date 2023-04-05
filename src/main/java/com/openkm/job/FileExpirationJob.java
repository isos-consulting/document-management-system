package com.openkm.job;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openkm.automation.AutomationException;
import com.openkm.bean.Repository;
import com.openkm.core.AccessDeniedException;
import com.openkm.core.DatabaseException;
import com.openkm.core.LockException;
import com.openkm.core.PathNotFoundException;
import com.openkm.core.RepositoryException;
import com.openkm.dao.NodeBaseDAO;
import com.openkm.dao.NodeDocumentDAO;
import com.openkm.dao.bean.NodeBase;
import com.openkm.dao.bean.NodeDocument;
import com.openkm.module.db.DbDocumentModule;

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
