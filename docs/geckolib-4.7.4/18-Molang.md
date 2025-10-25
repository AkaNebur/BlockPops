# Molang (Geckolib4) Documentation

## Overview

Molang is an expression-based language enabling dynamic animations through mathematical expressions. Rather than static keyframe values, developers write expressions that evaluate at runtime.

## Core Concept

"Molang allows you to type directly write mathematical expressions into your animation keyframe" enabling creation of "dynamic animations with minimal effort." For example, `5 * 4` evaluates to 20, while `math.cos(query.anim_time)` creates smooth oscillating motion.

## Operators

Molang supports standard mathematical and logical operators:

| Operator | Purpose |
|----------|---------|
| `+`, `-`, `*`, `/`, `%` | Arithmetic operations |
| `^` | Exponentiation |
| `&&`, `\|\|`, `!` | Logical operations |
| `<`, `<=`, `>`, `>=`, `==`, `!=` | Comparisons |
| `=` | Variable assignment |
| `? :` | Conditional ternary |
| `()` | Grouping expressions |
| `;` | Separates compound expressions |

## Mathematical Functions

Core math functions include:

- **Trigonometric**: `math.sin`, `math.cos`, `math.asin`, `math.acos`, `math.atan`, `math.atan2`
- **Rounding**: `math.floor`, `math.ceil`, `math.round`, `math.trunc`
- **Utilities**: `math.abs`, `math.min`, `math.max`, `math.clamp`, `math.lerp`, `math.sqrt`, `math.pow`
- **Advanced**: `math.hermite_blend`, `math.die_roll`, `math.random`

## Query System

Queries retrieve runtime game values as doubles. Examples include:

- `query.anim_time` — Animation duration in seconds
- `query.ground_speed` — Entity lateral velocity
- `query.body_x_rotation`, `query.body_y_rotation` — Entity rotation angles
- `query.health`, `query.max_health` — Health values
- `query.is_moving`, `query.is_on_ground` — State checks
- `query.time_of_day`, `query.time_stamp` — Temporal data

Queries apply selectively; incompatible usage returns 0 and logs errors.

## Custom Extensions

**Custom Functions** register via `MathParser#registerFunction` in mod constructors, enabling specialized calculations.

**Custom Queries** require:
1. Registration through `MolangQueries#setActorVariable` (single-type) or `MathParser#registerVariable` (multi-type)
2. Value provision by overriding `GeoModel#applyMolangQueries` using `MathParser#setVariable`

Naming convention recommends prefixing with mod ID: `query.modid_variable_name`

## Advanced Features

**Compound Expressions** chain multiple operations using `;` separator, executing sequentially with the final expression's value returned:

```
v.is_animating = 1; math.sin(query.anim_time)
```

**Variables** store arbitrary data globally within expressions for multi-step calculations, though caution applies since they're shared across all animatables.

## Additional Resources

For comprehensive reference material, consult Microsoft's official "Molang Introduction" documentation (noting GeckoLib's implementation differs slightly in query arguments and available functions).
