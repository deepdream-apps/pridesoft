package cm.antic.pridesoft.localsrv.exceptions;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import cm.antic.pridesoft.datamodel.exceptions.LocalEntityNotFoundException;

@ControllerAdvice
public class ExceptionsHandler {
	
	
	@ExceptionHandler(IOException.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public void handleIOException() {}
	
	
	@ExceptionHandler(FileNotFoundException.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public void handleFileNotFoundException() {}
	
	
	@ExceptionHandler(LocalEntityNotFoundException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public void handeEntityNotFoundException() {
		
	}
	
	

}
