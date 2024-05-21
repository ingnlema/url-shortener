

# URL Shortener Service

## Descripción

Este servicio de acortamiento de URLs permite transformar una URL larga en una versión más corta que es más fácil de compartir. La solución está diseñada para manejar una alta carga de tráfico y garantizar tiempos de respuesta rápidos, cumpliendo con los requisitos de rendimiento y escalabilidad necesarios para un entorno de producción moderno.

## Características

- **Acortamiento de URL**: Genera una URL corta a partir de una larga.
- **Redirección a URL Larga**: Redirecciona desde una URL corta a su URL larga original.
- **Estadísticas de Uso**: Proporciona datos estadísticos sobre el uso de las URLs acortadas.
- **Alto Rendimiento**: Acondicionada para poder escalar vertical y horizontalmente con facilidad.
- **Borrado de URL**: Permite eliminar URLs cortas que ya no son necesarias.
- **Respuesta Rápida**: Todas las operaciones estan optimizadas para reponder rápido y utilizar pocos recursos.



## Tecnologías Utilizadas

- **Java** con **Spring Boot** para el backend.
- **Redis** como almacenamiento de clave-valor para manejo de sesiones y caché.
- **Docker** y **Kubernetes** para contenerización y orquestación.
- **Prometheus** y **Grafana** para monitoreo y visualización de métricas.
- **Micrometer** para instrumentación.
- **Actuator** para recopilar métricas y sondear estado.
- **K6** para test de carga y análisis de tiempos de respuesta.
- **Docker** y **Docker compose** para despliegue y configuracion en entornos locales.
- **Caffeine** para implementación de cache.

## Alquitectura

Este proyecto utiliza una arquitectura moderna diseñada para ser escalable, eficiente y fácil de mantener. A continuación se detallan los principales componentes y cómo interactúan entre sí:

### Backend
El sistema está construido sobre Spring Boot, lo cual facilita la creación de microservicios robustos con capacidades de autoconfiguración y una amplia gama de funcionalidades integradas. La lógica del negocio se maneja a través de varios componentes:

- **Controllers:** Manejan las solicitudes HTTP, permitiendo a los usuarios crear, recuperar, borrar y obtener estadísticas de URLs cortas.
- **Services:** Encapsulan la lógica de negocio principal, incluyendo la generación de URLs cortas y la recuperación de URLs largas correspondientes.
- **Repositories:** Abstraen el acceso a la base de datos, en este caso, Redis, para el almacenamiento de la relación entre URLs cortas y largas.
- **Base de Datos:** Redis se utiliza como la base de datos principal debido a su rendimiento superior en operaciones de lectura/escritura, lo que es esencial para cumplir con los requisitos de alta velocidad de respuesta del servicio. Esto es particularmente importante para cumplir con el objetivo de que el 90% de las solicitudes se completen en menos de 10 ms.

### Caché
Se implementa una caché en memoria para reducir la latencia y las cargas de la base de datos, mejorando el tiempo de respuesta para las URLs frecuentemente accedidas y las estadísticas.

### Contenerización y Orquestación
Docker y Docker Compose son utilizados para contenerizar el servicio y sus dependencias, asegurando la consistencia entre diferentes entornos de desarrollo y producción.
Kubernetes se emplea para la orquestación de contenedores en producción, facilitando el escalado automático, la gestión de fallos y el despliegue sin interrupciones.
### Monitoreo
Prometheus recoge métricas continuas del sistema y de la aplicación, lo que permite un seguimiento detallado del rendimiento y la salud del servicio.
Grafana se utiliza para visualizar estas métricas, ofreciendo dashboards configurables que facilitan la identificación rápida de cualquier problema o cuello de botella.
### CI/CD
GitHub Actions automatiza la integración continua y la entrega continua, permitiendo pruebas automatizadas y despliegues regulares a medida que el código evoluciona.
Documentación de la API
OpenAPI (Swagger) proporciona documentación interactiva para la API, facilitando a los desarrolladores la comprensión y prueba de las funcionalidades del servicio.
### Seguridad
En este MVP algunos aspectos de seguridad que sos fundamentales para cualquier aplicación en producción quedaron fuera de alcance, dado que el projecto fue parte de un reto tecnico con una semana de limite de tiempo, en la cual se priorizo buscar un equilibro entre desarrollo y devops.

Esto no quita que sea fundamental la implentación de mecanismos de autenticación y autorización (ej: JWT), encriptación de datos sensibles y configuraciónes de red necesarias, las cuales al momento de integrar este sistema deberan ser analizadas e implementadas.

Cabe destacar que al ser un projecto con spring-boot muchas de estas implementaciónes no tendrán una gran carga de implementación y configuración gracias a las ventajas de este framework. 

## Estructura del Proyecto

```
/src
  /main
    /java
      /com.mercadolibre.urlshortener
        /controller
        /model
        /service
        /configuration
  /test
    /java
      /com.mercadolibre.urlshortener
/docker
/docker-compose
/k6
/kubernetes
/logs
```

## Instalación y Despliegue

### Requisitos Previos

- Java 17
- Maven
- Docker
- Kubernetes

### Instrucciones de Instalación

1. **Clonar el repositorio**:

   ```bash
   git clone https://github.com/ingnlema/url-shortener
   ```

2. **Construir el proyecto**:

   ```bash
   mvn clean install
   ```

3. **Construir y desplegar con Docker**:

   ```bash
   docker-compose up --build
   ```

4. **Despliegue en Kubernetes**:

   ```bash
   cd kubernetes
   kubectl apply -f redis-secret.yml
   kubectl apply -f persistent-volume.yml
   kubectl apply -f redis.yml
   kubectl apply -f prometheus.yml
   kubectl apply -f grafana.yml
   kubectl apply -f deployment.yml
   ```

## Uso

### Crear una URL corta

`POST /api/url/shorten`

**Body**:
```json
{
  "url": "http://example.com"
}
```

**Respuesta**:
```json
{
  "shortUrl": "2q"
}
```

### Obtener URL larga

`GET /api/url/{shortUrl}`

**Respuesta**:
```json
{
  "url": "http://example.com"
}
```

## Monitoreo

Prometheus y Grafana están configurados para monitorear métricas vitales como uso de CPU, memoria y tiempos de respuesta. Accede a Grafana a través de `http://localhost:3000`.

## Problemas Conocidos y Soluciones

En esta sección, se documentan los problemas identificados durante el desarrollo y despliegue del proyecto, junto con las soluciones aplicadas o las sugerencias de mejora.
### Problema 1: Única instacia de BD redis.

**Descripción**: El uso de una única instancia de Redis representa un punto único de fallo en la arquitectura del sistema. En caso de que esta instancia falle o se vuelva inaccesible, todo el servicio de acortamiento de URLs podría dejar de funcionar, lo que resulta en la pérdida de acceso a datos críticos y en una disminución significativa de la disponibilidad del servicio.

**Posible Solución**: 
Configurar Redis como un clúster para proporcionar mayor disponibilidad y tolerancia a fallos. Un clúster de Redis ofrece varias ventajas sobre una única instancia, incluyendo:

Alta Disponibilidad: Al distribuir los datos entre varios nodos, el clúster puede continuar operando incluso si uno o más nodos fallan. Esto es crucial para mantener el servicio activo y accesible en todo momento.

Tolerancia a Fallos: Con la replicación de datos entre los nodos, un clúster de Redis puede manejar fallos de nodos sin pérdida de datos. Los nodos secundarios pueden tomar automáticamente el relevo si el nodo principal falla.

Escalabilidad: Un clúster permite escalar la capacidad de almacenamiento y el rendimiento más allá de las limitaciones de una única máquina, distribuyendo la carga entre varios nodos.
Particionamiento Automático de Datos: Redis Cluster automáticamente particiona los datos entre los nodos, lo que mejora el rendimiento y optimiza el uso de recursos.

**Estado**: Pendiente.

### Problema 2: Uso Ineficiente de Caffeine como Cache Distribuido

**Descripción**: Caffeine se utiliza en el proyecto como sistema de caché para mejorar el rendimiento del servicio de acortamiento de URLs. Sin embargo, Caffeine es una caché local en memoria, lo cual presenta limitaciones en un entorno distribuido donde múltiples instancias de la aplicación necesitan acceder y modificar la caché de manera coherente. La falta de sincronización entre las instancias puede llevar a inconsistencias de datos y, en última instancia, a una experiencia de usuario degradada.

**Posible Solución**: Migrar de una caché local en memoria a un sistema de caché distribuido que pueda manejar adecuadamente la coherencia y la replicación entre múltiples nodos. Redis, configurado como caché distribuido, es una excelente alternativa, ya que ofrece mecanismos integrados para manejar la coherencia de datos en un entorno distribuido.

**Estado**: Pendiente.

### Problema 3: Límites de Solicitudes y Retintos Inadecuados

**Descripción**: En el entorno actual, el servicio de acortamiento de URLs no implementa una gestión efectiva de límites de solicitudes (rate limiting) ni mecanismos de reintento. Esto puede llevar a problemas durante picos de tráfico o en caso de fallos temporales, potencialmente sobrecargando el servicio o la base de datos subyacente y degradando la experiencia del usuario.

**Posible Solución**: Implementar un sistema de control de límites de solicitudes junto con estrategias de reintento inteligentes para gestionar mejor la carga en el servicio y mejorar la resiliencia del sistema.

**Estado**: Pendiente.

### Problema 4: Integración de Métricas de Conteo y Timing al Monitoreo. 

**Descripción**: Actualmente, el sistema carece de una integración completa de métricas de conteo y temporización dentro del sistema de monitoreo. Esto limita la capacidad de los operadores y desarrolladores para obtener insights detallados sobre el rendimiento operativo del servicio, como la frecuencia de uso de ciertas URLs y los tiempos de respuesta para las operaciones críticas.

**Posible Solución**: Para asegurar una integración completa y efectiva de métricas de conteo y temporización en el sistema de monitoreo, se propone revisar la configuración actual y las dependencias, así como expandir la implementación para incluir nuevas métricas que proporcionen una visión más detallada del rendimiento del servicio

**Estado**: Pendiente.



