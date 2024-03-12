``` mermaid
---
title: Model
---
classDiagram
    class Card {
        - Front front
        - Back back
        - List~Corner~ cornerList
    }


    class Front {
        - int points
    }
    class Back {
        - int Score
        - Color color
        + getters()
        + setters()
    }

    class Color{
        <<enumeration>>
        RED,
        BLUE,
        YELLOW,
        GREEN,
        PURPLE
    }
```

---

### Todo

- [ ] add interfaces
- [ ] refactor methods in specialized classes
- [ ] check availability of specialized methods in base class
