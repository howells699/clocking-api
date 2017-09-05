package ClockingAPI.resources;

import ClockingAPI.api.EventStoreReader;
import ClockingAPI.api.EventStoreWriter;
import ClockingAPI.exceptions.APIException;
import ClockingAPI.models.TimeInput;
import akka.io.SelectionHandler;
import org.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/")
public class ClockingAPIResource {

    private final String hostname = "127.0.0.1";
    private final int port = 2113;
    private final String login = "admin";
    private final String password = "changeit";
    private final String eventType = "clocking-event";
    private final String streamId = "time-keeping-stream";

    private String timeRegex = "^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$";
    private String dateRegex = "^(?:(?:(?:(?:31\\/(?:0?[13578]|1[02]))|(?:(?:29|30)\\/(?:0?[13-9]|1[0-2])))\\/(?:1[6-9]|[2-9]\\d)\\d{2})|(?:29\\/0?2\\/(?:(?:(1[6-9]|[2-9]\\d)(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))|(?:0?[1-9]|1\\d|2[0-8])\\/(?:(?:0?[1-9])|(?:1[0-2]))\\/(?:(?:1[6-9]|[2-9]\\d)\\d{2}))$";

    @POST
    @Path("store-time")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})

    public Response storeTime(String clockingInformationAsJson) throws IOException,APIException {

        try {
            JSONObject obj = new JSONObject(clockingInformationAsJson);
            if(obj.getString("time").matches(timeRegex)
                    && obj.getString("date").matches(dateRegex)
                    && !obj.getString("userId").isEmpty()
                    && (obj.getString("timeInput").equals("IN") || obj.getString("timeInput").equals("OUT"))){

                        EventStoreReader esv = new EventStoreReader(hostname, port, login, password, streamId);
                        String userId = esv.readEvent(obj.getString("userId"));

                        if (!userId.equals(null)) {
                            EventStoreWriter esr = new EventStoreWriter(hostname, port, login, password, streamId);
                            esr.write(obj.toString(),"This is the description");
                            return Response.ok("").status(200)
                                    .header("Content-Type", "application/json")
                                    .build();
                        } else {
                            throw new APIException(400,"Invalid UserId");
                        }
                    }else{
                        throw new APIException(400, "Bad JSON Data");
                    }
        }catch(Exception ex){
            System.out.print(ex.getMessage());
            throw new APIException(400, "Incorrect JSON format");
        }catch (APIException exception) {
            throw new APIException(exception.getCode(), exception.getMessage());
        }
    }

    @POST
    @Path("get-time")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})

    public Response getTime(String userIdAsJson) throws IOException,APIException {

        return Response.ok().status(200)
                .header("Content-Type", "application/json").build();

    }

}
