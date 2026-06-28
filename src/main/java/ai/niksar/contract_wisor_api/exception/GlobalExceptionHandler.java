package ai.niksar.contract_wisor_api.exception;

import com.healthmarketscience.jackcess.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ai.niksar.contract_wisor_api.dto.ResponseDTO;
import ai.niksar.contract_wisor_api.workflow.DocumentParseWorkflowImpl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ResponseEntity<Map<String, String>> buildErrorResponse(String message, HttpStatus status,String code) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", message);
        errorResponse.put("code",code);
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(ContractWisorException.E001.class)
    public ResponseEntity<Map<String, String>> handleE001Exception(ContractWisorException.E001 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND,"E-001");
    }

    @ExceptionHandler(ContractWisorException.E002.class)
    public ResponseEntity<Map<String, String>> handleE002Exception(ContractWisorException.E002 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND,"E-002");
    }

    @ExceptionHandler(ContractWisorException.E003.class)
    public ResponseEntity<Map<String, String>> handleE003Exception(ContractWisorException.E003 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND,"E-003");
    }

    @ExceptionHandler(ContractWisorException.E004.class)
    public ResponseEntity<Map<String, String>> handleE004Exception(ContractWisorException.E004 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND,"E-004");
    }

    @ExceptionHandler(ContractWisorException.E005.class)
    public ResponseEntity<Map<String, String>> handleE005Exception(ContractWisorException.E005 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND,"E-005");
    }

    @ExceptionHandler(ContractWisorException.E006.class)
    public ResponseEntity<Map<String, String>> handleE006Exception(ContractWisorException.E006 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST,"E-006");
    }

    @ExceptionHandler(ContractWisorException.E007.class)
    public ResponseEntity<Map<String, String>> handleE007Exception(ContractWisorException.E007 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST,"E-007");
    }

    @ExceptionHandler(ContractWisorException.E008.class)
    public ResponseEntity<Map<String, String>> handleE008Exception(ContractWisorException.E008 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST,"E-008");
    }

    @ExceptionHandler(ContractWisorException.E009.class)
    public ResponseEntity<Map<String, String>> handleE009Exception(ContractWisorException.E009 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST,"E-009");
    }

    @ExceptionHandler(ContractWisorException.E010.class)
    public ResponseEntity<Map<String, String>> handleE010Exception(ContractWisorException.E010 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST,"E-010");
    }

    @ExceptionHandler(ContractWisorException.E011.class)
    public ResponseEntity<Map<String, String>> handleE011Exception(ContractWisorException.E011 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN,"E-011");
    }


    @ExceptionHandler(ContractWisorException.E012.class)
    public ResponseEntity<Map<String, String>> handleE012Exception(ContractWisorException.E012 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST,"E-012");
    }

    @ExceptionHandler(ContractWisorException.E013.class)
    public ResponseEntity<Map<String, String>> handleE013Exception(ContractWisorException.E013 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT,"E-013");
    }

    @ExceptionHandler(ContractWisorException.E014.class)
    public ResponseEntity<Map<String, String>> handleE014Exception(ContractWisorException.E014 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT,"E-014");
    }

    @ExceptionHandler(ContractWisorException.E015.class)
    public ResponseEntity<Map<String, String>> handleE015Exception(ContractWisorException.E015 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND,"E-015");
    }

    @ExceptionHandler(ContractWisorException.E016.class)
    public ResponseEntity<Map<String, String>> handleE016Exception(ContractWisorException.E016 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse( ex.getMessage(), HttpStatus.BAD_REQUEST,"E-016");
    }

    @ExceptionHandler(ContractWisorException.E017.class)
    public ResponseEntity<Map<String, String>> handleE017Exception(ContractWisorException.E017 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST,"E-017");
    }

    @ExceptionHandler(ContractWisorException.E018.class)
    public ResponseEntity<Map<String, String>> handleE018Exception(ContractWisorException.E018 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST,"E-018");
    }

    @ExceptionHandler(ContractWisorException.E019.class)
    public ResponseEntity<Map<String, String>> handleE019Exception(ContractWisorException.E019 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST,"E-019");
    }

    @ExceptionHandler(ContractWisorException.E020.class)
    public ResponseEntity<Map<String, String>> handleE020Exception(ContractWisorException.E020 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse( ex.getMessage(), HttpStatus.BAD_REQUEST,"E-020");
    }

    @ExceptionHandler(ContractWisorException.E021.class)
    public ResponseEntity<Map<String, String>> handleE021Exception(ContractWisorException.E021 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse( ex.getMessage(), HttpStatus.BAD_REQUEST,"E-021");
    }

    @ExceptionHandler(ContractWisorException.E022.class)
    public ResponseEntity<Map<String, String>> handleE022Exception(ContractWisorException.E022 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse( ex.getMessage(), HttpStatus.BAD_REQUEST,"E-022");
    }

    @ExceptionHandler(ContractWisorException.E023.class)
    public ResponseEntity<Map<String, String>> handleE023Exception(ContractWisorException.E023 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse( ex.getMessage(), HttpStatus.BAD_REQUEST,"E-023");
    }

    @ExceptionHandler(ContractWisorException.E024.class)
    public ResponseEntity<Map<String, String>> handleE024Exception(ContractWisorException.E024 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse( ex.getMessage(), HttpStatus.BAD_REQUEST,"E-024");
    }

    @ExceptionHandler(ContractWisorException.E025.class)
    public ResponseEntity<Map<String, String>> handleE025Exception(ContractWisorException.E025 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse( ex.getMessage(), HttpStatus.BAD_REQUEST,"E-025");
    }

    @ExceptionHandler(ContractWisorException.E026.class)
    public ResponseEntity<Map<String, String>> handleE026Exception(ContractWisorException.E026 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse( ex.getMessage(), HttpStatus.BAD_REQUEST,"E-026");
    }

    @ExceptionHandler(ContractWisorException.E027.class)
    public ResponseEntity<Map<String, String>> handleE027Exception(ContractWisorException.E027 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse( ex.getMessage(), HttpStatus.BAD_REQUEST,"E-027");
    }

    @ExceptionHandler(ContractWisorException.E028.class)
    public ResponseEntity<Map<String, String>> handleE028Exception(ContractWisorException.E028 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse( ex.getMessage(), HttpStatus.FORBIDDEN,"E-028");
    }

    @ExceptionHandler(ContractWisorException.E029.class)
    public ResponseEntity<Map<String, String>> handleE029Exception(ContractWisorException.E029 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse( ex.getMessage(), HttpStatus.BAD_REQUEST,"E-029");
    }

    @ExceptionHandler(ContractWisorException.E030.class)
    public ResponseEntity<Map<String, String>> handleE030Exception(ContractWisorException.E030 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse( ex.getMessage(), HttpStatus.NOT_FOUND,"E-030");
    }

    @ExceptionHandler(ContractWisorException.E031.class)
    public ResponseEntity<Map<String, String>> handleE031Exception(ContractWisorException.E031 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse( ex.getMessage(), HttpStatus.BAD_REQUEST,"E-031");
    }

    @ExceptionHandler(ContractWisorException.E032.class)
    public ResponseEntity<Map<String, String>> handleE032Exception(ContractWisorException.E032 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse( ex.getMessage(), HttpStatus.BAD_REQUEST,"E-032");
    }

    @ExceptionHandler(ContractWisorException.E033.class)
    public ResponseEntity<Map<String, String>> handleE033Exception(ContractWisorException.E033 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse( ex.getMessage(), HttpStatus.BAD_REQUEST,"E-033");
    }

    @ExceptionHandler(ContractWisorException.E034.class)
    public ResponseEntity<Map<String, String>> handleE033Exception(ContractWisorException.E034 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse( ex.getMessage(), HttpStatus.BAD_REQUEST,"E-034");
    }

    @ExceptionHandler(ContractWisorException.E035.class)
    public ResponseEntity<Map<String, String>> handleE014Exception(ContractWisorException.E035 ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT,"E-035");
    }

    @ExceptionHandler(ContractWisorException.InvalidRequestField.class)
    public ResponseEntity<Map<String, String>> handleInvalidRequestField(ContractWisorException.InvalidRequestField ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse("Invalid field: " + ex.getMessage(), HttpStatus.BAD_REQUEST,"GE-001");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST,"GE-002");
    }

    @ExceptionHandler(ContractWisorException.CustomException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(ContractWisorException.CustomException ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST,"GE-003");
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Map<String, String>> handleNullPointerException(NullPointerException ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse("An object was used as null: " + ex.getMessage(), HttpStatus.BAD_REQUEST,"GE-004");
    }

    // Dedicated handling for ArrayIndexOutOfBoundsException
    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    public ResponseEntity<Map<String, String>> handleArrayIndexOutOfBoundsException(ArrayIndexOutOfBoundsException ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse("Access was made outside the array bounds : "+ex.getMessage(),HttpStatus.BAD_REQUEST,"GE-005");
    }

    // Dedicated handling for IndexOutOfBoundsException
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public ResponseEntity<Map<String, String>> handleIndexOutOfBoundsException(IndexOutOfBoundsException ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse("Access was made outside the list bounds : " + ex.getMessage(), HttpStatus.BAD_REQUEST,"GE-006");
    }

    // Dedicated handling for NoSuchElementException
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>>  handleNoSuchElementException(NoSuchElementException ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse("The requested item was not found.", HttpStatus.NOT_FOUND,"GE-007");
    }

    // Dedicated handling for IOException
    @ExceptionHandler(IOException.class)
    public ResponseEntity<Map<String, String>> handleIOException(IOException ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse("An input/output error occurred : "+ex.getMessage(), HttpStatus.BAD_REQUEST,"GE-008");
    }

    // Dedicated handling for SQLException
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Map<String, String>> handleSQLException(SQLException ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse("A database error occurred : "+ex.getMessage(), HttpStatus.BAD_REQUEST,"GE-009");

    }

    // Dedicated handling for MalformedURLException
    @ExceptionHandler(MalformedURLException.class)
    public ResponseEntity<Map<String, String>> handleMalformedURLException(MalformedURLException ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse("Invalid URL format.", HttpStatus.BAD_REQUEST,"GE-010");

    }

    // Dedicated handling for ConstraintViolationException
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse("Data constraint violation: " + ex.getMessage(), HttpStatus.BAD_REQUEST,"GE-011");

    }

    // Dedicated handling for ArithmeticException
    @ExceptionHandler(ArithmeticException.class)
    public ResponseEntity<Map<String, String>> handleArithmeticException(ArithmeticException ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse("Mathematical operation error: " + ex.getMessage(), HttpStatus.BAD_REQUEST,"GE-012");

    }
    // Dedicated handling for IllegalStateException
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>>  handleIllegalStateException(IllegalStateException ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse("Invalid state: " + ex.getMessage(), HttpStatus.BAD_REQUEST,"GE-013");

    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>>  handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse("Unique constraint violation: " + ex.getMessage(), HttpStatus.CONFLICT,"GE-014");
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Map<String, String>>  handleDuplicateKeyException(DuplicateKeyException ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse("Unique key violation: " + ex.getMessage(), HttpStatus.CONFLICT,"GE-015");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse("Invalid parameter: " + ex.getParameter().getParameterName(), HttpStatus.BAD_REQUEST,"GE-016");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse("The required request body is missing. Please make sure you send all required data. " + ex.getMessage(), HttpStatus.BAD_REQUEST,"GE-017");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, String>>handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        logger.error(ex.getMessage(), ex);
        String parameterName = ex.getParameterName();
        return buildErrorResponse("The required request parameter is missing: " + parameterName + ". Please make sure you send this parameter.", HttpStatus.BAD_REQUEST,"GE-018");
    }

    @ExceptionHandler(StringIndexOutOfBoundsException.class)
    public ResponseEntity<Map<String, String>> handleStringIndexOutOfBoundsException(StringIndexOutOfBoundsException ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse("An invalid index was accessed during string operations: " + ex.getMessage(), HttpStatus.BAD_REQUEST,"GE-019");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoHandlerFoundException(NoResourceFoundException ex) {
        logger.error(ex.getMessage(), ex);
        return buildErrorResponse("No API was found for this URL. Please enter a valid URL. " + ex.getMessage(), HttpStatus.NOT_FOUND,"GE-020");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, String>> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex) {
        logger.error(ex.getMessage(), ex);
        String message = String.format(
                "Error: Request method '%s' is not supported. Supported methods: %s",
                ex.getMethod(),
                ex.getSupportedHttpMethods()
        );
        return buildErrorResponse(message, HttpStatus.METHOD_NOT_ALLOWED,"GE-021");
    }

    // General Exception handling (for all other exceptions)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception ex) {
        logger.error("Unhandled exception caught by global handler [{}]: {}",
                ex.getClass().getName(), ex.getMessage(), ex);
        return buildErrorResponse("General System Error", HttpStatus.BAD_REQUEST,"GE-022");
    }
}
