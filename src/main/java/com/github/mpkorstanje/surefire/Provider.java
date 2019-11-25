package com.github.mpkorstanje.surefire;

import java.util.Set;

public interface Provider {
    Set<String> discoverTests(SurefireTestDiscoveryRequest surefireRequest);

    void execute(String test);
}
