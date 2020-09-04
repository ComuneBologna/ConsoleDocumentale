package it.eng.portlet.consolepec.spring.bean.session.user.impl;

import it.eng.portlet.consolepec.spring.bean.session.user.ConsolePecUser;
import it.eng.portlet.consolepec.spring.bean.session.user.UserCustomFields;
import it.eng.portlet.consolepec.spring.bean.session.user.UserException;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.expando.model.ExpandoColumn;
import com.liferay.portlet.expando.model.ExpandoTable;
import com.liferay.portlet.expando.model.ExpandoValue;
import com.liferay.portlet.expando.service.ExpandoColumnLocalServiceUtil;
import com.liferay.portlet.expando.service.ExpandoTableLocalServiceUtil;
import com.liferay.portlet.expando.service.ExpandoValueLocalServiceUtil;

public class UserCustomFieldsImpl implements UserCustomFields {

	Logger logger = LoggerFactory.getLogger(UserCustomFields.class);

	@Override
	public void popolaCustomFields(ConsolePecUser cpu) throws UserException {
		logger.debug("popolaCustomFields companyId: {} liferay user is null: {}", cpu.getCompanyId(), cpu.getUser() == null);
		try {
			User user = cpu.getUser();
			ExpandoTable expandoTable = ExpandoTableLocalServiceUtil.getDefaultTable(cpu.getCompanyId(), User.class.getName());
			List<ExpandoColumn> columns = ExpandoColumnLocalServiceUtil.getColumns(expandoTable.getTableId());
			for (ExpandoColumn ec : columns) {
				ExpandoValue expandoValue = ExpandoValueLocalServiceUtil.getValue(expandoTable.getTableId(), ec.getColumnId(), user.getExpandoBridge().getClassPK());
				logger.debug("field name: {} field value: {}", ec.getName(), expandoValue.getData());
				cpu.addCustomAttribute(ec.getName(), expandoValue.getData());
			}
		} catch (Exception e) {
			String err = "Errore nel popolamento custom fields";
			logger.error(err, e);
			throw new UserException(err, e);
		}
	}

	@Override
	public void updateCustomField(ConsolePecUser cpu, String attributeName, String attributeValue) throws UserException {
		logger.debug("updateCustomField  attributeName: {} attributeValue: {} liferay user is null: {}", attributeName, attributeValue, cpu.getUser() == null);
		try {
			User user = cpu.getUser();
			ExpandoTable expandoTable = ExpandoTableLocalServiceUtil.getDefaultTable(cpu.getCompanyId(), User.class.getName());
			ExpandoColumn column = ExpandoColumnLocalServiceUtil.getColumn(expandoTable.getTableId(), attributeName);
			ExpandoValue expandoValue = ExpandoValueLocalServiceUtil.getValue(expandoTable.getTableId(), column.getColumnId(), user.getExpandoBridge().getClassPK());
			if (expandoValue == null) {
				ExpandoValueLocalServiceUtil.addValue(PortalUtil.getClassNameId(user.getClass()), expandoTable.getTableId(), column.getColumnId(), user.getExpandoBridge().getClassPK(), attributeValue);
			} else {
				expandoValue.setData(attributeValue);
				ExpandoValueLocalServiceUtil.updateExpandoValue(expandoValue);
			}
			cpu.addCustomAttribute(attributeName, attributeValue);
		} catch (Exception e) {
			String err = "Errore nell'aggiornamento custom field: " + attributeName;
			logger.error(err, e);
			throw new UserException(err, e);
		}
	}

	@Override
	public void deleteCustomField(ConsolePecUser cpu, String attributeName) throws UserException {
		logger.debug("deleteCustomField  attributeName: {}  liferay user is null: {}", attributeName, cpu.getUser() == null);
		try {
			User user = cpu.getUser();
			ExpandoTable expandoTable = ExpandoTableLocalServiceUtil.getDefaultTable(cpu.getCompanyId(), User.class.getName());
			ExpandoColumn column = ExpandoColumnLocalServiceUtil.getColumn(expandoTable.getTableId(), attributeName);
			if(column != null){
				
				ExpandoValue expandoValue = ExpandoValueLocalServiceUtil.getValue(expandoTable.getTableId(), column.getColumnId(), user.getExpandoBridge().getClassPK());
				if (expandoValue != null) {
					ExpandoValueLocalServiceUtil.deleteExpandoValue(expandoValue);
				}	
			}
			/* aggiorno consolepecuser */
			cpu.removeCustomAttribute(attributeName);
		} catch (Exception e) {
			String err = "Errore nell'aggiornamento custom field: " + attributeName;
			logger.error(err, e);
			throw new UserException(err, e);
		}
	}
}
