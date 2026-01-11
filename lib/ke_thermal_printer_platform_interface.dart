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

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
