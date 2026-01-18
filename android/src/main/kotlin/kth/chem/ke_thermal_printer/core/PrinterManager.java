package kth.chem.ke_thermal_printer.core;

import android.telephony.mbms.MbmsErrors;
import android.util.Log;

import com.rt.printerlibrary.bean.BluetoothEdrConfigBean;
import com.rt.printerlibrary.cmd.Cmd;
import com.rt.printerlibrary.cmd.CpclFactory;
import com.rt.printerlibrary.cmd.EscFactory;
import com.rt.printerlibrary.cmd.TscFactory;
import com.rt.printerlibrary.connect.PrinterInterface;
import com.rt.printerlibrary.enumerate.BaseEnum;
import com.rt.printerlibrary.enumerate.ConnectStateEnum;
import com.rt.printerlibrary.factory.cmd.CmdFactory;
import com.rt.printerlibrary.factory.printer.LabelPrinterFactory;
import com.rt.printerlibrary.factory.printer.PinPrinterFactory;
import com.rt.printerlibrary.factory.printer.PrinterFactory;
import com.rt.printerlibrary.factory.printer.ThermalPrinterFactory;
import com.rt.printerlibrary.factory.printer.UniversalPrinterFactory;
import com.rt.printerlibrary.printer.RTPrinter;
import com.rt.printerlibrary.utils.ConnectListener;

import kth.chem.ke_thermal_printer.core.exceptions.ConnectionException;
import kth.chem.ke_thermal_printer.core.exceptions.ExceptionMessageUtils;

public class PrinterManager {
    private final String TAG = getClass().getSimpleName();
    @BaseEnum.CmdType
    private int currentCmdType = BaseEnum.CMD_ESC;
    @BaseEnum.ConnectType
    private int currentConnectType = BaseEnum.NONE;
    private static PrinterManager instance;
    private RTPrinter rtPrinter;
//    private PrinterManager() {
//        PrinterFactory printerFactory = new LabelPrinterFactory();
//        rtPrinter = printerFactory.create();
//        rtPrinter = new UniversalPrinterFactory().create();
//    }

    private PrinterManager() {
    }

//    public void init(@BaseEnum.CmdType int printerCMD) {
//        initPrinter(printerCMD);

    /// /        switch (printerCMD) {
    /// /            case BaseEnum.CmdType:
    /// /                initPrinter(BaseEnum.CMD_ESC);
    /// /                break;
    /// /            case "CPCL":
    /// /                initPrinter(BaseEnum.CMD_CPCL);
    /// /                break;
    /// /            case "TSC":
    /// /                initPrinter(BaseEnum.CMD_TSC);
    /// /                break;
    /// /            case "ZPL":
    /// /                initPrinter(BaseEnum.CMD_ZPL);
    /// /                break;
    /// /            default:
    /// /                initPrinter(BaseEnum.CMD_ESC);
    /// /                break;
    /// /        }
//    }
    public void init(@BaseEnum.CmdType int currentCmdType) {
        this.currentCmdType = currentCmdType;
        PrinterFactory factory;
        if (currentCmdType == BaseEnum.CMD_ESC) {
            factory = new ThermalPrinterFactory();
        } else if (currentCmdType == BaseEnum.CMD_CPCL || currentCmdType == BaseEnum.CMD_TSC || currentCmdType == BaseEnum.CMD_ZPL) {
            factory = new LabelPrinterFactory();
        } else if (currentCmdType == BaseEnum.CMD_PIN) {
            factory = new PinPrinterFactory();
        } else {
            factory = new UniversalPrinterFactory();
        }
        rtPrinter = factory.create();
        Log.i(TAG, "init: " + "Printer initialized with CMD type " + currentCmdType);
    }

    public static PrinterManager getInstance() {
        if (instance == null) {
            instance = new PrinterManager();
        }
        return instance;
    }

    public ConnectStateEnum connected() {
        return rtPrinter.getConnectState();
    }

    public RTPrinter getPrinter() {
        return rtPrinter;
    }

    public void setConnectListener(ConnectListener listener) {
        if (rtPrinter == null) {
            Log.e(TAG, "setConnectListener: rtPrinter is null. Please initialize the printer first.");
        } else {
            if (listener == null) {
                Log.w(TAG, "setConnectListener: listener is null.");
            }
            rtPrinter.setConnectListener(listener);
        }
    }

    public void setPrinterInterface(PrinterInterface printerInterface) {
        rtPrinter.setPrinterInterface(printerInterface);
    }

    public void disconnect() {
        if (rtPrinter.getConnectState() == ConnectStateEnum.Connected) {
            rtPrinter.disConnect();
        }
    }

    public void connect(BluetoothEdrConfigBean config) throws Exception {
        rtPrinter.connect(config);
    }

    public void selfTest() throws ConnectionException {
        if (rtPrinter.getConnectState() != ConnectStateEnum.Connected) {
            throw new ConnectionException(ExceptionMessageUtils.getConnectExceptionMsg(currentConnectType));
        }
        // ESC Factory
        Cmd cmd = new EscFactory().create();
        // Factory set
        if (currentCmdType == BaseEnum.CMD_ESC) {
            CmdFactory cmdFactory = new EscFactory();
            cmd = cmdFactory.create();
        } else if (currentCmdType == BaseEnum.CMD_CPCL) {
            CmdFactory cmdFactory = new CpclFactory();
            cmd = cmdFactory.create();
        } else if (currentCmdType == BaseEnum.CMD_TSC) {
            CmdFactory cmdFactory = new TscFactory();
            cmd = cmdFactory.create();
        } else if (currentCmdType == BaseEnum.CMD_ZPL) {
            CmdFactory cmdFactory = new TscFactory();
            cmd = cmdFactory.create();
        }
        rtPrinter.writeMsgAsync(cmd.getSelfTestCmd());
    }
}
