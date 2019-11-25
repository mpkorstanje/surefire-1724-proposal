package com.github.mpkorstanje.surefire;

import java.util.Set;

public interface Provider {
    Set<String> discoverTests(Set<String> testsDiscoveredBySurefire);

    void execute(String test);
}
