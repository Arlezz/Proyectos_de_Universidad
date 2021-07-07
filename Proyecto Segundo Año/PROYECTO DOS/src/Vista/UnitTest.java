package Vista;

import static org.junit.Assert.assertEquals;

import javax.swing.JTable;

import org.junit.Test;

import Vista.AlumnoGUI.ExamenGUI;

public class UnitTest {
	
	@Test
	public void testProfesor() {
		ProfesorGUI profe = new ProfesorGUI();
		String email = profe.getCorreoAlumno("Antony Rodriguez");
		String emailEsp = "antonyrodriguezc.20001@gmail.com";
		assertEquals(email, emailEsp);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testProfesor2() {
		ProfesorGUI profe = new ProfesorGUI();
		profe.llenaTablaDeExamenes(null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testProfesor3() {
		ProfesorGUI profe = new ProfesorGUI();
		profe.llenaTablaDeAlumnos(null, null, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testProfesor4() {
		ProfesorGUI profe = new ProfesorGUI();
		profe.graficarResultados(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAlum() {
		AlumnoGUI alum = new AlumnoGUI();
		alum.realizarExamen(-1,null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void Alum2() {
		AlumnoGUI alum = new AlumnoGUI();
		alum.agregaProfesoresCombo(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void examenGUI() {
		ExamenGUI exa = new AlumnoGUI().new ExamenGUI();		
		exa.agregaBotonesDinamicoSelecMult(null);
	}
	@Test(expected = IllegalArgumentException.class)
	public void Alum3() {
		AlumnoGUI alum = new AlumnoGUI();
		alum.llenaTablaDeExamenes(new JTable(),null);
	}
	
}