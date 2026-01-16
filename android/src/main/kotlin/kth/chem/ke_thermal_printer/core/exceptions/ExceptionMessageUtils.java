package kth.chem.ke_thermal_printer.core.exceptions;

import com.rt.printerlibrary.enumerate.BaseEnum;

public class ExceptionMessageUtils {

    public static String getConnectExceptionMsg(int currentConnectType) {
        switch (currentConnectType) {
            case BaseEnum.CON_BLUETOOTH:
                return "Bluetooth not connected";
            case BaseEnum.CON_BLUETOOTH_BLE:
                return "Bluetooth BLE not connected";
            case BaseEnum.CON_WIFI:
                return "WiFi not connected";
            case BaseEnum.CON_USB:
                return "USB not connected";
            case BaseEnum.CON_COM:
                return "COM port not connected";
        }
        return "Printer not connected";
    }
}
