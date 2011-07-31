package org.neurus.evolution;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.neurus.testing.MoreAsserts.assertStringContains;

import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.neurus.fitness.Fitness;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
public class LoggingEvolutionListenerTest {

  private Logger logger;
  private ArgumentCaptor<String> loggingCaptor;

  @Before
  public void setUp() {
    logger = mock(Logger.class);
    loggingCaptor = ArgumentCaptor.forClass(String.class);
    Whitebox.setInternalState(LoggingEvolutionListener.class, "logger", logger);
  }

  @Test
  public void testLoggingEvolutionListener() {
    LoggingEvolutionListener listener = new LoggingEvolutionListener();
    Population population = TestPopulations.populationWithFitnesses(0.25, 1.0, 1.25);
    EvolutionSnapshot es = new EvolutionSnapshot(45, population, population.get(0), null);
    listener.onNewGeneration(es);
    verify(logger).info(loggingCaptor.capture());
    assertStringContains("Generation: 45", loggingCaptor.getValue());
    assertStringContains("Average fitness: 0.833333", loggingCaptor.getValue());
    assertStringContains("Best Training: 0.250000", loggingCaptor.getValue());
  }

  @Test
  public void testLoggingWithValidationFitness() {
    LoggingEvolutionListener listener = new LoggingEvolutionListener();
    Population population = TestPopulations.populationWithFitnesses(0.25, 1.0, 1.25);
    population.get(1).setValidationFitness(new Fitness(0.8345888888d));
    EvolutionSnapshot es = new EvolutionSnapshot(45, population, population.get(0), population.get(1));
    listener.onNewGeneration(es);
    verify(logger).info(loggingCaptor.capture());
    assertStringContains("Generation: 45", loggingCaptor.getValue());
    assertStringContains("Average fitness: 0.833333", loggingCaptor.getValue());
    assertStringContains("Best Training: 0.250000", loggingCaptor.getValue());
    assertStringContains("Best Validation: 0.834589", loggingCaptor.getValue());
  }
}
