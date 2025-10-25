# Getting Started with Architectury API

## What is Architectury API?

Architectury API is an intermediary API designed to ease the development of multiplatform Minecraft mods. It provides abstractions over both Fabric and Forge APIs, allowing you to write code once that works on both platforms.

## Key Features

- **Over 90+ Event Hooks**: Comprehensive event system covering client and server-side events
- **Networking Abstraction**: Unified networking API for both Fabric and Forge
- **Registry Abstraction**: Platform-agnostic registry system
- **Platform Utilities**: Easy access to platform-specific information
- **@ExpectPlatform Annotation**: Write platform-specific code cleanly (part of Architectury Injectables)

## Your Project Configuration

- **Minecraft Version**: 1.20.1
- **Architectury API Version**: 9.2.14
- **Forge Version**: 1.20.1-47.4.9
- **Enabled Platforms**: Forge

## Project Structure

Architectury projects typically follow this structure:

```
project-root/
├── common/          - Platform-agnostic code
├── forge/           - Forge-specific implementations
├── fabric/          - Fabric-specific implementations (optional)
└── build.gradle     - Root build configuration
```

## Adding Architectury API to Your Project

### In gradle.properties

```properties
architectury_api_version = 9.2.14
```

### In common/build.gradle

```groovy
dependencies {
    modImplementation "dev.architectury:architectury:${rootProject.architectury_api_version}"
}
```

### In forge/build.gradle

```groovy
dependencies {
    modImplementation("dev.architectury:architectury-forge:${rootProject.architectury_api_version}")
}
```

## When to Use Architectury API

### Use Architectury API When:

- You need cross-platform compatibility between Fabric and Forge
- You want a cleaner, more unified API surface
- You need extensive event hooks without writing platform-specific code
- You're building a mod that should work on multiple loaders

### Architectury API is Optional

You can use the Architectury toolchain (Architectury Loom and Architectury Plugin) without the API. The API is just one component that provides convenience abstractions.

## Next Steps

1. **[Platform Abstraction](02-platform-abstraction.md)** - Learn about platform detection and utilities
2. **[Registry System](03-registry-system.md)** - Understand how to register game content
3. **[Networking](04-networking.md)** - Set up client-server communication
4. **[Events](05-events-overview.md)** - Hook into game events

## Additional Resources

- **GitHub Repository**: https://github.com/architectury/architectury-api
- **Discord**: https://discord.gg/C2RdJDpRBP
- **Example Mod**: https://github.com/architectury/architectury-example-mod
- **Mod Templates**: https://github.com/architectury/architectury-templates
- **Official Docs**: https://docs.architectury.dev/

## License

This documentation is based on the Architectury API source code (version 9.2.14), which is licensed under the GNU Lesser General Public License v3.0.

---

*Last Updated: 2025-10-25*
*For: BlockPops Project - Minecraft 1.20.1*
