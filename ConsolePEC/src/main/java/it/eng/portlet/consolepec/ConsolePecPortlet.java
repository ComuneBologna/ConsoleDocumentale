package it.eng.portlet.consolepec;

import java.io.IOException;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.PortletSecurityException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.servlet.PortalSessionThreadLocal;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.expando.model.ExpandoColumn;
import com.liferay.portlet.expando.model.ExpandoTable;
import com.liferay.portlet.expando.model.ExpandoValue;
import com.liferay.portlet.expando.service.ExpandoColumnLocalServiceUtil;
import com.liferay.portlet.expando.service.ExpandoTableLocalServiceUtil;
import com.liferay.portlet.expando.service.ExpandoValueLocalServiceUtil;

import it.eng.portlet.consolepec.gwt.shared.SessionUtils;
import it.eng.portlet.consolepec.spring.bean.session.TipoLogin;
import it.eng.portlet.consolepec.spring.bean.session.user.ConsolePecUser;

public class ConsolePecPortlet extends com.liferay.util.bridges.mvc.MVCPortlet {

	protected String structureCategoriesRoot_;

	protected String viewJsp_;

	protected String editJsp_;

	Logger logger = LoggerFactory.getLogger(ConsolePecPortlet.class);

	@Override
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, PortletSecurityException, IOException {
		logger.debug("ConsolePecPortlet IN");

		try {
			User user = PortalUtil.getUser(renderRequest);
			if (user != null) {
				logger.debug("Caricato user {} {}", user.getUserUuid(), user.getFullName());
			} else {
				logger.warn("Utente non caricato!");
			}

			TipoLogin tipoLogin = PortalSessionThreadLocal.getHttpSession().getAttribute(SessionUtils.SESSION_TIPO_LOGIN) != null
					? TipoLogin.from(PortalSessionThreadLocal.getHttpSession().getAttribute(SessionUtils.SESSION_TIPO_LOGIN).toString()) : TipoLogin.LDAP;
			logger.info("Tipo login: " + tipoLogin);

			long companyId = PortalUtil.getCompanyId(renderRequest);
			ConsolePecUser consolePecUser = new ConsolePecUser(companyId, user, tipoLogin);

			ExpandoTable expandoTable = ExpandoTableLocalServiceUtil.getDefaultTable(companyId, User.class.getName());
			List<ExpandoColumn> columns = ExpandoColumnLocalServiceUtil.getColumns(expandoTable.getTableId());
			for (ExpandoColumn ec : columns) {
				ExpandoValue expandoValue = ExpandoValueLocalServiceUtil.getValue(expandoTable.getTableId(), ec.getColumnId(), user.getExpandoBridge().getClassPK());
				consolePecUser.addCustomAttribute(ec.getName(), expandoValue == null ? null : expandoValue.getData());
				logger.debug("Custom attribute: ", consolePecUser.getCustomAttribute(ec.getName()));

			}

			logger.debug("Utente Custom attributes matricola:{} coboCF:{}", consolePecUser.getCustomAttribute("matricola"), consolePecUser.getCustomAttribute("coboCF"));
			PortletSession portletSession = renderRequest.getPortletSession();
			logger.debug("jsessionid portletside: " + portletSession.getId());
			portletSession.setAttribute(SessionUtils.CURRENT_USER_SESSION_KEY, consolePecUser, PortletSession.APPLICATION_SCOPE);
			if (portletSession.getAttribute(SessionUtils.HTTP_SESSION_KEY, PortletSession.APPLICATION_SCOPE) != null) {
				logger.debug("Cleanup delle Pratiche in sessione");
				portletSession.setAttribute(SessionUtils.HTTP_SESSION_KEY, null, PortletSession.APPLICATION_SCOPE);
			}
		} catch (PortalException e) {
			logger.error("PortalException", e);
		} catch (SystemException e) {
			logger.error("SystemException", e);
		}
		super.doView(renderRequest, renderResponse);
		logger.debug("ConsolePecPortlet out");
	}

}
