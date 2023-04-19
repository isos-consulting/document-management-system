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

package com.isos.dao;

import com.isos.dao.bean.NodeDocumentExpireProperty;
import com.openkm.dao.HibernateUtil;
import com.openkm.dao.bean.NodeBase;
import com.openkm.dao.bean.NodeProperty;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
/**
 * NodeDocumentExpirePropertyDAO
 *
 * @author IDKNWHORU
 */
public final class NodeDocumentExpirePropertyDAO {
	private static final Logger log = LoggerFactory.getLogger(NodeDocumentExpirePropertyDAO.class);
	private static final NodeDocumentExpirePropertyDAO instance = new NodeDocumentExpirePropertyDAO();

	private NodeDocumentExpirePropertyDAO() {
	}

	public static NodeDocumentExpirePropertyDAO getInstance() {
		return instance;
	}

	public void create(Session session, NodeBase node, NodeProperty nodeProperty) {
		log.debug("create({}, {}, {})", session, node, nodeProperty);
		try {
			long id = findByFK(node.getUuid());
			NodeDocumentExpireProperty nodeDocumentExpireProperty = id > 0 ? (NodeDocumentExpireProperty) session.load(NodeDocumentExpireProperty.class, id) : new NodeDocumentExpireProperty();
			LocalDate date = LocalDate.now().plusYears(Integer.parseInt(nodeProperty.getValue().replace("[\"", "").replace("\"]", "")));

			nodeDocumentExpireProperty.setNode(node);
			nodeDocumentExpireProperty.setValue(date);

			log.info("nodeDocumentExpireProperty({})", nodeDocumentExpireProperty);

			session.saveOrUpdate(nodeDocumentExpireProperty);
		} catch (Exception e) {
			throw new HibernateException("Cannot create node document expire property", e);
		}
	}

	private long findByFK(String fk) {
		String qs = "select ndep.id from NodeDocumentExpireProperty ndep where ndep.node.uuid = :fk";
		Session session = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();

			return (long) session.createQuery(qs).setParameter("fk", fk).uniqueResult();
		} catch (NullPointerException npe) {
			return 0;
		}catch (Exception e) {
			log.error(e.getMessage());
			throw new HibernateException("Cannot find node document expire property by fk", e);
		} finally {
			HibernateUtil.close(session);
		}
	}

	public List findByToday(LocalDate today) {
		String qs = "select ndep.node.uuid from NodeDocumentExpireProperty ndep where ndep.value = :today";
		Session session = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();

			return session.createQuery(qs).setParameter("today", today).list();
		} catch (Exception e) {
			throw new HibernateException("Cannot find node document expire property by fk", e);
		} finally {
			HibernateUtil.close(session);
		}
	}

	public void delete(Session session, String uuid) {
		log.debug("delete({})", uuid);

		try {
			long id = findByFK(uuid);
			NodeDocumentExpireProperty nodeDocumentExpireProperty = id > 0 ? (NodeDocumentExpireProperty) session.load(NodeDocumentExpireProperty.class, id) : null;

			if (nodeDocumentExpireProperty != null) {
				session.delete(nodeDocumentExpireProperty);
			}
		} catch (Exception e) {
			throw new HibernateException("Cannot delete node document expire property", e);
		}
	}
}
