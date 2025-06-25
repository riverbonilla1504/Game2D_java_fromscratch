# 🎮 LibGDX Setup Guide

Este proyecto ahora incluye **LibGDX**, **Box2D**, **OpenAL** y **ImGui** para mejorar el rendimiento y escalabilidad.

## 📦 Librerías Agregadas

### **LibGDX Core**
- **gdx**: Librería principal de LibGDX
- **gdx-backend-lwjgl3**: Backend para desktop (Windows/Mac/Linux)
- **gdx-platform**: Nativos para desktop

### **Box2D Physics**
- **gdx-box2d**: Motor de física 2D
- **gdx-box2d-platform**: Nativos de Box2D

### **Audio (OpenAL)**
- **Incluido automáticamente** a través de LWJGL3 backend
- Soporte para música y efectos de sonido

### **ImGui**
- **imgui-java-binding**: Bindings de Java para ImGui
- **imgui-java-lwjgl3**: Backend para LWJGL3
- **imgui-java-binding-lwjgl3**: Bindings específicos para LWJGL3

## 🚀 Cómo Ejecutar

### **1. Compilar el proyecto**
```bash
mvn clean compile
```

### **2. Ejecutar el ejemplo LibGDX**
```bash
mvn exec:java -Dexec.mainClass="com.game.libgdx.LibGDXLauncher"
```

### **3. Ejecutar el juego original (Swing)**
```bash
mvn exec:java -Dexec.mainClass="com.game.App"
```

## 🎯 Ventajas de LibGDX vs Swing

| Característica | Swing (Actual) | LibGDX (Nuevo) |
|----------------|----------------|----------------|
| **Rendimiento** | CPU rendering | GPU rendering (OpenGL) |
| **FPS** | ~60 FPS limitado | 60+ FPS constante |
| **Escalabilidad** | Limitada | Excelente |
| **Física** | Manual | Box2D integrado |
| **Audio** | Java Sound | OpenAL (mejor calidad) |
| **Debug UI** | Swing dialogs | ImGui (más rápido) |
| **Cross-platform** | Sí | Sí (mejor) |

## 🔧 Migración Gradual

### **Fase 1: Rendimiento Gráfico**
```java
// Antes (Swing)
Graphics2D g2 = (Graphics2D) g;
g2.drawImage(sprite, x, y, width, height, null);

// Después (LibGDX)
spriteBatch.begin();
spriteBatch.draw(texture, x, y, width, height);
spriteBatch.end();
```

### **Fase 2: Física con Box2D**
```java
// Antes (colisiones manuales)
if (player.getBounds().intersects(enemy.getBounds())) {
    // Handle collision
}

// Después (Box2D)
world.step(1/60f, 6, 2);
// Las colisiones se manejan automáticamente
```

### **Fase 3: Audio con OpenAL**
```java
// Antes (Java Sound)
// Código complejo para audio

// Después (LibGDX OpenAL)
Music music = Gdx.audio.newMusic(Gdx.files.internal("audio/background.mp3"));
music.play();
```

### **Fase 4: Debug UI con ImGui**
```java
// Antes (Swing dialogs)
JOptionPane.showMessageDialog(null, "Debug info");

// Después (ImGui)
ImGui.begin("Debug");
ImGui.text("FPS: " + fps);
ImGui.text("Player Position: " + playerX + ", " + playerY);
ImGui.end();
```

## 📁 Estructura de Archivos

```
src/main/java/com/game/
├── libgdx/                    # Nuevas clases LibGDX
│   ├── LibGDXExample.java     # Ejemplo completo
│   └── LibGDXLauncher.java    # Launcher
├── main/                      # Código original (Swing)
├── entity/                    # Entidades (compatible)
├── tile/                      # Tiles (compatible)
└── object/                    # Objetos (compatible)
```

## 🎮 Controles del Ejemplo

- **Mouse**: Interactuar con ImGui
- **F1**: Toggle demo window
- **F2**: Toggle physics debug
- **ESC**: Salir

## 🔍 Debug Features

### **ImGui Debug Window**
- **FPS Counter**: Rendimiento en tiempo real
- **Game Time**: Tiempo transcurrido
- **Physics Debug**: Visualizar cuerpos físicos
- **Audio Controls**: Reproducir/pausar audio
- **Demo Window**: Ejemplos de ImGui

### **Box2D Debug Renderer**
- **Cuerpos estáticos**: Verde
- **Cuerpos dinámicos**: Azul
- **Joints**: Líneas blancas
- **Velocidades**: Flechas

## 📚 Recursos Adicionales

### **Documentación Oficial**
- [LibGDX Wiki](https://libgdx.com/wiki/)
- [Box2D Manual](https://box2d.org/documentation/)
- [ImGui Documentation](https://github.com/ocornut/imgui)

### **Tutoriales Recomendados**
- [LibGDX Getting Started](https://libgdx.com/wiki/start/a-simple-game)
- [Box2D Tutorial](https://libgdx.com/wiki/extensions/physics/box2d)
- [ImGui Java Examples](https://github.com/spair/imgui-java)

### **Comunidad**
- [LibGDX Discord](https://discord.gg/libgdx)
- [Stack Overflow - libgdx](https://stackoverflow.com/questions/tagged/libgdx)
- [Reddit r/libgdx](https://www.reddit.com/r/libgdx/)

## 🚀 Próximos Pasos

1. **Migrar el sistema de rendering** de Swing a LibGDX
2. **Implementar Box2D** para colisiones automáticas
3. **Agregar audio** con OpenAL
4. **Crear debug UI** con ImGui
5. **Optimizar rendimiento** con GPU rendering

## ⚠️ Notas Importantes

- **Compatibilidad**: El código original sigue funcionando
- **Migración gradual**: Puedes migrar por partes
- **Recursos**: Los sprites y assets son compatibles
- **Performance**: LibGDX es significativamente más rápido
- **Debug**: ImGui es mucho más rápido que Swing dialogs 