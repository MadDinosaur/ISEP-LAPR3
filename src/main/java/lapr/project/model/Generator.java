package lapr.project.model;

import lapr.project.exception.IllegalGeneratorException;

public class Generator {


    /**
     * The Number of generators
     */
    private int numberOfGenerators;

    /**
     * The output of the generators
     */
    private long generatorOutput;

    /**
     * creates a generator class
     * @param numberOfGenerators The Number of generators
     * @param generatorOutput The output of the generators
     */
    public Generator(int numberOfGenerators, long generatorOutput){
        setNumberOfGenerators(numberOfGenerators);
        setGeneratorOutput(generatorOutput);
    }

    /**
     * set's the generator's output
     * @param generatorOutput The output of the generators
     */
    public void setGeneratorOutput(long generatorOutput) {
        if (checkGeneratorOutputRules(generatorOutput))
            this.generatorOutput = generatorOutput;
    }

    /**
     * checks if the output values are within boundaries
     * @param generatorOutput The output of the generators
     * @return true if the output is within boundaries
     */
    private boolean checkGeneratorOutputRules(long generatorOutput) {
        if (generatorOutput >= 0)
            return true;
        else
            throw new IllegalGeneratorException("Generator output cannot be negative");
    }

    /**
     * sets the number of generators
     * @param numberOfGenerators The Number of generators
     */
    public void setNumberOfGenerators(int numberOfGenerators) {
        if (checkNumberOfGeneratorsRules(numberOfGenerators))
            this.numberOfGenerators = numberOfGenerators;
    }

    /**
     * checks if the numbers are within boundaries
     * @param numberOfGenerators The Number of generators
     * @return true if the Generator number is withing expected boundaries
     */
    private boolean checkNumberOfGeneratorsRules(int numberOfGenerators) {
        if (numberOfGenerators > 0)
            return true;
        else
            throw new IllegalGeneratorException("Generator Number cannot be lower than 1");
    }
}
