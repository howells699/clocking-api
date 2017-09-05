package ClockingAPI.api;

import akka.actor.*;
import akka.actor.Status.Failure;
import akka.event.*;
import eventstore.*;


public  class ReadResult extends UntypedActor {
    final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public void onReceive(Object message) throws Exception {
        if (message instanceof ReadEventCompleted) {
            final ReadEventCompleted completed = (ReadEventCompleted) message;
            final Event event = completed.event();
            log.info("event: {}", event);
        } else if (message instanceof Status.Failure) {
            final Failure failure = ((Failure) message);
            final EsException exception = (EsException) failure.cause();
            log.error(exception, exception.toString());
        } else
            unhandled(message);
        context().system().terminate();
    }

}
