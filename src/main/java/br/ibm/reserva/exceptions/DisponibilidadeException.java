package br.ibm.reserva.exceptions;

public class DisponibilidadeException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; //Só para tirar Warning

	public DisponibilidadeException(String message) {
		super(message);
	}
}
