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
  final KEThermalPrinterUtils _kePrinter = KEThermalPrinterUtils();

  @override
  void initState() {
    super.initState();
    _initBluetooth();
    _kePrinter.initialize(CmdTypes.esc).then((value) {
      log('_kePrinter init --> $value');

      /// init listener
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(title: const Text('Plugin example app')),
        body: StreamBuilder(
          stream: _kePrinter.startScanBluetoothDevices(),
          builder: (context, asyncSnapshot) {
            if (!asyncSnapshot.hasData) {
              return const Center(child: CircularProgressIndicator());
            }
            if (asyncSnapshot.hasError) {
              return Center(child: Text('Error: ${asyncSnapshot.error}'));
            }
            if (asyncSnapshot.data == null || asyncSnapshot.data!.isEmpty) {
              return const Center(child: Text('No devices found'));
            }
            final scanResults = asyncSnapshot.data!;
            return ListView.builder(
              itemCount: scanResults.length,
              itemBuilder: (context, index) {
                final scanResult = scanResults[index];
                return ListTile(
                  onTap: () async {
                    final res = await _kePrinter.connectBluetoothDevice(
                      scanResult.device.remoteId.str,
                    );

                    log('Connection result: $res');

                    _kePrinter.listenEventChannel().listen((event) {
                      log('Event channel -> $event');
                    });

                    _kePrinter.listenPrinterStatus().listen((event) {
                      log('Printer status -> $event');
                    });
                  },
                  title: Text(
                    'Device: ${scanResult.device.remoteId.str} || RSSI: ${scanResult.rssi}',
                  ),
                );
              },
            );
          },
        ),
      ),
    );
  }

  Future<void> _initBluetooth() async {
    _kePrinter.startScanBluetoothDevices().listen((event) {
      log('Scan result event received with ${event.length} devices.');
      for (BLEScanResult r in event) {
        log(r.advertisementData.manufacturerData.toString());
        r.bleDevice.connectionState.listen((event) {
          // event.isConnected;
        });
        log(
          '${r.device.remoteId.str} || ${r.device.platformName} found! rssi: ${r.rssi}',
        );
      }
    });
  }

  void _initListener() {
    _kePrinter.listenEventChannel().listen((event) {
      log('Event channel -> $event');
    });
    _kePrinter.listenPrinterStatus().listen((event) {
      log('Printer status -> $event');
    });
    _kePrinter.listenPrintStatus().listen((event) {
      log('Print status -> $event');
    });
  }
}
