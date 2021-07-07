package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
import java.util.stream.Stream;

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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.commons.lang3.ArrayUtils;

import Modelo.ConvierteExamenJson;
import Modelo.Exam;
import Modelo.FileSystemModel;
import Modelo.JSONaExamen;
import Modelo.ModeloNotasAlumno;
import Modelo.Resp_Cortas_Pregunta;
import Modelo.Selec_Mul_Pregunta;
import Modelo.StringSimilar;
import Modelo.TFpreguntas;

/**
 * @author Antony La clase AlumnoGUI contiene las funciones de logueo, registro
 *         y la posibilidad de realizar los examenes dejados por el profesor
 *         elejido al momento del registro, al finalizar podra visualizar la
 *         nota obtenida.
 */
@SuppressWarnings("serial")
public class AlumnoGUI extends JFrame {

	private JPanel panelMayorAlumno;
	JTextField textRutInicSes;
	JPasswordField textContraInicSes;
	JTextField textNombre;
	JTextField textApellido;
	JTextField textRut;
	JComboBox<String> profesores;
	JTextField textCorreo;
	JPasswordField textContra;
	JPasswordField textConfirma;
	String rut;
	String ubicacionArchivos = "";
	String ubicacionDeLaNotaAlumno = "";
	String nombreAlumno;
	int fila;
	String profe;
	int formaExamen;



	/**
	 * Constructor de AlumnoGUI Ejecula la ventana para el logueo del alumno si los
	 * datos ingresados coinciden con los del sistema se ingresa al software.
	 * 
	 */
	public AlumnoGUI() {
		setTitle("ExamSoft");
		try {
			Image imagenLogo = ImageIO.read(new File(new File(".").getCanonicalPath() + "\\res\\soft.png"));
			setIconImage(imagenLogo);
		} catch (Exception e) {

		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panelMayorAlumno = new JPanel();
		panelMayorAlumno.setLayout(new BorderLayout(0, 0));
		setContentPane(panelMayorAlumno);
		panelMayorAlumno.add(InicioSesionAlumno());
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * 
	 * Panel de inicio de sesion donde se le pide el rut y contraseña al alumno que
	 * ingrese.
	 * 
	 * @return JPanel
	 * 
	 * 
	 */
	public JPanel InicioSesionAlumno() {

		JPanel panelContenedorInicioSesion = new JPanel();
		panelContenedorInicioSesion.setBorder(new EmptyBorder(0, 0, 30, 0));
		panelContenedorInicioSesion.setLayout(new BoxLayout(panelContenedorInicioSesion, BoxLayout.Y_AXIS));

		JPanel panelMenuBar = new JPanel(new BorderLayout(0, 10));
		panelContenedorInicioSesion.add(panelMenuBar);

		JPanel panelImagenInicSes = new JPanel(new BorderLayout());
		panelImagenInicSes.setBorder(new EmptyBorder(40, 70, 0, 70));
		panelContenedorInicioSesion.add(panelImagenInicSes);

		panelContenedorInicioSesion.add(Box.createRigidArea(new Dimension(0, 30)));

		JPanel panelLabelRut = new JPanel(new BorderLayout());
		panelLabelRut.setBorder(new EmptyBorder(0, 40, 0, 40));
		panelContenedorInicioSesion.add(panelLabelRut);

		JPanel panelTextRut = new JPanel(new BorderLayout());
		panelTextRut.setBorder(new EmptyBorder(0, 40, 0, 40));
		panelContenedorInicioSesion.add(panelTextRut);

		panelContenedorInicioSesion.add(Box.createRigidArea(new Dimension(0, 30)));

		JPanel panelLabelContra = new JPanel(new BorderLayout());
		panelLabelContra.setBorder(new EmptyBorder(0, 40, 0, 40));
		panelContenedorInicioSesion.add(panelLabelContra);

		JPanel panelTextContra = new JPanel(new BorderLayout());
		panelTextContra.setBorder(new EmptyBorder(0, 40, 0, 40));
		panelContenedorInicioSesion.add(panelTextContra);

		panelContenedorInicioSesion.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel panelBotonIngresar = new JPanel(new BorderLayout());
		panelBotonIngresar.setBorder(new EmptyBorder(0, 40, 0, 40));
		panelContenedorInicioSesion.add(panelBotonIngresar);

		JPanel panelRecuperaContra = new JPanel(new BorderLayout());
		panelRecuperaContra.setBorder(new EmptyBorder(0, 25, 0, 25));
		panelContenedorInicioSesion.add(panelRecuperaContra);

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

		String titulo = "Inicio de Sesión<br>Alumno";
		JLabel lblTitulo = new JLabel("<html><div style='text-align: center;'>" + titulo + "</div></html>");
		lblTitulo.setFont(new Font("Thaoma", Font.PLAIN, 30));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		panelMenuBar.add(lblTitulo, BorderLayout.SOUTH);

		JLabel lblImagenAlumnoInicSes = new JLabel();
		String ubicacionImagen = "";
		try {

			ubicacionImagen = new File(".").getCanonicalPath() + "\\res\\alumno.png";
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		ImageIcon imagenAlumno = new ImageIcon(ubicacionImagen);
		lblImagenAlumnoInicSes
				.setIcon(new ImageIcon(imagenAlumno.getImage().getScaledInstance(240, 200, Image.SCALE_DEFAULT)));
		panelImagenInicSes.add(lblImagenAlumnoInicSes, BorderLayout.CENTER);

		JLabel lblRut = new JLabel("Rut");
		lblRut.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblRut.setHorizontalAlignment(SwingConstants.LEFT);
		panelLabelRut.add(lblRut, BorderLayout.CENTER);

		textRutInicSes = new JTextField();
		textRutInicSes.setBackground(Color.WHITE);
		textRutInicSes.setForeground(Color.BLACK);
		textRutInicSes.addKeyListener(new noAgregaLetrasRUTField());
		textRutInicSes.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panelTextRut.add(textRutInicSes, BorderLayout.CENTER);

		JLabel lblContraseña = new JLabel("Contraseña");
		lblContraseña.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblContraseña.setHorizontalAlignment(SwingConstants.LEFT);
		panelLabelContra.add(lblContraseña, BorderLayout.CENTER);

		textContraInicSes = new JPasswordField();
		textContraInicSes.setBackground(Color.WHITE);
		textContraInicSes.setForeground(Color.BLACK);
		textContraInicSes.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panelTextContra.add(textContraInicSes, BorderLayout.CENTER);

		JButton botonIngresar = new JButton("Ingresar");
		botonIngresar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String clave = "";
				for (char c : textContraInicSes.getPassword()) {
					clave += c;
				}

				if (verificaCasillasVaciasInicSes()) {
					if (validarut(textRutInicSes.getText())) {
						if (clave.equalsIgnoreCase(getClaveAlumno())) {
							panelMayorAlumno.removeAll();
							panelMayorAlumno.add(MenuAlumno());
							panelMayorAlumno.updateUI();
							pack();
							setLocationRelativeTo(null);
						} else {
							JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrecto");
						}

					} else {
						JOptionPane.showMessageDialog(null, "Formato de rut invalido");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Llene todos los campos");
				}
			}
		});
		botonIngresar.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panelBotonIngresar.add(botonIngresar, BorderLayout.CENTER);

		JButton botonRecuperaContra = new JButton("Recuperar Contraseña");
		botonRecuperaContra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				new Email("alumnos");
			}
		});
		botonRecuperaContra.setFont(new Font("Tahoma", Font.PLAIN, 13));
		botonRecuperaContra.setBackground(new Color(23, 32, 48));
		botonRecuperaContra.setBorderPainted(false);
		panelRecuperaContra.add(botonRecuperaContra, BorderLayout.WEST);

		JButton botonCrearCuenta = new JButton("No tengo cuenta");
		botonCrearCuenta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panelMayorAlumno.removeAll();
				panelMayorAlumno.add(panelRegistro());
				pack();
				setLocationRelativeTo(null);
				panelMayorAlumno.updateUI();
			}
		});
		botonCrearCuenta.setFont(new Font("Tahoma", Font.PLAIN, 13));
		botonCrearCuenta.setBackground(new Color(23, 32, 48));
		botonCrearCuenta.setBorderPainted(false);
		panelRecuperaContra.add(botonCrearCuenta, BorderLayout.EAST);

		return panelContenedorInicioSesion;
	}

	/**
	 * 
	 * Panel de registro. Aca se le piden el nombre, apellido, rut, correo. Tambien
	 * se le pide que seleccione el profesor con el que se va a registrar, de esto
	 * ultimo dependen los examenes que podrá realizar el alumno.
	 * 
	 * @return JPanel
	 * 
	 */
	public JPanel panelRegistro() {

		JPanel panelMayorRegistro = new JPanel();
		panelMayorRegistro.setLayout(new BoxLayout(panelMayorRegistro, BoxLayout.Y_AXIS));
		panelMayorRegistro.setBorder(new EmptyBorder(30, 15, 30, 15));

		JPanel panelTitulo = new JPanel(new BorderLayout(0, 20));
		panelMayorRegistro.add(panelTitulo);

		panelMayorRegistro.add(Box.createRigidArea(new Dimension(0, 30)));

		JPanel panelNombreApellido = new JPanel();
		panelNombreApellido.setBorder(new EmptyBorder(0, 15, 0, 15));
		panelNombreApellido.setLayout(new BoxLayout(panelNombreApellido, BoxLayout.X_AXIS));
		panelMayorRegistro.add(panelNombreApellido);

		JPanel panelName = new JPanel(new BorderLayout());
		JPanel panelApellido = new JPanel(new BorderLayout());
		panelNombreApellido.add(panelName);
		panelNombreApellido.add(Box.createRigidArea(new Dimension(30, 0)));
		panelNombreApellido.add(panelApellido);

		panelMayorRegistro.add(Box.createRigidArea(new Dimension(0, 15)));

		JPanel panelRutyProfesor = new JPanel(new BorderLayout());
		panelRutyProfesor.setBorder(new EmptyBorder(0, 15, 0, 15));
		panelRutyProfesor.setLayout(new BoxLayout(panelRutyProfesor, BoxLayout.X_AXIS));
		panelMayorRegistro.add(panelRutyProfesor);

		JPanel panelRut = new JPanel(new BorderLayout());
		JPanel panelProfesor = new JPanel(new BorderLayout());
		panelRutyProfesor.add(panelRut);
		panelRutyProfesor.add(Box.createRigidArea(new Dimension(30, 0)));
		panelRutyProfesor.add(panelProfesor);

		panelMayorRegistro.add(Box.createRigidArea(new Dimension(0, 15)));

		JPanel panelCorreo = new JPanel(new BorderLayout());
		panelCorreo.setBorder(new EmptyBorder(0, 15, 0, 15));
		panelMayorRegistro.add(panelCorreo);

		panelMayorRegistro.add(Box.createRigidArea(new Dimension(0, 15)));

		JPanel panelContrayConfirma = new JPanel();
		panelContrayConfirma.setBorder(new EmptyBorder(0, 15, 0, 15));
		panelContrayConfirma.setLayout(new BoxLayout(panelContrayConfirma, BoxLayout.X_AXIS));
		panelMayorRegistro.add(panelContrayConfirma);

		JPanel panelContra = new JPanel(new BorderLayout());
		JPanel panelConfirma = new JPanel(new BorderLayout());
		panelContrayConfirma.add(panelContra);
		panelContrayConfirma.add(Box.createRigidArea(new Dimension(30, 0)));
		panelContrayConfirma.add(panelConfirma);

		panelMayorRegistro.add(Box.createRigidArea(new Dimension(0, 15)));

		JPanel panelBotones = new JPanel(new BorderLayout());
		panelBotones.setBorder(new EmptyBorder(0, 0, 0, 15));
		panelMayorRegistro.add(panelBotones);

		String titulo = "Crea una cuenta de<br>Alumno";
		JLabel lblTitulo = new JLabel("<html><div style='text-align: center;'>" + titulo + "</div></html>");
		lblTitulo.setFont(new Font("Thaoma", Font.PLAIN, 30));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		panelTitulo.add(lblTitulo, BorderLayout.NORTH);

		JLabel lblImagenAlumnoInicSes = new JLabel();
		lblImagenAlumnoInicSes.setHorizontalAlignment(SwingConstants.CENTER);
		String ubicacionImagen = "";
		try {

			ubicacionImagen = new File(".").getCanonicalPath() + "\\res\\alumno.png";
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		ImageIcon imagenAlumno = new ImageIcon(ubicacionImagen);
		lblImagenAlumnoInicSes
				.setIcon(new ImageIcon(imagenAlumno.getImage().getScaledInstance(190, 160, Image.SCALE_DEFAULT)));
		panelTitulo.add(lblImagenAlumnoInicSes, BorderLayout.CENTER);

		JLabel labelNombre = new JLabel("Nombre");
		labelNombre.setFont(new Font("Tahoma", Font.PLAIN, 18));
		labelNombre.setHorizontalAlignment(SwingConstants.LEFT);
		panelName.add(labelNombre, BorderLayout.NORTH);

		JLabel labelApellido = new JLabel("Apellido");
		labelApellido.setFont(new Font("Tahoma", Font.PLAIN, 18));
		labelApellido.setHorizontalAlignment(SwingConstants.LEFT);
		panelApellido.add(labelApellido, BorderLayout.NORTH);

		textNombre = new JTextField();
		textNombre.setFont(new Font("Tahoma", Font.PLAIN, 20));
		textNombre.setBackground(Color.WHITE);
		textNombre.setForeground(Color.BLACK);
		textNombre.addKeyListener(new letraMayuscula());
		panelName.add(textNombre, BorderLayout.CENTER);

		textApellido = new JTextField();
		textApellido.setFont(new Font("Tahoma", Font.PLAIN, 20));
		textApellido.setBackground(Color.WHITE);
		textApellido.setForeground(Color.BLACK);
		textApellido.addKeyListener(new letraMayuscula());
		panelApellido.add(textApellido, BorderLayout.CENTER);

		JLabel labelRut = new JLabel("Rut");
		labelRut.setFont(new Font("Tahoma", Font.PLAIN, 18));
		labelRut.setHorizontalAlignment(SwingConstants.LEFT);
		panelRut.add(labelRut, BorderLayout.NORTH);

		textRut = new JTextField(8);
		textRut.setFont(new Font("Tahoma", Font.PLAIN, 20));
		textRut.addKeyListener(new noAgregaLetrasRUTField());
		textRut.setBackground(Color.WHITE);
		textRut.setForeground(Color.BLACK);
		panelRut.add(textRut, BorderLayout.CENTER);

		JLabel labelProfesor = new JLabel("Profesor");
		labelProfesor.setFont(new Font("Tahoma", Font.PLAIN, 18));
		labelProfesor.setHorizontalAlignment(SwingConstants.LEFT);
		panelProfesor.add(labelProfesor, BorderLayout.NORTH);

		profesores = new JComboBox<>();
		profesores.setFont(new Font("Tahoma", Font.PLAIN, 14));
		agregaProfesoresCombo(profesores);
		panelProfesor.add(profesores, BorderLayout.CENTER);

		JLabel labelCorreo = new JLabel("Correo");
		labelCorreo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		labelCorreo.setHorizontalAlignment(SwingConstants.LEFT);
		panelCorreo.add(labelCorreo, BorderLayout.NORTH);

		textCorreo = new JTextField();
		textCorreo.setFont(new Font("Tahoma", Font.PLAIN, 20));
		textCorreo.setBackground(Color.WHITE);
		textCorreo.setForeground(Color.BLACK);
		panelCorreo.add(textCorreo, BorderLayout.CENTER);

		JLabel labelContra = new JLabel("Contraseña");
		labelContra.setFont(new Font("Tahoma", Font.PLAIN, 18));
		labelContra.setHorizontalAlignment(SwingConstants.LEFT);
		panelContra.add(labelContra, BorderLayout.NORTH);

		JLabel labelConfirma = new JLabel("Confirma");
		labelConfirma.setFont(new Font("Tahoma", Font.PLAIN, 18));
		labelConfirma.setHorizontalAlignment(SwingConstants.LEFT);
		panelConfirma.add(labelConfirma, BorderLayout.NORTH);

		textContra = new JPasswordField();
		textContra.setFont(new Font("Tahoma", Font.PLAIN, 20));
		textContra.setBackground(Color.WHITE);
		textContra.setForeground(Color.BLACK);
		panelContra.add(textContra, BorderLayout.CENTER);

		textConfirma = new JPasswordField();
		textConfirma.setFont(new Font("Tahoma", Font.PLAIN, 20));
		textConfirma.setBackground(Color.WHITE);
		textConfirma.setForeground(Color.BLACK);
		panelConfirma.add(textConfirma, BorderLayout.CENTER);

		JButton botonCrearCuenta = new JButton("Crear Cuenta");
		botonCrearCuenta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (verificaCasillasVaciasRegistro()) {
					String clave = "", conficlave = "";
					for (char contra : textContra.getPassword()) {
						clave += contra;
					}
					for (char confi : textConfirma.getPassword()) {
						conficlave += confi;
					}
					if (clave.length() < 8 || conficlave.length() < 8) {
						JOptionPane.showMessageDialog(null, "La contraseña debe tener al menos 8 caracteres");
						return;
					}
					llenaDatosABase();
				} else {
					JOptionPane.showMessageDialog(null, "Llene todos los campos");
				}

			}
		});
		botonCrearCuenta.setFont(new Font("Tahoma", Font.PLAIN, 13));
		panelBotones.add(botonCrearCuenta, BorderLayout.EAST);

		JButton botonIniciarSesion = new JButton("Iniciar Sesión");
		botonIniciarSesion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panelMayorAlumno.removeAll();
				panelMayorAlumno.add(InicioSesionAlumno());
				pack();
				setLocationRelativeTo(null);
				panelMayorAlumno.updateUI();
			}
		});
		botonIniciarSesion.setFont(new Font("Tahoma", Font.PLAIN, 13));
		botonIniciarSesion.setBackground(new Color(23, 32, 48));
		botonIniciarSesion.setBorderPainted(false);
		panelBotones.add(botonIniciarSesion, BorderLayout.WEST);

		return panelMayorRegistro;
	}

	/**
	 * 
	 * Aca se agregan todos los profesores existentes de la base de datos al
	 * JComboBox para que el alumno seleccione con cual desea registrarse.
	 * 
	 * @param profesores
	 * 
	 *
	 */
	public void agregaProfesoresCombo(JComboBox<String> profesores) {
		if (profesores != null) {
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				Conexion objCon = new Conexion();
				Connection conn = objCon.getConnection();
				ps = conn.prepareStatement("SELECT Nombre,Apellido FROM profesores");
				rs = ps.executeQuery();

				profesores.addItem("Seleccione");
				while (rs.next()) {
					profesores.addItem(rs.getString("Nombre") + " " + rs.getString("Apellido"));
				}

			} catch (Exception e) {
				System.err.println(e.toString());
			}
		} else {
			throw new IllegalArgumentException("Profesores es nulo");
		}
	}

	/**
	 * 
	 * Panel del menu del alumno contiene informacion del usuario y contiene tabla
	 * de los examenes pendientes.
	 * 
	 * @return JPanel
	 * 
	 * 
	 */
	public JPanel MenuAlumno() {

		JPanel panelContenedorMenu = new JPanel(new BorderLayout());
		JPanel panelContenedorIzquierdo = new JPanel(new BorderLayout());
		panelContenedorIzquierdo.setLayout(new BorderLayout());
		panelContenedorIzquierdo.setBorder(new EmptyBorder(20, 20, 10, 20));
		panelContenedorMenu.add(panelContenedorIzquierdo, BorderLayout.WEST);
		JPanel panelContenedorDerecho = new JPanel(new BorderLayout());
		panelContenedorMenu.add(panelContenedorDerecho, BorderLayout.CENTER);

		JPanel panelImagen = new JPanel(new BorderLayout());
		panelImagen.setBorder(BorderFactory.createLineBorder(new Color(33, 48, 71), 2, true));
		panelContenedorIzquierdo.add(panelImagen, BorderLayout.CENTER);
		JPanel panelImagenInterno = new JPanel();
		panelImagenInterno.setLayout(new BoxLayout(panelImagenInterno, BoxLayout.Y_AXIS));
		panelImagen.add(panelImagenInterno, BorderLayout.CENTER);

		JMenuBar barraOpciones = new JMenuBar();
		panelContenedorMenu.add(barraOpciones, BorderLayout.NORTH);
		JMenu menuOpciones = new JMenu("Opciones");
		menuOpciones.setFont(new Font("Tahoma", Font.PLAIN, 15));
		barraOpciones.add(menuOpciones);
		JMenuItem VolverOpcion = new JMenuItem("Volver");
		VolverOpcion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				new AlumnoGUI();
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

		JLabel lblImagenAlumno = new JLabel();
		String ubicacionImagen = "";

		try {
			ubicacionImagen = new File(".").getCanonicalPath() + "\\res\\alumno.png";
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		panelImagenInterno.add(Box.createRigidArea(new Dimension(0, 80)));
		JPanel panelIcono = new JPanel(new BorderLayout());
		panelImagenInterno.add(panelIcono);
		ImageIcon imagenAdmin = new ImageIcon(ubicacionImagen);
		lblImagenAlumno.setIcon(new ImageIcon(imagenAdmin.getImage().getScaledInstance(220, 180, Image.SCALE_DEFAULT)));
		lblImagenAlumno.setHorizontalAlignment(SwingConstants.CENTER);
		panelIcono.add(lblImagenAlumno, BorderLayout.CENTER);

		JPanel panelInfo = new JPanel(new BorderLayout());
		panelImagenInterno.add(panelInfo);
		JLabel labelInfoUser = new JLabel();
		labelInfoUser.setText("<html><font color=\"white\">Usuario: </font>" + getNombreAlumno() + "</html>");
		labelInfoUser.setFont(new Font("Tahoma", Font.PLAIN, 16));
		labelInfoUser.setForeground(new Color(62, 208, 162));

		labelInfoUser.setHorizontalAlignment(SwingConstants.CENTER);
		panelInfo.add(labelInfoUser, BorderLayout.CENTER);

		JLabel labelInfoProfe = new JLabel();
		labelInfoProfe.setText("<html><font color=\"white\">Docente:  </font>" + getProfesorAlumno() + "</html>");
		labelInfoProfe.setFont(new Font("Tahoma", Font.PLAIN, 16));
		labelInfoProfe.setForeground(new Color(221, 199, 17));

		labelInfoProfe.setHorizontalAlignment(SwingConstants.CENTER);
		panelInfo.add(labelInfoProfe, BorderLayout.SOUTH);
		panelImagenInterno.add(Box.createRigidArea(new Dimension(0, 100)));

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
					+ getProfesorAlumno();
			if (!new File(ubicacionArchivos).exists()) {
				new File(ubicacionArchivos).mkdirs();
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		JTable tablaDeExamamenesParaRealizar = new JTable();
		tablaDeExamamenesParaRealizar.setModel(encabezadoExamen());
		llenaTablaDeExamenes(tablaDeExamamenesParaRealizar, ubicacionArchivos);
		tablaDeExamamenesParaRealizar.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					fila = tablaDeExamamenesParaRealizar.getSelectedRow();
					TableModel tm = tablaDeExamamenesParaRealizar.getModel();
					if (!String.valueOf(tm.getValueAt(fila, 1)).equalsIgnoreCase("Resuelto")) {
						realizarExamen(fila, ubicacionArchivos);
					} else {
						JOptionPane.showMessageDialog(null, "Ya resolvio el examen");
					}

				}
			}
		});
		tablaDeExamamenesParaRealizar.getColumnModel().getColumn(0).setPreferredWidth(300);
		tablaDeExamamenesParaRealizar.getColumnModel().getColumn(1).setPreferredWidth(100);
		tablaDeExamamenesParaRealizar.getTableHeader().setReorderingAllowed(false);
		tablaDeExamamenesParaRealizar.getTableHeader().setResizingAllowed(false);

		JScrollPane scrollArbol = new JScrollPane(tablaDeExamamenesParaRealizar);
		panelCreacionExamen.add(scrollArbol, BorderLayout.CENTER);

		JTable tablaDeExamenesNotas = new JTable();
		tablaDeExamenesNotas.setModel(encabezadoNota());
		llenaTablaDeNotas(tablaDeExamenesNotas, ubicacionArchivos);
		tablaDeExamenesNotas.getColumnModel().getColumn(0).setPreferredWidth(300);
		tablaDeExamenesNotas.getColumnModel().getColumn(1).setPreferredWidth(100);
		tablaDeExamenesNotas.getTableHeader().setReorderingAllowed(false);
		tablaDeExamenesNotas.getTableHeader().setResizingAllowed(false);
		JScrollPane scrollTablaNota = new JScrollPane(tablaDeExamenesNotas);
		panelNotasExamen.add(scrollTablaNota, BorderLayout.CENTER);

		return panelContenedorMenu;

	}

	/**
	 * Retorna la clave del alumno para ser verificada en el inicio de sesión.
	 * 
	 * @return String
	 * 
	 * 
	 */
	public String getClaveAlumno() {
		String clave = "";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			Conexion objCon = new Conexion();
			Connection conn = objCon.getConnection();
			rut = textRutInicSes.getText();
			ps = conn.prepareStatement("SELECT Contraseña, CAST(AES_DECRYPT(Contraseña,'alumno')AS CHAR(50)) FROM alumnos WHERE Rut = ?");
			ps.setString(1, rut);
			rs = ps.executeQuery();
			while (rs.next()) {
				clave = rs.getString("CAST(AES_DECRYPT(Contraseña,'alumno')AS CHAR(50))");
			}
		} catch (Exception e2) {
			System.err.println(e2.toString());
		}
		return clave;
	}

	/**
	 * 
	 * Retorna nombre del profesor el cual el alumno elijió a la hora del registro.
	 * 
	 * @return String
	 * 
	 */
	public String getProfesorAlumno() {
		String profe = "";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			Conexion objCon = new Conexion();
			Connection conn = objCon.getConnection();
			if (textRutInicSes != null) {
				rut = textRutInicSes.getText();
				ps = conn.prepareStatement("SELECT Profesor FROM alumnos WHERE Rut = ?");
				ps.setString(1, rut);
				rs = ps.executeQuery();

				while (rs.next()) {
					profe = rs.getString("Profesor");

				}
			}
		} catch (Exception e2) {
			System.err.println(e2.toString());
		}
		return profe;
	}

	/**
	 * Encabezado de la tabla de las notas.
	 * 
	 * @return DefaultTableModel
	 * 
	 */
	DefaultTableModel encabezadoNota() {
		String[] rubrosTablaProf = { "Examenes", "Nota" };
		DefaultTableModel tablaPorDefecto = new DefaultTableModel(rubrosTablaProf, 0);
		return tablaPorDefecto;
	}

	/**
	 * Encabezado de la tabla de examenes.
	 * 
	 * @return DefaultTableModel
	 * 
	 */
	DefaultTableModel encabezadoExamen() {
		String[] rubrosTablaProf = { "Examenes", "Estado" };
		DefaultTableModel tablaPorDefecto = new DefaultTableModel(rubrosTablaProf, 0);
		return tablaPorDefecto;
	}

	/**
	 * 
	 * Rellena la tabla de examenes creador por el profesor elejido.
	 * 
	 * @param tabla Tabla
	 * @param ubicacion Ubicacion del archivo
	 * 
	 * 
	 * 
	 */
	public void llenaTablaDeExamenes(JTable tabla, String ubicacion) {
		if (tabla != null && ubicacion != null) {
			JSONaExamen nota = new JSONaExamen();
			DefaultTableModel modelo = new DefaultTableModel() {
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			modelo.addColumn("Examenes");
			modelo.addColumn("Estado");
			Modelo.FileSystemModel ubicacionArchivos = new FileSystemModel(new File(ubicacion));
			File archivos = new File(ubicacion);

			String[] totalArchivos = archivos.list();
			Object[] fila = new Object[2];
			for (int i = 0; i < totalArchivos.length; i++) {

				fila[0] = ubicacionArchivos.getNombreArchivos(archivos, i).substring(0,
						ubicacionArchivos.getNombreArchivos(archivos, i).lastIndexOf("."));
				try {
					ubicacionDeLaNotaAlumno = new File(".").getCanonicalPath() + "\\Profesor\\Notas\\Notas "
							+ getProfesorAlumno() + "\\Notas Examen "
							+ ubicacionArchivos.getNombreArchivos(archivos, i).substring(0,
									ubicacionArchivos.getNombreArchivos(archivos, i).indexOf("."))
							+ "\\" + getNombreAlumno() + ".json";

				} catch (Exception e) {
					System.out.println(e.toString());
				}
				if (!new File(ubicacionDeLaNotaAlumno).exists()) {
					fila[1] = "No realizado";
				} else {
					try {
						fila[1] = nota.getEstadoExamen(new File(ubicacionDeLaNotaAlumno));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
				modelo.addRow(fila);
			}
			tabla.setModel(modelo);
		} else {
			throw new IllegalArgumentException("tabla y/o ubicacion nula");
		}

	}

	/**
	 * Rellena la tabla de notas con los resultados sacados por elalumno
	 * 
	 * @param tabla Tabla
	 * @param ubicacion Ubicacion del archivo
	 * 
	 */
	public void llenaTablaDeNotas(JTable tabla, String ubicacion) {
		if (tabla != null && ubicacion != null) {
			DefaultTableModel modelo = new DefaultTableModel() {
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			String ubicacionDeLaNotaAlumnoParaTablaNota = "";
			JSONaExamen resultados = new JSONaExamen();
			ModeloNotasAlumno notaEnTabla;

			modelo.addColumn("Examenes");
			modelo.addColumn("Nota");
			Modelo.FileSystemModel ubicacionArchivos = new FileSystemModel(new File(ubicacion));
			File archivos = new File(ubicacion);
			String[] totalArchivos = archivos.list();
			Object[] fila = new Object[2];
			for (int i = 0; i < totalArchivos.length; i++) {
				fila[0] = ubicacionArchivos.getNombreArchivos(archivos, i).substring(0,
						ubicacionArchivos.getNombreArchivos(archivos, i).lastIndexOf("."));
				try {
					ubicacionDeLaNotaAlumnoParaTablaNota = new File(".").getCanonicalPath()
							+ "\\Profesor\\Notas\\Notas " + getProfesorAlumno() + "\\Notas Examen "
							+ ubicacionArchivos.getNombreArchivos(archivos, i).substring(0,
									ubicacionArchivos.getNombreArchivos(archivos, i).indexOf("."))
							+ "\\" + getNombreAlumno() + ".json";

				} catch (Exception e) {
					System.out.println(e.toString());
				}
				if (!new File(ubicacionDeLaNotaAlumnoParaTablaNota).exists()) {
					fila[1] = "Sin califiacar";
				} else {
					try {
						notaEnTabla = resultados.generaResultadoExam(new File(ubicacionDeLaNotaAlumnoParaTablaNota));

						fila[1] = String.valueOf(notaEnTabla.getPuntajeTotalAlumno()) + "/"
								+ String.valueOf(notaEnTabla.getPuntajeTotalExam());
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
				modelo.addRow(fila);
			}
			tabla.setModel(modelo);
		} else {
			throw new IllegalArgumentException("tabla y/o ubicacion nula");
		}
	}

	/**
	 * 
	 * Ejecuta el examen elejido en tabla.
	 * 
	 * @param fila Fila de la tabla
	 * @param ubicacion Ubicacion del archivo
	 * 
	 * 
	 */
	public void realizarExamen(int fila, String ubicacion) {
		if (ubicacion != null && fila >= 0) {
			File archivos = new File(ubicacion);
			if (archivos.exists()) {
				JSONaExamen conversor = new JSONaExamen();
				String[] archivoElegido = archivos.list();
				try {
					ExamenGUI ExamenGenerado = new ExamenGUI(
							conversor.generaExamen(new File(ubicacion + "\\" + archivoElegido[fila])),
							getProfesorAlumno(), getNameExamen(), getNombreAlumno());
					panelMayorAlumno.removeAll();
					panelMayorAlumno.add(ExamenGenerado);
					panelMayorAlumno.updateUI();
					pack();
					setLocationRelativeTo(null);
				} catch (Exception e) {
					System.out.println(e);
				}
			} else {
				throw new IllegalArgumentException("La ubicacion no existe");
			}
		} else {
			throw new IllegalArgumentException("La ubicacion es nula");
		}
	}

	/**
	 * Verifica si las casillas del panel del registro NO estan vacias.
	 * 
	 * @return boolean
	 * 
	 */
	private boolean verificaCasillasVaciasRegistro() {
		String clave = "", conficlave = "";
		for (char contra : textContra.getPassword()) {
			clave += contra;
		}
		for (char confi : textConfirma.getPassword()) {
			conficlave += confi;
		}
		if (textApellido.getText().isBlank() || textNombre.getText().isBlank() || textRut.getText().isBlank()
				|| textCorreo.getText().isBlank() || clave.isBlank() || conficlave.isBlank()
				|| profesores.getSelectedIndex() == 0) {
			return false;
		}
		return true;
	}

	/**
	 * Verifica si las casillas del panel de inicio de sesion NO estan vacias.
	 * 
	 * @return boolean
	 * 
	 * 
	 */
	private boolean verificaCasillasVaciasInicSes() {
		String clave = "";
		for (char c : textContraInicSes.getPassword()) {
			clave += c;
		}
		if (textRutInicSes.getText().isBlank() || clave.isBlank()) {
			return false;
		}
		return true;
	}

	/**
	 * Verifica si el rut cumple con el formato establecido
	 * 
	 * @param rut Rut
	 * @return boolean
	 * 
	 * 
	 */
	public static boolean validarut(String rut) {
		return rut.matches("^([0-9]{8,9}-[0-9|k|K])$");
	}

	/**
	 * Lleva los datos del registo a la base de datos.
	 */
	private void llenaDatosABase() {
		PreparedStatement ps = null;
		String claveaux1 = "", claveaux2 = "";
		if (!cuentaExiste()) {
			try {

				Conexion objCon = new Conexion();
				Connection conn = objCon.getConnection();
				ps = conn.prepareStatement(
						"INSERT INTO alumnos (Nombre,Apellido,Rut,Correo,Profesor,Contraseña) VALUES(?,?,?,?,?,AES_ENCRYPT(?,'alumno'))");

				for (char c : textContra.getPassword()) {
					claveaux1 += c;
				}
				for (char c : textConfirma.getPassword()) {
					claveaux2 += c;
				}

				if (claveaux1.equals(claveaux2)) {
					if (validarut(textRut.getText())) {

						ps.setString(1, textNombre.getText());
						ps.setString(2, textApellido.getText());
						ps.setString(3, textRut.getText());
						ps.setString(4, textCorreo.getText());
						ps.setString(5, (String) profesores.getSelectedItem());
						ps.setString(6, claveaux2);

						int resl = ps.executeUpdate();

						if (resl > 0) {
							JOptionPane.showMessageDialog(null, "Cuenta Creada");
							limpiaCajas();
						} else {
							JOptionPane.showMessageDialog(null, "No se pudo crear la cuenta");
						}
						conn.close();

					} else {
						JOptionPane.showMessageDialog(null, "Formato de Rut invalido");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden");
				}
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		} else {
			JOptionPane.showMessageDialog(null, "<html>La cuenta ya existe<br>Por favor inicie sesión</html>");
		}

	}

	/**
	 * Setea en null las casillas del panel de registro.
	 */
	public void limpiaCajas() {
		textApellido.setText(null);
		textNombre.setText(null);
		textRut.setText(null);
		textCorreo.setText(null);
		textContra.setText(null);
		textConfirma.setText(null);
	}

	/**
	 * Verifica al momento del registro si la cuenta ya existe, y no deja crear
	 * otra.
	 * 
	 * @return boolean
	 * 
	 */
	private boolean cuentaExiste() {

		String rut = "";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			Conexion objCon = new Conexion();
			Connection conn = objCon.getConnection();
			ps = conn.prepareStatement("SELECT Rut FROM alumnos WHERE Rut = ?");
			ps.setString(1, textRut.getText());
			rs = ps.executeQuery();

			while (rs.next()) {
				rut = rs.getString("Rut");
			}
			if (rut.equalsIgnoreCase(textRut.getText())) {
				return true;
			}
		} catch (Exception e2) {
			System.err.println(e2.toString());
		}
		return false;
	}

	/**
	 * Hace que al momento de ingresar el rut no permita ingresar otros caracteres,
	 * excepto el '-' y 'k'
	 * 
	 * @author Antony
	 * 
	 */
	private class noAgregaLetrasRUTField implements KeyListener {
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
			if (textRut != null) {
				if (textRut.getText().length() > 9) {
					arg0.consume();
				}
			}
			if (textRutInicSes != null) {
				if (textRutInicSes.getText().length() > 9) {
					arg0.consume();
				}
			}

		}

	}

	/**
	 * Hace que la primera letra ingresada el las casilla sea mayuscula
	 * 
	 * @author Antony
	 * 
	 */
	private class letraMayuscula implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			JTextField txtnombre = (JTextField) arg0.getComponent();

			String texto = txtnombre.getText().trim();
			if (texto.length() > 0) {
				char primero = texto.charAt(0);
				texto = Character.toUpperCase(primero) + texto.substring(1, texto.length());
				txtnombre.setText(texto);
			}

		}

	}

	/**
	 * Retorna el nombre del alumno en base al rut ingresado
	 * 
	 * @return String
	 * 
	 */
	public String getNombreAlumno() {
		String name = new String();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			Conexion objCon = new Conexion();
			Connection conn = objCon.getConnection();
			if (textRutInicSes != null) {
				rut = textRutInicSes.getText();
			}
			ps = conn.prepareStatement("SELECT Nombre,Apellido FROM alumnos WHERE Rut = ?");
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
	 * Retorna el nombre del examen que esta realizando el alumno.
	 * 
	 * @return String
	 * 
	 */
	public String getNameExamen() {
		String ubicacion = "";
		try {
			ubicacion = new File(".").getCanonicalPath() + "\\Profesor\\Examenes\\Examenes " + getProfesorAlumno();

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		File archivos = new File(ubicacion);
		String[] archivoElegido = archivos.list();
		return archivoElegido[fila].substring(0, archivoElegido[fila].lastIndexOf("."));
	}

	/**
	 * Esta clase permite realizar cualquier examen que el alumno halla elegido
	 * posteriormente en la tabla del menú.
	 * 
	 * @author Antony
	 * 
	 *
	 */
	public class ExamenGUI extends JPanel {

		JPanel panelMaster;
		JPanel panelEnunciado;
		JTextArea textEnunciado;
		JScrollPane scroll;
		JPanel panelRespuestaSelecMultiple;
		JPanel panelContenedorRespuestasAbsoluto;
		JPanel panelBotones;
		JButton btnSiguiente;
		JLabel labelTipoDePregunta;
		JLabel labelCantidadPreguntas;
		JLabel labelTiempo;
		ButtonGroup grupoDeBotones;
		Modelo.Exam examen;
		private String[] respuestaSeleccionadaUser;
		private ArrayList<JRadioButton> botonesSelecMult;
		private ArrayList<JRadioButton> botonesTF;

		int iterador = 0;
		private JButton btnAnterior;
		private JPanel panelRespuestaTrueFalse;
		private JPanel panelRespuestaCorta;
		Timer tiempo;
		boolean flag = true;
		private JRadioButton[] ArregloRbutton;
		private String[] respuestasCorrectasEXM;
		private int[] puntajesUser;
		private int[] puntajeExam;
		private String direccion;
		private String nombreAlumno;
		private int m = 10, s = 0;
		private JLabel labelM;
		private JLabel labelS;

		/**
		 * Constructor vacio de ExamenGUI
		 */
		public ExamenGUI() {
		}

		/**
		 * Constructor de ExamenGUI inicia con una ventana de logueo para el alumno
		 * 
		 * 
		 * @param examen Examen generado
		 * @param nombreProfesor Nombre del preofesor
		 * @param nombreExamen Nombre del examen
		 * @param nombreAlumno Nombre del alumno
		 */
		public ExamenGUI(Exam examen, String nombreProfesor, String nombreExamen, String nombreAlumno) {

			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			panelMaster = new JPanel(new BorderLayout());

			panelMaster.setBorder(new EmptyBorder(0, 0, 0, 0));
			panelMaster.setLayout(new BoxLayout(panelMaster, BoxLayout.Y_AXIS));
			setContentPane(panelMaster);
			panelMaster.add(BarraMenu(), BorderLayout.NORTH);
			panelMaster.add(iniciador(examen, nombreProfesor, nombreExamen, nombreAlumno), BorderLayout.CENTER);
			setResizable(false);
			pack();
			setLocationRelativeTo(null);
			setVisible(true);
		}

		/**
		 * Barra superior de opciones, pernite salir al menu del alumno.
		 * 
		 * 
		 * @return JPanel
		 * 
		 */
		public JPanel BarraMenu() {

			JPanel panelContenedorMenu = new JPanel();
			panelContenedorMenu.setLayout(new BoxLayout(panelContenedorMenu, BoxLayout.Y_AXIS));
			/************************* PANEL DE OPCIONES *******************************/
			JPanel panelMenu = new JPanel(new BorderLayout());
			JMenuBar BarraOpciones = new JMenuBar();
			JMenu Opciones = new JMenu("Opciones");
			Opciones.setFont(new Font("Segoe UI", Font.PLAIN, 13));
			JMenuItem OpcionCerrarSesion = new JMenuItem("Salir");
			OpcionCerrarSesion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
			OpcionCerrarSesion.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					int respuesta = JOptionPane.showConfirmDialog(null,"<html>Esta deguro de salir, se perderan todos sus progresos<br>y no podra volver a dar el examen</html>","Finalizar", JOptionPane.YES_NO_OPTION);
					ConvierteExamenJson json = new ConvierteExamenJson();
					if(respuesta == JOptionPane.YES_OPTION) {
						for (int i = 0; i < respuestaSeleccionadaUser.length; i++) {
							respuestaSeleccionadaUser[i] = "No contestó";
							
						}
						json.guardaNotasAlumno(puntajesUser, puntajeExam, respuestaSeleccionadaUser, respuestasCorrectasEXM, getDireccion() + "\\" + getNombreAlumno());
						
						panelMaster.removeAll();
						panelMaster.add(MenuAlumno());
						panelMaster.updateUI();
						pack();
						setLocationRelativeTo(null);
					}
				}
			});
			Opciones.add(OpcionCerrarSesion);
			BarraOpciones.add(Opciones);
			panelMenu.add(BarraOpciones, BorderLayout.SOUTH);
			panelContenedorMenu.add(panelMenu);
			/***************************************************************************/
			return panelContenedorMenu;
		}

		public void setDireccion(String direccion) {
			this.direccion = direccion;
		}

		public String getDireccion() {
			return direccion;
		}

		public void setNombreAlumno(String nombreAlumno) {
			this.nombreAlumno = nombreAlumno;
		}

		public String getNombreAlumno() {
			return nombreAlumno;
		}

		/**
		 * 
		 * Ejecuta el examen, agrega tantos paneles como preguntas tenga el examen.
		 *
		 * @param examen Examen generado
		 * @param nombreProfesor Nombre del preofesor
		 * @param nombreExamen Nombre del examen
		 * @param nombreAlumno Nombre del alumno
		 * @return JPanel
		 * 
		 * 
		 * 
		 */
		public JPanel iniciador(Exam examen, String nombreProfesor, String nombreExamen, String nombreAlumno) {
			setNombreAlumno(nombreAlumno);
			formaExamen = examen.getFormaExamen();
			m = examen.getTiempo();
			if (m > 0) {
				tiempo = new Timer(1000, acciones);
				tiempo.start();
			}

			JPanel panelIniciador = new JPanel(new BorderLayout(0, 20));
			panelIniciador.setBorder(new EmptyBorder(20, 20, 20, 20));

			JPanel panelAgregados = new JPanel();
			panelAgregados.setLayout(new BorderLayout(20, 0));
			panelIniciador.add(panelAgregados, BorderLayout.NORTH);

			JPanel panelContenedorPregunta = new JPanel(new BorderLayout());
			panelContenedorPregunta.setBorder(BorderFactory.createLineBorder(new Color(33, 48, 71), 2, true));
			panelIniciador.add(panelContenedorPregunta, BorderLayout.CENTER);

			JPanel panelContenedorBotones = new JPanel();
			panelContenedorBotones.setLayout(new BoxLayout(panelContenedorBotones, BoxLayout.Y_AXIS));
			panelIniciador.add(panelContenedorBotones, BorderLayout.SOUTH);

			/************************ PANEL DE AGREGADOS ****************************/
			JPanel panelInfoPreg = new JPanel();
			panelInfoPreg.setLayout(new BoxLayout(panelInfoPreg, BoxLayout.Y_AXIS));
			panelAgregados.add(panelInfoPreg, BorderLayout.WEST);

			JPanel panelTiempo = new JPanel(new GridLayout(0, 2, 0, 0));
			panelAgregados.add(panelTiempo, BorderLayout.CENTER);

			JPanel panelBotonFinalizar = new JPanel();
			panelBotonFinalizar.setLayout(new BorderLayout(0, 0));
			panelAgregados.add(panelBotonFinalizar, BorderLayout.EAST);

			try {
				setDireccion(new File(".").getCanonicalPath() + "\\Profesor\\Notas\\Notas " + nombreProfesor
						+ "\\Notas Examen " + nombreExamen);
				if (!new File(getDireccion()).exists()) {
					new File(getDireccion()).mkdirs();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			JButton botonFinalizar = new JButton("Finalizar Examen");
			botonFinalizar.addActionListener(new ActionListener() {
				ConvierteExamenJson json = new ConvierteExamenJson();
				public void actionPerformed(ActionEvent arg0) {
					ArrayList<Integer> respuestasVacias = new ArrayList<>();
					String mensaje = "";
					for (int i = 0; i < respuestaSeleccionadaUser.length; i++) {
						if (respuestaSeleccionadaUser[i] == null || respuestaSeleccionadaUser[i] == "") {
							respuestasVacias.add(i + 1);
						}
					}
					for (Integer integer : respuestasVacias) {
						if (mensaje.isBlank()) {
							mensaje += Integer.toString(integer);
						} else {
							mensaje = mensaje + "," + Integer.toString(integer);
						}
					}
					if (respuestasVacias.size() >= 1) {
						int respuesta = JOptionPane.showConfirmDialog(null,
								"<html>Seguro que quiere finalizar<br>le falta contestar las preguntas : " + mensaje
										+ "</br> </html>",
								"Finalizar", JOptionPane.YES_NO_OPTION);
						if (respuesta == JOptionPane.YES_OPTION) {
							for (int i = 0; i < respuestaSeleccionadaUser.length; i++) {
								if (respuestaSeleccionadaUser[i] == null || respuestaSeleccionadaUser[i].isBlank()) {
									respuestaSeleccionadaUser[i] = "No contestó";
								}
							}
							json.guardaNotasAlumno(puntajesUser, puntajeExam, respuestaSeleccionadaUser,
									respuestasCorrectasEXM, getDireccion() + "\\" + nombreAlumno);
							if (m > 0 || s > 0) {
								tiempo.stop();
							}
							panelMaster.removeAll();
							panelMaster.add(MenuAlumno());
							panelMaster.updateUI();
							pack();
							setLocationRelativeTo(null);
						}
					} else {
						json.guardaNotasAlumno(puntajesUser, puntajeExam, respuestaSeleccionadaUser,
								respuestasCorrectasEXM, getDireccion() + "\\" + nombreAlumno);
						if (m > 0 || s > 0) {
							tiempo.stop();
						}
						panelMaster.removeAll();
						panelMaster.add(MenuAlumno());
						panelMaster.updateUI();
						pack();
						setLocationRelativeTo(null);
					}
				}
			});
			botonFinalizar.setFont(new Font("Tahoma", Font.PLAIN, 17));
			panelBotonFinalizar.add(botonFinalizar);

			labelM = new JLabel();
			labelM.setHorizontalAlignment(SwingConstants.RIGHT);
			labelM.setFont(new Font("Tahoma", Font.PLAIN, 15));
			panelTiempo.add(labelM);

			labelS = new JLabel();
			labelS.setHorizontalAlignment(SwingConstants.LEFT);
			labelS.setFont(new Font("Tahoma", Font.PLAIN, 15));
			panelTiempo.add(labelS);
			if (m > 0) {
				labelM.setText("Tiempo " + m + ":");
				labelS.setText("0" + s + " minutos");
			} else {
				labelM.setText("Tiempo --:");
				labelS.setText("-- minutos");
			}

			labelCantidadPreguntas = new JLabel();
			labelCantidadPreguntas.setFont(new Font("Tahoma", Font.PLAIN, 15));

			labelCantidadPreguntas.setHorizontalAlignment(SwingConstants.CENTER);
			panelInfoPreg.add(labelCantidadPreguntas);

			labelTipoDePregunta = new JLabel();
			labelTipoDePregunta.setFont(new Font("Tahoma", Font.PLAIN, 15));
			labelTipoDePregunta.setHorizontalAlignment(SwingConstants.CENTER);
			panelInfoPreg.add(labelTipoDePregunta);

			switch (formaExamen) {
			case 1: {
				labelCantidadPreguntas.setText("Pregunta " + (iterador + 1) + " de "+ (examen.selecmulpreg.size() + examen.tfpreg.size() + examen.rcpreg.size()));
				labelTipoDePregunta.setText("SELECCIÓN MULTIPLE");
				labelTipoDePregunta.setForeground(Color.GREEN);
				ArrayList<String> aux1 = new ArrayList<>();
				ArrayList<String> aux2 = new ArrayList<>();
				ArrayList<String> aux3 = new ArrayList<>();
				int[] pes1 = new int[examen.selecmulpreg.size()];
				int[] pes2 = new int[examen.tfpreg.size()];
				int[] pes3 = new int[examen.rcpreg.size()];
				for (int i = 0; i < examen.selecmulpreg.size(); i++) {
					aux1.add(examen.selecmulpreg.get(i).getRespuestaCorecta());
					pes1[i] = examen.selecmulpreg.get(i).getPeso();
				}
				for (int i = 0; i < examen.tfpreg.size(); i++) {
					aux2.add((examen.tfpreg.get(i).getRespuestaTF() == true) ? "Verdadero" : "Falso");
					pes2[i] = examen.tfpreg.get(i).getPeso();
				}
				for (int i = 0; i < examen.rcpreg.size(); i++) {
					aux3.add(examen.rcpreg.get(i).getRespuestaCorta());
					pes3[i] = examen.rcpreg.get(i).getPeso();
				}
				respuestasCorrectasEXM = Stream.of(aux1.toArray(), aux2.toArray(), aux3.toArray()).flatMap(Stream::of).toArray(String[]::new);
				puntajesUser = new int[respuestasCorrectasEXM.length];
				respuestaSeleccionadaUser = new String[respuestasCorrectasEXM.length];
				puntajeExam = ArrayUtils.addAll(pes1, pes2);
				puntajeExam = ArrayUtils.addAll(puntajeExam, pes3);
				break;
			}
			case 2: {

				labelCantidadPreguntas.setText("Pregunta " + (iterador + 1) + " de "+ (examen.selecmulpreg.size() + examen.tfpreg.size() + examen.rcpreg.size()));
				labelTipoDePregunta.setText("VERDADERO O FALSO");
				labelTipoDePregunta.setForeground(Color.CYAN);
				ArrayList<String> aux1 = new ArrayList<>();
				ArrayList<String> aux2 = new ArrayList<>();
				ArrayList<String> aux3 = new ArrayList<>();
				int[] pes1 = new int[examen.selecmulpreg.size()];
				int[] pes2 = new int[examen.tfpreg.size()];
				int[] pes3 = new int[examen.rcpreg.size()];
				for (int i = 0; i < examen.selecmulpreg.size(); i++) {
					aux1.add(examen.selecmulpreg.get(i).getRespuestaCorecta());
					pes1[i] = examen.selecmulpreg.get(i).getPeso();
					
				}
				for (int i = 0; i < examen.tfpreg.size(); i++) {
					aux2.add((examen.tfpreg.get(i).getRespuestaTF() == true) ? "Verdadero" : "Falso");
					pes2[i] = examen.tfpreg.get(i).getPeso();
				}
				for (int i = 0; i < examen.rcpreg.size(); i++) {
					aux3.add(examen.rcpreg.get(i).getRespuestaCorta());
					pes3[i] = examen.rcpreg.get(i).getPeso();
				}
				respuestasCorrectasEXM = Stream.of(aux2.toArray(), aux1.toArray(), aux3.toArray()).flatMap(Stream::of).toArray(String[]::new);
				puntajeExam = ArrayUtils.addAll(pes2, pes1);
				puntajeExam = ArrayUtils.addAll(puntajeExam, pes3);
				
				respuestaSeleccionadaUser = new String[respuestasCorrectasEXM.length];
				puntajesUser = new int[respuestasCorrectasEXM.length];
				break;
			}
			case 3: {

				labelCantidadPreguntas.setText("Pregunta " + (iterador + 1) + " de "+ (examen.selecmulpreg.size() + examen.tfpreg.size() + examen.rcpreg.size()));
				labelTipoDePregunta.setText("RESPUESTA CORTA");
				labelTipoDePregunta.setForeground(Color.YELLOW);
				ArrayList<String> aux1 = new ArrayList<>();
				ArrayList<String> aux2 = new ArrayList<>();
				ArrayList<String> aux3 = new ArrayList<>();
				int[] pes1 = new int[examen.selecmulpreg.size()];
				int[] pes2 = new int[examen.tfpreg.size()];
				int[] pes3 = new int[examen.rcpreg.size()];
				for (int i = 0; i < examen.selecmulpreg.size(); i++) {
					aux1.add(examen.selecmulpreg.get(i).getRespuestaCorecta());
					pes1[i] = examen.selecmulpreg.get(i).getPeso();
				}
				for (int i = 0; i < examen.tfpreg.size(); i++) {
					aux2.add((examen.tfpreg.get(i).getRespuestaTF() == true) ? "Verdadero" : "Falso");
					pes2[i] = examen.tfpreg.get(i).getPeso();
				}
				for (int i = 0; i < examen.rcpreg.size(); i++) {
					aux3.add(examen.rcpreg.get(i).getRespuestaCorta());
					pes3[i] = examen.rcpreg.get(i).getPeso();
				}
				respuestasCorrectasEXM = Stream.of(aux3.toArray(), aux2.toArray(), aux1.toArray()).flatMap(Stream::of).toArray(String[]::new);
				puntajeExam = ArrayUtils.addAll(pes3, pes2);
				puntajeExam = ArrayUtils.addAll(puntajeExam, pes1);
				respuestaSeleccionadaUser = new String[respuestasCorrectasEXM.length];
				puntajesUser = new int[respuestasCorrectasEXM.length];
				

				break;
			}
			case 4: {
				labelCantidadPreguntas.setText("Pregunta " + (iterador + 1) + " de " + (examen.selecmulpreg.size()));
				respuestasCorrectasEXM = new String[examen.selecmulpreg.size()];
				respuestaSeleccionadaUser = new String[respuestasCorrectasEXM.length];
				puntajesUser = new int[respuestasCorrectasEXM.length];
				puntajeExam = new int[respuestasCorrectasEXM.length];
				labelTipoDePregunta.setText("SELECCION MULTIPLE");
				labelTipoDePregunta.setForeground(Color.GREEN);
				for (int i = 0; i < examen.selecmulpreg.size(); i++) {
					respuestasCorrectasEXM[i] = examen.selecmulpreg.get(i).getRespuestaCorecta();
					puntajeExam [i] = examen.selecmulpreg.get(i).getPeso();
				}

				break;
			}
			case 5: {
				labelCantidadPreguntas.setText("Pregunta " + (iterador + 1) + " de " + (examen.tfpreg.size()));
				respuestasCorrectasEXM = new String[examen.tfpreg.size()];
				respuestaSeleccionadaUser = new String[respuestasCorrectasEXM.length];
				puntajesUser = new int[respuestasCorrectasEXM.length];
				puntajeExam = new int[respuestasCorrectasEXM.length];
				labelTipoDePregunta.setText("VERDADERO O FALSO");
				labelTipoDePregunta.setForeground(Color.CYAN);
				for (int i = 0; i < examen.selecmulpreg.size(); i++) {
					respuestasCorrectasEXM[i] = (examen.tfpreg.get(i).getRespuestaTF() == true) ? "Verdadero" : "Falso";
					puntajeExam[i] = examen.tfpreg.get(i).getPeso();
				}
				break;
			}
			case 6: {
				labelCantidadPreguntas.setText("Pregunta " + (iterador + 1) + " de " + (examen.rcpreg.size()));
				respuestasCorrectasEXM = new String[examen.rcpreg.size()];
				respuestaSeleccionadaUser = new String[respuestasCorrectasEXM.length];
				puntajesUser = new int[respuestasCorrectasEXM.length];
				puntajeExam = new int[respuestasCorrectasEXM.length];
				labelTipoDePregunta.setText("RESPUESTA CORTA");
				labelTipoDePregunta.setForeground(Color.YELLOW);
				for (int i = 0; i < examen.selecmulpreg.size(); i++) {
					respuestasCorrectasEXM[i] = examen.rcpreg.get(i).getRespuestaCorta();
					puntajeExam[i] = examen.rcpreg.get(i).getPeso();
				}
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + formaExamen);
			}
			/***************************************************************************/

			/************************* PANEL DEL ENUNCIADO ******************************/
			panelEnunciado = new JPanel(new BorderLayout());
			panelEnunciado.setBorder(new EmptyBorder(10, 10, 10, 10));
			panelContenedorPregunta.add(panelEnunciado, BorderLayout.NORTH);

			textEnunciado = new JTextArea();
			textEnunciado.setLineWrap(true);
			textEnunciado.setWrapStyleWord(true);
			textEnunciado.setFont(new Font("Tahoma", Font.PLAIN, 24));
			textEnunciado.setForeground(Color.WHITE);
			textEnunciado.setEditable(false);
			textEnunciado.setPreferredSize(new Dimension(900, 220));

			switch (formaExamen) {
			case 1:
			case 4: {
				textEnunciado.setText(examen.selecmulpreg.get(0).getEnunciado());
				break;
			}
			case 2:
			case 5: {
				textEnunciado.setText(examen.tfpreg.get(0).getEnunciado());
				break;
			}
			case 3:
			case 6: {
				textEnunciado.setText(examen.rcpreg.get(0).getEnunciado());
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + formaExamen);
			}

			scroll = new JScrollPane();
			scroll.setViewportView(textEnunciado);

			panelEnunciado.add(scroll, BorderLayout.CENTER);

			/***************************************************************************/

			/************************* PANEL DE LA RESPUESTA ******************************/
			panelContenedorRespuestasAbsoluto = new JPanel(new BorderLayout());
			panelContenedorRespuestasAbsoluto.setBorder(new EmptyBorder(20, 70, 30, 70));
			panelContenedorRespuestasAbsoluto.setPreferredSize(new Dimension(900, 250));

			panelRespuestaSelecMultiple = new JPanel(new GridLayout(5, 4, 20, 20/* 5, 4, 50, 30 */));

			panelRespuestaTrueFalse = new JPanel(new GridLayout(0, 2, 10, 0));
			panelRespuestaTrueFalse.setPreferredSize(panelRespuestaSelecMultiple.getPreferredSize());

			panelRespuestaCorta = new JPanel(new BorderLayout());
			panelRespuestaCorta.setBorder(new EmptyBorder(60, 20, 60, 20));

			switch (formaExamen) {
			case 1:
			case 4: {
				panelContenedorRespuestasAbsoluto.add(panelRespuestaSelecMultiple, BorderLayout.CENTER);
				agregaBotonesDinamicoSelecMult(examen.selecmulpreg.get(iterador));
				break;
			}
			case 2:
			case 5: {
				panelContenedorRespuestasAbsoluto.add(panelRespuestaTrueFalse, BorderLayout.CENTER);
				agregaBotonesDinamicoTrueFalse(examen.tfpreg.get(iterador));
				break;
			}
			case 3:
			case 6: {
				panelContenedorRespuestasAbsoluto.add(panelRespuestaCorta, BorderLayout.CENTER);
				agregaTextPaneDinamico(examen.rcpreg.get(iterador));
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + formaExamen);
			}

			panelContenedorPregunta.add(panelContenedorRespuestasAbsoluto, BorderLayout.CENTER);
			/***************************************************************************/

			/************************* PANEL DE LOS BOTONES ****************************/
			panelBotones = new JPanel(new BorderLayout());
			panelContenedorBotones.add(panelBotones, BorderLayout.SOUTH);

			btnSiguiente = new JButton("Siguiente");
			btnSiguiente.setFont(new Font("Tahoma", Font.PLAIN, 15));
			btnSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						switch (formaExamen) {
						case 1:
						case 2:
						case 3: {
							if (iterador < (examen.selecmulpreg.size() + examen.tfpreg.size() + examen.rcpreg.size())
									- 1) {
								iterador++;
								labelCantidadPreguntas.setText("Pregunta " + (iterador + 1) + " de "
										+ (examen.selecmulpreg.size() + examen.tfpreg.size() + examen.rcpreg.size()));
								AvanzaRetrocedePregunta(examen);
							}
							break;
						}
						case 4: {
							if (iterador < examen.selecmulpreg.size() - 1) {
								iterador++;
								labelCantidadPreguntas
										.setText("Pregunta " + (iterador + 1) + " de " + examen.selecmulpreg.size());
								AvanzaRetrocedePregunta(examen);
							}
							break;
						}
						case 5: {
							if (iterador < examen.tfpreg.size() - 1) {
								iterador++;
								labelCantidadPreguntas
										.setText("Pregunta " + (iterador + 1) + " de " + examen.tfpreg.size());
								AvanzaRetrocedePregunta(examen);
							}
							break;
						}
						case 6: {
							if (iterador < examen.rcpreg.size() - 1) {
								iterador++;
								labelCantidadPreguntas
										.setText("Pregunta " + (iterador + 1) + " de " + examen.rcpreg.size());
								AvanzaRetrocedePregunta(examen);
							}
							break;
						}
						default:
							throw new IllegalArgumentException("Unexpected value: " + formaExamen);
						}
					} catch (Exception e) {
						System.err.println(e);
					}
				}
			});

			btnAnterior = new JButton("Anterior");
			btnAnterior.setFont(new Font("Tahoma", Font.PLAIN, 15));
			btnAnterior.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						if (iterador > 0 && (formaExamen == 1 || formaExamen == 2 || formaExamen == 3)) {
							iterador--;
							labelCantidadPreguntas.setText("Pregunta " + (iterador + 1) + " de "
									+ (examen.selecmulpreg.size() + examen.tfpreg.size() + examen.rcpreg.size()));
							panelRespuestaSelecMultiple.removeAll();
							AvanzaRetrocedePregunta(examen);
						}
						if (iterador > 0 && formaExamen == 4) {
							iterador--;
							labelCantidadPreguntas
									.setText("Pregunta " + (iterador + 1) + " de " + examen.selecmulpreg.size());
							AvanzaRetrocedePregunta(examen);
						}
						if (iterador > 0 && formaExamen == 5) {
							iterador--;
							labelCantidadPreguntas
									.setText("Pregunta " + (iterador + 1) + " de " + examen.tfpreg.size());
							AvanzaRetrocedePregunta(examen);
						}
						if (iterador > 0 && formaExamen == 6) {
							iterador--;
							labelCantidadPreguntas
									.setText("Pregunta " + (iterador + 1) + " de " + examen.rcpreg.size());
							AvanzaRetrocedePregunta(examen);
						}
					} catch (Exception e) {
						System.err.println(e);
					}
				}
			});
			panelBotones.add(btnAnterior, BorderLayout.WEST);
			panelBotones.add(btnSiguiente, BorderLayout.EAST);
			/***************************************************************************/

			return panelIniciador;
		}

		/**
		 * Funcion para avanzar y retroceder entre preguntas, funciona con un iterador
		 * 
		 * 
		 * 
		 * @param examen Examen generado
		 * 
		 * 
		 */
		void AvanzaRetrocedePregunta(Exam examen) {

			switch (formaExamen) {
			case 1: {// selec mul - tf - rc
				if (iterador < examen.selecmulpreg.size()) {
					pack();
					SelMulActualizaPanel(examen);
				} else if (iterador >= (examen.selecmulpreg.size())
						&& iterador < (examen.selecmulpreg.size() + examen.tfpreg.size())) {
					pack();
					TrueFalseActualizaPanel(examen);
				} else if (iterador >= (examen.selecmulpreg.size() + examen.tfpreg.size())
						&& iterador < (examen.selecmulpreg.size() + examen.tfpreg.size() + examen.rcpreg.size())) {
					pack();
					RespCortActualizaPanel(examen);
				}
				break;
			}
			case 2: {// tf - selec mul - rc
				if (iterador < examen.tfpreg.size()) {
					pack();
					TrueFalseActualizaPanel(examen);
				} else if (iterador >= (examen.tfpreg.size())
						&& iterador < (examen.selecmulpreg.size() + examen.tfpreg.size())) {
					pack();
					SelMulActualizaPanel(examen);
				} else if (iterador >= (examen.tfpreg.size() + examen.selecmulpreg.size())
						&& iterador < (examen.selecmulpreg.size() + examen.tfpreg.size() + examen.rcpreg.size())) {
					pack();
					RespCortActualizaPanel(examen);
				}
				break;
			}
			case 3: {// rc - tf - selec mul
				if (iterador < examen.rcpreg.size()) {
					pack();
					RespCortActualizaPanel(examen);

				} else if (iterador >= (examen.rcpreg.size())
						&& iterador < (examen.rcpreg.size() + examen.tfpreg.size())) {
					pack();
					TrueFalseActualizaPanel(examen);

				} else if (iterador >= (examen.tfpreg.size() + examen.rcpreg.size())
						&& iterador < (examen.selecmulpreg.size() + examen.tfpreg.size() + examen.rcpreg.size())) {
					pack();
					SelMulActualizaPanel(examen);
				}
				break;
			}
			case 4: {
				if (iterador < examen.selecmulpreg.size()) {
					pack();
					SelMulActualizaPanel(examen);
				}
				break;
			}
			case 5: {
				if (iterador < examen.tfpreg.size()) {
					pack();
					TrueFalseActualizaPanel(examen);
				}
				break;
			}
			case 6: {
				if (iterador < examen.rcpreg.size()) {
					pack();
					RespCortActualizaPanel(examen);
				}
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + formaExamen);
			}
		}

		/**
		 * Panel de preguntas de seleccion multiple contiene el enunciado. el panel de
		 * alternativas
		 * 
		 * 
		 * @param examen Examen generado
		 * 
		 * 
		 */
		public void SelMulActualizaPanel(Exam examen) {
			panelContenedorRespuestasAbsoluto.removeAll();
			panelContenedorRespuestasAbsoluto.add(panelRespuestaSelecMultiple, BorderLayout.CENTER);
			panelContenedorRespuestasAbsoluto.updateUI();

			panelRespuestaSelecMultiple.removeAll();
			labelTipoDePregunta.setText("SELECCIÓN MULTIPLE");
			labelTipoDePregunta.setForeground(Color.GREEN);
			switch (formaExamen) {
			case 1:
			case 4: {
				textEnunciado.setText(examen.selecmulpreg.get(iterador).getEnunciado());
				agregaBotonesDinamicoSelecMult(examen.selecmulpreg.get(iterador));
				break;
			}
			case 2: {
				textEnunciado.setText(examen.selecmulpreg.get(iterador - examen.tfpreg.size()).getEnunciado());
				agregaBotonesDinamicoSelecMult(examen.selecmulpreg.get(iterador - examen.tfpreg.size()));
				break;
			}
			case 3: {
				textEnunciado.setText(examen.selecmulpreg.get(iterador - examen.tfpreg.size() - examen.rcpreg.size()).getEnunciado());
				agregaBotonesDinamicoSelecMult(examen.selecmulpreg.get(iterador - examen.tfpreg.size() - examen.rcpreg.size()));
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + formaExamen);
			}
			RbutonSeleccionado(botonesSelecMult);
		}

		/**
		 * Contiene el panel del enunciado como tambien el panel de respuestas
		 * 
		 * 
		 * @param examen Examen generado
		 * 
		 * 
		 */
		public void TrueFalseActualizaPanel(Exam examen) {
			panelContenedorRespuestasAbsoluto.removeAll();
			panelContenedorRespuestasAbsoluto.add(panelRespuestaTrueFalse, BorderLayout.CENTER);
			panelContenedorRespuestasAbsoluto.updateUI();
			panelRespuestaTrueFalse.removeAll();

			labelTipoDePregunta.setText("VERDADERO O FALSO");
			labelTipoDePregunta.setForeground(Color.CYAN);
			switch (formaExamen) {
			case 1: {
				textEnunciado.setText(examen.tfpreg.get(iterador - examen.selecmulpreg.size()).getEnunciado());
				agregaBotonesDinamicoTrueFalse(examen.tfpreg.get(iterador - examen.selecmulpreg.size()));
				break;
			}
			case 2:
			case 5: {
				textEnunciado.setText(examen.tfpreg.get(iterador).getEnunciado());
				agregaBotonesDinamicoTrueFalse(examen.tfpreg.get(iterador));
				break;
			}
			case 3: {
				textEnunciado.setText(examen.tfpreg.get(iterador - examen.rcpreg.size()).getEnunciado());
				agregaBotonesDinamicoTrueFalse(examen.tfpreg.get(iterador - examen.rcpreg.size()));
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + formaExamen);
			}
			RbutonSeleccionado(botonesTF);
		}

		/**
		 * Contiene el panel de enunciado como tambien el panel para escribir la
		 * respuesta
		 * 
		 * @param examen Examen generado
		 * 
		 * 
		 */
		public void RespCortActualizaPanel(Exam examen) {
			panelContenedorRespuestasAbsoluto.removeAll();
			panelContenedorRespuestasAbsoluto.add(panelRespuestaCorta, BorderLayout.CENTER);
			panelContenedorRespuestasAbsoluto.updateUI();
			panelRespuestaCorta.removeAll();
			labelTipoDePregunta.setText("RESPUESTA CORTA");
			labelTipoDePregunta.setForeground(Color.YELLOW);
			switch (formaExamen) {
			case 1: {
				textEnunciado.setText(examen.rcpreg.get(iterador - examen.selecmulpreg.size() - examen.tfpreg.size()).getEnunciado());
				agregaTextPaneDinamico(examen.rcpreg.get(iterador - examen.selecmulpreg.size() - examen.tfpreg.size()));
				break;
			}
			case 2: {
				textEnunciado.setText(
						examen.rcpreg.get(iterador - examen.selecmulpreg.size() - examen.tfpreg.size()).getEnunciado());
				agregaTextPaneDinamico(examen.rcpreg.get(iterador - examen.selecmulpreg.size() - examen.tfpreg.size()));
				break;
			}
			case 3:
			case 6: {
				textEnunciado.setText(examen.rcpreg.get(iterador).getEnunciado());
				agregaTextPaneDinamico(examen.rcpreg.get(iterador));
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + formaExamen);
			}
		}

		/**
		 * Hace que al volver entre preguntas la eleccion anterior no se pierda.
		 * 
		 * @param botones Botones
		 * 
		 */
		public void RbutonSeleccionado(ArrayList<JRadioButton> botones) {
			if (botones != null) {
				for (int i = 0; i < botones.size(); i++) {
					if (botones.get(i).getText().equalsIgnoreCase(respuestaSeleccionadaUser[iterador])) {
						botones.get(i).setSelected(true);
						break;
					}
				}
			}
		}

		/**
		 * Agrega alternativas dinamicamente en el panel.
		 *
		 * @param pregunta Pregunta de seleccion multiple
		 * 
		 * 
		 */
		void agregaBotonesDinamicoSelecMult(Selec_Mul_Pregunta pregunta) {
			if (pregunta != null) {
				ArregloRbutton = new JRadioButton[pregunta.getopciones().length];
				botonesSelecMult = new ArrayList<JRadioButton>();
				grupoDeBotones = new ButtonGroup();
				for (int i = 0; i < ArregloRbutton.length; i++) {
					ArregloRbutton[i] = new JRadioButton(pregunta.getopciones()[i]);
					ArregloRbutton[i].setFont(new Font("Tahoma", Font.PLAIN, 20));
					botonesSelecMult.add(ArregloRbutton[i]);
					grupoDeBotones.add(ArregloRbutton[i]);
					panelRespuestaSelecMultiple.add(ArregloRbutton[i]);
					int aux = i;
					ArregloRbutton[i].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							respuestaSeleccionadaUser[iterador] = ArregloRbutton[aux].getText();

							if (ArregloRbutton[aux].getText().equalsIgnoreCase(pregunta.getRespuestaCorecta())) {
								puntajesUser[iterador] = pregunta.getPeso();
							} else {
								puntajesUser[iterador] = 0;
							}
						}
					});
				}
				panelRespuestaSelecMultiple.updateUI();
				pack();
			} else {
				throw new IllegalArgumentException("La pregunta es nula");
			}

		}

		/**
		 * Agrega panel de respuestas True Flase dinamicamente por cada pregunta
		 * 
		 * @param pregunta Pregunta de verdareo o falso
		 * 
		 */
		void agregaBotonesDinamicoTrueFalse(TFpreguntas pregunta) {
			if (pregunta != null) {
				ArregloRbutton = new JRadioButton[2];
				botonesTF = new ArrayList<JRadioButton>();
				grupoDeBotones = new ButtonGroup();
				String[] opcion = { "Verdadero", "Falso" };
				String rta = (pregunta.getRespuestaTF() == true) ? "Verdadero" : "Falso";
				for (int i = 0; i < ArregloRbutton.length; i++) {
					ArregloRbutton[i] = new JRadioButton(opcion[i]);
					ArregloRbutton[i].setHorizontalAlignment(SwingConstants.CENTER);
					ArregloRbutton[i].setFont(new Font("Tahoma", Font.PLAIN, 20));
					botonesTF.add(ArregloRbutton[i]);
					grupoDeBotones.add(ArregloRbutton[i]);
					panelRespuestaTrueFalse.add(ArregloRbutton[i]);
					int aux = i;
					ArregloRbutton[i].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							respuestaSeleccionadaUser[iterador] = ArregloRbutton[aux].getText();

							if (ArregloRbutton[aux].getText().equalsIgnoreCase(rta)) {
								puntajesUser[iterador] = pregunta.getPeso();
							} else {
								puntajesUser[iterador] = 0;
							}
						}
					});
				}
				panelRespuestaTrueFalse.updateUI();
				pack();
			} else {
				throw new IllegalArgumentException("La pregunta es nula");
			}
		}

		/**
		 * Agrega JTextPane dinamicamente por cada pregunta de respuestas cortas
		 * 
		 * 
		 * @param pregunta Pregunta de respuestas cortas
		 * 
		 * 
		 */
		public void agregaTextPaneDinamico(Resp_Cortas_Pregunta pregunta) {
			StringSimilar comparador = new StringSimilar();
			JTextField txtRespuestaCortaPgr = new JTextField();
			txtRespuestaCortaPgr.setText((respuestaSeleccionadaUser[iterador] == null) ? "" : (respuestaSeleccionadaUser[iterador]));
			txtRespuestaCortaPgr.setBackground(Color.WHITE);
			txtRespuestaCortaPgr.setForeground(Color.BLACK);
			txtRespuestaCortaPgr.setFont(new Font("Thaoma", Font.PLAIN, 23));
			panelRespuestaCorta.add(txtRespuestaCortaPgr, BorderLayout.CENTER);
			txtRespuestaCortaPgr.addFocusListener(new FocusListener() {
				public void focusLost(FocusEvent arg0) {
					respuestaSeleccionadaUser[iterador] = txtRespuestaCortaPgr.getText();		
					if ( ((int) (((double) comparador.similarity(txtRespuestaCortaPgr.getText(), pregunta.getRespuestaCorta())) * 100))  > 68) {
						puntajesUser[iterador] = pregunta.getPeso();
					} else {
						puntajesUser[iterador] = 0;
					}
				}

				public void focusGained(FocusEvent arg0) {
				}
			});

			panelRespuestaCorta.updateUI();
			pack();
		}

		/**
		 * Tiempo del examen, si el tiempo a la hora de creacion del examen no es
		 * establecido el examen es ilimitado
		 */
		private ActionListener acciones = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (s == 0) {
					s = 60;
					m--;
				}

				if (m == 0) {
					labelS.setForeground(Color.RED);
					labelM.setForeground(Color.red);
				}

				if (m < 0) {
					JOptionPane.showMessageDialog(rootPane, "<html>Tiempo Finalizado<br>Examen Terminado</html>");
					m = 0;
					s = 0;
					tiempo.stop();
					ConvierteExamenJson json = new ConvierteExamenJson();

					for (int i = 0; i < respuestaSeleccionadaUser.length; i++) {
						if (respuestaSeleccionadaUser[i] == null || respuestaSeleccionadaUser[i].isBlank()) {
							respuestaSeleccionadaUser[i] = "No contestó";
						}
					}

					json.guardaNotasAlumno(puntajesUser, puntajeExam, respuestaSeleccionadaUser, respuestasCorrectasEXM, getDireccion() + "\\" + getNombreAlumno());
					if (m > 0) {
						tiempo.stop();
					}
					panelMaster.removeAll();
					panelMaster.add(MenuAlumno());
					panelMaster.updateUI();
					pack();
					setLocationRelativeTo(null);
				} else {

					s--;
					if (s < 10) {
						labelS.setText("0" + s + " minutos");
						flag = false;
					}
					if (m < 10) {
						labelM.setText("Tiempo 0" + m + ":");
						if (s < 10) {
							labelS.setText("0" + s + " minutos");
						} else {
							labelS.setText("" + s + " minutos");
						}
						flag = false;
					}
					if (flag) {
						labelM.setText("Tiempo " + m + ":");
						labelS.setText("" + s + " minutos");
					}
				}
			}
		};
	}

}
