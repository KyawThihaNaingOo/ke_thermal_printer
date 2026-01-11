import 'dart:developer';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_blue_plus/flutter_blue_plus.dart';
import 'package:ke_thermal_printer/ke_thermal_printer.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String? bluetoothUUID;

  @override
  void initState() {
    super.initState();
    _initBluetooth();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(title: const Text('Plugin example app')),
        body: Center(child: Text('Running...')),
      ),
    );
  }

  Future<void> _initBluetooth() async {
    FlutterBluePlus.adapterState.listen((state) {
      if (state == BluetoothAdapterState.on) {
        FlutterBluePlus.startScan(timeout: const Duration(seconds: 5));
      }
    });
    FlutterBluePlus.scanResults.listen((results) {
      for (ScanResult r in results) {
        print('${r.device.platformName} found! rssi: ${r.rssi}');
        if (r.device.platformName == 'Mobile printer-385C') {
          FlutterBluePlus.stopScan();
          r.device.connect(license: License.free);
          bluetoothUUID = r.device.remoteId.str;
          log(bluetoothUUID!, name: 'Bluetooth UUID');
          // listenerDevice(r.device);
          break;
        }
      }
    });
  }

  // void listenerDevice(BluetoothDevice device) {
  //   device.state.listen((state) {
  //     print('Device ${device.platformName} is now in state $state');
  //     if (state == BluetoothConnectionState.connected) {
  //       print('Device ${device.platformName} is now in state $state');
  //     }
  //   });
  // }
}
