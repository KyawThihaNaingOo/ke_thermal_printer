package kth.chem.ke_thermal_printer.core.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.util.Log;

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

import kth.chem.ke_thermal_printer.core.domain.ConnectionManager;
import kth.chem.ke_thermal_printer.core.domain.PrinterService;
import kth.chem.ke_thermal_printer.core.exceptions.ExceptionMessageUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Java implementation of the PrinterService using the RTPrinter library.
 */
public class RTPrinterService implements PrinterService {
    private static final String TAG = "RTPrinterService";

    private int currentCmdType = BaseEnum.CMD_ESC;
    private RTPrinter rtPrinter;
    private PrinterFactory printerFactory;
    private CmdFactory cmdFactory;
    private Cmd printerCmd;

    public RTPrinterService(ConnectionManager connectionManager) {
        // connectionManager retained for potential future use
    }

    @Override
    public void initialize(int cmdType) {
        this.currentCmdType = cmdType;
        if (cmdType == BaseEnum.CMD_ESC) {
            printerFactory = new ThermalPrinterFactory();
        } else if (cmdType == BaseEnum.CMD_CPCL || cmdType == BaseEnum.CMD_TSC || cmdType == BaseEnum.CMD_ZPL) {
            printerFactory = new LabelPrinterFactory();
        } else if (cmdType == BaseEnum.CMD_PIN) {
            printerFactory = new PinPrinterFactory();
        } else {
            printerFactory = new UniversalPrinterFactory();
        }
        rtPrinter = (RTPrinter<?>) printerFactory.create();
        Log.i(TAG, "Printer initialized with CMD type " + currentCmdType);
    }

    @Override
    public void setCmdType(int cmdType) {
        if (currentCmdType == BaseEnum.CMD_ESC) {
            printerCmd = new EscFactory().create();
        } else if (currentCmdType == BaseEnum.CMD_CPCL) {
            printerCmd = new CpclFactory().create();
        } else if (currentCmdType == BaseEnum.CMD_TSC) {
            printerCmd = new TscFactory().create();
        } else if (currentCmdType == BaseEnum.CMD_ZPL) {
            printerCmd = new TscFactory().create();
        } else {
            printerCmd = new EscFactory().create();
        }
    }

    @Override
    public void connect(Object config) throws Exception {
        // Use reflection to find a suitable connect method on RTPrinter and invoke it.
        if (rtPrinter == null) {
            Log.e(TAG, "RTPrinter not initialized");
            return;
        }
        rtPrinter.connect(config);
    }

    @Override
    public void disconnect() {
        rtPrinter.disConnect();
    }

    private ConnectStateEnum getRtPrinterConnectState() {
        if (rtPrinter == null) return ConnectStateEnum.NoConnect;
        return rtPrinter.getConnectState();
    }

    @Override
    public ConnectStateEnum getConnectionState() {
        return getRtPrinterConnectState();
    }

    @Override
    public void setConnectListener(ConnectListener listener) {
        if (rtPrinter == null) {
            Log.e(TAG, "RTPrinter is not initialized. Please initialize the printer first.");
            return;
        }
        rtPrinter.setConnectListener(listener);
    }

    // Exposed to allow the facade to set the transport interface (e.g. BLE)
    public void setPrinterInterface(PrinterInterface<?> printerInterface) {
        if (rtPrinter == null) {
            Log.e(TAG, "RTPrinter is not initialized. Please initialize the printer first.");
            return;
        }
        rtPrinter.setPrinterInterface(printerInterface);
    }

    @Override
    public void selfTest() {
        if (getRtPrinterConnectState() != ConnectStateEnum.Connected) {
            throw new RuntimeException(ExceptionMessageUtils.getConnectExceptionMsg(BaseEnum.CON_BLUETOOTH_BLE));
        }
        if (printerCmd == null) {
            throw new RuntimeException("Printer command set is not initialized.");
        }
        try {
            byte[] selfTestCmd = printerCmd.getSelfTestCmd();
            rtPrinter.writeMsg(selfTestCmd);
        } catch (Exception e) {
            throw new RuntimeException("Failed to run selfTest: " + e.getMessage());
        }
    }

    @Override
    public void printText(String text) {
        Log.i(TAG, "Printing text: " + text);
    }

    @Override
    public void printPdf(Context context, String pdfFilePath) {
        if (getRtPrinterConnectState() != ConnectStateEnum.Connected) {
            throw new RuntimeException(ExceptionMessageUtils.getConnectExceptionMsg(BaseEnum.CON_BLUETOOTH_BLE));
        }

        ParcelFileDescriptor descriptor = null;
        PdfRenderer renderer = null;
        try {
            File file = new File(pdfFilePath);
            if (!file.exists()) {
                Log.e(TAG, "PDF file does not exist: " + pdfFilePath);
                return;
            }
            descriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            renderer = new PdfRenderer(descriptor);
            if (renderer.getPageCount() <= 0) {
                Log.e(TAG, "PDF has no pages: " + pdfFilePath);
                return;
            }
            PdfRenderer.Page page = renderer.openPage(0);
            int pageWidth = page.getWidth();
            int pageHeight = page.getHeight();
            int targetWidth = 576; // adjust as needed for target printer
            float scale = (float) targetWidth / (float) pageWidth;
            int targetHeight = (int) (pageHeight * scale);
            Bitmap bitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_PRINT);
            page.close();

            // Try invoking an RTPrinter bitmap print method if present
            try {
                Method m = rtPrinter.getClass().getMethod("printBitmap", Bitmap.class);
                m.invoke(rtPrinter, bitmap);
                Log.i(TAG, "Printed PDF via rtPrinter.printBitmap");
                return;
            } catch (NoSuchMethodException nsme) {
                Log.w(TAG, "rtPrinter.printBitmap not available, fallback not implemented");
            }

            Log.i(TAG, "Rendered PDF to bitmap (size " + bitmap.getWidth() + "x" + bitmap.getHeight() + ") but no direct print method available.");

        } catch (Exception e) {
            Log.e(TAG, "Failed to render/print PDF: " + e.getMessage());
        } finally {
            try {
                if (renderer != null) renderer.close();
            } catch (Exception ignored) {
            }
            try {
                if (descriptor != null) descriptor.close();
            } catch (Exception ignored) {
            }
        }
    }
}
