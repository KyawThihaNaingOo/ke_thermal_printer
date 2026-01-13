import 'dart:async';
import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:ke_thermal_printer/ke_thermal_printer.dart';

/// k/Db3@j=7X@:$n)
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
  final KEThermalPrinter _kePrinter = KEThermalPrinter();

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
    _kePrinter.startScanBluetoothDevices().listen((event) {
      log('Scan result event received with ${event.length} devices.');
      for (BLEScanResult r in event) {
        log('${r.device.remoteId.str} || ${r.device.platformName} found! rssi: ${r.rssi}');
      }
    });
  }
}
