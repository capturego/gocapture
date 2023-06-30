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
import logika.Igralec;
import logika.Polje;
import logika.Stanje;
import logika.Tocka;
import logika.Graf;


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
	 * V grafični kontekst {@g2} nariši žeton barve {@barva} v polje {@(i,j)}
	 * @param g2
	 * @param i
	 * @param j
	 * @param barva
	 */
	private void paintO(Graphics2D g2, int i, int j, Color barva) {
		double w = squareWidth();
		double d = 1.5 * w * (1.0 - LINE_WIDTH - 2.0 * PADDING); // premer O
		double x = w * (i + 0.35 + 0.5 * LINE_WIDTH + PADDING);
		double y = w * (j + 0.35 + 0.5 * LINE_WIDTH + PADDING);
		g2.setColor(barva);
		g2.setStroke(new BasicStroke((float) (w * LINE_WIDTH)));
		g2.fillOval((int)x, (int)y, (int)d , (int)d);
	}
	
	private void obkrozi(Graphics2D g2, int i, int j) {
		double w = squareWidth();
		double d = 1.5 * w * (1.0 - LINE_WIDTH - 2.0 * PADDING); // premer O
		double x = w * (i + 0.35 + 0.5 * LINE_WIDTH + PADDING);
		double y = w * (j + 0.35 + 0.5 * LINE_WIDTH + PADDING);
		g2.setColor(Color.RED);
		g2.setStroke(new BasicStroke((float) (3 * w * LINE_WIDTH)));
		g2.drawOval((int)x, (int)y, (int)d , (int)d);
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;

		double w = squareWidth();

//		if ((Vodja.igra != null) && ((Vodja.igra.stanje == Stanje.ZMAGA_BELI) || (Vodja.igra.stanje == Stanje.ZMAGA_CRNI))) {
//			System.out.println("OK");
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
		
		// žetoni
		Polje[][] plosca;;
		if (Vodja.igra != null) {
			plosca = Vodja.igra.getPlosca();
			for (int i = 0; i < Igra.N; i++) {
				for (int j = 0; j < Igra.N; j++) {
					switch(plosca[i][j]) {
					case BELO: paintO(g2, i, j, Color.WHITE); break;
					case CRNO: paintO(g2, i, j, Color.BLACK); break;
					default: break;
					}
				}
			}
			for (String imeGrafa : Vodja.igra.grafi0libs) {
				Graf ujetGraf = Vodja.igra.GRAFI.get(imeGrafa);
//				if (ujetGraf.lastnik == Vodja.igra.naPotezi) {
				if ((ujetGraf.lastnik == Igralec.CRNI && Vodja.igra.stanje() == Stanje.ZMAGA_BELI) ||
					(ujetGraf.lastnik == Igralec.BELI && Vodja.igra.stanje() == Stanje.ZMAGA_CRNI)) {
					for (Tocka tocka : ujetGraf.tocke) {
						obkrozi(g2, tocka.x, tocka.y);
					}
				}
			}
		}	
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (Vodja.clovekNaVrsti) {
			int w = (int)(squareWidth());
			int x = e.getX();
			int y = e.getY();
			int i = (int) (x - .5*w) / w ;
			int j = (int) (y - .5*w) / w ;
			if (0 <= i && i < Igra.N &&
				0 <= j && j < Igra.N) {
				Vodja.igrajClovekovoPotezo (new Tocka(i, j));
			}
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
