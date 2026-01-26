package kth.chem.ke_thermal_printer.core

import android.content.Context
import com.rt.printerlibrary.bean.BluetoothEdrConfigBean
import com.rt.printerlibrary.utils.ConnectListener
import kth.chem.ke_thermal_printer.core.data.BluetoothConnectionManager
import kth.chem.ke_thermal_printer.core.data.RTPrinterService
import kth.chem.ke_thermal_printer.core.domain.ConnectionManager
import kth.chem.ke_thermal_printer.core.domain.PrinterService


class PrinterFacade {

    private val connectionManager: ConnectionManager = BluetoothConnectionManager()
    private val printerService: PrinterService = RTPrinterService(connectionManager)

    fun initialize(cmdType: Int): PrinterFacade {
        printerService.initialize(cmdType)
        return this
    }

    fun setCmdType(cmdType: Int): PrinterFacade {
        printerService.setCmdType(cmdType)
        return this
    }

    fun connectBle(address: String): PrinterFacade {
        val config = connectionManager.getConfig(address)
        val printerInterface = connectionManager.getInterface()
        (printerService as RTPrinterService).setPrinterInterface(printerInterface)
        printerService.connect(config)
        return this
    }

    fun disconnect() {
        printerService.disconnect()
    }

    fun getStatus() = printerService.getConnectionState()

    fun setConnectListener(listener: ConnectListener): PrinterFacade {
        printerService.setConnectListener(listener)
        return this
    }

    fun selfTest() {
        printerService.selfTest()
    }

    fun printText(text: String) {
        printerService.printText(text)
    }

    // New: print a PDF file via the underlying PrinterService implementation
    fun printPdf(context: Context, pdfFilePath: String) {
        printerService.printPdf(context, pdfFilePath)
    }
}