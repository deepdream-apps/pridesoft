package cm.antic.pridesoft.datamodel.exceptions;

public class LocalEntityNotValidException extends RuntimeException{
	
	public LocalEntityNotValidException() {
		
	}
	
	
	public LocalEntityNotValidException(String message) {
		super(message) ;
	}
	
	
	public LocalEntityNotValidException(String message, Throwable t) {
		super(message, t) ;
	}

}
