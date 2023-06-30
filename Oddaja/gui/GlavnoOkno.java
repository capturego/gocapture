package gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EnumMap;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import vodja.Vodja;
import vodja.VrstaIgralca;
import logika.Igralec;
import logika.Tezavnost;


/**
 * Glavno okno aplikacije hrani trenutno stanje igre in nadzoruje potek
 * igre.
 *
 */
@SuppressWarnings("serial")
public class GlavnoOkno extends JFrame implements ActionListener {
	/**
	 * JPanel, v katerega igramo
	 */
	private IgralnoPolje polje;

	
	//Statusna vrstica v spodnjem delu okna
	private JLabel status;
	
	// Izbire v menujih
	private JMenuItem igraClovekRacunalnik;
	private JMenuItem igraRacunalnikClovek;
	private JMenuItem igraClovekClovek;
	private JMenuItem igraRacunalnikRacunalnik;
	private JMenuItem naloziIgro;
	private JMenuItem shraniIgro;
	private JMenuItem izhod;

	// Za shranjevanje in nalaganje iger
	private JFileChooser FC;

//	private String[] tezavnosti = {"srednja", "lahka", "prelahka"};;
	/**
	 * Ustvari novo glavno okno in prični igrati igro.
	 */
	public GlavnoOkno() {
		
		this.setTitle("Capture Go");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridBagLayout());
		this.FC = new JFileChooser();
		// menu
		JMenuBar menu_bar = new JMenuBar();
		this.setJMenuBar(menu_bar);
		JMenu igra_menu = new JMenu("Nova igra");
		menu_bar.add(igra_menu);
		JMenu ostalo = new JMenu("Ostalo");
		menu_bar.add(ostalo);

		igraClovekRacunalnik = new JMenuItem("Človek – računalnik");
		igra_menu.add(igraClovekRacunalnik);
		igraClovekRacunalnik.addActionListener(this);
		
		igraRacunalnikClovek = new JMenuItem("Računalnik – človek");
		igra_menu.add(igraRacunalnikClovek);
		igraRacunalnikClovek.addActionListener(this);
		
		igraClovekClovek = new JMenuItem("Človek – človek");
		igra_menu.add(igraClovekClovek);
		igraClovekClovek.addActionListener(this);
		
		igraRacunalnikRacunalnik = new JMenuItem("Računalnik – računalnik");
		igra_menu.add(igraRacunalnikRacunalnik);
		igraRacunalnikRacunalnik.addActionListener(this);
		
		naloziIgro = new JMenuItem("Naloži igro");
		ostalo.add(naloziIgro);
		naloziIgro.addActionListener(this);

		shraniIgro = new JMenuItem("Shrani igro");
		ostalo.add(shraniIgro);
		shraniIgro.addActionListener(this);

		izhod = new JMenuItem("Izhod");
		ostalo.add(izhod);
		izhod.addActionListener(this);

		// igralno polje
		polje = new IgralnoPolje();

		GridBagConstraints polje_layout = new GridBagConstraints();
		polje_layout.gridx = 0;
		polje_layout.gridy = 0;
		polje_layout.fill = GridBagConstraints.BOTH;
		polje_layout.weightx = 1.0;
		polje_layout.weighty = 1.0;
		getContentPane().add(polje, polje_layout);
		
		// statusna vrstica za sporočila
		status = new JLabel();
		status.setFont(new Font(status.getFont().getName(),
							    status.getFont().getStyle(),
							    20));
		GridBagConstraints status_layout = new GridBagConstraints();
		status_layout.gridx = 0;
		status_layout.gridy = 1;
		status_layout.anchor = GridBagConstraints.CENTER;
		getContentPane().add(status, status_layout);
		
		status.setText("Izberite igro!");
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == igraClovekRacunalnik) {
			Vodja.tezavnost = dolociTezavnost();
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.CRNI, VrstaIgralca.C); 
			Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.R);
			Vodja.igramoNovoIgro();
		} else if (e.getSource() == igraRacunalnikClovek) {
			Vodja.tezavnost = dolociTezavnost();
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.CRNI, VrstaIgralca.R); 
			Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.C);
			Vodja.igramoNovoIgro();
		} else if (e.getSource() == igraClovekClovek) {
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.CRNI, VrstaIgralca.C); 
			Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.C);
			Vodja.igramoNovoIgro();
		} else if (e.getSource() == igraRacunalnikRacunalnik) {
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.CRNI, VrstaIgralca.R); 
			Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.R);
			Vodja.igramoNovoIgro();
		} else if (e.getSource() == shraniIgro) {
			int returnVal = FC.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
			    try {
					File file = FC.getSelectedFile();
					FileOutputStream fileOutputStream = new FileOutputStream(file);
					ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
					Vodja shranjeno = new Vodja();
					objectOutputStream.writeObject(shranjeno);
				    objectOutputStream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else if (e.getSource() == naloziIgro) {
			int returnValn = FC.showOpenDialog(this);
			if (returnValn == JFileChooser.APPROVE_OPTION) {
			    try {
					File file = FC.getSelectedFile();
					FileInputStream fileInputStream = new FileInputStream(file);
					ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream); 
				    Vodja shranjeno = (Vodja) objectInputStream.readObject();
				    objectInputStream.close();
				    Vodja.vrstaIgralca = shranjeno.vrstaIgralca_;
				    Vodja.clovekNaVrsti = shranjeno.clovekNaVrsti_;
				    Vodja.igramoShranjenoIgro(shranjeno.igra_);
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		} else if (e.getSource() == izhod) {
			this.dispose();
		}
	}

	public void osveziGUI() {
		if (Vodja.igra == null) {
			status.setText("Igra ni v teku.");
		}
		else {
			switch(Vodja.igra.stanje()) {
			case V_TEKU: 
				status.setText("Na potezi je " + Vodja.igra.naPotezi() + 
						" - " + Vodja.vrstaIgralca.get(Vodja.igra.naPotezi())); 
				break;
			case ZMAGA_CRNI: 
				status.setText("Zmagal je črni - " + 
						Vodja.vrstaIgralca.get(Igralec.CRNI));
				break;
			case ZMAGA_BELI: 
				status.setText("Zmagal je beli - " + 
						Vodja.vrstaIgralca.get(Igralec.BELI));
				break;
			}
		}
		polje.repaint();
	}
	private int dolociTezavnost () {
		String s = (String)JOptionPane.showInputDialog(
		                    this,
		                    "Izberite težavnost igre, privzeta je Srednja:",
		                    "Težavnost",
		                    JOptionPane.PLAIN_MESSAGE,
		                    null,
		                    Tezavnost.seznam(),
		                    null);
		return Tezavnost.ostevilci(s);
	}
}
