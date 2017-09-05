package ClockingAPI.api;

import akka.actor.*;
import eventstore.*;
import eventstore.j.EventDataBuilder;
import eventstore.j.SettingsBuilder;
import eventstore.j.WriteEventsBuilder;
import eventstore.tcp.ConnectionActor;

import java.net.InetSocketAddress;
import java.util.UUID;

public class EventStoreWriter {

    private String hostname = "127.0.0.1";
    private int port = 2113;
    private String login = "admin";
    private String password = "changeit";
    private String streamId = "time-keeping-stream";

    public EventStoreWriter(String hostname, int port, String login, String password, String streamId) {
        this.hostname = hostname;
        this.port = port;
        this.login = login;
        this.password = password;
        this.streamId = streamId;
    }

    public boolean write(String dataToWrite, String description) {
        final ActorSystem system = ActorSystem.create();
        final Settings settings = new SettingsBuilder()
                .address(new InetSocketAddress(hostname, port))
                .defaultCredentials(login, password)
                .build();
        final ActorRef connection = system.actorOf(ConnectionActor.getProps(settings));
        final ActorRef writeResult = system.actorOf(Props.create(WriteResult.class));


        final EventData event = new EventDataBuilder("clocking-event")
                .eventId(UUID.randomUUID())
                .data(dataToWrite)
                .metadata(description)
                .build();

        final WriteEvents writeEvents = new WriteEventsBuilder(streamId)
                .addEvent(event)
                .expectAnyVersion()
                .build();

        try {
            connection.tell(writeEvents, writeResult);
            return true;
        }catch (Exception ex){
            return false;
        }
    }
}
