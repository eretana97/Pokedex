
# Hola, Edgar Retana les saluda! 👋
A continuacion encontraran la descripción a detalle de todo el proyecto PokedexApp

## Acerca de PokedexApp

La app te permite ver la lista completa de pokemons existentes, podras ver el nombre, numero de id, tipos elementales y los stats (Ataque, Defensa, Velocidad) de cada uno de los pokemons. Tambien podras realizar busqueda por nombre.


## Pantallas

- Splash Screen
- Home


## Caracteristicas Implementadas

- Patron de diseño MVVM
- Splash Screen (3s Delay)
- Animaciones con libreria Lottiefiles
- Libreria Material Desing 3
- Libreria Retrofit para peticiones HTTP
- Consumo de API [PokéAPI](https://pokeapi.co/)
- Buscador de pokemon por nombre
- Almacenamiento de datos en base local con libreria Room
- SearchBar con Material Desing 3
- Soporte de lenguaje Español (SV) / Ingles
- Libreria Glide para mostrar Imagenes
- Servicio en segundo plano
- Coo-rutinas para manejo de peticiones
- Notificaciones
- Solicitud de permisos de usuario
- 2 Pruebas unitarias con JUnit
- Excepciones y alertas para manejo de errores
- Uso de Flow para observar cambios en base de datos


## Peticiones HTTP
Lista de peticiones realizadas a la API

#### Obtener lista de pokemons

```http
  GET/pokemon
```

| Parametro | Tipo    | Descripción                |
| :-------- | :------- | :------------------------- |
| `limit` | `int` | Limite de resultados |
| `offset` | `int` | Desplazamiento de resultados |

#### Filtrar pokemon por nombre

```http
  GET pokemon/{name}
```

| Parametro | Tipo    | Descripción                |
| :-------- | :------- | :-------------------------------- |
| `name`      | `string` | Nombre del pokemon |


## Autor

Edgar Retana Github: [@eretana97](https://www.github.com/eretana97)
