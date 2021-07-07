package Modelo;

/**
 * Subclase de Pregunta, esta permite realizar preguntas de verdadero o falso.
 * 
 * @author Antony
 *
 */
public class TFpreguntas extends Pregunta {
	private boolean respuestaTF;


	/**
	 * Metodo constructor
	 * 
	 * @param enunciado Texto del encabezado que tendrá la pregunta.
	 * @param respuesta Puede ser true o false.
	 * @param peso      Valor numerico que tendrá la pregunta.
	 */
	public TFpreguntas(String enunciado, boolean respuesta, int peso) {
		super(enunciado, peso);
		this.respuestaTF = respuesta;
	}

	/**
	 * Metodo que realiza el proceso de preguntar retorna "true" si la respuesta es
	 * correcta y "false" si es incorrecta.
	 */
	@Override
	@Deprecated
	public boolean buscar() {
		return respuestaTF;
		/*boolean eleccion = false;
		System.out.print(getEnunciado() + "(ingrese \"T\" o \"F\"): ");
		char opcionUsuario = complementos.leerDeTeclado().toUpperCase().charAt(0);
		while (!(rango(opcionUsuario)) && Exam.Intentos >= 1) {
			--Exam.Intentos;
			System.err.print("Ingrese solo \"T\" o \"F\", le quedan " + Exam.Intentos + " intentos: ");
			opcionUsuario = complementos.leerDeTeclado().toUpperCase().charAt(0);
			if (rango(opcionUsuario) && Exam.Intentos < 5) {
				eleccion = (opcionUsuario == 'T') ? true : false;
				break;
			} else if (!(rango(opcionUsuario)) && Exam.Intentos == 1) {
				System.err.println("\nDemasiados intentos fallidos. VALLA A ESTUDIAR!");
				complementos menuExam = new complementos();
				//Exam.preg_sinTerminar= Exam.getTotalPreguntas()- Exam.numPreg;
				menuExam.despedida(0, 2);
				System.exit(0);
			}
		}
		eleccion = (opcionUsuario == 'T') ? true : false;
		if (eleccion == getRespuestaTF()) {
			System.out.println("CORRECTO!\n");
			Exam.preg_buenas++;
			return true;
		} else {
			System.out.println("INCORRECTO, la opcion correcta era " + getRespuestaTF() + "\n");
			Exam.preg_malas++;
			return false;
		}*/
	}

	/**
	 * Devuelve la respuesta "true" o "false".
	 * 
	 * @return boolean
	 */
	public boolean getRespuestaTF() {
		return this.respuestaTF;
	}

	/**
	 * Setea la respuesta correcta.
	 * 
	 * @param respuestaTF Tipo boolean
	 */
	public void setRespuesta(boolean respuestaTF) {
		this.respuestaTF = respuestaTF;
	}

}
