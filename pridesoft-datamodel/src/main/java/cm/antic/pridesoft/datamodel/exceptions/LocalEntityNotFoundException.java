package cm.antic.pridesoft.datamodel.exceptions;

public class LocalEntityNotFoundException extends RuntimeException{
	
	public LocalEntityNotFoundException() {
		
	}
	
	
	public LocalEntityNotFoundException(String message) {
		super(message) ;
	}
	
	
	public LocalEntityNotFoundException(String message, Throwable t) {
		super(message, t) ;
	}

}
