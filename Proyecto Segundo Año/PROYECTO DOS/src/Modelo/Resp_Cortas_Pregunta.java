package Modelo;

/**
 * Subclase de Pregunta, esta permite realizar preguntas con respuestas cortas.
 * 
 * @author Antony
 *
 */
public class Resp_Cortas_Pregunta extends Pregunta {

	private String respuestaCorta;

	/**
	 * Metodo constructor.
	 * 
	 * @param enunciado      Texto del encabezado que tendrá la pregunta.
	 * @param respuestaCorta Palabra que el usuario ingresa para responder la
	 *                       pregunta.
	 * @param peso           Valor numerico que tendrá la pregunta.
	 */
	public Resp_Cortas_Pregunta(String enunciado, String respuestaCorta, int peso) {
		super(enunciado, peso);
		setRespuestaCorta(respuestaCorta);
	}

	/**
	 * Metodo que realiza el proceso de preguntar retorna "true" si la respuesta es
	 * correcta y "false" si es incorrecta.
	 */
	@Override
	@Deprecated
	public boolean buscar() {
		return false;
		/*System.out.print(getEnunciado() + ": ");
		if (getRespuestaCorta().equalsIgnoreCase(complementos.leerDeTeclado())) {
			System.out.println("CORRECTO\n");
			Exam.preg_buenas++;
			return true;
		} else {
			System.err.println("INCORRECTA, la opcion correcta era " + getRespuestaCorta() + "\n");
			Exam.preg_malas++;
			return false;
		}*/
	}

	/**
	 * Devuelve la respuesta correcta.
	 * 
	 * @return String
	 */
	public String getRespuestaCorta() {
		return this.respuestaCorta;
	}

	/**
	 * Setea la respuesta correcta del enunciado.
	 * 
	 * @param respuestaCorta String que contiene la respuesta
	 */
	public void setRespuestaCorta(String respuestaCorta) {
		this.respuestaCorta = respuestaCorta;
	}

}
