package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme;

import Modelo.ConvierteExamenJson;
import Modelo.Exam;
import Modelo.FechaDeCreacion;
import Modelo.FileSystemModel;
import Modelo.JSONaExamen;
import Modelo.ModeloNotasAlumno;
import Modelo.Resp_Cortas_Pregunta;
import Modelo.Selec_Mul_Pregunta;
import Modelo.StringSimilar;
import Modelo.TFpreguntas;

/**
 * La clase profesor tiene la posibilidad de crear examenes para los alumnos,
 * como tambien ver las notas detalladamente y poder acceder al detalle de cada
 * pregunta y enviar estos datos a travez de un correo al alumno.
 *
 * 
 * @author Antony
 * 
 * 
 */
@SuppressWarnings("serial")
public class ProfesorGUI extends JFrame {

	private JPanel PanelMayorProfesor;
	static String nombreDelArchivo = "";
	String rut;
	JTextField textRut;
	int fila;
	String ubicacionArchivos = "";
	String nombreExamen = "";
	JTable tablaDeExamenesNotas;

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(new FlatCarbonIJTheme());
		} catch (Exception ex) {
			System.err.println("Tema no aplicado");
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new ProfesorGUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Constructor de ProfesorGUI Inicializa los componentes para iniciar sesion
	 */
	public ProfesorGUI() {
		setTitle("ExamSoft");

		try {
			Image imagenLogo = ImageIO.read(new File(new File(".").getCanonicalPath() + "\\res\\soft.png"));
			setIconImage(imagenLogo);
		} catch (Exception e) {

		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		PanelMayorProfesor = new JPanel();
		PanelMayorProfesor.setLayout(new BorderLayout(10, 10));
		setContentPane(PanelMayorProfesor);
		PanelMayorProfesor.add(inicioSesionProfesor());
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);

	}

	/**
	 * Este metodo muestra un panel por pantalla en el que se tiene que ingresar los
	 * datos para poder acceder a la plataforma.
	 * 
	 * @return JPanel
	 * 
	 * 
	 */
	public JPanel inicioSesionProfesor() {
		JPanel panelContenedorInicioSesion = new JPanel();
		panelContenedorInicioSesion.setBorder(new EmptyBorder(0, 0, 20, 0));
		panelContenedorInicioSesion.setLayout(new BoxLayout(panelContenedorInicioSesion, BoxLayout.Y_AXIS));

		JPanel panelMenuBar = new JPanel(new BorderLayout(0, 10));
		panelContenedorInicioSesion.add(panelMenuBar);

		panelContenedorInicioSesion.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel panelImagen = new JPanel(new BorderLayout());
		panelImagen.setBorder(new EmptyBorder(0, 70, 0, 70));
		panelContenedorInicioSesion.add(panelImagen);

		panelContenedorInicioSesion.add(Box.createRigidArea(new Dimension(0, 30)));

		JPanel panelLblRut = new JPanel(new BorderLayout());
		panelLblRut.setBorder(new EmptyBorder(0, 40, 0, 40));
		panelContenedorInicioSesion.add(panelLblRut);

		JPanel panelTextRut = new JPanel();
		panelTextRut.setBorder(new EmptyBorder(0, 40, 0, 40));
		panelTextRut.setLayout(new BoxLayout(panelTextRut, BoxLayout.X_AXIS));
		panelContenedorInicioSesion.add(panelTextRut);

		panelContenedorInicioSesion.add(Box.createRigidArea(new Dimension(0, 30)));

		JPanel panelLblContra = new JPanel(new BorderLayout());
		panelLblContra.setBorder(new EmptyBorder(0, 40, 0, 40));
		panelContenedorInicioSesion.add(panelLblContra);

		JPanel panelTextContra = new JPanel();
		panelTextContra.setBorder(new EmptyBorder(0, 40, 0, 40));
		panelTextContra.setLayout(new BoxLayout(panelTextContra, BoxLayout.X_AXIS));
		panelContenedorInicioSesion.add(panelTextContra);

		panelContenedorInicioSesion.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel panelBotonLogin = new JPanel(new GridLayout(0, 1, 0, 0));
		panelBotonLogin.setBorder(new EmptyBorder(0, 40, 0, 40));
		panelContenedorInicioSesion.add(panelBotonLogin);

		panelContenedorInicioSesion.add(Box.createRigidArea(new Dimension(0, 0)));

		JPanel panelLblReset = new JPanel();
		panelLblReset.setBorder(new EmptyBorder(0, 65, 0, 65));
		panelLblReset.setLayout(new BorderLayout());
		panelContenedorInicioSesion.add(panelLblReset);

		JMenuBar barraOpciones = new JMenuBar();
		panelMenuBar.add(barraOpciones, BorderLayout.NORTH);
		JMenu menuOpciones = new JMenu("Opciones");
		barraOpciones.add(menuOpciones);
		JMenuItem VolverOpcion = new JMenuItem("Volver");
		VolverOpcion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				new InicioSesion();
			}
		});
		JMenuItem SalirOpcion = new JMenuItem("Salir");
		SalirOpcion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		menuOpciones.add(VolverOpcion);
		menuOpciones.add(SalirOpcion);

		String titulo = "Inicio de Sesión<br>Profesor";
		JLabel lblEncabezado = new JLabel("<html><div style='text-align: center;'>" + titulo + "</div></html>");
		lblEncabezado.setHorizontalAlignment(SwingConstants.CENTER);
		lblEncabezado.setFont(new Font("Thaoma", Font.PLAIN, 30));
		panelMenuBar.add(lblEncabezado, BorderLayout.SOUTH);

		JLabel lblImagenProfeInicSes = new JLabel();
		lblImagenProfeInicSes.setHorizontalAlignment(SwingConstants.CENTER);
		String ubicacionImagen = "";
		try {

			ubicacionImagen = new File(".").getCanonicalPath() + "\\res\\profesor.png";
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		ImageIcon imagenAlumno = new ImageIcon(ubicacionImagen);
		lblImagenProfeInicSes
				.setIcon(new ImageIcon(imagenAlumno.getImage().getScaledInstance(240, 220, Image.SCALE_DEFAULT)));
		panelImagen.add(lblImagenProfeInicSes, BorderLayout.CENTER);

		JLabel lblRut = new JLabel("Rut");
		lblRut.setFont(new Font("Thaoma", Font.PLAIN, 18));
		panelLblRut.add(lblRut, BorderLayout.WEST);

		textRut = new JTextField();
		textRut.setBackground(Color.WHITE);
		textRut.setForeground(Color.BLACK);
		textRut.addKeyListener(new noAgregaLetrasRUTField());
		textRut.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panelTextRut.add(textRut);

		JLabel lblContra = new JLabel("Contraseña");
		lblContra.setHorizontalAlignment(SwingConstants.LEFT);
		lblContra.setFont(new Font("Thaoma", Font.PLAIN, 18));
		panelLblContra.add(lblContra, BorderLayout.WEST);

		JPasswordField textContrasenia = new JPasswordField();
		textContrasenia.setBackground(Color.WHITE);
		textContrasenia.setForeground(Color.BLACK);
		textContrasenia.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panelTextContra.add(textContrasenia);

		JButton botonLogin = new JButton("Ingresar");
		botonLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String claveAux = "";
				for (char c : textContrasenia.getPassword()) {
					claveAux += c;
				}
				if (claveAux.equalsIgnoreCase(retornaClaveProfesor())) {
					PanelMayorProfesor.removeAll();
					PanelMayorProfesor.add(MenuProfesor());
					PanelMayorProfesor.updateUI();
					pack();
					setLocationRelativeTo(null);
				} else {
					JOptionPane.showMessageDialog(null, "Rut o Contraseña Incorrecto");
				}
			}
		});
		botonLogin.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panelBotonLogin.add(botonLogin);

		JButton btnResetContra = new JButton("Recuperar Contraseña");
		btnResetContra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				new Email("profesores");
			}
		});

		btnResetContra.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnResetContra.setBackground(new Color(23, 32, 48));
		btnResetContra.setBorderPainted(false);
		panelLblReset.add(btnResetContra, BorderLayout.CENTER);

		return panelContenedorInicioSesion;
	}

	/**
	 * Menu donde se pueden crear examenes, revisar notas de los estudiantes, enviar
	 * correos.
	 * 
	 * @return JPanel
	 * 
	 * 
	 */
	public JPanel MenuProfesor() {

		JPanel panelContenedorMenu = new JPanel(new BorderLayout());
		JPanel panelContenedorIzquierdo = new JPanel(new BorderLayout());
		panelContenedorIzquierdo.setBorder(new EmptyBorder(20, 20, 10, 10));
		JPanel panelContenedorDerecho = new JPanel(new BorderLayout());

		panelContenedorMenu.add(panelContenedorIzquierdo, BorderLayout.WEST);
		panelContenedorMenu.add(panelContenedorDerecho, BorderLayout.CENTER);

		JPanel panelBorde = new JPanel(new BorderLayout());
		panelBorde.setBorder(BorderFactory.createLineBorder(new Color(33, 48, 71), 2, true));
		panelContenedorIzquierdo.add(panelBorde, BorderLayout.CENTER);
		JPanel panelImagen = new JPanel();
		panelImagen.setBorder(new EmptyBorder(10, 10, 10, 10));
		panelImagen.setLayout(new BoxLayout(panelImagen, BoxLayout.Y_AXIS));
		panelBorde.add(panelImagen, BorderLayout.CENTER);

		JPanel panelInternoCentrarImagen = new JPanel(new BorderLayout());
		panelImagen.add(Box.createRigidArea(new Dimension(0, 160)));
		panelImagen.add(panelInternoCentrarImagen);

		JMenuBar barraOpciones = new JMenuBar();
		panelContenedorMenu.add(barraOpciones, BorderLayout.NORTH);
		JMenu menuOpciones = new JMenu("Opciones");
		menuOpciones.setFont(new Font("Tahoma", Font.PLAIN, 15));
		barraOpciones.add(menuOpciones);
		JMenu menuCreacion = new JMenu("Creación");
		menuCreacion.setFont(new Font("Tahoma", Font.PLAIN, 15));
		barraOpciones.add(menuCreacion);
		JMenuItem VolverOpcion = new JMenuItem("Volver");
		VolverOpcion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				new ProfesorGUI();
			}
		});
		JMenuItem SalirOpcion = new JMenuItem("Salir");
		SalirOpcion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		VolverOpcion.setFont(new Font("Tahoma", Font.PLAIN, 15));
		SalirOpcion.setFont(new Font("Tahoma", Font.PLAIN, 15));
		menuOpciones.add(VolverOpcion);
		menuOpciones.add(SalirOpcion);
		JMenuItem CrearExamOpcion = new JMenuItem("Crear Exámen");
		CrearExamOpcion.setFont(new Font("Tahoma", Font.PLAIN, 15));
		CrearExamOpcion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (ventanaColocaNombreArchivo()) {
					PanelMayorProfesor.removeAll();
					PanelMayorProfesor.add(new CrearExamen());
					PanelMayorProfesor.updateUI();
					pack();
					setLocationRelativeTo(null);
				}
			}
		});
		menuCreacion.add(CrearExamOpcion);

		JLabel lblImagenProfesor = new JLabel();
		String ubicacionImagen = "";
		try {

			ubicacionImagen = new File(".").getCanonicalPath() + "\\res\\profesor.png";
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		ImageIcon imagenAdmin = new ImageIcon(ubicacionImagen);
		lblImagenProfesor
				.setIcon(new ImageIcon(imagenAdmin.getImage().getScaledInstance(210, 200, Image.SCALE_DEFAULT)));
		panelInternoCentrarImagen.add(lblImagenProfesor, BorderLayout.CENTER);

		JLabel labelInfo = new JLabel();
		labelInfo.setText("<html><font color=\"white\">Profesor: </font>" + getProfesorName() + "</html>");
		labelInfo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		labelInfo.setForeground(new Color(233, 216, 26));
		labelInfo.setHorizontalAlignment(SwingConstants.CENTER);
		panelInternoCentrarImagen.add(labelInfo, BorderLayout.SOUTH);
		panelImagen.add(Box.createRigidArea(new Dimension(0, 180)));

		JTabbedPane panelControladorProfesor = new JTabbedPane();
		panelContenedorDerecho.add(panelControladorProfesor, BorderLayout.CENTER);

		JPanel panelCreacionExamen = new JPanel(new BorderLayout());
		panelCreacionExamen.setBorder(new EmptyBorder(10, 10, 10, 10));
		panelControladorProfesor.addTab("Examenes", panelCreacionExamen);

		JPanel panelNotasExamen = new JPanel(new BorderLayout());
		panelNotasExamen.setBorder(new EmptyBorder(10, 10, 10, 10));
		panelControladorProfesor.addTab("Notas", panelNotasExamen);

		JPanel panelArbolCarpetasCreaExamen = new JPanel(new BorderLayout(0, 10));
		panelArbolCarpetasCreaExamen.setBorder(new EmptyBorder(10, 10, 10, 0));

		try {
			ubicacionArchivos = new File(".").getCanonicalPath() + "\\Profesor\\Examenes\\Examenes "
					+ getProfesorName();
			if (!new File(ubicacionArchivos).exists()) {
				new File(ubicacionArchivos).mkdirs();
			}

			if (!new File(new File(".").getCanonicalPath() + "\\Profesor\\Notas\\Notas " + getProfesorName())
					.exists()) {
				new File(new File(".").getCanonicalPath() + "\\Profesor\\Notas\\Notas " + getProfesorName()).mkdirs();
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		JTree arbolCarpetas = new JTree();
		Modelo.FileSystemModel modelo = new FileSystemModel(new File(ubicacionArchivos));
		arbolCarpetas.setModel(modelo);

		JScrollPane scrollArbol = new JScrollPane(arbolCarpetas);
		panelCreacionExamen.add(scrollArbol, BorderLayout.CENTER);

		tablaDeExamenesNotas = new JTable();
		tablaDeExamenesNotas.setModel(encabezado());
		llenaTablaDeExamenes(tablaDeExamenesNotas, ubicacionArchivos);
		tablaDeExamenesNotas.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					fila = tablaDeExamenesNotas.getSelectedRow();
					PanelMayorProfesor.removeAll();
					PanelMayorProfesor.add(tablaResultadosDeExamen());
					PanelMayorProfesor.updateUI();
					setLocationRelativeTo(null);

				}
			}
		});
		tablaDeExamenesNotas.getColumnModel().getColumn(0).setPreferredWidth(300);
		tablaDeExamenesNotas.getColumnModel().getColumn(1).setPreferredWidth(100);
		tablaDeExamenesNotas.getTableHeader().setReorderingAllowed(false);
		tablaDeExamenesNotas.getTableHeader().setResizingAllowed(false);
		JScrollPane scrollTablaNota = new JScrollPane(tablaDeExamenesNotas);
		panelNotasExamen.add(scrollTablaNota, BorderLayout.CENTER);

		return panelContenedorMenu;
	}

	/**
	 * Encabezado por defecto de la tabla del detalle de los estudiantes.
	 *
	 * @return DefaultTableModel
	 * 
	 * 
	 */
	DefaultTableModel encabezado() {
		String[] rubrosTablaProf = { "Nombre", "P1", "P2", "P3", "P4", "P5", "Nota" };
		DefaultTableModel tablaPorDefecto = new DefaultTableModel(rubrosTablaProf, 0);
		return tablaPorDefecto;
	}

	/**
	 * Tabla donde se muestran los resultados de todos los alumnos que dieron la
	 * prueba.
	 * 
	 * @return JPanel
	 * 
	 * 
	 */
	public JPanel tablaResultadosDeExamen() {
		JTable tablaNotas = new JTable();
		// tablaNotas.setPreferredSize(new Dimension(20, 100));
		tablaNotas.setModel(encabezado());
		tablaNotas.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					try {
						new muestraDatosAlumno(tablaNotas, tablaNotas.getSelectedRow(), new File(".").getCanonicalPath()
								+ "\\Profesor\\Notas\\Notas " + getProfesorName() + "\\Notas Examen " + nombreExamen);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		JPanel panelMaestroTabla = new JPanel(new BorderLayout(0, 10));

		JPanel panelMenu = new JPanel(new BorderLayout());
		panelMaestroTabla.add(panelMenu, BorderLayout.NORTH);

		JPanel panelTabla = new JPanel(new BorderLayout(0, 10));
		panelTabla.setBorder(new EmptyBorder(0, 20, 20, 20));
		panelMaestroTabla.add(panelTabla, BorderLayout.CENTER);

		JMenuBar menuOpciones = new JMenuBar();
		panelMenu.add(menuOpciones, BorderLayout.CENTER);
		JMenu opciones = new JMenu("Opciones");
		opciones.setFont(new Font("Tahoma", Font.PLAIN, 15));
		menuOpciones.add(opciones);
		JMenu graficar = new JMenu("Graficar");
		graficar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		menuOpciones.add(graficar);
		JMenuItem opcionVolver = new JMenuItem("Volver");
		opcionVolver.setFont(new Font("Tahoma", Font.PLAIN, 15));
		opcionVolver.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				PanelMayorProfesor.removeAll();
				PanelMayorProfesor.add(MenuProfesor());
				PanelMayorProfesor.updateUI();
				pack();
				setLocationRelativeTo(null);
			}
		});
		opciones.add(opcionVolver);
		JMenuItem opcionGraficar = new JMenuItem("<html>Graficar resultados<br>del examen</html>");
		opcionGraficar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		opcionGraficar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (tablaNotas != null) {
					if (tablaNotas.getRowCount() > 0) {
						panelMaestroTabla.add(graficarResultados(tablaNotas), BorderLayout.EAST);
						panelMaestroTabla.updateUI();
						pack();
						setLocationRelativeTo(null);
					} else {
						JOptionPane.showMessageDialog(null, "No hay nada que graficar");
					}

				}
			}
		});
		graficar.add(opcionGraficar);

		JSONaExamen conversor = new JSONaExamen();
		File archivos = new File(ubicacionArchivos);
		String[] archivoElegido = archivos.list();

		Exam examen = new Exam();
		try {
			examen = conversor.generaExamen(new File(ubicacionArchivos + "\\" + archivoElegido[fila]));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		nombreExamen = archivoElegido[fila].substring(0, archivoElegido[fila].lastIndexOf("."));

		String ubicacion = "";
		try {
			ubicacion = new File(".").getCanonicalPath() + "\\Profesor\\Notas\\Notas " + getProfesorName()
					+ "\\Notas Examen " + nombreExamen;
		} catch (IOException e) {
			e.printStackTrace();
		}
		llenaTablaDeAlumnos(tablaNotas, ubicacion, examen);
		tablaNotas.getTableHeader().setReorderingAllowed(false);
		tablaNotas.getTableHeader().setResizingAllowed(false);
		JScrollPane scrolltabla = new JScrollPane(tablaNotas, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tablaNotas.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		panelTabla.add(scrolltabla);

		JLabel labelInformacion = new JLabel();
		labelInformacion.setText("<html><font color=\"white\">Notas del Examen: </font>" + nombreExamen + "</html>");
		labelInformacion.setFont(new Font("Tahoma", Font.PLAIN, 15));
		labelInformacion.setForeground(new Color(233, 26, 158));
		labelInformacion.setHorizontalAlignment(SwingConstants.LEFT);
		panelTabla.add(labelInformacion, BorderLayout.NORTH);

		return panelMaestroTabla;
	}

	/**
	 * Este metodo permite graficar los resultados de los alumnos
	 * 
	 * @param tabla 
	 * @return JPanel
	 * 
	 * 
	 */
	public JPanel graficarResultados(JTable tabla) {
		if (tabla != null) {
			JPanel panelGrafica = new JPanel(new BorderLayout());
			int filas = tabla.getRowCount();
			int columnaDeLaNota = tabla.getColumnCount();
			DefaultCategoryDataset data = new DefaultCategoryDataset();

			for (int i = 0; i < filas; i++) {
				String[] arr = String.valueOf(tabla.getValueAt(i, columnaDeLaNota - 1)).split("/");

				int porcentaje = (int) ((double) Integer.valueOf(arr[0]) / (Integer.valueOf(arr[1])) * 100);
				data.addValue(porcentaje, "Estudiante", String.valueOf(tabla.getValueAt(i, 0)));
			}

			JFreeChart grafica = ChartFactory.createBarChart3D("Grafico de notas", "Alumnos", "% de nota", data,
					PlotOrientation.VERTICAL, false, true, false);
			CategoryPlot plot = (CategoryPlot) grafica.getPlot();
			NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
			rangeAxis.setRange(0, 100);

			ChartPanel contenedor = new ChartPanel(grafica);

			panelGrafica.add(contenedor, BorderLayout.CENTER);

			return panelGrafica;
		} else {
			throw new IllegalArgumentException("La tabla es luna");
		}
	}

	/**
	 * Esta funcion rellena la tabla de las notas con cada nota de los alumnos.
	 * 
	 *
	 * 
	 * @param tabla
	 * @param ubicacion
	 * @param examen
	 * 
	 * 
	 */
	public void llenaTablaDeAlumnos(JTable tabla, String ubicacion, Exam examen) {
		if (tabla != null && ubicacion != null && examen != null) {
			JSONaExamen revision = new JSONaExamen();
			ModeloNotasAlumno alumno = new ModeloNotasAlumno();

			ArrayList<String> nombresColumna = new ArrayList<>();
			nombresColumna.add("Nombre");
			for (int i = 0; i < examen.getTotalPreguntas(); i++) {
				nombresColumna.add("Pregunta" + (i + 1));
			}
			nombresColumna.add("Puntaje");
			DefaultTableModel modelo = new DefaultTableModel(nombresColumna.toArray(new String[0]), 0) {
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			tabla.setModel(modelo);

			Modelo.FileSystemModel ubicacionArchivos = new FileSystemModel(new File(ubicacion));
			File archivos = new File(ubicacion);
			String[] totalArchivos = archivos.list();
			if (totalArchivos != null) {
				Object[] fila = new Object[examen.getTotalPreguntas() + 2];
				for (int i = 0; i < totalArchivos.length; i++) {
					fila[0] = ubicacionArchivos.getNombreArchivos(archivos, i).substring(0,
							ubicacionArchivos.getNombreArchivos(archivos, i).lastIndexOf("."));
					try {
						alumno = revision.generaResultadoExam(
								new File(ubicacion + "\\" + ubicacionArchivos.getNombreArchivos(archivos, i)));
						int[] arr = alumno.getPuntajeAlumno();
						for (int j = 1; j <= examen.getTotalPreguntas() + 1; j++) {
							if (j == examen.getTotalPreguntas() + 1) {
								fila[j] = Integer.toString(alumno.getPuntajeTotalAlumno()) + "/"
										+ Integer.toString(alumno.getPuntajeTotalExam());
								break;
							}
							fila[j] = Integer.toString(arr[j - 1]);
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					modelo.addRow(fila);
				}
				tabla.getColumnModel().getColumn(0).setPreferredWidth(150);
				for (int i = 1; i <= examen.getTotalPreguntas(); i++) {
					tabla.getColumnModel().getColumn(i).setPreferredWidth(100);
				}
				tabla.getColumnModel().getColumn(tabla.getColumnCount() - 1).setPreferredWidth(100);
			}
		} else {
			throw new IllegalArgumentException("Parametros no pueden ser nulos");
		}
	}

	/**
	 * Esta clse permite mostrar detalladamente las respuestas de cada alumno.
	 *
	 * 
	 * @author Anton
	 * 
	 * 
	 */
	public class muestraDatosAlumno extends JFrame {

		JSONaExamen revision = new JSONaExamen();
		ModeloNotasAlumno alumno = new ModeloNotasAlumno();

		JPanel panelData;

		/**
		 * Este constructor inicializa los componentes para poder mostrar la tabla de
		 * los detalles.
		 *
		 * 
		 * @param tabla
		 * @param fila
		 * @param ubicacion
		 * 
		 * 
		 * 
		 */
		public muestraDatosAlumno(JTable tabla, int fila, String ubicacion) {
			
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			Modelo.FileSystemModel ubicacionArchivos = new FileSystemModel(new File(ubicacion));
			panelData = new JPanel(new BorderLayout());
			setContentPane(panelData);
			panelData.add(tabla(tabla, fila, ubicacionArchivos, ubicacion));
			pack();
			setResizable(false);
			setLocationRelativeTo(null);
			setVisible(true);

		}

		/**
		 * Este metodo retorna una tabla con el detalle especifico de las respuestas que
		 * el estudiante envió.
		 *
		 * @param tabla
		 * @param fila
		 * @param ubicacionArchivos
		 * @param ubicacion
		 * @return JTable
		 * 
		 * 
		 * 
		 */
		JPanel tabla(JTable tabla, int fila, FileSystemModel ubicacionArchivos, String ubicacion) {

			JPanel tablaAlumno = new JPanel(new BorderLayout(0, 10));
			tablaAlumno.setPreferredSize(new Dimension(800, 190));
			tablaAlumno.setBorder(new EmptyBorder(20, 20, 20, 20));
			JPanel panelNombreAlumno = new JPanel(new BorderLayout());
			tablaAlumno.add(panelNombreAlumno, BorderLayout.NORTH);
			JPanel panelTabla = new JPanel(new BorderLayout());
			tablaAlumno.add(panelTabla, BorderLayout.CENTER);

			JLabel nombreAlumno = new JLabel();
			nombreAlumno.setText("<html><font color=\"white\">Alumno: </font>"+ String.valueOf(tabla.getValueAt(fila, 0)) + "</html>");
			setTitle("Respuestas "+String.valueOf(tabla.getValueAt(fila, 0)));
			try {
				Image imagenLogo = ImageIO.read(new File(new File(".").getCanonicalPath() + "\\res\\soft.png"));
				setIconImage(imagenLogo);
			} catch (Exception e) {

			}
			nombreAlumno.setFont(new Font("Tahoma", Font.PLAIN, 16));
			nombreAlumno.setHorizontalAlignment(SwingConstants.LEFT);
			nombreAlumno.setForeground(new Color(62, 208, 162));
			panelNombreAlumno.add(nombreAlumno, BorderLayout.WEST);

			File archivos = new File(ubicacion);
			try {
				alumno = revision.generaResultadoExam(
						new File(ubicacion + "\\" + ubicacionArchivos.getNombreArchivos(archivos, fila)));
				Object[] datos = new Object[alumno.getRespuestaAlumno().length + 1];

				ArrayList<String> columnasTabla = new ArrayList<>();
				columnasTabla.add("----");
				for (int i = 0; i < datos.length - 1; i++) {
					columnasTabla.add("Pregunta" + (i + 1));
				}

				DefaultTableModel modelo = new DefaultTableModel(columnasTabla.toArray(new String[0]), 0) {
					public boolean isCellEditable(int row, int column) {
						return false;
					}
				};

				String[] rubro = { "Rta Correcta", "Rta usuario" };
				for (int i = 0; i < 2; i++) {
					if (i == 0) {
						datos[0] = rubro[i];
					} else {
						datos[0] = rubro[i];
					}
					for (int j = 1; j <= alumno.getRespuestaAlumno().length; j++) {
						if (i == 0) {
							datos[j] = alumno.getRespuestaExamen()[j - 1];
						} else {
							datos[j] = alumno.getRespuestaAlumno()[j - 1];
						}
					}
					modelo.addRow(datos);
				}
				
				StringSimilar comparador = new StringSimilar();
				
				JTable tablaDeElecciones = new JTable() {
					public Component prepareRenderer(TableCellRenderer renderer, int index_row, int index_col) {
						Component comp = super.prepareRenderer(renderer, index_row, index_col);
						if (index_row > 0 && index_col > 0) {
							if (((int) (((double) comparador.similarity(alumno.getRespuestaExamen()[index_col - 1], alumno.getRespuestaAlumno()[index_col - 1])) * 100)) > 68) {
								comp.setForeground(Color.GREEN);
							} else {
								comp.setForeground(Color.RED);
							}
						} else {
							comp.setForeground(Color.WHITE);
						}
						return comp;
					}
				};
				tablaDeElecciones.setModel(modelo);
				tablaDeElecciones.getColumnModel().getColumn(0).setPreferredWidth(120);
				for (int i = 1; i < datos.length; i++) {
					tablaDeElecciones.getColumnModel().getColumn(i).setPreferredWidth(120);
				}
				tablaDeElecciones.getTableHeader().setReorderingAllowed(false);
				tablaDeElecciones.getTableHeader().setResizingAllowed(false);
				JScrollPane scroll = new JScrollPane(tablaDeElecciones, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				tablaDeElecciones.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

				panelTabla.add(scroll, BorderLayout.CENTER);

				String ubicacionImagen = "";
				try {
					ubicacionImagen = new File(".").getCanonicalPath() + "\\res\\gmail.png";
				} catch (Exception e) {
					System.out.println(e.toString());
				}
				ImageIcon imagenGmail = new ImageIcon(ubicacionImagen);
				JButton correoIcono = new JButton();
				correoIcono.setIcon(new ImageIcon(imagenGmail.getImage().getScaledInstance(28, 20, Image.SCALE_DEFAULT)));
				correoIcono.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						
						int respuesta = JOptionPane.showConfirmDialog(null,"Esta seguro de enviar el correo?","Confirmación", JOptionPane.YES_NO_OPTION);
						if(respuesta == JOptionPane.YES_OPTION) {
							Email mail = new Email();
							mail.enviaNotasCorreo(tablaDeElecciones, tabla, fila, String.valueOf(tabla.getValueAt(fila, 0)),
									getCorreoAlumno(String.valueOf(tabla.getValueAt(fila, 0))), nombreExamen);
						}
					}
				});
				panelNombreAlumno.add(correoIcono, BorderLayout.EAST);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return tablaAlumno;
		}
	}

	/**
	 * Este metodo retorna el correo del alumno para poder enviar el detalle del
	 * examen.
	 * 
	 *
	 * 
	 * @param alumno
	 * @return String
	 * 
	 * 
	 */
	public String getCorreoAlumno(String alumno) {
		if (alumno != null) {
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				Conexion objCon = new Conexion();
				Connection conn = objCon.getConnection();
				String[] nombreApellido = alumno.split(" ");
				ps = conn.prepareStatement("SELECT Correo FROM alumnos WHERE (Nombre = ?) AND (Apellido = ?)");
				ps.setString(1, nombreApellido[0]);
				ps.setString(2, nombreApellido[1]);
				rs = ps.executeQuery();
				while (rs.next()) {
					return rs.getString("Correo");
				}
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
		throw new IllegalArgumentException("El alumno no puede ser null");
	}

	/**
	 *  Esta funcion llena la tabla de examenes creados por el
	 *  profesor.
	 * 
	 * 
	 * @param tabla
	 * @param ubicacion
	 * 
	 *                 
	 * 
	 */
	public void llenaTablaDeExamenes(JTable tabla, String ubicacion) {
		if (tabla != null && ubicacion != null) {
			DefaultTableModel modelo = new DefaultTableModel() {
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			FechaDeCreacion fecha = new FechaDeCreacion();
			modelo.addColumn("Nombre");
			modelo.addColumn("Fecha");
			Modelo.FileSystemModel ubicacionArchivos = new FileSystemModel(new File(ubicacion));
			File archivos = new File(ubicacion);
			String[] totalArchivos = archivos.list();
			Object[] fila = new Object[2];
			for (int i = 0; i < totalArchivos.length; i++) {
				fila[0] = ubicacionArchivos.getNombreArchivos(archivos, i).substring(0,
						ubicacionArchivos.getNombreArchivos(archivos, i).lastIndexOf("."));
				fila[1] = fecha.retornaFecha(ubicacion + "\\" + ubicacionArchivos.getNombreArchivos(archivos, i));
				modelo.addRow(fila);
			}
			tabla.setModel(modelo);
		} else {
			throw new IllegalArgumentException("La ubicacion es nula");
		}
	}

	/**
	 * Esta ventana se ejecuta al clikear la opcion "Crear Examen", donde se tiene
	 * que ingresar un nombre para el examen, si no se ingresa.
	 *
	 * 
	 * @return Boolean
	 * 
	 * 
	 * 
	 */
	Boolean ventanaColocaNombreArchivo() {

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(new EmptyBorder(10, 10, 0, 10));

		JPanel panelLabel = new JPanel(new BorderLayout());
		panel.add(panelLabel);
		panel.add(Box.createRigidArea(new Dimension(0, 15)));
		JPanel paneltexto = new JPanel(new BorderLayout());
		panel.add(paneltexto);

		JLabel label = new JLabel("Cree Un Nombre Para Su Examen");
		label.setHorizontalAlignment(JLabel.LEFT);
		label.setFont(new Font("Thaoma", Font.PLAIN, 16));

		JTextField ingresaNombreArchivo = new JTextField();
		ingresaNombreArchivo.setFont(new Font("Thaoma", Font.PLAIN, 18));
		ingresaNombreArchivo.setForeground(Color.BLACK);
		ingresaNombreArchivo.setBackground(Color.WHITE);

		panelLabel.add(label, BorderLayout.WEST);
		paneltexto.add(ingresaNombreArchivo, BorderLayout.CENTER);

		String[] options = new String[] { "Cancelar", "Aceptar" };

		int option = JOptionPane.showOptionDialog(null, panel, "Nombre del Exámen", JOptionPane.NO_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[1]);

		if (ingresaNombreArchivo.getText().isBlank() && option == 1) {
			JOptionPane.showMessageDialog(null, "Nombre no puede estar vacio");

		} else {
			if (option == 1) {
				nombreDelArchivo = ingresaNombreArchivo.getText();
				return true;
			} else {
				return false;
			}
		}

		return false;
	}

	/**
	 * Esta funcion retorna la clave del profesor para poder conpararla con la que
	 * ingresa el usuario
	 * 
	 * @return String
	 * 
	 * 
	 */
	public String retornaClaveProfesor() {
		String clave = "";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			Conexion objCon = new Conexion();
			Connection conn = objCon.getConnection();
			rut = textRut.getText();
			ps = conn.prepareStatement(
					"SELECT Contraseña, CAST(AES_DECRYPT(Contraseña,'profesores')AS CHAR(50)) FROM profesores WHERE Rut = ?");
			ps.setString(1, rut);
			rs = ps.executeQuery();

			while (rs.next()) {
				clave = rs.getString("CAST(AES_DECRYPT(Contraseña,'profesores')AS CHAR(50))");
			}
		} catch (Exception e2) {
			System.err.println(e2.toString());
		}
		return clave;
	}

	/**
	 * Retorna el nombre del profesor en base al rut
	 * 
	 * @return String
	 * 
	 * 
	 */
	public String getProfesorName() {
		String name = new String();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			Conexion objCon = new Conexion();
			Connection conn = objCon.getConnection();
			if (textRut != null) {
				rut = textRut.getText();
			}
			ps = conn.prepareStatement("SELECT Nombre,Apellido FROM profesores WHERE Rut = ?");
			ps.setString(1, rut);
			rs = ps.executeQuery();

			while (rs.next()) {
				name = rs.getString("Nombre") + " " + rs.getString("Apellido");
			}

		} catch (Exception e2) {
			System.err.println(e2.toString());
		}
		return name;
	}

	/**
	 * Hace que en la casilla del rut no se puedan ingresar letras.
	 *
	 * @author Antony
	 * 
	 * 
	 */
	public class noAgregaLetrasRUTField implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			char letraIngresada = arg0.getKeyChar();
			if (!((letraIngresada >= '0') && (letraIngresada <= '9')
					|| ((letraIngresada == '-') || (letraIngresada == 'k') || (letraIngresada == 'K')))
					|| (letraIngresada == KeyEvent.VK_BACK_SPACE) || (letraIngresada == KeyEvent.VK_DELETE)) {
				arg0.consume();
			}

			if (textRut.getText().length() > 9) {
				arg0.consume();
			}

		}

	}

	/**
	 * Esta clase permite crear un examen, setear el puntaje y establecer el tiempo
	 * de duracion.
	 *
	 * @author Antony
	 * 
	 * 
	 */
	class CrearExamen extends JPanel {

		JPanel panelEnunciado;
		JTextPane textEnunciado;
		JTextField textPeso;
		JScrollPane scroll;
		JPanel panelMayorCrearExamen;
		JPanel panelRespuestas;
		JPanel panelAlternativas;
		JPanel panelRdBotones;
		JPanel panelAlternativasInterno;
		ArrayList<JRadioButton> esCorrecta;
		ArrayList<JTextField> respuestasDeLaPregunta;
		ButtonGroup grupoCorrecto;
		JButton botonFinalizar;
		JLabel labelContadorTotalPreguntas;
		JTextField textTiempo;
		JRadioButton activarTiempo;
		boolean activador = false;
		int letra = 65;
		int totalPreguntas = 0;
		JTextPane txtRespuestaCortaPgr;
		Modelo.Exam examenCreado;
		String enunciadoDeLaPregunta;
		int pesoDeLaPregunta;
		Modelo.ConvierteExamenJson jsonArchivo;
		String respuestaCorrectaSelecMul;
		int indexCorrectoSelecMul;
		String[] alternativas;
		boolean respuestaVerdFalso;
		JPanel panelCreador;
		String respuestaRespCorta;
		int formaExamen;
		int contadorPreguntaSelecMul = 0;
		int contadorPreguntaVerdFals = 0;
		int contadorPreguntaRespCort = 0;
		JComboBox<String> formasExamen;

		/**
		 * Constructor de CrearExamen
		 * 
		 * Inicializa todos los componentes para la creacion de examens.
		 */
		public CrearExamen() {

			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			panelMayorCrearExamen = new JPanel();
			panelMayorCrearExamen.setLayout(new BorderLayout(10, 0));
			setContentPane(panelMayorCrearExamen);
			JMenuBar menu = new JMenuBar();
			panelMayorCrearExamen.add(menu, BorderLayout.NORTH);
			JMenu opciones = new JMenu("Opciones");
			opciones.setFont(new Font("Tahoma", Font.PLAIN, 15));
			menu.add(opciones);
			JMenuItem volver = new JMenuItem("Volver");
			volver.setFont(new Font("Tahoma", Font.PLAIN, 15));
			volver.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int i = JOptionPane.showConfirmDialog(null,
							"<html>Se perderan todos los progresos<br>¿Esta seguro de volver?</html>", "Alerta",
							JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
					if (i == 0) {
						panelMayorCrearExamen.removeAll();
						panelMayorCrearExamen.add(MenuProfesor());
						panelMayorCrearExamen.updateUI();
						pack();
						setLocationRelativeTo(null);
					}
				}
			});
			opciones.add(volver);

			panelMayorCrearExamen.add(panelIniciador(), BorderLayout.CENTER);
			setResizable(false);
			pack();
			setLocationRelativeTo(null);
			setVisible(true);
		}

		/**
		 * Esta funcion retorna un JPanel que muestra por pantalla el panel de creacion
		 * del examen.
		 * 
		 * A primera instancia muesta la forma del examen, esto es el orden que seguiran
		 * las preguntas. Posteriormente se preoceden a crear las preguntas del tipo que
		 * el profesor elija, cuantas desee. Tambien se le puede establecer tiempo, si
		 * no lo hace el examen tiene una duracion ilimitada, esto es hasta que se le de
		 * al boton de finalizar.
		 * 
		 * @return JPanel
		 * 
		 * 
		 */
		private JPanel panelIniciador() {
			jsonArchivo = new ConvierteExamenJson();
			examenCreado = new Exam();
			JButton botonCrearSelecMul = new JButton();
			JButton botonVerdaderoFalso = new JButton();
			JButton botonRespuestCorta = new JButton();

			JPanel panelContenedorCreaExamen = new JPanel(new BorderLayout());
			panelContenedorCreaExamen.setBorder(new EmptyBorder(0, 20, 20, 20));

			JPanel panelTitulo = new JPanel(new BorderLayout(30, 30));
			panelTitulo.setBorder(new EmptyBorder(20, 5, 20, 5));
			panelCreador = new JPanel();

			JPanel panelSuperiorCreador = new JPanel(new BorderLayout(200, 0));
			JPanel panelTiempo = new JPanel(new BorderLayout(5, 0));
			panelTiempo.setBorder(new EmptyBorder(0, 0, 0, 0));
			panelSuperiorCreador.add(panelTiempo, BorderLayout.WEST);
			panelTitulo.add(panelSuperiorCreador, BorderLayout.SOUTH);

			JPanel panelContadorTotalPreguntas = new JPanel(new BorderLayout());
			panelSuperiorCreador.add(panelContadorTotalPreguntas, BorderLayout.EAST);
			labelContadorTotalPreguntas = new JLabel("Total preguntas: " + totalPreguntas);
			labelContadorTotalPreguntas
					.setText("<html><font color=\"white\">Total preguntas: </font>" + totalPreguntas + "</html>");
			labelContadorTotalPreguntas.setFont(new Font("Tahoma", Font.PLAIN, 18));
			labelContadorTotalPreguntas.setForeground(new Color(214, 194, 21));
			panelContadorTotalPreguntas.add(labelContadorTotalPreguntas, BorderLayout.CENTER);

			JPanel panelFormaExam = new JPanel(new BorderLayout());
			panelSuperiorCreador.add(panelFormaExam, BorderLayout.CENTER);

			JPanel panelBotonesCrearPreguntas = new JPanel(new BorderLayout());
			panelBotonesCrearPreguntas.setBorder(new EmptyBorder(0, 50, 0, 50));
			panelBotonesCrearPreguntas.setBorder(BorderFactory.createLineBorder(new Color(33, 48, 71), 2, true));

			JPanel panelLabelBotones = new JPanel(new BorderLayout());
			panelLabelBotones.setBorder(new EmptyBorder(10, 10, 10, 10));
			panelBotonesCrearPreguntas.add(panelLabelBotones, BorderLayout.NORTH);

			JPanel panelBotonesCreador = new JPanel(new GridLayout(0, 3, 12, 12));
			panelBotonesCreador.setBorder(new EmptyBorder(5, 50, 5, 50));
			panelBotonesCrearPreguntas.add(panelBotonesCreador, BorderLayout.SOUTH);

			panelContenedorCreaExamen.add(panelTitulo, BorderLayout.NORTH);
			panelContenedorCreaExamen.add(panelCreador, BorderLayout.CENTER);
			panelContenedorCreaExamen.add(panelBotonesCrearPreguntas, BorderLayout.SOUTH);

			JLabel labelForma = new JLabel("Forma del Examen: ");
			labelForma.setFont(new Font("Tahoma", Font.PLAIN, 18));
			labelForma.setHorizontalAlignment(SwingConstants.RIGHT);
			panelFormaExam.add(labelForma, BorderLayout.WEST);

			formasExamen = new JComboBox<>();
			formasExamen.setFont(new Font("Tahoma", Font.PLAIN, 18));
			formasExamen.addItem("Seleccione");
			formasExamen.addItem("SM-VF-RC");
			formasExamen.addItem("VF-SM-RC");
			formasExamen.addItem("RC-VF-SM");
			formasExamen.addItem("Sele.Mult.");
			formasExamen.addItem("Verd.Fals.");
			formasExamen.addItem("Resp.Cort.");
			formasExamen.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (formasExamen.getSelectedIndex() == 0) {
						botonCrearSelecMul.setEnabled(false);
						botonVerdaderoFalso.setEnabled(false);
						botonRespuestCorta.setEnabled(false);
						panelCreador.removeAll();
						pack();
						setLocationRelativeTo(null);
					} else if (formasExamen.getSelectedIndex() == 1 || formasExamen.getSelectedIndex() == 2
							|| formasExamen.getSelectedIndex() == 3) {
						if (botonCrearSelecMul != null && botonVerdaderoFalso != null && botonRespuestCorta != null) {
							botonCrearSelecMul.setEnabled(true);
							botonVerdaderoFalso.setEnabled(true);
							botonRespuestCorta.setEnabled(true);
						}
					} else if (formasExamen.getSelectedIndex() == 4) {
						if (botonCrearSelecMul != null) {
							botonCrearSelecMul.setEnabled(true);
							botonVerdaderoFalso.setEnabled(false);
							botonRespuestCorta.setEnabled(false);
							panelBotonesCrearPreguntas.setBorder(null);
							agregaOtraPregunta(panelCreador, panelCreadorSeleccionMultiple());
						}
					} else if (formasExamen.getSelectedIndex() == 5) {
						if (botonVerdaderoFalso != null) {
							botonCrearSelecMul.setEnabled(false);
							botonVerdaderoFalso.setEnabled(true);
							botonRespuestCorta.setEnabled(false);
							panelBotonesCrearPreguntas.setBorder(null);
							agregaOtraPregunta(panelCreador, panelCreadorVerdaderoFalso());
						}
					} else if (formasExamen.getSelectedIndex() == 6) {
						if (botonRespuestCorta != null) {
							botonCrearSelecMul.setEnabled(false);
							botonVerdaderoFalso.setEnabled(false);
							botonRespuestCorta.setEnabled(true);
							panelBotonesCrearPreguntas.setBorder(null);
							agregaOtraPregunta(panelCreador, panelCreadorRespuestaCorta());
						}
					}
				}
			});

			panelFormaExam.add(formasExamen, BorderLayout.CENTER);

			activarTiempo = new JRadioButton();
			activarTiempo.addActionListener(new botonActivarTiempo());
			panelTiempo.add(activarTiempo, BorderLayout.WEST);

			JLabel labelTiempo = new JLabel("Tiempo:");
			labelTiempo.setFont(new Font("Tahoma", Font.PLAIN, 18));
			panelTiempo.add(labelTiempo, BorderLayout.CENTER);

			textTiempo = new JTextField();
			textTiempo.setEnabled(false);
			textTiempo.setFont(new Font("Tahoma", Font.PLAIN, 15));
			textTiempo.setBackground(new Color(23, 32, 48));
			textTiempo.addKeyListener(new noAgregaLetrasField());
			panelTiempo.add(textTiempo, BorderLayout.EAST);

			JLabel label = new JLabel("Creador de Examenes: " + nombreDelArchivo);
			label.setText("<html><font color=\"white\">Creador de Examenes: </font>" + nombreDelArchivo + "</html>");
			label.setFont(new Font("Tahoma", Font.PLAIN, 20));
			label.setForeground(new Color(22, 215, 17));
			panelTitulo.add(label, BorderLayout.WEST);

			botonFinalizar = new JButton("Finalizar");
			botonFinalizar.setFont(new Font("Tahoma", Font.PLAIN, 15));
			botonFinalizar.setEnabled(false);
			botonFinalizar.addActionListener(new botonFinalizarCreacionExamen());
			panelTitulo.add(botonFinalizar, BorderLayout.EAST);

			JLabel labelBotones = new JLabel("Agregar pregunta");
			labelBotones.setHorizontalAlignment(SwingConstants.CENTER);
			labelBotones.setFont(new Font("Tahoma", Font.PLAIN, 20));
			panelLabelBotones.add(labelBotones, BorderLayout.CENTER);

			botonCrearSelecMul.setText("Seleccion Multiple");
			botonCrearSelecMul.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					panelBotonesCrearPreguntas.setBorder(null);
					if (CamposVacios()) {
						letra = 65;
						agregaOtraPregunta(panelCreador, panelCreadorSeleccionMultiple());
						panelCreador.updateUI();
						setLocationRelativeTo(null);
					} else {
						JOptionPane.showMessageDialog(null, "Tiene Cambios Sin Guardar");
					}

				}
			});
			botonCrearSelecMul.setFont(new Font("Tahoma", Font.PLAIN, 16));
			botonCrearSelecMul.setEnabled(false);
			panelBotonesCreador.add(botonCrearSelecMul);

			botonVerdaderoFalso.setText("Verdadero o Falso");
			botonVerdaderoFalso.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					panelBotonesCrearPreguntas.setBorder(null);
					if (CamposVacios()) {
						agregaOtraPregunta(panelCreador, panelCreadorVerdaderoFalso());
						setLocationRelativeTo(null);
					} else {
						JOptionPane.showMessageDialog(null, "Tiene Cambios Sin Guardar");
					}
				}
			});
			botonVerdaderoFalso.setFont(new Font("Tahoma", Font.PLAIN, 16));
			botonVerdaderoFalso.setEnabled(false);
			panelBotonesCreador.add(botonVerdaderoFalso);

			botonRespuestCorta.setText("Respuesta Corta");
			botonRespuestCorta.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					panelBotonesCrearPreguntas.setBorder(null);
					if (CamposVacios()) {
						agregaOtraPregunta(panelCreador, panelCreadorRespuestaCorta());
						setLocationRelativeTo(null);
					} else {
						JOptionPane.showMessageDialog(null, "Tiene Cambios Sin Guardar");
					}
				}
			});
			botonRespuestCorta.setFont(new Font("Tahoma", Font.PLAIN, 16));
			botonRespuestCorta.setEnabled(false);
			panelBotonesCreador.add(botonRespuestCorta);

			return panelContenedorCreaExamen;
		}

		/**
		 * Retorna una panel con todos los componentes que tiene una pregunta de
		 * seleccion multiple
		 * 
		 * @return JPanel
		 * 
		 * 
		 * 
		 */
		private JPanel panelCreadorSeleccionMultiple() {

			grupoCorrecto = new ButtonGroup();
			esCorrecta = new ArrayList<>();
			respuestasDeLaPregunta = new ArrayList<>();

			JPanel panelCreadorSelecMulti = new JPanel(new BorderLayout());
			panelCreadorSelecMulti.setBorder(BorderFactory.createLineBorder(new Color(33, 48, 71), 2, true));
			panelCreadorSelecMulti.setLayout(new BoxLayout(panelCreadorSelecMulti, BoxLayout.Y_AXIS));

			/************************* PANEL DEL ENUNCIADO ******************************/
			panelEnunciado = new JPanel(new BorderLayout(20, 10));
			panelEnunciado.setBorder(new EmptyBorder(20, 20, 20, 20));
			panelCreadorSelecMulti.add(panelEnunciado, BorderLayout.NORTH);

			JPanel panelEnunciadoSuperior = new JPanel(new BorderLayout());
			panelEnunciado.add(panelEnunciadoSuperior, BorderLayout.NORTH);

			JLabel lblAyudaEnunciado = new JLabel("Enunciado de la pregunta:");
			lblAyudaEnunciado.setFont(new Font("Tahoma", Font.PLAIN, 20));
			panelEnunciadoSuperior.add(lblAyudaEnunciado, BorderLayout.WEST);

			JLabel lblPeso = new JLabel("Puntaje : ");
			lblPeso.setFont(new Font("Tahoma", Font.PLAIN, 20));
			lblPeso.setHorizontalAlignment(SwingConstants.RIGHT);
			panelEnunciadoSuperior.add(lblPeso, BorderLayout.CENTER);
			textPeso = new JTextField();
			textPeso.setBackground(Color.WHITE);
			textPeso.setForeground(Color.BLACK);
			textPeso.setFont(new Font("Tahoma", Font.PLAIN, 15));
			textPeso.addKeyListener(new noAgregaLetrasField());
			panelEnunciadoSuperior.add(textPeso, BorderLayout.EAST);

			textEnunciado = new JTextPane();
			textEnunciado.setBackground(Color.WHITE);
			textEnunciado.setForeground(Color.BLACK);
			textEnunciado.setFont(new Font("Tahoma", Font.PLAIN, 20));

			scroll = new JScrollPane();
			scroll.setViewportView(textEnunciado);
			textEnunciado.setPreferredSize(new Dimension(900, 100));
			panelEnunciado.add(scroll, BorderLayout.CENTER);
			/***************************************************************************/

			/************************* PANEL DE LA RESPUESTA ***************************/

			panelRespuestas = new JPanel(new BorderLayout(20, 10));
			panelRespuestas.setBorder(new EmptyBorder(20, 20, 20, 20));
			panelCreadorSelecMulti.add(panelRespuestas, BorderLayout.SOUTH);

			JLabel lblAyudaRespuestas = new JLabel("Ingrese las respuestas: ");
			lblAyudaRespuestas.setFont(new Font("Tahoma", Font.PLAIN, 20));
			panelRespuestas.add(lblAyudaRespuestas, BorderLayout.NORTH);

			panelAlternativas = new JPanel();
			panelAlternativas.setLayout(new BoxLayout(panelAlternativas, BoxLayout.Y_AXIS));
			panelAlternativas.setPreferredSize(new Dimension(10, 300));

			JPanel panelCentralAlternativas = new JPanel(new BorderLayout());
			panelRespuestas.add(panelCentralAlternativas, BorderLayout.CENTER);

			JScrollPane scrllBotones = new JScrollPane();
			scrllBotones.setPreferredSize(new Dimension(20, 200));

			panelCentralAlternativas.add(scrllBotones, BorderLayout.CENTER);

			panelRdBotones = new JPanel();
			panelRdBotones.setBorder(new EmptyBorder(20, 20, 20, 20));
			panelRdBotones.setLayout(new BoxLayout(panelRdBotones, BoxLayout.Y_AXIS));
			scrllBotones.setViewportView(panelRdBotones);
			agregaAlternativa(grupoCorrecto);
			agregaAlternativa(grupoCorrecto);
			agregaAlternativa(grupoCorrecto);
			JButton botonAgregaMasAlternativas = new JButton("Agregar Alternativa");// estoy aca arreglando
			botonAgregaMasAlternativas.addActionListener(new btnAgregaMasAlternativas());

			JButton botonGuardar = new JButton("Guardar Pregunta");
			botonGuardar.addActionListener(new botonGuardarSelMulListener());

			JButton botonLimpia = new JButton("Limpiar casillas");
			botonLimpia.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					letra = 65;
					panelCreador.removeAll();
					panelCreador.add(panelCreadorSeleccionMultiple());
					panelCreador.updateUI();
				}
			});

			JPanel panelBotonesSelecMul = new JPanel(new BorderLayout(250, 0));
			panelRespuestas.add(panelBotonesSelecMul, BorderLayout.SOUTH);

			panelBotonesSelecMul.add(botonAgregaMasAlternativas, BorderLayout.WEST);
			panelBotonesSelecMul.add(botonLimpia, BorderLayout.CENTER);
			panelBotonesSelecMul.add(botonGuardar, BorderLayout.EAST);

			/***************************************************************************/

			return panelCreadorSelecMulti;
		}

		/**
		 * Retorna un panel con todos los componentes que posee una pregunta de
		 * Verdadero o Falso.
		 * 
		 * @return JPanel
		 * 
		 *
		 */
		private JPanel panelCreadorVerdaderoFalso() {
			grupoCorrecto = new ButtonGroup();

			JPanel panelCreadorVerdaderoFalso = new JPanel(new BorderLayout());
			panelCreadorVerdaderoFalso.setBorder(BorderFactory.createLineBorder(new Color(33, 48, 71), 2, true));
			panelCreadorVerdaderoFalso.setLayout(new BoxLayout(panelCreadorVerdaderoFalso, BoxLayout.Y_AXIS));

			/************************* PANEL DEL ENUNCIADO ******************************/
			panelEnunciado = new JPanel(new BorderLayout(20, 10));
			panelEnunciado.setBorder(new EmptyBorder(20, 20, 20, 20));
			panelCreadorVerdaderoFalso.add(panelEnunciado, BorderLayout.NORTH);

			JPanel panelEnunciadoSuperior = new JPanel(new BorderLayout());
			panelEnunciado.add(panelEnunciadoSuperior, BorderLayout.NORTH);

			JLabel lblAyudaEnunciado = new JLabel("Enunciado de la pregunta:");
			lblAyudaEnunciado.setFont(new Font("Tahoma", Font.PLAIN, 20));
			panelEnunciadoSuperior.add(lblAyudaEnunciado, BorderLayout.WEST);

			JLabel lblPeso = new JLabel("Puntaje : ");
			lblPeso.setFont(new Font("Tahoma", Font.PLAIN, 20));
			lblPeso.setHorizontalAlignment(SwingConstants.RIGHT);
			panelEnunciadoSuperior.add(lblPeso, BorderLayout.CENTER);
			textPeso = new JTextField();
			textPeso.setBackground(Color.WHITE);
			textPeso.setForeground(Color.BLACK);
			textPeso.setFont(new Font("Tahoma", Font.PLAIN, 15));
			textPeso.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					char letraIngresada = e.getKeyChar();
					if (!((letraIngresada >= '0') && (letraIngresada <= '9'))
							|| (letraIngresada == KeyEvent.VK_BACK_SPACE) || (letraIngresada == KeyEvent.VK_DELETE)) {
						e.consume();
					}
				}
			});
			panelEnunciadoSuperior.add(textPeso, BorderLayout.EAST);

			textEnunciado = new JTextPane();
			textEnunciado.setBackground(Color.WHITE);
			textEnunciado.setForeground(Color.BLACK);
			textEnunciado.setFont(new Font("Tahoma", Font.PLAIN, 20));

			scroll = new JScrollPane();
			scroll.setViewportView(textEnunciado);
			textEnunciado.setPreferredSize(new Dimension(900, 100));
			panelEnunciado.add(scroll, BorderLayout.CENTER);
			/***************************************************************************/

			/************************* PANEL DE LA RESPUESTA ***************************/

			panelRespuestas = new JPanel(new BorderLayout(20, 10));
			panelRespuestas.setBorder(new EmptyBorder(20, 20, 20, 20));
			panelCreadorVerdaderoFalso.add(panelRespuestas, BorderLayout.SOUTH);

			JLabel lblAyudaRespuestas = new JLabel("Respuesta: ");
			lblAyudaRespuestas.setFont(new Font("Tahoma", Font.PLAIN, 20));
			panelRespuestas.add(lblAyudaRespuestas, BorderLayout.NORTH);

			JPanel panelCentralVerdaFalso = new JPanel(new GridLayout(0, 2, 10, 10));
			panelCentralVerdaFalso.setBorder(new EmptyBorder(30, 30, 30, 30));
			panelRespuestas.add(panelCentralVerdaFalso, BorderLayout.CENTER);

			JRadioButton rdbVerdadero = new JRadioButton("Verdadero");
			rdbVerdadero.setFont(new Font("Tahoma", Font.PLAIN, 20));
			rdbVerdadero.setHorizontalAlignment(SwingConstants.CENTER);
			rdbVerdadero.setActionCommand("Verdadero");
			grupoCorrecto.add(rdbVerdadero);
			panelCentralVerdaFalso.add(rdbVerdadero);

			JRadioButton rdbFalso = new JRadioButton("Falso");
			rdbFalso.setFont(new Font("Tahoma", Font.PLAIN, 20));
			rdbFalso.setHorizontalAlignment(SwingConstants.CENTER);
			rdbFalso.setActionCommand("Falso");
			grupoCorrecto.add(rdbFalso);
			panelCentralVerdaFalso.add(rdbFalso);

			JButton botonGuardar = new JButton("Guardar Pregunta");
			botonGuardar.addActionListener(new botonGuardarVerdFalsoListener());

			JButton botonLimpiar = new JButton("Limpiar casillas");
			botonLimpiar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					panelCreador.removeAll();
					panelCreador.add(panelCreadorVerdaderoFalso());
					panelCreador.updateUI();
				}
			});

			JPanel panelBotonGuardar = new JPanel(new BorderLayout());
			panelRespuestas.add(panelBotonGuardar, BorderLayout.SOUTH);

			panelBotonGuardar.add(botonLimpiar, BorderLayout.WEST);
			panelBotonGuardar.add(botonGuardar, BorderLayout.EAST);

			/***************************************************************************/
			return panelCreadorVerdaderoFalso;
		}

		/**
		 * Retorna un panel con todos los componentes que posee una pregunta de
		 * respuesta corta.
		 * 
		 * @return JPanel
		 * 
		 */
		private JPanel panelCreadorRespuestaCorta() {
			JPanel panelCreadorRespCorta = new JPanel(new BorderLayout());
			panelCreadorRespCorta.setBorder(BorderFactory.createLineBorder(new Color(33, 48, 71), 2, true));
			panelCreadorRespCorta.setLayout(new BoxLayout(panelCreadorRespCorta, BoxLayout.Y_AXIS));

			/************************* PANEL DEL ENUNCIADO ******************************/
			panelEnunciado = new JPanel(new BorderLayout(20, 10));
			panelEnunciado.setBorder(new EmptyBorder(20, 20, 20, 20));
			panelCreadorRespCorta.add(panelEnunciado, BorderLayout.NORTH);

			JPanel panelEnunciadoSuperior = new JPanel(new BorderLayout());
			panelEnunciado.add(panelEnunciadoSuperior, BorderLayout.NORTH);

			JLabel lblAyudaEnunciado = new JLabel("Enunciado de la pregunta:");
			lblAyudaEnunciado.setFont(new Font("Tahoma", Font.PLAIN, 20));
			panelEnunciadoSuperior.add(lblAyudaEnunciado, BorderLayout.WEST);

			JLabel lblPeso = new JLabel("Valor: ");
			lblPeso.setFont(new Font("Tahoma", Font.PLAIN, 20));
			lblPeso.setHorizontalAlignment(SwingConstants.RIGHT);
			panelEnunciadoSuperior.add(lblPeso, BorderLayout.CENTER);
			textPeso = new JTextField();
			textPeso.setBackground(Color.WHITE);
			textPeso.setForeground(Color.BLACK);
			textPeso.setFont(new Font("Tahoma", Font.PLAIN, 15));
			textPeso.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					char letraIngresada = e.getKeyChar();
					if (!((letraIngresada >= '0') && (letraIngresada <= '9'))
							|| (letraIngresada == KeyEvent.VK_BACK_SPACE) || (letraIngresada == KeyEvent.VK_DELETE)) {
						e.consume();
					}
				}
			});
			panelEnunciadoSuperior.add(textPeso, BorderLayout.EAST);

			textEnunciado = new JTextPane();
			textEnunciado.setBackground(Color.WHITE);
			textEnunciado.setForeground(Color.BLACK);
			textEnunciado.setFont(new Font("Tahoma", Font.PLAIN, 20));

			scroll = new JScrollPane();
			scroll.setViewportView(textEnunciado);
			textEnunciado.setPreferredSize(new Dimension(900, 100));
			panelEnunciado.add(scroll, BorderLayout.CENTER);
			/***************************************************************************/

			/************************* PANEL DE LA RESPUESTA ***************************/

			panelRespuestas = new JPanel(new BorderLayout(20, 10));
			panelRespuestas.setBorder(new EmptyBorder(20, 20, 20, 20));
			panelCreadorRespCorta.add(panelRespuestas, BorderLayout.SOUTH);

			txtRespuestaCortaPgr = new JTextPane();
			txtRespuestaCortaPgr.setBackground(Color.WHITE);
			txtRespuestaCortaPgr.setForeground(Color.BLACK);
			txtRespuestaCortaPgr.setFont(new Font("Thaoma", Font.PLAIN, 20));

			JLabel lblAyudaRespuestas = new JLabel("Respuesta: ");
			lblAyudaRespuestas.setFont(new Font("Tahoma", Font.PLAIN, 20));
			panelRespuestas.add(lblAyudaRespuestas, BorderLayout.NORTH);

			JScrollPane scrollRespCorta = new JScrollPane();
			scrollRespCorta.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			scrollRespCorta.setViewportView(txtRespuestaCortaPgr);
			txtRespuestaCortaPgr.setPreferredSize(new Dimension(700, 100));
			panelRespuestas.add(scrollRespCorta, BorderLayout.CENTER);

			JButton botonGuardar = new JButton("Guardar Pregunta");
			botonGuardar.setFont(new Font("Tahoma", Font.PLAIN, 15));
			botonGuardar.addActionListener(new botonGuardarRespCorta());

			JButton botonLimpiar = new JButton("Limpiar casillas");
			botonLimpiar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					panelCreador.removeAll();
					panelCreador.add(panelCreadorRespuestaCorta());
					panelCreador.updateUI();
				}
			});

			JPanel panelBotonGuardar = new JPanel(new BorderLayout());
			panelRespuestas.add(panelBotonGuardar, BorderLayout.SOUTH);

			panelBotonGuardar.add(botonLimpiar, BorderLayout.WEST);
			panelBotonGuardar.add(botonGuardar, BorderLayout.EAST);

			/***************************************************************************/

			return panelCreadorRespCorta;
		}

		/**
		 * Este metodo agrega mas alternativas a las pregunta de seleccion multiple, con
		 * un maximo de 15
		 * 
		 * @param grupoCorrecto
		 */
		private void agregaAlternativa(ButtonGroup grupoCorrecto) {
			String letraAux = Character.toString((char) letra);
			JPanel panel = new JPanel();
			panelRdBotones.add(panel, BorderLayout.CENTER);
			panel.setLayout(new BorderLayout());
			panel.setBorder(new EmptyBorder(20, 20, 20, 200));
			JTextField alternativa = new JTextField();
			alternativa.setBackground(Color.WHITE);
			alternativa.setForeground(Color.BLACK);
			alternativa.setFont(new Font("Tahoma", Font.PLAIN, 20));
			JRadioButton correcto = new JRadioButton(" " + letraAux + " ");

			correcto.addActionListener(new ActionListener() {
				int i = 0;

				public void actionPerformed(ActionEvent arg0) {
					try {
						for (JRadioButton alternativa : esCorrecta) {
							if (alternativa != null && alternativa.isSelected()) {
								if (respuestasDeLaPregunta.get(i) != null && !respuestasDeLaPregunta.get(i).getText().isBlank()) {
									respuestaCorrectaSelecMul = respuestasDeLaPregunta.get(i).getText();
									indexCorrectoSelecMul = i;
								} else {
									grupoCorrecto.clearSelection();
									JOptionPane.showMessageDialog(null,new JLabel("<html>Alternativa Vacia<br>Agregue un Texto</html>"), "Error",JOptionPane.ERROR_MESSAGE);
									i = 0;
									return;
								}
								i = 0;
								break;
							}

							i++;
						}

					} catch (Exception e) {
						System.out.println(e.toString());
					}

				}
			});
			letra++;
			grupoCorrecto.add(correcto);
			panel.add(correcto, BorderLayout.WEST);
			panel.add(alternativa, BorderLayout.CENTER);
			esCorrecta.add(correcto);
			respuestasDeLaPregunta.add(alternativa);
			panelRdBotones.updateUI();
		}

		/**
		 * Este metodo permite agregar otra pregunta ya sea de cualquier tipo.
		 * 
		 * @param padre
		 * @param panelNuevo
		 */
		void agregaOtraPregunta(JPanel padre, JPanel panelNuevo) {
			padre.removeAll();
			padre.add(panelNuevo);
			pack();
			padre.updateUI();
		}

		/**
		 * Verifica que todos los campos SI estan vacios
		 * 
		 * @return
		 * 
		 * 
		 * 
		 */
		public boolean CamposVacios() {
			if (textPeso == null || textPeso.getText().isBlank() && textEnunciado == null
					|| textEnunciado.getText().isBlank()) {
				return true;
			} else {
				return false;
			}
		}

		/**
		 * Setea en null el panel de respuesta de la pregunta de seleccion multiple
		 */
		public void vaciaTextSelecMul() {
			for (JTextField resp : respuestasDeLaPregunta) {
				resp.setText(null);
			}
			for (JRadioButton rdb : esCorrecta) {
				rdb.setSelected(false);
			}
			textEnunciado.setText(null);
			textPeso.setText(null);
		}

		/**
		 * Setea en null los campos del panel de la pregunta de verdadero o falso.
		 */
		public void vaciaTextVerFalso() {
			textEnunciado.setText(null);
			textPeso.setText(null);
			grupoCorrecto.clearSelection();
		}

		/**
		 * Setea en null los campos del panel de la pregunta de respuesta corta
		 */
		public void vaciaRespuestaCorta() {
			textEnunciado.setText(null);
			txtRespuestaCortaPgr.setText(null);
			textPeso.setText(null);
		}

		/**
		 * Hace que no se puedan ingresar caracteres que no sean numeros en un
		 * jtextfield.
		 *
		 * @author Anton
		 * 
		 */
		private class noAgregaLetrasField implements KeyListener {

			@Override
			public void keyPressed(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				char letraIngresada = arg0.getKeyChar();
				if (!((letraIngresada >= '0') && (letraIngresada <= '9')) || (letraIngresada == KeyEvent.VK_BACK_SPACE)
						|| (letraIngresada == KeyEvent.VK_DELETE)) {
					arg0.consume();
				}
			}

		}

		/**
		 * Funcion del boton de agregar mas alternaativas a la pregunta de seleccion
		 * multiple.
		 * 
		 * @author Anton
		 * 
		 */
		private class btnAgregaMasAlternativas implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (letra <= 79) {
					agregaAlternativa(grupoCorrecto);

				} else {
					JOptionPane.showMessageDialog(null, "Alternativas maxima alcanzada");
				}
			}
		}

		/**
		 * Guarda la pregunta de seleccion multiple en un arraylis que despues sera
		 * guardado en un archivo.
		 * 
		 * @author Antony
		 * 
		 */
		private class botonGuardarSelMulListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (examenCreado != null) {

					if (textEnunciado == null || textEnunciado.getText().isBlank()) {
						JOptionPane.showMessageDialog(null, new JLabel("Enunciado Vacio"), "ERROR CRITICO",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					if (textPeso == null || textPeso.getText().isBlank()) {
						JOptionPane.showMessageDialog(null, new JLabel("Peso Vacio"), "ERROR CRITICO",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					if (respuestasDeLaPregunta.get(indexCorrectoSelecMul) == null
							|| respuestasDeLaPregunta.get(indexCorrectoSelecMul).getText().isBlank()) {
						JOptionPane.showMessageDialog(null, new JLabel("Casilla Correcta Vacia"), "ERROR CRITICO",
								JOptionPane.ERROR_MESSAGE);
						grupoCorrecto.clearSelection();
						return;
					}

					if (grupoCorrecto == null || grupoCorrecto.getSelection() == null) {
						JOptionPane.showMessageDialog(null, new JLabel("Seleccione Respuesta Correcta"),
								"ERROR CRITICO", JOptionPane.ERROR_MESSAGE);
						return;
					}

					List<String> temp = new ArrayList<String>();

					for (int i = 0; i < respuestasDeLaPregunta.size(); i++) {
						if (respuestasDeLaPregunta.get(i).getText() != null
								&& !respuestasDeLaPregunta.get(i).getText().isBlank()) {
							temp.add(respuestasDeLaPregunta.get(i).getText());
						}
					}

					for (int i = 0; i < temp.size(); i++) {
						if (temp.get(i).equals(respuestasDeLaPregunta.get(indexCorrectoSelecMul).getText())) {
							indexCorrectoSelecMul = i;
							break;
						}
					}
					alternativas = temp.toArray(new String[0]);

					if (alternativas == null || alternativas.length < 3) {
						JOptionPane.showMessageDialog(null, new JLabel("Agrege 3 Alternativas Válidas"),
								"ERROR CRITICO", JOptionPane.ERROR_MESSAGE);
						return;
					}

					enunciadoDeLaPregunta = textEnunciado.getText();
					pesoDeLaPregunta = Integer.parseInt(textPeso.getText());
					vaciaTextSelecMul();
					examenCreado.agregaPregunta(new Selec_Mul_Pregunta(enunciadoDeLaPregunta, alternativas,
							indexCorrectoSelecMul, pesoDeLaPregunta));
					totalPreguntas++;
					contadorPreguntaSelecMul++;
					formasExamen.setEnabled(false);
					labelContadorTotalPreguntas.setText("Total preguntas: " + (totalPreguntas));
					botonFinalizar.setEnabled(true);
					grupoCorrecto.clearSelection();
					letra = 65;
					agregaOtraPregunta(panelCreador, panelCreadorSeleccionMultiple());
				} else {
					JOptionPane.showMessageDialog(null, new JLabel("Examen Vacio"), "ERROR CRITICO",
							JOptionPane.ERROR_MESSAGE);
				}

			}

		}

		/**
		 * Guarda en un arraylist las preguntas para despues pasarlas a un archivo
		 *
		 * @author Antony
		 */
		private class botonGuardarVerdFalsoListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (examenCreado != null) {
					if (textEnunciado == null || textEnunciado.getText().isBlank()) {
						JOptionPane.showMessageDialog(null, new JLabel("Enunciado Vacio"), "ERROR CRITICO",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					if (textPeso == null || textPeso.getText().isBlank()) {
						JOptionPane.showMessageDialog(null, new JLabel("Peso Vacio"), "ERROR CRITICO",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					if (grupoCorrecto != null && grupoCorrecto.getSelection() != null) {
						if (grupoCorrecto.getSelection().getActionCommand().equals("Verdadero")) {
							respuestaVerdFalso = true;
						} else {
							respuestaVerdFalso = false;
						}
					} else {
						JOptionPane.showMessageDialog(null, new JLabel("Seleccione estado de la respuesta"),
								"ERROR CRITICO", JOptionPane.ERROR_MESSAGE);
						return;
					}

					enunciadoDeLaPregunta = textEnunciado.getText();
					pesoDeLaPregunta = Integer.parseInt(textPeso.getText());

					examenCreado.agregaPregunta(
							new TFpreguntas(enunciadoDeLaPregunta, respuestaVerdFalso, pesoDeLaPregunta));
					totalPreguntas++;
					contadorPreguntaVerdFals++;
					formasExamen.setEnabled(false);
					labelContadorTotalPreguntas.setText("Total preguntas: " + (totalPreguntas));
					botonFinalizar.setEnabled(true);
					vaciaTextVerFalso();
				} else {
					JOptionPane.showMessageDialog(null, new JLabel("Examen Vacio"), "ERROR CRITICO",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		}

		/**
		 * Guarda en un arraylist las preguntas para despues pasarlas a un archivo
		 * 
		 * @author Anton
		 * 
		 */
		private class botonGuardarRespCorta implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (examenCreado != null) {
					if (textEnunciado == null || textEnunciado.getText().isBlank()) {
						JOptionPane.showMessageDialog(null, new JLabel("Enunciado Vacio"), "ERROR CRITICO",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					if (textPeso == null || textPeso.getText().isBlank()) {
						JOptionPane.showMessageDialog(null, new JLabel("Peso Vacio"), "ERROR CRITICO",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					if (txtRespuestaCortaPgr == null || txtRespuestaCortaPgr.getText().isBlank()) {
						JOptionPane.showMessageDialog(null, new JLabel("Respuesta Vacia"), "ERROR CRITICO",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					pesoDeLaPregunta = Integer.parseInt(textPeso.getText());
					enunciadoDeLaPregunta = textEnunciado.getText();
					respuestaRespCorta = txtRespuestaCortaPgr.getText();

					examenCreado.agregaPregunta(
							new Resp_Cortas_Pregunta(enunciadoDeLaPregunta, respuestaRespCorta, pesoDeLaPregunta));
					totalPreguntas++;
					contadorPreguntaRespCort++;
					formasExamen.setEnabled(false);
					labelContadorTotalPreguntas.setText("Total preguntas: " + (totalPreguntas));
					botonFinalizar.setEnabled(true);
					vaciaRespuestaCorta();
				} else {
					JOptionPane.showMessageDialog(null, new JLabel("Examen Vacio"), "ERROR CRITICO",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		/**
		 * Este boton permite activar el ingreso de tiempo para el examen.
		 *
		 * @author Antony
		 * 
		 * 
		 */
		private class botonActivarTiempo implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (activarTiempo.isSelected()) {
					textTiempo.setEnabled(true);
					textTiempo.setEditable(true);
					textTiempo.setBackground(Color.WHITE);
					textTiempo.setForeground(Color.BLACK);
					textTiempo.setFont(new Font("Tahoma", Font.PLAIN, 15));
				} else {
					textTiempo.setEnabled(false);
					textTiempo.setBackground(new Color(23, 32, 48));
					textTiempo.setEditable(false);
					textTiempo.setText(null);
				}
			}
		}

		/**
		 * Este boton finaliza la creacion del examen tomando todas las preguntas
		 * creadas en el proceso y guardandolas en en un archivo .json con el nombre del
		 * examen. Luego este se mostrara en el menu de los estudiantes
		 * 
		 * @author Antony
		 * 
		 */
		private class botonFinalizarCreacionExamen implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Modelo.ConvierteExamenJson json = new ConvierteExamenJson();

				String direccion = "";

				try {
					direccion = new File(".").getCanonicalPath() + "\\Profesor\\Examenes\\Examenes "
							+ getProfesorName();

				} catch (IOException e) {
					e.printStackTrace();
				}
				if (formasExamen.getSelectedIndex() == 1 || formasExamen.getSelectedIndex() == 2
						|| formasExamen.getSelectedIndex() == 3) {
					if (contadorPreguntaSelecMul < 1 || contadorPreguntaVerdFals < 1 || contadorPreguntaRespCort < 1) {
						JOptionPane.showMessageDialog(null, "Debe agregar al menos 1 pregunta de cada tipo");
						return;
					}
				}

				if (examenCreado != null) {
					if (CamposVacios()) {
						if (activarTiempo.isSelected()) {
							if (textTiempo == null || textTiempo.getText().isBlank()) {
								JOptionPane.showMessageDialog(null, new JLabel("Asigne Tiempo Al Examen"),
										"ERROR CRITICO", JOptionPane.ERROR_MESSAGE);
								return;
							}
						}
						if (formasExamen.getSelectedIndex() != 0) {
							json.guardarenArchivo(direccion + "\\" + ProfesorGUI.nombreDelArchivo, textTiempo.getText(),
									totalPreguntas, formasExamen.getSelectedIndex());
						} else {
							JOptionPane.showInternalMessageDialog(null, "Seleccione forma del examen");
						}
						if (examenCreado.selecmulpreg != null && examenCreado.selecmulpreg.size() >= 1) {
							for (int i = 0; i < examenCreado.selecmulpreg.size(); i++) {
								json.guardarenArreglo(direccion, examenCreado.selecmulpreg.get(i),
										ProfesorGUI.nombreDelArchivo);
							}

						}
						if (examenCreado.tfpreg != null && examenCreado.tfpreg.size() >= 1) {
							for (int i = 0; i < examenCreado.tfpreg.size(); i++) {
								json.guardarenArreglo(direccion, examenCreado.tfpreg.get(i),
										ProfesorGUI.nombreDelArchivo);
							}

						}
						if (examenCreado.rcpreg != null && examenCreado.rcpreg.size() >= 1) {
							for (int i = 0; i < examenCreado.rcpreg.size(); i++) {
								json.guardarenArreglo(direccion, examenCreado.rcpreg.get(i),
										ProfesorGUI.nombreDelArchivo);
							}

						}
						json.guardaArregloEnArchivo(direccion + "\\" + ProfesorGUI.nombreDelArchivo);
						try {
							if (!new File(new File(".").getCanonicalPath() + "\\Profesor\\Notas\\Notas "
									+ getProfesorName() + "\\Notas Examen " + ProfesorGUI.nombreDelArchivo).exists()) {
								new File(new File(".").getCanonicalPath() + "\\Profesor\\Notas\\Notas "
										+ getProfesorName() + "\\Notas Examen " + ProfesorGUI.nombreDelArchivo)
												.mkdirs();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						JOptionPane.showInternalMessageDialog(null, "Examen Creado");
						panelMayorCrearExamen.removeAll();
						panelMayorCrearExamen.add(MenuProfesor());
						panelMayorCrearExamen.repaint();
						pack();
						setLocationRelativeTo(null);
					} else {
						JOptionPane.showMessageDialog(null, new JLabel("Guarde La Pregunta Antes De Finalizar"),
								"ERROR CRITICO", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, new JLabel("Examen Vacio"), "ERROR CRITICO",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

}
