package zagon;

import java.io.IOException;

import gui.GlavnoOkno;

/**
 * @author Gasper
 * Metoda main zazene igro Reversija.
 */
public class Reversi {

	public static void main(String[] args) throws IOException {
		GlavnoOkno okno = new GlavnoOkno();
		okno.pack();
		okno.setLocationRelativeTo(null);
		okno.setVisible(true);
	}
}
