package org.neurus.evolution;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.neurus.testing.MoreAsserts.assertStringPresent;

import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
public class LoggingEvolutionListenerTest {

  private Logger logger;
  private ArgumentCaptor<String> loggingCaptor;
  private EvolutionState evolutionState;

  @Before
  public void setUp() {
    logger = mock(Logger.class);
    loggingCaptor = ArgumentCaptor.forClass(String.class);
    Whitebox.setInternalState(LoggingEvolutionListener.class, "logger", logger);
    evolutionState = mock(EvolutionState.class);
  }

  @Test
  public void testLoggingEvolutionListener() {
    LoggingEvolutionListener listener = new LoggingEvolutionListener();
    Population population = TestPopulations.populationWithFitnesses(0.25, 1.0, 1.25);
    when(evolutionState.getPopulation()).thenReturn(population);
    when(evolutionState.getGeneration()).thenReturn(45);
    listener.onNewGeneration(evolutionState);
    verify(logger).info(loggingCaptor.capture());
    assertStringPresent("Generation: 45", loggingCaptor.getValue());
    assertStringPresent("Average fitness: 0.833333", loggingCaptor.getValue());
    assertStringPresent("Best: 0.250000", loggingCaptor.getValue());
  }
}
