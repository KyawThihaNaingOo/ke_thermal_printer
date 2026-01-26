package kth.chem.ke_thermal_printer.core.domain;

import android.content.Context;

import com.rt.printerlibrary.enumerate.ConnectStateEnum;
import com.rt.printerlibrary.utils.ConnectListener;

/**
 * Java interface for printer operations. Contains a method to print PDFs.
 */
public interface PrinterService {
    void initialize(int cmdType);

    void setCmdType(int cmdType);

    void connect(Object config) throws Exception;

    void disconnect();

    ConnectStateEnum getConnectionState();

    void setConnectListener(ConnectListener listener);

    void selfTest();

    void printText(String text);

    /**
     * Print a PDF file located at the given file path. Implementations may render pages
     * and send printable bytes to the printer. Context is provided for PdfRenderer or file access.
     *
     * @param context     Android context
     * @param pdfFilePath absolute path to the PDF file
     */
    void printPdf(Context context, String pdfFilePath);
}

