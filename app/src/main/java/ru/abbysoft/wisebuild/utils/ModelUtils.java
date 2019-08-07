package ru.abbysoft.wisebuild.utils;

import java.util.List;
import java.util.Random;

import ru.abbysoft.wisebuild.databinding.ParamDescription;
import ru.abbysoft.wisebuild.databinding.ReflectionAccessor;
import ru.abbysoft.wisebuild.model.CPU;
import ru.abbysoft.wisebuild.model.ComputerPart;
import ru.abbysoft.wisebuild.model.MemoryModule;
import ru.abbysoft.wisebuild.model.Motherboard;

/**
 * Helper functions related to model
 *
 * @author apopov
 */
public class ModelUtils {

    private static final Random RANDOM = new Random(System.currentTimeMillis());

    /**
     * Generate random part of random type (except AssembledPC)
     *
     * @return random computer part
     */
    public static ComputerPart generateRandomPart() {
        List<ComputerPart.ComputerPartType> types =
                ComputerPart.ComputerPartType.getEntriesWithoutAssembly();

        ComputerPart.ComputerPartType type = types.get(RANDOM.nextInt(types.size() - 1));

        return generateRandomPartOfType(type);
    }

    /**
     * Generate random part
     *
     * AssembledPC part type is not returned
     *
     * @param type type of generated part
     * @return generated random ComputerPart
     */
    public static ComputerPart generateRandomPartOfType(ComputerPart.ComputerPartType type) {
        ComputerPart part;
        switch (type) {
            case MOTHERBOARD:
                part = generateRandomMotherboard();
                break;
            case CPU:
                part = generateRandomCPU();
                break;
            case MEMORY_MODULE:
                part = generateRandomMemory();
                break;
                default:
                    part = generateRandomMotherboard();
                    break;
        }

        part.setPriceUsd(RANDOM.nextInt(120000));

        return part;
    }

    private static ComputerPart generateRandomCPU() {
        String[] names = {"Pentium G2020", "Celeron N2040",
                "i3 3450", "i9 9800K", "Ryzen 7 3400X", "FX 8200"};
        String[] manufactures = {"Intel", "AMD", "ARM", "Elbrus"};

        String name = names[RANDOM.nextInt(names.length)];
        String manufacturer = manufactures[RANDOM.nextInt(manufactures.length)];
        int cores = RANDOM.nextInt(64);

        CPU cpu = new CPU();
        cpu.setName(name);
        cpu.setManufacturer(manufacturer);
        cpu.setCores(cores);

        return cpu;
    }

    private static Motherboard generateRandomMotherboard() {
        Motherboard.SocketType[] socketTypes = Motherboard.SocketType.values();
        Motherboard.SocketType type = socketTypes[RANDOM.nextInt(socketTypes.length)];

        String name = "Motherboard-" + RANDOM.nextInt(5000);
        Motherboard motherboard = new Motherboard();
        motherboard.setName(name);
        motherboard.setSocketType(type);

        return motherboard;
    }

    private static MemoryModule generateRandomMemory() {
        String[] names = {"Patriot", "Kingston", "Samsung", "Apple", "Elbrus"};
        MemoryModule.MemoryType[] types = MemoryModule.MemoryType.values();

        String name = names[RANDOM.nextInt(names.length)];
        int capacity = RANDOM.nextInt(32 * 1024);
        MemoryModule.MemoryType type = types[RANDOM.nextInt(types.length)];

        MemoryModule module = new MemoryModule();
        module.setName(name);
        module.setMemoryType(type);
        module.setCapacityMb(capacity);

        return module;
    }

    /**
     * Create int parameter with default values
     * @param name name of model property
     * @return description of property
     */
    public static ParamDescription createIntParameter(String name) {
        return createParameter(name, int.class);
    }

    /**
     * Create long parameter with default values
     * @param name name of model property
     * @return description of property
     */
    public static ParamDescription createLongParameter(String name) {
        return createParameter(name, long.class);
    }

    /**
     * Create float parameter with default values
     * @param name name of model property
     * @return description of property
     */
    public static ParamDescription createFloatParameter(String name) {
        return createParameter(name, float.class);
    }

    /**
     * Create double parameter with default values
     * @param name name of model property
     * @return description of property
     */
    public static ParamDescription createDoubleParameter(String name) {
        return createParameter(name, double.class);
    }

    /**
     * Create string parameter with default values
     * @param name name of model property
     * @return description of property
     */
    public static ParamDescription createStringParameter(String name) {
        return createParameter(name, String.class);
    }

    /**
     * Create parameter with default values
     * @param name name of model property
     * @param claz class of model property value
     * @return description of property
     */
    public static ParamDescription createParameter(String name, Class<? extends Object> claz) {
        return new ParamDescription(name, claz, new ReflectionAccessor(name));
    }
}
