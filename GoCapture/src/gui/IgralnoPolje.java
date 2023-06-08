package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import vodja.Vodja;
import logika.Igra;
import logika.Polje;
import logika.Stanje;
import logika.Graf;
import splosno.Poteza;

/**
 * Pravokotno območje, v katerem je narisano igralno polje.
 */
@SuppressWarnings("serial")
public class IgralnoPolje extends JPanel implements MouseListener {
	
	public IgralnoPolje() {
		setBackground(new Color(51,128,255));
		this.addMouseListener(this);
		
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(400, 400);
	}

	
	// Relativna širina črte
	private final static double LINE_WIDTH = 0.02;
	
	// Širina enega kvadratka
	private double squareWidth() {
		return Math.min(getWidth(), getHeight()) / (Igra.N+1);
	}
	
	// Relativni prostor okoli X in O
	private final static double PADDING = 0.18;
	
	/**
	 * V grafični kontekst g2 nariši križec v polje (i,j)
	 * 
	 * @param g2
	 * @param i
	 * @param j
	 */
//	private void paintX(Graphics2D g2, int i, int j) {
//		double w = squareWidth();
//		double d = w * (1.0 - LINE_WIDTH - 2.0 * PADDING); // premer O
//		double x = w * (i + 0.5 + 0.5 * LINE_WIDTH + PADDING);
//		double y = w * (j + 0.5 + 0.5 * LINE_WIDTH + PADDING);
//		g2.setColor(Color.WHITE);
//		g2.setStroke(new BasicStroke((float) (w * LINE_WIDTH)));
//		g2.fillOval((int)x, (int)y, (int)d , (int)d);
//	}
	
	/**
	 * V grafični kontekst {@g2} nariši križec v polje {@(i,j)}
	 * @param g2
	 * @param i
	 * @param j
	 */
	private void paintO(Graphics2D g2, int i, int j, Color barva) {
		double w = squareWidth();
		double d = 1.5 * w * (1.0 - LINE_WIDTH - 2.0 * PADDING); // premer O
		double x = w * (i + 0.5 + 0.5 * LINE_WIDTH + PADDING);
		double y = w * (j + 0.5 + 0.5 * LINE_WIDTH + PADDING);
//		g2.setColor(Color.BLACK);
		g2.setColor(barva);
		g2.setStroke(new BasicStroke((float) (w * LINE_WIDTH)));
		g2.fillOval((int)x, (int)y, (int)d , (int)d);
	}
	
	private void obkrozi(Graphics2D g2, int i, int j) {
		double w = squareWidth();
		double d = 1.5 * w * (1.0 - LINE_WIDTH - 2.0 * PADDING); // premer O
		double x = w * (i + 0.5 + 0.5 * LINE_WIDTH + PADDING);
		double y = w * (j + 0.5 + 0.5 * LINE_WIDTH + PADDING);
		g2.setColor(Color.RED);
		g2.setStroke(new BasicStroke((float) (3 * w * LINE_WIDTH)));
		g2.drawOval((int)x, (int)y, (int)d , (int)d);
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;

		double w = squareWidth();

		if ((Vodja.igra != null) && ((Vodja.igra.stanje_ == Stanje.ZMAGA_BELI) || (Vodja.igra.stanje_ == Stanje.ZMAGA_CRNI))) {
			System.out.println("OK");
		}
		// če imamo zmagovalno terico, njeno ozadje pobarvamo
//		Vrsta t = null;
//		if (Vodja.igra != null) {t = Vodja.igra.zmagovalnaVrsta();}
//		if (t != null) {
//			g2.setColor(new Color(255, 255, 196));
//			for (int k = 0; k < Igra.N; k++) {
//				int i = t.x[k];
//				int j = t.y[k];
//				g2.fillRect((int)(w * i), (int)(w * j), (int)w, (int)w);
//			}
//		}
		
		// črte
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke((float) (w * LINE_WIDTH)));
		for (int i = 1; i < Igra.N+1; i++) {
			g2.drawLine((int)(i * w),
					    (int)(w),
					    (int)(i * w),
					    (int)((Igra.N) * w));
			g2.drawLine((int)(w),//
					    (int)(i * w),
					    (int)((Igra.N) * w),
					    (int)(i * w));
		}
		
		// križci in krožci
		Polje[][] plosca;;
		if (Vodja.igra != null) {
			plosca = Vodja.igra.getPlosca();
			for (int i = 0; i < Igra.N; i++) {
				for (int j = 0; j < Igra.N; j++) {
					switch(plosca[i][j]) {
//					case BELO: paintX(g2, i, j); break;
					case BELO: paintO(g2, i, j, Color.WHITE); break;
					case CRNO: paintO(g2, i, j, Color.BLACK); break;
					default: break;
					}
				}
			}
			for (String imeGrafa : Vodja.igra.grafi0libs) {
				Graf ujetGraf = Vodja.igra.GRAFI.get(imeGrafa);
				if (ujetGraf.lastnik == Vodja.igra.naPotezi) {
					for (Poteza tocka : ujetGraf.tocke) {
						obkrozi(g2, tocka.x(), tocka.y());
					}
				}
			}
		}	
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (Vodja.clovekNaVrsti) {
//			System.out.println("na vrsti: " + Vodja.clovekNaVrsti + ", njegovi libs");
			int w = (int)(squareWidth());
			int x = e.getX();
			int y = e.getY();
			int i = (int) (x - .25*w) / w ;
			int j = (int) (y - .25*w) / w ;
//			System.out.println("Kliknil: " + i + "-"+j);
			if (0 <= i && i < Igra.N &&
				0 <= j && j < Igra.N) {
				Vodja.igrajClovekovoPotezo (new Poteza(i, j));
//				Vodja.igrajClovekovoPotezo (new Koordinati(i, j));
			}
//			for (Liberty lib : Igra.LIBS.values()) {
//				System.out.println(lib);
//			}
//			for (String lib : Igra.LIBS.keySet()) {
//				System.out.println(Igra.LIBS.get(lib));
//			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {		
	}

	@Override
	public void mouseReleased(MouseEvent e) {		
	}

	@Override
	public void mouseEntered(MouseEvent e) {		
	}

	@Override
	public void mouseExited(MouseEvent e) {		
	}
	
}
