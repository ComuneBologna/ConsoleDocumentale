package it.eng.consolepec.spagicclient.bean.request.modulistica;

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode
@NoArgsConstructor
public class Sezione implements NodoModulistica{
	@Getter @Setter String titolo;
	@Getter final TipoNodoModulistica tipoNodo = TipoNodoModulistica.SEZIONE;
	@Getter List<NodoModulistica> nodi = new ArrayList<NodoModulistica>();
	
}
