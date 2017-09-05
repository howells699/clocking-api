package ClockingAPI;

import ClockingAPI.resources.ClockingAPIResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ClockingAPIApplication extends Application<ClockingAPIConfiguration> {

    public static void main(final String[] args) throws Exception {
        new ClockingAPIApplication().run(args);
    }

    @Override
    public String getName() {
        return "ClockingAPI";
    }

    @Override
    public void initialize(final Bootstrap<ClockingAPIConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final ClockingAPIConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application

        environment.jersey().register(new ClockingAPIResource());
    }

}
