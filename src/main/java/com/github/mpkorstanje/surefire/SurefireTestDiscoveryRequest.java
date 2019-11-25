package com.github.mpkorstanje.surefire;

import java.nio.file.Path;
import java.util.Set;

class SurefireTestDiscoveryRequest {

    final Set<String> testsDiscoveredBySurefire;
    final Set<Path> classPathRoots;

    SurefireTestDiscoveryRequest(Set<String> testsDiscoveredBySurefire, Set<Path> classPathRoots) {
        this.testsDiscoveredBySurefire = testsDiscoveredBySurefire;
        this.classPathRoots = classPathRoots;
    }
}
