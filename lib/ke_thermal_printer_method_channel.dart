import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'ke_thermal_printer_platform_interface.dart';

/// An implementation of [KeThermalPrinterPlatform] that uses method channels.
class MethodChannelKeThermalPrinter extends KeThermalPrinterPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('ke_thermal_printer');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
