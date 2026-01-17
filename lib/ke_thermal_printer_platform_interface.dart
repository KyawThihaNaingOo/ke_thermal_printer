import 'package:flutter_blue_plus/flutter_blue_plus.dart';
import 'package:ke_thermal_printer/enums/cmd_types.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'ke_thermal_printer_method_channel.dart';

abstract class KeThermalPrinterPlatform extends PlatformInterface {
  /// Constructs a KeThermalPrinterPlatform.
  KeThermalPrinterPlatform() : super(token: _token);

  static final Object _token = Object();

  static KeThermalPrinterPlatform _instance = MethodChannelKeThermalPrinter();

  /// The default instance of [KeThermalPrinterPlatform] to use.
  ///
  /// Defaults to [MethodChannelKeThermalPrinter].
  static KeThermalPrinterPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [KeThermalPrinterPlatform] when
  /// they register themselves.
  static set instance(KeThermalPrinterPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<void> initialize(CmdTypes cmdType) async {
    throw UnimplementedError('initialize() has not been implemented.');
  }

  Stream<List<ScanResult>> startScanBluetoothDevices({
    Duration timeout = const Duration(seconds: 5),
  }) {
    throw UnimplementedError(
      'startScanBluetoothDevices() has not been implemented.',
    );
  }

  Future<Map<String, dynamic>?> connectBluetoothDevice(String printerID) {
    throw UnimplementedError(
      'connectBluetoothDevice() has not been implemented.',
    );
  }

  Future<Map<String, dynamic>?> selfTestPrinter() {
    throw UnimplementedError('selfTestPrinter() has not been implemented.');
  }

  Future<Map<String, dynamic>?> getPrinterStatus() {
    throw UnimplementedError('getPrinterStatus() has not been implemented.');
  }
}
