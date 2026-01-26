import 'package:flutter_blue_plus/flutter_blue_plus.dart';
import 'package:ke_thermal_printer/ke_thermal_printer.dart';

class KEThermalPrinterUtils {
  final KeThermalPrinterPlatform _printerPlatform =
      KeThermalPrinterPlatform.instance;

  Future<Map<String, dynamic>?> initialize(
    CmdTypes cmdType,
    ConnectTypes connectionType,
  ) async {
    return KeThermalPrinterPlatform.instance.initialize(
      cmdType,
      connectionType,
    );
  }

  // ################### BLE Methods ###################
  Future<bool> get isSupportBLE async {
    return FlutterBluePlus.isSupported;
  }

  Future<void> turnOnBluetooth() async {
    final isSupport = await isSupportBLE;
    if (!isSupport) {
      throw BLENotSupportException();
    }
    return FlutterBluePlus.turnOn();
  }

  void stopScanBluetoothDevices() {
    FlutterBluePlus.stopScan();
  }

  Stream<List<BLEScanResult>> startScanBluetoothDevices() {
    return KeThermalPrinterPlatform.instance.startScanBluetoothDevices().map((
      event,
    ) {
      return event
          .map(
            (e) => BLEScanResult(
              device: e.device,
              advertisementData: e.advertisementData,
              rssi: e.rssi,
              timeStamp: e.timeStamp,
            ),
          )
          .toSet()
          .toList();
    });
  }

  Future<Map<String, dynamic>?> connectBluetoothDevice(String printerID) {
    return _printerPlatform.connectBluetoothDevice(printerID);
  }

  Future<Map<String, dynamic>?> getPrinterStatus() {
    return _printerPlatform.getPrinterStatus();
  }

  Future<Map<String, dynamic>?> selfTestPrinter() {
    return _printerPlatform.selfTestPrinter();
  }

  Stream<Map<String, dynamic>?> listenEventChannel() {
    return _printerPlatform.listenEventChannel();
  }

  Stream<Map<String, dynamic>?> listenPrintStatus() {
    return _printerPlatform.listenPrintStatus();
  }

  Stream<Map<String, dynamic>?> listenPrinterStatus() {
    return _printerPlatform.listenPrinterStatus();
  }
}

// ################### End of BLE Methods ###################
