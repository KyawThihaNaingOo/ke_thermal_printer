package kth.chem.ke_thermal_printer.core.domain;

import com.rt.printerlibrary.connect.PrinterInterface;

/**
 * Java version of ConnectionManager to be implemented by connection managers.
 */
public interface ConnectionManager {
    /**
     * Return a connection/config object (implementation-specific) for the given address.
     *
     * @param address device address (for example Bluetooth MAC)
     * @return config object to be passed to printer libraries
     */
    Object getConfig(String address);

    /**
     * Return a PrinterInterface implementation instance for the underlying transport.
     * Use a wildcard generic to avoid tying the interface to a specific transport type.
     *
     * @return PrinterInterface instance
     */
    PrinterInterface<?> getInterface();
}
