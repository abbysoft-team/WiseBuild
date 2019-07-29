package ru.abbysoft.wisebuild.utils;

import java.util.List;
import java.util.Random;

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
     * Generate random part
     *
     * AssembledPC part type is not returned
     *
     * @param type type of generated part
     * @return generated random ComputerPart
     */
    public static ComputerPart generateRandomPart(ComputerPart.ComputerPartType type) {
//        List<ComputerPart.ComputerPartType> types =
//                ComputerPart.ComputerPartType.getEntriesWithoutAssembly();
//
//        ComputerPart.ComputerPartType type = types.get(RANDOM.nextInt(types.size() - 1));

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

        return new CPU(names[RANDOM.nextInt(names.length)],
                manufactures[RANDOM.nextInt(manufactures.length)], RANDOM.nextInt(64));
    }

    private static Motherboard generateRandomMotherboard() {
        Motherboard.SocketType[] socketTypes = Motherboard.SocketType.values();
        Motherboard.SocketType type = socketTypes[RANDOM.nextInt(socketTypes.length)];

        return new Motherboard("Motherboard" + RANDOM.nextInt(), type);
    }

    private static MemoryModule generateRandomMemory() {
        String[] names = {"Patriot", "Kingston", "Samsung", "Apple", "Elbrus"};
        MemoryModule.MemoryType[] types = MemoryModule.MemoryType.values();

        return new MemoryModule(names[RANDOM.nextInt(names.length)],
                types[RANDOM.nextInt(types.length)], RANDOM.nextInt(32));
    }
}
