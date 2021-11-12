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
    private float generatorOutput;

    /**
     * creates a generator class
     * @param numberOfGenerators The Number of generators
     * @param generatorOutput The output of the generators
     */
    public Generator(int numberOfGenerators, float generatorOutput){
        setNumberOfGenerators(numberOfGenerators);
        setGeneratorOutput(generatorOutput);
    }

    /**
     * set's the generator's output
     * @param generatorOutput The output of the generators
     */
    public void setGeneratorOutput(float generatorOutput) {
        checkGeneratorOutputRules(generatorOutput);
        this.generatorOutput = generatorOutput;
    }

    /**
     * checks if the output values are within boundaries
     * @param generatorOutput The output of the generators
     */
    private void checkGeneratorOutputRules(float generatorOutput) {
        if (generatorOutput <= 0)
            throw new IllegalGeneratorException("Generator output cannot be negative");
    }

    /**
     * sets the number of generators
     * @param numberOfGenerators The Number of generators
     */
    public void setNumberOfGenerators(int numberOfGenerators) {
        checkNumberOfGeneratorsRules(numberOfGenerators);
        this.numberOfGenerators = numberOfGenerators;
    }

    /**
     * checks if the numbers are within boundaries
     * @param numberOfGenerators The Number of generators
     */
    private void checkNumberOfGeneratorsRules(int numberOfGenerators) {
        if (numberOfGenerators <= 0)
            throw new IllegalGeneratorException("Generator Number cannot be lower than 1");
    }

    /**
     * returns the generator's output
     * @return returns the generator's output
     */
    public float getGeneratorOutput() {
        return generatorOutput;
    }

    /**
     * return the number of generators
     * @return return the number of generators
     */
    public int getNumberOfGenerators() {
        return numberOfGenerators;
    }
}
