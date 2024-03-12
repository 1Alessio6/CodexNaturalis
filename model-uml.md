``` mermaid
---
title: Model
---
classDiagram
    class Card {
        - front
    }


    class Front
    class Back
        Back: - int Score
        Back: - Color color

    class Color{
        <<enumeration>>
        RED,
        BLUE,
        YELLOW,
        GREEN,
        PURPLE
    }
```
