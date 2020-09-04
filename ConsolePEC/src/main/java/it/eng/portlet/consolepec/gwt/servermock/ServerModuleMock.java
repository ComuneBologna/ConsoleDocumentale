/**
 * Copyright 2011 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package it.eng.portlet.consolepec.gwt.servermock;

import it.eng.portlet.consolepec.gwt.servermock.fascicolo.CancellaAllegatoFascicoloMockActionHandler;
import it.eng.portlet.consolepec.gwt.servermock.fascicolo.CaricaPraticaFascicoloMockActionHandler;
import it.eng.portlet.consolepec.gwt.servermock.fascicolo.CercaPraticheMockActionHandler;
import it.eng.portlet.consolepec.gwt.servermock.fascicolo.CreaRispostaMockActionHandler;
import it.eng.portlet.consolepec.gwt.servermock.fascicolo.FirmaAllegatoFascicoloMockActionHandler;
import it.eng.portlet.consolepec.gwt.servermock.fascicolo.SalvaFascicoloMockActionHandler;
import it.eng.portlet.consolepec.gwt.servermock.fascicolo.UploadAllegatoFascicoloMockActionHandler;
import it.eng.portlet.consolepec.gwt.servermock.pec.CancellaAllegatoPecOutMockActionHandler;
import it.eng.portlet.consolepec.gwt.servermock.pec.CaricaPraticaEmailMockActionHandler;
import it.eng.portlet.consolepec.gwt.servermock.pec.CaricaPraticaEmailOutMockActionHandler;
import it.eng.portlet.consolepec.gwt.servermock.pec.CreaFascicoloMockActionHandler;
import it.eng.portlet.consolepec.gwt.servermock.pec.SalvaBozzaInvioMockActionHandler;
import it.eng.portlet.consolepec.gwt.servermock.pec.UploadAllegatoPraticaEmailOutMockActionHandler;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;
import it.eng.portlet.consolepec.gwt.shared.action.GetConfigurazioneCampiProtocollazione;
import it.eng.portlet.consolepec.gwt.shared.action.ProtocollaAction;
import it.eng.portlet.consolepec.gwt.shared.action.assegna.pec.RiassegnaPecIn;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CancellaAllegatoFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CaricaPraticaFascicoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaFascicoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaRisposta;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.FirmaAllegatoFascicoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.SalvaFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.UploadAllegatoFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CambiaStatoPecInAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CancellaAllegatoPecOut;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CaricaPraticaEmailInAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CaricaPraticaEmailOutAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.SalvaBozzaInvio;
import it.eng.portlet.consolepec.gwt.shared.action.pec.UploadAllegatoPraticaAction;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.gwtplatform.dispatch.server.actionvalidator.ActionValidator;
import com.gwtplatform.dispatch.server.spring.HandlerModule;
import com.gwtplatform.dispatch.server.spring.actionvalidator.DefaultActionValidator;
import com.gwtplatform.dispatch.server.spring.configuration.DefaultModule;

@Configuration
@Import(DefaultModule.class)
public class ServerModuleMock extends HandlerModule {

	public ServerModuleMock() {
	}

	@Bean
	public ActionValidator getDefaultActionValidator() {
		return new DefaultActionValidator();
	}

	protected void configureHandlers() {
		bindHandler(CaricaPraticaEmailInAction.class, CaricaPraticaEmailMockActionHandler.class);
		bindHandler(CambiaStatoPecInAction.class, ArchiviaEliminaActionMockHandler.class);

		bindHandler(RiassegnaPecIn.class, CambiaGruppoActionMockHandler.class);
		bindHandler(GetConfigurazioneCampiProtocollazione.class, GetConfigurazioneCampiMockActionHandler.class);
		bindHandler(ProtocollaAction.class, ProtocollaMockActionHandler.class);
		
		// gestione fascicolo

		bindHandler(CercaPratiche.class, CercaPraticheMockActionHandler.class);
		bindHandler(CaricaPraticaFascicoloAction.class, CaricaPraticaFascicoloMockActionHandler.class);
		bindHandler(CreaRisposta.class, CreaRispostaMockActionHandler.class);
		bindHandler(SalvaFascicolo.class, SalvaFascicoloMockActionHandler.class);
		bindHandler(CancellaAllegatoFascicolo.class, CancellaAllegatoFascicoloMockActionHandler.class);
		bindHandler(FirmaAllegatoFascicoloAction.class, FirmaAllegatoFascicoloMockActionHandler.class);

		// gestione emailOUT
		bindHandler(CaricaPraticaEmailOutAction.class, CaricaPraticaEmailOutMockActionHandler.class);
		bindHandler(SalvaBozzaInvio.class, SalvaBozzaInvioMockActionHandler.class);

		bindHandler(CreaFascicoloAction.class, CreaFascicoloMockActionHandler.class);

		bindHandler(CancellaAllegatoPecOut.class, CancellaAllegatoPecOutMockActionHandler.class);

		bindHandler(UploadAllegatoPraticaAction.class, UploadAllegatoPraticaEmailOutMockActionHandler.class);

		bindHandler(UploadAllegatoFascicolo.class, UploadAllegatoFascicoloMockActionHandler.class);

	}

	@Bean
	public CercaPraticheMockActionHandler CercaPraticheActionHandler() {
		return new CercaPraticheMockActionHandler();
	}

	@Bean
	public GetConfigurazioneCampiMockActionHandler GetConfigurazioneCampiActionHandler() {
		return new GetConfigurazioneCampiMockActionHandler();
	}

	@Bean
	public CaricaPraticaEmailMockActionHandler getEspandiRigaActionActionHandler() {
		return new CaricaPraticaEmailMockActionHandler();
	}

	@Bean
	public ArchiviaEliminaActionMockHandler getArchiviaEliminaActionActionHandler() {
		return new ArchiviaEliminaActionMockHandler();
	}

	@Bean
	public CambiaGruppoActionMockHandler getCambiaGruppoActionHandler() {
		return new CambiaGruppoActionMockHandler();
	}

	@Bean
	public ProtocollaMockActionHandler getProtocollaActionHandler() {
		return new ProtocollaMockActionHandler();
	}

	@Bean
	public CaricaPraticaFascicoloMockActionHandler getRecuperaDettaglioPraticheActionHandler() {
		return new CaricaPraticaFascicoloMockActionHandler();
	}

	@Bean
	public CreaRispostaMockActionHandler getCreaRispostaMockActionHandler() {
		return new CreaRispostaMockActionHandler();
	}

	@Bean
	public SalvaFascicoloMockActionHandler getSalvaFascicoloMockActionHandler() {
		return new SalvaFascicoloMockActionHandler();
	}

	@Bean
	public CaricaPraticaEmailOutMockActionHandler getCaricaPraticaEmailOutMockActionHandler() {
		return new CaricaPraticaEmailOutMockActionHandler();
	}

	@Bean
	public SalvaBozzaInvioMockActionHandler getSalvaBozzaInvioActionHandler() {

		return new SalvaBozzaInvioMockActionHandler();
	}

	@Bean
	public CreaFascicoloMockActionHandler getCreaFascicoloMockActionHandler() {

		return new CreaFascicoloMockActionHandler();
	}

	@Bean
	public CancellaAllegatoPecOutMockActionHandler getCancellaAllegatoPecOutActionHandler() {

		return new CancellaAllegatoPecOutMockActionHandler();
	}

	@Bean
	public UploadAllegatoPraticaEmailOutMockActionHandler getUploadAllegatoPecOutActionHandler() {

		return new UploadAllegatoPraticaEmailOutMockActionHandler();
	}

	@Bean
	public UploadAllegatoFascicoloMockActionHandler getUploadAllegatoFascicoloActionHandler() {
		return new UploadAllegatoFascicoloMockActionHandler();
	}

	@Bean
	public CancellaAllegatoFascicoloMockActionHandler getCancellaAllegatoFascicoloMockActionHandler() {
		return new CancellaAllegatoFascicoloMockActionHandler();
	}

	public FirmaAllegatoFascicoloMockActionHandler getFirmaAllegatoFascicoloMockActionHandler() {
		return new FirmaAllegatoFascicoloMockActionHandler();
	}

}
