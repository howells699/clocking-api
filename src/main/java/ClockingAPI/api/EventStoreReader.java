package ClockingAPI.api;

import akka.actor.*;
import eventstore.*;
import eventstore.j.*;
import eventstore.tcp.ConnectionActor;

import java.net.InetSocketAddress;

public class EventStoreReader {

    private String hostname = "127.0.0.1";
    private int port = 2113;
    private String login = "admin";
    private String password = "changeit";
    private String streamId = "time-keeping-stream";

    public EventStoreReader(String hostname, int port, String login, String password, String streamId) {
        this.hostname = hostname;
        this.port = port;
        this.login = login;
        this.password = password;
        this.streamId = streamId;
    }

    public String readEvent(String userId) {
        final ActorSystem system = ActorSystem.create();
        final Settings settings = new SettingsBuilder()
                .address(new InetSocketAddress(hostname, port))
                .defaultCredentials(login, password)
                .build();
        final ActorRef connection = system.actorOf(ConnectionActor.getProps(settings));
        final ActorRef readResult = system.actorOf(Props.create(ReadResult.class));

        final ReadEvent readEvent = new ReadEventBuilder(streamId)
                .first()
                .resolveLinkTos(false)
                .requireMaster(true)
                .build();

        try {
            connection.tell(readEvent, readResult);
            return readResult.toString();
        }catch (Exception ex){
            return null;
        }

    }
}
