package Modelo;

/**
 * Esta clase entrega el modelo de las notas de un alumno
 * el cual se visualizara en la tabla del profesor.
 * 
 * @author Antony
 *
 */
public class ModeloNotasAlumno {

	String[] respuestaAlumno;
	String[] respuestaExamen;
	int[] puntajeAlumno;
	int[] puntajeExamen;
	
	/**
	 * Constructor vacio
	 */
	public ModeloNotasAlumno() {
		
	}
	
	/**
	 * Retorna un arreglo que contiene las respuestas seleccionadas por el alumno
	 * 
	 * @return String
	 */
	public String[] getRespuestaAlumno() {
		return respuestaAlumno;
	}
	/**
	 * 
	 * Establece una arreglo con las respuestas que selecciono el alumno
	 * 
	 * @param respuestaAlumno
	 */
	public void setRespuestaAlumno(String[] respuestaAlumno) {
		this.respuestaAlumno = respuestaAlumno;
	}
	/**
	 * retorna un arreglo con las respuesta correctas que tenia el examen
	 * 
	 * @return
	 */
	public String[] getRespuestaExamen() {
		return respuestaExamen;
	}
	/**
	 * 
	 * Setea un arreglo con las respuestas correctas del examen
	 * 
	 * @param respuestaExamen
	 */
	public void setRespuestaExamen(String[] respuestaExamen) {
		this.respuestaExamen = respuestaExamen;
	}
	/**
	 * 
	 * Devuelve un arreglo con el puntaje obtenido del alumno
	 * 
	 * @return
	 */
	public int[] getPuntajeAlumno() {
		return puntajeAlumno;
	}
	/**
	 * Setea un arreglo con el puntaje del alumno
	 * 
	 * @param puntajeAlumno
	 */
	public void setPuntajeAlumno(int[] puntajeAlumno) {
		this.puntajeAlumno = puntajeAlumno;
	}
	/**
	 * Retorna un arreglo con el valor de cada pregunta en el examen
	 * 
	 * @return
	 */
	public int[] getPuntajeExamen() {
		return puntajeExamen;
	}
	/**
	 * 
	 * Setea un arreglo con el valor para cada pregunta del examen
	 * 
	 * @param puntajeExamen
	 */
	public void setPuntajeExamen(int[] puntajeExamen) {
		this.puntajeExamen = puntajeExamen;
	}
	
	/**
	 * 
	 * Retorna el puntaje total que tiene el examen
	 * 
	 * @return
	 */
	public int getPuntajeTotalExam() {
		int total = 0;
		for (int i : puntajeExamen) {
			total += i;
		}
		return total;
	}
	
	/**
	 * 
	 * Retorna el puntaje obtenido por el alumno
	 * 
	 * @return
	 */
	public int getPuntajeTotalAlumno() {
		int total = 0;
		for (int i : puntajeAlumno) {
			total += i;
		}
		return total;
	}
	
	
	
	
}
