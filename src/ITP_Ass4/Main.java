package ITP_Ass4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/*
Main class
*/

public class Main {
    public static final int ANIMAL_CUT_OFF = 3;
    public static final int MIN_LINES_NUMBER = 4;
    public static final int WEIGHT_INDEX = 1;
    public static final int SPEED_INDEX = 2;
    public static final int ENERGY_INDEX = 3;
    public static final int MIN_DAYS = 1;
    public static final int MAX_DAYS = 30;
    public static final int MIN_NUM_OF_ANIMALS = 1;
    public static final int MAX_NUM_OF_ANIMALS = 20;
    public static final float COEFFICIENT = 0.1f;
    public static void main(String[] args) {
        List<String> lines = new ArrayList<>();
        // Reading lines from file to list array
        try (Scanner scanner = new Scanner(new File("input.txt"))) {
            if (scanner.hasNextLine()) {
                while (scanner.hasNextLine()) {
                    String currentLine = scanner.nextLine();
                    if (!currentLine.equals(" ") && !currentLine.isEmpty()) {
                        lines.add(currentLine);
                    }
                }
            } else {
                throw new InvalidInputsException();
            }
        } catch (FileNotFoundException | InvalidInputsException ex) {
            System.out.println(ex.getMessage());
            return;
        }

        try {
            // Checking whether grass amount is a float and input file has at least 4 lines
            if (lines.size() <= MIN_LINES_NUMBER - 1 || !isFloat(lines.get(1))) {
                throw new InvalidInputsException();
            }
            // Creating a Field and checking whether grass amount is in bounds
            Field field = new Field(Float.parseFloat(lines.get(1)));

            // Checking whether days and number of animals are integers
            if (!isInteger(lines.getFirst()) || !isInteger(lines.get(2))) {
                throw new InvalidInputsException();
            }

            // Creating variables for days and number of animals
            int days = Integer.parseInt(lines.getFirst());
            int numberOfAnimals = Integer.parseInt(lines.get(2));

            // Checking whether days and number of animals are in bounds
            if (!daysInRange(days) || !numberOfAnimalsInRange(numberOfAnimals)) {
                throw new InvalidInputsException();
            }
            // Reading animals from file to animals array
            List<Animal> animals = readAnimals(lines.subList(ANIMAL_CUT_OFF, lines.size()));

            // Checking for "Invalid number of animals" exception
            if (lines.subList(ANIMAL_CUT_OFF, lines.size()).size() != numberOfAnimals) {
                throw new InvalidInputsException();
            }
            // Running simulation
            runSimulation(days, field.getGrassAmount(), animals);

        } catch (GrassOutOfBoundsException ex) {
            System.out.println(ex.getMessage());
        } catch (InvalidInputsException ex) {
            System.out.println(ex.getMessage());
        } catch (InvalidNumberOfAnimalParametersException ex) {
            System.out.println(ex.getMessage());
        } catch (WeightOutOfBoundsException ex) {
            System.out.println(ex.getMessage());
        } catch (SpeedOutOfBoundsException ex) {
            System.out.println(ex.getMessage());
        } catch (EnergyOutOfBoundsException ex) {
            System.out.println(ex.getMessage());
        }

    }

    // Other methods

    /**
     * Function that checks if the parameters are float numbers
     * @param list - list of parameters
     * @return true if parameters are valid else false
     */
    public static boolean parametersAreValid(String[] list) {
        return isFloat(list[WEIGHT_INDEX]) && isFloat(list[SPEED_INDEX]) && isFloat(list[ENERGY_INDEX]);
    }

    /**
     * Function that checks if the days are in valid range
     * @param days - days from input
     * @return true if valid else false
     */
    private static boolean daysInRange(int days) {
        return (MIN_DAYS <= days && days <= MAX_DAYS);
    }

    /**
     * Function that checks if the number of animals variable is in valid range
     * @param numberOfAnimals - number of animals from input
     * @return true if valid else false
     */
    private static boolean numberOfAnimalsInRange(int numberOfAnimals) {
        return (MIN_NUM_OF_ANIMALS <= numberOfAnimals && numberOfAnimals <= MAX_NUM_OF_ANIMALS);
    }

    /**
     * Function that checks if the string can be transformed into the integer
     * @param word - string to check
     * @return true if it can be else false
     */
    public static boolean isInteger(String word) {
        try {
            Integer.parseInt(word);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    /**
     * Function that checks if the string can be transformed into the float number
     * @param word - string to check
     * @return true if it can be else false
     */
    public static boolean isFloat(String word) {
        try {
            Float.parseFloat(word);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    // UML Diagram methods

    /**
     * Function that reads and checks animals from the input file
     * @param lines - lines from input file
     * @return list of animals read
     * @throws InvalidInputsException - happens if any wrong inputs are given
     * @throws InvalidNumberOfAnimalParametersException - happens if number of parameters is invalid
     * @throws EnergyOutOfBoundsException - happens if energy is out of bounds
     * @throws SpeedOutOfBoundsException - happens if speed is out of bounds
     * @throws WeightOutOfBoundsException - happens if weight is out of bounds
     */
    private static List<Animal> readAnimals(List<String> lines)
            throws InvalidInputsException, InvalidNumberOfAnimalParametersException,
            EnergyOutOfBoundsException, SpeedOutOfBoundsException, WeightOutOfBoundsException {
        List<Animal> animals = new ArrayList<>();
        for (String el : lines) {
            String[] splitLines = el.split(" ");
            if (splitLines.length != MIN_LINES_NUMBER) {
                throw new InvalidNumberOfAnimalParametersException();
            }
            switch (splitLines[0]) {
                case "Lion":
                    if (!parametersAreValid(splitLines)) {
                        throw new InvalidInputsException();
                    }

                    animals.add(new Lion(
                            Float.parseFloat(splitLines[WEIGHT_INDEX]),
                            Float.parseFloat(splitLines[SPEED_INDEX]),
                            Float.parseFloat(splitLines[ENERGY_INDEX])
                    ));
                    break;
                case "Zebra":
                    if (!parametersAreValid(splitLines)) {
                        throw new InvalidInputsException();
                    }

                    animals.add(new Zebra(
                            Float.parseFloat(splitLines[WEIGHT_INDEX]),
                            Float.parseFloat(splitLines[SPEED_INDEX]),
                            Float.parseFloat(splitLines[ENERGY_INDEX])
                    ));
                    break;
                case "Boar":
                    if (!parametersAreValid(splitLines)) {
                        throw new InvalidInputsException();
                    }

                    animals.add(new Boar(
                            Float.parseFloat(splitLines[WEIGHT_INDEX]),
                            Float.parseFloat(splitLines[SPEED_INDEX]),
                            Float.parseFloat(splitLines[ENERGY_INDEX])
                    ));
                    break;
                default:
                    throw new InvalidInputsException();
            }
        }

        return animals;
    }

    /**
     * Function that runs the simulation and performs main logic
     * @param days - number of days to simulate
     * @param grassAmount - grass amount to create a Field object
     * @param animals - array of animals
     * @throws InvalidInputsException - happens if any wrong inputs are given
     * @throws GrassOutOfBoundsException - happens if grass is out of bounds
     */
    private static void runSimulation(int days, float grassAmount, List<Animal> animals)
            throws InvalidInputsException, GrassOutOfBoundsException {
        // Stopping the program if all animals are dead
        removeDeadAnimals(animals);
        if (animals.isEmpty()) {
            return;
        }
        // Adding first animal to the end to implement a cycle
        animals.add(animals.getFirst());

        Field field = new Field(grassAmount);
        boolean cycleChange = false;

        for (int i = 0; i < days; i++) {
            for (int j = 0; j < animals.size() - 1; j++) {
                if (animals.get(j).getEnergy() <= 0) {
                    continue;
                }
                // Enumerating the animals
                if (animals.get(j) instanceof Lion) {
                    try {
                        // Eating animal in front
                        ((Lion) animals.get(j)).huntPrey(animals.get(j), animals.get(j + 1));
                        animals.get(j + 1).setEnergy(0);
                        animals.get(j).setEnergy(animals.get(j).getEnergy() + animals.get(j + 1).getWeight());
                        if ((j + 1) == animals.size() - 1) {
                            animals.getFirst().setEnergy(0);
                            cycleChange = true;
                        }
                    } catch (SelfHuntingException ex) {
                        System.out.println(ex.getMessage());
                    } catch (CannibalismException ex) {
                        System.out.println(ex.getMessage());
                    } catch (TooStrongPreyException ex) {
                        System.out.println(ex.getMessage());
                    }
                } else if (animals.get(j) instanceof Zebra) {
                    // Eating grass
                    if (field.getGrassAmount() > COEFFICIENT * animals.get(j).getWeight()) {
                        ((Zebra) animals.get(j)).grazeInTheField(animals.get(j), field);
                    }
                } else if (animals.get(j) instanceof Boar) {
                    // Eating grass
                    if (field.getGrassAmount() > COEFFICIENT * animals.get(j).getWeight()) {
                        ((Boar) animals.get(j)).grazeInTheField(animals.get(j), field);
                    }
                    // Eating animal in front
                    try {
                        ((Boar) animals.get(j)).huntPrey(animals.get(j), animals.get(j + 1));
                        animals.get(j + 1).setEnergy(0);
                        animals.get(j).setEnergy(animals.get(j).getEnergy() + animals.get(j + 1).getWeight());
                        if ((j + 1) == animals.size() - 1) {
                            animals.getFirst().setEnergy(0);
                            cycleChange = true;
                        }
                    } catch (SelfHuntingException ex) {
                        System.out.println(ex.getMessage());
                    } catch (CannibalismException ex) {
                        System.out.println(ex.getMessage());
                    } catch (TooStrongPreyException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
            // Decrementing the energy
            for (Animal el : animals.subList(0, animals.size() - 1)) {
                el.decrementEnergy();
            }
            // Clearing dead animals, remembering the last dead animal
            Animal helpAnimal = animals.getLast();
            removeDeadAnimals(animals);
            if (animals.isEmpty()) {
                return;
            }
            // Operating the cycle
            if (cycleChange) {
                animals.addLast(animals.getFirst());
                cycleChange = false;
            } else if (!animals.getLast().equals(helpAnimal)) {
                animals.add(animals.getFirst());
            }
            // Doubling the grass amount
            field.makeGrassGrow();
        }
        printAnimals(animals.subList(0, animals.size() - 1));
    }

    /**
     * Function that makes all animals from the list to make sounds
     * @param animals - list of animals
     */
    private static void printAnimals(List<Animal> animals) {
        for (Animal el : animals) {
            el.makeSound();
        }
    }

    /**
     * Function that deletes animals with <=0 energy from the list
     * @param animals  - list of animals
     */
    private static void removeDeadAnimals(List<Animal> animals) {
        Iterator<Animal> iterator = animals.iterator();
        while (iterator.hasNext()) {
            Animal el = iterator.next();
            if (el.getEnergy() <= 0) {
                iterator.remove();
            }
        }
    }
}

/*
Classes
*/


abstract class Animal {
    public static final float MIN_SPEED = 5;
    public static final float MAX_SPEED = 60;
    public static final float MIN_ENERGY = 0;
    public static final float MAX_ENERGY = 100;
    public static final float MIN_WEIGHT = 5;
    public static final float MAX_WEIGHT = 200;
    public static final float COEFFICIENT = 0.1f;

    private float weight;
    private float speed;
    private float energy;

    protected Animal(float weight, float speed, float energy)
            throws SpeedOutOfBoundsException, EnergyOutOfBoundsException, WeightOutOfBoundsException {
        if (weight < MIN_WEIGHT || weight > MAX_WEIGHT) {
            throw new WeightOutOfBoundsException();
        } else if (speed < MIN_SPEED || speed > MAX_SPEED) {
            throw new SpeedOutOfBoundsException();
        } else if (energy < MIN_ENERGY || energy > MAX_ENERGY) {
            throw new EnergyOutOfBoundsException();
        }
        this.weight = weight;
        this.speed = speed;
        this.energy = energy;
    }

    public void makeSound() {
        // no instructions
    }

    public void decrementEnergy() {
        this.energy--;
    }

    abstract void eat(List<Animal> animals, Field field);

    public float getWeight() {
        return weight;
    }

    public float getSpeed() {
        return speed;
    }

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float energy) {
        this.energy = energy > MAX_ENERGY ? MAX_ENERGY : energy;
    }
}


class Lion extends Animal implements Carnivore {
    public Lion(float weight, float speed, float energy)
            throws EnergyOutOfBoundsException, SpeedOutOfBoundsException, WeightOutOfBoundsException {
        super(weight, speed, energy);
    }

    @Override
    void eat(List<Animal> animals, Field field) {
        // no instructions
    }

    @Override
    public <T> Animal choosePrey(List<Animal> animals, T hunter) {
        // no instructions
        return null;
    }

    @Override
    public void huntPrey(Animal hunter, Animal prey)
            throws CannibalismException, SelfHuntingException, TooStrongPreyException {
        if (hunter.equals(prey)) {
            throw new SelfHuntingException();
        } else if (hunter.getClass() == prey.getClass()) {
            throw new CannibalismException();
        } else if (!((prey.getSpeed() < hunter.getSpeed()) || (prey.getEnergy() < hunter.getEnergy()))) {
            throw new TooStrongPreyException();
        }
    }

    @Override
    public void makeSound() {
        System.out.println(AnimalSound.LION.getSound());
    }
}


class Zebra extends Animal implements Herbivore {
    public Zebra(float weight, float speed, float energy)
            throws EnergyOutOfBoundsException, SpeedOutOfBoundsException, WeightOutOfBoundsException {
        super(weight, speed, energy);
    }

    @Override
    void eat(List<Animal> animals, Field field) {
        // no instructions

    }

    @Override
    public void grazeInTheField(Animal grazer, Field field) {
        grazer.setEnergy(grazer.getEnergy() + grazer.getWeight() * COEFFICIENT);
        field.setGrassAmount(field.getGrassAmount() - grazer.getWeight() * COEFFICIENT);
    }

    @Override
    public void makeSound() {
        System.out.println(AnimalSound.ZEBRA.getSound());
    }
}


class Boar extends Animal implements Omnivore {
    protected Boar(float weight, float speed, float energy)
            throws EnergyOutOfBoundsException, SpeedOutOfBoundsException, WeightOutOfBoundsException {
        super(weight, speed, energy);
    }

    @Override
    void eat(List<Animal> animals, Field field) {
        // no instructions
    }

    @Override
    public <T> Animal choosePrey(List<Animal> animals, T hunter) {
        // no instructions
        return null;
    }

    @Override
    public void huntPrey(Animal hunter, Animal prey)
            throws CannibalismException, SelfHuntingException, TooStrongPreyException {
        if (hunter.equals(prey)) {
            throw new SelfHuntingException();
        } else if (hunter.getClass() == prey.getClass()) {
            throw new CannibalismException();
        } else if (!((prey.getSpeed() < hunter.getSpeed()) || (prey.getEnergy() < hunter.getEnergy()))) {
            throw new TooStrongPreyException();
        }
    }

    @Override
    public void grazeInTheField(Animal grazer, Field field) {
        grazer.setEnergy(grazer.getEnergy() + grazer.getWeight() * COEFFICIENT);
        field.setGrassAmount(field.getGrassAmount() - grazer.getWeight() * COEFFICIENT);
    }

    @Override
    public void makeSound() {
        System.out.println(AnimalSound.BOAR.getSound());
    }
}


class Field {
    private static final int MAX_GRASS_AMOUNT = 100;
    private float grassAmount;

    public Field(float grassAmount) throws GrassOutOfBoundsException {
        if (!grassInRange(grassAmount)) {
            throw new GrassOutOfBoundsException();
        }
        this.grassAmount = grassAmount;
    }

    public void makeGrassGrow() {
        this.grassAmount = this.grassAmount * 2 > MAX_GRASS_AMOUNT ? MAX_GRASS_AMOUNT : this.grassAmount * 2;
    }

    public float getGrassAmount() {
        return this.grassAmount;
    }

    public void setGrassAmount(float grassAmount) {
        this.grassAmount = grassAmount;
    }

    public boolean grassInRange(float grassAmount) {
        return 0 <= grassAmount && grassAmount <= MAX_GRASS_AMOUNT;
    }
}

/*
Enumeration
*/

enum AnimalSound {
    LION("Roar"),
    ZEBRA("Ihoho"),
    BOAR("Oink");

    private final String sound;

    AnimalSound(String sound) {
        this.sound = sound;
    }

    public String getSound() {
        return sound;
    }
}

/*
Interfaces
*/

interface Carnivore {
    <T> Animal choosePrey(List<Animal> animals, T hunter);

    void huntPrey(Animal hunter, Animal prey) throws SelfHuntingException, TooStrongPreyException, CannibalismException;
}

interface Herbivore {
    void grazeInTheField(Animal grazer, Field field);
}

interface Omnivore extends Herbivore, Carnivore {
}

/*
Exceptions
 */

class WeightOutOfBoundsException extends Exception {
    @Override
    public String getMessage() {
        return "The weight is out of bounds";
    }
}

class EnergyOutOfBoundsException extends Exception {
    @Override
    public String getMessage() {
        return "The energy is out of bounds";
    }
}

class SpeedOutOfBoundsException extends Exception {
    @Override
    public String getMessage() {
        return "The speed is out of bounds";
    }
}

class SelfHuntingException extends Exception {
    @Override
    public String getMessage() {
        return "Self-hunting is not allowed";
    }
}

class TooStrongPreyException extends Exception {
    @Override
    public String getMessage() {
        return "The prey is too strong or too fast to attack";
    }
}

class CannibalismException extends Exception {
    @Override
    public String getMessage() {
        return "Cannibalism is not allowed";
    }
}

class GrassOutOfBoundsException extends Exception {
    @Override
    public String getMessage() {
        return "The grass is out of bounds";
    }
}

class InvalidNumberOfAnimalParametersException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid number of animal parameters";
    }
}

class InvalidInputsException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid inputs";
    }
}

