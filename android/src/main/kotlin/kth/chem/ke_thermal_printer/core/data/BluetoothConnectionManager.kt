package kth.chem.ke_thermal_printer.core.data

import android.bluetooth.BluetoothAdapter
import android.os.Build
import androidx.annotation.RequiresApi
import com.rt.printerlibrary.bean.BluetoothEdrConfigBean
import com.rt.printerlibrary.connect.PrinterInterface
import com.rt.printerlibrary.factory.connect.BluetoothFactory
import com.rt.printerlibrary.factory.connect.PIFactory
import kth.chem.ke_thermal_printer.core.domain.ConnectionManager

class BluetoothConnectionManager : ConnectionManager {

    @RequiresApi(Build.VERSION_CODES.ECLAIR)
    override fun getConfig(address: String): Any {
        val device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address)
        return BluetoothEdrConfigBean(device)
    }

    override fun getInterface(): PrinterInterface<*> {
        val factory: PIFactory = BluetoothFactory()
        return factory.create()
    }
}

