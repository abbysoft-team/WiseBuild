package ru.abbysoft.wisebuild.assembly;

import ru.abbysoft.wisebuild.model.ComputerPart;

/**
 * Part listener
 *
 * @author apopov
 */
public interface PartListener {

    void partReceived(ComputerPart part);
}
