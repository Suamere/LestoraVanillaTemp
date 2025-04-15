# Lestora Vanilla Temperature

This is a client-side helper mod that adds vanilla temperature calculation to a location (player).  It mainly takes into account the biome with some opinionated temperatures.  It also "rubberbands" such that your temperature changes toward new environment changes at different speeds based on how dramatic the change is.  It will be colder in snow, hotter near lava (no matter where you are), holding buckets of snow/lava, and more.

This mod also allows you to optionally depend on Lestora Config, which will create a `config/lestora-biome.toml` with default values, where you can add or update values.

Also, this optionally depends on Lestora Wetness, because the temperature may be affected by wetness, which is more complex than "in water" or "not in water".

Lastly, this mod optionally depends on Lestora Debug, which will show the temperature at the current location in an F3 menu.

## Features
- **Usage:** Calculates a rubber-band affected swing in temperature based on the environment.
- **Configuration:** Use the dependency on Lestora Config's lestora-biome.toml, used for configuring biome temperatures.

## Manual Installation
1. Download the mod JAR from CurseForge.
2. Place the JAR file into your `mods` folder.
3. Launch Minecraft with the Forge profile.

## Commands
- Use the command `/lestora vanillaTemp current` - Outputs the current player's body temperature.  Useful if Debug is not installed, or disabled.

## Compatibility
- **Minecraft Version:** 1.21.4
- **Forge Version:** 54.1.0

## Troubleshooting
If you run into issues (e.g., crashes or unexpected behavior), check the logs in your `crash-reports` or `logs` folder. You can also open an issue on the mod’s GitHub repository.

## Contributing
Contributions are welcome! Please submit pull requests or open issues if you have suggestions or bug reports.

## License
This project is licensed under the MIT License.
