#include "include/ke_thermal_printer/ke_thermal_printer_plugin_c_api.h"

#include <flutter/plugin_registrar_windows.h>

#include "ke_thermal_printer_plugin.h"

void KeThermalPrinterPluginCApiRegisterWithRegistrar(
    FlutterDesktopPluginRegistrarRef registrar) {
  ke_thermal_printer::KeThermalPrinterPlugin::RegisterWithRegistrar(
      flutter::PluginRegistrarManager::GetInstance()
          ->GetRegistrar<flutter::PluginRegistrarWindows>(registrar));
}
