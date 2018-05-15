package gui;

//Strateg bo abstrakten razred, saj bosta na njem temeljila razred Clovek in razred Racunalnik

public abstract class Strateg {
	
	public abstract void na_potezi();
	
	public abstract void klik(int x, int y);
	
	public abstract void prekini();

}
