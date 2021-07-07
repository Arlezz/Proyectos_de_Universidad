package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * Esta clase tiene la posibilidad de enviar
 * correos, tanto como de restauracion de contraseña
 * como de envio de notas a los estudiantes.
 * 
 * @author Antony
 * 
 *
 */
@SuppressWarnings("serial")
public class Email extends JFrame {

	private JPanel PanelMayorAbs;
	private String codigoDeVerificacion;

	/**
	 * Constructor vacio de Email.
	 */
	public Email() {
		
	}
	
	/**
	 * Constructor de Email. Abre una ventana donde se
	 * le tiene que ingresar un correo para restablecer 
	 * la contraseña
	 * 
	 * @param tabla Tabla
	 * 
	 * 
	 */
	public Email(String tabla) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		PanelMayorAbs = new JPanel();
		PanelMayorAbs.setLayout(new BorderLayout(0, 0));
		setContentPane(PanelMayorAbs);
		PanelMayorAbs.add(iniciador(tabla));
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);

	}

	/**
	 * esta clase inicia un jpanel donde al ingresar 
	 * el correo verifica si esta existe en la base de 
	 * datos y envia un codigo de restauracion.
	 *
	 * @param tabla Tabla
	 * @return JPanel
	 * 
	 *  
	 */
	public JPanel iniciador(String tabla) {


		JPanel panelMayorEmail = new JPanel(new BorderLayout(0, 35));

		JPanel panelHedderMayor = new JPanel(new BorderLayout());
		panelMayorEmail.add(panelHedderMayor, BorderLayout.NORTH);

		JMenuBar menuOpciones = new JMenuBar();
		JMenu opcion = new JMenu("Opciones");
		menuOpciones.add(opcion);
		JMenuItem regresar = new JMenuItem("Volver al inicio");
		regresar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				if (tabla.equalsIgnoreCase("profesores")) {
					new ProfesorGUI();
				} else {
					new AlumnoGUI();
				}
			}
		});
		opcion.add(regresar);
		panelHedderMayor.add(menuOpciones, BorderLayout.NORTH);

		JPanel panelIntroduceCorreo = new JPanel(new BorderLayout(0, 25));
		panelIntroduceCorreo.setBorder(new EmptyBorder(0, 35, 40, 35));
		panelMayorEmail.add(panelIntroduceCorreo, BorderLayout.CENTER);

		JPanel panelHedderIntroCorreo = new JPanel(new BorderLayout(0, 6));
		panelIntroduceCorreo.add(panelHedderIntroCorreo, BorderLayout.NORTH);
		JPanel panelBoddyIntroCorreo = new JPanel();
		panelBoddyIntroCorreo.setBorder(new EmptyBorder(0, 40, 0, 40));
		panelBoddyIntroCorreo.setLayout(new BoxLayout(panelBoddyIntroCorreo, BoxLayout.Y_AXIS));
		panelIntroduceCorreo.add(panelBoddyIntroCorreo, BorderLayout.CENTER);

		JLabel labelHeder = new JLabel("¿Has olvidado tu contraseña?");
		labelHeder.setHorizontalAlignment(SwingConstants.CENTER);
		labelHeder.setFont(new Font("Thaoma", Font.PLAIN, 35));
		panelHedderIntroCorreo.add(labelHeder, BorderLayout.NORTH);

		String text = "Por favor introduce la cuenta de email usada en el<br>registro";
		JLabel labelInstrucciones = new JLabel("<html><div style='text-align: center;'>" + text + "</div></html>");
		labelInstrucciones.setFont(new Font("Thaoma", Font.PLAIN, 16));
		labelInstrucciones.setHorizontalAlignment(SwingConstants.CENTER);
		panelHedderIntroCorreo.add(labelInstrucciones, BorderLayout.SOUTH);

		JPanel panelLabelBoddyInCorr = new JPanel(new BorderLayout());
		panelBoddyIntroCorreo.add(panelLabelBoddyInCorr);
		JPanel panelTextBoddyInCorr = new JPanel(new BorderLayout());
		panelBoddyIntroCorreo.add(panelTextBoddyInCorr);

		panelBoddyIntroCorreo.add(Box.createRigidArea(new Dimension(0, 25)));

		JPanel panelBtnBoddyInCorr = new JPanel(new BorderLayout());
		panelBoddyIntroCorreo.add(panelBtnBoddyInCorr);

		JLabel LabelBoddyInCorr = new JLabel("Tu Email");
		LabelBoddyInCorr.setFont(new Font("Thaoma", Font.PLAIN, 16));
		panelLabelBoddyInCorr.add(LabelBoddyInCorr, BorderLayout.WEST);

		JTextField TextBoddyInCorr = new JTextField();
		TextBoddyInCorr.setBackground(Color.WHITE);
		TextBoddyInCorr.setForeground(Color.BLACK);
		TextBoddyInCorr.setColumns(10);
		TextBoddyInCorr.setFont(new Font("Thaoma", Font.PLAIN, 20));
		panelTextBoddyInCorr.add(TextBoddyInCorr, BorderLayout.CENTER);

		JButton BtnBoddyInCorr = new JButton("Obtén una nueva contraseña");
		BtnBoddyInCorr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (TextBoddyInCorr.getText().isBlank()) {
					JOptionPane.showMessageDialog(null, "Ingrese un Email");
					return;
				}
				if (!isValidEmailAddress(TextBoddyInCorr.getText())) {
					JOptionPane.showMessageDialog(null, "Correo invalido");
					return;
				} 
				if(!correoExiste(TextBoddyInCorr.getText(),tabla)) {
					JOptionPane.showMessageDialog(null, "El correo no existe en el sistema");
					return;
				}
				Properties propiedad = new Properties();
				String destinatario;
				String email;
				String nombre = "", apellido = "";
				PreparedStatement ps = null;
				ResultSet rs = null;
				MimeMessage mail;
				try {
					Conexion objCon = new Conexion();
					Connection conn = objCon.getConnection();
					email = TextBoddyInCorr.getText();
					ps = conn.prepareStatement("SELECT Nombre,Apellido FROM " + tabla + " WHERE Correo = ?");
					ps.setString(1, email);
					rs = ps.executeQuery();

					while (rs.next()) {
						nombre = rs.getString("Nombre");
						apellido = rs.getString("Apellido");
					}
				} catch (Exception e) {
					System.err.println(e.toString());
				}

				propiedad.setProperty("mail.smtp.host", "smtp.gmail.com");
				propiedad.setProperty("mail.smtp.starttls.enable", "true");
				propiedad.setProperty("mail.smtp.port", "587");
				propiedad.setProperty("mail.smtp.auth", "true");

				Session sesion = Session.getDefaultInstance(propiedad);

				codigoDeVerificacion = codigoRandom();
				String correoEnvia = "exam.soft.edu@gmail.com";
				String contraseña = "melopomelo13";
				destinatario = TextBoddyInCorr.getText();
				String asunto = "Restauracion de contraseña ExamSoft";

				try {
					
					mail = new MimeMessage(sesion);
					mail.setFrom(new InternetAddress(correoEnvia));
					//InternetAddress correo = new InternetAddress(destinatario);
					mail.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
					mail.setSubject(asunto);
					mail.setContent("<!doctype html>\r\n"
							+ "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\r\n"
							+ "  <head>\r\n"
							+ "    <title>\r\n"
							+ "    </title>\r\n"
							+ "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n"
							+ "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n"
							+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\r\n"
							+ "    <style type=\"text/css\">\r\n"
							+ "      #outlook a{padding: 0;}\r\n"
							+ "      			.ReadMsgBody{width: 100%;}\r\n"
							+ "      			.ExternalClass{width: 100%;}\r\n"
							+ "      			.ExternalClass *{line-height: 100%;}\r\n"
							+ "      			body{margin: 0; padding: 0; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%;}\r\n"
							+ "      			table, td{border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt;}\r\n"
							+ "      			img{border: 0; height: auto; line-height: 100%; outline: none; text-decoration: none; -ms-interpolation-mode: bicubic;}\r\n"
							+ "      			p{display: block; margin: 13px 0;}\r\n"
							+ "    </style>\r\n"
							+ "    <!--[if !mso]><!-->\r\n"
							+ "    <style type=\"text/css\">\r\n"
							+ "      @media only screen and (max-width:480px) {\r\n"
							+ "      			  		@-ms-viewport {width: 320px;}\r\n"
							+ "      			  		@viewport {	width: 320px; }\r\n"
							+ "      				}\r\n"
							+ "    </style>\r\n"
							+ "    <!--<![endif]-->\r\n"
							+ "    <!--[if mso]> \r\n"
							+ "		<xml> \r\n"
							+ "			<o:OfficeDocumentSettings> \r\n"
							+ "				<o:AllowPNG/> \r\n"
							+ "				<o:PixelsPerInch>96</o:PixelsPerInch> \r\n"
							+ "			</o:OfficeDocumentSettings> \r\n"
							+ "		</xml>\r\n"
							+ "		<![endif]-->\r\n"
							+ "    <!--[if lte mso 11]> \r\n"
							+ "		<style type=\"text/css\"> \r\n"
							+ "			.outlook-group-fix{width:100% !important;}\r\n"
							+ "		</style>\r\n"
							+ "		<![endif]-->\r\n"
							+ "    <style type=\"text/css\">\r\n"
							+ "      @media only screen and (min-width:480px) {\r\n"
							+ "      .dys-column-per-100 {\r\n"
							+ "      	width: 100.000000% !important;\r\n"
							+ "      	max-width: 100.000000%;\r\n"
							+ "      }\r\n"
							+ "      }\r\n"
							+ "      @media only screen and (min-width:480px) {\r\n"
							+ "      .dys-column-per-100 {\r\n"
							+ "      	width: 100.000000% !important;\r\n"
							+ "      	max-width: 100.000000%;\r\n"
							+ "      }\r\n"
							+ "      }\r\n"
							+ "      @media only screen and (max-width:480px) {\r\n"
							+ "      \r\n"
							+ "      			  table.full-width-mobile { width: 100% !important; }\r\n"
							+ "      				td.full-width-mobile { width: auto !important; }\r\n"
							+ "      \r\n"
							+ "      }\r\n"
							+ "      @media only screen and (min-width:480px) {\r\n"
							+ "      .dys-column-per-100 {\r\n"
							+ "      	width: 100.000000% !important;\r\n"
							+ "      	max-width: 100.000000%;\r\n"
							+ "      }\r\n"
							+ "      .dys-column-per-25 {\r\n"
							+ "      	width: 25.000000% !important;\r\n"
							+ "      	max-width: 25.000000%;\r\n"
							+ "      }\r\n"
							+ "      }\r\n"
							+ "      @media only screen and (min-width:480px) {\r\n"
							+ "      .dys-column-per-90 {\r\n"
							+ "      	width: 90% !important;\r\n"
							+ "      	max-width: 90%;\r\n"
							+ "      }\r\n"
							+ "      }\r\n"
							+ "      @media only screen and (min-width:480px) {\r\n"
							+ "      .dys-column-per-100 {\r\n"
							+ "      	width: 100.000000% !important;\r\n"
							+ "      	max-width: 100.000000%;\r\n"
							+ "      }\r\n"
							+ "      }\r\n"
							+ "    </style>\r\n"
							+ "  </head>\r\n"
							+ "  <body>\r\n"
							+ "    <div>\r\n"
							+ "      <table align='center' background='https://s3.amazonaws.com/swu-filepicker/4E687TRe69Ld95IDWyEg_bg_top_02.jpg' border='0' cellpadding='0' cellspacing='0' role='presentation' style='background:url(https://s3.amazonaws.com/swu-filepicker/4E687TRe69Ld95IDWyEg_bg_top_02.jpg) top center / auto repeat;width:100%;'>\r\n"
							+ "        <tbody>\r\n"
							+ "          <tr>\r\n"
							+ "            <td>\r\n"
							+ "              <!--[if mso | IE]>\r\n"
							+ "<v:rect style=\"mso-width-percent:1000;\" xmlns:v=\"urn:schemas-microsoft-com:vml\" fill=\"true\" stroke=\"false\"><v:fill src=\"https://s3.amazonaws.com/swu-filepicker/4E687TRe69Ld95IDWyEg_bg_top_02.jpg\" origin=\"0.5, 0\" position=\"0.5, 0\" type=\"tile\" /><v:textbox style=\"mso-fit-shape-to-text:true\" inset=\"0,0,0,0\">\r\n"
							+ "<![endif]-->\r\n"
							+ "              <div style='margin:0px auto;max-width:600px;'>\r\n"
							+ "                <div style='font-size:0;line-height:0;'>\r\n"
							+ "                  <table align='center' border='0' cellpadding='0' cellspacing='0' role='presentation' style='width:100%;'>\r\n"
							+ "                    <tbody>\r\n"
							+ "                      <tr>\r\n"
							+ "                        <td style='direction:ltr;font-size:0px;padding:20px 0px 30px 0px;text-align:center;vertical-align:top;'>\r\n"
							+ "                          <!--[if mso | IE]>\r\n"
							+ "<table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr><td style=\"vertical-align:top;width:600px;\">\r\n"
							+ "<![endif]-->\r\n"
							+ "                          <div class='dys-column-per-100 outlook-group-fix' style='direction:ltr;display:inline-block;font-size:13px;text-align:left;vertical-align:top;width:100%;'>\r\n"
							+ "                            <table border='0' cellpadding='0' cellspacing='0' role='presentation' width='100%'>\r\n"
							+ "                              <tbody>\r\n"
							+ "                                <tr>\r\n"
							+ "                                  <td style='padding:0px 20px;vertical-align:top;'>\r\n"
							+ "                                    <table border='0' cellpadding='0' cellspacing='0' role='presentation' style='' width='100%'>\r\n"
							+ "                                      <tr>\r\n"
							+ "                                        <td align='left' style='font-size:0px;padding:0px;word-break:break-word;'>\r\n"
							+ "                                          <table border='0' cellpadding='0' cellspacing='0' style='cellpadding:0;cellspacing:0;color:#000000;font-family:Helvetica, Arial, sans-serif;font-size:13px;line-height:22px;table-layout:auto;width:100%;' width='100%'>\r\n"
							+ "                                            <tr>\r\n"
							+ "                                              <td align='left'>\r\n"
							+ "                                                <a href='#'>\r\n"
							+ "                                                  <img align='left' alt='Logo' height='33' padding='5px' src='https://i.ibb.co/wLW5p6c/software.png' width='120' />\r\n"
							+ "                                                </a>\r\n"
							+ "                                              </td>\r\n"
							+ "                                              <td align='right' style='vertical-align:bottom;' width='34px'>\r\n"
							+ "                                                <a href='https://twitter.com/Rl3zz'>\r\n"
							+ "                                                  <img alt='Twitter' height='22' src='https://s3.amazonaws.com/swu-cs-assets/OSET/social/Twitter_grey.png' width='22' />\r\n"
							+ "                                                </a>\r\n"
							+ "                                              </td>\r\n"
							+ "                                              <td align='right' style='vertical-align:bottom;' width='34px'>\r\n"
							+ "                                                <a href='https://www.facebook.com/Rlezz'>\r\n"
							+ "                                                  <img alt='Facebook' height='22' src='https://swu-cs-assets.s3.amazonaws.com/OSET/social/f_grey.png' width='22' />\r\n"
							+ "                                                </a>\r\n"
							+ "                                              </td>\r\n"
							+ "                                              <td align='right' style='vertical-align:bottom;' width='34px'>\r\n"
							+ "                                                <a href='https://www.instagram.com/arlezz13/'>\r\n"
							+ "                                                  <img alt='Instagram' height='22' src='https://swu-cs-assets.s3.amazonaws.com/OSET/social/instagrey.png' width='22' />\r\n"
							+ "                                                </a>\r\n"
							+ "                                              </td>\r\n"
							+ "                                            </tr>\r\n"
							+ "                                          </table>\r\n"
							+ "                                        </td>\r\n"
							+ "                                      </tr>\r\n"
							+ "                                    </table>\r\n"
							+ "                                  </td>\r\n"
							+ "                                </tr>\r\n"
							+ "                              </tbody>\r\n"
							+ "                            </table>\r\n"
							+ "                          </div>\r\n"
							+ "                          <!--[if mso | IE]>\r\n"
							+ "</td></tr></table>\r\n"
							+ "<![endif]-->\r\n"
							+ "                        </td>\r\n"
							+ "                      </tr>\r\n"
							+ "                    </tbody>\r\n"
							+ "                  </table>\r\n"
							+ "                </div>\r\n"
							+ "              </div>\r\n"
							+ "              <!--[if mso | IE]>\r\n"
							+ "</v:textbox></v:rect>\r\n"
							+ "<![endif]-->\r\n"
							+ "            </td>\r\n"
							+ "          </tr>\r\n"
							+ "        </tbody>\r\n"
							+ "      </table>\r\n"
							+ "      <table align='center' border='0' cellpadding='0' cellspacing='0' role='presentation' style='background:#f7f7f7;background-color:#f7f7f7;width:100%;'>\r\n"
							+ "        <tbody>\r\n"
							+ "          <tr>\r\n"
							+ "            <td>\r\n"
							+ "              <div style='margin:0px auto;max-width:600px;'>\r\n"
							+ "                <table align='center' border='0' cellpadding='0' cellspacing='0' role='presentation' style='width:100%;'>\r\n"
							+ "                  <tbody>\r\n"
							+ "                    <tr>\r\n"
							+ "                      <td style='direction:ltr;font-size:0px;padding:20px 0;text-align:center;vertical-align:top;'>\r\n"
							+ "                        <!--[if mso | IE]>\r\n"
							+ "<table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr><td style=\"vertical-align:top;width:600px;\">\r\n"
							+ "<![endif]-->\r\n"
							+ "                        <div class='dys-column-per-100 outlook-group-fix' style='direction:ltr;display:inline-block;font-size:13px;text-align:left;vertical-align:top;width:100%;'>\r\n"
							+ "                          <table border='0' cellpadding='0' cellspacing='0' role='presentation' style='vertical-align:top;' width='100%'>\r\n"
							+ "                            <tr>\r\n"
							+ "                              <td align='center' style='font-size:0px;padding:10px 25px;word-break:break-word;'>\r\n"
							+ "                                <div style='color:#4d4d4d;font-family:Oxygen, Helvetica neue, sans-serif;font-size:32px;font-weight:700;line-height:37px;text-align:center;'>\r\n"
							+ "                                  ExamSoft\r\n"
							+ "                                </div>\r\n"
							+ "                              </td>\r\n"
							+ "                            </tr>\r\n"
							+ "                            <tr>\r\n"
							+ "                              <td align='center' style='font-size:0px;padding:10px 25px;word-break:break-word;'>\r\n"
							+ "                                <div style='color:#777777;font-family:Oxygen, Helvetica neue, sans-serif;font-size:14px;line-height:21px;text-align:center;'>\r\n"
							+ "                                  El aprendizaje en línea es el presente y el futuro.\r\n"
							+ "                                </div>\r\n"
							+ "                              </td>\r\n"
							+ "                            </tr>\r\n"
							+ "                          </table>\r\n"
							+ "                        </div>\r\n"
							+ "                        <!--[if mso | IE]>\r\n"
							+ "</td></tr></table>\r\n"
							+ "<![endif]-->\r\n"
							+ "                      </td>\r\n"
							+ "                    </tr>\r\n"
							+ "                  </tbody>\r\n"
							+ "                </table>\r\n"
							+ "              </div>\r\n"
							+ "            </td>\r\n"
							+ "          </tr>\r\n"
							+ "        </tbody>\r\n"
							+ "      </table>\r\n"
							+ "      <!--[if mso | IE]>\r\n"
							+ "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:600px;\" width=\"600\"><tr><td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\r\n"
							+ "<![endif]-->\r\n"
							+ "      <div style='background:#FFFFFF;background-color:#FFFFFF;margin:0px auto;max-width:600px;'>\r\n"
							+ "        <table align='center' border='0' cellpadding='0' cellspacing='0' role='presentation' style='background:#FFFFFF;background-color:#FFFFFF;width:100%;'>\r\n"
							+ "          <tbody>\r\n"
							+ "            <tr>\r\n"
							+ "              <td style='direction:ltr;font-size:0px;padding:20px 0;padding-bottom:0px;text-align:center;vertical-align:top;'>\r\n"
							+ "                <!--[if mso | IE]>\r\n"
							+ "<table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr><td style=\"vertical-align:top;width:600px;\">\r\n"
							+ "<![endif]-->\r\n"
							+ "                <div class='dys-column-per-100 outlook-group-fix' style='direction:ltr;display:inline-block;font-size:13px;text-align:left;vertical-align:top;width:100%;'>\r\n"
							+ "                  <table border='0' cellpadding='0' cellspacing='0' role='presentation' width='100%'>\r\n"
							+ "                    <tbody>\r\n"
							+ "                      <tr>\r\n"
							+ "                        <td style='padding:0px;vertical-align:top;'>\r\n"
							+ "                          <table border='0' cellpadding='0' cellspacing='0' role='presentation' style='' width='100%'>\r\n"
							+ "                            <tr>\r\n"
							+ "                              <td align='center' style='font-size:0px;padding:10px 25px;padding-bottom:5px;word-break:break-word;'>\r\n"
							+ "                                <div style='color:#4d4d4d;font-family:Oxygen, Helvetica neue, sans-serif;font-size:24px;font-weight:700;line-height:30px;text-align:center;'>\r\n"
							+ "                                </div>\r\n"
							+ "                              </td>\r\n"
							+ "                            </tr>\r\n"
							+ "                            <tr>\r\n"
							+ "                              <td align='center' style='font-size:0px;padding:0px;word-break:break-word;'>\r\n"
							+ "                                <div style='color:#777777;font-family:Oxygen, Helvetica neue, sans-serif;font-size:14px;line-height:21px;text-align:center;'>\r\n"
							+ "                                </div>\r\n"
							+ "                              </td>\r\n"
							+ "                            </tr>\r\n"
							+ "                          </table>\r\n"
							+ "                        </td>\r\n"
							+ "                      </tr>\r\n"
							+ "                    </tbody>\r\n"
							+ "                  </table>\r\n"
							+ "                </div>\r\n"
							+ "                <!--[if mso | IE]>\r\n"
							+ "</td></tr></table>\r\n"
							+ "<![endif]-->\r\n"
							+ "              </td>\r\n"
							+ "            </tr>\r\n"
							+ "          </tbody>\r\n"
							+ "        </table>\r\n"
							+ "      </div>\r\n"
							+ "      <!--[if mso | IE]>\r\n"
							+ "</td></tr></table>\r\n"
							+ "<![endif]-->\r\n"
							+ "      <!--[if mso | IE]>\r\n"
							+ "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:600px;\" width=\"600\"><tr><td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\r\n"
							+ "<![endif]-->\r\n"
							+ "      <div style='margin:0px auto;max-width:600px;'>\r\n"
							+ "        <table align='center' border='0' cellpadding='0' cellspacing='0' role='presentation' style='width:100%;'>\r\n"
							+ "          <tbody>\r\n"
							+ "            <tr>\r\n"
							+ "              <td style='direction:ltr;font-size:0px;padding:20px 0;padding-bottom:0px;text-align:center;vertical-align:top;'>\r\n"
							+ "                <!--[if mso | IE]>\r\n"
							+ "<table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr><td style=\"vertical-align:top;width:150px;\">\r\n"
							+ "<![endif]-->\r\n"
							+ "                <div class='dys-column-per-25 outlook-group-fix' style='direction:ltr;display:inline-block;font-size:13px;text-align:left;vertical-align:top;width:100%;'>\r\n"
							+ "                  <table border='0' cellpadding='0' cellspacing='0' role='presentation' style='vertical-align:top;' width='100%'>\r\n"
							+ "                    <tr>\r\n"
							+ "                      <td align='center' style='font-size:0px;padding:5px;word-break:break-word;'>\r\n"
							+ "                        <table border='0' cellpadding='0' cellspacing='0' role='presentation' style='border-collapse:collapse;border-spacing:0px;'>\r\n"
							+ "                          <tbody>\r\n"
							+ "                            <tr>\r\n"
							+ "                              <td style='width:140px;'>\r\n"
							+ "                                <img alt='Descriptive Alt Text' height='auto' src='https://www.compartirpalabramaestra.org/sites/default/files/field/image/infografia-lo-que-hay-que-saber-del-aprendizaje-en-linea.jpg' style='border:none;border-radius:5px;display:block;font-size:13px;height:auto;outline:none;text-decoration:none;width:100%;' width='140' />\r\n"
							+ "                              </td>\r\n"
							+ "                            </tr>\r\n"
							+ "                          </tbody>\r\n"
							+ "                        </table>\r\n"
							+ "                      </td>\r\n"
							+ "                    </tr>\r\n"
							+ "                  </table>\r\n"
							+ "                </div>\r\n"
							+ "                <!--[if mso | IE]>\r\n"
							+ "</td><td style=\"vertical-align:top;width:150px;\">\r\n"
							+ "<![endif]-->\r\n"
							+ "                <div class='dys-column-per-25 outlook-group-fix' style='direction:ltr;display:inline-block;font-size:13px;text-align:left;vertical-align:top;width:100%;'>\r\n"
							+ "                  <table border='0' cellpadding='0' cellspacing='0' role='presentation' style='vertical-align:top;' width='100%'>\r\n"
							+ "                    <tr>\r\n"
							+ "                      <td align='center' style='font-size:0px;padding:5px;word-break:break-word;'>\r\n"
							+ "                        <table border='0' cellpadding='0' cellspacing='0' role='presentation' style='border-collapse:collapse;border-spacing:0px;'>\r\n"
							+ "                          <tbody>\r\n"
							+ "                            <tr>\r\n"
							+ "                              <td style='width:140px;'>\r\n"
							+ "                                <img alt='Descriptive Alt Text' height='auto' src='https://conidea.mx/wp-content/uploads/2020/05/ensenar-en-forma-virtual-01.jpg' style='border:none;border-radius:5px;display:block;font-size:13px;height:auto;outline:none;text-decoration:none;width:100%;' width='140' />\r\n"
							+ "                              </td>\r\n"
							+ "                            </tr>\r\n"
							+ "                          </tbody>\r\n"
							+ "                        </table>\r\n"
							+ "                      </td>\r\n"
							+ "                    </tr>\r\n"
							+ "                  </table>\r\n"
							+ "                </div>\r\n"
							+ "                <!--[if mso | IE]>\r\n"
							+ "</td><td style=\"vertical-align:top;width:150px;\">\r\n"
							+ "<![endif]-->\r\n"
							+ "                <div class='dys-column-per-25 outlook-group-fix' style='direction:ltr;display:inline-block;font-size:13px;text-align:left;vertical-align:top;width:100%;'>\r\n"
							+ "                  <table border='0' cellpadding='0' cellspacing='0' role='presentation' style='vertical-align:top;' width='100%'>\r\n"
							+ "                    <tr>\r\n"
							+ "                      <td align='center' style='font-size:0px;padding:5px;word-break:break-word;'>\r\n"
							+ "                        <table border='0' cellpadding='0' cellspacing='0' role='presentation' style='border-collapse:collapse;border-spacing:0px;'>\r\n"
							+ "                          <tbody>\r\n"
							+ "                            <tr>\r\n"
							+ "                              <td style='width:140px;'>\r\n"
							+ "                                <img alt='Descriptive Alt Text' height='auto' src='https://image.freepik.com/vector-gratis/concepto-cursos-linea_23-2148529256.jpg' style='border:none;border-radius:5px;display:block;font-size:13px;height:auto;outline:none;text-decoration:none;width:100%;' width='140' />\r\n"
							+ "                              </td>\r\n"
							+ "                            </tr>\r\n"
							+ "                          </tbody>\r\n"
							+ "                        </table>\r\n"
							+ "                      </td>\r\n"
							+ "                    </tr>\r\n"
							+ "                  </table>\r\n"
							+ "                </div>\r\n"
							+ "                <!--[if mso | IE]>\r\n"
							+ "</td><td style=\"vertical-align:top;width:150px;\">\r\n"
							+ "<![endif]-->\r\n"
							+ "                <div class='dys-column-per-25 outlook-group-fix' style='direction:ltr;display:inline-block;font-size:13px;text-align:left;vertical-align:top;width:100%;'>\r\n"
							+ "                  <table border='0' cellpadding='0' cellspacing='0' role='presentation' style='vertical-align:top;' width='100%'>\r\n"
							+ "                    <tr>\r\n"
							+ "                      <td align='center' style='font-size:0px;padding:5px;word-break:break-word;'>\r\n"
							+ "                        <table border='0' cellpadding='0' cellspacing='0' role='presentation' style='border-collapse:collapse;border-spacing:0px;'>\r\n"
							+ "                          <tbody>\r\n"
							+ "                            <tr>\r\n"
							+ "                              <td style='width:140px;'>\r\n"
							+ "                                <img alt='Descriptive Alt Text' height='auto' src='https://image.freepik.com/vector-gratis/cursos-tutoriales-linea_52683-37860.jpg' style='border:none;border-radius:5px;display:block;font-size:13px;height:auto;outline:none;text-decoration:none;width:100%;' width='140' />\r\n"
							+ "                              </td>\r\n"
							+ "                            </tr>\r\n"
							+ "                          </tbody>\r\n"
							+ "                        </table>\r\n"
							+ "                      </td>\r\n"
							+ "                    </tr>\r\n"
							+ "                  </table>\r\n"
							+ "                </div>\r\n"
							+ "                <!--[if mso | IE]>\r\n"
							+ "</td></tr></table>\r\n"
							+ "<![endif]-->\r\n"
							+ "              </td>\r\n"
							+ "            </tr>\r\n"
							+ "          </tbody>\r\n"
							+ "        </table>\r\n"
							+ "      </div>\r\n"
							+ "      <!--[if mso | IE]>\r\n"
							+ "</td></tr></table>\r\n"
							+ "<![endif]-->\r\n"
							+ "      <!--[if mso | IE]>\r\n"
							+ "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:600px;\" width=\"600\"><tr><td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\r\n"
							+ "<![endif]-->\r\n"
							+ "      <div style='margin:0px auto;max-width:600px;'>\r\n"
							+ "        <table align='center' border='0' cellpadding='0' cellspacing='0' role='presentation' style='width:100%;'>\r\n"
							+ "          <tbody>\r\n"
							+ "            <tr>\r\n"
							+ "              <td style='border-bottom:1px solid #e5e5e5;direction:ltr;font-size:0px;padding:20px 0;padding-top:0px;text-align:center;vertical-align:top;'>\r\n"
							+ "                <!--[if mso | IE]>\r\n"
							+ "<table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr><td style=\"vertical-align:top;width:150px;\">\r\n"
							+ "<![endif]-->\r\n"
							+ "                <div class='dys-column-per-25 outlook-group-fix' style='direction:ltr;display:inline-block;font-size:13px;text-align:left;vertical-align:top;width:100%;'>\r\n"
							+ "                  <table border='0' cellpadding='0' cellspacing='0' role='presentation' style='vertical-align:top;' width='100%'>\r\n"
							+ "                    <tr>\r\n"
							+ "                      <td align='center' style='font-size:0px;padding:5px;word-break:break-word;'>\r\n"
							+ "                        <table border='0' cellpadding='0' cellspacing='0' role='presentation' style='border-collapse:collapse;border-spacing:0px;'>\r\n"
							+ "                          <tbody>\r\n"
							+ "                            <tr>\r\n"
							+ "                              <td style='width:140px;'>\r\n"
							+ "                                <img alt='Descriptive Alt Text' height='auto' src='https://ww2.elmercurio.com.ec/wp-content/uploads/2020/08/concepto-educacion-online_52683-9044.jpg' style='border:none;border-radius:5px;display:block;font-size:13px;height:auto;outline:none;text-decoration:none;width:100%;' width='140' />\r\n"
							+ "                              </td>\r\n"
							+ "                            </tr>\r\n"
							+ "                          </tbody>\r\n"
							+ "                        </table>\r\n"
							+ "                      </td>\r\n"
							+ "                    </tr>\r\n"
							+ "                  </table>\r\n"
							+ "                </div>\r\n"
							+ "                <!--[if mso | IE]>\r\n"
							+ "</td><td style=\"vertical-align:top;width:150px;\">\r\n"
							+ "<![endif]-->\r\n"
							+ "                <div class='dys-column-per-25 outlook-group-fix' style='direction:ltr;display:inline-block;font-size:13px;text-align:left;vertical-align:top;width:100%;'>\r\n"
							+ "                  <table border='0' cellpadding='0' cellspacing='0' role='presentation' style='vertical-align:top;' width='100%'>\r\n"
							+ "                    <tr>\r\n"
							+ "                      <td align='center' style='font-size:0px;padding:5px;word-break:break-word;'>\r\n"
							+ "                        <table border='0' cellpadding='0' cellspacing='0' role='presentation' style='border-collapse:collapse;border-spacing:0px;'>\r\n"
							+ "                          <tbody>\r\n"
							+ "                            <tr>\r\n"
							+ "                              <td style='width:140px;'>\r\n"
							+ "                                <img alt='Descriptive Alt Text' height='auto' src='https://elcomercio.pe/resizer/7JNxUw-xkh8PPwWxfWdRch5i_pE=/980x0/smart/filters:format(jpeg):quality(75)/arc-anglerfish-arc2-prod-elcomercio.s3.amazonaws.com/public/FQHSI742VJBWRDTHSI7JU4A6AU.jpg' style='border:none;border-radius:5px;display:block;font-size:13px;height:auto;outline:none;text-decoration:none;width:100%;' width='140' />\r\n"
							+ "                              </td>\r\n"
							+ "                            </tr>\r\n"
							+ "                          </tbody>\r\n"
							+ "                        </table>\r\n"
							+ "                      </td>\r\n"
							+ "                    </tr>\r\n"
							+ "                  </table>\r\n"
							+ "                </div>\r\n"
							+ "                <!--[if mso | IE]>\r\n"
							+ "</td><td style=\"vertical-align:top;width:150px;\">\r\n"
							+ "<![endif]-->\r\n"
							+ "                <div class='dys-column-per-25 outlook-group-fix' style='direction:ltr;display:inline-block;font-size:13px;text-align:left;vertical-align:top;width:100%;'>\r\n"
							+ "                  <table border='0' cellpadding='0' cellspacing='0' role='presentation' style='vertical-align:top;' width='100%'>\r\n"
							+ "                    <tr>\r\n"
							+ "                      <td align='center' style='font-size:0px;padding:5px;word-break:break-word;'>\r\n"
							+ "                        <table border='0' cellpadding='0' cellspacing='0' role='presentation' style='border-collapse:collapse;border-spacing:0px;'>\r\n"
							+ "                          <tbody>\r\n"
							+ "                            <tr>\r\n"
							+ "                              <td style='width:140px;'>\r\n"
							+ "                                <img alt='Descriptive Alt Text' height='auto' src='https://assets.puzzlefactory.pl/puzzle/258/598/original.jpg' style='border:none;border-radius:5px;display:block;font-size:13px;height:auto;outline:none;text-decoration:none;width:100%;' width='140' />\r\n"
							+ "                              </td>\r\n"
							+ "                            </tr>\r\n"
							+ "                          </tbody>\r\n"
							+ "                        </table>\r\n"
							+ "                      </td>\r\n"
							+ "                    </tr>\r\n"
							+ "                  </table>\r\n"
							+ "                </div>\r\n"
							+ "                <!--[if mso | IE]>\r\n"
							+ "</td><td style=\"vertical-align:top;width:150px;\">\r\n"
							+ "<![endif]-->\r\n"
							+ "                <div class='dys-column-per-25 outlook-group-fix' style='direction:ltr;display:inline-block;font-size:13px;text-align:left;vertical-align:top;width:100%;'>\r\n"
							+ "                  <table border='0' cellpadding='0' cellspacing='0' role='presentation' style='vertical-align:top;' width='100%'>\r\n"
							+ "                    <tr>\r\n"
							+ "                      <td align='center' style='font-size:0px;padding:5px;word-break:break-word;'>\r\n"
							+ "                        <table border='0' cellpadding='0' cellspacing='0' role='presentation' style='border-collapse:collapse;border-spacing:0px;'>\r\n"
							+ "                          <tbody>\r\n"
							+ "                            <tr>\r\n"
							+ "                              <td style='width:140px;'>\r\n"
							+ "                                <img alt='Descriptive Alt Text' height='auto' src='https://hablandoencorto.com/wp-content/uploads/2020/05/educacion-online.jpg' style='border:none;border-radius:5px;display:block;font-size:13px;height:auto;outline:none;text-decoration:none;width:100%;' width='140' />\r\n"
							+ "                              </td>\r\n"
							+ "                            </tr>\r\n"
							+ "                          </tbody>\r\n"
							+ "                        </table>\r\n"
							+ "                      </td>\r\n"
							+ "                    </tr>\r\n"
							+ "                  </table>\r\n"
							+ "                </div>\r\n"
							+ "                <!--[if mso | IE]>\r\n"
							+ "</td></tr></table>\r\n"
							+ "<![endif]-->\r\n"
							+ "              </td>\r\n"
							+ "            </tr>\r\n"
							+ "          </tbody>\r\n"
							+ "        </table>\r\n"
							+ "      </div>\r\n"
							+ "      <!--[if mso | IE]>\r\n"
							+ "</td></tr></table>\r\n"
							+ "<![endif]-->\r\n"
							+ "      <table align='center' border='0' cellpadding='0' cellspacing='0' role='presentation' style='background:#f7f7f7;background-color:#f7f7f7;width:100%;'>\r\n"
							+ "        <tbody>\r\n"
							+ "          <tr>\r\n"
							+ "            <td>\r\n"
							+ "              <div style='margin:0px auto;max-width:600px;'>\r\n"
							+ "                <table align='center' border='0' cellpadding='0' cellspacing='0' role='presentation' style='width:100%;'>\r\n"
							+ "                  <tbody>\r\n"
							+ "                    <tr>\r\n"
							+ "                      <td style='direction:ltr;font-size:0px;padding:20px;text-align:center;vertical-align:top;'>\r\n"
							+ "                        <!--[if mso | IE]>\r\n"
							+ "<table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr><td style=\"vertical-align:top;width:540px;\">\r\n"
							+ "<![endif]-->\r\n"
							+ "                        <div class='dys-column-per-90 outlook-group-fix' style='direction:ltr;display:inline-block;font-size:13px;text-align:left;vertical-align:top;width:100%;'>\r\n"
							+ "                          <table border='0' cellpadding='0' cellspacing='0' role='presentation' width='100%'>\r\n"
							+ "                            <tbody>\r\n"
							+ "                              <tr>\r\n"
							+ "                                <td style='background-color:#ffffff;border:1px solid #ccc;padding:50px 15px;vertical-align:top;'>\r\n"
							+ "                                  <table border='0' cellpadding='0' cellspacing='0' role='presentation' style='' width='100%'>\r\n"
							+ "                                    <tr>\r\n"
							+ "                                      <td align='center' style='font-size:0px;padding:10px 25px;word-break:break-word;'>\r\n"
							+ "                                        <div style='color:#777777;font-family:Oxygen, Helvetica neue, sans-serif;font-size:14px;line-height:21px;text-align:center;'>\r\n"
							+ "                                          <span>\r\n"
							+ "                                            Estimado/a "+nombre+" "+apellido+"\r\n"
							+ "                                            <br />\r\n"
							+ "                                            <br />\r\n"
							+ "                                            Hemos recibido una solicitud para\r\n"
							+ "                                          </span>\r\n"
							+ "                                          <span style='font-weight:700; color: #3EA795; font-size: 18px;'>\r\n"
							+ "                                            restablecer su contraseña.\r\n"
							+ "                                          </span>\r\n"
							+ "                                          <span>\r\n"
							+ "                                            Ingrese el siguiente codigo para restablecer su contraseña:\r\n"
							+ "                                          </span>\r\n"
							+ "                                        </div>\r\n"
							+ "                                      </td>\r\n"
							+ "                                    </tr>\r\n"
							+ "                                    <tr>\r\n"
							+ "                                      <td align='center' style='font-size:0px;padding:10px 25px;word-break:break-word;'>\r\n"
							+ "                                        <div style='color:#777777;font-family:Oxygen, Helvetica neue, sans-serif;font-size:14px;line-height:21px;text-align:center;'>\r\n"
							+ "                                          <p style='padding: 10px 0; border: 1px solid #cccccc; color: #4d4d4d; font-weight: bold; font-size: 18px; text-align: center;'>\r\n"
							+ "                                            "+codigoDeVerificacion+"\r\n"
							+ "                                          </p>\r\n"
							+ "                                        </div>\r\n"
							+ "                                      </td>\r\n"
							+ "                                    </tr>\r\n"
							+ "                                    <tr>\r\n"
							+ "                                      <td align='center' style='font-size:0px;padding:10px 25px;word-break:break-word;' vertical-align='middle'>\r\n"
							+ "                                        <table border='0' cellpadding='0' cellspacing='0' role='presentation' style='border-collapse:separate;line-height:100%;'>\r\n"
							+ "                                          <tr>\r\n"
							+ "                                            <td align='center' bgcolor='#ffffff' role='presentation' style='background-color:#ffffff;border:none;border-radius:5px;cursor:auto;padding:10px 25px;' valign='middle'>\r\n"
							+ "                                              <a href style='background:#ffffff;color:#777777;font-family:Oxygen, Helvetica neue, sans-serif;font-size:14px;font-weight:400;line-height:21px;margin:0;text-decoration:none;text-transform:none;' target='_blank'>\r\n"
							+ "                                                Saludos cordiales.\r\n"
							+ "                                              </a>\r\n"
							+ "                                            </td>\r\n"
							+ "                                          </tr>\r\n"
							+ "                                        </table>\r\n"
							+ "                                      </td>\r\n"
							+ "                                    </tr>\r\n"
							+ "                                  </table>\r\n"
							+ "                                </td>\r\n"
							+ "                              </tr>\r\n"
							+ "                            </tbody>\r\n"
							+ "                          </table>\r\n"
							+ "                        </div>\r\n"
							+ "                        <!--[if mso | IE]>\r\n"
							+ "</td></tr></table>\r\n"
							+ "<![endif]-->\r\n"
							+ "                      </td>\r\n"
							+ "                    </tr>\r\n"
							+ "                  </tbody>\r\n"
							+ "                </table>\r\n"
							+ "              </div>\r\n"
							+ "            </td>\r\n"
							+ "          </tr>\r\n"
							+ "        </tbody>\r\n"
							+ "      </table>\r\n"
							+ "      <table align='center' border='0' cellpadding='0' cellspacing='0' role='presentation' style='background:#f7f7f7;background-color:#f7f7f7;width:100%;'>\r\n"
							+ "        <tbody>\r\n"
							+ "          <tr>\r\n"
							+ "            <td>\r\n"
							+ "              <div style='margin:0px auto;max-width:600px;'>\r\n"
							+ "                <table align='center' border='0' cellpadding='0' cellspacing='0' role='presentation' style='width:100%;'>\r\n"
							+ "                  <tbody>\r\n"
							+ "                    <tr>\r\n"
							+ "                      <td style='direction:ltr;font-size:0px;padding:20px 0;text-align:center;vertical-align:top;'>\r\n"
							+ "                        <!--[if mso | IE]>\r\n"
							+ "<table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr><td style=\"vertical-align:top;width:600px;\">\r\n"
							+ "<![endif]-->\r\n"
							+ "                        <div class='dys-column-per-100 outlook-group-fix' style='direction:ltr;display:inline-block;font-size:13px;text-align:left;vertical-align:top;width:100%;'>\r\n"
							+ "                          <table border='0' cellpadding='0' cellspacing='0' role='presentation' style='vertical-align:top;' width='100%'>\r\n"
							+ "                            <tr>\r\n"
							+ "                              <td align='center' style='font-size:0px;padding:5px 25px;word-break:break-word;'>\r\n"
							+ "                                <div style='color:#777777;font-family:Oxygen, Helvetica neue, sans-serif;font-size:14px;font-style:bold;font-weight:700;line-height:21px;text-align:center;'>\r\n"
							+ "                                  ExamSoft by Antony Rodriguez\r\n"
							+ "                                </div>\r\n"
							+ "                              </td>\r\n"
							+ "                            </tr>\r\n"
							+ "                            <tr>\r\n"
							+ "                              <td align='center' style='font-size:0px;padding:5px 25px;word-break:break-word;'>\r\n"
							+ "                                <div style='color:#777777;font-family:Oxygen, Helvetica neue, sans-serif;font-size:14px;font-style:bold;line-height:1;text-align:center;'>\r\n"
							+ "                                  Universidad de la serena\r\n"
							+ "                                </div>\r\n"
							+ "                              </td>\r\n"
							+ "                            </tr>\r\n"
							+ "                            <tr>\r\n"
							+ "                              <td align='center' style='font-size:0px;padding:5px 25px;word-break:break-word;'>\r\n"
							+ "                                <div style='color:#777777;font-family:Oxygen, Helvetica neue, sans-serif;font-size:14px;font-style:bold;line-height:1;text-align:center;'>\r\n"
							+ "                                  P.O.O.\r\n"
							+ "                                </div>\r\n"
							+ "                              </td>\r\n"
							+ "                            </tr>\r\n"
							+ "                          </table>\r\n"
							+ "                        </div>\r\n"
							+ "                        <!--[if mso | IE]>\r\n"
							+ "</td></tr></table>\r\n"
							+ "<![endif]-->\r\n"
							+ "                      </td>\r\n"
							+ "                    </tr>\r\n"
							+ "                  </tbody>\r\n"
							+ "                </table>\r\n"
							+ "              </div>\r\n"
							+ "            </td>\r\n"
							+ "          </tr>\r\n"
							+ "        </tbody>\r\n"
							+ "      </table>\r\n"
							+ "    </div>\r\n"
							+ "  </body>\r\n"
							+ "</html>","text/html");
					Transport transporte = sesion.getTransport("smtp");
					transporte.connect(correoEnvia, contraseña);
					transporte.sendMessage(mail, mail.getRecipients(Message.RecipientType.TO));
					transporte.close();
				} catch (AddressException e) {
					e.printStackTrace();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "Codigo enviado");
				panelMayorEmail.removeAll();				
				panelMayorEmail.add(panelIntroduceCodigo(tabla,destinatario));
				panelMayorEmail.updateUI();
				pack();
				setLocationRelativeTo(null);

			}
		});
		BtnBoddyInCorr.setFont(new Font("Thaoma", Font.PLAIN, 20));
		panelBtnBoddyInCorr.add(BtnBoddyInCorr, BorderLayout.CENTER);

		return panelMayorEmail;
	}
	
	/**
	 * 
	 * Este metodo envia un correo al alumno seleccionado en tabla 
	 * posteriormente y envia las respuestas que el dio al momento
	 * de finalizar el examen, ademas agrega una columna mas con 
	 * la nota esperado y la nota obtenida por el usuario.
	 * 
	 * @param tabla Tabla
	 * @param tab Tabla
	 * @param fila Fila de la tabla
	 * @param nombre Nombre del Alumno
	 * @param correo Correo destino
	 * @param nombreExamen Nombre del examen
	 * 
	 */
	public void enviaNotasCorreo(JTable tabla,JTable tab,int fila,String nombre, String correo,String nombreExamen) {
		Properties propiedad = new Properties();
		String destinatario;
		MimeMessage mail;


		propiedad.setProperty("mail.smtp.host", "smtp.gmail.com");
		propiedad.setProperty("mail.smtp.starttls.enable", "true");
		propiedad.setProperty("mail.smtp.port", "587");
		propiedad.setProperty("mail.smtp.auth", "true");

		Session sesion = Session.getDefaultInstance(propiedad);

		codigoDeVerificacion = codigoRandom();
		String correoEnvia = "exam.soft.edu@gmail.com";
		String contraseña = "melopomelo13";
		destinatario = correo;
		String asunto = "Peticion de Respuestas del Examen: "+nombreExamen;
		StringBuilder preguntas = new StringBuilder();
		StringBuilder respuestasExam = new StringBuilder();
		StringBuilder respuestasUser = new StringBuilder();
		StringBuilder tablaExtra = new StringBuilder("");
		String[] nota = String.valueOf(tab.getValueAt(fila, tab.getColumnCount()-1)).split("/");
		
		if(tabla.getColumnCount()-1 < 9) {
			for (int i = 1; i < tabla.getColumnCount(); i++) {
				preguntas.append("<th align=\"center\" style=\"background-color: #1b7991; color: #ffffff; height: 23px; width: 10%; \">Preg"+(i)+"</th>"+"\n");
			}
			preguntas.append("<th align=\"center\" style=\"background-color: #1b7991; color: #ffffff; height: 23px; width: 10%; \">Ptj.</th>"+"\n");
			for (int i = 1; i < tabla.getColumnCount(); i++) {
				respuestasExam.append("<td style=\"background-color: #a6d8e2; color: #000000; text-align: center; height: 23px; width: 10%; \">"+String.valueOf(tabla.getValueAt(0, i))+"</td>"+"\n");
			}
			respuestasExam.append("<td style=\"background-color: #1b7991; color: #ffffff; text-align: center; height: 23px; width: 10%; \">"+nota[1]+"</td>"+"\n");
			for (int i = 1; i < tabla.getColumnCount(); i++) {
				respuestasUser.append("<td style=\"background-color: #a6d8e2; color: #000000; text-align: center; width: 10%; \">"+String.valueOf(tabla.getValueAt(1, i))+"</td>"+"\n");
			}
			respuestasUser.append("<td style=\"background-color: #1b7991; color: #ffffff; text-align: center; width: 10%; \">"+nota[0]+"</td>"+"\n");
		}else {
			tablaExtra.append("<tr align=\"center\" style=\"height: 23px;\">"+"\n");
			StringBuilder tmp1 = new StringBuilder();
			tmp1.append("<th align=\"center\" style=\"background-color: #ffffff; color: #ffffff; height: 23px; width: 6%; \"><br></th>"+"\n");
			StringBuilder tmp2 = new StringBuilder();
			tmp2.append("<th align=\"center\" style=\"background-color: #1b7991; color: #ffffff; height: 23px; width: 6%; \">Resp. Correcta</th>"+"\n");
			StringBuilder tmp3 = new StringBuilder();
			tmp3.append("<th align=\"center\" style=\"background-color: #1b7991; color: #ffffff; height: 23px; width: 6%; \">Resp. Usuario</th>"+"\n");
			for (int i = 1; i < tabla.getColumnCount(); i++) {
				if(i < 9) {
					preguntas.append("<th align=\"center\" style=\"background-color: #1b7991; color: #ffffff; height: 23px; width: 10%; \">Preg"+(i)+"</th>"+"\n");
				}else {
					tmp1.append("<th align=\"center\" style=\"background-color: #1b7991; color: #ffffff; height: 23px; width: 10%; \">Preg"+(i)+"</th>"+"\n");
				}
			}
			tmp1.append("<th align=\"center\" style=\"background-color: #1b7991; color: #ffffff; height: 23px; width: 10%; \">Ptj.</th>"+"\n");
			tablaExtra.append(tmp1.toString()+"\n");
			tablaExtra.append("</tr>"+"\n");
			tablaExtra.append("<tr style=\"height: 23px;\">"+"\n");
			for (int i = 1; i < tabla.getColumnCount(); i++) {
				if(i < 9) {
					respuestasExam.append("<td style=\"background-color: #a6d8e2; color: #000000; text-align: center; height: 23px; width: 10%; \">"+String.valueOf(tabla.getValueAt(0, i))+"</td>"+"\n");
				}else {
					tmp2.append("<td style=\"background-color: #a6d8e2; color: #000000; text-align: center; height: 23px; width: 10%; \">"+String.valueOf(tabla.getValueAt(0, i))+"</td>"+"\n");
				}
			}
			tmp2.append("<th align=\"center\" style=\"background-color: #1b7991; color: #ffffff; height: 23px; width: 10%; \">"+nota[1]+"</th>"+"\n");
			tablaExtra.append(tmp2.toString()+"\n");
			tablaExtra.append("</tr>"+"\n");
			tablaExtra.append("<tr style=\"height: 23px;\">"+"\n");
			for (int i = 1; i < tabla.getColumnCount(); i++) {
				if(i < 9) {
					respuestasUser.append("<td style=\"background-color: #a6d8e2; color: #000000; text-align: center; width: 10%; \">"+String.valueOf(tabla.getValueAt(1, i))+"</td>"+"\n");
				}else {
					tmp3.append("<td style=\"background-color: #a6d8e2; color: #000000; text-align: center; width: 10%; \">"+String.valueOf(tabla.getValueAt(1, i))+"</td>"+"\n");
				}		
			}
			tmp3.append("<th align=\"center\" style=\"background-color: #1b7991; color: #ffffff; height: 23px; width: 10%; \">"+nota[0]+"</th>"+"\n");
			tablaExtra.append(tmp3.toString()+"\n");
			tablaExtra.append("</tr>"+"\n");
		}
		
	
		
		String mensaje = "<p><br></p>\r\n"
				+ "<!-- [if !mso]><!-->\r\n"
				+ "<p><br></p>\r\n"
				+ "<!--<![endif]-->\r\n"
				+ "<p><br></p>\r\n"
				+ "<!-- [if mso]> \r\n"
				+ "		<xml> \r\n"
				+ "			<o:OfficeDocumentSettings> \r\n"
				+ "				<o:AllowPNG/> \r\n"
				+ "				<o:PixelsPerInch>96</o:PixelsPerInch> \r\n"
				+ "			</o:OfficeDocumentSettings> \r\n"
				+ "		</xml>\r\n"
				+ "		<![endif]-->\r\n"
				+ "<p><br></p>\r\n"
				+ "<!-- [if lte mso 11]> \r\n"
				+ "		<style type=\"text/css\"> \r\n"
				+ "			.outlook-group-fix{width:100% !important;}\r\n"
				+ "		</style>\r\n"
				+ "		<![endif]-->\r\n"
				+ "<p><br></p>\r\n"
				+ "<div>\r\n"
				+ "    <!-- [if mso | IE]>\r\n"
				+ "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:600px;\" width=\"600\"><tr><td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\r\n"
				+ "<![endif]-->\r\n"
				+ "    <div style=\"background: #4DBFBF; background-color: #fffff; margin: 0px auto; max-width: 600px;\">\r\n"
				+ "\r\n"
				+ "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"background: #ffffff; width: 99.8333%;\">\r\n"
				+ "            <tbody>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td style=\"direction: ltr; font-size: 0px; padding: 20px 0px; text-align: center; vertical-align: top; background-color: #4dbfbf; width: 100%;\">\r\n"
				+ "                        <div class=\"dys-column-per-100 outlook-group-fix\" style=\"direction: ltr; display: inline-block; font-size: 13px; text-align: left; vertical-align: top; width: 100%;\"><br>\r\n"
				+ "                            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"vertical-align: top;\" width=\"100%\">\r\n"
				+ "                                <tbody>\r\n"
				+ "                                    <tr>\r\n"
				+ "                                        <td align=\"center\" style=\"font-size: 0px; padding: 10px 25px; word-break: break-word; background-color: #4dbfbf; color: #ffffff;\">\r\n"
				+ "                                            <div style=\"color: #ffffff; font-family:Oxygen, Helvetica neue, sans-serif; font-size: 50px; line-height: 1; text-align: center;\">ExamSoft</div>\r\n"
				+ "                                            <div style=\"color: rgba(0,0,0,0.01); width: 0; height: 0;\">&nbsp;</div>\r\n"
				+ "                                            <div style=\"color: rgba(0,0,0,0.01); width: 0; height: 0;\">&nbsp;</div>\r\n"
				+ "                                        </td>\r\n"
				+ "                                    </tr>\r\n"
				+ "                                    <tr>\r\n"
				+ "                                        <td align=\"center\" style=\"font-size: 0px; padding: 10px 25px; word-break: break-word; background-color: #4dbfbf;\">\r\n"
				+ "                                            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"border-collapse: collapse; border-spacing: 0px; width: 216px;\">\r\n"
				+ "                                                <tbody>\r\n"
				+ "                                                    <tr>\r\n"
				+ "                                                        <td style=\"width: 215px;\"><img alt=\"Descriptive Alt Text\" height=\"189\" src=\"https://i.ibb.co/wLW5p6c/software.png\" style=\"border: none; display: block; font-size: 13px; height: 189px; outline: none; text-decoration: none; width: 100%;\" width=\"216\"></td>\r\n"
				+ "                                                    </tr>\r\n"
				+ "                                                </tbody>\r\n"
				+ "                                            </table>\r\n"
				+ "                                        </td>\r\n"
				+ "                                    </tr>\r\n"
				+ "                                    <tr>\r\n"
				+ "                                        <td align=\"center\" style=\"font-size: 0px; padding: 10px 25px; word-break: break-word; background-color: #4dbfbf;\">\r\n"
				+ "                                            <div style=\"color: #ffffff; font-family:Oxygen, Helvetica neue, sans-serif; font-size: 25px; line-height: 25px; text-align: center;\">El aprendizaje en línea es el presente y el futuro.</div>\r\n"
				+ "                                        </td>\r\n"
				+ "                                    </tr>\r\n"
				+ "                                </tbody>\r\n"
				+ "                            </table>\r\n"
				+ "                        </div>\r\n"
				+ "                        <!-- [if mso | IE]>\r\n"
				+ "</td></tr></table>\r\n"
				+ "<![endif]-->\r\n"
				+ "                        <div style=\"color: rgba(0,0,0,0.01); width: 0; height: 0;\">&nbsp;</div>\r\n"
				+ "                        <div style=\"color: rgba(0,0,0,0.01); width: 0; height: 0;\">&nbsp;</div>\r\n"
				+ "                    </td>\r\n"
				+ "                </tr>\r\n"
				+ "            </tbody>\r\n"
				+ "        </table>\r\n"
				+ "    </div>\r\n"
				+ "    <!-- [if mso | IE]>\r\n"
				+ "</td></tr></table>\r\n"
				+ "<![endif]-->\r\n"
				+ "    <!-- [if mso | IE]>\r\n"
				+ "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:600px;\" width=\"600\"><tr><td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\r\n"
				+ "<![endif]-->\r\n"
				+ "    <div style=\"background: #ffffff; background-color: #ffffff; margin: 0px auto; max-width: 600px;\">\r\n"
				+ "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"height: 24px; width: 595px; background: #ffffff;\">\r\n"
				+ "            <tbody>\r\n"
				+ "                <tr style=\"height: 24px;\">\r\n"
				+ "                    <td style=\"direction: ltr; font-size: 0px; padding: 20px 0px; text-align: center; vertical-align: top; background-color: #ffffff; height: 24px; width: 594px;\">\r\n"
				+ "                        <!-- [if mso | IE]>\r\n"
				+ "<table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr><td style=\"vertical-align:top;width:300px;\">\r\n"
				+ "<![endif]-->\r\n"
				+ "                        <div class=\"dys-column-per-50 outlook-group-fix\" style=\"direction: ltr; display: inline-block; font-size: 13px; text-align: left; vertical-align: top; width: 100%;\">\r\n"
				+ "                            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"height: 20px; vertical-align: top; width: 100%;\" width=\"100%\">\r\n"
				+ "                                <tbody>\r\n"
				+ "                                    <tr style=\"height: 39px;\">\r\n"
				+ "                                        <td align=\"left\" style=\"font-size: 0px; padding: 25px 25px 0px 50px; word-break: break-word; height: 39px;\">\r\n"
				+ "                                            <div style=\"color: #777777; font-family: 'Droid Sans', 'Helvetica Neue', Arial, sans-serif; font-size: 25px; line-height: 1; text-align: left;\">"+nombre+"</div>\r\n"
				+ "                                        </td>\r\n"
				+ "                                    </tr>\r\n"
				+ "                                    <tr style=\"height: 28px;\">\r\n"
				+ "                                        <td align=\"left\" style=\"font-size: 0px; padding: 0px 25px 0px 50px; word-break: break-word; height: 28px;\">\r\n"
				+ "                                            <div style=\"color: #1b7991;font-family: 'Droid Sans', 'Helvetica Neue', Arial, sans-serif; font-size: 20px; line-height: 1; text-align: left;\">Respuestas del Examen "+nombreExamen+"</div>\r\n"
				+ "                                        </td>\r\n"
				+ "                                    </tr>\r\n"
				+ "                                </tbody>\r\n"
				+ "                            </table>\r\n"
				+ "                        </div>\r\n"
				+ "                        <!-- [if mso | IE]>\r\n"
				+ "</td><td style=\"vertical-align:top;width:300px;\">\r\n"
				+ "<![endif]-->\r\n"
				+ "                        <div class=\"dys-column-per-50 outlook-group-fix\" style=\"direction: ltr; display: inline-block; font-size: 13px; text-align: left; vertical-align: top; width: 100%;\"><br></div>\r\n"
				+ "                        <!-- [if mso | IE]>\r\n"
				+ "</td></tr></table>\r\n"
				+ "<![endif]-->\r\n"
				+ "                        <div style=\"color: rgba(0,0,0,0.01); width: 0; height: 0;\">&nbsp;</div>\r\n"
				+ "                    </td>\r\n"
				+ "                </tr>\r\n"
				+ "            </tbody>\r\n"
				+ "        </table>\r\n"
				+ "    </div>\r\n"
				+ "    <!-- [if mso | IE]>\r\n"
				+ "</td></tr></table>\r\n"
				+ "<![endif]-->\r\n"
				+ "    <!-- [if mso | IE]>\r\n"
				+ "<table align=\"margin: 0 auto;\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:600px;\" width=\"600\"><tr><td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\r\n"
				+ "<![endif]-->\r\n"
				+ "    <div style=\"background: #ffffff; background-color: #ffffff; margin: 0px auto; max-width: 5000px;\">\r\n"
				+ "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"background: #ffffff; width: 1200px; height: 100px;\">\r\n"
				+ "            <tbody>\r\n"
				+ "                <tr style=\"height: 100px;\">\r\n"
				+ "                    <td style=\"direction: ltr; font-size: 0px; padding: 20px 0px; text-align: center; vertical-align: top; height: 100px; width: 999px;\">\r\n"
				+ "                        <!-- [if mso | IE]>\r\n"
				+ "<table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr><td style=\"vertical-align:top;width:600px;\">\r\n"
				+ "<![endif]-->\r\n"
				+ "                        <div class=\"dys-column-per-100 outlook-group-fix\" style=\"direction: ltr; display: inline-block; font-size: 13px; text-align: left; vertical-align: top; width: 100%;\">\r\n"
				+ "                            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"vertical-align: top; height: 83px; width: 100%;\" width=\"100%\">\r\n"
				+ "                                <tbody>\r\n"
				+ "                                    <tr style=\"height: 83px;\">\r\n"
				+ "                                        <td align=\"left\" style=\"font-size: 0px; padding: 10px 50px; word-break: break-word; height: 83px;\">\r\n"
				+ "                                            <table border = 1px cellpadding=\"10\" cellspacing=\"0\" style=\"color: #ffffff; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 22px; table-layout: auto; width: 100%; height: 46px;\" width=\"100%\">\r\n"
				+ "                                                <tbody>\r\n"
				+ "                                                    <tr align=\"center\" style=\"height: 23px;\">\r\n"
				+ "                                                        <th align=\"center\" style=\"background-color: #ffffff; color: #ffffff; height: 23px; width: 5%; \"><br></th>   \r\n"
				+ "                                           \r\n"
				+
				preguntas.toString()
				+ "                                                    </tr>\r\n"
				+ "                                                    <tr style=\"height: 23px;\">\r\n"
				+ "                                                        <td style=\"background-color: #1b7991; color: #ffffff; text-align: center; height: 23px; width: 5%; \">Resp. Correcta</td>\r\n"																		
				+ "\r\n"
				+
				respuestasExam.toString()
				+ "                                                       \r\n"
				+ "                                                    </tr>\r\n"
				+ "                                                    <tr>\r\n"
				+ "                                                        <td style=\"background-color: #1b7991; color: #ffffff; text-align: center; width: 5%; \">Resp. Alumno</td>\r\n"
				+ "\r\n"
				+
				respuestasUser.toString()
				+ "                                                        \r\n"
				+ "                                                    </tr>\r\n"
				+
				tablaExtra.toString()
				
				+ "                                                </tbody>\r\n"
				+ "                                            </table>\r\n"
				+ "                                        </td>\r\n"
				+ "                                    </tr>\r\n"
				+ "                                </tbody>\r\n"
				+ "                            </table>\r\n"
				+ "                        </div>\r\n"
				+ "                        <!-- [if mso | IE]>\r\n"
				+ "</td></tr></table>\r\n"
				+ "<![endif]-->\r\n"
				+ "                        <div style=\"color: rgba(0,0,0,0.01); width: 0; height: 0;\">&nbsp;</div>\r\n"
				+ "                    </td>\r\n"
				+ "                </tr>\r\n"
				+ "            </tbody>\r\n"
				+ "        </table>\r\n"
				+ "    </div>\r\n"
				+ "    <div style=\"background: #F5774E; background-color: #f5774e; margin: 0px auto; max-width: 600px;\">\r\n"
				+ "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"background: #F5774E; background-color: #f5774e; width: 100%;\">\r\n"
				+ "            <tbody>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td style=\"direction: ltr; font-size: 0px; padding: 10px 0px; text-align: center; vertical-align: top; background-color: #ffffff;\">\r\n"
				+ "                        <!-- [if mso | IE]>\r\n"
				+ "<table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr><td style=\"vertical-align:top;width:540px;\">\r\n"
				+ "<![endif]-->\r\n"
				+ "                        <div class=\"dys-column-per-90 outlook-group-fix\" style=\"direction: ltr; display: inline-block; font-size: 13px; text-align: left; vertical-align: top; width: 100%;\">\r\n"
				+ "                            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"vertical-align: top;\" width=\"100%\">\r\n"
				+ "                                <tbody>\r\n"
				+ "                                    <tr>\r\n"
				+ "                                        <td align=\"left\" style=\"font-size: 0px; padding: 10px 25px; word-break: break-word;\">\r\n"
				+ "                                            <div style=\"color: #1b7991;font-family: 'Droid Sans', 'Helvetica Neue', Arial, sans-serif; font-size: 13px; line-height: 1; text-align: left;\">Gracias por usar este software. Comuníquese con un administrador si tiene alguna duda relacionada con esta Informacion ExamSoft.</div>\r\n"
				+ "                                        </td>\r\n"
				+ "                                    </tr>\r\n"
				+ "                                </tbody>\r\n"
				+ "                            </table>\r\n"
				+ "                        </div>\r\n"
				+ "                        <!-- [if mso | IE]>\r\n"
				+ "</td></tr></table>\r\n"
				+ "<![endif]-->\r\n"
				+ "                        <div style=\"color: rgba(0,0,0,0.01); width: 0; height: 0;\">&nbsp;</div>\r\n"
				+ "                    </td>\r\n"
				+ "                </tr>\r\n"
				+ "            </tbody>\r\n"
				+ "        </table>\r\n"
				+ "    </div>\r\n"
				+ "    <!-- [if mso | IE]>\r\n"
				+ "</td></tr></table>\r\n"
				+ "<![endif]-->\r\n"
				+ "    <!-- [if mso | IE]>\r\n"
				+ "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:600px;\" width=\"600\"><tr><td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\r\n"
				+ "<![endif]-->\r\n"
				+ "    <div style=\"background: #414141; background-color: #414141; margin: 0px auto; max-width: 600px;\">\r\n"
				+ "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"background: #414141; background-color: #414141; width: 100%;\">\r\n"
				+ "            <tbody>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td style=\"direction: ltr; font-size: 0px; padding: 20px 0; padding-bottom: 0px; text-align: center; vertical-align: top;\">\r\n"
				+ "                        <!-- [if mso | IE]>\r\n"
				+ "<table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr><td style=\"vertical-align:top;width:600px;\">\r\n"
				+ "<![endif]-->\r\n"
				+ "                        <div class=\"dys-column-per-100 outlook-group-fix\" style=\"direction: ltr; display: inline-block; font-size: 13px; text-align: left; vertical-align: top; width: 100%;\">\r\n"
				+ "                            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"vertical-align: top;\" width=\"100%\">\r\n"
				+ "                                <tbody>\r\n"
				+ "                                    <tr>\r\n"
				+ "                                        <td align=\"center\" style=\"font-size: 0px; padding: 10px 25px; word-break: break-word;\">\r\n"
				+ "                                            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"cellpadding: 0; cellspacing: 0; color: #000000; font-family: Helvetica, Arial, sans-serif; font-size: 13px; line-height: 22px; table-layout: auto; width: 40%;\" width=\"40%\">\r\n"
				+ "                                                <tbody>\r\n"
				+ "                                                    <tr align=\"center\">\r\n"
				+ "                                                        <td align=\"center\"><a href=\"https://facebook.com\"> <img alt=\"facebook\" height=\"50px\" src=\"https://swu-cs-assets.s3.amazonaws.com/OSET/neopolitan/facebook.png\" width=\"50px\"> </a></td>\r\n"
				+ "                                                        <td align=\"center\"><a href=\"https://twitter.com\"> <img alt=\"twitter\" height=\"50px\" src=\"https://swu-cs-assets.s3.amazonaws.com/OSET/neopolitan/twitter.png\" width=\"50px\"> </a></td>\r\n"
				+ "                                                    </tr>\r\n"
				+ "                                                </tbody>\r\n"
				+ "                                            </table>\r\n"
				+ "                                        </td>\r\n"
				+ "                                    </tr>\r\n"
				+ "                                </tbody>\r\n"
				+ "                            </table>\r\n"
				+ "                        </div>\r\n"
				+ "                        <!-- [if mso | IE]>\r\n"
				+ "</td></tr></table>\r\n"
				+ "<![endif]-->\r\n"
				+ "                    </td>\r\n"
				+ "                </tr>\r\n"
				+ "            </tbody>\r\n"
				+ "        </table>\r\n"
				+ "    </div>\r\n"
				+ "    <!-- [if mso | IE]>\r\n"
				+ "</td></tr></table>\r\n"
				+ "<![endif]-->\r\n"
				+ "    <!-- [if mso | IE]>\r\n"
				+ "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:600px;\" width=\"600\"><tr><td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\r\n"
				+ "<![endif]-->\r\n"
				+ "    <div style=\"background: #414141; background-color: #414141; margin: 0px auto; max-width: 600px;\">\r\n"
				+ "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"background: #414141; background-color: #414141; width: 100%;\">\r\n"
				+ "            <tbody>\r\n"
				+ "                <tr>\r\n"
				+ "                    <td style=\"direction: ltr; font-size: 0px; padding: 20px 0; padding-top: 0px; text-align: center; vertical-align: top;\">\r\n"
				+ "                        <!-- [if mso | IE]>\r\n"
				+ "<table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr><td style=\"vertical-align:top;width:600px;\">\r\n"
				+ "<![endif]-->\r\n"
				+ "                        <div class=\"dys-column-per-100 outlook-group-fix\" style=\"direction: ltr; display: inline-block; font-size: 13px; text-align: left; vertical-align: top; width: 100%;\">\r\n"
				+ "                            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"vertical-align: top;\" width=\"100%\">\r\n"
				+ "                                <tbody>\r\n"
				+ "                                    <tr>\r\n"
				+ "                                        <td align=\"center\" style=\"font-size: 0px; padding: 10px 25px; word-break: break-word;\">\r\n"
				+ "                                            <div style=\"color: #bbbbbb; font-family: 'Droid Sans', 'Helvetica Neue', Arial, sans-serif; font-size: 12px; line-height: 1; text-align: center;\">Ver en el buscador | Desuscribirse | Contacto © 2020 Todos Los Derechos Reservados</div>\r\n"
				+ "                                        </td>\r\n"
				+ "                                    </tr>\r\n"
				+ "                                </tbody>\r\n"
				+ "                            </table>\r\n"
				+ "                        </div>\r\n"
				+ "                        <!-- [if mso | IE]>\r\n"
				+ "</td></tr></table>\r\n"
				+ "<![endif]-->\r\n"
				+ "                    </td>\r\n"
				+ "                </tr>\r\n"
				+ "            </tbody>\r\n"
				+ "        </table>\r\n"
				+ "    </div>\r\n"
				+ "    <!-- [if mso | IE]>\r\n"
				+ "</td></tr></table>\r\n"
				+ "<![endif]-->\r\n"
				+ "</div>";
		

		try {
			
			mail = new MimeMessage(sesion);
			mail.setFrom(new InternetAddress(correoEnvia));
			mail.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
			mail.setSubject(asunto);
			mail.setContent(mensaje,"text/html");
			Transport transporte = sesion.getTransport("smtp");
			transporte.connect(correoEnvia, contraseña);
			transporte.sendMessage(mail, mail.getRecipients(Message.RecipientType.TO));
			transporte.close();
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "Respuestas enviadas");

	}
	

	/**
	 * Este metodo funciona una vez el codigo de confirmacion ingresardo 
	 * coincide con el que se genero en el programa, entonces se procede
	 * a cambiar la contraseña.
	 *
	 * @param tablaName Nombre
	 * @param correo Destinatario
	 * @return JPanel 
	 * 
	 *  
	 * 
	 */
	public JPanel panelCambiaContraseña(String tablaName, String correo) {
		JPanel panelCambioContra = new JPanel(new BorderLayout(0, 20));

		JPanel panelSuperior = new JPanel(new BorderLayout());
		panelCambioContra.add(panelSuperior, BorderLayout.NORTH);
		JPanel panelCuerpo = new JPanel(new BorderLayout(0, 20));
		panelCuerpo.setBorder(new EmptyBorder(0, 0, 0, 0));
		panelCambioContra.add(panelCuerpo, BorderLayout.CENTER);
		JPanel panelBoton = new JPanel(new BorderLayout());
		panelBoton.setBorder(new EmptyBorder(0, 100, 30, 100));
		panelCambioContra.add(panelBoton, BorderLayout.SOUTH);

		JPanel panelTitulo = new JPanel(new BorderLayout(0, 10));
		panelTitulo.setBorder(new EmptyBorder(0, 70, 0, 70));
		panelCuerpo.add(panelTitulo, BorderLayout.NORTH);
		JPanel panelContra = new JPanel(new BorderLayout());
		panelContra.setBorder(new EmptyBorder(0, 100, 0, 100));
		panelCuerpo.add(panelContra, BorderLayout.CENTER);
		JPanel panelConfirmaContra = new JPanel(new BorderLayout());
		panelConfirmaContra.setBorder(new EmptyBorder(0, 100, 0, 100));
		panelCuerpo.add(panelConfirmaContra, BorderLayout.SOUTH);

		JMenuBar barra = new JMenuBar();
		panelSuperior.add(barra, BorderLayout.NORTH);
		JMenu opciones = new JMenu("Opciones");
		opciones.setFont(new Font("Thaoma", Font.PLAIN, 15));
		barra.add(opciones);
		JMenuItem volver = new JMenuItem("Volver");
		volver.setFont(new Font("Thaoma", Font.PLAIN, 15));
		volver.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				if (tablaName.equalsIgnoreCase("profesores")) {
					new ProfesorGUI();
				} else {
					new AlumnoGUI();
				}

			}
		});
		opciones.add(volver);
		JMenuItem salir = new JMenuItem("Salir");
		salir.setFont(new Font("Thaoma", Font.PLAIN, 15));
		salir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		opciones.add(salir);

		JLabel labelTitulo = new JLabel("Cambio de contraseña");
		labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		labelTitulo.setFont(new Font("Thaoma", Font.PLAIN, 40));
		panelTitulo.add(labelTitulo, BorderLayout.CENTER);

		JLabel labelInfoTitulo = new JLabel("<html><div style='text-align: center;'> Ingrese una nueva contraseña<br>de almenos 8 caracteres </div></html>");
		labelInfoTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		labelInfoTitulo.setFont(new Font("Thaoma", Font.PLAIN, 18));
		panelTitulo.add(labelInfoTitulo, BorderLayout.SOUTH);

		JLabel labelContra = new JLabel("Nueva Contraseña");
		labelContra.setFont(new Font("Thaoma", Font.PLAIN, 18));
		labelContra.setHorizontalAlignment(SwingConstants.LEFT);
		panelContra.add(labelContra, BorderLayout.NORTH);

		JPasswordField textContra = new JPasswordField();
		textContra.setBackground(Color.WHITE);
		textContra.setForeground(Color.BLACK);
		textContra.setFont(new Font("Thaoma", Font.PLAIN, 20));
		panelContra.add(textContra, BorderLayout.CENTER);

		JLabel labelConfirmaContra = new JLabel("Confirmar");
		labelConfirmaContra.setFont(new Font("Thaoma", Font.PLAIN, 18));
		labelConfirmaContra.setHorizontalAlignment(SwingConstants.LEFT);
		panelConfirmaContra.add(labelConfirmaContra, BorderLayout.NORTH);

		JPasswordField textConfirmaContra = new JPasswordField();
		textConfirmaContra.setBackground(Color.WHITE);
		textConfirmaContra.setForeground(Color.BLACK);
		textConfirmaContra.setFont(new Font("Thaoma", Font.PLAIN, 20));
		panelConfirmaContra.add(textConfirmaContra, BorderLayout.CENTER);

		JButton botonAplicar = new JButton("Aplicar Cambios");
		botonAplicar.setFont(new Font("Thaoma", Font.PLAIN, 18));
		botonAplicar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String contra = "";
				String contra2 = "";
				for (char c : textContra.getPassword()) {
					contra += c;
				}
				for (char c : textConfirmaContra.getPassword()) {
					contra2 += c;
				}

				if (contra.isBlank() || contra2.isBlank()) {
					JOptionPane.showMessageDialog(null, "Llene las casillas");
					return;
				}
				if (contra.length() < 8 || contra2.length() < 8) {
					JOptionPane.showMessageDialog(null, "La contraseña debe tener al menos 8 caracteres");
					return;
				}
				if (!contra.equals(contra2)) {
					JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden");
					return;
				}
				PreparedStatement ps = null;
				try {
					Conexion objCon = new Conexion();
					Connection conn = objCon.getConnection();
					if (tablaName.equals("alumnos")) {
						ps = conn.prepareStatement("UPDATE alumnos SET Contraseña = AES_ENCRYPT(?,'alumno') WHERE Correo = ?");
					} else {
						ps = conn.prepareStatement("UPDATE profesores SET Contraseña = AES_ENCRYPT(?,'profesores') WHERE Correo = ?");
					}
					ps.setString(1, contra2);
					ps.setString(2, correo);
					int resl = ps.executeUpdate();

					if (resl > 0) {
						JOptionPane.showMessageDialog(null, "Contraseña Actualizada");
						dispose();
						if (tablaName.equalsIgnoreCase("profesores")) {
							new ProfesorGUI();
						} else {
							new AlumnoGUI();
						}
					} else {
						JOptionPane.showMessageDialog(null, "No se pudo cambiar la contraseña");
					}
					conn.close();
				} catch (Exception e) {
					System.err.println(e.toString());
				}

			}
		});
		panelBoton.add(botonAplicar, BorderLayout.CENTER);

		return panelCambioContra;
	}
	
	/**
	 * En esta funcion se tiene una casilla para ingresar
	 * el codigo enviado al correo y se procede a comparar
	 * con el codigo generado.
	 * 
	 * 
	 * @param tabla
	 * @param destinatario
	 * @return JPanel
	 * 
	 * 
	 * 
	 */
	JPanel panelIntroduceCodigo(String tabla,String destinatario) {
		JPanel panelCodigo = new JPanel(new BorderLayout());
		JPanel panelMenu = new JPanel(new BorderLayout());
		panelCodigo.add(panelMenu,BorderLayout.NORTH);
		
		JPanel panelCuerpo = new JPanel(new BorderLayout(0,20));
		panelCuerpo.setBorder(new EmptyBorder(30, 30, 30, 30));
		panelCodigo.add(panelCuerpo,BorderLayout.CENTER);
		
		JPanel panelTitulo = new JPanel(new BorderLayout());
		panelTitulo.setBorder(new EmptyBorder(20, 30, 10, 30));
		panelMenu.add(panelTitulo,BorderLayout.SOUTH);
		
		JPanel panelIntroCode = new JPanel(new BorderLayout(0,5));
		panelIntroCode.setBorder(new EmptyBorder(0, 30, 0, 30));
		panelCuerpo.add(panelIntroCode,BorderLayout.CENTER);
		
		JPanel panelboton = new JPanel(new BorderLayout());
		panelboton.setBorder(new EmptyBorder(0, 30, 0, 30));
		panelCuerpo.add(panelboton,BorderLayout.SOUTH);
		
		JMenuBar barra = new JMenuBar();
		panelMenu.add(barra, BorderLayout.NORTH);
		JMenu opciones = new JMenu("Opciones");
		opciones.setFont(new Font("Thaoma", Font.PLAIN, 15));
		barra.add(opciones);
		JMenuItem volver = new JMenuItem("Volver");
		volver.setFont(new Font("Thaoma", Font.PLAIN, 15));
		volver.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				if (tabla.equalsIgnoreCase("profesores")) {
					new ProfesorGUI();
				} else {
					new AlumnoGUI();
				}
			}
		});
		opciones.add(volver);
		JMenuItem salir = new JMenuItem("Salir");
		salir.setFont(new Font("Thaoma", Font.PLAIN, 15));
		salir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		opciones.add(salir);
		
		JLabel labelTitulo = new JLabel("<html><div style='text-align: center;'> Por favor introdusca el codigo que se<br>le envió al correo asociado </div></html>");
		labelTitulo.setFont(new Font("Thaoma", Font.PLAIN, 22));
		panelTitulo.add(labelTitulo,BorderLayout.CENTER);
		
		JLabel labelCodigo = new JLabel("Codigo");
		labelCodigo.setFont(new Font("Thaoma", Font.PLAIN, 18));
		labelCodigo.setHorizontalAlignment(SwingConstants.LEFT);
		panelIntroCode.add(labelCodigo,BorderLayout.NORTH);
		
		JTextField textCode = new JTextField();
		textCode.setBackground(Color.WHITE);
		textCode.setForeground(Color.BLACK);
		textCode.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {
				if(textCode.getText().length() > 5) {
						arg0.consume();
				}
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				int pos = textCode.getCaretPosition();
				 textCode.setText(textCode.getText().toUpperCase());
				 textCode.setCaretPosition(pos);
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
			}
		});
		textCode.setFont(new Font("Thaoma", Font.PLAIN, 20));
		panelIntroCode.add(textCode,BorderLayout.CENTER);
		
		JButton boton = new JButton("Continuar");
		boton.setFont(new Font("Thaoma", Font.PLAIN, 20));
		boton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!textCode.getText().equals(codigoDeVerificacion)) {
					JOptionPane.showMessageDialog(null, "Codigo Invalido");
					return;
				}
				panelCodigo.removeAll();
				panelCodigo.add(panelCambiaContraseña(tabla, destinatario));
				panelCodigo.updateUI();
				pack();
				setLocationRelativeTo(null);	
			}
		});
		panelboton.add(boton,BorderLayout.SOUTH);
		
		
		
		
		return panelCodigo;
	}

	/**
	 * Este metodo retorna un codigo random.
	 * 
	 * 
	 * @return String
	 * 
	 * 
	 */
	public String codigoRandom() {
		int leftLimit = 48; // numero '0'
		int rightLimit = 90; // letra 'z'
		int targetStringLength = 6;
		Random random = new Random();

		String codigoRndm = random.ints(leftLimit, rightLimit + 1)
				.filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)).limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

		return codigoRndm;
	}

	/**
	 * Este metodo asegura que el formato del email es valido.
	 * 
	 * @param email
	 * @return Boolean
	 * 
	 * 
	 */
	public static boolean isValidEmailAddress(String email) {
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			result = false;
		}
		return result;
	}
	
	/**
	 * Este metodo asegura que el correo existe en la base de datos.
	 * 
	 * 
	 * @param email Email
	 * @param tabla Tabla
	 * @return boolean
	 * 
	 * 
	 */
	public boolean correoExiste(String email,String tabla) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Conexion objCon = new Conexion();
			Connection conn = objCon.getConnection();
			ps = conn.prepareStatement("SELECT Correo FROM " + tabla);
			rs = ps.executeQuery();

			while (rs.next()) {
				if(email.equals(rs.getString("Correo"))) {
					return true;
				}
			}
		} catch (Exception e) {
			System.err.println(e.toString());
		}	
		return false;
	}

}
