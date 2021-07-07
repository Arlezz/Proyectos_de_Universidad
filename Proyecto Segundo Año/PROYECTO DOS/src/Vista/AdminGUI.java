package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.mysql.jdbc.ResultSetMetaData;
import com.placeholder.PlaceHolder;

/**
 * @author Anton 
 * La clase AdminGUI contiene 2 funciones 
 * muy escenciales las cuales son las de 
 * agregar y eliminar profesores a la 
 * base de datos.
 *
 *
 */
class AdminGUI extends JFrame {


	private static final long serialVersionUID = 2969976089813766930L;

	private JPanel panelMayorRegistro;
	private JPanel rutPanel;
	private JPanel clavePanel;
	private JPanel confirmaClavePanel;
	private JPanel panelAgregarProfesor;
	private JPanel panelEliminarProfesor;

	private JLabel lblNombreAgrega;
	private JLabel lblApellidoAgrega;
	private JLabel lblCorreoAgrega;
	private JLabel lblRutProfesorAgrega;
	private JLabel lblConfirmaClave;
	private JLabel lblContraseniaProfesor;
	private JLabel lblNombreElimina;
	private JLabel lblApellidoElimina;
	private JLabel lblCorreoElimina;
	private JLabel lblRutProfesorElimina;

	private JTextField textNombreAgrega;
	private JTextField textApellidoAgrega;
	private JTextField textRutAgrega;
	private JTextField textCorreoAgrega;
	private JTextField textNombreElimina;
	private JTextField textApellidoElimina;
	private JTextField textRutElimina;
	private JTextField textCorreoElimina;

	private JPasswordField textClave;
	private JPasswordField textConfirmaClave;
	private Modelo.admin adm;
	private JTable tablaDatosProfesores;
	int cantidadFilas = 0;
	String id;
	PlaceHolder holder;

	/**
	 * Modelo que se usa para la tabla donde se agregan los profesores.
	 * Tambien hace que las filas y columnas no se puedan editar.
	 */

	DefaultTableModel modelo = new DefaultTableModel() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 4855303865336763594L;

		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};


	/**
	 * Metodo contructor de AdminGUI el cual abre la ventana
	 * de iniciar sesion para el administrador, pide una 
	 * contraseña y la verifica, si esta coincide con la de los registros
	 * se accede a las funciones del administrador.
	 */
	public AdminGUI() {
		setTitle("ExamSoft");
		try {
			Image imagenLogo = ImageIO.read(new File(new File(".").getCanonicalPath() + "\\res\\soft.png"));
			setIconImage(imagenLogo);
		} catch (Exception e) {
			
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panelMayorRegistro = new JPanel(new BorderLayout());
		setContentPane(panelMayorRegistro);
		JMenuBar barra = new JMenuBar();
		panelMayorRegistro.add(barra, BorderLayout.NORTH);
		JMenu opciones = new JMenu("Opciones");
		barra.add(opciones);
		JMenuItem opcionVolver = new JMenuItem("Volver");
		opcionVolver.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				new InicioSesion();
			}
		});
		opciones.add(opcionVolver);
		panelMayorRegistro.add(inicioSesionPanel(), BorderLayout.CENTER);
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Setea en vacio los JTextField de los paneles "agregar y eliminar" profesor.
	 */
	private void limpiarCajas() {
		textNombreAgrega.setText(null);
		textApellidoAgrega.setText(null);
		textCorreoAgrega.setText(null);
		textRutAgrega.setText(null);
		textClave.setText(null);
		textConfirmaClave.setText(null);

		textNombreElimina.setText(null);
		textApellidoElimina.setText(null);
		textCorreoElimina.setText(null);
		textRutElimina.setText(null);
	}

	/**
	 * Verifica usando expresiones regulares si el rut cumple el formato 
	 * establecido.
	 * @param rut String ingresado por el usuario
	 * @return	boolean
	 * 
	 *  
	 */
	public static boolean validarut(String rut) {
		return rut.matches("^([0-9]{8,9}-[0-9|k|K])$");
	}

	/**
	 * 
	 * Panel de inicio de sesion donde lo unico que se le pide al usuario
	 * es la contraseña ya que el administrador en este caso solo puede ser
	 * uno solo.
	 * 
	 * @return JPanel
	 * 
	 * 
	 */
	public JPanel inicioSesionPanel() {
		
		adm = new Modelo.admin();//Instancio un objeto "admin" para conprobar la contraseña.
		JPanel IniciadorInicioSesionAdmin = new JPanel(new BorderLayout(0, 10));//panel que contiene todos los conponentes y sera retornado
		IniciadorInicioSesionAdmin.setBorder(new EmptyBorder(30, 30, 30, 30));

		JPanel panelLabel = new JPanel(new BorderLayout(0, 40));//panel donde ira el titulo
		panelLabel.setBorder(new EmptyBorder(0, 20, 20, 20));
		IniciadorInicioSesionAdmin.add(panelLabel, BorderLayout.NORTH);

		JPanel panelTextField = new JPanel(new BorderLayout(0, 5));//panel donde iran los campos para ingresar datos
		IniciadorInicioSesionAdmin.add(panelTextField, BorderLayout.CENTER);

		JPanel panelBotones = new JPanel(new BorderLayout());//panel del boton ingresar
		IniciadorInicioSesionAdmin.add(panelBotones, BorderLayout.SOUTH);

		JLabel encabezado = new JLabel("<html><div style='text-align: center;'>" + "Inicio de Sesión<br>Administrador" + "</div></html>");//titulo del panel
		encabezado.setFont(new Font("Tahoma", Font.PLAIN, 30));
		encabezado.setHorizontalAlignment(SwingConstants.CENTER);
		panelLabel.add(encabezado, BorderLayout.NORTH);

		JLabel lblImagenAdminInicSes = new JLabel();//imagen del panel referenciando al admin
		String ubicacionImagen = "";
		try {

			ubicacionImagen = new File(".").getCanonicalPath() + "\\res\\admin.png";//ubicacion de dodne se saca la imagen
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		ImageIcon imagenAlumno = new ImageIcon(ubicacionImagen);
		lblImagenAdminInicSes.setIcon(new ImageIcon(imagenAlumno.getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT)));//se setea la imagen
		lblImagenAdminInicSes.setHorizontalAlignment(SwingConstants.CENTER);
		panelLabel.add(lblImagenAdminInicSes, BorderLayout.CENTER);

		JLabel labelPass = new JLabel("Contraseña");//label de ayuda para la contraseña
		labelPass.setFont(new Font("Tahoma", Font.PLAIN, 20));
		labelPass.setHorizontalAlignment(SwingConstants.LEFT);
		panelTextField.add(labelPass, BorderLayout.NORTH);

		JPasswordField admPass = new JPasswordField();//caja de texto donde se escribe la contraseña para ser verificada
		admPass.setForeground(Color.BLACK);
		admPass.addKeyListener(new KeyAdapter() {//se le agregan funciones al JPasswordField
			@Override
			public void keyReleased(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {//si presiona "Enter" en la caja de texto se ingresa a la plataforma
				String clave = "";
				for (char c : admPass.getPassword()) {
					clave += c;
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (verificaAdm(clave)) {
						panelMayorRegistro.removeAll();
						panelMayorRegistro.add(MenuAdmin());
						panelMayorRegistro.updateUI();
						pack();
						setLocationRelativeTo(null);
					} else {
						JOptionPane.showMessageDialog(null, "Clave incorrecta");
					}
				}
			}
		});
		admPass.setFont(new Font("Tahoma", Font.PLAIN, 20));
		admPass.setPreferredSize(new Dimension(200, 30));
		admPass.setBackground(Color.WHITE);
		panelTextField.add(admPass, BorderLayout.CENTER);

		JButton btnIngresar = new JButton("Ingresar");//boton para ingresar a la plataforma
		btnIngresar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String clave = "";
				for (char c : admPass.getPassword()) {
					clave += c;
				}
				if (verificaAdm(clave)) {
					panelMayorRegistro.removeAll();
					panelMayorRegistro.add(MenuAdmin());
					panelMayorRegistro.updateUI();
					pack();
					setLocationRelativeTo(null);
				} else {
					JOptionPane.showMessageDialog(null, "Clave incorrecta");
				}
			}
		});

		btnIngresar.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panelBotones.add(btnIngresar, BorderLayout.CENTER);

		return IniciadorInicioSesionAdmin;
	}

	/**
	 * Panel donde se encuentran los campos para agregar un profesor al sistema.
	 * 
	 * @return JPanel
	 * 
	 */
	public JPanel agregaPorfesor() {
		JPanel IniciadorAgregaProfPanel = new JPanel();//panel que contiene todos los componentes y se retorna.
		IniciadorAgregaProfPanel.setLayout(new BoxLayout(IniciadorAgregaProfPanel, BoxLayout.Y_AXIS));

		JPanel panelIngresoDatosAgregaProf = new JPanel();
		panelIngresoDatosAgregaProf.setLayout(new BoxLayout(panelIngresoDatosAgregaProf, BoxLayout.Y_AXIS));
		IniciadorAgregaProfPanel.add(panelIngresoDatosAgregaProf);
		/************************** PANELES *******************************/

		JPanel nombrePanel = new JPanel(new GridLayout(0, 2, 30, 0));
		panelIngresoDatosAgregaProf.add(nombrePanel);

		panelIngresoDatosAgregaProf.add(Box.createRigidArea(new Dimension(0, 30)));

		JPanel apellidoPanel = new JPanel(new GridLayout(0, 2, 30, 0));
		panelIngresoDatosAgregaProf.add(apellidoPanel);

		panelIngresoDatosAgregaProf.add(Box.createRigidArea(new Dimension(0, 30)));

		rutPanel = new JPanel(new GridLayout(0, 2, 30, 0));
		panelIngresoDatosAgregaProf.add(rutPanel);

		panelIngresoDatosAgregaProf.add(Box.createRigidArea(new Dimension(0, 30)));

		JPanel correoPanel = new JPanel(new GridLayout(0, 2, 30, 0));
		panelIngresoDatosAgregaProf.add(correoPanel);

		panelIngresoDatosAgregaProf.add(Box.createRigidArea(new Dimension(0, 30)));

		clavePanel = new JPanel(new GridLayout(0, 2, 30, 0));
		panelIngresoDatosAgregaProf.add(clavePanel);

		panelIngresoDatosAgregaProf.add(Box.createRigidArea(new Dimension(0, 30)));

		confirmaClavePanel = new JPanel(new GridLayout(0, 2, 30, 0));
		panelIngresoDatosAgregaProf.add(confirmaClavePanel);

		/******************************************************************/

		lblNombreAgrega = new JLabel("Nombre");
		lblNombreAgrega.setFont(new Font("Calibri", Font.BOLD, 20));
		nombrePanel.add(lblNombreAgrega);

		textNombreAgrega = new JTextField(15);
		textNombreAgrega.setBackground(Color.WHITE);
		textNombreAgrega.addKeyListener(new letraMayuscula());
		holder = new PlaceHolder(textNombreAgrega, "Ingrese un nombre");
		nombrePanel.add(textNombreAgrega);

		lblApellidoAgrega = new JLabel("Apellido");
		lblApellidoAgrega.setFont(new Font("Calibri", Font.BOLD, 20));
		apellidoPanel.add(lblApellidoAgrega);

		textApellidoAgrega = new JTextField(15);
		textApellidoAgrega.setBackground(Color.WHITE);
		textApellidoAgrega.addKeyListener(new letraMayuscula());
		holder = new PlaceHolder(textApellidoAgrega, "Ingrese un apellido");
		apellidoPanel.add(textApellidoAgrega);

		lblRutProfesorAgrega = new JLabel("Rut");
		lblRutProfesorAgrega.setFont(new Font("Calibri", Font.BOLD, 20));
		rutPanel.add(lblRutProfesorAgrega);

		textRutAgrega = new JTextField(15);
		textRutAgrega.setBackground(Color.WHITE);
		textRutAgrega.addKeyListener(new noAgregaLetrasRUTField());
		holder = new PlaceHolder(textRutAgrega, "RUT sin punto y con guión");
		rutPanel.add(textRutAgrega);

		lblCorreoAgrega = new JLabel("Correo");
		lblCorreoAgrega.setFont(new Font("Calibri", Font.BOLD, 20));
		correoPanel.add(lblCorreoAgrega);

		textCorreoAgrega = new JTextField();
		textCorreoAgrega.setBackground(Color.WHITE);
		holder = new PlaceHolder(textCorreoAgrega, "Ingrese un correo");
		correoPanel.add(textCorreoAgrega);

		lblContraseniaProfesor = new JLabel("Contrase\u00F1a");
		lblContraseniaProfesor.setFont(new Font("Calibri", Font.BOLD, 20));
		clavePanel.add(lblContraseniaProfesor);

		textClave = new JPasswordField(15);
		textClave.setBackground(Color.WHITE);
		holder = new PlaceHolder(textClave, "*********");
		clavePanel.add(textClave, BorderLayout.EAST);

		lblConfirmaClave = new JLabel("Confirmar contrase\u00F1a");
		lblConfirmaClave.setFont(new Font("Calibri", Font.BOLD, 20));
		confirmaClavePanel.add(lblConfirmaClave, BorderLayout.WEST);

		textConfirmaClave = new JPasswordField(15);
		textConfirmaClave.setBackground(Color.WHITE);
		holder = new PlaceHolder(textConfirmaClave, "*********");
		confirmaClavePanel.add(textConfirmaClave, BorderLayout.EAST);

		return IniciadorAgregaProfPanel;
	}

	/**
	 * Panel que contiene los campos para la eliminacion de los profesores en el sistema.
	 * @return JPanel
	 * 
	 */
	public JPanel EliminaProfe() {

		JPanel IniciadorEliminaProfPanel = new JPanel();
		IniciadorEliminaProfPanel.setLayout(new BoxLayout(IniciadorEliminaProfPanel, BoxLayout.Y_AXIS));

		JPanel panelIngresoDatosEliminaProf = new JPanel();
		panelIngresoDatosEliminaProf.setLayout(new BoxLayout(panelIngresoDatosEliminaProf, BoxLayout.Y_AXIS));
		IniciadorEliminaProfPanel.add(panelIngresoDatosEliminaProf);
		/************************** PANELES *******************************/

		JPanel nombrePanel = new JPanel(new GridLayout(0, 2, 30, 0));
		panelIngresoDatosEliminaProf.add(nombrePanel);

		panelIngresoDatosEliminaProf.add(Box.createRigidArea(new Dimension(0, 40)));

		JPanel apellidoPanel = new JPanel(new GridLayout(0, 2, 30, 0));
		panelIngresoDatosEliminaProf.add(apellidoPanel);

		panelIngresoDatosEliminaProf.add(Box.createRigidArea(new Dimension(0, 40)));

		rutPanel = new JPanel(new GridLayout(0, 2, 30, 0));
		panelIngresoDatosEliminaProf.add(rutPanel);

		panelIngresoDatosEliminaProf.add(Box.createRigidArea(new Dimension(0, 40)));

		JPanel correoPanel = new JPanel(new GridLayout(0, 2, 30, 0));
		panelIngresoDatosEliminaProf.add(correoPanel);

		panelIngresoDatosEliminaProf.add(Box.createRigidArea(new Dimension(0, 32)));

		/******************************************************************/

		lblNombreElimina = new JLabel("Nombre");
		lblNombreElimina.setFont(new Font("Calibri", Font.BOLD, 22));
		nombrePanel.add(lblNombreElimina);

		textNombreElimina = new JTextField(15);
		textNombreElimina.setFont(new Font("Tahoma", Font.PLAIN, 18));
		textNombreElimina.setForeground(Color.BLACK);
		textNombreElimina.setBackground(Color.WHITE);
		textNombreElimina.addKeyListener(new letraMayuscula());
		nombrePanel.add(textNombreElimina);

		lblApellidoElimina = new JLabel("Apellido");
		lblApellidoElimina.setFont(new Font("Calibri", Font.BOLD, 22));
		apellidoPanel.add(lblApellidoElimina);

		textApellidoElimina = new JTextField(15);
		textApellidoElimina.setFont(new Font("Tahoma", Font.PLAIN, 18));
		textApellidoElimina.setForeground(Color.BLACK);
		textApellidoElimina.setBackground(Color.WHITE);
		textApellidoElimina.addKeyListener(new letraMayuscula());
		apellidoPanel.add(textApellidoElimina);

		lblRutProfesorElimina = new JLabel("Rut");
		lblRutProfesorElimina.setFont(new Font("Calibri", Font.BOLD, 22));
		rutPanel.add(lblRutProfesorElimina);

		textRutElimina = new JTextField(15);
		textRutElimina.addKeyListener(new noAgregaLetrasRUTField());
		textRutElimina.setFont(new Font("Tahoma", Font.PLAIN, 18));
		textRutElimina.setForeground(Color.BLACK);
		textRutElimina.setBackground(Color.WHITE);
		rutPanel.add(textRutElimina);

		lblCorreoElimina = new JLabel("Correo");
		lblCorreoElimina.setFont(new Font("Calibri", Font.BOLD, 22));
		correoPanel.add(lblCorreoElimina);

		textCorreoElimina = new JTextField();
		textCorreoElimina.setFont(new Font("Tahoma", Font.PLAIN, 18));
		textCorreoElimina.setForeground(Color.BLACK);
		textCorreoElimina.setBackground(Color.WHITE);
		correoPanel.add(textCorreoElimina);

		return IniciadorEliminaProfPanel;
	}

	/**
	 * 
	 * Panel que contiene los paneles de agrega y elimina profesor, ademas de un icono e informacion del usuario que ingresó.
	 * @return JPanel
	 * 
	 */
	public JPanel MenuAdmin() {

		JPanel panelContenedorMenu = new JPanel(new BorderLayout());
		panelContenedorMenu.setBorder(new EmptyBorder(0, 0, 20, 20));

		JPanel panelContenedorSuperior = new JPanel(new BorderLayout());
		panelContenedorMenu.add(panelContenedorSuperior, BorderLayout.NORTH);
		JPanel panelContenedorDerecho = new JPanel(new BorderLayout(0, 10));
		panelContenedorMenu.add(panelContenedorDerecho, BorderLayout.CENTER);
		JPanel panelContenedorIzquierdo = new JPanel(new BorderLayout());
		panelContenedorIzquierdo.setBorder(new EmptyBorder(10, 10, 0, 10));
		panelContenedorMenu.add(panelContenedorIzquierdo, BorderLayout.WEST);
		JPanel panelImagen = new JPanel(new BorderLayout());
		panelImagen.setBorder(BorderFactory.createLineBorder(new Color(33, 48, 71), 2, true));
		panelContenedorIzquierdo.add(panelImagen, BorderLayout.CENTER);
		JPanel panelImagenInterno = new JPanel();
		panelImagenInterno.setLayout(new BoxLayout(panelImagenInterno, BoxLayout.Y_AXIS));
		panelImagenInterno.setBorder(new EmptyBorder(0, 20, 0, 20));
		panelImagen.add(panelImagenInterno, BorderLayout.CENTER);

		JMenuBar menuSuperior = new JMenuBar();
		panelContenedorSuperior.add(menuSuperior, BorderLayout.NORTH);
		JMenu menuOpciones = new JMenu("Opciones");
		menuSuperior.add(menuOpciones);
		JMenuItem opcionVolver = new JMenuItem("Volver");
		opcionVolver.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				new AdminGUI();
			}
		});
		menuOpciones.add(opcionVolver);
		JMenuItem opcionSalir = new JMenuItem("Salir");
		opcionSalir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		menuOpciones.add(opcionSalir);

		panelImagenInterno.add(Box.createRigidArea(new Dimension(0, 180)));

		JPanel panelLabelInfo = new JPanel(new BorderLayout(0, 0));
		panelImagenInterno.add(panelLabelInfo);

		panelImagenInterno.add(Box.createRigidArea(new Dimension(0, 180)));

		JLabel labelImagenAdmin = new JLabel();
		String ubicacionImagen = "";
		try {
			ubicacionImagen = new File(".").getCanonicalPath() + "\\res\\admin.png";

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		ImageIcon imagenAdmin = new ImageIcon(ubicacionImagen);
		labelImagenAdmin.setHorizontalAlignment(SwingConstants.CENTER);
		labelImagenAdmin
				.setIcon(new ImageIcon(imagenAdmin.getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT)));
		panelLabelInfo.add(labelImagenAdmin, BorderLayout.NORTH);

		JLabel labelInfoUser = new JLabel();
		labelInfoUser.setText("<html><font color=\"white\">Admin: </font>Antony Rodriguez</html>");
		labelInfoUser.setFont(new Font("Tahoma", Font.PLAIN, 20));
		labelInfoUser.setForeground(new Color(213, 18, 18));
		labelInfoUser.setHorizontalAlignment(SwingConstants.CENTER);
		panelLabelInfo.add(labelInfoUser, BorderLayout.CENTER);

		JTabbedPane PanelControlador = new JTabbedPane();
		panelContenedorDerecho.add(PanelControlador, BorderLayout.NORTH);

		JPanel panelTablaDeDatos = new JPanel(new BorderLayout());
		panelContenedorDerecho.add(panelTablaDeDatos, BorderLayout.CENTER);

		panelAgregarProfesor = new JPanel(new BorderLayout(0, 10));
		panelAgregarProfesor.setPreferredSize(new Dimension(0, 0));
		PanelControlador.addTab("Agregar Profesor", null, panelAgregarProfesor, null);

		panelEliminarProfesor = new JPanel(new BorderLayout(0, 20));
		PanelControlador.addTab("Eliminar Profesor", null, panelEliminarProfesor, null);

		JPanel panelDatosAgregaProf = agregaPorfesor();
		panelDatosAgregaProf.setBorder(new EmptyBorder(30, 20, 0, 20));
		panelAgregarProfesor.add(panelDatosAgregaProf, BorderLayout.CENTER);

		JPanel IniciadorEliminaProfesorPanel = EliminaProfe();
		IniciadorEliminaProfesorPanel.setBorder(new EmptyBorder(60, 20, 0, 20));
		panelEliminarProfesor.add(IniciadorEliminaProfesorPanel, BorderLayout.CENTER);

		JPanel btnGuardarPanel = new JPanel(new BorderLayout());
		btnGuardarPanel.setBorder(new EmptyBorder(0, 20, 0, 20));
		panelAgregarProfesor.add(btnGuardarPanel, BorderLayout.SOUTH);

		JPanel btnBorrarPanel = new JPanel(new BorderLayout());
		btnBorrarPanel.setBorder(new EmptyBorder(0, 0, 0, 20));
		panelEliminarProfesor.add(btnBorrarPanel, BorderLayout.SOUTH);

		tablaDatosProfesores = new JTable();
		tablaDatosProfesores.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				PreparedStatement ps = null;
				ResultSet rs = null;

				try {

					Conexion objCon = new Conexion();
					Connection conn = objCon.getConnection();
					int fila = tablaDatosProfesores.getSelectedRow();
					id = tablaDatosProfesores.getValueAt(fila, 0).toString();
					ps = conn.prepareStatement("SELECT Nombre,Apellido,Rut,Correo FROM profesores WHERE Id = ?");
					ps.setString(1, id);
					rs = ps.executeQuery();

					while (rs.next()) {
						textNombreElimina.setText(rs.getString("Nombre"));
						textApellidoElimina.setText(rs.getString("Apellido"));
						textRutElimina.setText(rs.getString("Rut"));
						textCorreoElimina.setText(rs.getString("Correo"));

					}

				} catch (Exception e2) {
					System.err.println(e2.toString());
				}

			}
		});
		tablaDatosProfesores.setModel(encabezadoTabla());
		cargaTablaBaseDatos();
		tablaDatosProfesores.getTableHeader().setReorderingAllowed(false);
		tablaDatosProfesores.getTableHeader().setResizingAllowed(false);
		JScrollPane scrollTabla = new JScrollPane(tablaDatosProfesores);
		scrollTabla.setPreferredSize(new Dimension(452, 200));
		panelTablaDeDatos.add(scrollTabla, BorderLayout.CENTER);

		JButton btnGuardar = new JButton("Guardar");//manda los datos escritos en los JTextField a la base de datos
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				PreparedStatement ps = null;
				String claveaux1 = "", claveaux2 = "";

				try {

					Conexion objCon = new Conexion();
					Connection conn = objCon.getConnection();
					ps = conn.prepareStatement(
							"INSERT INTO profesores (Id,Nombre,Apellido,Rut,Correo,Contraseña) VALUES(?,?,?,?,?,AES_ENCRYPT(?,'profesores'))");

					for (char c : textClave.getPassword()) {
						claveaux1 += c;
					}
					for (char c : textConfirmaClave.getPassword()) {
						claveaux2 += c;
					}

					if (!camposVacios()) {
						if (claveaux1.equals(claveaux2)) {
							if (validarut(textRutAgrega.getText())) {
								++cantidadFilas;

								ps.setString(1, Integer.toString(cantidadFilas));
								ps.setString(2, textNombreAgrega.getText());
								ps.setString(3, textApellidoAgrega.getText());
								ps.setString(4, textRutAgrega.getText());
								ps.setString(5, textCorreoAgrega.getText());
								ps.setString(6, claveaux2);

								int resl = ps.executeUpdate();

								Object[] fila = new Object[5];
								fila[0] = cantidadFilas;
								fila[1] = textNombreAgrega.getText();
								fila[2] = textApellidoAgrega.getText();
								fila[3] = textRutAgrega.getText();
								fila[4] = textCorreoAgrega.getText();

								modelo.addRow(fila);

								if (resl > 0) {
									JOptionPane.showMessageDialog(null, "Profesor guardado");
									limpiarCajas();
								} else {
									JOptionPane.showMessageDialog(null, "Error al guardar profesor");
									limpiarCajas();
								}
								conn.close();

							} else {
								JOptionPane.showMessageDialog(null, "Formato de Rut invalido");
							}
						} else {
							JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Campos vacios no se puede guardar");
					}

				} catch (Exception e) {
					System.err.println(e);
				}
			}
		});
		btnGuardarPanel.add(btnGuardar, BorderLayout.EAST);

		JButton botonBorrar = new JButton("Borrar");//borra de la base de datos al profesor que se seleccione en la tabla de profesores
		botonBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PreparedStatement ps = null;
				ResultSet rs = null;
				try {
					if (textNombreElimina.getText().equals("") || textApellidoElimina.getText().equals("")|| textRutElimina.getText().equals("") || textCorreoElimina.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "<html>Campos vacios<br>Seleccione algun profesor</html>");

					} else {
						String clave = ventanaConfirmacion();
						if (!clave.isBlank()) {
							if (verificaAdm(clave)) {
								Conexion objCon = new Conexion();
								Connection conn = objCon.getConnection();
								ps = conn.prepareStatement("SELECT Id FROM profesores");
								rs = ps.executeQuery();

								int aux = 0;

								while (rs.next()) {
									if (rs.getString("Id").equals(id)) {
										modelo.removeRow(aux);
									}
									aux++;
								}

								ps = conn.prepareStatement("DELETE FROM profesores WHERE Id = ?");
								ps.setString(1, id);
								ps.executeUpdate();

								limpiarCajas();
							} else {

								JOptionPane.showMessageDialog(null, "Contraseña Incorrecta");
							}
						}
					}

				} catch (Exception e) {
					System.out.println(e.toString());
				}

			}
		});
		btnBorrarPanel.add(botonBorrar, BorderLayout.EAST);

		return panelContenedorMenu;
	}

	/**
	 * 
	 * Verifica si la clave ingresada es correcta
	 * 
	 * 
	 * @param clave Palabra ingresada
	 * @return boolean
	 * 
	 */
	boolean verificaAdm(String clave) {
		adm = new Modelo.admin();
		if (clave.equalsIgnoreCase(adm.getClaveAdmin())) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * Establece los rubros de la tabla de los profesores
	 * 
	 * @return DefaultTableModel
	 * 
	 */
	DefaultTableModel encabezadoTabla() {
		String[] rubrosTablaProf = { "Id", "Nombre", "Apellido", "Rut", "Correo" };
		DefaultTableModel tablaPorDefecto = new DefaultTableModel(rubrosTablaProf, 0);
		return tablaPorDefecto;
	}

	/**
	 * Carga desde la base de datos los profesores agregados a la tabla en el programa
	 */
	void cargaTablaBaseDatos() {

		try {

			tablaDatosProfesores.setModel(modelo);

			PreparedStatement ps = null;
			ResultSet rs = null;
			Conexion conec = new Conexion();
			Connection con = conec.getConnection();

			String sql = "SELECT Id, Nombre,Apellido,Rut,Correo FROM profesores";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			ResultSetMetaData rsMd = (ResultSetMetaData) rs.getMetaData();
			int cantidadColumnas = rsMd.getColumnCount();

			modelo.addColumn("Id");
			modelo.addColumn("Nombre");
			modelo.addColumn("Apellido");
			modelo.addColumn("Rut");
			modelo.addColumn("Correo");

			int[] anchos = { 50, 300, 300, 300, 400 };

			for (int x = 0; x < cantidadColumnas; x++) {
				tablaDatosProfesores.getColumnModel().getColumn(x).setPreferredWidth(anchos[x]);
			}

			while (rs.next()) {
				cantidadFilas++;
				Object[] filas = new Object[cantidadColumnas];
				for (int i = 0; i < cantidadColumnas; i++) {
					filas[i] = rs.getObject(i + 1);
				}
				modelo.addRow(filas);
			}

			cantidadFilas = (cantidadFilas == 0) ? 0 : (int) tablaDatosProfesores.getValueAt(cantidadFilas - 1, 0);

		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	/**
	 * verifica si los campos de ingreso de datos estan vacios
	 * 
	 * @return boolean
	 * 
	 */
	boolean camposVacios() {
		if (textNombreAgrega.getText().equalsIgnoreCase("Ingrese un nombre")
				&& textApellidoAgrega.getText().equalsIgnoreCase("Ingrese un apellido")
				&& textCorreoAgrega.getText().equalsIgnoreCase("Ingrese un correo")
				&& textRutAgrega.getText().equalsIgnoreCase("RUT sin punto y con guión")) {
			return true;
		}
		return false;
	}

	/**
	 * Ventana para confirmar que quiere borrar un profesor
	 * pide la contraseña del administrador
	 * @return String
	 * 
	 */
	String ventanaConfirmacion() {

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		JLabel label = new JLabel("Contraseña de Administrador :");
		label.setHorizontalAlignment(JLabel.LEFT);
		label.setFont(new Font("Thaoma", Font.PLAIN, 14));

		JPasswordField passwordField = new JPasswordField(15);
		passwordField.setFont(new Font("Calibri", Font.BOLD, 14));
		passwordField.setForeground(Color.BLACK);
		passwordField.setBackground(Color.WHITE);

		panel.add(label);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(passwordField);

		String[] options = new String[] { "Aceptar", "Cancelar" };

		int option = JOptionPane.showOptionDialog(null, panel, "Ingreso de Contraseña", JOptionPane.NO_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[1]);

		if (option == 0) {
			char[] password = passwordField.getPassword();
			return new String(password);
		}
		return "";
	}

	/**
	 * Setea la primera letra ingresada de los campos en mayuscula.
	 *
	 * @author Antony
	 *	
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
	 * Hace que no se puedan ingresar letras en la caja del rut.
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
		}

	}
}
