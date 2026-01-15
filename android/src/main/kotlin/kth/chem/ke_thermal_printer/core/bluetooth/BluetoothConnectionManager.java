package kth.chem.ke_thermal_printer.core.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.rt.printerlibrary.bean.BluetoothEdrConfigBean;
import com.rt.printerlibrary.connect.PrinterInterface;
import com.rt.printerlibrary.enumerate.ConnectStateEnum;
import com.rt.printerlibrary.factory.connect.BluetoothFactory;
import com.rt.printerlibrary.factory.connect.PIFactory;

import kth.chem.ke_thermal_printer.core.PrinterManager;


public class BluetoothConnectionManager {

    public ConnectStateEnum connect(String macAddress) throws Exception {

        BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(macAddress);
        BluetoothEdrConfigBean config = new BluetoothEdrConfigBean(device);
        PIFactory factory = new BluetoothFactory();
        PrinterInterface printerInterface = factory.create();
        printerInterface.setConfigObject(config);

        PrinterManager manager = PrinterManager.getInstance();
        manager.getPrinter().setPrinterInterface(printerInterface);
        manager.getPrinter().connect(config);

        return manager.getPrinter().getConnectState();
    }

    public void disconnect() {
        PrinterManager.getInstance().disconnect();
    }
}
