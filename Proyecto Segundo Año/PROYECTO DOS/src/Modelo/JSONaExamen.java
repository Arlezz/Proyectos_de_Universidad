package Modelo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.xml.bind.DatatypeConverter;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Transforma el archivo leido a un json y despues a un
 * objeto Exam.
 * 
 * @author Antony
 *
 */
public class JSONaExamen {
	
	StringBuilder texto = new StringBuilder();
	JSONObject preg;
	JSONObject smtt;
	JSONObject tftt;
	JSONObject rctt;
	JSONArray selmul;
	JSONArray tf;
	JSONArray rc;
	JSONObject formaExam;
	
	JSONObject jsonAlumno;
	
	int numeroDeLinea = 1;
	byte[] base64Decoded;

	/**
	 * 
	 * Lee un archivo y genera un examen
	 * de acuerdo a las indicaciones.
	 * 
	 * @param ubicacion
	 * @return
	 * @throws FileNotFoundException
	 */
	public Exam generaExamen(File ubicacion) throws FileNotFoundException {
		Exam examen = new Exam();	
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(ubicacion);
		while (scan.hasNext()) {
			String text = scan.nextLine();
			if(!validaExamen(text)) {
				throw new IllegalArgumentException("Archivo modificado");
			}
			base64Decoded = DatatypeConverter.parseBase64Binary(text);
			String textoConvertido = new String(base64Decoded);
			
			switch (numeroDeLinea) {
			case 1: {
				preg = new JSONObject(textoConvertido);
				numeroDeLinea++;
				break;
			}
			case 2: {
				selmul = new JSONArray(textoConvertido);
				numeroDeLinea++;
				break;
			}
			case 3: {
				tf = new JSONArray(textoConvertido);
				numeroDeLinea++;
				break;
			}
			case 4: {
				rc = new JSONArray(textoConvertido);
				numeroDeLinea = 1;
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + numeroDeLinea);
			}
			
		}
		numeroDeLinea = 0;
		
		examen.setFormaExamen(preg.getInt("formaExamen"));
		examen.setTiempo(preg.getInt("Tiempo"));
		examen.setTotalPreguntas(preg.getInt("TotalPreguntas"));
		switch (preg.getInt("formaExamen")) {
		case 1:
		case 2:
		case 3:{
			for (int i = 0; i < selmul.length(); i++) {
				JSONArray arr = selmul.getJSONObject(i).getJSONObject("Seleccion Multiple " + (i + 1)).getJSONArray("StrRespuesta");
				String[] alternativas = new String[arr.length()];
				for (int j = 0; j < arr.length(); j++) {
					alternativas[j] = arr.getString(j);
				}
				examen.agregaPregunta(new Selec_Mul_Pregunta(selmul.getJSONObject(i).getJSONObject("Seleccion Multiple " + (i + 1)).getString("Enunciado"), alternativas, selmul.getJSONObject(i).getJSONObject("Seleccion Multiple " + (i + 1)).getInt("IndexCorrecto"), selmul.getJSONObject(i).getJSONObject("Seleccion Multiple " + (i + 1)).getInt("peso")));
			}
			for (int i = 0; i < tf.length(); i++) {
				examen.agregaPregunta(new TFpreguntas(tf.getJSONObject(i).getJSONObject("Verdadero o Falso " + (i + 1)).getString("Enunciado"), tf.getJSONObject(i).getJSONObject("Verdadero o Falso " + (i + 1)).getBoolean("Respuesta"), tf.getJSONObject(i).getJSONObject("Verdadero o Falso " + (i + 1)).getInt("Peso")));
			}
			for (int i = 0; i < rc.length(); i++) {
				examen.agregaPregunta(new Resp_Cortas_Pregunta(rc.getJSONObject(i).getJSONObject("Respuesta Corta " + (i + 1)).getString("Enunciado"), rc.getJSONObject(i).getJSONObject("Respuesta Corta " + (i + 1)).getString("Respuesta"), rc.getJSONObject(i).getJSONObject("Respuesta Corta " + (i + 1)).getInt("Peso")));
			}
			return examen;
		}
		case 4: {
			for (int i = 0; i < selmul.length(); i++) {
				JSONArray arr = selmul.getJSONObject(i).getJSONObject("Seleccion Multiple " + (i + 1)).getJSONArray("StrRespuesta");
				String[] alternativas = new String[arr.length()];
				for (int j = 0; j < arr.length(); j++) {
					alternativas[j] = arr.getString(j);
				}
				examen.agregaPregunta(new Selec_Mul_Pregunta(selmul.getJSONObject(i).getJSONObject("Seleccion Multiple " + (i + 1)).getString("Enunciado"), alternativas, selmul.getJSONObject(i).getJSONObject("Seleccion Multiple " + (i + 1)).getInt("IndexCorrecto"), selmul.getJSONObject(i).getJSONObject("Seleccion Multiple " + (i + 1)).getInt("peso")));
			}
			return examen;
		}
		case 5: {
			for (int i = 0; i < tf.length(); i++) {
				examen.agregaPregunta(new TFpreguntas(tf.getJSONObject(i).getJSONObject("Verdadero o Falso " + (i + 1)).getString("Enunciado"), tf.getJSONObject(i).getJSONObject("Verdadero o Falso " + (i + 1)).getBoolean("Respuesta"), tf.getJSONObject(i).getJSONObject("Verdadero o Falso " + (i + 1)).getInt("Peso")));
			}
			return examen;
		}
		case 6: {
			for (int i = 0; i < rc.length(); i++) {
				examen.agregaPregunta(new Resp_Cortas_Pregunta(rc.getJSONObject(i).getJSONObject("Respuesta Corta " + (i + 1)).getString("Enunciado"), rc.getJSONObject(i).getJSONObject("Respuesta Corta " + (i + 1)).getString("Respuesta"), rc.getJSONObject(i).getJSONObject("Respuesta Corta " + (i + 1)).getInt("Peso")));
			}
			return examen;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + preg.getInt("formaExamen"));
		}
	}
		
	/**
	 * 
	 * Genera el modelo de las notas del alumno
	 * lo que se mostrara en la tabla de los resultados
	 * 
	 * @param ubicacion
	 * @return
	 * @throws FileNotFoundException
	 */
	public ModeloNotasAlumno generaResultadoExam(File ubicacion) throws FileNotFoundException{
		ModeloNotasAlumno alumno = new ModeloNotasAlumno();
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(ubicacion);
		while (scan.hasNext()) {
			String text = scan.nextLine();
			if(!validaExamen(text)) {
				throw new IllegalArgumentException("Archivo modificado");
			}
			base64Decoded = DatatypeConverter.parseBase64Binary(text);
			String textoConvertido = new String(base64Decoded);
			jsonAlumno = new JSONObject(textoConvertido);
		}
		
		JSONArray arr = jsonAlumno.getJSONArray("puntajeUsuario");
		int[] puntajeA = new int[arr.length()];
		for(int i = 0; i < arr.length(); i++) {
			puntajeA[i] = arr.getInt(i);
		}
		
		JSONArray arr2 = jsonAlumno.getJSONArray("puntajeExamen");
		int[] puntajeE = new int[arr2.length()];
		for (int i = 0; i < arr2.length(); i++) {
			puntajeE[i] = arr2.getInt(i);
		}
		JSONArray arr3 = jsonAlumno.getJSONArray("respuestaUsuario");
		String[] respuestaA = new String[arr3.length()];
		for (int i = 0; i < arr3.length(); i++) {
			respuestaA[i] = arr3.getString(i);
		}
		
		JSONArray arr4 = jsonAlumno.getJSONArray("respuestaExamen");
		String[] respuestaE = new String[arr4.length()];
		for (int i = 0; i < arr4.length(); i++) {
			respuestaE[i] = arr4.getString(i);
		}
		
		alumno.setPuntajeAlumno(puntajeA);
		alumno.setPuntajeExamen(puntajeE);
		alumno.setRespuestaAlumno(respuestaA);
		alumno.setRespuestaExamen(respuestaE);
			
		return alumno;
	}
	
	/**
	 * 
	 * retorna el estado del examen
	 * el cual permitira realizar
	 * el examen de la manera correcta
	 * 
	 * @param ubicacion
	 * @return
	 * @throws FileNotFoundException
	 */
	public String getEstadoExamen(File ubicacion) throws FileNotFoundException {
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(ubicacion);
		while (scan.hasNext()) {
			String text = scan.nextLine();
			if(!validaExamen(text)) {
				throw new IllegalArgumentException("Archivo modificado");
			}
			base64Decoded = DatatypeConverter.parseBase64Binary(text);
			String textoConvertido = new String(base64Decoded);
			jsonAlumno = new JSONObject(textoConvertido);
		}
		return jsonAlumno.getString("estadoExamen");
	}
	
	
	/**
	 * valida que un archivo no halla sido modificaado
	 * 
	 * @param examen
	 * @return
	 */
	public static boolean validaExamen(String examen) {
		return examen.matches("^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$");

	}
}
