package com.github.mpkorstanje.surefire;


import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.LoggingListener;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toList;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClasspathRoots;

public class JUnitPlatformProvider implements Provider {

    private final Launcher launcher = LauncherFactory.create();
    private final Set<Path> classPathRootDiscoverySelectorFromConfiguration;

    JUnitPlatformProvider(Set<Path> classPathRootDiscoverySelectorFromConfiguration,
                          Object... otherDiscoverySelectorsFromConfiguration) {
        this.classPathRootDiscoverySelectorFromConfiguration = classPathRootDiscoverySelectorFromConfiguration;
    }

    @Override
    public Set<String> discoverTests(Set<String> testsDiscoveredBySurefire) {
        System.out.println("JUnit Platform launching test engines to discover tests");

        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(testsDiscoveredBySurefire.stream()
                        .map(DiscoverySelectors::selectClass)
                        .collect(toList()))
                .selectors(selectClasspathRoots(classPathRootDiscoverySelectorFromConfiguration))
                .build();

        TestPlan testPlan = launcher.discover(request);
        return testPlan.getRoots().stream()
                .map(testIdentifier -> extractTestIds(testPlan, testIdentifier))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private Set<String> extractTestIds(TestPlan testPlan, TestIdentifier testIdentifier) {
        if (testIdentifier.isTest()) {
            return singleton(testIdentifier.getUniqueId());
        }
        return testPlan.getDescendants(testIdentifier).stream()
                .filter(TestIdentifier::isTest)
                .map(TestIdentifier::getUniqueId)
                .collect(Collectors.toSet());
    }

    @Override
    public void execute(String test) {
        UniqueId uniqueId = UniqueId.parse(test);

        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(DiscoverySelectors.selectUniqueId(uniqueId))
                .build();

        TestPlan testPlan = launcher.discover(request);

        System.out.println("JUnit Platform executing " + test);
        launcher.execute(testPlan, LoggingListener.forJavaUtilLogging());
        System.out.println("JUnit Platform executed " + test);
    }
}
