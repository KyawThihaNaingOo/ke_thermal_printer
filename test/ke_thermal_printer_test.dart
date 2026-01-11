// import 'package:flutter_test/flutter_test.dart';
// import 'package:ke_thermal_printer/ke_thermal_printer.dart';
// import 'package:ke_thermal_printer/ke_thermal_printer_platform_interface.dart';
// import 'package:ke_thermal_printer/ke_thermal_printer_method_channel.dart';
// import 'package:plugin_platform_interface/plugin_platform_interface.dart';
//
// class MockKeThermalPrinterPlatform
//     with MockPlatformInterfaceMixin
//     implements KeThermalPrinterPlatform {
//   @override
//   Future<String?> getPlatformVersion() => Future.value('42');
//
//   @override
//   Future<bool> startScanBluetoothDevices() async {
//     return true;
//   }
// }
//
// void main() {
//   final KeThermalPrinterPlatform initialPlatform =
//       KeThermalPrinterPlatform.instance;
//
//   test('$MethodChannelKeThermalPrinter is the default instance', () {
//     expect(initialPlatform, isInstanceOf<MethodChannelKeThermalPrinter>());
//   });
//
//   test('getPlatformVersion', () async {
//     KeThermalPrinter keThermalPrinterPlugin = KeThermalPrinter();
//     MockKeThermalPrinterPlatform fakePlatform = MockKeThermalPrinterPlatform();
//     KeThermalPrinterPlatform.instance = fakePlatform;
//
//     expect(await keThermalPrinterPlugin.getPlatformVersion(), '42');
//   });
// }
