package net.catenax.semantics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import net.catenax.semantics.hub.persistence.PersistenceLayer;

@Component
public class TriplestoreLivenessProbe implements HealthIndicator {
    @Autowired
    PersistenceLayer pl;

    @Override
    public Health health() {
        try {
            if(pl.echo()) {
                return Health.up().build();
            }
            return Health.down().build();
        } catch (Exception e) {
            e.printStackTrace();
            return Health.down().build();
        }
    }
}
