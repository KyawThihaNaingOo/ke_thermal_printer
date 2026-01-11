#ifndef FLUTTER_PLUGIN_KE_THERMAL_PRINTER_PLUGIN_H_
#define FLUTTER_PLUGIN_KE_THERMAL_PRINTER_PLUGIN_H_

#include <flutter/method_channel.h>
#include <flutter/plugin_registrar_windows.h>

#include <memory>

namespace ke_thermal_printer {

class KeThermalPrinterPlugin : public flutter::Plugin {
 public:
  static void RegisterWithRegistrar(flutter::PluginRegistrarWindows *registrar);

  KeThermalPrinterPlugin();

  virtual ~KeThermalPrinterPlugin();

  // Disallow copy and assign.
  KeThermalPrinterPlugin(const KeThermalPrinterPlugin&) = delete;
  KeThermalPrinterPlugin& operator=(const KeThermalPrinterPlugin&) = delete;

  // Called when a method is called on this plugin's channel from Dart.
  void HandleMethodCall(
      const flutter::MethodCall<flutter::EncodableValue> &method_call,
      std::unique_ptr<flutter::MethodResult<flutter::EncodableValue>> result);
};

}  // namespace ke_thermal_printer

#endif  // FLUTTER_PLUGIN_KE_THERMAL_PRINTER_PLUGIN_H_
