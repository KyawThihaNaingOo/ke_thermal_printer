package kth.chem.ke_thermal_printer.core.enums

enum class EventTypes(val value: String) {

    PRINTER_STATUS("printer_status"),
    PRINT_STATUS("print_status");
//    fun isPrinterEvent(): Boolean =
//        this == PRINTER_STATUS
}