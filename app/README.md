# Chef Alarm
#### Video Demo:  <https://www.youtube.com/watch?v=7weflV3ISpU>
#### Description:

Chef Alarm es una aplicación movil para Android que permite programar alarmas para que el usuario sepa cuando cierto alimento ya está cocinado. Para conocer ese tiempo, se han incluido varios factores que, luego de realizar operaciones matemáticas con cada factor, se llega a un tiempo estimado.
Para el proyecto se ha trabajado solo con 3 alimentos: Papa, Arroz blanco y Espaguetis. El motivo de esto es para lograr que la aplicación funcione y que no escale a un problema mucho más complejo.
La app se desarrolló en Kotlin y se utilizó el framework Jetpack Compose y Room. Se eligió Kotlin junto con Jetpack Compose y Room por ser las opciones más populares en el desarrollo de apps Android. Además, cuentan con una gran comunidad y numerosos recursos disponibles para consulta y aprendizaje.

## Archivos del proyecto:
- `MainActivity.kt`: Contiene una visión global de la aplicación. Acá se ve que habrá 2 páginas ("Page1" y "Page2"), además de establecer el encabezado superior que se muestra en ambas páginas. También incluye la función que interactúa con el reloj de Android y establece las alarmas.

### pages
#### page1
- `Page1.kt`: Contiene todos los elementos que se muestran en la primera página.
- `Page1ViewModel.kt`: Contiene el modelo de vista el cual hace la solicitud a la base de datos sobre los alimentos guardados y luego pasarlos al archivo 'Page1.kt' para que los muestre

### page2
- `MyRow.kt`: Debido a que hay varias filas que se crean, se optó por aislar toda la función en un archivo aparte.
- `Page2.kt`: Contiene todos los elementos que se muestran en la segunda página.
- `Page2ViewModel.kt`: Contiene el modelo de vista que se encarga de realizar la lógica según las opciones que va escogiendo el usuario en "Page2". Una de sus principales funciones es entregarle a Page2 el tiempo estimado de cocción para que lo muestre en la UI.

### database
En esta carpeta se incluyen todos los archivos relacionado a la base de datos. Para este proceso se requirió de ayuda de la IA.
- `AppDatabase.kt`: Archivo que se encarga de construir la base de datos.
- `SavedConfig.kt`: Contiene todas las columnas de la base de datos.
- `SavedConfigDao.kt`: Contiene todas las solicitudes de SQL que se hacen en la aplicación.

### ui.theme
- `Color.kt`: Acá se incluye la paleta de colores que se utilizó para el proyecto.
- `Type.kt`: Contiene la fuente de letra utilizada en el proyecto.

## Decisiones de diseño:
Al inicio se pensó en establecer una página más que muestre una barra de progreso para que el usuario pueda ver cómo va la cocción de su alimento. Sin embargo, se tuvo que descartar porque extendería demasiado el desarrollo de la app por estos problemas:
- Enlazar la barra de progreso con la aplicación de reloj de Android para que, si el usuario eliminaba la alarma, la barra de progreso dentro de Chef Alarm también se elimine.
- Establecer un botón dentro de Chef Alarm que permita eliminar la barra de progreso y, al mismo tiempo, deshacer la alarma.
Por otro lado, se optó por utilizar el patrón "Modelo-Vista-Modelo de Vista" ya que permitía poder cambiar entre pestañas y que la información ingresada por el usuario permanezca, evitando así que cada página se recomponga y el usuario tenga que ingresar los datos desde cero.
Otro motivo para escoger este patrón es que mejora la organización del código. 'Page1' y 'Page2' solo se encargan de la interfaz de usuario, mientras que sus respectivos ViewModel gestionan la lógica y las operaciones, promoviendo una clara separación de responsabilidades.

## ¿Cómo funciona?
Su uso es muy sencillo. Al abrir la aplicación, lo primero que se verá es la primera página ("Alimentos guardados"). Por defecto aparecerá vacío. 
Lo siguiente que deberá hacer el usuario es moverse hacia la segunda página. Allí verá una primera fila donde deberá elegir el alimento a cocinar. Por ahora solo hay 3 opciones: "Papa", "Arroz blanco" y "Espaguetis".
La elección de cada uno provocará que se muestren distintas filas. La más compleja será la de la papa, ya que hay varios factores en tener en cuenta para estimar su tiempo de cocción.
Una vez que el usuario haya ingresado toda la información requerida, la app mostrará un tiempo aproximado de cocción. En caso el usuario no esté 100% conforme con ese tiempo, podrá usar los botones "+1" y "-1" para aumentar o disminuir el tiempo.
Por último, al presionar en "Programar alarma", la aplicación interactuará con el reloj nativo de Android y establecerá una alarma.
En caso de que la configuración establecida sea una que se va a cocinar regularmente, se puede marcar la casilla "Recordar alimento", así se guarda en la base de datos y se muestra en la página "Alimentos guardados". De esa manera, la próxima vez que cocine ya no deberá ingresar todos los datos, solo presionar su alimento y la alarma se programará automáticamente.

## Utilidad
Esta app busca ser una ayuda a los usuarios en la cocina. Por lo general, al cocinar papas, espaguetis o arroz blanco, el usuario tiene que estar atento o revisando en cada momento si el alimento ya se cocinó.
Con esta app, el usuario solo necesita ingresar la información y programar la alarma. Luego, puede aprovechar el tiempo en otras tareas como limpiar platos, preparar ensaladas o atender otros procesos de la cocina, sin preocuparse por el tiempo de cocción.

## Créditos:
Alvaro Flores Lucano: Programación de la app
Marcia Urquizo Balboa: Diseño del logo e íconos