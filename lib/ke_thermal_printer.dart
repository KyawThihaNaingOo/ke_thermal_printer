
import 'ke_thermal_printer_platform_interface.dart';

class KeThermalPrinter {
  Future<String?> getPlatformVersion() {
    return KeThermalPrinterPlatform.instance.getPlatformVersion();
  }
}
