package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme;

/**
 * Esta clase se encarga de que los usuarios puedan
 * iniciar sesión.
 *
 * @author Antony
 * 
 * 
 */
@SuppressWarnings("serial")
public class InicioSesion extends JFrame {

	private JPanel PanelMayorInicioSesion;


	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(new FlatCarbonIJTheme());
		} catch (Exception ex) {
			System.err.println("Tema no aplicado");
		}
		new InicioSesion();
	}

	/**
	 * este metodo lo que hace es mostrar en pantalla
	 * los paneles de identificacion para los 3 tipos
	 * de usuario.
	 * 
	 * @return JPanel
	 * 
	 * 
	 */
	public JPanel Iniciador() {
		
		setTitle("ExamSoft");
		try {
			Image imagenLogo = ImageIO.read(new File(new File(".").getCanonicalPath() + "\\res\\soft.png"));
			setIconImage(imagenLogo);
		} catch (Exception e) {
			
		}
		JPanel panelContenedor = new JPanel(new BorderLayout());
		JPanel panelMenu = new JPanel(new BorderLayout());
		panelContenedor.add(panelMenu,BorderLayout.NORTH);
		JPanel panelCentral = new JPanel(new BorderLayout(0,20));
		panelCentral.setBorder(new EmptyBorder(40, 40, 40, 40));
		panelContenedor.add(panelCentral,BorderLayout.CENTER);
		
		JMenuBar barraMenu = new JMenuBar();
		panelMenu.add(barraMenu,BorderLayout.CENTER);
		JMenu menuOpciones = new JMenu("Opciones");
		barraMenu.add(menuOpciones);
		JMenuItem opcionSalir = new JMenuItem("Salir");
		opcionSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		menuOpciones.add(opcionSalir);

		JPanel panelLabel = new JPanel(new BorderLayout(0,20));
		panelCentral.add(panelLabel,BorderLayout.NORTH);

		JPanel panelEleccion = new JPanel(new BorderLayout(30,0));
		panelCentral.add(panelEleccion,BorderLayout.CENTER);

		JPanel panelBotones = new JPanel(new BorderLayout());
		panelCentral.add(panelBotones,BorderLayout.SOUTH);
		
		ButtonGroup grupoelecciones = new ButtonGroup();
		JRadioButton eleccionAdmin = new JRadioButton("Admin");
		eleccionAdmin.setForeground(Color.WHITE);
		eleccionAdmin.setHorizontalAlignment(SwingConstants.CENTER);
		eleccionAdmin.setFont(new Font("Tahoma", Font.PLAIN, 25));
		JRadioButton eleccionProfe = new JRadioButton("Profesor");
		eleccionProfe.setForeground(Color.WHITE);
		eleccionProfe.setHorizontalAlignment(SwingConstants.CENTER);
		eleccionProfe.setFont(new Font("Tahoma", Font.PLAIN, 25));
		JRadioButton eleccionAlumno = new JRadioButton("Alumno");
		eleccionAlumno.setForeground(Color.WHITE);
		eleccionAlumno.setHorizontalAlignment(SwingConstants.CENTER);
		eleccionAlumno.setFont(new Font("Tahoma", Font.PLAIN, 25));

		grupoelecciones.add(eleccionAdmin);
		grupoelecciones.add(eleccionProfe);
		grupoelecciones.add(eleccionAlumno);

		JLabel lblImagenSoftInicSes = new JLabel();
		lblImagenSoftInicSes.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel lblImagenAlumnoInicSes = new JLabel();
		lblImagenAlumnoInicSes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				eleccionAlumno.setSelected(true);
			}
		});
		lblImagenAlumnoInicSes.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblImagenProfeInicSes = new JLabel();
		lblImagenProfeInicSes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				eleccionProfe.setSelected(true);
			}
		});
		lblImagenProfeInicSes.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel lblImagenAdminInicSes = new JLabel();
		lblImagenAdminInicSes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				eleccionAdmin.setSelected(true);
			}
		});
		lblImagenAdminInicSes.setHorizontalAlignment(SwingConstants.CENTER);
		
		String ubicacionImagenSoft = "";
		String ubicacionImagenAlum = "";
		String ubicacionImagenProf = "";
		String ubicacionImagenAdmi = "";
		
		try {
			ubicacionImagenSoft = new File(".").getCanonicalPath() + "\\res\\software.png";
			ubicacionImagenAlum = new File(".").getCanonicalPath() + "\\res\\alumno.png";
			ubicacionImagenProf = new File(".").getCanonicalPath() + "\\res\\profesor.png";
			ubicacionImagenAdmi = new File(".").getCanonicalPath() + "\\res\\admin.png";
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		lblImagenSoftInicSes.setIcon(new ImageIcon(new ImageIcon(ubicacionImagenSoft).getImage().getScaledInstance(260, 210, Image.SCALE_DEFAULT)));
		lblImagenAlumnoInicSes.setIcon(new ImageIcon(new ImageIcon(ubicacionImagenAlum).getImage().getScaledInstance(110, 90, Image.SCALE_DEFAULT)));
		lblImagenProfeInicSes.setIcon(new ImageIcon(new ImageIcon(ubicacionImagenProf).getImage().getScaledInstance(110, 100, Image.SCALE_DEFAULT)));
		lblImagenAdminInicSes.setIcon(new ImageIcon(new ImageIcon(ubicacionImagenAdmi).getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
		
		panelLabel.add(lblImagenSoftInicSes, BorderLayout.CENTER);
		JLabel labelEleccion = new JLabel("Por favor Identifiquese:");
		labelEleccion.setFont(new Font("Thaoma", Font.PLAIN, 25));
		labelEleccion.setForeground(Color.WHITE);
		labelEleccion.setHorizontalAlignment(SwingConstants.CENTER);
		panelLabel.add(labelEleccion,BorderLayout.SOUTH);

		JPanel panelAlumno = new JPanel(new BorderLayout());
		panelAlumno.add(lblImagenAlumnoInicSes,BorderLayout.CENTER);
		panelAlumno.add(eleccionAlumno,BorderLayout.SOUTH);
		panelEleccion.add(panelAlumno,BorderLayout.WEST);
		
		JPanel panelProfe = new JPanel(new BorderLayout());
		panelProfe.add(lblImagenProfeInicSes,BorderLayout.CENTER);
		panelProfe.add(eleccionProfe,BorderLayout.SOUTH);
		panelEleccion.add(panelProfe,BorderLayout.CENTER);
		
		JPanel panelAdmin = new JPanel(new BorderLayout());
		panelAdmin.add(lblImagenAdminInicSes,BorderLayout.CENTER);
		panelAdmin.add(eleccionAdmin,BorderLayout.SOUTH);
		panelEleccion.add(panelAdmin,BorderLayout.EAST);

		JButton botonSiguiente = new JButton("Iniciar");
		botonSiguiente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(eleccionAdmin.isSelected()) {
					dispose();
					new AdminGUI();
				}
				if(eleccionAlumno.isSelected()) {
					dispose();
					new AlumnoGUI();
					
				}
				if(eleccionProfe.isSelected()) {
					dispose();
					new ProfesorGUI();
				}
			}
		});
		botonSiguiente.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panelBotones.add(botonSiguiente,BorderLayout.CENTER);

		return panelContenedor;
	}

	
	/**
	 * Constructor de InicioSesion
	 * Inicializa los componenetes para luego
	 * desplegarlos en pantalla. 
	 */
	public InicioSesion() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		PanelMayorInicioSesion = new JPanel();
		PanelMayorInicioSesion.setLayout(new BorderLayout(0, 0));
		setContentPane(PanelMayorInicioSesion);
		PanelMayorInicioSesion.add(Iniciador());
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
