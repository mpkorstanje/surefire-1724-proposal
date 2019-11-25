package com.github.mpkorstanje.surefire;

import com.github.mpkorstanje.tests.Junit4Test;
import com.github.mpkorstanje.tests.Junit5Test;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.of;

public class Main {

    private static List<Provider> providers = asList(
            new JUnit4Provider(),
            new JUnitPlatformProvider()
    );

    public static void main(String... args) {

        // Construct discovery request
        SurefireTestDiscoveryRequest surefireTestDiscoveryRequest = new SurefireTestDiscoveryRequest(
                surefireTestDiscovery(),
                classPathRootDiscoveryFromConfiguration()
        );

        // Discover tests
        Map<Provider, Set<String>> testsPerProvider = providers.stream()
                .collect(toMap(identity(), discoverTestsInFork(surefireTestDiscoveryRequest)));

        // Distribute tests for execution
        testsPerProvider.forEach((provider, tests) ->
                tests.forEach(test -> executeTestInFork(provider, test)));
    }

    private static Set<Path> classPathRootDiscoveryFromConfiguration() {
        return Collections.singleton(new File("target/classes").getAbsoluteFile().toPath());
    }

    private static Function<Provider, Set<String>> discoverTestsInFork(SurefireTestDiscoveryRequest surefireTestDiscoveryRequest) {
        // DiscoverTests may load classes. Should happen in a separate fork
        return provider -> provider.discoverTests(surefireTestDiscoveryRequest);
    }

    private static void executeTestInFork(Provider provider, String test) {
        // Would normally start a fork for each test or a set of tests.
        provider.execute(test);
    }

    private static Set<String> surefireTestDiscovery() {
        // Would normally scan test classes directory for tests.
        return of(Junit4Test.class, Junit5Test.class)
                .map(Class::getName)
                .collect(Collectors.toSet());
    }
}
