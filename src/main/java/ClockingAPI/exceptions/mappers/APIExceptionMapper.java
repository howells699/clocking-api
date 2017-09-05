package ClockingAPI.exceptions.mappers;

import ClockingAPI.exceptions.APIException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class APIExceptionMapper implements ExceptionMapper<APIException> {

    public Response toResponse(APIException exception) {
        return Response.status(exception.getCode())
                .entity(exception.getMessage())
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}
