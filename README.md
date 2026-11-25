# citafy
Sistema de gestión de citas multinegocio, proyecto final de Programación 1.

## Cómo crear una vista nueva

Toda página usa el layout común. Copia esta plantilla y cambia el título y el contenido:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
      th:replace="~{fragments/layout :: layout(~{::title}, ~{::section})}">
<head>
    <title>Clientes · Citafy</title>
</head>
<body>
    <section>
        <!-- contenido propio de la página, para cuando vallan a trabajar en sus correspondientes HU05, 07, 08 etc...-->
    </section>
</body>
</html>
```

Reglas: el archivo va en `src/main/resources/templates/<modulo>/`, el contenido siempre va dentro de `<section>`, y el título dentro de `<title>`. Bootstrap ya está cargado en el layout.