package Modelo;

/**
 * Subclase de Pregunta, esta permite realizar preguntas de selección multiple.
 * 
 * @author Antony
 *
 */
public class Selec_Mul_Pregunta extends Pregunta {

	private String[] opciones;
	private int index_respuesta;
	private int tamanio_arreglo;

	/**
	 * Metodo Constructor
	 * 
	 * @param enunciado       Texto del encabezado que tendrá la pregunta.
	 * @param opciones        Todas las posibles respuestas para la pregunta.
	 * @param index_respuesta Posicion en la que se encuentra la respuesta correcta.
	 * @param peso            Valor numerico que tendrá la pregunta.
	 */
	public Selec_Mul_Pregunta(String enunciado, String[] opciones, int index_respuesta, int peso) {
		super(enunciado, peso);
		setopciones(opciones);
		setIndex_respuesta(index_respuesta);
		setTamanio_arreglo(opciones.length);
	}

	/**
	 * Metodo que realiza el proceso de preguntar retorna "true" si la respuesta es
	 * correcta y "false" si es incorrecta.
	 */
	@Override
	@Deprecated
	public boolean buscar() {
		return false;
		/*int aux_numero = 65; // Letra "A" en la tabla ascii
		char opcionCorrecta = '\0'; // Variable donde se almacenara la letra generada.
		char[] rango = new char[getTamanio_arreglo()];// arreglo que almacena el rango de las opciones.

		System.out.println(getEnunciado()); // Se imprime el enunciado
		if(getTamanio_arreglo() < 2) {
			System.out.println("\nNumero de alternativas insuficiente...\nTienen que ser 2 o mas.\n");
			System.exit(0);
		}else {
			for (int i = 0; i < getTamanio_arreglo(); i++) { // Ciclo for que imprime las respuestas,
				char letra = (char) aux_numero;				 // genera el rango y obtiene la respuesta
				rango[i] = letra;							 // correcta a travez del 'getIndex_respuest'
				if (i == getIndex_respuesta()) {
					opcionCorrecta = letra;
				}
				System.out.println(letra + ")" + getopciones()[i]);
				++aux_numero;
			}
		}
		
		System.out.print("Ingrese la letra que prefiera: ");
		char opcionUsuario = complementos.leerDeTeclado().toUpperCase().charAt(0);
		while (verifica_rango(rango, opcionUsuario) != true && Exam.Intentos >= 1) {
			--Exam.Intentos;
			System.err.print("Opcion fuera de rango.\nLe quedan " + Exam.Intentos + " intentos: ");
			opcionUsuario = complementos.leerDeTeclado().toUpperCase().charAt(0);
			if (verifica_rango(rango, opcionUsuario) && Exam.Intentos < 5) {
				break;
			} else if (!(verifica_rango(rango, opcionUsuario)) && Exam.Intentos == 1) {
				System.err.println("\nDemasiados intentos fallidos. VALLA A ESTUDIAR!");
				complementos menuExam = new complementos();
				//Exam.preg_sinTerminar= Exam.getTotalPreguntas()- Exam.numPreg;
				menuExam.despedida(0, 2);	
				System.exit(0);
			}
		}
		if (opcionUsuario == opcionCorrecta) {
			System.out.println("CORRECTO\n");
			Exam.preg_buenas++;
			return true;
		} else {
			System.out.println("INCORRECTO, la opcion correcta era " + opcionCorrecta + "\n");
			Exam.preg_malas++;
			return false;
		}*/
	}

	/**
	 * Setea un arreglo de posibles respuestas.
	 * 
	 * @param opciones Todas las posibles respuestas para la pregunta.
	 */
	public void setopciones(String[] opciones) {
		this.opciones = opciones;
	}

	/**
	 * Setea el índice donde se encuentra la respuesta correcta.
	 * 
	 * @param index_respuesta Posicion en la que se encuentra la respuesta correcta.
	 */
	public void setIndex_respuesta(int index_respuesta) {
		this.index_respuesta = index_respuesta;
	}

	/**
	 * Setea el tamaño del arreglo.
	 * 
	 * @param tamanio_arreglo Valor numerico
	 */
	public void setTamanio_arreglo(int tamanio_arreglo) {
		this.tamanio_arreglo = tamanio_arreglo;
	}

	/*-------------------------Getters---------------------------------*/
	/**
	 * Devuelve un arreglo de Strings que almacena las respuestas.
	 * 
	 * @return String[]
	 */
	public String[] getopciones() {
		return this.opciones;
	}

	/**
	 * Devuelve el índice donde se encuentra la respuesta correcta.
	 * 
	 * @return int
	 */
	public int getIndex_respuesta() {
		return this.index_respuesta;
	}

	/**
	 * Devuelve el tamaño del arreglo.S
	 * 
	 * @return int
	 */
	public int getTamanio_arreglo() {
		return this.tamanio_arreglo;
	}
	
	public String getRespuestaCorecta() {
		return opciones[index_respuesta];
	}
}









