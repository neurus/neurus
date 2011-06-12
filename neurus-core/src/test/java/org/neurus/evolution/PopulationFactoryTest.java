package org.neurus.evolution;

import static junit.framework.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.neurus.instruction.Program;

import com.google.common.collect.Lists;

public class PopulationFactoryTest {

  private List<Individual> createdIndividuals = Lists.newArrayList();

  @Test
  public void testInitializeCreatesRandomPupulation() {
    IndividualInitializer initializer = mockInitializer();
    PopulationFactory factory = new PopulationFactory(initializer);
    Population population = factory.initialize(50);
    assertEquals(50, population.size());
    assertIndividuals(population);
  }

  private void assertIndividuals(Population population) {
    for (int x = 0; x < population.size(); x++) {
      assertEquals(createdIndividuals.get(x), population.get(x));
    }
  }

  private IndividualInitializer mockInitializer() {
    IndividualInitializer initializer = Mockito.mock(IndividualInitializer.class);
    Mockito.when(initializer.newIndividual()).thenAnswer(new Answer<Individual>() {

      @Override
      public Individual answer(InvocationOnMock invocation) throws Throwable {
        Individual i = new Individual(new Program(new byte[] {}));
        createdIndividuals.add(i);
        return i;
      }
    });
    return initializer;
  }
}
