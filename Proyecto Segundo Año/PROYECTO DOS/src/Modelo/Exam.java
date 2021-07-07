package Modelo;

import java.util.ArrayList;

/**
 * Clase que realiza el examen al usuario.
 * 
 * @author Antony
 *
 */
public class Exam {

	
	public  ArrayList<Selec_Mul_Pregunta> selecmulpreg = new ArrayList<Selec_Mul_Pregunta>();
	public ArrayList<TFpreguntas> tfpreg = new ArrayList<TFpreguntas>();
	public  ArrayList<Resp_Cortas_Pregunta> rcpreg = new ArrayList<Resp_Cortas_Pregunta>();
	public int tiempo;//tiempo del examen
	public int totalPreguntas;//total de preguntas del examen
	public int smTT;//total de pregunta de seleccion multiple
	public int tfTT;//total de pregunta de verdadero o falso
	public int rcTT;//total de pregunta de respuestas cortas
	public int formaExamen;//forma del examen, orden de las preguntas	
	
	/**
	 * Agrega preguntas de seleccion multiple en el almacen de preguntas.
	 * 
	 * @param PreguntaSM Seleccion multiple
	 */
	public void agregaPregunta(Selec_Mul_Pregunta PreguntaSM) {
		selecmulpreg.add(PreguntaSM);
	}

	/**
	 * Agrega preguntas de verdadero o falso en el almacen de preguntas.
	 * 
	 * @param PreguntaTF True or false
	 */
	public void agregaPregunta(TFpreguntas PreguntaTF) {
		tfpreg.add(PreguntaTF);
	}

	/**
	 * Agrega preguntas con respuestas cortas en el almacen de preguntas.
	 * 
	 * @param PreguntaRC Resspuestas cortas
	 */
	public void agregaPregunta(Resp_Cortas_Pregunta PreguntaRC) {
		rcpreg.add(PreguntaRC);
	}
		
	/**
	 * Retorna el tiempo de diracion del examen
	 * @return
	 */
	public int getTiempo() {
		return tiempo;
	}

	/**
	 * Establece el tiempo de duracion del examen
	 * 
	 * @param tiempo
	 */
	public void setTiempo(int tiempo) {
		this.tiempo = tiempo;
	}

	/**
	 * retorna la cantidad de preguntas del exament
	 * 
	 * @return
	 */
	public int getTotalPreguntas() {
		return totalPreguntas;
	}

	/**
	 * Establece la cantidad de preguntas del examen
	 * 
	 * @param totalPreguntas
	 */
	public void setTotalPreguntas(int totalPreguntas) {
		this.totalPreguntas = totalPreguntas;
	}

	/**
	 * retorna el Total de preguntas seleccion Multiple
	 * @return
	 */
	public int getSmTT() {
		return smTT;
	}

	/**
	 * establece el total de preguntas seleccion Multiple
	 * @param smTT
	 */
	public void setSmTT(int smTT) {
		this.smTT = smTT;
	}

	/**
	 * retorna el Total de preguntas verdadero o falso
	 * @return
	 */
	public int getTfTT() {
		return tfTT;
	}

	/**
	 * establece el total de preguntas verdadero o falso
	 * @param tfTT
	 */
	public void setTfTT(int tfTT) {
		this.tfTT = tfTT;
	}

	/**
	 * retorna el Total de preguntas respuestas cortas
	 * @return
	 */
	public int getRcTT() {
		return rcTT;
	}

	/**
	 * establece el total de preguntas respuestas cortas
	 * @param rcTT
	 */
	public void setRcTT(int rcTT) {
		this.rcTT = rcTT;
	}

	/**
	 * retorna la forma del examen
	 * @return
	 */
	public int getFormaExamen() {
		return formaExamen;
	}

	/**
	 * establece la forma del examen
	 * @param formaExamen
	 */
	public void setFormaExamen(int formaExamen) {
		this.formaExamen = formaExamen;
	}
	
}
