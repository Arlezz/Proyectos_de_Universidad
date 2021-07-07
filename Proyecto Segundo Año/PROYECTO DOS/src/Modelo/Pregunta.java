package Modelo;

/**
 * Clase abstracta de la cual dependen las demas subclases y contiene atributos
 * y metodos en común de los distintos tipos de pregunta.
 * 
 * @author Antony
 *
 */
public abstract class Pregunta {

	protected int peso;
	protected String enunciado;
	/**
	 * Metodo constructor.
	 * 
	 * @param enunciado Texto del encabezado que tendrá la pregunta.
	 * @param peso      Valor numerico que tendrá la pregunta.
	 */
	public Pregunta(String enunciado, int peso) {
		this.enunciado = enunciado;
		setPeso(peso);
	}

	/**
	 * Metodo abstracto que heredaran las subclases.
	 * 
	 * @return boolean
	 */
	@Deprecated
	public abstract boolean buscar();

	/**
	 * Devuelve el peso.
	 * 
	 * @return int
	 */
	public int getPeso() {
		return this.peso;
	}

	/**
	 * Setea el peso.
	 * 
	 * @param peso Valor numerico que tendrá la pregunta.
	 */
	public void setPeso(int peso) {
		this.peso = peso;
	}

	/**
	 * Devuelve el enunciado
	 * 
	 * @return String
	 */
	public String getEnunciado() {
		return this.enunciado;
	}

}
