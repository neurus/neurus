package org.neurus.breeder;

import static org.neurus.rng.RandomUtils.randomEnabledBit;
import static org.neurus.util.Primitives.ubtoi;

import java.util.BitSet;

import org.neurus.evolution.Individual;
import org.neurus.machine.EffectivenessAnalyzer;
import org.neurus.machine.InstructionRandomizer;
import org.neurus.machine.Machine;
import org.neurus.machine.Operator;
import org.neurus.machine.Program;
import org.neurus.rng.RandomChoice;
import org.neurus.rng.RandomNumberGenerator;

public class EffectiveMicroMutation implements Breeder {

  private InstructionRandomizer instructionRandomizer;
  private RandomChoice mutationTypeChoice;
  private EffectivenessAnalyzer effectivenessAnalyzer;
  private Machine machine;
  private RandomNumberGenerator rng;

  public EffectiveMicroMutation(Machine machine, InstructionRandomizer instructionRandomizer,
      RandomNumberGenerator rng, EffectivenessAnalyzer effectivenessAnalyzer,
      double pRegisterMutation, double pOperatorMutation, double pConstantMutation) {
    this.machine = machine;
    this.rng = rng;
    this.instructionRandomizer = instructionRandomizer;
    this.effectivenessAnalyzer = effectivenessAnalyzer;
    this.mutationTypeChoice = new RandomChoice(rng, new double[] { pRegisterMutation,
        pOperatorMutation, pConstantMutation });
  }

  @Override
  public Individual[] breed(Individual[] parents) {
    Program parentProgram = parents[0].getProgram();
    
    effectivenessAnalyzer.analyzeProgram(parentProgram);
    // can't pick any effective instructions in a totally non effective program
    if(parentProgram.getEffectiveInstructions().cardinality() == 0) {
      return new Individual[] {};
    }
    
    int mutationType = mutationTypeChoice.pickNext();
    Program offspring = null;
    if (mutationType == 0) {
      offspring = registerMutation(parentProgram);
    } else if (mutationType == 1) {
      offspring = operatorMutation(parentProgram);
    } else if (mutationType == 2) {
      offspring = constantMutation(parentProgram);
    } else {
      throw new AssertionError("Not exepcted");
    }
    if (offspring == null) {
      return new Individual[] {};
    }
    return new Individual[] { new Individual(offspring) };
  }

  private Program registerMutation(Program parentProgram) {
    // copy the bytecode so we can modify it
    byte[] newBytecode = parentProgram.getBytecode().clone();

    int instruction = randomEnabledBit(rng, parentProgram.getEffectiveInstructions());

    // chose between input and destination register
    Operator operator = operatorAtLine(newBytecode, instruction);
    int toChoseFrom = operator.hasDestinationRegister() ? 1 : 0;
    toChoseFrom += operator.getInputRegisters();
    if (toChoseFrom == 0) {
      // no registers, we can do nothing
      return null;
    }
    int chosen = rng.nextInt(toChoseFrom);
    if (operator.hasDestinationRegister() && chosen == toChoseFrom - 1) {
      int destinationAddress = address(instruction) + machine.getBytesPerInstruction() - 1;
      int oldDestinationRegister = ubtoi(newBytecode[destinationAddress]);
      // We chose to change the destination register
      BitSet effectiveRegisters = effectivenessAnalyzer.analyzeEffectiveRegistersAtInstruction(
          parentProgram, instruction);
      int newDestinationRegister = randomEnabledBit(rng, effectiveRegisters);
      if (oldDestinationRegister == newDestinationRegister) {
        // TODO May be retry a few times?
        return null;
      }
      newBytecode[destinationAddress] = (byte) newDestinationRegister;
    } else {
      int destinationAddress = address(instruction) + 1 + chosen;
      int oldRegister = ubtoi(newBytecode[destinationAddress]);
      // We are modifying an input register
      int newRegister;
      if (chosen == 0) {
        newRegister = instructionRandomizer.randomCalculationRegister();
      } else {
        newRegister = instructionRandomizer.randomRegister();
      }
      if (oldRegister == newRegister) {
        // TODO May be retry a few times?
        return null;
      }
      newBytecode[destinationAddress] = (byte) newRegister;
    }
    return new Program(newBytecode);
  }

  private Operator operatorAtLine(byte[] bytecode, int line) {
    return machine.getOperator(ubtoi(bytecode[address(line)]));
  }

  private int address(int pointer) {
    return pointer * machine.getBytesPerInstruction();
  }

  private Program constantMutation(Program parentProgram) {
    // TODO Implement me
    return null;
  }

  private Program operatorMutation(Program parentProgram) {
    // copy the bytecode so we can modify it
    byte[] newBytecode = parentProgram.getBytecode().clone();
    // select a random instruction to mutate
    int instruction = randomEnabledBit(rng, parentProgram.getEffectiveInstructions());
    // select a random operator
    int address = address(instruction);
    int oldOperator = ubtoi(newBytecode[address]);
    int newOperator = instructionRandomizer.randomOperator();
    if (oldOperator == newOperator) {
      // TODO May be retry a few times?
      return null;
    }
    newBytecode[address] = (byte) newOperator;
    return new Program(newBytecode);
  }

  @Override
  public int getMaxNumberOfOffsprings() {
    return 1;
  }

  @Override
  public int getNumberOfParents() {
    return 1;
  }
}
