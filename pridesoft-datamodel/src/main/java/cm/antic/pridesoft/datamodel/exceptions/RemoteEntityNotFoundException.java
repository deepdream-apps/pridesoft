package cm.antic.pridesoft.datamodel.exceptions;

public class RemoteEntityNotFoundException extends RuntimeException{
	
	public RemoteEntityNotFoundException() {
		
	}
	
	
	public RemoteEntityNotFoundException(String message) {
		super(message) ;
	}
	
	
	public RemoteEntityNotFoundException(String message, Throwable t) {
		super(message, t) ;
	}

}
