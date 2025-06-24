# 🎮 Game2D Java from Scratch

Un juego 2D desarrollado en Java desde cero con generación procedural de dungeons usando **BSP (Binary Space Partitioning)**.

## 🎮 Características

- **Motor de juego 2D** desarrollado desde cero
- **Generación BSP de dungeons** - Algoritmo clásico y confiable
- **Sistema de cámara inteligente** - Mapas más grandes que la pantalla
- **Sistema de colisiones** robusto y optimizado
- **Animaciones de sprites** fluidas
- **Gestión eficiente de recursos** con ResourceManager
- **Logging de mapas** para análisis y debugging
- **Interfaz de usuario** con menú principal

## 🎯 Controles

| Tecla | Acción |
|-------|--------|
| `W` | Mover hacia arriba |
| `A` | Mover hacia la izquierda |
| `S` | Mover hacia abajo |
| `D` | Mover hacia la derecha |
| `Enter` | Iniciar juego desde el menú |
| `R` | Regenerar mapa BSP |
| `ESC` | Salir del juego |

## 🏗️ Generación BSP de Dungeons

### ¿Qué es BSP?
**Binary Space Partitioning (BSP)** es un algoritmo clásico para generación de dungeons que:

1. **Divide recursivamente** el espacio en regiones más pequeñas
2. **Crea habitaciones** en los nodos hoja del árbol
3. **Conecta automáticamente** las habitaciones con pasillos
4. **Garantiza 100% de conectividad** - sin áreas aisladas

### Proceso de Generación

#### 1. División del Espacio
```
Mapa inicial (64x48)
    ↓
División horizontal/vertical aleatoria
    ↓
Dos sub-regiones
    ↓
Repetir hasta que las regiones sean del tamaño de habitaciones
```

#### 2. Creación de Habitaciones
- Cada nodo hoja del árbol BSP contiene una habitación
- Tamaño: 6x6 a 12x12 tiles
- Posición: Centrada en la región con variación aleatoria
- Espaciado: 2 tiles mínimo entre habitaciones

#### 3. Conexión con Pasillos
- **Pasillos en L**: Conectan centros de habitaciones hermanas
- **Conexión recursiva**: Desde hojas hasta la raíz del árbol
- **Garantía de conectividad**: Todos los espacios son accesibles

### Parámetros del Algoritmo
```java
MIN_ROOM_SIZE = 6          // Tamaño mínimo de habitación
MAX_ROOM_SIZE = 12         // Tamaño máximo de habitación
MIN_REGION_SIZE = 10       // Tamaño mínimo antes de parar división
ROOM_PADDING = 2           // Espaciado entre habitaciones
MAX_RECURSION_DEPTH = 6    // Profundidad máxima de recursión
```

### Ventajas del BSP
- ✅ **Conectividad garantizada** - 100% de espacios accesibles
- ✅ **Habitaciones bien definidas** - Estructura clara
- ✅ **Pasillos organizados** - Conexiones lógicas
- ✅ **Rendimiento excelente** - 4-6ms de generación
- ✅ **Consistencia** - Resultados predecibles y navegables

## 📷 Sistema de Cámara

### Características
- **Seguimiento automático** del jugador
- **Mapas más grandes** que la pantalla (64x48 vs 16x12)
- **Renderizado eficiente** - Solo dibuja tiles visibles
- **Transformación de coordenadas** mundo ↔ pantalla
- **Límites de mapa** - Cámara no sale del área del juego

### Funcionalidades
```java
// Seguimiento del jugador
camera.setTarget(player);
camera.update();

// Transformación de coordenadas
int screenX = camera.worldToScreenX(worldX);
int screenY = camera.worldToScreenY(worldY);

// Verificación de visibilidad
boolean visible = camera.isVisible(worldX, worldY, width, height);
```

## 🗂️ Estructura del Proyecto

```
src/
├── main/                    # Clases principales del juego
│   ├── main.java           # Punto de entrada
│   ├── GamePanel.java      # Panel principal con soporte de cámara
│   ├── GameConfig.java     # Configuración centralizada
│   ├── ResourceManager.java # Gestión de recursos
│   ├── BSPDungeonGenerator.java # Generador BSP de dungeons
│   ├── Camera.java         # Sistema de cámara
│   ├── MapLogger.java      # Sistema de logging de mapas
│   ├── KeyHandler.java     # Manejo de input
│   ├── CollisionChecker.java # Detección de colisiones optimizada
│   ├── GameState.java      # Estados del juego
│   ├── GameMenu.java       # Menú principal
│   └── AssetSetter.java    # Configuración de assets
├── entity/                  # Entidades del juego
│   ├── Entity.java         # Clase base para entidades
│   ├── Player.java         # Jugador
│   ├── Direction.java      # Enum de direcciones
│   ├── EntityFactory.java  # Factory para entidades
│   └── PlayerFactory.java  # Factory específica para jugador
├── tile/                    # Sistema de tiles
│   ├── Tile.java           # Clase de tile individual
│   ├── TileManager.java    # Gestión de tiles y mapas BSP
│   └── ...
└── object/                  # Objetos del juego
    ├── SuperObject.java    # Clase base para objetos
    ├── Star.java           # Objeto estrella
    └── ...
```

## 🚀 Compilación y Ejecución

### Compilar el proyecto
```bash
javac -cp "src" -d bin src/main/*.java src/entity/*.java src/object/*.java src/tile/*.java
```

### Ejecutar el juego
```bash
java -cp "bin" main.main
```

### Probar generación BSP
```bash
# Compilar test
javac -cp "src" -d bin src/main/BSPDungeonGenerator.java src/main/MapLogger.java test_bsp_generation.java

# Ejecutar test
java -cp "bin" test_bsp_generation
```

## 📊 Logging de Mapas

El sistema genera logs automáticamente en la carpeta `logs/`:

- **Archivos de datos**: Contienen el mapa como matriz numérica y estadísticas
- **Archivos visuales**: Representación ASCII del mapa para análisis visual
- **Información incluida**: Seed, dimensiones, número de habitaciones, accesibilidad

### Ejemplo de log visual
```
=== VISUAL MAP REPRESENTATION ===
Seed: 1750799621538
Legend: . = Floor, # = Wall

################################################################################
##############################################.........#########################
######...........##########............................#########################
######...........##########.......############.........#########################
######....................................########.#############################
###########.###############.......#######.########.#############################
###########.##################..#########.########.#############################
#######...........########..........#####.########.#..........##################
############.#############..........#####.########............##################
################################################################################
```

## 🔧 Configuración

### Tamaños de Mapa
```java
// En GameConfig.java
MAP_WIDTH = 64;    // Ancho del mapa (tiles)
MAP_HEIGHT = 48;   // Alto del mapa (tiles)
TILE_SIZE = 48;    // Tamaño de cada tile (píxeles)
```

### Parámetros BSP
```java
// En BSPDungeonGenerator.java
MIN_ROOM_SIZE = 6;           // Habitación mínima
MAX_ROOM_SIZE = 12;          // Habitación máxima
MIN_REGION_SIZE = 10;        // Región mínima antes de parar
MAX_RECURSION_DEPTH = 6;     // Profundidad máxima
```

## 📈 Resultados de Rendimiento

### Generación de Mapas
- **Tiempo promedio**: 4-6ms
- **Habitaciones generadas**: 2-12 (dependiendo del tamaño)
- **Conectividad**: 100% garantizada
- **Regiones conectadas**: Siempre 1 (sin áreas aisladas)

### Rendimiento del Juego
- **FPS objetivo**: 60 FPS
- **Renderizado**: Solo tiles visibles en pantalla
- **Memoria**: Optimizada con reutilización de objetos
- **Colisiones**: Detección eficiente con TileManager

## 🎯 Mejoras Implementadas

### Estructura del Código
- **ResourceManager**: Gestión centralizada de assets
- **GameConfig**: Constantes centralizadas
- **Enums**: Para estados y direcciones
- **ArrayList**: Para objetos dinámicos
- **Manejo de errores**: Validaciones y logging

### Generación de Mapas
- **Algoritmo BSP**: Clásico y confiable
- **Conectividad garantizada**: Sin áreas aisladas
- **Logging detallado**: Análisis y visualización
- **Parámetros ajustables**: Fácil configuración

### Rendimiento
- **Game loop optimizado**: 60 FPS constante
- **Carga eficiente**: Assets cargados una sola vez
- **Memoria optimizada**: Reutilización de objetos
- **Cámara inteligente**: Solo renderiza lo visible

## 🔮 Próximas Mejoras

- [ ] **Sistema de inventario**
- [ ] **Enemigos y combate**
- [ ] **Múltiples niveles**
- [ ] **Efectos de sonido**
- [ ] **Guardado de progreso**
- [ ] **Más tipos de habitaciones** (tesoros, jefes, etc.)
- [ ] **Decoraciones en habitaciones**
- [ ] **Sistema de puertas y llaves**
- [ ] **Generación de mazmorras multi-nivel**
- [ ] **Algoritmos de pathfinding**

## 🤝 Contribuir

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

---

**¡Disfruta explorando los dungeons generados proceduralmente con BSP!** 🗺️⚔️
