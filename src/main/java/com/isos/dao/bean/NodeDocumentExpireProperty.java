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

package com.isos.dao.bean;

import com.openkm.dao.bean.NodeBase;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ISO_NODE_DOCUMENT_EXPIRE_PROPERTY", uniqueConstraints = {
	@UniqueConstraint(name = "IDX_NOD_EXPR_PROP_NODEXPNAM", columnNames = {"NDE_NODE"})})
public class NodeDocumentExpireProperty {
	@Id
	@Column(name = "NDE_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "NDE_VALUE")
	private LocalDate value;

	@OneToOne
	@JoinColumn(name = "NDE_NODE")
	private NodeBase node;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getValue() {
		return value;
	}

	public void setValue(LocalDate value) {
		this.value = value;
	}


	public NodeBase getNode() {
		return node;
	}

	public void setNode(NodeBase node) {
		this.node = node;
	}

	@Override
	public String toString() {
		return "NodeDocumentExpireProperty{" +
			"id=" + id +
			", value=" + value +
			", node=" + node +
			"}";
	}
}
