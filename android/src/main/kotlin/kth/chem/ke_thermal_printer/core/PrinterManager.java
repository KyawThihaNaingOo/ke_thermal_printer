package kth.chem.ke_thermal_printer.core;

import com.rt.printerlibrary.enumerate.ConnectStateEnum;
import com.rt.printerlibrary.factory.printer.UniversalPrinterFactory;
import com.rt.printerlibrary.printer.RTPrinter;
import com.rt.printerlibrary.utils.ConnectListener;

public class PrinterManager {

    private static PrinterManager instance;
    private final RTPrinter rtPrinter;

    private PrinterManager() {
        rtPrinter = new UniversalPrinterFactory().create();
    }

    public static synchronized PrinterManager getInstance() {
        if (instance == null) {
            instance = new PrinterManager();
        }
        return instance;
    }

    public RTPrinter getPrinter() {
        return rtPrinter;
    }

    public void setConnectListener(ConnectListener listener) {
        rtPrinter.setConnectListener(listener);
    }

    public void disconnect() {
        if (rtPrinter.getConnectState() == ConnectStateEnum.Connected) {
            rtPrinter.disConnect();
        }
    }
}
